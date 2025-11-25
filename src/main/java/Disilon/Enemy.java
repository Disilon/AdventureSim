package Disilon;

import java.util.ArrayList;

import static Disilon.Main.random;

public class Enemy extends Actor {
    double strength = 1;
    double base_lvl;
    boolean active = false;
    String previous_spell = "";

    public Enemy() {
        super();
        poison_mult = 13.0 / 15.0;
    }

    public void resetStats() {
        skills.disableAll();
        counter_dodge = false;
        counter_heal = false;
        base_lvl = 0;
        fire_res = 0;
        water_res = 0;
        wind_res = 0;
        earth_res = 0;
        light_res = 0;
        dark_res = 0;
        phys_res = 0;
        magic_res = 0;
        base_hp_max = 0;
        base_exp = 0;
        base_atk = 0;
        base_def = 0;
        base_int = 0;
        base_res = 0;
        base_hit = 0;
        base_speed = 0;
        base_fire = 0;
        base_water = 0;
        base_wind = 0;
        base_earth = 0;
        base_light = 0;
        base_dark = 0;
        dodge_mult = 1;
    }

    public void makeSquirrel(int lvl) {
        name = "Squirrel Mage";
        resetStats();
        base_lvl = lvl;
        base_hp_max = lvl * 500 / base_lvl;
        base_exp = lvl * 4000 / base_lvl;
        base_atk = Math.pow(lvl, 0.9) * 5 / base_lvl;
        base_def = Math.pow(lvl, 0.9) * 10 / base_lvl;
        base_int = Math.pow(lvl, 0.95) * 10 / base_lvl;
        base_res = Math.pow(lvl, 0.9) * 10 / base_lvl;
        base_hit = Math.pow(lvl, 0.95) * 10 / base_lvl;
        base_speed = Math.pow(lvl, 0.95) * 25 / base_lvl;
        skills.enableActive("Elemental Blast");
        active_skills.get("Elemental Blast").setSkill(10, SkillMod.Basic);
        skills.enableActive("Flee");
//        passives.get("Stealth").enabled = true;
//        passives.get("Stealth").setLvl(20);
        active = true;
    }

    public void setEnemy(String name) {
        this.name = name;
        resetStats();
        switch (name) {
            case "Dagon" -> {
                base_lvl = 100;
                if (Main.game_version < 1541) {
                    base_hp_max = 16000 / base_lvl;
                } else {
                    base_hp_max = 22000 / base_lvl;
                }
                base_exp = 13200 / base_lvl;
                base_atk = 1100 / base_lvl;
                base_def = 2000 / base_lvl;
                base_int = 1000 / base_lvl;
                base_res = 2000 / base_lvl;
                if (Main.game_version < 1541) {
                    base_hit = 400 / base_lvl;
                } else {
                    base_hit = 350 / base_lvl;
                }
                base_speed = 600 / base_lvl;
                base_water = 400 / base_lvl;
                fire_res = 0.5;
                wind_res = 0.5;
                skills.enableActive("Water Punch");
                skills.enableActive("Killing Strike");
                skills.enableActive("Tsunami");
            }
            case "Lamia" -> {
                base_lvl = 100;
                base_hp_max = 48000 / base_lvl;
                base_exp = 14400 / base_lvl;
                base_atk = 500 / base_lvl;
                base_def = 1200 / base_lvl;
                base_int = 1100 / base_lvl;
                base_res = 1100 / base_lvl;
                base_hit = 1200 / base_lvl;
                base_speed = 500 / base_lvl;
                base_fire = 400 / base_lvl;
                earth_res = 0.5;
                wind_res = 0.5;
                skills.enableActive("Fire Ball");
                skills.enableActive("Fire Pillar");
                skills.enableActive("Explosion");
            }
            case "Shax" -> {
                base_lvl = 100;
                base_hp_max = 19200 / base_lvl;
                if (Main.game_version < 1541) {
                    base_exp = 9200 / base_lvl;
                } else {
                    if (Main.game_version < 1566) {
                        base_exp = 10500 / base_lvl;
                    } else {
                        base_exp = 11500 / base_lvl;
                    }
                }
                base_atk = 1100 / base_lvl;
                base_def = 600 / base_lvl;
                base_int = 1000 / base_lvl;
                base_res = 1100 / base_lvl;
                base_hit = 1200 / base_lvl;
                base_speed = 3500 / base_lvl;
                base_wind = 100 / base_lvl;
                earth_res = 0.5;
                wind_res = -0.5;
                skills.enableActive("Gust");
                skills.enableActive("Air Compression");
                counter_dodge = true;
                counter_heal = true;
            }
            case "Tyrant" -> {
                base_lvl = 125;
                base_hp_max = 26000 / base_lvl;
                base_exp = 20000 / base_lvl;
                base_atk = 1000 / base_lvl;
                base_def = 5000 / base_lvl;
                if (Main.game_version < 1566) {
                    base_int = 300 / base_lvl;
                } else {
                    base_int = 375 / base_lvl;
                }
                base_res = 5000 / base_lvl;
                base_hit = 1500 / base_lvl;
                base_speed = 625 / base_lvl;
                magic_res = 0.5;
                skills.enableActive("Soul Slash");
            }
            case "Fairy" -> {
                base_lvl = 150;
                if (Main.game_version < 1568) {
                    base_hp_max = 57000 / base_lvl;
                    base_exp = 45000 / base_lvl;
                } else {
                    base_hp_max = 120000 / base_lvl;
                    base_exp = 70500 / base_lvl;
                }
                base_atk = 2250 / base_lvl;
                base_def = 4500 / base_lvl;
                base_int = 2250 / base_lvl;
                base_res = 2250 / base_lvl;
                base_hit = 11250 / base_lvl;
                base_speed = 750 / base_lvl;
                active_skills.get("Charge Up").setSkill(10, SkillMod.Basic);
                skills.enableActive("Charge Up");
                skills.enableActive("Arrow Of Light");
            }
            case "Raum" -> {
                base_lvl = 175;
                if (Main.game_version < 1568) {
                    base_hp_max = 87500 / base_lvl;
                } else {
                    base_hp_max = 100625 / base_lvl;
                }
                base_exp = 38850 / base_lvl;
                base_atk = 5600 / base_lvl;
                base_def = 700 / base_lvl;
                base_int = 700 / base_lvl;
                base_res = 2100 / base_lvl;
                base_hit = 3500 / base_lvl;
                base_speed = 1225 / base_lvl;
                if (Main.game_version < 1573) {
                    dark_res = 0.5;
                }
                active_skills.get("Rapid Stabs").setSkill(13, SkillMod.Damage);
                skills.enableActive("Rapid Stabs");
                counter_dodge = true;
                if (Main.game_version < 1580) {
                    counter_strike = 0.25;
                } else {
                    counter_strike = 0.3;
                }
            }
            case "Asura" -> {
                base_lvl = 200;
                base_hp_max = 150000 / base_lvl;
                if (Main.game_version >= 1587) {
                    base_exp = 50500 / base_lvl;
                } else {
                    base_exp = 42000 / base_lvl;
                }
                base_def = 1000 / base_lvl;
                base_int = 1600 / base_lvl;
                base_res = 1000 / base_lvl;
                base_hit = 1800 / base_lvl;
                if (Main.game_version >= 1566) {
                    base_atk = 1560 / base_lvl;
                    base_speed = 1700 / base_lvl;
                    phys_res = -0.2;
                    if (Main.game_version >= 1591) {
                        fire_res = 0.25;
                    } else {
                        fire_res = 0.1;
                    }
                } else {
                    base_atk = 1700 / base_lvl;
                    base_speed = 1600 / base_lvl;
                    fire_res = 0.25;
                    phys_res = -0.3;
                }
                skills.enableActive("Holy Slash");
                skills.enableActive("Holy Power Slash");
                skills.enableActive("Sense");
            }
            case "Devil" -> {
                base_lvl = 90;
                base_hp_max = 10350 / base_lvl;
                base_exp = 2430 / base_lvl;
                base_atk = 900 / base_lvl;
                base_def = 495 / base_lvl;
                base_int = 630 / base_lvl;
                base_res = 270 / base_lvl;
                base_hit = 1080 / base_lvl;
                base_speed = 225 / base_lvl;
                base_dark = 180 / base_lvl;
                dark_res = 0.5;
                light_res = -0.5;
                skills.enableActive("Poison Attack");
                skills.enableActive("Dark Slash");
            }
            case "Tengu" -> {
                base_lvl = 50;
                base_hp_max = 3750 / base_lvl;
                base_exp = 450 / base_lvl;
                base_atk = 275 / base_lvl;
                base_def = 75 / base_lvl;
                base_int = 50 / base_lvl;
                base_res = 75 / base_lvl;
                base_hit = 250 / base_lvl;
                base_speed = 325 / base_lvl;
                base_wind = 100 / base_lvl;
                wind_res = 1;
                dodge_mult = 1.25;
                skills.enableActive("Bash");
                skills.enableActive("Double Attack");
            }
            case "Amon" -> {
                base_lvl = 50;
                base_hp_max = 2750 / base_lvl;
                base_exp = 450 / base_lvl;
                base_atk = 50 / base_lvl;
                base_def = 300 / base_lvl;
                base_int = 250 / base_lvl;
                base_res = 300 / base_lvl;
                base_hit = 250 / base_lvl;
                base_speed = 100 / base_lvl;
                base_water = 100 / base_lvl;
                water_res = 1;
                skills.enableActive("Magic Missile");
                skills.enableActive("Elemental Blast");
            }
            case "Akuma" -> {
                base_lvl = 50;
                base_hp_max = 4000 / base_lvl;
                base_exp = 750 / base_lvl;
                base_atk = 450 / base_lvl;
                base_def = 175 / base_lvl;
                base_int = 450 / base_lvl;
                base_res = 175 / base_lvl;
                base_hit = 350 / base_lvl;
                base_speed = 200 / base_lvl;
                base_fire = 100 / base_lvl;
                fire_res = 0.5;
                skills.enableActive("Dragon Punch");
                skills.enableActive("Aura Shot");
            }
            case "Dummy" -> {
                base_lvl = 100;
                base_hp_max = 400000 / base_lvl;
                base_exp = 100000 / base_lvl;
                base_atk = 600 / base_lvl;
                base_def = 600 / base_lvl;
                base_int = 600 / base_lvl;
                base_res = 600 / base_lvl;
                base_hit = 600 / base_lvl;
                base_speed = 600 / base_lvl;
                skills.enableActive("Cupid Hard Love Shot");
            }
            case "Astaroth" -> {
                base_lvl = 40;
                base_hp_max = 1800 / base_lvl;
                base_exp = 180 / base_lvl;
                base_atk = 120 / base_lvl;
                base_def = 60 / base_lvl;
                base_int = 60 / base_lvl;
                base_res = 100 / base_lvl;
                base_hit = 120 / base_lvl;
                base_speed = 100 / base_lvl;
                base_fire = 60 / base_lvl;
                fire_res = 0.6;
                skills.enableActive("Tsunami");
                skills.enableActive("Blind Enemy");
                skills.enableActive("Poison Attack");
            }
            case "Shinigami" -> {
                base_lvl = 40;
                base_hp_max = 2000 / base_lvl;
                base_exp = 180 / base_lvl;
                base_atk = 140 / base_lvl;
                base_def = 60 / base_lvl;
                base_int = 40 / base_lvl;
                base_res = 120 / base_lvl;
                base_hit = 120 / base_lvl;
                base_speed = 60 / base_lvl;
                base_dark = 60 / base_lvl;
                dark_res = 0.6;
                skills.enableActive("Killing Strike");
                skills.enableActive("Back Stab");
            }
            case "Wraith" -> {
                base_lvl = 30;
                base_hp_max = 1500 / base_lvl;
                base_exp = 120 / base_lvl;
                base_atk = 105 / base_lvl;
                base_def = 90 / base_lvl;
                base_int = 45 / base_lvl;
                base_res = 5 / base_lvl;
                base_hit = 90 / base_lvl;
                base_speed = 75 / base_lvl;
                base_wind = 45 / base_lvl;
                wind_res = 0.6;
                light_res = -0.5;
                skills.enableActive("Attack");
                skills.enableActive("Mark Target");
            }
            case "Ghoul2" -> {
                base_lvl = 30;
                base_hp_max = 1950 / base_lvl;
                base_exp = 120 / base_lvl;
                base_atk = 105 / base_lvl;
                base_def = 75 / base_lvl;
                base_int = 30 / base_lvl;
                base_res = 30 / base_lvl;
                base_hit = 60 / base_lvl;
                base_speed = 45 / base_lvl;
                light_res = -0.5;
                skills.enableActive("Charge Attack");
            }
            case "Slime" -> {
                base_lvl = 1;
                base_hp_max = 18 / base_lvl;
                base_exp = 3 / base_lvl;
                base_atk = 3 / base_lvl;
                base_def = 2 / base_lvl;
                base_int = 3 / base_lvl;
                base_res = 7 / base_lvl;
                base_hit = 5 / base_lvl;
                base_speed = 2 / base_lvl;
                water = 2;
                skills.enableActive("Water Punch");
            }
            case "Slime2" -> {
                base_lvl = 10;
                base_hp_max = 250 / base_lvl;
                base_exp = 30 / base_lvl;
                base_atk = 15 / base_lvl;
                base_def = 10 / base_lvl;
                base_int = 15 / base_lvl;
                base_res = 35 / base_lvl;
                base_hit = 25 / base_lvl;
                base_speed = 10 / base_lvl;
                water = 10;
                skills.enableActive("Water Punch");
            }
            case "Imp" -> {
                base_lvl = 10;
                base_hp_max = 300 / base_lvl;
                base_exp = 25 / base_lvl;
                base_atk = 10 / base_lvl;
                base_def = 35 / base_lvl;
                base_int = 25 / base_lvl;
                base_res = 15 / base_lvl;
                base_hit = 20 / base_lvl;
                base_speed = 15 / base_lvl;
                base_fire = 10 / base_lvl;
                skills.enableActive("Fire Ball");
            }
            case "Goblin" -> {
                base_lvl = 10;
                base_hp_max = 350 / base_lvl;
                base_exp = 30 / base_lvl;
                base_atk = 25 / base_lvl;
                base_def = 20 / base_lvl;
                base_int = 10 / base_lvl;
                base_res = 15 / base_lvl;
                base_hit = 20 / base_lvl;
                base_speed = 25 / base_lvl;
                skills.enableActive("Poison Attack");
                skills.enableActive("Quick Hit");
            }
            case "Imp2" -> {
                base_lvl = 20;
                base_hp_max = 600 / base_lvl;
                base_exp = 50 / base_lvl;
                base_atk = 20 / base_lvl;
                base_def = 70 / base_lvl;
                base_int = 50 / base_lvl;
                base_res = 30 / base_lvl;
                base_hit = 40 / base_lvl;
                base_speed = 30 / base_lvl;
                base_fire = 20 / base_lvl;
                skills.enableActive("Fire Ball");
            }
            case "Goblin2" -> {
                base_lvl = 20;
                base_hp_max = 700 / base_lvl;
                base_exp = 60 / base_lvl;
                base_atk = 50 / base_lvl;
                base_def = 40 / base_lvl;
                base_int = 20 / base_lvl;
                base_res = 30 / base_lvl;
                base_hit = 40 / base_lvl;
                base_speed = 50 / base_lvl;
                skills.enableActive("Poison Attack");
                skills.enableActive("Quick Hit");
            }
            case "Ghoul" -> {
                base_lvl = 20;
                base_hp_max = 1300 / base_lvl;
                base_exp = 80 / base_lvl;
                base_atk = 70 / base_lvl;
                base_def = 50 / base_lvl;
                base_int = 20 / base_lvl;
                base_res = 20 / base_lvl;
                base_hit = 40 / base_lvl;
                base_speed = 30 / base_lvl;
                light_res = -0.5;
                skills.enableActive("Charge Attack");
            }
        }
        active = true;
    }

    public void reroll(double hp_mult, double stats_mult) {
        int lvl = (int) Math.round(base_lvl * strength);
        this.hp_max = base_hp_max * lvl * hp_mult;
        this.hp = this.hp_max;
//        System.out.println("HP=" + hp);
        this.exp = base_exp * lvl;
        this.atk = base_atk * lvl * stats_mult;
        this.def = base_def * lvl * stats_mult;
        this.intel = base_int * lvl * stats_mult;
        this.resist = base_res * lvl * stats_mult;
        this.hit = base_hit * lvl * stats_mult;
        this.speed = base_speed * lvl * stats_mult;

        this.fire = base_fire * lvl * stats_mult;
        this.water = base_water * lvl * stats_mult;
        this.wind = base_wind * lvl * stats_mult;
        this.earth = base_earth * lvl * stats_mult;
        this.light = base_light * lvl * stats_mult;
        this.dark = base_dark * lvl * stats_mult;

        this.buffs.clear();
        this.debuffs.clear();
        this.casting = null;
        this.remove_charge = false;
        stun_time = 0;
        check_buffs();
        check_debuffs();
    }

    public void rollStrength() {
        strength = (random.nextInt(21) + 90) / 100.0;
    }

    public ActiveSkill rollAttack(Player player) {
        double roll = random.nextDouble() * 100;
        switch (name) {
            case "Dagon" -> {
                return roll < 50 ? active_skills.get("Water Punch") : roll >= 50 && roll < 80 ? active_skills.get("Killing Strike") : active_skills.get("Tsunami");
            }
            case "Lamia" -> {
                return roll < 50 ? active_skills.get("Fire Ball") : roll >= 50 && roll < 85 ? active_skills.get("Fire Pillar") : active_skills.get("Explosion");
            }
            case "Shax" -> {
                return roll < 30 ? active_skills.get("Air Compression") : active_skills.get("Gust"); //Hadn't measured chances, I don't think it matters
            }
            case "Devil" -> {
                return roll < 70 ? active_skills.get("Dark Slash") : active_skills.get("Poison Attack"); //Chances to select skills were calculated empirically
            }
            case "Tengu" -> {
                return roll < 50 ? active_skills.get("Bash") : active_skills.get("Double Attack");
            }
            case "Amon" -> {
                return roll < 50 ? active_skills.get("Magic Missile") : active_skills.get("Elemental Blast");
            }
            case "Akuma" -> {
                return roll < 50 ? active_skills.get("Dragon Punch") : active_skills.get("Aura Shot");
            }
            case "Fairy" -> {
                return (charge == 0 && roll < 60) ? active_skills.get("Charge Up") : active_skills.get("Arrow Of Light");
            }
            case "Asura" -> {
                if (player.hide_bonus > 0) {
                    return active_skills.get("Sense");
                } else {
                    return roll < 30 ? active_skills.get("Holy Power Slash") : active_skills.get("Holy Slash");
                }
            }
            case "Dummy" -> {
                return active_skills.get("Cupid Hard Love Shot");
            }
            default -> {
                return getRandomSkill();
            }
        }
    }

    public ActiveSkill getRandomSkill() {
        ArrayList<ActiveSkill> enemy_skills = new ArrayList<>();
        for (String n : active_skills.keySet()) {
            if (active_skills.get(n).available) enemy_skills.add(active_skills.get(n));
        }
        if (enemy_skills.isEmpty()) return getWeakSkill();
        return enemy_skills.get(random.nextInt(enemy_skills.size()));
    }

    public ActiveSkill getCasting(Player player) {
        if (casting == null) {
            casting = rollAttack(player);
            previous_spell = casting.name;
        }
        return casting;
    }
}
