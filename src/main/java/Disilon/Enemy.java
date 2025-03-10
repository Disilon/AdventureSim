package Disilon;

import java.util.Random;

public class Enemy extends Actor {
    private Random random = new Random();
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

    ActiveSkill slash = new ActiveSkill("Dark Slash", 1, 135, 165, 1, 0, 0.9, 0.9, Scaling.atk, Element.dark,
            false, false);
    ActiveSkill poison = new ActiveSkill("Poison Attack", 1, 36, 44, 1, 0, 0.4, 0.9, Scaling.atk, Element.phys,
            false, false);

    double strength = 0.9;

    public Enemy() {
        ball.addDebuff("Burn", 3, 1);
        pillar.addDebuff("Burn", 3, 1);
        explosion.addDebuff("Burn", 3, 1);
        poison.addDebuff("Poison", 3, 0.1);
        tsunami.addDebuff("Poison", 3, 0.1);
    }

    public void setEnemy(String name) {
        this.name = name;
        enemy_skills.clear();
        counter_dodge = false;
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
        switch (name) {
            case "Dagon" -> {
                base_hp_max = 16000;
                base_exp = 13200;
                base_atk = 1100;
                base_def = 2000;
                base_int = 1000;
                base_res = 2000;
                base_hit = 400;
                base_speed = 600;
                base_water = 400;
                fire_res = 0.5;
                wind_res = 0.5;
                enemy_skills.add(waterpunch);
                enemy_skills.add(killingstrike);
                enemy_skills.add(tsunami);
            }
            case "Lamia" -> {
                base_hp_max = 48000;
                base_exp = 14400;
                base_atk = 500;
                base_def = 1200;
                base_int = 1100;
                base_res = 1100;
                base_hit = 1200;
                base_speed = 500;
                base_fire = 400;
                earth_res = 0.5;
                wind_res = 0.5;
                enemy_skills.add(ball);
                enemy_skills.add(pillar);
                enemy_skills.add(explosion);
            }
            case "Shax" -> {
                base_hp_max = 19200;
                base_exp = 9200;
                base_atk = 1100;
                base_def = 600;
                base_int = 1000;
                base_res = 1100;
                base_hit = 1200;
                base_speed = 3500;
                base_wind = 100;
                earth_res = 0.5;
                wind_res = -0.5;
                enemy_skills.add(gust);
                enemy_skills.add(compression);
                counter_dodge = true;
            }
            case "Devil" -> {
                base_hp_max = 10350;
                base_exp = 2430;
                base_atk = 900;
                base_def = 495;
                base_int = 630;
                base_res = 270;
                base_hit = 1080;
                base_speed = 225;
                base_dark = 180;
                dark_res = 0.5;
                light_res = -0.5;
                enemy_skills.add(poison);
                enemy_skills.add(slash);
            }
        }
        reroll();
    }

    @Override
    public void reroll() {
        this.hp_max = base_hp_max * strength;
        this.hp = this.hp_max;
        this.exp = base_exp * strength;
        this.atk = base_atk * strength;
        this.def = base_def * strength;
        this.intel = base_int * strength;
        this.resist = base_res * strength;
        this.hit = base_hit * strength;
        this.speed = base_speed * strength;

        this.fire = base_fire * strength;
        this.water = base_water * strength;
        this.wind = base_wind * strength;
        this.earth = base_earth * strength;
        this.light = base_light * strength;
        this.dark = base_dark * strength;

        this.debuffs.clear();
        this.casting = null;
    }

    public void rollStrength() {
        strength = (this.random.nextInt(21) + 90) / 100.0;
    }

    public void incrementStrength() {
        strength += 0.01;
        if (strength > 1.1) strength = 0.9;
    }

    public ActiveSkill rollAttack() {
        double roll = this.random.nextDouble() * 100;
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
            default -> {
                return null;
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
