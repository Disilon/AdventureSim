package Disilon;

import static Disilon.Main.random;

public class Enemy extends Actor {
    ActiveSkill waterpunch = new ActiveSkill("Water Punch", 1, 99, 121, 1, 0, 0.9, 0.9, Scaling.atkint, Element.water,
            false, false);
    ActiveSkill killingstrike = new ActiveSkill("Killing Strike", 1, 207.9, 254.1, 0.7, 0, 2, 2, Scaling.atk,
            Element.dark, false, false);
    ActiveSkill tsunami = new ActiveSkill("Tsunami", 1, 252, 308, 1, 0, 4, 6, Scaling.intel, Element.water, true, false);

    ActiveSkill ball = new ActiveSkill("Fire Ball", 1, 69.3, 84.7, 1.35, 0, 1.15, 1, Scaling.intel, Element.fire,
            false, false);
    ActiveSkill pillar = new ActiveSkill("Fire Pillar", 1, 126, 154, 1, 0, 1.5, 1.5, Scaling.intel,
            Element.fire, false, false);
    ActiveSkill explosion = new ActiveSkill("Explosion", 1, 945, 1155, 1.15, 0, 8, 30, Scaling.intel, Element.fire, true, false);

    ActiveSkill gust = new ActiveSkill("Gust", 1, 56.7, 69.3, 1, 0, 0.8, 0.8, Scaling.intel, Element.wind,
            false, false);
    ActiveSkill compression = new ActiveSkill("Air Compression", 1, 91.35, 111.65, 1, 0, 1.2, 1.2, Scaling.intel,
            Element.wind,
            false, false);

    ActiveSkill soulslash = new ActiveSkill("Soul Slash", 1, 2250, 2750, 2, 0, 0.3, 75, Scaling.atk,
            Element.phys, false, false);

    ActiveSkill slash = new ActiveSkill("Dark Slash", 1, 135, 165, 1, 0, 0.9, 0.9, Scaling.atk, Element.dark,
            false, false);
    ActiveSkill poison = new ActiveSkill("Poison Attack", 1, 36, 44, 1, 0, 0.4, 0.9, Scaling.atk, Element.phys,
            false, false);

    ActiveSkill dp = new ActiveSkill("Dragon Punch", 3, 76.5, 93.5, 0.8, 0, 1, 3, Scaling.atk, Element.phys,
            false, false);
    ActiveSkill as = new ActiveSkill("Aura Shot", 1, 112.5, 137.5, 1, 0, 0.9, 0.9, Scaling.atkint, Element.fire,
            false, false);

    ActiveSkill eblast = new ActiveSkill("Elemental Blast", 1, 81.9, 100.1, 1.0, 0, 1.2, 1.2, Scaling.intel,
            Element.magic, false, false);
    ActiveSkill mm = new ActiveSkill("Magic Missile", 1, 103.95, 127.05, 1.5, 0, 1.5, 1.5, Scaling.intel,
            Element.magic, false, false);

    ActiveSkill bash = new ActiveSkill("Bash", 1, 103.5, 126.5, 1, 0, 1.2, 1.1, Scaling.atk, Element.phys, false,
            false);
    ActiveSkill da = new ActiveSkill("Double Attack", 2, 64.8, 79.2, 1, 0, 1, 1, Scaling.atk, Element.phys, false,
            false);

    ActiveSkill cupid_atkint = new ActiveSkill("Cupid Hard Love Shot", 1, 189, 231, 1.75, 0, 1.4, 1.4, Scaling.atkint, Element.physmagic,
            false, false);

    ActiveSkill blind = new ActiveSkill("Blind", 1, 76.5, 93.5, 1, 0, 1, 1, Scaling.atk, Element.phys,
            false, false);
    ActiveSkill backstab = new ActiveSkill("Back Stab", 1, 225, 275, 2, 0, 2, 2, Scaling.atk, Element.phys,
            false, false);
    ActiveSkill attack = new ActiveSkill("Attack", 1, 90, 110, 1, 0, 1, 1, Scaling.atk, Element.phys,
            false, false);
    ActiveSkill charge = new ActiveSkill("Charge Attack", 1, 270, 330, 1, 0, 2.5, 2, Scaling.atk, Element.phys,
            false, false);
    ActiveSkill mark = new ActiveSkill("Mark Target", 1, 0, 0, 1.5, 10, 0.5, 0.5, Scaling.atk, Element.none,
            false,false);

    double strength = 1;
    double base_lvl;
    double core_mult; //currently unused

    public Enemy() {
        ball.addDebuff("Burn", 3, 1);
        pillar.addDebuff("Burn", 3, 1);
        explosion.addDebuff("Burn", 3, 1);
        poison.addDebuff("Poison", 3, 0.1);
        tsunami.addDebuff("Poison", 3, 0.1);
        bash.addDebuff("Defense Break", 3, 0.2);
        mm.addDebuff("Resist Break", 3, 0.25);
        blind.addDebuff("Smoke", 3, 0);
        mark.addDebuff("Mark", 1, 0.2);
    }

    public void setEnemy(String name) {
        this.name = name;
        enemy_skills.clear();
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
                enemy_skills.add(waterpunch);
                enemy_skills.add(killingstrike);
                enemy_skills.add(tsunami);
                core_mult = 45;
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
                enemy_skills.add(ball);
                enemy_skills.add(pillar);
                enemy_skills.add(explosion);
                core_mult = 50;
            }
            case "Shax" -> {
                base_lvl = 100;
                base_hp_max = 19200 / base_lvl;
                if (Main.game_version < 1541) {
                    base_exp = 9200 / base_lvl;
                } else {
                    base_exp = 10500 / base_lvl;
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
                enemy_skills.add(gust);
                enemy_skills.add(compression);
                counter_dodge = true;
                counter_heal = true;
                core_mult = 40;
            }
            case "Tyrant" -> {
                base_lvl = 100;
                base_hp_max = 26000 / base_lvl;
                base_exp = 20000 / base_lvl;
                base_atk = 1000 / base_lvl;
                base_def = 5000 / base_lvl;
                base_int = 300 / base_lvl;
                base_res = 5000 / base_lvl;
                base_hit = 1500 / base_lvl;
                base_speed = 600 / base_lvl;
                magic_res = 0.5;
                enemy_skills.add(soulslash);
                core_mult = 100;
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
                enemy_skills.add(poison);
                enemy_skills.add(slash);
                core_mult = 18;
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
                enemy_skills.add(bash);
                enemy_skills.add(da);
                core_mult = 10;
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
                enemy_skills.add(mm);
                enemy_skills.add(eblast);
                core_mult = 10;
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
                enemy_skills.add(dp);
                enemy_skills.add(as);
                core_mult = 12;
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
                enemy_skills.add(cupid_atkint);
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
                enemy_skills.add(blind);
                enemy_skills.add(poison);
                core_mult = 9;
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
                enemy_skills.add(killingstrike);
                enemy_skills.add(backstab);
                core_mult = 9;
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
                enemy_skills.add(attack);
                enemy_skills.add(mark);
                core_mult = 8;
            }
            case "Ghoul" -> {
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
                enemy_skills.add(charge);
                core_mult = 8;
            }
        }
        //reroll();
    }

    @Override
    public void reroll() {
        int lvl = (int) Math.round(base_lvl * strength);
        this.hp_max = base_hp_max * lvl;
        this.hp = this.hp_max;
        this.exp = base_exp * lvl;
        this.atk = base_atk * lvl;
        this.def = base_def * lvl;
        this.intel = base_int * lvl;
        this.resist = base_res * lvl;
        this.hit = base_hit * lvl;
        this.speed = base_speed * lvl;

        this.fire = base_fire * lvl;
        this.water = base_water * lvl;
        this.wind = base_wind * lvl;
        this.earth = base_earth * lvl;
        this.light = base_light * lvl;
        this.dark = base_dark * lvl;

        this.debuffs.clear();
        this.casting = null;
    }

    public void rollStrength() {
        strength = (random.nextInt(21) + 90) / 100.0;
    }

    public ActiveSkill rollAttack() {
        double roll = random.nextDouble() * 100;
        switch (name) {
            case "Dagon" -> {
                return roll < 50 ? waterpunch : roll >= 50 && roll < 80 ? killingstrike : tsunami;
            }
            case "Lamia" -> {
                return roll < 50 ? ball : roll >= 50 && roll < 85 ? pillar : explosion;
            }
            case "Shax" -> {
                return roll < 30 ? compression : gust; //Hadn't measured chances, I don't think it matters
            }
            case "Devil" -> {
                return roll < 70 ? slash : poison; //Chances to select skills were calculated empirically
            }
            case "Tengu" -> {
                return roll < 50 ? bash : da;
            }
            case "Amon" -> {
                return roll < 50 ? mm : eblast;
            }
            case "Akuma" -> {
                return roll < 50 ? dp : as;
            }
            case "Dummy" -> {
                return cupid_atkint;
            }
            default -> {
                return enemy_skills.get(random.nextInt(enemy_skills.size()));
            }
        }
    }

    @Override
    public ActiveSkill getCasting() {
        if (casting == null) {
            casting = rollAttack();
        }
        return casting;
    }
}
