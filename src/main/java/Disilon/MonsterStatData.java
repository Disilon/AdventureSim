package Disilon;

import java.util.HashMap;

import static Disilon.Main.df2;

public class MonsterStatData {
    public HashMap<String, Double> dmg_sum = new HashMap<>();
    public HashMap<String, Double> debuff_chance_sum = new HashMap<>();
    public HashMap<String, Double> hit_chance_sum = new HashMap<>();
    public HashMap<String, Double> dot_sum = new HashMap<>();
    public HashMap<String, Integer> hits_total = new HashMap<>();
    public HashMap<String, Integer> casts_total = new HashMap<>();
    public HashMap<String, Integer> used_debuffed = new HashMap<>();
    public HashMap<String, HashMap<Integer, Double>> cores; //Grade, Count in nested map
    public double base_rp;
    public double gained_rp;
    public double gained_rp_rng;
    public double[] rp_instance;
    public HashMap<String, Integer> deaths = new HashMap<>();
    public double overkill_sum;
    public int kills;

    public MonsterStatData(String[] possibleEnemies) {
        cores = new HashMap<>();
        rp_instance = new double[7];
        for (String e : possibleEnemies) {
            HashMap<Integer, Double> nested = new HashMap<>();
            for (int i = 0; i < 9; i++) {
                nested.put(i, 0.0);
            }
            cores.put(e, nested);
        }
    }

    public void incrementStats(String e, String s, double dmg, double hit_chance, double debuff, int uses, int hits,
                               double dot) {
        String key = e + ": " + s;
        dmg_sum.put(key, dmg_sum.containsKey(key) ? dmg_sum.get(key) + dmg : dmg);
        hit_chance_sum.put(key, hit_chance_sum.containsKey(key) ? hit_chance_sum.get(key) + hit_chance : hit_chance);
        debuff_chance_sum.put(key, debuff_chance_sum.containsKey(key) ? debuff_chance_sum.get(key) + debuff : debuff);
        casts_total.put(key, casts_total.containsKey(key) ? casts_total.get(key) + uses : uses);
        hits_total.put(key, hits_total.containsKey(key) ? hits_total.get(key) + hits : hits);
        dot_sum.put(key, dot_sum.containsKey(key) ? dot_sum.get(key) + dot : dot);
    }

    public void incrementDmg(String e, String s, double dmg) {
        String key = e + ": " + s;
        if (dmg > 0) {
            dmg_sum.put(key, dmg_sum.containsKey(key) ? dmg_sum.get(key) + dmg : dmg);
            hits_total.put(key, hits_total.containsKey(key) ? hits_total.get(key) + 1 : 1);
        }
    }

    public void incrementHit(String e, String s, double hit) {
        String key = e + ": " + s;
        hit_chance_sum.put(key, hit_chance_sum.containsKey(key) ? hit_chance_sum.get(key) + hit : hit);
        casts_total.put(key, casts_total.containsKey(key) ? casts_total.get(key) + 1 : 1);
    }

    public void incrementDebuff(String e, String s, double debuff) {
        String key = e + ": " + s;
        debuff_chance_sum.put(key, debuff_chance_sum.containsKey(key) ? debuff_chance_sum.get(key) + debuff : debuff);
    }

    public void incrementDot(String e, String s, double dmg) {
        String key = e + ": " + s;
        dot_sum.put(key, dot_sum.containsKey(key) ? dot_sum.get(key) + dmg : dmg);
    }

    public void incrementUsedDebuffed(String e, String s, int increment) {
        String key = e + ": " + s;
        used_debuffed.put(key, used_debuffed.containsKey(key) ? used_debuffed.get(key) + increment : increment);
//        used_debuffed.merge(key, increment, Integer::sum);
    }

    public void clear_recorded_data() {
        base_rp = 0;
        gained_rp = 0;
        overkill_sum = 0;
        kills = 0;
        rp_instance = new double[7];
        dmg_sum.clear();
        hit_chance_sum.clear();
        debuff_chance_sum.clear();
        casts_total.clear();
        hits_total.clear();
        dot_sum.clear();
        cores.clear();
        deaths.clear();
        used_debuffed.clear();
    }

    public void recordOverkill(Enemy e, Player player) {
        double percent = -e.getHp() / e.getHp_max() * 100;
        overkill_sum += percent;
        kills++;
        if (percent <= 1) {
            addCore(e.name, 4, player);
        } else if (percent <= 10) {
            addCore(e.name, 3, player);
        } else if (percent <= 25) {
            addCore(e.name, 2, player);
        } else if (percent <= 50) {
            addCore(e.name, 1, player);
        } else if (percent <= 200) {
            addCore(e.name, 0, player);
        }
        deaths.put(e.getName(), deaths.containsKey(e.getName()) ? deaths.get(e.getName()) + 1 : 1);
    }

    public void addCore(String name, int grade, Player p) {
        int research = p.research_lvls.get("Core quality").intValue();
        double drop_rate = 0.01 * (p.set_core * 1.5 + p.core_mult
                + 0.01 * p.research_lvls.getOrDefault("Core drop", 0.0).intValue());
        if (!cores.containsKey(name)) {
            HashMap<Integer, Double> nested = new HashMap<>();
            for (int i = 0; i < 9; i++) {
                nested.put(i, 0.0);
            }
            cores.put(name, nested);
        }
        base_rp += getCoreRP(grade, name) * 0.01;
        double fractional = research / 100.0 - (double) (research / 100);
        int new_grade = Math.min(8, grade + research / 100);
        double gain = 0;
        if (fractional > 0 && new_grade < 8) {
            double count = 1 - fractional;
            cores.get(name).merge(new_grade, count, Double::sum);
            gain += getCoreRP(new_grade, name) * drop_rate * count;
            cores.get(name).merge(new_grade + 1, fractional, Double::sum);
            gain += getCoreRP(new_grade + 1, name) * drop_rate * fractional;
        } else {
            cores.get(name).merge(new_grade, 1.0, Double::sum);
            gain += getCoreRP(new_grade, name) * drop_rate * 1;
        }
        if (p.lvling && Main.game_version >= 1574) p.rp_balance += gain; //todo:fix it
        gained_rp += gain;
        addCoreRandom(name, grade, p);
    }

    public void addCoreRandom(String name, int grade, Player p) {
        int research = p.research_lvls.get("Core quality").intValue();
        double drop_rate = 0.01 * (p.set_core * 1.5 + p.core_mult
                + 0.01 * p.research_lvls.getOrDefault("Core drop", 0.0).intValue());
        double fractional = research / 100.0 - (double) (research / 100);
        int new_grade = Math.min(8, grade + research / 100);
        for (int i = 1; i < rp_instance.length; i++) {
            if (drop_rate > Math.random()) {
                double gain = 0;
                if (fractional > 0 && new_grade < 8) {
                    if (fractional > Math.random()) {
                        gain = getCoreRP(new_grade + 1, name);
                    } else {
                        gain = getCoreRP(new_grade, name);
                    }
                } else {
                    gain = getCoreRP(new_grade, name);
                }
                rp_instance[i] += gain;
            }
        }
    }

    public String getSkillData(int simulations) {
        StringBuilder sb = new StringBuilder();
        for (String name : casts_total.keySet()) {
            if (hit_chance_sum.containsKey(name)) {
                double average_hit_chance = hit_chance_sum.getOrDefault(name, 0.0) / casts_total.getOrDefault(name, 0);
                double average_dmg = dmg_sum.getOrDefault(name, 0.0) / hits_total.getOrDefault(name, 0);
                double average_debuff_chance =
                        debuff_chance_sum.getOrDefault(name, 0.0) / hits_total.getOrDefault(name, 0);
                double average_dot = dot_sum.getOrDefault(name, 0.0) / hits_total.getOrDefault(name, 0);
                double average_used = (double) casts_total.getOrDefault(name, 0) / simulations;
                double average_debuffed = used_debuffed.containsKey(name) ?
                        (double) used_debuffed.getOrDefault(name, 0) / casts_total.getOrDefault(name, 0) : 0;
                sb.append(name).append(" used: ").append(df2.format(average_used)).append(";");
                if (!name.endsWith("Counter Strike") && !name.endsWith("Counter Dodge")) {
                    sb.append(" hit: ").append((int) (average_hit_chance * 100)).append("%;");
                }
                sb.append(" dmg: ").append((int) average_dmg);
                sb.append(average_debuff_chance == 0 ? "" : "; debuff: " + (int) (average_debuff_chance * 100) + "%");
                sb.append(average_dot == 0 ? "" : "; DoT: " + (int) (average_dot));
                sb.append(average_debuffed == 0 ? "" : "; used debuffed: " + (int) (average_debuffed * 100) + "%");
                sb.append("\n");
            } else {
                double average_used = (double) casts_total.getOrDefault(name, 0) / simulations;
                sb.append(name).append(" used: ").append(df2.format(average_used)).append("\n");
            }
        }
        return sb.toString();
    }

    public String getCoreData(Player p, double time, boolean offline) {
        StringBuilder sb = new StringBuilder();
        double drop_rate = 0.01 * (p.set_core * 1.5 + p.core_mult
                        + 0.01 * p.research_lvls.getOrDefault("Core drop", 0.0).intValue());
        for (String name : cores.keySet()) {
            sb.append(name).append(": ");
            boolean first = true;
            for (Integer grade : cores.get(name).keySet()) {
                double count = cores.get(name).get(grade) * drop_rate;
                if (count > 0) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append("; ");
                    }
                    sb.append(getCoreGrade(grade)).append(": ").append(df2.format(count));
                }
            }
            sb.append("\n");
        }
        double offline_rp = 0;
        double offline_base_rp = 0;
        int research = p.research_lvls.get("Core quality").intValue();
        double fractional = research / 100.0 - (double) (research / 100);
        double percent = overkill_sum / kills;
        int grade = -1;
        if (percent <= 1) {
            grade = 4;
        } else if (percent <= 10) {
            grade = 3;
        } else if (percent <= 25) {
            grade = 2;
        } else if (percent <= 50) {
            grade = 1;
        } else if (percent <= 200) {
            grade = 0;
        }
        if (grade >= 0) {
            int new_grade = Math.min(8, grade + research / 100);
            for (String name : deaths.keySet()) {
                double count = deaths.getOrDefault(name, 0);
                offline_base_rp += getCoreRP(grade, name) * 0.01 * count;
                if (fractional > 0 && new_grade < 8) {
                    offline_rp += getCoreRP(new_grade, name) * drop_rate * count * (1 - fractional);
                    offline_rp += getCoreRP(new_grade + 1, name) * drop_rate * count * fractional;
                } else {
                    offline_rp += getCoreRP(new_grade, name) * drop_rate * count;
                }
            }
        }
        if (offline) {
            sb.append("Offline RP/h: ").append(df2.format(offline_rp / time * 3600));
            sb.append(" (base: ").append(df2.format(offline_base_rp / time * 3600)).append(")");
        } else {
            sb.append("RP/h: ").append(df2.format(gained_rp / time * 3600));
            sb.append(" (base: ").append(df2.format(base_rp / time * 3600)).append(")");
        }
        sb.append("\n");
//        if (Main.game_version < 1574) {
//            double max = 0;
//            for (int i = 1; i < 7; i++) {
//                if (rp_instance[i] > max) max = rp_instance[i];
//            }
//            sb.append("RP/h: ").append(df2.format(rp_instance[0] / time * 3600));
//            sb.append("\n");
//        }
        return sb.toString();
    }

    public static int getCoreRP(int grade, String name) {
        double rp = getCoreGradeRP(grade) * getCoreTypeRP(name);
        return (int) Math.round(rp);
    }

    public static double getCoreGradeRP(int grade) {
        double mult = Math.pow(1.3, Math.min(grade, 5));
        if (grade > 5) mult = mult * Math.pow(1.5, grade - 5);
        return mult;
    }

    public static double getCoreTypeRP(String name) {
        return switch (name) {
            case "Slime","Slime2" -> 4;
            case "Goblin","Goblin2" -> 5;
            case "Imp","Imp2" -> 5;
            case "Wrath" -> 8;
            case "Ghoul","Ghoul2" -> 8;
            case "Astaroth" -> 9;
            case "Shinigami" -> 9;
            case "Amon" -> 10;
            case "Tengu" -> 10;
            case "Akuma" -> 12;
            case "Devil" -> 18;
            case "Shax" -> Main.game_version < 1568 ? 40 : 45;
            case "Dagon" -> 60;
            case "Lamia" -> 70;
            case "Tyrant" -> 100;
            case "Fairy" -> Main.game_version < 1568 ? 175 : 350;
            case "Raum" -> Main.game_version < 1568 ? 150 : (Main.game_version < 1574 ? 180 : 230);
            case "Asura" -> 165;
            default -> 0;
        };
    }

    public static String getCoreGrade(int grade) {
        return switch (grade) {
            case 0 -> "F";
            case 1 -> "E";
            case 2 -> "D";
            case 3 -> "C";
            case 4 -> "B";
            case 5 -> "A";
            case 6 -> "S";
            case 7 -> "SS";
            case 8 -> "SSS";
            default -> throw new IllegalStateException("Unexpected value: " + grade);
        };
    }
}
