package Disilon;

import java.util.Objects;

import static Disilon.Main.df2;
import static Disilon.Main.df4;
import static Disilon.Main.game_version;

public class ActiveSkill {
    public String name;
    public int lvl;
    public double min;
    public double max;
    public double hit;
    public double mp_mult;
    public double mp;
    public int hits;
    public double cast_mult;
    public double delay_mult;
    public double dmg_mult = 1;
    private double base_min;
    private double base_max;
    private double base_hit;
    public double base_mp;
    public double mp_additive;
    private double base_cast;
    private double base_delay;
    public SkillMod skillMod = SkillMod.Enemy;
    public Scaling scaling;
    public Element element;
    public boolean aoe;
    public boolean heal;
    public String debuff_name;
    public double debuff_duration;
    public double debuff_dmg;
    public double debuff_effect;
    public double base_debuff_duration;
    public double base_debuff_dmg;
    public String buff_name;
    public double buff_duration;
    public double buff_bonus;
    public double base_buff_duration;
    public double base_buff_bonus;
    public double cast;
    public double delay;
    public int used_in_rotation;
    public int used_debuffed;
    public double exp;
    public double hit_chance_sum;
    public double dmg_sum;
    public double debuff_chance_sum;
    public double mana_used;
    public int used;
    public int attacks_total;
    public int hits_total;
    public double old_lvl;
    public double last_casted_at = 0;
    public boolean random_targets = false;
    public boolean enabled = false;
    public int use_setting;
    public boolean overkill = true;
    public boolean can_kill = true;

    public ActiveSkill(String name) {
        this.name = name;
        this.lvl = 0;
    }

    public ActiveSkill(String name, int hits, double min, double max, double hit, double mp, double cast_mult, double delay_mult,
                       Scaling scaling, Element element, boolean aoe, boolean heal) {
        this.name = name;
        this.base_min = min;
        this.base_max = max;
        this.base_hit = hit;
        this.base_mp = mp;
        this.base_cast = cast_mult;
        this.base_delay = delay_mult;
        this.lvl = 0;
        this.hits = hits;
        this.mp_mult = 1;
        this.scaling = scaling;
        this.element = element;
        this.aoe = aoe;
        this.heal = heal;
        setSkill(lvl, SkillMod.Enemy);
    }

    public boolean shouldUse(Actor actor) {
        if (heal) {
            used_in_rotation++;
            return actor.getHp() / actor.getHp_max() * 100.0 < use_setting;
        } else {
            if (name.equals("Bless") && actor.blessed > 0) {
                return false;
            }
            if (name.equals("Charge Up") && actor.charge > 0) {
                return false;
            }
            if (name.equals("Empower HP") && actor.empower_hp > 0) {
                return false;
            }
            if (name.equals("Careful Shot") && actor.zone.getMaxEnemyHp() > use_setting) {
                return false;
            }
            if (name.equals("Dispel") && actor.zone.getEnemyBuffCount() == 0) {
                return false;
            }
            return used_in_rotation < use_setting;
        }
    }

    public boolean canCast(Actor actor) {
        if (name.equals("Prepare")) return false;
        return actor.getMp() >= calculate_manacost(actor);
    }

    public void startCast(Actor attacker, Actor target) {
        double speed_mult = Math.clamp((target.getSpeed() + 1000) / (attacker.getSpeed() + 1000), 0.75, 1.5);
        cast = 3 * speed_mult * attacker.getCast_speed_mult() * cast_mult + target.stealthDelay();
        if (attacker.isAmbushing()) cast = Math.max(0.01, cast - 5);
        delay = 1 * speed_mult * attacker.getDelay_speed_mult() * delay_mult;
        used_in_rotation++;
        //System.out.println(attacker.getName() + " casting " + name + " at " + target.getName() + " cast: " + cast +
        //" delay: " + delay);
    }

    public void startCastPlayer(Actor attacker) {
        double speed_mult = Math.clamp((attacker.zone.getAvgSpeed() + 1000) / (attacker.getSpeed() + 1000), 0.75, 1.5);
        cast = 3 * speed_mult * attacker.getCast_speed_mult() * cast_mult + attacker.zone.stealthDelay();
        if (attacker.isAmbushing()) cast = Math.max(0.01, cast - 5);
        delay = 1 * speed_mult * attacker.getDelay_speed_mult() * delay_mult;
        used_in_rotation++;
//        System.out.println(attacker.name + " started casting " + name);
    }

    public boolean progressCast(Actor actor, double delta) {
        if (actor.stun_time > 0) {
            if (actor.stun_time > delta) {
                actor.stun_time -= delta;
                delta = 0;
            } else {
                delta -= actor.stun_time;
                actor.stun_time = 0;
            }
        }
        cast -= delta;
        if (cast <= 0) {
            cast = 0;
            return true;
        }
        return false;
    }

    public boolean progressDelay(Actor actor, double delta) {
        if (actor.stun_time > 0) {
            if (actor.stun_time > delta) {
                actor.stun_time -= delta;
                delta = 0;
            } else {
                delta -= actor.stun_time;
                actor.stun_time = 0;
            }
        }
        delay -= delta;
        if (delay <= 0) {
            delay = 0;
            return true;
        }
        return false;
    }

    public double calculate_delta(Actor actor) {
        if (actor.stun_time > 0) {
            return actor.stun_time;
        }
        if (cast > 0) {
            return cast;
        }
        if (delay > 0) {
            return delay;
        }
        return 999;
    }

    public double calculate_manacost(Actor actor) {
        double cost = (mp * mp_mult + mp_additive) * actor.getMp_cost_mult() + actor.getMp_cost_add();
        cost *= 1 + actor.set_core;
        if (element == Element.water) cost *= 1 - actor.finke_bonus;
        return cost;
    }

    public void pay_manacost(Actor actor) {
        double cost = actor.casting.calculate_manacost(actor);
        mana_used += cost;
        actor.setMp(actor.getMp() - cost);
    }

    public void addDebuff(String name, double duration, double dmg) {
        this.debuff_name = name;
        this.base_debuff_duration = duration;
        this.base_debuff_dmg = dmg;
        setSkill(lvl, SkillMod.Enemy);
    }

    public void addBuff(String name, double duration, double bonus) {
        this.buff_name = name;
        this.base_buff_duration = duration;
        this.base_buff_bonus = bonus;
        setSkill(lvl, SkillMod.Enemy);
    }

    public double getLvl() {
        double fraction = exp / need_for_lvl(lvl);
        return lvl + fraction;
    }

    public double getLpercent() {
        return exp / need_for_lvl(lvl) * 100;
    }

    public void setLvl(double lvl) {
        setLvl((int) lvl);
        double next_lvl_exp = need_for_lvl((int) lvl);
        exp = next_lvl_exp * (lvl - (int) lvl);
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public void setSkill(double lvl, SkillMod type) {
        setLvl(lvl);
        setSkill(type);
    }

    public void setSkill(int lvl, SkillMod type) {
        setLvl(lvl);
        setSkill(type);
    }

    public void setSkill(SkillMod type) {
        if (name.equals("Prepare")) {
            return;
        }
        this.min = this.base_min;
        this.max = this.base_max;
        this.hit = this.base_hit;
        this.cast_mult = this.base_cast;
        this.delay_mult = this.base_delay;
        this.mp = this.base_mp;
        this.dmg_mult = 1;
        this.mp_additive = 0;
        this.mp_mult = 1;
        this.skillMod = type;
        switch (type) {
            case Basic:
                if (game_version >= 1563) {
                    this.min = this.base_min * (1 + 0.025 * lvl);
                    this.max = this.base_max * (1 + 0.025 * lvl);
                } else {
                    this.min = this.base_min * (1 + 0.02 * lvl);
                    this.max = this.base_max * (1 + 0.02 * lvl);
                }
                this.hit = this.base_hit * (1 + 0.01 * lvl);
                this.mp_mult = (1 + 0.01 * lvl);
                this.cast_mult = this.base_cast * (1 + 0.01 * lvl);
                this.delay_mult = this.base_delay * (1 + 0.01 * lvl);
                break;
            case Pow:
                if (game_version >= 1563) {
                    this.min = this.base_min * (1 + 0.015 * lvl);
                    this.max = this.base_max * (1 + 0.015 * lvl);
                } else {
                    this.min = this.base_min * (1 + 0.01 * lvl);
                    this.max = this.base_max * (1 + 0.01 * lvl);
                }
                break;
            case Hit:
                if (game_version >= 1563) {
                    this.hit = this.base_hit * (1 + 0.025 * lvl);
                } else {
                    this.hit = this.base_hit * (1 + 0.01 * lvl);
                }
                break;
            case Cheap:
                this.min = this.base_min * (1 - 0.01 * lvl);
                this.max = this.base_max * (1 - 0.01 * lvl);
                this.mp_mult = (1 - 0.02 * lvl);
                break;
            case Fast:
                this.mp_mult = (1 + 0.02 * lvl);
                this.cast_mult = this.base_cast * (1 - 0.01 * lvl);
                this.delay_mult = this.base_delay * (1 - 0.01 * lvl);
                break;
            case PowPow:
                this.min = this.base_min * (1 + 0.05 * lvl);
                this.max = this.base_max * (1 + 0.05 * lvl);
                this.hit = this.base_hit * (1 + 0.02 * lvl);
                this.mp_mult = (1 + 0.1 * lvl);
                this.mp = this.base_mp + lvl;
                break;
            case Damage:
                this.dmg_mult = (1 + 0.05 * lvl);
                this.mp_mult = (1 + 0.05 * lvl);
                this.mp_additive = lvl;
                break;
            case SlowHit:
                if (game_version >= 1563) {
                    this.hit = this.base_hit * (1 + 0.06 * lvl);
                    this.cast_mult = this.base_cast * (1 + 0.01 * lvl);
                    this.delay_mult = this.base_delay * (1 + 0.01 * lvl);
                } else {
                    this.hit = this.base_hit * (1 + 0.05 * lvl);
                    this.cast_mult = this.base_cast * (1 + 0.02 * lvl);
                    this.delay_mult = this.base_delay * (1 + 0.02 * lvl);
                }
                break;
            default:
                break;
        }
        if (debuff_name != null) {
            switch (debuff_name) {
                case "Burn":
                case "Poison":
                    this.debuff_duration = switch (this.skillMod) {
                        case SkillMod.Basic -> base_debuff_duration * (1 + (game_version >= 1563 ? 0.025 : 0.02) * lvl);
                        case SkillMod.Pow -> base_debuff_duration * (1 + (game_version >= 1563 ? 0.015 : 0.01) * lvl);
                        case SkillMod.PowPow -> base_debuff_duration * (1 + 0.05 * lvl);
                        case SkillMod.Cheap -> base_debuff_duration * (1 - 0.01 * lvl);
                        case SkillMod.Enemy -> base_debuff_duration;
                        case SkillMod.Damage -> base_debuff_duration;
                        default -> base_debuff_duration;
                    };
                    this.debuff_dmg = base_debuff_dmg * (1 + 0.02 * this.lvl);
                    break;
                case "Mark":
                case "Defense Break":
                case "Resist Break":
                case "Weaken":
                    this.debuff_duration = switch (this.skillMod) {
                        case SkillMod.Basic -> base_debuff_duration * (1 + (game_version >= 1563 ? 0.025 : 0.02) * lvl);
                        case SkillMod.Pow -> base_debuff_duration * (1 + (game_version >= 1563 ? 0.015 : 0.01) * lvl);
                        case SkillMod.PowPow -> base_debuff_duration * (1 + 0.05 * lvl);
                        case SkillMod.Cheap -> base_debuff_duration * (1 - 0.01 * lvl);
                        default -> base_debuff_duration;
                    };
                    this.debuff_effect = switch (this.skillMod) {
                        case SkillMod.Basic -> base_debuff_dmg * (1 + (game_version >= 1563 ? 0.025 : 0.02) * lvl);
                        case SkillMod.Pow -> base_debuff_dmg * (1 + (game_version >= 1563 ? 0.015 : 0.01) * lvl);
                        case SkillMod.PowPow -> base_debuff_dmg * (1 + 0.05 * lvl);
                        case SkillMod.Cheap -> base_debuff_dmg * (1 - 0.01 * lvl);
                        default -> base_debuff_dmg;
                    };
                    this.debuff_dmg = 0;
                    break;
                default:
                    this.debuff_duration = base_debuff_duration;
                    this.debuff_dmg = base_debuff_dmg * (1 + 0.02 * this.lvl);
                    this.debuff_effect = base_debuff_dmg * (1 + 0.02 * this.lvl);
                    break;
            }
        }
        if (buff_name != null) {
            this.buff_bonus = switch (this.skillMod) {
                case SkillMod.Basic -> base_buff_bonus * (1 + 0.02 * lvl);
                case SkillMod.Pow -> base_buff_bonus * (1 + 0.01 * lvl);
                case SkillMod.PowPow -> base_buff_bonus * (1 + 0.05 * lvl);
                case SkillMod.Cheap -> base_buff_bonus * (1 - 0.01 * lvl);
                default -> base_buff_bonus;
            };
            this.buff_duration = base_buff_duration;
        }
    }

    public void use(Actor attacker) {
        if (attacker.hide_bonus > 0) attacker.hide_bonus = 0;
        double gain = 0;
        int duration_bonus = attacker.buff_boost > 1 ? 1 : 0;
        attacker.current_skill_hit = true;
        switch (name) {
            case "Hide":
                attacker.hide_bonus = this.min;
                break;
            case "First Aid", "Heal":
                gain = min + max / 100.0 * (attacker.getIntel() / 2 + attacker.getResist() / 2);
                break;
            case "Prayer":
                double rng = Math.random() * 100;
                double power = this.min / 100;
                int rolls = game_version >= 1568 ? 4 : 5;
                double chance = 100.0 / rolls;
                if (rng < chance) {
                    hits_total += 1;
                    for (Enemy e : attacker.zone.enemies) {
                        if (e.getHp() < power * e.getHp_max()) {
                            hit_chance_sum += 1;
                            dmg_sum += e.getHp();
                            e.setHp(0);
                        }
                    }
                }
                if (rng >= chance && rng < chance * 2) {
                    attacker.hp = attacker.getHp() + attacker.getHp_max() * power;
                }
                if (rng >= chance * 2 && rng < chance * 3) {
                    if (attacker.charge == 0) {
                        attacker.buffs.add(new Buff("Charge Up", 1, power));
                    }
                }
                if (rolls >= 5 && rng >= chance * 3 && rng < chance * 4) {
                    attacker.setMp(attacker.getMp() + attacker.getMp_max() * power);
                }
                break;
            default:
                attacker.buffs.add(new Buff(buff_name, name.equals("Charge Up") ? (int) buff_duration + duration_bonus :
                        (int) buff_duration + 1 + duration_bonus, buff_bonus * attacker.buff_boost));
//                System.out.println(buff_name + " added to " + attacker.name + " duration " + buff_duration);
                break;
        }
        gain += attacker.getHp_max() * attacker.hp_regen;
        if (gain > 0) {
            attacker.setHp(attacker.getHp() + gain);
            //System.out.println(attacker.name + " healed for " + (int) gain);
        }
        Zone zone = attacker.zone;
        if (zone != null) {
            for (Enemy enemy : zone.enemies) {
                if (heal && enemy.counter_heal) {
                    counter_attack(attacker, enemy, true); //Counter heal will log as Counter Strike
                }
            }
        }
        attacker.last_skill = this;
    }

    public double attack(Actor attacker, Actor defender, int overwrite_hits) {
        double gain = attacker.getHp_max() * attacker.hp_regen;
        attacks_total++;
        if (gain > 0) {
            //System.out.println(gain);
            attacker.setHp(attacker.getHp() + gain);
        }
        if (!this.aoe && defender.hide_bonus > 0) {
            //System.out.println("The target is hidden!");
            return 0;
        }
        double total = 0;

        double hit_chance = (attacker.isSmoked() ? 0.5 : 1) * attacker.getHit() * this.hit / defender.getSpeed() / 1.2;
        hit_chance = Math.max(0.05, hit_chance / defender.getDodge_mult());
        if (name.equals("Back Stab") && !defender.isSmoked()) hit_chance *= 0.5;
        hit_chance_sum += Math.min(hit_chance, 1);
        if (defender.zone != null) {
            defender.zone.stats.incrementHit(attacker.getName(), name, hit_chance);
            if (!attacker.debuffs.isEmpty()) defender.zone.stats.incrementUsedDebuffed(attacker.getName(), name, 1);
        }
        if (!attacker.debuffs.isEmpty()) used_debuffed++;
        if (hit_chance < 1 && attacker.cl > 0) {
            //System.out.println(name + " has hit chance of " + hit_chance * 100 + "%, smoked=" + attacker.isSmoked());
        }
        if ((hit_chance >= 1) || (Math.random() < hit_chance)) {
            attacker.current_skill_hit = true;
            hits_total++;
            if (this.debuff_name != null) {
                applyDebuff(attacker, defender);
            }
            if (max > 0) {
                if (defender.counter_strike > 0 && defender.counter_strike > Math.random()) {
                    counter_attack(attacker, defender, false);
                }
                double enemy_resist;
                double atk = 0;
                double def = 0;
                double dmg_mult = attacker.getDmg_mult();
                dmg_mult += attacker.hide_bonus;
                dmg_mult *= 1.0 + attacker.ambush_bonus;
                dmg_mult *= this.dmg_mult;
                if (element == Element.water) dmg_mult *= 1 + attacker.finke_bonus;
                if (name.equals("Back Stab") && defender.isSmoked()) dmg_mult *= 2;
                enemy_resist = switch (this.element) {
                    case Element.dark -> {
                        atk = attacker.getDark();
                        yield defender.getDark_res();
                    }
                    case Element.fire -> {
                        atk = attacker.getFire();
                        yield defender.getFire_res();
                    }
                    case Element.light -> {
                        atk = attacker.getLight();
                        yield defender.getLight_res();
                    }
                    case Element.water -> {
                        atk = attacker.getWater();
                        dmg_mult *= attacker.set_water;
                        yield defender.getWater_res();
                    }
                    case Element.wind -> {
                        atk = attacker.getWind();
                        yield defender.getWind_res();
                    }
                    case Element.earth -> {
                        atk = attacker.getEarth();
                        yield defender.getEarth_res();
                    }
                    case Element.phys -> {
                        yield defender.getPhys_res();
                    }
                    case Element.magic -> {
                        yield defender.getMagic_res();
                    }
                    case Element.eleblast -> {
                        atk += attacker.getFire() * (1 - defender.getFire_res());
                        atk += attacker.getWind() * (1 - defender.getWind_res());
                        atk += attacker.getWater() * (1 - defender.getWater_res());
                        atk += attacker.getEarth() * (1 - defender.getEarth_res());
                        yield defender.getMagic_res(); //Not sure if it's right formula
                    }
                    case Element.physmagic -> {
                        yield defender.getPhys_res() / 2 + defender.getMagic_res() / 2 * game_version < 1532 ? -1 : 1;
                    }
                    default -> 0;
                };
                def = switch (this.scaling) {
                    case atk -> {
                        atk += attacker.getAtk();
                        dmg_mult *= attacker.set_physdmg;
                        yield defender.getDef();
                    }
                    case atkint -> {
                        atk += attacker.getAtk() / 2 + attacker.getIntel() / 2;
                        dmg_mult *= attacker.set_magicdmg;
                        yield defender.getDef() / 2 + defender.getResist() / 2;
                    }
                    case atkhit -> {
                        atk += attacker.getAtk() / 2 + attacker.getHit() / 2;
                        dmg_mult *= attacker.set_physdmg;
                        yield defender.getDef();
                    }
                    case intel -> {
                        atk += attacker.getIntel();
                        dmg_mult *= attacker.set_magicdmg;
                        yield defender.getResist();
                    }
                    case resint -> {
                        atk += attacker.getResist() / 2 + attacker.getIntel() / 2;
                        dmg_mult *= attacker.set_magicdmg;
                        yield defender.getResist();
                    }
                    case res -> {
                        atk += attacker.getResist();
                        yield defender.getResist();
                    }
                };
                if (attacker.gear_crit > 0 && Math.random() < attacker.gear_crit) {
                    atk *= 1.5;
                }
                if (name.equals("Pierce")) {
                    def = 0;
                }
                double atk_mit = atk;
                if (game_version < 1566) {
                    atk_mit *= (1 - enemy_resist);
                } else {
                    dmg_mult *= (1 - enemy_resist);
                }
                dmg_mult *= attacker.isMulti_hit_override() ? attacker.multi_arrows : 1;
                dmg_mult *= (1 - attacker.set_training);
                int calc_hits = overwrite_hits > 0 ? overwrite_hits : hits;
                if (this.name.equals("Dispel")) {
                    calc_hits = defender.buff_count();
                    defender.buffs.clear();
                    defender.tick_buffs();
                }
                if (attacker.gear_stun > 0 && game_version >= 1566 && Math.random() < attacker.gear_stun) {
                    defender.stun_time += 2.0;
                }
                for (int i = 0; i < calc_hits; i++) {
                    if (attacker.gear_stun > 0 && game_version < 1566 && Math.random() < attacker.gear_stun) {
                        defender.stun_time += 2.0;
                    }
                    double dmg = Math.random() * (this.max - this.min) + this.min;
                    dmg =
                            ((dmg * (atk_mit)) / (Math.pow(def, 0.7) + 100) - Math.pow(def, 0.85)) * Math.pow(1.1,
                                    calc_hits) * dmg_mult;
                    dmg = Math.max(1, dmg);
//                    System.out.println(attacker.name + attacker + " dealt " + (int) dmg + " damage with " + this.name +
//                            " to " + defender.name);
                    total += dmg;
                    //if (total > defender.getHp()) break; //doesn't work like that according to tests
                    if (total - dmg > defender.getHp() && i == calc_hits - 1) {
                        total = defender.getHp() + dmg;
                    }
                }
            }
        } else {
            if (defender.counter_dodge) {
                counter_attack(attacker, defender, true);
            }
        }
        if (total > 0 && !name.equals("Mark Target")) defender.remove_mark();
        if (attacker.hide_bonus > 0) attacker.hide_bonus = 0;
        dmg_sum += total;
        if (defender.zone != null) {
            defender.zone.stats.incrementDmg(attacker.getName(), name, total);
        }
        if (name.equals("Careful Shot")) {
            total = Math.min(total, defender.getHp());
        }
        if (name.equals("Aimed Shot")) {
            total = Math.min(total, defender.getHp() - 1);
        }
        attacker.last_skill = this;
        return total;
    }

    public void counter_attack(Actor attacker, Actor defender, boolean counter_dodge) {
        double atk = defender.getAtk();
        double def = attacker.getDef();
        double dmg = (atk * 100 / (Math.pow(def, 0.7) + 100) - Math.pow(def, 0.85)) * Math.pow(1.1, 1) * 2;
        if (game_version >= 1568) {
            dmg *= (1 - attacker.getPhys_res()) * 2;
        }
        dmg = Math.max(1, dmg);
        ActiveSkill skill = counter_dodge ? defender.counter_dodge_log : defender.counter_strike_log;
        if (attacker.zone != null) {
            attacker.zone.stats.incrementStats(defender.getName(), skill.name, dmg, 1, 0, 1, 1, 0);
        } else {
            skill.used += 1;
            skill.hits_total += 1;
            skill.dmg_sum += dmg;
        }
//        System.out.println(skill.name + ": " + dmg);
        attacker.setHp(attacker.getHp() - dmg);
    }

    public void applyDebuff(Actor attacker, Actor defender) {
        double hit_chance =
                (attacker.getHit() * this.hit + attacker.getIntel()) / (defender.getDef() + defender.getResist()) / 1.2;
        if (debuff_name.equals("Poison")) {
            if (game_version >= 1535) {
                hit_chance =
                        (attacker.getHit() * this.hit + attacker.getSpeed()) / (defender.getDef() + defender.getResist()) / 1.2;
            }
            if (attacker.isPoison_boost()) hit_chance *= 2;
//            System.out.println(name + " poison chance: " + hit_chance);
        }
        if (debuff_name.equals("Burn")) {
            // System.out.println(attacker.name + ": " + name + " burn chance: " + hit_chance);
        }

        hit_chance /= defender.getAilment_res();
        if (Objects.equals(this.debuff_name, "Smoke")) {
            hit_chance = 1;
            defender.setSmoked(true);
        }
        if (debuff_name.equals("Mark")) {
            hit_chance = 1;
        }
        if (hit_chance < 0.2) {
            return;
        } else {
//            System.out.println(attacker.name + ": " + name + " debuff chance: " + hit_chance);
            debuff_chance_sum += hit_chance;
            if (defender.zone != null) {
                defender.zone.stats.incrementDebuff(attacker.getName(), name, hit_chance);
            }
        }
        if ((hit_chance >= 1) || (Math.random() < hit_chance)) {
            int duration = (int) this.debuff_duration;
            double fractional = this.debuff_duration - duration;
            if (Math.random() < fractional) {
                duration += 1;
            }
            double dmg = switch (this.debuff_name) {
                case "Poison" -> this.debuff_dmg * (attacker.getIntel() + attacker.getAtk()) * attacker.poison_mult;
                case "Burn" ->
                        this.debuff_dmg * (attacker.getIntel() / 10 + attacker.getFire() / 5) * (1 - defender.fire_res) * (attacker.burn_mult + attacker.gear_burn);
                default -> 0;
            };
            if (defender.zone != null) {
                defender.zone.stats.incrementDot(attacker.getName(), name, duration * dmg);
            }
            defender.debuffs.add(new Debuff(this.debuff_name, duration, dmg, debuff_effect));
        }
    }

    public void gainExp(double value) {
        if (enabled) {
            exp += value;
            double need = need_for_lvl(lvl);
            if (exp >= need && lvl < 20) {
                lvl++;
                exp -= need;
                setSkill(lvl, skillMod);
            }
        }
    }

    public void gainExp() {
        gainExp(1);
    }

    public double need_for_lvl(int lvl) {
        return ((Math.pow(Math.max(lvl, 1), 2)) * 1000);
    }

    public double average_hit_chance() {
        return attacks_total > 0 ? hit_chance_sum / attacks_total : 0;
    }

    public double average_dmg() {
        return hits_total > 0 ? dmg_sum / hits_total : 0;
    }

    public double average_debuff_chance() {
        return hits_total > 0 ? debuff_chance_sum / hits_total : 0;
    }

    public void clear_recorded_data() {
        used = 0;
        mana_used = 0;
        hits_total = 0;
        attacks_total = 0;
        hit_chance_sum = 0;
        dmg_sum = 0;
        debuff_chance_sum = 0;
        used_debuffed = 0;
    }

    public String getRecordedData(int simulations) {
        if (hit == 0 && !name.equals("Prayer")) {
            switch (name) {
                case "Counter Strike", "Counter Dodge" -> {
                    return name + " used: " + df4.format((double) used / simulations) +
                            "; dmg: " + (int) average_dmg() + "; mana used: " + (int) mana_used / simulations + "\n";
                }
                default -> {
                    return name + " used: " + df4.format((double) used / simulations) + "; mana used: " + (int) mana_used / simulations + "\n";
                }
            }
        } else {
            return name + " used: " + df4.format((double) used / simulations) + "; hit: " + df2.format(average_hit_chance() * 100) + "%" +
                    "; dmg: " + (int) average_dmg() + "; mana used: " + (int) mana_used / simulations +
                    (debuff_name == null ? "" : "; debuff chance: " + df2.format(average_debuff_chance() * 100) + "%") +
                    (used_debuffed == 0 ? "" : "; used debuffed: " + df2.format((double) used_debuffed / attacks_total)) +
                    "\n";
        }
    }

    public String getWeakRecordedData() {
        return name + " used: " + used + "; hit: " + df2.format(average_hit_chance() * 100) + "%" +
                "; dmg: " + (int) average_dmg() + "\n";
    }

    public boolean isSingleTarget() {
        return (!aoe && !random_targets);
    }
}
