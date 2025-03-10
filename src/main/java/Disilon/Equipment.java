package Disilon;

import java.util.Map;

public class Equipment {
    String name;
    String slot;
    String displayName = "None";
    String quality;
    int upgrade = 0;

    // Stats
    double atk = 0;
    double def = 0;
    double intel = 0;
    double resist = 0;
    double hit = 0;
    double speed = 0;

    //Elements
    double fire = 0;
    double water = 0;
    double wind = 0;
    double earth = 0;
    double dark = 0;
    double light = 0;

    //Damage Mit
    double phys_res = 0;
    double magic_res = 0;
    double fire_res = 0;
    double water_res = 0;
    double wind_res = 0;
    double earth_res = 0;
    double light_res = 0;
    double dark_res = 0;

    //Special
    double burn = 0;
    double crit = 0;
    double stun = 0;

    Map equipStats;

    public Equipment(String name, String slot) {
        this.name = name;
        this.slot = slot;
        quality = "normal";
        upgrade = 0;
    }

    public Equipment(String name, String slot, Map equipStats) {
        this.name = name;
        this.slot = slot;
        quality = "normal";
        upgrade = 0;
        this.equipStats = equipStats;
        calcStats();
    }
    
    public void setQualityLvl(String quality, int lvl) {
        this.quality = quality;
        this.upgrade = lvl;
        calcStats();
    }

    public void calcStats() {
        if (equipStats == null) return;
        // Stats
        double mult = multiplier(quality, upgrade, 1);
        this.atk = equipStats.containsKey("ATK") ? (double) equipStats.get("ATK") * mult : 0;
        this.def = equipStats.containsKey("DEF") ? (double) equipStats.get("DEF") * mult : 0;
        this.intel = equipStats.containsKey("INT") ? (double) equipStats.get("INT") * mult : 0;
        this.resist = equipStats.containsKey("RES") ? (double) equipStats.get("RES") * mult : 0;
        this.hit = equipStats.containsKey("HIT") ? (double) equipStats.get("HIT") * mult : 0;
        this.speed = equipStats.containsKey("SPD") ? (double) equipStats.get("SPD") * mult : 0;

        // Elements
        this.fire = equipStats.containsKey("FIRE") ? (double) equipStats.get("FIRE") * mult : 0;
        this.water = equipStats.containsKey("WATER") ? (double) equipStats.get("WATER") * mult : 0;
        this.wind = equipStats.containsKey("WIND") ? (double) equipStats.get("WIND") * mult : 0;
        this.earth = equipStats.containsKey("EARTH") ? (double) equipStats.get("EARTH") * mult : 0;
        this.dark = equipStats.containsKey("DARK") ? (double) equipStats.get("DARK") * mult : 0;
        this.light = equipStats.containsKey("LIGHT") ? (double) equipStats.get("LIGHT") * mult : 0;

        //Damage Mit
        mult = multiplier(quality, upgrade, 2);
        this.phys_res = equipStats.containsKey("PHY_RES") ? (double) equipStats.get("PHY_RES") * mult : 0;
        this.magic_res = equipStats.containsKey("MAG_RES") ? (double) equipStats.get("MAG_RES") * mult : 0;
        this.fire_res = equipStats.containsKey("FIRE_RES") ? (double) equipStats.get("FIRE_RES") * mult : 0;
        this.water_res = equipStats.containsKey("WATER_RES") ? (double) equipStats.get("WATER_RES") * mult : 0;
        this.wind_res = equipStats.containsKey("WIND_RES") ? (double) equipStats.get("WIND_RES") * mult : 0;
        this.earth_res = equipStats.containsKey("EARTH_RES") ? (double) equipStats.get("EARTH_RES") * mult : 0;
        this.dark_res = equipStats.containsKey("DARK_RES") ? (double) equipStats.get("DARK_RES") * mult : 0;
        this.light_res = equipStats.containsKey("LIGHT_RES") ? (double) equipStats.get("LIGHT_RES") * mult : 0;

        // Special
        mult = 1.0;
        this.burn = equipStats.containsKey("BURN") ? (double) equipStats.get("BURN") * mult : 0;
        this.crit = equipStats.containsKey("CRIT") ? (double) equipStats.get("CRIT") * mult : 0;
        this.stun = equipStats.containsKey("STUN") ? (double) equipStats.get("STUN") * mult : 0;

        // Set name
        if (equipStats.containsKey("SET")) this.displayName = (String) equipStats.get("SET");
    }

    public static double multiplier(String quality, int upgrade, int scaling_type) {
        switch (scaling_type) {
            case 1 -> {
                return multiplier_from_tier(quality) * (1 + upgrade * 0.1);
            }
            case 2 -> {
                return (0.5 + multiplier_from_tier(quality) * 0.5) * (1 + upgrade * 0.025);
            }
            default -> {
                return 1;
            }
        }
    }

    public static double multiplier_from_tier(String quality) {
        return switch (quality.toLowerCase()) {
            case "poor" -> 0.5;
            case "flawed" -> 0.75;
            case "normal" -> 1;
            case "good" -> 1.25;
            case "superior" -> 1.5;
            case "exceptional" -> 2;
            case "divine" -> 2.5;
            case "legendary" -> 3;
            case "mythic" -> 4;
            case "godly" -> 5;
            default -> -1;
        };
    }
}
