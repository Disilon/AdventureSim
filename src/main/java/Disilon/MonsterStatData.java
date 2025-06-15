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
    public HashMap<String, Integer> overkill_1 = new HashMap<>();
    public HashMap<String, Integer> overkill_10 = new HashMap<>();
    public HashMap<String, Integer> overkill_25 = new HashMap<>();
    public HashMap<String, Integer> overkill_50 = new HashMap<>();
    public HashMap<String, Integer> overkill_200 = new HashMap<>();
    public HashMap<String, Integer> deaths = new HashMap<>();

    private void incrementStats(Actor e, ActiveSkill s, double dmg, double hit, double debuff, int uses, int h,
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
        overkill_1.clear();
        overkill_10.clear();
        overkill_25.clear();
        overkill_50.clear();
        overkill_200.clear();
        deaths.clear();
    }

    public void recordOverkill(Actor e) {
        double percent = - e.getHp() / e.getHp_max() * 100;
        if (percent <= 1) {
            overkill_1.put(e.getName(), overkill_1.containsKey(e.getName()) ? overkill_1.get(e.getName()) + 1 : 1);
        } else if (percent <= 10) {
            overkill_10.put(e.getName(), overkill_10.containsKey(e.getName()) ? overkill_10.get(e.getName()) + 1 : 1);
        } else if (percent <= 25) {
            overkill_25.put(e.getName(), overkill_25.containsKey(e.getName()) ? overkill_25.get(e.getName()) + 1 : 1);
        } else if (percent <= 50) {
            overkill_50.put(e.getName(), overkill_50.containsKey(e.getName()) ? overkill_50.get(e.getName()) + 1 : 1);
        } else if (percent <= 200) {
            overkill_200.put(e.getName(), overkill_200.containsKey(e.getName()) ? overkill_200.get(e.getName()) + 1 : 1);
        }
        deaths.put(e.getName(), deaths.containsKey(e.getName()) ? deaths.get(e.getName()) + 1 : 1);
    }

    public String getSkillData() {
        StringBuilder sb = new StringBuilder();
        for (String name : hit_chance_sum.keySet()) {
            double average_hit_chance = hit_chance_sum.get(name) / casts_total.get(name);
            double average_dmg = dmg_sum.get(name) / hits_total.get(name);
            double average_debuff_chance = debuff_chance_sum.get(name) / hits_total.get(name);
            double average_dot = dot_sum.get(name) / hits_total.get(name);
            sb.append(name).append(" hit: ").append((int) (average_hit_chance * 100)).append("%");
            sb.append("; dmg: ").append((int) average_dmg);
            sb.append(average_debuff_chance == 0 ? "" : "; debuff: " + (int) (average_debuff_chance * 100) + "%");
            sb.append(average_dot == 0 ? "" : "; DoT: " + (int) (average_dot));
            sb.append("\n");
        }
        return sb.toString();
    }

    public String getCoreData(Player p, double time) {
        StringBuilder sb = new StringBuilder();
        double rp = 0;
        double drop_rate = 0.01 * p.core_mult;
        for (String name : deaths.keySet()) {
            sb.append(name).append(": ");
            if (overkill_200.containsKey(name)) {
                int grade = 0;
                double count = (double) overkill_200.get(name) * drop_rate;
                rp += getCoreRP(grade, name) * count;
                sb.append("F: ").append(df2.format(count));
            }
            if (overkill_50.containsKey(name)) {
                int grade = 1;
                double count = (double) overkill_50.get(name) * drop_rate;
                rp += getCoreRP(grade, name) * count;
                sb.append("; E: ").append(df2.format(count));
            }
            if (overkill_25.containsKey(name)) {
                int grade = 2;
                double count = (double) overkill_25.get(name) * drop_rate;
                rp += getCoreRP(grade, name) * count;
                sb.append("; D: ").append(df2.format(count));
            }
            if (overkill_10.containsKey(name)) {
                int grade = 3;
                double count = (double) overkill_10.get(name) * drop_rate;
                rp += getCoreRP(grade, name) * count;
                sb.append("; C: ").append(df2.format(count));
            }
            if (overkill_1.containsKey(name)) {
                int grade = 4;
                double count = (double) overkill_1.get(name) * drop_rate;
                rp += getCoreRP(grade, name) * count;
                sb.append("; B: ").append(df2.format(count));
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
            default -> 0;
        };
    }
}
