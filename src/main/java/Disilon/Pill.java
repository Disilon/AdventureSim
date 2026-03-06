package Disilon;

import java.util.ArrayList;

public class Pill {
    String name;

    public Pill(String name) {
        this.name = name;
    }

    public void applyEffects(Actor a) {
        switch (name) {
            case "Toughness" -> {
                double effect = 0.2 * a.pill_effect;
                a.add_resist("All", effect);
            }
            case "Speedy" -> {
                double effect = 0.1 * a.pill_effect;
                a.cast_speed_mult *= 1.0 - effect;
                a.mp_cost_mult *= 1.0 + effect;
            }
            case "Berserk" -> {
                double effect = 0.2 * a.pill_effect;
                a.dmg_mult *= 1.0 + effect;
                a.berserk_dmg = 200 * a.pill_effect;
            }
            case "Wise" -> {
                double effect = 0.2 * a.pill_effect;
                a.exp_mult *= 1.0 + effect;
            }
            case "Critical" -> {
                double effect = 0.2 * a.pill_effect;
                a.gear_crit += effect;
            }
            case "Ultimate" -> {
                double effect = 0.18 * a.pill_effect;
                a.atk_mult *= 1.0 + effect;
                a.def_mult *= 1.0 + effect;
                a.int_mult *= 1.0 + effect;
                a.res_mult *= 1.0 + effect;
                a.hit_mult *= 1.0 + effect;
                a.speed_mult *= 1.0 + effect;
            }
        }
    }

    public static ArrayList<String> getAvailablePills() {
        ArrayList<String> v = new ArrayList<>();
        v.add("None");
        v.add("Toughness");
        v.add("Speedy");
        v.add("Berserk");
        v.add("Wise");
        v.add("Critical");
        v.add("Ultimate");
        return v;
    }

    public double getBaseCraftTime() {
        return switch (name) {
            case "None" -> 0;
            case "Ultimate" -> 2000;
            default -> 1500;
        };
    }

    public double calc_time(double time, int alchemy, int research_alch) {
        double need_to_craft = time / 7200;
        return getBaseCraftTime() * need_to_craft / (1 + 0.01 * alchemy) / (1 + 0.01 * research_alch);
    }
}
