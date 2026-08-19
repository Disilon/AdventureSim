package Disilon;

import static Disilon.Main.df2;
import static Disilon.Main.game_version;
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
            double bonus = getBonus(actor, id);
            switch (name) {
                case "Slime" -> {
                    actor.core_intdam += bonus / 100;
                }
                case "Goblin" -> {
                    actor.core_atkdam += bonus / 100;
                }
                case "Devil" -> {
                    actor.core_poison += bonus / 100;
                }
                case "Fire Lizard" -> {
                    actor.gear_crit += bonus / 100;
                }
                case "Blood Lizard" -> {
                    actor.core_critdmg += bonus / 100;
                }
                case "Empress" -> {
                    actor.core_manacost -= bonus / 100;
                }
                case "Tengu" -> {
                    actor.core_item_drop += bonus / 100;
                }
                case "Akuma" -> {
                    actor.core_exp += bonus / 100;
                }
                case "Squirrel Mage" -> {
                    actor.core_cdr_add += bonus / 100;
                }
                case "Asura" -> {
                    actor.gear_atk += bonus;
                }
                case "Lamia" -> {
                    actor.gear_int += bonus;
                }
                case "Imp" -> {
                    actor.gear_fire += bonus;
                }
                case "Ghoul" -> {
                    actor.gear_earth += bonus;
                }
                case "Wraith" -> {
                    actor.gear_wind += bonus;
                }
                case "Dagon" -> {
                    actor.gear_water += bonus;
                }
                case "Shinigami" -> {
                    actor.gear_dark += bonus;
                }
                case "Astaroth" -> {
                    actor.gear_light += bonus;
                }
                case "Raum" -> {
                    actor.gear_no_elem += bonus;
                }
                case "Tyrant" -> {
                    actor.gear_def += bonus;
                }
                case "Amon" -> {
                    actor.gear_res += bonus;
                }
                case "Shax" -> {
                    actor.gear_speed += bonus;
                }
                case "Fairy" -> {
                    actor.gear_hit += bonus;
                }
                case "Tree Golem" -> {
                    actor.gear_hp += bonus;
                }
                case "Gloom Flower" -> {
                    actor.shield_max += bonus;
                }
                case "Dark Reaper" -> {
                    actor.gear_hp += bonus;
                    actor.gear_atk += bonus;
                    actor.gear_int += bonus;
                    actor.gear_def += bonus;
                    actor.gear_res += bonus;
                    actor.gear_hit += bonus;
                    actor.gear_speed += bonus;
                }
            }
        }
    }

    public double getBaseBonus() {
        double max_power = Math.pow(equipLvl(), 100);
        max_power *= Math.pow(equipQual(), 10);
        max_power *= Math.pow(coreLvl(), 15);
        max_power *= Math.pow(coreGrade(), 9);
        return 1 / max_power * (getMaxBonus() - getFlatBonus());
    }

    public double getFlatBonus() {
        return switch (name) {
            case "Tree Golem" -> 60*10;
            case "Dark Reaper" -> 60/2;
            case "Imp" -> 40;
            case "Ghoul" -> 40;
            case "Wraith" -> 40;
            case "Dagon" -> 40;
            case "Shinigami" -> 40;
            case "Astaroth" -> 40;
            case "Raum" -> 40;
            case "Gloom Flower" -> 200;
            case "Fire Lizard" -> 1;
            case "Blood Lizard" -> 1;
            case "Squirrel Mage" -> 1;
            default -> {
                yield isFlat() ? 60 : 1;
            }
        };
    }

    public double getMaxBonus() {
        return switch (name) {
            case "Tree Golem" -> 24000*10;
            case "Dark Reaper" -> 24000/2;
            case "Imp" -> 19200;
            case "Ghoul" -> 19200;
            case "Wraith" -> 19200;
            case "Dagon" -> 19200;
            case "Shinigami" -> 19200;
            case "Astaroth" -> 19200;
            case "Raum" -> 19200;
            case "Gloom Flower" -> 35000;
            case "Fire Lizard" -> 70;
            case "Blood Lizard" -> 70*1.25;
            case "Devil" -> 100;
            case "Squirrel Mage" -> game_version >= 1681 ? 160 : (game_version >= 1680 ? 140 : 70);
            default -> {
                yield isFlat() ? 24000 : 70;
            }
        };
    }

    public double getBonus(Actor actor, int id) {
        if (enabled && !name.equals("None")) {
            Equipment item = actor.equipment.get(getSlot(actor, id));
            if (item != null) {
                int upgrade = getUpgradeCap(item.skill_required, item.upgrade);
                int quality = getQualityCap(item.skill_required, item.quality.ordinal());
                double flat = getFlatBonus();
                double scaling = getScaling(upgrade, quality);
                double base = getBaseBonus();
                return flat + base * scaling;
            }
        }
        return 0;
    }

    public static int getUpgradeCap(int skill_required, int value) {
        if (game_version <= 1681) return value;
        int max = switch (skill_required) {
            default -> 10;
            case 10 -> 20;
            case 20 -> 30;
            case 35 -> 45;
            case 50 -> 60;
            case 65 -> 75;
            case 80 -> 100;
        };
        return Math.min(value, max);
    }

    public static int getQualityCap(int skill_required, int value) {
        if (game_version <= 1681) return value;
        int max = switch (skill_required) {
            default -> 4;
            case 10 -> 5;
            case 20 -> 6;
            case 35 -> 7;
            case 50 -> 8;
            case 65 -> 9;
            case 80 -> 9;
        };
        return Math.min(value, max);
    }

    public double getScaling(int upgrade, int quality) {
        double scaling = Math.pow(equipLvl(), upgrade);
        scaling *= Math.pow(equipQual(), quality + 1);
        scaling *= Math.pow(coreLvl(), lvl);
        scaling *= Math.pow(coreGrade(), grade + 1);
        return scaling;
    }

    public double equipLvl() {
        if (isFlat()) {
            return 1.015;
        } else {
            return 1.01;
        }
    }
    public double equipQual() {
        if (isFlat()) {
            return 1.2;
        } else {
            return 1.15;
        }
    }
    public double coreLvl() {
        if (isFlat()) {
            return 1.25;
        } else {
            return game_version >= 1680 ? 1.2 : 1.25;
        }
    }
    public double coreGrade() {
        if (isFlat()) {
            return 1.4;
        } else {
            return 1.3;
        }
    }

    public double calcRpWorth() {
        if (!enabled) return 0;
        double base = getCoreRP(grade, name);
        return Math.pow(2, lvl - 1) * base;
    }

    public double calcRemovalCost() {
        if (!enabled) return 0;
        if (game_version >= 1681 & lvl < 4) return 0;
        return Math.ceil(6 * Math.pow(1.3, lvl) * Math.pow(2, grade + 1));
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
            case "Fire Lizard" -> game_version < 1681;
            case "Blood Lizard" -> game_version < 1681;
            case "Tengu" -> false;
            case "Akuma" -> false;
            case "Squirrel Mage" -> false;
            case "Empress" -> false;
            default -> true;
        };
    }
}
