package Disilon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static Disilon.Main.random;

public enum Zone {
    z4("Wraith/Ghoul"),
    z5("Astaroth/Shinigami"),
    z6("Tengu"),
    z7("Amon"),
    z8("Tengu/Amon/Akuma"),
    z9("Devil"),
    z10("Shax"),
    z11("Dagon"),
    z12("Lamia"),
    Dummy("Dummy"),
    HelplessDummy("Dummy");

    final String display_name;
    final String[] possible_enemies;
    public final ArrayList<Enemy> enemies = new ArrayList<>(1);
    final int max_enemies;
    public double strength;
    private HashMap<String, Double> dmg_sum = new HashMap<>();
    private HashMap<String, Double> hit_sum = new HashMap<>();
    private HashMap<String, Double> debuff_sum = new HashMap<>();
    private HashMap<String, Double> dot_sum = new HashMap<>();
    private HashMap<String, Double> casts = new HashMap<>();
    private HashMap<String, Double> hits = new HashMap<>();

    Zone(String enemies) {
        this.display_name = this.name() + "(" + enemies + ")";
        max_enemies = enemies.split("/").length;
        possible_enemies = new String[max_enemies];
        System.arraycopy(enemies.split("/"), 0, possible_enemies, 0, max_enemies);
        strength = 0.9;
    }

    @Override
    public String toString() {
        return display_name;
    }

    public void respawn() {
        enemies.clear();
        if (Main.game_version >= 1535) {
            incrementStrength();
        }
        if (max_enemies == 1) {
            Enemy e = new Enemy();
            e.setEnemy(possible_enemies[0]);
            enemies.add(e);
        } else {
            int number = random.nextInt(max_enemies - 1, max_enemies + 1);
            for (int i = 0; i < number; i++) {
                Enemy e = new Enemy();
                e.setEnemy(possible_enemies[random.nextInt(0, max_enemies)]);
                enemies.add(e);
            }
        }
        for (Enemy e : enemies) {
            if (Main.game_version < 1535) {
                e.rollStrength();
                e.reroll();
            } else {
                e.strength = strength;
                e.reroll();
            }
            if (this == HelplessDummy) {
                e.atk = 1;
                e.intel = 1;
            }
        }
//        System.out.println(enemies.stream().map(Enemy::getName).collect(Collectors.joining(", ")));
    }

    public double getAvgSpeed() {
        return enemies.stream().mapToDouble(Enemy::getSpeed).average().getAsDouble();
    }

    public double stealthDelay() {
        return enemies.stream().mapToDouble(Enemy::stealthDelay).max().getAsDouble();
    }

    public void incrementStrength() {
        strength += 0.01;
        if (strength > 1.1) strength = 0.9;
    }

    public double getTime_to_respawn() {
        return switch (this) {
            case z4 -> 4;
            case z5 -> 4.5;
            case z6, z7, z8, z9 -> 5;
            case z10, z11, z12 -> 6;
            default -> 6;
        };
    }

    public Enemy getRandomEnemy() {
        int index = random.nextInt(enemies.size()); //todo:implement mark for targeting
        return enemies.get(index);
    }

    public double calculateDelta() {
        double delta = 3600;
        for (Enemy enemy : enemies) {
            if (enemy.casting != null) delta = Math.min(delta, enemy.casting.calculate_delta());
        }
        return delta;
    }

    public LinkedHashMap<Enemy, Integer> getRandomTargets(int hits) {
        LinkedHashMap<Enemy, Integer> targets = new LinkedHashMap<>();
        for (int i = 0; i < hits; i++) {
            Enemy e = getRandomEnemy();
            targets.put(e, targets.containsKey(e) ? targets.get(e) + 1 : 1);
        }
        return targets;
    }

    private void incrementStats(Actor e, ActiveSkill s, double dmg, double hit, double debuff, int uses, int h,
                                double dot) {
        String key = e.getName() + ": " + s.name;
        dmg_sum.put(key, dmg_sum.containsKey(key) ? dmg_sum.get(key) + dmg : dmg);
        hit_sum.put(key, hit_sum.containsKey(key) ? hit_sum.get(key) + hit : hit);
        debuff_sum.put(key, debuff_sum.containsKey(key) ? debuff_sum.get(key) + debuff : debuff);
        casts.put(key, casts.containsKey(key) ? casts.get(key) + uses : uses);
        hits.put(key, hits.containsKey(key) ? hits.get(key) + h : h);
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
        hit_sum.clear();
        debuff_sum.clear();
        casts.clear();
        hits.clear();
        dot_sum.clear();
    }

    public String getRecordedData() {
        StringBuilder sb = new StringBuilder();
        for (String name : hit_sum.keySet()) {
            double average_hit_chance = hit_sum.get(name) / casts.get(name);
            double average_dmg = dmg_sum.get(name) / hits.get(name);
            double average_debuff_chance = debuff_sum.get(name) / hits.get(name);
            double average_dot = dot_sum.get(name) / hits.get(name);
            sb.append(name).append(" hit: ").append((int) (average_hit_chance * 100)).append("%");
            sb.append("; dmg: ").append((int) average_dmg);
            sb.append(average_debuff_chance == 0 ? "" : "; debuff: " + (int) (average_debuff_chance * 100) + "%");
            sb.append(average_dot == 0 ? "" : "; DoT: " + (int) (average_dot));
            sb.append("\n");
        }
        return sb.toString();
    }
}
