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
    public HashMap<String, HashMap<Integer, Double>> cores; //Grade, Count in nested map
    public HashMap<String, Integer> deaths = new HashMap<>();

    public MonsterStatData(String[] possibleEnemies) {
        cores = new HashMap<>();
        for (String e : possibleEnemies) {
            HashMap<Integer, Double> nested = new HashMap<>();
            for (int i = 0; i < 9; i++) {
                nested.put(i, 0.0);
            }
            cores.put(e, nested);
        }
    }

    public void incrementStats(Actor e, ActiveSkill s, double dmg, double hit, double debuff, int uses, int h,
                               double dot) {
        String key = e.getName() + ": " + s.name;
        dmg_sum.put(key, dmg_sum.containsKey(key) ? dmg_sum.get(key) + dmg : dmg);
        hit_chance_sum.put(key, hit_chance_sum.containsKey(key) ? hit_chance_sum.get(key) + hit : hit);
        debuff_chance_sum.put(key, debuff_chance_sum.containsKey(key) ? debuff_chance_sum.get(key) + debuff : debuff);
        casts_total.put(key, casts_total.containsKey(key) ? casts_total.get(key) + uses : uses);
        hits_total.put(key, hits_total.containsKey(key) ? hits_total.get(key) + h : h);
        dot_sum.put(key, dot_sum.containsKey(key) ? dot_sum.get(key) + dot : dot);
    }

    public void incrementDmg(Actor e, ActiveSkill s, double dmg) {
        if (dmg > 0) {
            incrementStats(e, s, dmg, 0, 0, 0, 1, 0);
        }
    }

    public void incrementHit(Actor e, ActiveSkill s, double hit) {
        incrementStats(e, s, 0, hit, 0, 1, 0, 0);
    }

    public void incrementDebuff(Actor e, ActiveSkill s, double debuff) {
        incrementStats(e, s, 0, 0, debuff, 0, 0, 0);
    }

    public void incrementDot(Actor e, ActiveSkill s, double dmg) {
        incrementStats(e, s, 0, 0, 0, 0, 0, dmg);
    }

    public void clear_recorded_data() {
        dmg_sum.clear();
        hit_chance_sum.clear();
        debuff_chance_sum.clear();
        casts_total.clear();
        hits_total.clear();
        dot_sum.clear();
        cores.clear();
        deaths.clear();
    }

    public void recordOverkill(Actor e, int research) {
        double percent = -e.getHp() / e.getHp_max() * 100;
        if (percent <= 1) {
            addCore(e.name, 4, research);
        } else if (percent <= 10) {
            addCore(e.name, 3, research);
        } else if (percent <= 25) {
            addCore(e.name, 2, research);
        } else if (percent <= 50) {
            addCore(e.name, 1, research);
        } else if (percent <= 200) {
            addCore(e.name, 0, research);
        }
        deaths.put(e.getName(), deaths.containsKey(e.getName()) ? deaths.get(e.getName()) + 1 : 1);
    }

    public void addCore(String name, int grade, int research) {
        if (!cores.containsKey(name)) {
            HashMap<Integer, Double> nested = new HashMap<>();
            for (int i = 0; i < 9; i++) {
                nested.put(i, 0.0);
            }
            cores.put(name, nested);
        }
        double fractional = research / 100.0 - (double) (research / 100);
        int new_grade = Math.min(8, grade + research / 100);
        if (fractional > 0 && new_grade < 8) {
            Double count = 1 - fractional;
            cores.get(name).merge(new_grade, count, Double::sum);
            cores.get(name).merge(new_grade + 1, fractional, Double::sum);
        } else {
            cores.get(name).merge(new_grade, 1.0, Double::sum);
        }
    }

    public String getSkillData(int simulations) {
        StringBuilder sb = new StringBuilder();
        for (String name : hit_chance_sum.keySet()) {
            double average_hit_chance = hit_chance_sum.get(name) / casts_total.get(name);
            double average_dmg = dmg_sum.get(name) / hits_total.get(name);
            double average_debuff_chance = debuff_chance_sum.get(name) / hits_total.get(name);
            double average_dot = dot_sum.get(name) / hits_total.get(name);
            double average_used = (double) casts_total.get(name) / simulations;
            sb.append(name).append(" used: ").append(df2.format(average_used)).append(";");
            if (!name.endsWith("Counter Strike") && !name.endsWith("Counter Dodge")) {
                sb.append(" hit: ").append((int) (average_hit_chance * 100)).append("%;");
            }
            sb.append(" dmg: ").append((int) average_dmg);
            sb.append(average_debuff_chance == 0 ? "" : "; debuff: " + (int) (average_debuff_chance * 100) + "%");
            sb.append(average_dot == 0 ? "" : "; DoT: " + (int) (average_dot));
            sb.append("\n");
        }
        return sb.toString();
    }

    public String getCoreData(Player p, double time) {
        StringBuilder sb = new StringBuilder();
        double rp = 0;
        double drop_rate = 0.01 * p.core_mult * (1 + 0.01 * p.research_lvls.getOrDefault("CoreDrop", 0));
        for (String name : cores.keySet()) {
            sb.append(name).append(": ");
            boolean first = true;
            for (Integer grade : cores.get(name).keySet()) {
                double count = cores.get(name).get(grade) * drop_rate;
                if (count > 0) {
                    rp += getCoreRP(grade, name) * count;
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
        sb.append("RP/h: ").append(df2.format(rp / time * 3600));
        sb.append("\n");
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
            case "Slime" -> 4;
            case "Goblin" -> 5;
            case "Imp" -> 5;
            case "Wrath" -> 8;
            case "Ghoul" -> 8;
            case "Astaroth" -> 9;
            case "Shinigami" -> 9;
            case "Amon" -> 10;
            case "Tengu" -> 10;
            case "Akuma" -> 12;
            case "Devil" -> 18;
            case "Shax" -> 40;
            case "Dagon" -> 45;
            case "Lamia" -> 50;
            case "Tyrant" -> 100;
            case "Raum" -> 150;
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
