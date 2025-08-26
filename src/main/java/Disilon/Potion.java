package Disilon;

import java.util.Vector;

import static Disilon.Main.df2;

public class Potion {
    String type;
    int tier;
    double cooldown;
    int threshold;
    int used;
    int count;

    public Potion(String type, int tier, int threshold) {
        this.type = type;
        this.tier = tier;
        this.threshold = threshold;
    }

    public Potion(String type, int threshold) {
        this.type = type.substring(0, 2);
        this.tier = Integer.parseInt(type.substring(4, 5));
        if (type.length() > 5) {
            this.tier += 10;
        }
        this.threshold = threshold;
    }

    public Potion(String type, int tier, int threshold, int count) {
        this.type = type;
        this.tier = tier;
        this.threshold = threshold;
        this.count = count;
    }

    public void checkPotion(Actor player, double time) {
        if (cooldown > 0) {
            cooldown -= time;
        }
        if (cooldown <= 0) {
            switch (type.toLowerCase()) {
                case "hp":
                    if (player.getHp() < player.getHp_max() * threshold / 100) {
                        player.setHp(player.getHp() + hp_gain());
                        cooldown = hp_cd();
                        used++;
                        count--;
                    }
                    break;
                case "mp":
                    if (player.getMp() < player.getMp_max_no_buffs() * threshold / 100) {
                        player.setMp(player.getMp() + mp_gain());
                        cooldown = mp_cd();
                        used++;
                        count--;
                    }
                    break;
            }
        }
    }

    public void tickPotion(double time) {
        if (cooldown > 0) {
            cooldown -= time;
        }
    }

    private int hp_gain() {
        return switch (tier) {
            case 1 -> 200;
            case 2 -> 500;
            case 3 -> 1200;
            case 4 -> 3000;
            case 5 -> 7500;
            case 11 -> 250;
            case 12 -> 600;
            case 13 -> 1350;
            case 14 -> 3500;
            case 15 -> 9000;
            default -> 0;
        };
    }

    private int mp_gain() {
        return switch (tier) {
            case 1 -> 125;
            case 2 -> 300;
            case 3 -> 700;
            case 4 -> 1500;
            case 5 -> 3000;
            case 11 -> 150;
            case 12 -> 350;
            case 13 -> 800;
            case 14 -> 1750;
            case 15 -> 3500;
            default -> 0;
        };
    }

    private double hp_cd() {
        return switch (tier) {
            case 1 -> 15;
            case 2 -> 20;
            case 3 -> 30;
            case 4 -> 40;
            case 5 -> 50;
            case 11 -> 7.5;
            case 12 -> 10;
            case 13 -> 15;
            case 14 -> 20;
            case 15 -> 25;
            default -> 0;
        };
    }

    private double mp_cd() {
        return switch (tier) {
            case 1 -> 15;
            case 2 -> 25;
            case 3 -> 45;
            case 4 -> 60;
            case 5 -> 90;
            case 11 -> 7.5;
            case 12 -> 12.5;
            case 13 -> 22.5;
            case 14 -> 30;
            case 15 -> 45;
            default -> 0;
        };
    }

    public double craft_time(int crafting, int alchemy, int research_craft, int research_alch) {
        double craft_spd = (1 + 0.01 * crafting) * (1 + 0.01 * research_craft);
        double alch_spd = (1 + 0.01 * alchemy) * (1 + 0.01 * research_alch);
        return switch (tier) {
            case 1 -> 2 / craft_spd + 3 / alch_spd;
            case 2 -> 4 / craft_spd + 6 / alch_spd;
            case 3 -> 6 / craft_spd + 9 / alch_spd;
            case 4 -> 8 / craft_spd + 12 / alch_spd;
            case 5 -> 10 / craft_spd + 15 / alch_spd;
            case 11 -> 2 / craft_spd + 3 / alch_spd * 1.5;
            case 12 -> 4 / craft_spd + 6 / alch_spd * 1.5;
            case 13 -> 6 / craft_spd + 9 / alch_spd * 1.5;
            case 14 -> 8 / craft_spd + 12 / alch_spd * 1.5;
            case 15 -> 10 / craft_spd + 15 / alch_spd * 1.5;
            default -> 0;
        };
    }

    public double calc_time(int crafting, int alchemy, int research_craft, int research_alch) {
        int need_to_craft = 0;
        double time = 0;
        if (count < 0) {
            need_to_craft = count * -1;
            time = craft_time(crafting, alchemy, research_craft, research_alch) * need_to_craft;
            count = 0;
        }
        return time;
    }

    public static Vector<String> getAvailablePotions() {
        Vector<String> result = new Vector<>();
        result.add("None");
        for (int i = 1; i < 6; i++) {
            result.add("HP T" + i);
            result.add("HP T" + i + " I");
        }
        for (int i = 1; i < 6; i++) {
            result.add("MP T" + i);
            result.add("MP T" + i + " I");
        }
        return result;
    }

    public String getRecordedData(double time) {
        return type.toUpperCase() + " potion tier: " + getTier() + ", used: " + used + ", per hour: " + df2.format(used / time * 3600) +
                "\n";
    }

    public String getTier() {
        return tier > 9 ? (tier - 10) + " I" : String.valueOf(tier);
    }
}
