package Disilon;

import static Disilon.Main.df2;
import static Disilon.MonsterStatData.getCoreRP;

public class Core {
    public String name;
    public int grade;
    public int lvl;
    public boolean enabled;

    public Core(String name, int grade, int lvl, boolean enabled) {
        this.name = name;
        this.grade = grade;
        this.lvl = lvl;
        this.enabled = enabled;
    }

    public Core(String name, int grade, int lvl) {
        this.name = name;
        this.grade = grade;
        this.lvl = lvl;
    }

    public Core() {
    }

    public void applyStats(Actor actor, int id) {
        if (enabled && !name.equals("None")) {
            double flat = getFlatBonus();
            double scaling = getScaling(actor, id);
            double base = getBaseBonus();
            switch (name) {
                case "Slime" -> {
                    actor.set_magicdmg *= 1 + (flat + base * scaling) / 100;
                }
                case "Goblin" -> {
                    actor.set_physdmg *= 1 + (flat + base * scaling) / 100;
                }
                case "Devil" -> {
                    actor.poison_mult *= 1 + (flat + base * scaling) / 100;
                }
                case "Fire Lizard" -> {
                    actor.gear_crit += (flat + base * scaling) / 100;
                }
                case "Blood Lizard" -> {
                    actor.gear_crit_dmg += (flat + base * scaling) / 100;
                }
                case "Empress" -> {
                    actor.mp_cost_mult *= 1.0 - (flat + base * scaling) / 100;
                }
                case "Tengu" -> {
                    actor.drop_mult *= 1 + (flat + base * scaling) / 100;
                }
                case "Akuma" -> {
                    actor.exp_mult *= 1 + (flat + base * scaling) / 100;
                }
                case "Squirrel Mage" -> {
                    actor.core_mult_mult *= 1 + (flat + base * scaling) / 100;
                }
                case "Asura" -> {
                    actor.gear_atk += flat + base * scaling;
                }
                case "Lamia" -> {
                    actor.gear_int += flat + base * scaling;
                }
                case "Imp" -> {
                    actor.gear_fire += flat + base * scaling;
                }
                case "Ghoul" -> {
                    actor.gear_earth += flat + base * scaling;
                }
                case "Wraith" -> {
                    actor.gear_wind += flat + base * scaling;
                }
                case "Dagon" -> {
                    actor.gear_water += flat + base * scaling;
                }
                case "Shinigami" -> {
                    actor.gear_dark += flat + base * scaling;
                }
                case "Astaroth" -> {
                    actor.gear_light += flat + base * scaling;
                }
                case "Raum" -> {
                    actor.gear_no_elem += flat + base * scaling;
                }
                case "Tyrant" -> {
                    actor.gear_def += flat + base * scaling;
                }
                case "Amon" -> {
                    actor.gear_res += flat + base * scaling;
                }
                case "Shax" -> {
                    actor.gear_speed += flat + base * scaling;
                }
                case "Fairy" -> {
                    actor.gear_hit += flat + base * scaling;
                }
                case "Tree Golem" -> {
                    actor.gear_hp += flat + base * scaling;
                }
                case "Gloom Flower" -> {
                    actor.shield_max += flat + base * scaling;
                }
                case "Dark Reaper" -> {
                    actor.gear_atk += flat + base * scaling;
                    actor.gear_int += flat + base * scaling;
                    actor.gear_def += flat + base * scaling;
                    actor.gear_res += flat + base * scaling;
                    actor.gear_hit += flat + base * scaling;
                    actor.gear_speed += flat + base * scaling;
                }
            }
        }
    }

    public double getBaseBonus() {
        return switch (name) {
            case "Slime" -> 0.99*100/6831;
            case "Goblin" -> 0.99*100/6831;
            case "Devil" -> 0.99*100/6831;
            case "Fire Lizard" -> 0.99*100/6831;
            case "Blood Lizard" -> 0.99*100/6831;
            case "Empress" -> 0.99*100/6831;
            case "Tengu" -> 0.99*100/6831;
            case "Akuma" -> 0.99*100/6831;
            case "Squirrel Mage" -> 0.99*100/6831;
            case "Tree Golem" -> 0.998*30000/36349*4;
            case "Asura" -> 0.998*30000/36349;
            case "Lamia" -> 0.998*30000/36349;
            case "Tyrant" -> 0.998*30000/36349;
            case "Amon" -> 0.998*30000/36349;
            case "Fairy" -> 0.998*30000/36349;
            case "Shax" -> 0.998*30000/36349;
            case "Dark Reaper" -> 0.998*30000/36349;
            case "Imp" -> 0.998*300*80/36349;
            case "Ghoul" -> 0.998*300*80/36349;
            case "Wraith" -> 0.998*300*80/36349;
            case "Dagon" -> 0.998*300*80/36349;
            case "Shinigami" -> 0.998*300*80/36349;
            case "Astaroth" -> 0.998*300*80/36349;
            case "Raum" -> 0.998*300*80/36349;
            case "Gloom Flower" -> 0.498*300*350/36349;
            default -> 0;
        };
    }

    public double getFlatBonus() {
        return switch (name) {
            case "Slime" -> 1;
            case "Goblin" -> 1;
            case "Devil" -> 1;
            case "Fire Lizard" -> 1;
            case "Blood Lizard" -> 1;
            case "Empress" -> 1;
            case "Tengu" -> 1;
            case "Akuma" -> 1;
            case "Squirrel Mage" -> 1;
            case "Tree Golem" -> 60*4;
            case "Asura" -> 60;
            case "Lamia" -> 60;
            case "Tyrant" -> 60;
            case "Amon" -> 60;
            case "Fairy" -> 60;
            case "Shax" -> 60;
            case "Dark Reaper" -> 60;
            case "Imp" -> 48;
            case "Ghoul" -> 48;
            case "Wraith" -> 48;
            case "Dagon" -> 48;
            case "Shinigami" -> 48;
            case "Astaroth" -> 48;
            case "Raum" -> 48;
            case "Gloom Flower" -> 210;
            default -> 0;
        };
    }

    public double getScaling(Actor actor, int id) {
        if (enabled && !name.equals("None")) {
            Equipment item = actor.equipment.get(getSlot(actor, id));
            if (item != null) {
                if (isFlat()) {
                    double scaling = Math.pow(1.015, Math.min(item.upgrade, item.skill_required + 15));
                    scaling *= Math.pow(1.35, item.quality.ordinal() + 1);
                    scaling *= Math.pow(1.22, lvl);
                    scaling *= Math.pow(1.4, grade + 1);
                    return scaling;
                } else {
                    double scaling = Math.pow(1.01, Math.min(item.upgrade, item.skill_required + 15));
                    scaling *= Math.pow(1.2, item.quality.ordinal() + 1);
                    scaling *= Math.pow(1.22, lvl);
                    scaling *= Math.pow(1.4, grade + 1);
                    return scaling;
                }
            }
        }
        return 0;
    }

    public double calcRpWorth() {
        if (!enabled) return 0;
        double base = getCoreRP(grade, name);
        return Math.pow(2, lvl - 1) * base;
    }

    public String getSlot(Actor actor, int id) {
        Equipment mh = actor.equipment.get("MH");
        return switch (id) {
            case 0,1 -> "MH";
            case 10,11 -> (mh != null && mh.slot.equals("2H")) ? "MH" : "OH";
            case 20,21,22 -> "Chest";
            case 30,31 -> "Pants";
            case 40 -> "Helmet";
            case 50 -> "Bracer";
            case 60 -> "Boots";
            case 70 -> "Accessory1";
            case 80 -> "Accessory2";
            case 90 -> "Necklace";
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }

    public static String getBonusType(String name, double bonus) {
        return switch (name) {
            case "Slime" -> "IntDam " + df2.format(bonus) + "%";
            case "Goblin" -> "AtkDam " + df2.format(bonus) + "%";
            case "Devil" -> "PoisonDam " + df2.format(bonus) + "%";
            case "Fire Lizard" -> "CritChance " + df2.format(bonus) + "%";
            case "Blood Lizard" -> "CritDmg " + df2.format(bonus) + "%";
            case "Asura" -> "Atk " + df2.format(bonus);
            case "Lamia" -> "Int " + df2.format(bonus);
            case "Imp" -> "Fire " + df2.format(bonus);
            case "Ghoul" -> "Earth " + df2.format(bonus);
            case "Wraith" -> "Wind " + df2.format(bonus);
            case "Dagon" -> "Water " + df2.format(bonus);
            case "Shinigami" -> "Dark " + df2.format(bonus);
            case "Astaroth" -> "Light " + df2.format(bonus);
            case "Raum" -> "Non elem " + df2.format(bonus);
            case "Tyrant" -> "Def " + df2.format(bonus);
            case "Amon" -> "Res " + df2.format(bonus);
            case "Shax" -> "Speed " + df2.format(bonus);
            case "Fairy" -> "Hit " + df2.format(bonus);
            case "Tree Golem" -> "HP " + df2.format(bonus);
            case "Dark Reaper" -> "All stats " + df2.format(bonus);
            case "Empress" -> "ManaCost -" + df2.format(bonus) + "%";
            case "Tengu" -> "DropChance " + df2.format(bonus) + "%";
            case "Akuma" -> "ExpBoost " + df2.format(bonus) + "%";
            case "Squirrel Mage" -> "CoreDrop " + df2.format(bonus) + "%";
            case "Gloom Flower" -> "Barrier " + df2.format(bonus);
            default -> "Unknown bonus";
        };
    }

    public boolean isFlat() {
        return switch (name) {
            case "Slime" -> false;
            case "Goblin" -> false;
            case "Devil" -> false;
            case "Fire Lizard" -> false;
            case "Blood Lizard" -> false;
            case "Tengu" -> false;
            case "Akuma" -> false;
            case "Squirrel Mage" -> false;
            default -> true;
        };
    }
}
