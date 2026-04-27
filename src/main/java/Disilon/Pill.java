package Disilon;

import java.util.ArrayList;

public class Pill {
    String name;

    public Pill(String name) {
        this.name = name;
    }

    public void applyEffects(Actor a) {
        a.add_resist("All", 0.2 * getEffect(a, "Toughness"));

        a.pill_cast_speed_mult *= 1.0 - 0.1 * getEffect(a, "Speedy");
        if (name.equals("Speedy")) {
            a.mp_cost_mult *= 1.0 + 0.1 * a.pill_effect;
        }

        a.dmg_mult *= 1.0 + 0.2 * getEffect(a, "Berserk");
        if (name.equals("Berserk")) {
            a.berserk_dmg = 200 * a.pill_effect;
        }

        a.exp_mult *= 1.0 + 0.2 * getEffect(a, "Wise");

        a.gear_crit += 0.2 * getEffect(a, "Critical");

        double stats = 0.18 * getEffect(a, "Ultimate");
        a.atk_mult *= 1.0 + stats;
        a.def_mult *= 1.0 + stats;
        a.int_mult *= 1.0 + stats;
        a.res_mult *= 1.0 + stats;
        a.hit_mult *= 1.0 + stats;
        a.speed_mult *= 1.0 + stats;
    }

    public double getEffect(Actor a, String pill) {
        double effect = name.equals(pill) ? 1 : 0;
        if (Main.game_version >= 1667) effect += 0.001 * a.passives.get(pill + " pill").lvl;
        return effect * a.pill_effect;
    }

    public void usePill(Actor a, double time) {
        if (!name.equals("None") && Main.game_version >= 1667) {
            if (a.passives.get(name + " pill").increasePillUsed(time / 7200)) {
                a.refreshStats();
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

    public double calc_time(double time, int alchemy, int alchemist_lvl, int research_alch) {
        double need_to_craft = time / 7200;
        double alch_spd = (1 + 0.01 * alchemy) * (1 + 0.01 * research_alch);
        if (alchemist_lvl >= 90) {
            alch_spd *= 1 + 0.0000125 * Math.pow(alchemist_lvl, 2);
        }
        return getBaseCraftTime() * need_to_craft / alch_spd;
    }
}
