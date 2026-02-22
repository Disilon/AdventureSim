package Disilon;

import static Disilon.Main.df2;
import static Disilon.Main.df2p;
import static Disilon.Main.df4;
import static Disilon.Main.game_version;
import static Disilon.Main.log;
import static java.lang.Math.max;

public class ActiveSkill {
    public String name;
    public ActorStats owner;
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
    public double full_cast;
    public double delay;
    public int used_in_rotation;
    public int used_debuffed;
    public double exp;
    public double hit_chance_sum;
    public double dmg_sum;
    public double extra_dmg_sum;
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
    public boolean available = true;
    public boolean triggers_counter = true;
    public boolean weapon_required = true;
    public boolean attack;

    public ActiveSkill(ActorStats owner, String name) {
        this.owner = owner;
        this.name = name;
        this.lvl = 0;
        this.attack = false;
    }

    public ActiveSkill(ActorStats owner, String name, double mp, double cast_mult, double delay_mult) {
        this.owner = owner;
        this.name = name;
        this.base_mp = mp;
        this.base_cast = cast_mult;
        this.base_delay = delay_mult;
        this.lvl = 0;
        this.mp_mult = 1;
        this.attack = false;
        setSkill(lvl, SkillMod.Enemy);
    }

    public ActiveSkill(ActorStats owner, String name, int hits, double min, double max, double hit, double mp,
                       double cast_mult, double delay_mult,
                       Scaling scaling, Element element, boolean aoe, boolean heal) {
        this.owner = owner;
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
        this.attack = max > 0;
        setSkill(lvl, SkillMod.Enemy);
    }

    public void applyVersion() {
        switch (name) {
            case "Binding Shot" -> {
                if (game_version >= 1621) {
                    base_cast = 0.2;
                    base_delay = 2.5;
                } else {
                    base_cast = 0.7;
                    base_delay = 1;
                }
            }
            case "Throw Sand" -> {
                if (game_version >= 1621) {
                    triggers_counter = false;
                } else {
                    triggers_counter = true;
                }
            }
            case "Analyze" -> {
                if (game_version >= 1649) {
                    base_cast = 2.8;
                    base_delay = 0.6;
                } else {
                    base_cast = 3;
                    base_delay = 0;
                }
            }
            case "Aimed Shot" -> {
                if (game_version >= 1649) {
                    base_min = 315;
                    base_max = 385;
                } else {
                    base_min = 270;
                    base_max = 330;
                }
            }
        }
    }

    public boolean shouldUse(Actor actor) {
        if (name.equals("Prepare")) return false;
        if (heal) {
            used_in_rotation++;
            return actor.hp / actor.getHp_max() * 100.0 < use_setting;
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
            if (name.equals("Careful Shot") || name.equals("Dispel")) {
                return false;
            }
            if (name.equals("Stone Barrier") && (actor.hasBuff(name) || actor.checkLastSkill(name))) {
                return false;
            }
            if (name.equals("Throw Sand") && use_setting == 2) {
                return false;
            }
            if (log.contains("skill_use")) System.out.println(name + " used: " + used_in_rotation + " setting: " + use_setting);
            return used_in_rotation < use_setting;
        }
    }

    public boolean canCast(Actor actor) {
        double cost = calculate_manacost(actor);
        boolean enough_mp = actor.mp >= cost;
        if (!enough_mp && log.contains("skill_enough_mp")) System.out.println(name + " skipped, not enough mp, cost: " + (int) cost);
        return enough_mp;
    }

    public static double offlineTime(double add, double total_time) {
        double exact = total_time + add;
        double tick = 0.18;
        double next_tick = Math.ceil(exact / tick) * tick;
//        System.out.println("exact=" + add + "; next_tick_diff=" + (next_tick - exact));
//        System.out.println(next_tick);
        return next_tick - total_time;
    }

    public static double onlineTime(double add, double total_time) {
        double exact = total_time + add;
        double tick = 0.03;
        double next_tick = Math.ceil(exact / tick) * tick;
//        System.out.println("exact=" + add + "; next_tick_diff=" + (next_tick - exact));
//        System.out.println(next_tick);
        return next_tick - total_time;
    }

    public void startCast(Actor attacker, Actor target, boolean offline, double time, double total_time) {
        double speed_mult = Math.clamp((target.getSpeed() + 1000) / (attacker.getSpeed() + 1000), 0.75, 1.5);
        cast = 3 * speed_mult * attacker.cast_speed_mult * cast_mult + target.stealthDelay();
        if (attacker.ambushing) cast = max(0.0, cast - 5);
        cast = max(0.01, cast);
        delay = 1 * speed_mult * attacker.delay_speed_mult * delay_mult;
        if (name.equals("Flee") && cast > 0.5) {
            cast -= 0.5;
            delay += 0.5;
        }
        if (log.contains("skill_cast_start")) System.out.println("\n" + attacker.name + " started casting " + name +
                " at " + df2.format(time) + ", cast_time = " + df2.format(cast) + ", delay_time = " + df2.format(delay));
    }

    public void startCastPlayer(Actor attacker, boolean offline, double time, double total_time) {
        double speed_mult = Math.clamp((attacker.zone.getAvgSpeed() + 1000) / (attacker.getSpeed() + 1000), 0.75, 1.5);
        attacker.speed_mult_sum += speed_mult;
        attacker.speed_mult_count += 1;
        if (name.equals("Analyze")) {
            cast =
                    3 * speed_mult * attacker.cast_speed_mult * cast_mult * attacker.analyze_speed + attacker.zone.stealthDelay();
            double factor =
                    attacker.zone.enemies[0].hp / (this.min / 100 * attacker.getIntel()) / (1 + attacker.gear_analyze);
            cast *= max(factor, 1);
//            System.out.println("Factor: " + df4.format(factor) + " ; cast: " + df2p.format(cast));
            delay = 0;
        } else {
            cast = 3 * speed_mult * attacker.cast_speed_mult * cast_mult + attacker.zone.stealthDelay();
            if (attacker.ambushing) cast = max(0.0, cast - 5);
            delay = 1 * speed_mult * attacker.delay_speed_mult * delay_mult;
        }
        cast = max(0.01, cast);
        delay = max(0.5*0.75, delay);
        if (log.contains("skill_cast_start")) System.out.println("\n" + attacker.name + " started casting " + name +
                " at " + df2.format(time) + ", cast_time = " + df2.format(cast) + ", delay_time = " + df2.format(delay));
    }

    public void pushCast(Actor attacker, Actor defender, double effect) {
        double time = 0;
        if (defender.casting.cast > 0) {
            time = 1;
        }
        defender.casting.cast += time;
        if (log.contains("debuff_applied") && defender.casting != null) {
            String str =
                    name + " have pushed cast " + defender.casting.name + " of " + defender.name + " by " + time;
            System.out.println(str);
        }
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
        if (delay >= 0) {
            return delay;
        }
        return 999;
    }

    public double calculate_manacost(Actor actor) {
        double cost = (mp * mp_mult + mp_additive) * actor.mp_cost_mult + actor.mp_cost_add;
        cost *= 1 + actor.set_core;
        cost *= 1 - actor.set_mana;
        if (element == Element.water) cost *= 1 - actor.finke_bonus;
        return cost;
    }

    public void pay_manacost(Actor actor) {
        double cost = actor.casting.calculate_manacost(actor);
        mana_used += cost;
        actor.setMp(actor.mp - cost);
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
        applyVersion();
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
                case "Stun":
                case "Defense Break":
                case "Resist Break":
                case "Weaken":
                case "Bound":
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
                case SkillMod.Basic -> base_buff_bonus * (1 + (game_version >= 1563 ? 0.025 : 0.02) * lvl);
                case SkillMod.Pow -> base_buff_bonus * (1 + (game_version >= 1563 ? 0.015 : 0.01) * lvl);
                case SkillMod.PowPow -> base_buff_bonus * (1 + 0.05 * lvl);
                case SkillMod.Cheap -> base_buff_bonus * (1 - 0.01 * lvl);
                default -> base_buff_bonus;
            };
            this.buff_duration = base_buff_duration;
        }
    }

    public void use(Actor attacker, double time) {
        if (attacker.hide_bonus > 0) attacker.hide_bonus = 0;
        double gain = 0;
        attacker.current_skill_hit = true;
        switch (name) {
            case "Hide":
                if (attacker.passives.get("Extra Attack").enabled && Math.random() < 0.05) {
//                    double mult = attacker.getDmg_mult() * this.dmg_mult * 1.1;
//                    mult *= 1.0 + attacker.ambush_bonus;
//                    mult *= 1 + attacker.finke_bonus;
//                    mult *= attacker.set_water;
//                    mult *= 1 + attacker.elemental_buff;
//                    mult *= attacker.isMulti_hit_override(this.name) ? attacker.multi_arrows : 1;
//                    mult *= (1 - attacker.set_training);
//                    mult *= attacker.set_physdmg;
//                    extra_attack(attacker, attacker, mult); it was "fixed", maybe v1631?
                } else {
                    attacker.hide_bonus = this.min;
                }
                break;
            case "First Aid", "Heal":
                gain = min + max / 100.0 * (attacker.getIntel() / 2 + attacker.getResist() / 2);
                break;
            case "Prayer":
                double rng = Math.random() * 100;
                double power = this.min / 100;
                int rolls = game_version >= 1568 ? 4 : 5;
                enum Roll{execute, heal, buff, mana, nothing};
                Roll roll = Roll.nothing;
                double chance = 100.0 / rolls;
                attacks_total += 1;
//                System.out.println("Enemy hp = " + df2.format(attacker.zone.getMaxEnemyHpPercent())
//                        + " Prayer execute = " + df2.format(power));
                if (rng < chance) {
                    if (game_version >= 1574 && attacker.zone.getMaxEnemyHpPercent() > power){
                        roll = Roll.heal;
                    } else {
                        roll = Roll.execute;
                    }
                }
                if (rng >= chance && rng < chance * 2) {
                    roll = Roll.heal;
                }
                if (rng >= chance * 2 && rng < chance * 3) {
                    roll = Roll.buff;
                }
                if (rolls >= 5 && rng >= chance * 3 && rng < chance * 4) {
                    roll = Roll.mana;
                }
                switch (roll) {
                    case execute -> {
                        hits_total += 1;
                        for (Enemy e : attacker.zone.enemies) {
                            if (e.active && e.hp < power * e.getHp_max()) {
                                hit_chance_sum += 1;
                                dmg_sum += e.hp;
                                e.setHp(0);
                            }
                        }
                    }
                    case heal -> {
                        attacker.setHp(attacker.hp + attacker.getHp_max() * power, power);
                    }
                    case buff -> {
                        if (attacker.charge == 0) {
                            attacker.applyBuff("Charge Up", 1, power);
                        }
                    }
                    case mana -> {
                        attacker.setMp(attacker.mp + attacker.getMp_max() * power);
                    }
                    default -> {}
                }
                break;
            case "Stone Barrier":
                double absorb = min * (attacker.getDef() + attacker.getResist()) * (1 + attacker.getEarth_res());
                attacker.remove_buff(name);
                attacker.buffs.add(new Buff(name, 1, absorb));
                break;
            default:
                if (buff_name != null) {
                    applyBuff(attacker);
                }
                break;
        }
        gain += attacker.getHp_max() * attacker.hp_regen;
        if (gain > 0) {
            attacker.setHp(attacker.hp + gain);
            //System.out.println(attacker.name + " healed for " + (int) gain);
        }
        Zone zone = attacker.zone;
        if (log.contains("skill_attack")) {
            System.out.println(attacker.name + " used " + name + " at " + df2.format(time));
        }
        if (zone != null) {
            for (Enemy enemy : zone.enemies) {
                if (heal && enemy.counter_heal && triggers_counter) {
                    counter_attack(attacker, enemy, true); //Counter heal will log as Counter Strike
                }
            }
        }
        attacker.last_skill = this;
    }

    public double attack(Actor attacker, Actor defender, int overwrite_hits, double time) {
        double gain = attacker.getHp_max() * attacker.hp_regen;
        attacks_total++;
        if (gain > 0) {
            //System.out.println(gain);
            attacker.setHp(attacker.hp + gain);
        }
        if (!this.aoe && defender.hide_bonus > 0) {
            //System.out.println("The target is hidden!");
            return 0;
        }
        double total = 0;
        double hit_chance = (attacker.smoked ? 0.5 : 1) * attacker.getHit() * this.hit / defender.getSpeed() / 1.2;
        if (game_version >= 1627 && attacker.set_squirrel_rate == 1) hit_chance /= 1.25;
        hit_chance = max(0.05, hit_chance / defender.getDodge_mult());
        hit_chance = Math.min(hit_chance, 1);
        hit_chance_sum += hit_chance;
        if (name.equals("Back Stab") && !(defender.smoked || defender.bound > 0)) {
            hit_chance *= 0.5;
        }
        if (defender.zone != null) {
            defender.zone.stats.incrementHit(attacker.name, name, hit_chance);
            if (!attacker.debuffs.isEmpty()) defender.zone.stats.incrementUsedDebuffed(attacker.name, name, 1);
        }
        if (!attacker.debuffs.isEmpty()) used_debuffed++;
        if ((hit_chance >= 1) || (Math.random() < hit_chance)) {
            attacker.current_skill_hit = true;
            hits_total++;
            if (this.debuff_name != null) {
                applyDebuff(attacker, defender);
            }
            if (defender.counter_strike > 0 && triggers_counter && defender.counter_strike > Math.random()) {
                counter_attack(attacker, defender, false);
            }
            if (max > 0) {
                double enemy_resist;
                double atk = 0;
                double def = 0;
                double dmg_mult = attacker.getDmg_mult();
                double dmg_mult1 = 1;
                dmg_mult *= 1.0 + attacker.hide_bonus;
                dmg_mult *= 1.0 + attacker.ambush_bonus;
                dmg_mult *= this.dmg_mult;
                if (name.equals("Back Stab") && (defender.smoked || defender.bound > 0)) {
                    dmg_mult1 *= 2;
                }
                enemy_resist = switch (this.element) {
                    case Element.dark -> {
                        atk = attacker.getDark();
                        yield defender.getDark_res();
                    }
                    case Element.fire -> {
                        atk = attacker.getFire();
                        dmg_mult *= attacker.set_fire;
                        dmg_mult *= 1 + attacker.elemental_buff;
                        yield defender.getFire_res();
                    }
                    case Element.light -> {
                        atk = attacker.getLight();
                        yield defender.getLight_res();
                    }
                    case Element.water -> {
                        atk = attacker.getWater();
                        dmg_mult *= 1 + attacker.finke_bonus;
                        dmg_mult *= attacker.set_water;
                        dmg_mult *= 1 + attacker.elemental_buff;
                        yield defender.getWater_res();
                    }
                    case Element.wind -> {
                        atk = attacker.getWind();
                        dmg_mult *= 1 + attacker.elemental_buff;
                        yield defender.getWind_res();
                    }
                    case Element.earth -> {
                        atk = attacker.getEarth();
                        dmg_mult *= attacker.set_earth;
                        dmg_mult *= 1 + attacker.elemental_buff;
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
                double crit_chance = defender.bound + attacker.gear_crit + attacker.base_crit_chance;
                double not_crit = (1 - defender.bound) * (1 - attacker.gear_crit - attacker.base_crit_chance);
                double crit_dmg = attacker.base_crit_damage;
                if (crit_chance > 0 && Math.random() < crit_chance) {
//                if (not_crit < 1 && Math.random() > not_crit) {
                    atk *= crit_dmg;
                    attacker.last_crit = true;
                } else {
                    attacker.last_crit = false;
                }
                if (name.equals("Pierce")) {
                    def = 0;
                }
                double atk_mit = atk;
                dmg_mult *= attacker.isMulti_hit_override(this.name) ? attacker.multi_arrows : 1;
                dmg_mult *= (1 - attacker.set_training);
                int calc_hits = overwrite_hits > 0 ? overwrite_hits : hits;
                if (this.name.equals("Dispel")) {
                    calc_hits = defender.buff_count();
                    defender.buffs.clear();
                    defender.check_buffs();
                }
                if (attacker.gear_stun > 0 && game_version >= 1566 && Math.random() < attacker.gear_stun) {
                    defender.stun_time += 2.0;
                }
                int extra = (attacker.passives.get("Extra Attack").enabled && !name.equals("Extra Attack")) ? 1 : 0;
                for (int i = 0; i < calc_hits; i++) {
                    if (attacker.gear_stun > 0 && game_version < 1566 && Math.random() < attacker.gear_stun) {
                        defender.stun_time += 2.0;
                    }
                    double dmg = this.max - Math.random() * (this.max - this.min) * (1 - attacker.dmg_range);
                    if (attacker.zone == null && weapon_required) {
                        dmg *= 0.7;
                    }
                    dmg =
                            ((dmg * (atk_mit)) / (Math.pow(def, 0.7) + 100) - Math.pow(def, 0.85)) * Math.pow(1.1,
                                    calc_hits) * dmg_mult * dmg_mult1;
                    dmg = dmg * (1 - enemy_resist);
                    dmg = max(1, dmg);
                    dmg = max(0, dmg - defender.getBarrier());
                    if (log.contains("skill_attack")) {
                        System.out.println(attacker.name + " dealt " + (int) dmg + " damage with " + this.name +
                                " to " + defender.name + " at " + df2.format(time) + " chance " + df2.format(hit_chance*100) + "%");
                    }
                    total += dmg;
                    if (total - dmg > defender.hp && i == calc_hits - 1) {
                        total = defender.hp + dmg;
                    }
                }
                if (extra == 1 && defender == attacker.target) {
                    if (Main.balance1 && total > defender.hp) {
                        total = defender.hp;
                    }
                    if (Main.balance2) {
                        extra_attack(attacker, defender, dmg_mult * dmg_mult1 * Math.pow(1.1,
                                calc_hits));
                    } else {
                        extra_attack(attacker, defender, dmg_mult * Math.pow(1.1,
                                calc_hits));
                    }
                }
            }
        } else {
            if (defender.counter_dodge && triggers_counter) {
                counter_attack(attacker, defender, true);
            }
            if (log.contains("skill_attack")) {
                System.out.println(attacker.name + " missed with " + this.name +
                        " at " + defender.name + " at " + df2.format(time));
            }
        }
        if (total > 0 && !name.equals("Mark Target")) defender.remove_mark();
        if (total == 0 && log.contains("skill_attack")) {
            System.out.println(attacker.name + " used " + this.name +
                    " at " + defender.name + " at " + df2.format(time));
        }
        if (name.equals("Push Blast")) {
            pushCast(attacker, defender, 0.2);
        }

        if (attacker.hide_bonus > 0) attacker.hide_bonus = 0;
        dmg_sum += total;
        if (defender.zone != null) {
            defender.zone.stats.incrementDmg(attacker.name, name, total);
        }
        if (name.equals("Careful Shot")) {
            total = Math.min(total, defender.hp);
        }
        if (name.equals("Aimed Shot")) {
            total = Math.min(total, defender.hp - 1);
        }
        attacker.last_skill = this;
        return total;
    }

    public void extra_attack(Actor attacker, Actor defender, double dmg_mult) {
        double def = defender.getDef();
        double dmg = attacker.passives.get("Extra Attack").getBonus() * 100;
        double atk = attacker.getAtk() + attacker.getWater();
        double crit_chance = defender.bound + attacker.gear_crit + attacker.base_crit_chance;
        double not_crit = (1 - defender.bound) * (1 - attacker.gear_crit - attacker.base_crit_chance);
        double crit_dmg = attacker.base_crit_damage;
        if (crit_chance > 0 && Math.random() < crit_chance) {
//        if (not_crit < 1 && Math.random() > not_crit) {
            atk *= crit_dmg;
            attacker.last_crit = true;
        } else {
            attacker.last_crit = false;
        }
        dmg = (dmg * atk / (Math.pow(def, 0.7) + 100) - Math.pow(def, 0.85)) * dmg_mult;
        dmg = dmg * (1 - defender.getWater_res());
        dmg = max(1, dmg);
        dmg = max(0, dmg - defender.getBarrier());
        if (log.contains("skill_attack")) {
            System.out.println(attacker.name + " dealt " + (int) dmg + " extra damage with " + this.name +
                    " to " + defender.name);
        }
        extra_dmg_sum += dmg;
        defender.setHp(defender.hp - dmg);
    }

    public void counter_attack(Actor attacker, Actor defender, boolean counter_dodge) {
        double atk = defender.getAtk();
        double def = attacker.getDef();
        double enemy_mult = 2;
        double dmg = (atk * 100 / (Math.pow(def, 0.7) + 100) - Math.pow(def, 0.85)) * Math.pow(1.1, 1);
        if (game_version >= 1568) {
            dmg *= (1 - attacker.getPhys_res());
            enemy_mult = 4;
        }
        if (attacker.zone != null) {
            dmg *= enemy_mult;
        }
        dmg = max(1, dmg);
        ActiveSkill skill = counter_dodge ? defender.counter_dodge_log : defender.counter_strike_log;
        if (attacker.zone != null) {
            attacker.zone.stats.incrementStats(defender.name, skill.name, dmg, 1, 0, 1, 1, 0);
            attacker.damage_taken += dmg;
        } else {
            skill.used += 1;
            skill.hits_total += 1;
            skill.dmg_sum += dmg;
        }
        if (log.contains("skill_attack")) {
            System.out.println(defender.name + " dealt " + (int) dmg + " damage with counterattack" +
                    " to " + attacker.name + " triggered by " + this.name);
        }
        dmg = max(0, dmg - attacker.getBarrier());
        attacker.setHp(attacker.hp - dmg);
    }

    public void applyBuff(Actor attacker) {
        double bonus = buff_bonus;
        int duration = (int) this.buff_duration;
        double fractional = this.buff_duration - duration;
        if (Math.random() < fractional) {
            duration += 1;
        }
        if (buff_name.equals("Bless")) {
            duration += (int) attacker.bless_duration;
            bonus *= attacker.bless_boost;
        }
        attacker.applyBuff(buff_name, duration, bonus);
    }

    public void applyDebuff(Actor attacker, Actor defender) {
        double hit_chance = (attacker.getHit() * this.hit + attacker.getIntel()) / (defender.getDef() + defender.getResist()) / 1.2;
        if (debuff_name.equals("Poison")) {
            hit_chance = (attacker.getHit() * this.hit + attacker.getSpeed()) / (defender.getDef() + defender.getResist()) / 1.2;
            if (attacker.passives.get("Poison Boost").enabled) hit_chance *= 2;
//            System.out.println(name + " poison chance: " + hit_chance);
        }
        if (name.equals("Throw Sand")) {
            hit_chance = (attacker.getHit() * this.hit + attacker.getSpeed()) / (defender.getDef() + defender.getResist()) / 1.2;
        }
        if (debuff_name.equals("Burn")) {
            // System.out.println(attacker.name + ": " + name + " burn chance: " + hit_chance);
        }

        hit_chance /= defender.ailment_res;
        if (name.equals("Smoke Screen")) {
            hit_chance = 1;
        }
        if (debuff_name.equals("Mark")) {
            hit_chance = 1;
        }
        if (hit_chance < 0.2) {
            hit_chance = 0;
        } else {
//            System.out.println(attacker.name + ": " + name + " debuff chance: " + hit_chance);
        }
        debuff_chance_sum += hit_chance;
        if (defender.zone != null) {
            defender.zone.stats.incrementDebuff(attacker.name, name, hit_chance);
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
                defender.zone.stats.incrementDot(attacker.name, name, duration * dmg);
            }
            defender.applyDebuff(debuff_name, duration, dmg, debuff_effect);
            if (log.contains("debuff_applied")) {
                String str = debuff_name + " was applied to " + name + " duration = " + duration;
                if (dmg > 0) str += " dmg = " + (int) dmg;
                if (debuff_effect > 0) str += " effect = " + df2.format(debuff_effect);
                System.out.println(str + " chance = " + df2.format(hit_chance * 100));
            }

        } else {
            if (log.contains("debuff_applied")) {
                String str = debuff_name + " was resisted by " + name;
                System.out.println(str + " chance = " + df2.format(hit_chance * 100));
            }
        }
    }

    public void gainExp(double value) {
        if (enabled) {
            exp += value;
            double need = need_for_lvl(lvl);
            if (exp >= need && lvl < owner.max_skill_lvl) {
                lvl++;
                exp -= need;
                setSkill(lvl, skillMod);
            }
        }
    }

    public double need_for_lvl(int lvl) {
        return ((Math.pow(max(lvl, 1), 2)) * 1000);
    }

    public double average_hit_chance() {
        return attacks_total > 0 ? hit_chance_sum / attacks_total : 0;
    }

    public double average_dmg() {
        return hits_total > 0 ? dmg_sum / hits_total : 0;
    }

    public double average_extra_dmg() {
        return hits_total > 0 ? extra_dmg_sum / hits_total : 0;
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
        extra_dmg_sum = 0;
        debuff_chance_sum = 0;
        used_debuffed = 0;
    }

    public String getRecordedData(int simulations) {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" used: ").append(df4.format((double) used / simulations));
        if (hit == 0 && !name.equals("Prayer")) {
            switch (name) {
                case "Counter Strike", "Counter Dodge" -> {
                    sb.append("; dmg: ").append((int) average_dmg());
                    sb.append("; mana used: ").append((int) mana_used / simulations);
                    sb.append("\n");
                }
                default -> {
                    sb.append("; mana used: ").append((int) mana_used / simulations);
                    sb.append("\n");
                }
            }
        } else {
            sb.append("; hit: ").append(df2.format(average_hit_chance() * 100)).append("%");
            sb.append("; dmg: ").append((int) average_dmg());
            if (extra_dmg_sum > 0) {
                sb.append("; extra: ").append((int) average_extra_dmg());
            }
            sb.append("; mana used: ").append((int) mana_used / simulations);
            if (debuff_name != null) {
                sb.append("; debuff chance: ").append((int) (average_debuff_chance() * 100)).append("%");
            }
            if (used_debuffed != 0) {
                sb.append("; used debuffed: ").append(df2.format((double) used_debuffed / attacks_total));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public String getWeakRecordedData(int simulations) {
        return name + " used: " + df4.format((double) used / simulations) + "; hit: " + df2.format(average_hit_chance() * 100) + "%" +
                "; dmg: " + (int) average_dmg() + "\n";
    }

    public boolean isSingleTarget() {
        return (!aoe && !random_targets);
    }
}
