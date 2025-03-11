package Disilon;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

public enum Zone {
    z6("Tengu"),
    z7("Amon"),
    z8("Tengu/Amon/Akuma"),
    z9("Devil"),
    z10("Shax"),
    z11("Dagon"),
    z12("Lamia");

    final String display_name;
    final String[] possible_enemies;
    public ArrayList<Enemy> enemies = new ArrayList<>(1);
    final int max_enemies;
    private final Random random = new Random();
    public double strength;

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
                incrementStrength();
            }
        }
        System.out.println(enemies.stream().map(Enemy::getName).collect(Collectors.joining(", ")));
        System.out.println(getAvgSpeed());
    }

    public double getAvgSpeed() {
        return enemies.stream().mapToDouble(Enemy::getSpeed).average().getAsDouble();
    }

    public void incrementStrength() {
        strength += 0.01;
        if (strength > 1.1) strength = 0.9;
    }

    public double getTime_to_respawn() {
        return switch (this) {
            case z6 -> 5;
            case z7 -> 5;
            case z8 -> 5;
            case z9 -> 5;
            case z10 -> 6;
            case z11 -> 6;
            case z12 -> 6;
        };
    }
}
