package Disilon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static Disilon.Main.df2;
import static Disilon.Main.game_version;

public class MonsterStatData {
    public HashMap<String, Double> dmg_sum = new HashMap<>();
    public HashMap<String, Double> debuff_chance_sum = new HashMap<>();
    public HashMap<String, Double> hit_chance_sum = new HashMap<>();
    public HashMap<String, Double> dot_sum = new HashMap<>();
    public HashMap<String, Integer> hits_total = new HashMap<>();
    public HashMap<String, Integer> casts_total = new HashMap<>();
    public HashMap<String, Integer> used_debuffed = new HashMap<>();
    public HashMap<String, HashMap<Integer, Double>> cores;
    public HashMap<String, HashMap<Integer, Double>> base_overkill;
    public HashMap<String, Double> analyzed = new HashMap<>();
    public double base_rp;
    public double gained_rp;
    public double nuts;
    public double gained_rp_rng;
    public double[] rp_instance;
    public HashMap<String, Integer> deaths = new HashMap<>();
    public double overkill_sum;
    public double base_overkill_sum;
    public int kills;
    public int squirrel_spawns;

    public MonsterStatData(String[] possibleEnemies) {
        cores = new HashMap<>();
        base_overkill = new HashMap<>();
        rp_instance = new double[7];
        for (String e : possibleEnemies) {
            HashMap<Integer, Double> nested = new HashMap<>();
            for (int i = 0; i < 9; i++) {
                nested.put(i, 0.0);
            }
            cores.put(e, nested);
//            HashMap<Integer, Double> base_nested = new HashMap<>();
//            for (int i = 0; i < 5; i++) {
//                nested.put(i, 0.0);
//            }
//            base_overkill.put(e, base_nested);
        }
    }

    public void incrementStats(String e, String s, double dmg, double hit_chance, double debuff, int uses, int hits,
                               double dot) {
        String key = e + ": " + s;
        dmg_sum.put(key, dmg_sum.getOrDefault(key, 0.0) + dmg);
        hit_chance_sum.put(key, hit_chance_sum.getOrDefault(key, 0.0) + hit_chance);
        debuff_chance_sum.put(key, debuff_chance_sum.getOrDefault(key, 0.0) + debuff);
        casts_total.put(key, casts_total.getOrDefault(key, 0) + uses);
        hits_total.put(key, hits_total.getOrDefault(key, 0) + hits);
        dot_sum.put(key, dot_sum.getOrDefault(key, 0.0) + dot);
    }

    public void incrementDmg(String e, String s, double dmg) {
        String key = e + ": " + s;
        if (dmg > 0) {
            dmg_sum.put(key, dmg_sum.getOrDefault(key, 0.0) + dmg);
            hits_total.put(key, hits_total.getOrDefault(key, 0) + 1);
        }
    }

    public void incrementHit(String e, String s, double hit) {
        String key = e + ": " + s;
        hit_chance_sum.put(key, hit_chance_sum.getOrDefault(key, 0.0) + hit);
        casts_total.put(key, casts_total.getOrDefault(key, 0) + 1);
    }

    public void incrementDebuff(String e, String s, double debuff) {
        String key = e + ": " + s;
        debuff_chance_sum.put(key, debuff_chance_sum.getOrDefault(key, 0.0) + debuff);
    }

    public void incrementDot(String e, String s, double dmg) {
        String key = e + ": " + s;
        dot_sum.put(key, dot_sum.getOrDefault(key, 0.0) + dmg);
    }

    public void incrementUsedDebuffed(String e, String s, int increment) {
        String key = e + ": " + s;
        used_debuffed.put(key, used_debuffed.getOrDefault(key, 0) + increment);
    }

    public void clear_recorded_data() {
        base_rp = 0;
        gained_rp = 0;
        nuts = 0;
        overkill_sum = 0;
        base_overkill_sum = 0;
        kills = 0;
        squirrel_spawns = 0;
        rp_instance = new double[7];
        dmg_sum.clear();
        hit_chance_sum.clear();
        debuff_chance_sum.clear();
        casts_total.clear();
        hits_total.clear();
        dot_sum.clear();
        cores.clear();
        base_overkill.clear();
        analyzed.clear();
        deaths.clear();
        used_debuffed.clear();
    }

    public int calcGrade(double percent) {
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
        return grade;
    }

    public void recordKill(Enemy e) {
        deaths.put(e.name, deaths.getOrDefault(e.name, 0) + 1);
    }

    public void recordOverkill(Enemy e, Player player, double base_overkill) {
        double percent = -e.hp / e.getHp_max() * 100;
        double base_percent = -base_overkill / e.getHp_max() * 100;
        overkill_sum += percent;
        base_overkill_sum += base_percent;
        kills++;
        int grade = calcGrade(percent);
        int base_grade = calcGrade(base_percent);
        if (grade >= 0) {
            addCore(e.name, grade, player);
        }
        if (base_grade >= 0) {
            addBaseOverkill(e.name, base_grade, player);
        }
    }

    public void recordAnalyze(Enemy e, Player player) {
        double squirrel = e.name.equals("Squirrel Mage") ? 6 : 1;
        double mult = 1 + (player.analyze_mult + player.analyze_buff);
        double amount = mult * squirrel;
        player.bestiary.put(e.name, player.bestiary.getOrDefault(e.name, 0.0) + amount);
        analyzed.put(e.name, analyzed.getOrDefault(e.name, 0.0) + amount);
    }

    public String getKillCount() {
        int total = 0;
        int squirrels = 0;
        for (String enemy : deaths.keySet()) {
            if (enemy.equals("Squirrel Mage")) {
                squirrels += deaths.get(enemy);
            } else {
                total += deaths.get(enemy);
            }
        }
        if (game_version >= 1620) {
            return total + ", squirrels: " + squirrels + "/" + squirrel_spawns;
        } else {
            return String.valueOf(total);
        }
    }

    public void addBaseOverkill(String name, int grade, Player p) {
        if (!base_overkill.containsKey(name)) {
            HashMap<Integer, Double> nested = new HashMap<>();
            for (int i = 0; i < 5; i++) {
                nested.put(i, 0.0);
            }
            base_overkill.put(name, nested);
        }
        base_overkill.get(name).put(grade, base_overkill.get(name).get(grade) + 1);
//        base_overkill.get(name).merge(grade, 1.0, Double::sum);
    }

    public void addCore(String name, int grade, Player p) {
        double mult = 1;
        double r_mult = 1;
        double research_bonus = 1 + p.core_drop_research;
        if (name.equals("Squirrel Mage")) {
            mult = p.getSquirrelMult(p.zone.getLvl());
        }
//        if (p.enemy_min_lvl_enabled && (game_version == 1638 || !name.equals("Squirrel Mage"))) {
//            r_mult = 1 + 0.005 * p.enemy_min_lvl;
//        }
        if (p.enemy_min_lvl_enabled) {
            r_mult = 1 + 0.005 * p.enemy_min_lvl;
        }
        double bestiary = 1 + 0.01 * p.getBestiaryMedals(25000);
        bestiary *= 1 + p.getBestiaryBonus(name);
        double drop_rate = 0.01 * (p.set_core * 1.5 + p.core_mult
                + research_bonus * r_mult) * mult * p.hard_reward * bestiary;
        if (!cores.containsKey(name)) {
            HashMap<Integer, Double> nested = new HashMap<>();
            for (int i = 0; i < 9; i++) {
                nested.put(i, 0.0);
            }
            cores.put(name, nested);
        }
        base_rp += getCoreRP(grade, name) * 0.01 * p.hard_reward * mult;
        double fractional = p.core_quality_research - (int) p.core_quality_research;
        int new_grade = Math.min(8, grade + (int) p.core_quality_research);
        double gain = 0;
        if (fractional > 0 && new_grade < 8) {
            double count = 1 - fractional;
            cores.get(name).merge(new_grade, count, Double::sum);
            gain += getCoreRP(new_grade, name) * drop_rate * count;
            cores.get(name).merge(new_grade + 1, fractional, Double::sum);
            gain += getCoreRP(new_grade + 1, name) * drop_rate * fractional;
        } else {
            cores.get(name).merge(new_grade, 1.0, Double::sum);
            gain += getCoreRP(new_grade, name) * drop_rate;
        }
        if (p.lvling && Main.game_version >= 1574) p.rp_balance += gain;
        gained_rp += gain;
//        addCoreRandom(name, grade, p);
    }

    public void addCoreRandom(String name, int grade, Player p) {
        int research = p.research_lvls.get("Core quality").intValue();
        double drop_rate = 0.01 * (p.set_core * 1.5 + p.core_mult
                + 0.01 * p.research_lvls.getOrDefault("Core drop", 0.0).intValue()) * p.hard_reward;
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
            int divide = name.contains("Squirrel Mage") ? squirrel_spawns : simulations;
            if (hit_chance_sum.containsKey(name)) {
                double average_hit_chance = hit_chance_sum.getOrDefault(name, 0.0) / casts_total.getOrDefault(name, 0);
                double average_dmg = dmg_sum.getOrDefault(name, 0.0) / hits_total.getOrDefault(name, 0);
                double average_debuff_chance =
                        debuff_chance_sum.getOrDefault(name, 0.0) / hits_total.getOrDefault(name, 0);
                double average_dot = dot_sum.getOrDefault(name, 0.0) / hits_total.getOrDefault(name, 0);
                double average_used = (double) casts_total.getOrDefault(name, 0) / divide;
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
                double average_used = (double) casts_total.getOrDefault(name, 0) / divide;
                sb.append(name).append(" used: ").append(df2.format(average_used)).append("\n");
            }
        }
        return sb.toString();
    }

    public String getBaseOverkill(Player p, double time, boolean offline) {
        StringBuilder sb = new StringBuilder();
        sb.append("Base overkill:").append("\n");
        for (String name : base_overkill.keySet()) {
            double total_count = base_overkill.get(name).values().stream().mapToDouble(Double::doubleValue).sum();
            if (total_count > 0) {
                sb.append(name).append(": ");
                boolean first = true;
                for (Integer grade : base_overkill.get(name).keySet()) {
                    double count = base_overkill.get(name).get(grade);
                    if (count > 0){
                        if (first) {
                            first = false;
                        } else {
                            sb.append("; ");
                        }
                        sb.append(getCoreGrade(grade)).append(":").append((int) (count / total_count * 100)).append("%");
                    }
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public String getCoreData(Player p, double time) {
        StringBuilder sb = new StringBuilder();
        StringBuilder pe = new StringBuilder();
        double research_bonus = 1 + p.core_drop_research;

        if (p.active_skills.get("Analyze").used > 0) {
            sb.append("Analyzed: \n");
            for (String name : analyzed.keySet()) {
                sb.append(name).append(": ").append(df2.format(analyzed.get(name)));
                sb.append("\n");
            }
        } else {
            sb.append("Cores: \n");
            for (String name : cores.keySet()) {
                LinkedHashMap<String,Double> grades = new LinkedHashMap<>();
                double mult = 1;
                double r_mult = 1;
                double per_enemy = 0;
                if (name.equals("Squirrel Mage")) {
                    mult = p.getSquirrelMult(p.zone.getLvl());
                }
                if (p.enemy_min_lvl_enabled && (game_version == 1638 || !name.equals("Squirrel Mage"))) {
                    r_mult = 1 + 0.005 * p.enemy_min_lvl;
                }
                double bestiary = 1 + 0.01 * p.getBestiaryMedals(25000);
                bestiary *= 1 + p.getBestiaryBonus(name);
                double drop_rate = 0.01 * (p.set_core * 1.5 + p.core_mult
                        + research_bonus * r_mult) * mult * p.hard_reward * bestiary;
                double total_count = 0;
                sb.append(name).append(": ");

                for (Integer grade : cores.get(name).keySet()) {
                    double count = cores.get(name).get(grade) * drop_rate;
                    if (count > 0) {
                        total_count += count;
                        grades.put(getCoreGrade(grade), count);
                        per_enemy += getCoreRP(grade, name) * count;
                    }
                }
                sb.append(df2.format(total_count)).append(" (");
                boolean first = true;
                for (String grade : grades.keySet()) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append("; ");
                    }
                    sb.append(grade).append(":").append((int) (grades.get(grade)/total_count*100)).append("%");
                }

                sb.append(")\n");
                pe.append(name).append(": ").append(df2.format(per_enemy / time * 3600)).append(" rp/h\n");
            }
            sb.append("RP/h: ").append(df2.format(gained_rp / time * 3600));
            sb.append(" (base: ").append(df2.format(base_rp / time * 3600)).append(")");
        }
        sb.append("\n");
        sb.append(pe);
//        if (nuts > 0) {
//            sb.append("Chestnuts: ").append(df2.format(nuts)).append("\n");
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
            case "Shax" -> Main.game_version < 1568 ? 40 : 45;
            case "Dagon" -> 60;
            case "Lamia" -> 70;
            case "Tyrant" -> 100;
            case "Fairy" -> Main.game_version < 1568 ? 175 : 350;
            case "Raum" -> Main.game_version < 1568 ? 150 : (Main.game_version < 1574 ? 180 : 230);
            case "Asura" -> 165;
            case "Squirrel Mage" -> Main.game_version < 1621 ? 2000 : (Main.game_version < 1622 ? 3000 : 2500);
            case "Caco" -> 1500;
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
