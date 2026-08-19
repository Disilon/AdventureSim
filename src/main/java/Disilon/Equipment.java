package Disilon;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static Disilon.Main.addToMap;

public class Equipment {
    String name;
    String slot;
    String displayName = "None";
    Quality quality;
    int upgrade = 0;
    int skill_required = 1;
    boolean mh_only = false;

    // Stats
    double atk = 0;
    double def = 0;
    double intel = 0;
    double resist = 0;
    double hit = 0;
    double speed = 0;
    double hp = 0;

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
    double TF = 0;
    double analyze = 0;
    double barrier = 0;
    double potion = 0;
    double dodge = 0;

    LinkedHashMap<String,Integer> mats;

    Map equipStats;

    public Equipment(String name, String slot) {
        this.name = name;
        this.slot = slot;
        quality = Quality.Normal;
        upgrade = 0;
        mats = new LinkedHashMap<>();
    }

    public Equipment(String name, String slot, Map equipStats) {
        this.name = name;
        this.slot = slot;
        quality = Quality.Normal;
        upgrade = 0;
        mats = new LinkedHashMap<>();
        this.equipStats = equipStats;
        calcStats();
    }

    public Equipment clone() {
        Equipment clone = new Equipment(this.name, this.slot);
        clone.quality = this.quality;
        clone.upgrade = this.upgrade;
        clone.equipStats = this.equipStats;
        clone.atk = this.atk;
        clone.def = this.def;
        clone.intel = this.intel;
        clone.resist = this.resist;
        clone.hit = this.hit;
        clone.speed = this.speed;
        clone.hp = this.hp;
        clone.fire = this.fire;
        clone.water = this.water;
        clone.wind = this.wind;
        clone.earth = this.earth;
        clone.dark = this.dark;
        clone.light = this.light;
        clone.phys_res = this.phys_res;
        clone.magic_res = this.magic_res;
        clone.fire_res = this.fire_res;
        clone.water_res = this.water_res;
        clone.wind_res = this.wind_res;
        clone.earth_res = this.earth_res;
        clone.light_res = this.light_res;
        clone.dark_res = this.dark_res;
        clone.burn = this.burn;
        clone.crit = this.crit;
        clone.stun = this.stun;
        clone.TF = this.TF;
        clone.analyze = this.analyze;
        clone.barrier = this.barrier;
        clone.potion = this.potion;
        clone.dodge = this.dodge;
        clone.mats = new LinkedHashMap<>(this.mats);
        return clone;
    }
    
    public void setQualityLvl(Quality quality, int lvl) {
        this.quality = quality;
        this.upgrade = lvl;
        calcStats();
    }

    public boolean hasSpecial() {
        if (burn > 0) return true;
        if (crit > 0) return true;
        if (stun > 0) return true;
        if (TF > 0) return true;
        if (analyze > 0) return true;
        if (barrier > 0) return true;
        if (potion > 0) return true;
        if (dodge > 0) return true;
        return false;
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
        this.hp = equipStats.containsKey("HP") ? (double) equipStats.get("HP") * mult : 0;

        if (Main.game_version >= 1566) { //temporary support for multiversion gear stats
            this.atk = equipStats.containsKey("ATK_NEW") ? (double) equipStats.get("ATK_NEW") * mult : this.atk;
            this.def = equipStats.containsKey("DEF_NEW") ? (double) equipStats.get("DEF_NEW") * mult : this.def;
            this.intel = equipStats.containsKey("INT_NEW") ? (double) equipStats.get("INT_NEW") * mult : this.intel;
            this.resist = equipStats.containsKey("RES_NEW") ? (double) equipStats.get("RES_NEW") * mult : this.resist;
            this.hit = equipStats.containsKey("HIT_NEW") ? (double) equipStats.get("HIT_NEW") * mult : this.hit;
            this.speed = equipStats.containsKey("SPD_NEW") ? (double) equipStats.get("SPD_NEW") * mult : this.speed;
            this.hp = equipStats.containsKey("HP_NEW") ? (double) equipStats.get("HP_NEW") * mult : this.hp;
        }

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
        mult = multiplier(quality, upgrade, 3);
        this.burn = equipStats.containsKey("BURN") ? (double) equipStats.get("BURN") * mult : 0;
        this.crit = equipStats.containsKey("CRIT") ? (double) equipStats.get("CRIT") * mult : 0;
        this.stun = equipStats.containsKey("STUN") ? (double) equipStats.get("STUN") * mult : 0;
        this.TF = equipStats.containsKey("TF") ? (double) equipStats.get("TF") * mult : 0;
        this.analyze = equipStats.containsKey("ANALYZE") ? (double) equipStats.get("ANALYZE") * mult : 0;
        this.barrier = equipStats.containsKey("BARRIER") ? (double) equipStats.get("BARRIER") * mult : 0;
        this.potion = equipStats.containsKey("POTION") ? (double) equipStats.get("POTION") * mult : 0;
        this.dodge = equipStats.containsKey("DODGE") ? (double) equipStats.get("DODGE") * mult : 0;

        // Set name
        if (equipStats.containsKey("SET")) {
            this.displayName = (String) equipStats.get("SET");
            skill_required = switch (displayName) {
                case "Iron","Leather" -> 10;
                case "Blazing","Earthen","Holy","Windy","Dark","Bronze" -> 20;
                case "Training","Aquatic","Hunter","Poison" -> 35;
                case "HolyDmg","Ninja" -> 50;
                default -> 1;
            };
        }

        if (equipStats.containsKey("LVL_REQUIRED")) this.skill_required = (int) (double) equipStats.get(
                "LVL_REQUIRED");

        // If true, then can't be equipped in offhand slot
        if (equipStats.containsKey("MH_ONLY")) this.mh_only = (boolean) equipStats.get("MH_ONLY");

        if (equipStats.containsKey("MATS")) {
            String line = (String) equipStats.get("MATS");
            String[] pairs = line.split(";");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim();
                    int value = Integer.parseInt(keyValue[1].trim());
                    mats.put(key, value);
                }
            }
        }
    }

    public static double multiplier(Quality quality, int upgrade, int scaling_type) {
        switch (scaling_type) {
            case 1 -> {
                return quality.getMult() * (1 + upgrade * 0.1);
            }
            case 2 -> {
                return (0.5 + quality.getMult() * 0.5) * (1 + upgrade * 0.025);
            }
            case 3 -> {
                if (Main.game_version < 1658) {
                    return 1 + upgrade * 0.05 * (0.5 + quality.getMult() * 0.5);
                } else {
                    return 0.6 * (1 + upgrade * 0.1) * (0.5 + quality.getMult() * 0.5);
                }
            }
            default -> {
                return 1;
            }
        }
    }

    public enum Quality {
        Poor(0.5),
        Flawed(0.75),
        Normal(1),
        Good(1.25),
        Superior(1.5),
        Exceptional(2),
        Divine(2.5),
        Legendary(3),
        Mythic(4),
        Godly(5);

        private final double mult;
        Quality(double mult) {
            this.mult = mult;
        }

        public double getMult() {
            return mult;
        }
    }

    public static double costDivisor(String mat) {
        return switch (mat) {
            case "Heat","IronBar","BronzeBar","CobaltBar","GoldBar" -> 2;
            case "Paper" -> 2;
            case "Beech" -> 10;
            case "Oak" -> 15;
            case "Teak" -> 15;
            default -> 1;
        };
    }

    public void getUpgradeMats(LinkedHashMap<String,HashMap<String,Double>> map, Player p) {
        double eqm = 1 + 0.01 * p.research_lvls.get("E. Quality mult").intValue();
        eqm *= 1 + 0.01 * p.research_lvls.get("E. Quality min").intValue();
        double ref_c = eqm;
        double ref_s = eqm;
        ref_c *= 1 + 0.01 * p.passives.get("Crafting").lvl;
        ref_s *= 1 + 0.01 * p.passives.get("Smithing").lvl;
        double c_spd = p.getCraftingSpeed();
        double s_spd = p.getSmithingSpeed();
        double a_spd = p.getAlchemySpeed();
        double side_craft_spd = p.getSidecraftingSpeed();
        if (side_craft_spd > 0) {
            c_spd *= side_craft_spd;
            s_spd *= side_craft_spd;
            a_spd *= side_craft_spd;
        }
        double heat = 0;
        for (String mat : mats.keySet()) {
            double amount = 0;
            double u = mats.get(mat) / costDivisor(mat) / 10 * quality.getMult();
            for (int i = 0; i < upgrade; i++) {
                amount += Math.ceil(u * (i + 2));
            }
            switch (mat) {
                case "Leather" -> {
                    addToMap(map,"Refined Leather","amount", amount);
                    double m1 = amount / ref_c * 10;
                    addToMap(map,"Refined Leather","time", m1/10 * 40 / c_spd);
                    addToMap(map,"Leather","amount", m1);
                    addToMap(map,"Leather","time", m1 * 10 / c_spd);
                    addToMap(map,"Rough Hide","amount", m1*2);
                }
                case "SmoothLeather" -> {
                    addToMap(map,"Refined Smoothy","amount", amount);
                    double m1 = amount / ref_c * 10;
                    addToMap(map,"Refined Smoothy","time", m1/10 * 40 / c_spd);
                    addToMap(map,"Smooth Leather","amount", m1);
                    addToMap(map,"Smooth Leather","time", m1 * 10 / c_spd);
                    addToMap(map,"Smooth Hide","amount", m1*2);
                }
                case "SilkyCloth" -> {
                    addToMap(map,"Refined Silky","amount", amount);
                    double m1 = amount / ref_c * 10;
                    addToMap(map,"Refined Silky","time", m1/10 * 40 / c_spd);
                    addToMap(map,"Silky Cloth","amount", m1);
                    addToMap(map,"Silky Cloth","time", m1 * 10 / c_spd);
                    addToMap(map,"Feather","amount", m1*4);
                }
                case "FairyLeather" -> {
                    addToMap(map,"Refined Wing","amount", amount);
                    double m1 = amount / ref_c * 10;
                    addToMap(map,"Refined Wing","time", m1/10 * 120 / c_spd);
                    addToMap(map,"Fairy Leather","amount", m1);
                    addToMap(map,"Fairy Leather","time", m1 * 22 / c_spd);
                    addToMap(map,"Fairy Wing","amount", m1);
                }
                case "LizardLeather" -> {
                    addToMap(map,"Refined Lizard","amount", amount);
                    double m1 = amount / ref_c * 10;
                    addToMap(map,"Refined Lizard","time", m1/10 * 40 / c_spd);
                    addToMap(map,"Lizard Leather","amount", m1);
                    addToMap(map,"Lizard Leather","time", m1 * 10 / c_spd);
                    addToMap(map,"Lizard Skin","amount", m1*2);
                }
                case "PoisonLeather" -> {
                    addToMap(map,"R Poison Leather","amount", amount);
                    double m1 = amount / ref_c * 10;
                    addToMap(map,"R Poison Leather","time", m1/10 * 40 / c_spd);
                    addToMap(map,"Poison Leather","amount", m1);
                    addToMap(map,"Poison Leather","time", m1 * 20 / c_spd);
                    addToMap(map,"Lizard Skin","amount", m1*2);
                    addToMap(map,"Blood Stone","amount", m1);
                }
                case "EvilLeather" -> {
                    addToMap(map,"R Evil Leather","amount", amount);
                    double m1 = amount / ref_c * 10;
                    addToMap(map,"R Evil Leather","time", m1/10 * 45 / c_spd);
                    addToMap(map,"Evil Leather","amount", m1);
                    addToMap(map,"Evil Leather","time", m1 * 22 / c_spd);
                    addToMap(map,"Evil Hide","amount", m1*2);
                    addToMap(map,"Shadow Flower","amount", m1);
                }
                case "Paper" -> {
                    addToMap(map,"Enhanced Paper","amount", amount);
                    double m1 = amount / ref_c * 10;
                    addToMap(map,"Enhanced Paper","time", m1/10 * 40 / c_spd);
                    addToMap(map,"Paper","amount", m1);
                    int c_lvl = p.passives.get("Crafting").lvl;
                    double pc = 0;
                    if (c_lvl >= 35) {
                        pc = m1 / 4;
                        addToMap(map,"Teak","amount", pc);
                        addToMap(map,"Paper","time", pc * 10 / c_spd);
                    } else {
                        if (c_lvl >= 20) {
                            pc = m1 / 3;
                            addToMap(map,"Oak","amount", pc);
                            addToMap(map,"Paper","time", pc * 8 / c_spd);
                        } else {
                            pc = m1 / 2;
                            addToMap(map,"Beech","amount", pc);
                            addToMap(map,"Paper","time", pc * 6 / c_spd);
                        }
                    }
                }
                case "Beech" -> {
                    addToMap(map,"Enhanced Beech","amount", amount);
                    double m1 = amount / ref_c * 60;
                    addToMap(map,"Enhanced Beech","time", m1/60 * 250 / c_spd);
                    addToMap(map,"Beech","amount", m1);
                }
                case "Oak" -> {
                    addToMap(map,"Enhanced Oak","amount", amount);
                    double m1 = amount / ref_c * 60;
                    addToMap(map,"Enhanced Oak","time", m1/60 * 350 / c_spd);
                    addToMap(map,"Oak","amount", m1);
                }
                case "Teak" -> {
                    addToMap(map,"Enhanced Teak","amount", amount);
                    double m1 = amount / ref_c * 60;
                    addToMap(map,"Enhanced Teak","time", m1/60 * 500 / c_spd);
                    addToMap(map,"Teak","amount", m1);
                }
                case "IronBar" -> {
                    addToMap(map,"Refined Iron","amount", amount);
                    double m1 = amount / ref_c * 10;
                    addToMap(map,"Refined Iron","time", m1/10 * 40 / c_spd);
                    heat += m1/10*100;
                    addToMap(map,"Iron Bar","amount", m1);
                    addToMap(map,"Iron Bar","time", m1 * 13.5 / c_spd);
                    heat += m1 * 20;
                    addToMap(map,"Iron Ore","amount", m1*2);
                }
                case "BronzeBar" -> {
                    addToMap(map,"Refined Bronze","amount", amount);
                    double m1 = amount / ref_c * 15;
                    addToMap(map,"Refined Bronze","time", m1/15 * 60 / c_spd);
                    heat += m1/15*150;
                    addToMap(map,"Bronze Bar","amount", m1);
                    addToMap(map,"Bronze Bar","time", m1 * 17 / c_spd);
                    heat += m1 * 30;
                    addToMap(map,"Copper Ore","amount", m1*4);
                    addToMap(map,"Tin Ore","amount", m1);
                }
                case "CobaltBar" -> {
                    addToMap(map,"Refined Cobalt","amount", amount);
                    double m1 = amount / ref_c * 20;
                    addToMap(map,"Refined Cobalt","time", m1/20 * 80 / c_spd);
                    heat += m1/20*200;
                    addToMap(map,"Cobalt Bar","amount", m1);
                    addToMap(map,"Cobalt Bar","time", m1 * 20 / c_spd);
                    heat += m1 * 40;
                    addToMap(map,"Cobalt Ore","amount", m1*5);
                }
                case "GoldBar" -> {
                    addToMap(map,"Refined Gold","amount", amount);
                    double m1 = amount / ref_c * 20;
                    addToMap(map,"Refined Gold","time", m1/20 * 100 / c_spd);
                    heat += m1/20*250;
                    addToMap(map,"Gold Bar","amount", m1);
                    addToMap(map,"Gold Bar","time", m1 * 22 / c_spd);
                    heat += m1 * 50;
                    addToMap(map,"Gold Ore","amount", m1*5);
                }
                case "MagicPowder","FireJewel","WaterJewel","EarthJewel","WindJewel","DarkJewel","LightJewel" -> {
                    addToMap(map,"Magic Powder","amount", amount);
                    addToMap(map,"Magic Powder","time", amount * 20 / a_spd);
                }
                default -> addToMap(map, mat,"amount", amount);
            }
        }
        if (heat > 0) {
            addToMap(map,"Heat","amount", heat);
            int s_lvl = p.passives.get("Smithing").lvl;
            double hc = 0;
            if (s_lvl >= 35) {
                hc = heat / 60;
                addToMap(map,"Teak","amount", hc);
                addToMap(map,"Heat","time", hc * 2.5 / s_spd);
            } else {
                if (s_lvl >= 20) {
                    hc = heat / 45;
                    addToMap(map,"Oak","amount", hc);
                    addToMap(map,"Heat","time", hc * 2.0 / s_spd);
                } else {
                    hc = heat / 30;
                    addToMap(map,"Beech","amount", hc);
                    addToMap(map,"Heat","time", hc * 1.5 / s_spd);
                }
            }
        }
    }
}
