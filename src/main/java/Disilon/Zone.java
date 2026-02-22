package Disilon;

import java.util.Arrays;
import java.util.LinkedHashMap;

import static Disilon.Main.df2;
import static Disilon.Main.game_version;
import static Disilon.Main.random;

public enum Zone {
    z1("Slime", 1),
    z2("Slime/Imp/Goblin", 10, 1, 1),
    z3("Goblin/Imp/Ghoul", 20, 1, 2),
    z4("Wraith/Ghoul", 30, 1, 2),
    z5("Astaroth/Shinigami", 40, 1, 2),
    z6("Tengu", 50),
    z7("Amon", 50),
    z8("Tengu/Amon/Akuma", 50, 2, 3),
    z9("Devil", 90),
    z10("Shax", 100),
    z11("Dagon", 100),
    z12("Lamia", 100),
    z13("Tyrant", 125),
    z14("Fairy", 150),
    z15("Raum", 175),
    z16("Asura", 200, 2, 4),
    test("Caco", 250),
    Dummy("Dummy", 100),
    HelplessDummy("Dummy", 100);

    final String display_name;
    final String[] possible_enemies;
    public final Enemy[] enemies = new Enemy[9];
    public final int min_enemies;
    public final int max_enemies;
    public final int level;
    public double strength;
    public double hard_hp = 1;
    public double hard_stats = 1;
    public final MonsterStatData stats;
    public int enemy_num;
    public int initial_seed;
    public double squirrel_counter = 0;
    public boolean disable_squirrel_passive = false;

    Zone(String enemies, int lvl) {
        this(enemies, lvl, 1, 1);
    }

    Zone(String enemies, int lvl, int min, int max) {
        for (int i = 0; i < 9; i++) {
            this.enemies[i] = new Enemy();
        }
        level = lvl;
        min_enemies = min;
        max_enemies = max;
        this.display_name = this.name() + "(" + enemies + ")";
        possible_enemies = new String[enemies.split("/").length];
        System.arraycopy(enemies.split("/"), 0, possible_enemies, 0, enemies.split("/").length);
        stats = new MonsterStatData(possible_enemies);
        strength = 0.9;
        squirrel_counter = 0;
//        rerollSeed();
    }

    public void rerollSeed() {
        if (max_enemies != 1) {
            enemy_num = random.nextInt(min_enemies, max_enemies + 1);
        } else {
            enemy_num = 1;
        }
        strength = 0.9;
        initial_seed = enemy_num;
    }

    public void incrementEnemyNum() {
        enemy_num = enemy_num < max_enemies ? enemy_num + 1 : min_enemies;
    }

    public void respawn(double squirrel_threshold, int min_lvl) {
        clear();
        if (squirrel_counter >= squirrel_threshold && game_version >= 1620) {
            enemies[0].makeSquirrel(getLvl());
            if (disable_squirrel_passive) {
                enemies[0].passives.get("Dodge").enabled = false;
            }
            enemies[0].strength = 1;
            enemies[0].reroll(level, 0, hard_hp, hard_stats);
            squirrel_counter -= squirrel_threshold;
            stats.squirrel_spawns++;
        } else {
            for (int i = 0; i < enemy_num; i++) {
                enemies[i].setEnemy(possible_enemies[random.nextInt(0, possible_enemies.length)]);
            }
            double individual_str_add = 0;
            for (Enemy e : enemies) {
                if (e.active) {
                    e.strength = strength + individual_str_add;
                    e.reroll(level, min_lvl, hard_hp, hard_stats);
                    if (strength > 1) {
                        individual_str_add -= 0.02;
                    } else {
                        individual_str_add += 0.02;
                    }
                    if (this == HelplessDummy) {
                        e.atk = 1;
                        e.intel = 1;
                    }
                }
            }
            incrementStrength();
            incrementEnemyNum();
//        System.out.println(Arrays.stream(enemies).filter(e -> e.active).map(Enemy::getName).collect(Collectors.joining(", ")));
//        System.out.println(Arrays.stream(enemies).map(Enemy::getHp_max_string).collect(Collectors.joining(", ")));
        }
    }

    public double getAvgSpeed() {
        double sum = 0;
        int counter = 0;
        for (int i = 0; i < 9; i++) {
            if (enemies[i].active) {
                counter++;
                sum += enemies[i].getSpeed();
            }
        }
        if (counter > 0) {
            return sum / counter;
        } else {
            return 0;
        }
    }

    public double stealthDelay() {
        double sum = 0;
        int counter = 0;
        for (int i = 0; i < 9; i++) {
            if (enemies[i].active) {
                counter++;
                sum += enemies[i].stealthDelay();
            }
        }
        if (counter > 0) {
            return sum / counter;
        } else {
            return 0;
        }
    }

    public void incrementStrength() {
        strength += 0.01;
        if (strength > 1.1) strength = 0.9;
    }

    public int getLvl() {
        return level;
//        return switch (this) {
//            case z1 -> 1;
//            case z2 -> 10;
//            case z3 -> 20;
//            case z4 -> 30;
//            case z5 -> 40;
//            case z6, z7, z8 -> 50;
//            case z9 -> 90;
//            case z10, z11, z12 -> 100;
//            case z13 -> 125;
//            case z14 -> 150;
//            case z15 -> 175;
//            case z16 -> 200;
//            default -> 200;
//        };
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
            return Arrays.stream(enemies).filter(e -> e.active).filter(e -> e.buff_count() > 0).findFirst().get();
        }
        if (skill.equals("Back Stab")) {
            return Arrays.stream(enemies).filter(e -> e.active).filter(e -> e.smoked || e.bound > 0).findFirst().orElse(getRandomEnemy());
        }
        return getRandomEnemy();
    }

    public Enemy getRandomEnemy() {
        int index = random.nextInt(aliveEnemies()); //todo:implement mark for targeting
//        System.out.println("Alive: " + aliveEnemies() + " picked index: " + index);
        for (int i = 0; i < 9; i++) {
            if (enemies[i].active) {
                if (index == 0) {
//                    System.out.println("i=" + i);
                    return enemies[i];
                }
                index--;
            }
        }
        return null;
    }

    public double calculateDelta() {
        double delta = 3600;
        for (Enemy enemy : enemies) {
            if (enemy.active && enemy.casting != null) delta = Math.min(delta, enemy.casting.calculate_delta(enemy));
        }
        return delta;
    }

    public LinkedHashMap<Actor, Integer> getRandomTargets(int hits, Actor initial_target) {
        LinkedHashMap<Actor, Integer> targets = new LinkedHashMap<>();
//        Actor e = initial_target;
//        targets.put(e, targets.getOrDefault(e, 0) + 1);
        for (int i = 1; i < hits; i++) {
            Actor e = getRandomEnemy();
//            if (Math.random() < 0.5) e = getRandomEnemy();
            targets.put(e, targets.getOrDefault(e, 0) + 1);
        }
        return targets;
    }

    public LinkedHashMap<Actor, Integer> getRandomTargets_chance(int hits, Actor initial_target) {
        LinkedHashMap<Actor, Integer> targets = new LinkedHashMap<>();
//        Actor e = initial_target;
//        targets.put(e, targets.getOrDefault(e, 0) + 1);
        int alive = aliveEnemies();
        int counter = 0;
        while (counter < hits) {
            for (Enemy e : enemies) {
                if (e.active && Math.random() < 1.0 / alive) {
                    targets.put(e, targets.getOrDefault(e, 0) + 1);
                    counter++;
                }
                if (counter >= hits) break;
            }
        }
//        System.out.println(counter);
        return targets;
    }

    public double getMaxEnemyHp() {
        double max = 0;
        for (Enemy e : enemies) {
            if (e.active) {
                if (e.hp > max) max = e.hp;
            }
        }
        return max;
    }

    public double getMaxEnemyHpPercent() {
        double max = 0;
        for (Enemy e : enemies) {
            if (e.active) {
                if (e.hp / e.getHp_max() > max) max = e.hp / e.getHp_max();
            }
        }
        return max;
    }

    public int getEnemyBuffCount() {
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            if (enemies[i].active) {
                sum += enemies[i].buff_count();
            }
        }
        return sum;
    }

    public double getZoneTimeCap() {
        if (game_version == 1649) return -1;
        return switch (this) {
            case z14 -> 30;
            case z15 -> 20;
            case z16 -> 60;
            case test -> 20;
            default -> -1;
        };
    }

    public double getZoneOfflineMult() {
        return switch (this) {
            case z1, z2, z3, z4, z5, z6, z7, z8, z9, z11, z12 -> 1.03;
            case z13, z14, z15, z16 -> 1.01;
            default -> 1;
        };
    }

    public boolean allowsSquirrel() {
        return switch (this) {
            case test, Dummy, HelplessDummy -> false;
            default -> true;
        };
    }

    @Override
    public String toString() {
        return display_name;
    }

    public boolean cleared() {
        for (Enemy e : enemies) {
            if (e.active) return false;
        }
        return true;
    }

    public void clear() {
        for (Enemy e : enemies) {
            e.active = false;
        }
    }

    public int aliveEnemies() {
        int counter = 0;
        for (Enemy e : enemies) {
            if (e.active) counter++;
        }
        return counter;
    }

    public double getRoughItemDropBonus(Player p) {
        double bonus = 0;
        for (String name : possible_enemies) {
            bonus += p.getBestiaryBonus(name);
        }
        bonus /= possible_enemies.length;
        return bonus;
    }
}
