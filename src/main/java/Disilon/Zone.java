package Disilon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static Disilon.Main.df2;
import static Disilon.Main.random;

public enum Zone {
    z1("Slime"),
    z2("Slime2/Imp/Goblin"),
    z3("Goblin2/Imp2/Ghoul"),
    z4("Wraith/Ghoul2"),
    z5("Astaroth/Shinigami"),
    z6("Tengu"),
    z7("Amon"),
    z8("Tengu/Amon/Akuma"),
    z9("Devil"),
    z10("Shax"),
    z11("Dagon"),
    z12("Lamia"),
    z13("Tyrant"),
    z14("Fairy"),
    z15("Raum"),
    z16("Asura"),
    Dummy("Dummy"),
    HelplessDummy("Dummy");

    final String display_name;
    final String[] possible_enemies;
    public final ArrayList<Enemy> enemies = new ArrayList<>(1);
    public int max_enemies;
    public double strength;
    public final MonsterStatData stats;

    Zone(String enemies) {
        this.display_name = this.name() + "(" + enemies + ")";
        max_enemies = enemies.split("/").length;
        possible_enemies = new String[max_enemies];
        System.arraycopy(enemies.split("/"), 0, possible_enemies, 0, max_enemies);
        stats = new MonsterStatData(possible_enemies);
        strength = 0.9;
    }

    @Override
    public String toString() {
        return display_name;
    }

    public void respawn() {
        enemies.clear();
        if (this == z16) {
            int number = random.nextInt(2, 5);
            max_enemies = 4;
            for (int i = 0; i < number; i++) {
                Enemy e = new Enemy();
                e.setEnemy(possible_enemies[0]);
                enemies.add(e);
            }
        } else {
            if (max_enemies == 1) {
                Enemy e = new Enemy();
                e.setEnemy(possible_enemies[0]);
                enemies.add(e);
            } else {
                int number = random.nextInt(max_enemies - 1, max_enemies + 1);
                if (this == z2) number = 1;
                if (this == z3) random.nextInt(1, 2);
                for (int i = 0; i < number; i++) {
                    Enemy e = new Enemy();
                    e.setEnemy(possible_enemies[random.nextInt(0, max_enemies)]);
                    enemies.add(e);
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        double individual_str_add = 0;
        if (strength == 0.9) strength += 0.02 * (max_enemies - 1);
        for (Enemy e : enemies) {
            if (Main.game_version < 1535) {
                e.rollStrength();
                e.reroll();
            } else {
                e.strength = strength + individual_str_add;
                sb.append(df2.format(e.strength * 100)).append("; ");
                e.reroll();
                individual_str_add -= 0.02;
            }
            if (this == HelplessDummy) {
                e.atk = 1;
                e.intel = 1;
            }
        }
        incrementStrength();
//        System.out.println(sb);
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
            case z1 -> 2.5;
            case z2 -> 3;
            case z3 -> 3.5;
            case z4 -> 4;
            case z5 -> 4.5;
            case z6, z7, z8, z9 -> 5;
            case z10, z11, z12, z13, z14, z15 -> 6;
            case z16 -> 7.5;
            default -> 6;
        };
    }

    public Enemy getRandomEnemy(String skill) {
        if (skill.equals("Dispel")) {
            return enemies.stream().filter(e -> e.buff_count() > 0).findFirst().get();
        } else {
            return getRandomEnemy();
        }
    }

    public Enemy getRandomEnemy() {
        int index = random.nextInt(enemies.size()); //todo:implement mark for targeting
        return enemies.get(index);
    }

    public double calculateDelta() {
        double delta = 3600;
        for (Enemy enemy : enemies) {
            if (enemy.casting != null) delta = Math.min(delta, enemy.casting.calculate_delta(enemy));
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

    public double getMaxEnemyHp() {
        double max = 0;
        for (Enemy e : enemies) {
            if (e.getHp() > max) max = e.getHp();
        }
        return max;
    }

    public double getMaxEnemyHpPercent() {
        double max = 0;
        for (Enemy e : enemies) {
            if (e.getHp() / e.getHp_max() > max) max = e.getHp() / e.getHp_max();
        }
        return max;
    }

    public int getEnemyBuffCount() {
        return enemies.stream().mapToInt(Enemy::buff_count).sum();
    }

    public double getZoneTimeCap() {
        return switch (this) {
            case z14 -> 30;
            case z15 -> 20;
            case z16 -> 60;
            default -> -1;
        };
    }

    public double getZoneOfflineMult() {
        return switch (this) {
            case z1, z2, z3, z4, z5, z6, z7, z8, z9, z11, z12 -> 1.03;
            default -> 1;
        };
    }
}
