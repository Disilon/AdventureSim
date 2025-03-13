package Disilon;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static Disilon.Main.df2;

public class Simulation {
    enum StatusType {death, respawn, combat, prepare, rerolling, delay}

    int sim_limit;
    double time_limit;
    int cl_limit;
    Player player;
    Zone zone;
    Potion potion1;
    Potion potion2;
    Potion potion3;
    StatusType status = StatusType.respawn;
    String title = "Default simulation.";
    int crafting_lvl = 20;
    int alchemy_lvl = 20;
    int sim_type = 1;
    double time_to_respawn = -1;
    String full_result = "";
    String essential_result = "";

    public Simulation() {
    }

    public void setupPotions(String type1, int tier1, int threshold1, String type2, int tier2, int threshold2,
                             String type3, int tier3, int threshold3) {
        if (type1 != null) potion1 = new Potion(type1, tier1, threshold1);
        if (type2 != null) potion2 = new Potion(type2, tier2, threshold2);
        if (type3 != null) potion3 = new Potion(type3, tier3, threshold3);
    }

    public void run(String skill1, double lvl1, SkillMod mod1, int setting1, String skill2, double lvl2, SkillMod mod2,
                    int setting2, String skill3, double lvl3, SkillMod mod3,
                    int setting3, double reroll) {
        int prepare_threshold = 0;
        player.eblast_enabled = skill1.equals("Elemental Blast") || skill2.equals("Elemental Blast") || skill3.equals("Elemental Blast");
        player.holylight_enabled = skill1.equals("Holy Light") || skill2.equals("Holy Light") || skill3.equals("Holy Light");
        player.aurablade_enabled = skill1.equals("Aura Blade") || skill2.equals("Aura Blade") || skill3.equals("Aura Blade");
        player.prepare = null;
        ActiveSkill s1 = player.getSkill(skill1);
        s1.setSkill(lvl1, mod1);
        s1.old_lvl = (int) lvl1;
        ActiveSkill s2 = null;
        if (!skill2.equals("Prepare")) {
            s2 = player.getSkill(skill2);
            if (s2 != null) {
                s2.setSkill(lvl2, mod2);
                s2.old_lvl = (int) lvl2;
            }
        } else {
            player.prepare = player.getSkill(skill2);
            player.prepare.setSkill(lvl2, SkillMod.Basic);
            player.prepare.old_lvl = (int) lvl2;
            prepare_threshold = setting2;
        }
        ActiveSkill s3 = null;
        if (!skill3.equals("Prepare")) {
            s3 = player.getSkill(skill3);
            if (s3 != null) {
                s3.setSkill(lvl3, mod3);
                s3.old_lvl = (int) lvl3;
            }
        } else {
            player.prepare = player.getSkill(skill3);
            player.prepare.setSkill(lvl3, SkillMod.Basic);
            player.prepare.old_lvl = (int) lvl3;
            prepare_threshold = setting3;
        }
        run(s1, setting1, s2, setting2, s3, setting3, prepare_threshold,
                reroll);
    }

    public void run(ActiveSkill skill1, int setting1, ActiveSkill skill2, int setting2, ActiveSkill skill3,
                    int setting3, double prepare_threshold, double reroll) {
        double exp = 0;
        double total_time = 0;
        double death_time = 0;
        double prepare_time = 0;
        double crafting_time = 0;
        double min_time = 9999;
        double max_time = 0;
        int total_casts = 0;
        int min_casts = 999;
        int max_casts = 0;
        double overkill = 0;
        double ignore_deaths = 0;
        double enemy_dmg = 0;
        int enemy_hits = 0;
        double healed = 0;
        int kills = 0;
        int cleared = 0;
        double oom_time = 0;
        title = zone.toString();
        if (time_to_respawn == -1) time_to_respawn = zone.getTime_to_respawn();
        if (potion1 != null) potion1.used = 0;
        if (potion2 != null) potion2.used = 0;
        if (potion3 != null) potion3.used = 0;
        StringBuilder result = new StringBuilder();
        StringBuilder setup = new StringBuilder();
        StringBuilder lvling_log = new StringBuilder();
        setup.append(title).append("\n");
        setup.append(player.getName()).append(" ML/CL ").append(player.getMl()).append("/").append(player.getCl()).append("\n");
        setup.append("Active Skills: \n").append(skill1.name).append(" lvl ").append(skill1.lvl).append(" ").append(skill1.skillMod).append(" / ").append(setting1).append("\n");
        if (skill2 != null)
            setup.append(skill2.name).append(" lvl ").append(skill2.lvl).append(" ").append(skill2.skillMod).append(" / ").append(setting2).append("\n");
        if (skill3 != null)
            setup.append(skill3.name).append(" lvl ").append(skill3.lvl).append(" ").append(skill3.skillMod).append(" / ").append(setting3).append("\n");
        setup.append("Passive skills:\n");
        for (Map.Entry<String, PassiveSkill> passive : player.passives.entrySet()) {
            if (passive.getValue().enabled) {
                setup.append(passive.getValue().name).append(" ").append(passive.getValue().lvl).append("\n");
            }
        }
        if (player.prepare != null) setup.append("Prepare threshold: ").append(prepare_threshold).append("\n");
        if (potion1 != null)
            setup.append(potion1.type.toUpperCase()).append(" potion tier: ").append(potion1.tier).append(", threshold: ").append(potion1.threshold).append(
                    "\n");
        if (potion2 != null)
            setup.append(potion2.type.toUpperCase()).append(" potion tier: ").append(potion2.tier).append(", threshold: ").append(potion2.threshold).append(
                    "\n");
        if (potion3 != null)
            setup.append(potion3.type.toUpperCase()).append(" potion tier: ").append(potion3.tier).append(", threshold: ").append(potion3.threshold).append(
                    "\n");
        player.debuffs.clear();
        player.buffs.clear();
        boolean end = false;
        while (!end) {
            Enemy target = null;
            double time = 0;
            double delta;
            int casts = 0;
            double delay_left = 0;
            ActiveSkill previous_cast = null;
            status = StatusType.respawn;
            zone.respawn();
            player.checkAmbush();
            if (Main.game_version < 1535 && reroll >= 1) {
                while (zone.getRandomEnemy().getHp_max() >= reroll) { //won't work properly with multiple enemies
                    zone.respawn();
                    delta = time_to_respawn;
                    time += delta;
                    total_time += delta;
                    player.setMp(player.getMp() + player.getMp_regen() * delta);
                    if (potion1 != null) potion1.checkPotion(player, delta);
                    if (potion2 != null) potion2.checkPotion(player, delta);
                    if (potion3 != null) potion3.checkPotion(player, delta);
                }
            }
            skill1.used_in_rotation = 0;
            if (skill2 != null) skill2.used_in_rotation = 0;
            if (skill3 != null) skill3.used_in_rotation = 0;
            while (time < time_to_respawn) {
                delta = Math.clamp(time_to_respawn - time, 0, 0.1);
                time += delta;
                total_time += delta;
                player.setMp(player.getMp() + player.getMp_regen() * delta);
                if (potion1 != null) potion1.checkPotion(player, delta);
                if (potion2 != null) potion2.checkPotion(player, delta);
                if (potion3 != null) potion3.checkPotion(player, delta);
            }
            if (time >= time_to_respawn) {
                status = StatusType.combat;
            }
            while (status == StatusType.combat && time < 3600) {
                if (player.casting == null && skill1.canCast(player) && ((skill3 == null && skill2 == null) || skill1.shouldUse(player,
                        setting1))) {
                    player.casting = skill1;
                    player.casting.startCastPlayer(player, zone);
                    if (skill2 != null) skill2.used_in_rotation = 0;
                    if (skill3 != null) skill3.used_in_rotation = 0;
                }
                if (player.casting == null && skill2 != null && skill2.canCast(player) && skill2.shouldUse(player,
                        setting2)) {
                    player.casting = skill2;
                    player.casting.startCastPlayer(player, zone);
                    //skill1.used_in_rotation = 0;
                }
                if (player.casting == null && skill3 != null && skill3.canCast(player) && skill3.shouldUse(player,
                        setting3)) {
                    player.casting = skill3;
                    player.casting.startCastPlayer(player, zone);
                    //skill1.used_in_rotation = 0;

                }
                if (skill3 != null && skill3.used_in_rotation >= setting3) {
                    skill1.used_in_rotation = 0;
                    if (skill2 != null) skill2.used_in_rotation = 0;
                    skill3.used_in_rotation = 0;
                }
                if (skill3 == null && skill2 != null && skill2.used_in_rotation >= setting2) {
                    skill1.used_in_rotation = 0;
                    skill2.used_in_rotation = 0;
                }
                if (player.casting != null) {
//                    System.out.println(time + " " + player.getName() + " casting " + player.casting.name + " at " +
//                            enemy.getName() + " cast time: " + player.casting.cast + " delay: " + player.casting.delay);
                }
                for (Iterator<Enemy> iterator = zone.enemies.iterator(); iterator.hasNext(); ) {
                    Enemy enemy = iterator.next();
                    if (enemy.casting == null) {
                        ActiveSkill enemyCast = enemy.getCasting();
                        enemyCast.startCast(enemy, player);
                        if (player.name.equals("Assassin1") &&
                                enemy.name.equals("Lamia") && enemyCast.name.equals("Explosion") && enemy.getHp() > enemy.getHp_max() * 0.4) {
                            enemy.reroll(); //todo: add user option to run away from lamia explosion
                            player.casting = null;
                            status = StatusType.rerolling;
                            break;
                        }
                    }
                }
                delta = 0.1;
                if (player.casting != null) delta = Math.min(delta, player.casting.calculate_delta());
                delta = Math.min(delta, zone.calculateDelta());
                time += delta;
                total_time += delta;
                player.setMp(player.getMp() + player.getMp_regen() * delta); //todo: implement mana properly for enemies
                if (potion1 != null) potion1.checkPotion(player, delta);
                if (potion2 != null) potion2.checkPotion(player, delta);
                if (potion3 != null) potion3.checkPotion(player, delta);
                if (player.casting != null) {
                    if (player.casting.cast > 0) {
                        if (player.casting.progressCast(delta)) {
                            if (player.casting.hit > 0) {
                                casts++;
                                total_casts++;
                                if (player.casting.aoe) {
                                    for (Enemy enemy : zone.enemies) {
                                        double dmg = player.casting.attack(player, enemy, 0);
                                        if (dmg > 0) {
                                            enemy.setHp(enemy.getHp() - dmg);
//                                    System.out.println("Player dealt " + (int) dmg + " damage with " + player.casting.name);
                                        } else {
//                                    System.out.println("Player missed with " + player.casting.name);
                                        }
                                    }
                                } else {
                                    if (target == null) {
                                        target = zone.getRandomEnemy();
                                        player.ambush_target = player.isAmbushing() ? target : null;
                                    }
                                    if (target != null) {
                                        if (player.casting.random_targets) {
                                            LinkedHashMap<Enemy, Integer> targets =
                                                    zone.getRandomTargets(player.casting.hits);
                                            targets.forEach((key, value) -> {
                                                double dmg = player.casting.attack(player, key, value);
                                                if (dmg > 0) {
                                                    key.setHp(key.getHp() - dmg);
//                                          System.out.println("Player dealt " + (int) dmg + " damage with " + player.casting.name);
                                                } else {
//                                          System.out.println("Player missed with " + player.casting.name);
                                                }
                                            });
                                        } else {
                                            double dmg = player.casting.attack(player, target, 0);
                                            if (dmg > 0) {
                                                target.setHp(target.getHp() - dmg);
//                                          System.out.println("Player dealt " + (int) dmg + " damage with " + player.casting.name);
                                            } else {
//                                          System.out.println("Player missed with " + player.casting.name);
                                            }
                                        }
                                    }
                                }
                            } else {
                                double previous_hp = player.getHp();
                                player.casting.use(player);
                                if (player.casting.heal) {
                                    healed += player.getHp() - previous_hp;
                                }
//                                System.out.println("Player casted " + player.casting.name);
                            }
                            if (player.lvling) player.casting.gainExp();
                            player.setMp(player.getMp() - player.casting.calculate_manacost(player));
                            player.tick_debuffs();
                            player.tick_buffs();
                        }
                    } else if (player.casting.delay > 0) {
                        if (player.casting.progressDelay(delta)) {
                            player.casting = null;
                        }
                    }
                } else {
                    oom_time += delta;
                }
                Iterator<Enemy> iterator = zone.enemies.iterator();
                while (iterator.hasNext()) {
                    Enemy enemy = iterator.next();
                    if (enemy.casting != null) {
                        if (enemy.casting.cast > 0) {
                            if (enemy.casting.progressCast(delta)) {
                                if (enemy.casting.hit > 0) {
                                    double dmg = 0;
                                    if (previous_cast != null && (time - previous_cast.last_casted_at) < 0.5) {
                                        dmg = previous_cast.attack(enemy, player, enemy.casting.hits);
                                    } else {
                                        dmg = enemy.casting.attack(enemy, player, 0);
                                    }
                                    if (previous_cast == null || (time - previous_cast.last_casted_at) >= 0.5) {
                                        previous_cast = enemy.casting;
                                        previous_cast.last_casted_at = time;
                                    }
                                    if (dmg > 0) {
                                        player.setHp(player.getHp() - dmg);
                                        enemy_hits++;
                                        enemy_dmg += dmg;
//                                    System.out.println("Enemy dealt " + (int) dmg + " damage with " + enemy.casting.name);
                                    } else {
//                                    System.out.println("Enemy missed with " + enemy.casting.name);
                                    }
//                                System.out.println("Player: " + (int) player.getHp() + "/" + (int) player.getHp_max() + " " + (int) player.getMp() + "/" + (int) player.getMp_max() + "; Enemy: " + (int) enemy.getHp() + "/" + (int) enemy.getHp_max());
                                } else {
                                    enemy.casting.use(enemy);
                                }
                                enemy.setMp(enemy.getMp() - enemy.casting.calculate_manacost(enemy));
                                enemy.tick_debuffs();
                                enemy.tick_buffs();
                            }
                        } else if (enemy.casting.delay > 0) {
                            if (enemy.casting.progressDelay(delta)) {
                                enemy.casting = null;
                            }
                        }
                    }
                    if (enemy.getHp() <= 0) {
                        overkill -= enemy.getHp();
                        for (Debuff d : enemy.debuffs) {
                            if (d.dmg > 0) overkill += d.getMaxTotalDmg();
                        }
                        double exp_gain = enemy.getExp() * player.getExp_mult();
                        if (player.lvling) player.increment_exp(exp_gain * player.milestone_exp_mult);
                        exp += exp_gain;
                        kills++;
                        if (Main.game_version >= 1535 && player.lvling) player.levelActives();
//                    System.out.println("Enemy killed at " + df2.format(time) + " s");
                        if (target == enemy) target = null;
                        iterator.remove();
                    }
                }
                if (zone.enemies.isEmpty()) {
                    status = StatusType.delay;
                    cleared++;
                    if (player.casting != null) {
                        delay_left = player.casting.delay;
                        player.casting = null;
                    }
                }
                if (player.getHp() <= 0) {
                    status = StatusType.death;
                    death_time += 15 * 60;
                    player.setHp(player.getHp_max());
                    player.setMp(player.getMp_max());
                    if (potion1 != null) potion1.cooldown = 0;
                    if (potion2 != null) potion2.cooldown = 0;
                    if (potion3 != null) potion3.cooldown = 0;
                    ignore_deaths += time;
                    //System.out.println("Player died at " + time);
                }
            }
            if (player.prepare != null && (status == StatusType.respawn || status == StatusType.rerolling || status == StatusType.delay)) {
                delta = 0.1;
                while (player.getHp() / player.getHp_max() < prepare_threshold / 100 || player.getMp() / player.getMp_max() < prepare_threshold / 100) {
                    //status = StatusType.prepare;
                    total_time += delta;
                    prepare_time += delta;
                    delay_left -= delta;
                    if (player.lvling) player.prepare.gainExp(delta);
                    player.setHp(player.getHp() + player.getPrepare_hps() * delta);
                    player.setMp(player.getMp() + player.getPrepare_mps() * delta);
                    if (Main.game_version >= 1534) {
                        if (potion1 != null) potion1.tickPotion(player, delta);
                        if (potion2 != null) potion2.tickPotion(player, delta);
                        if (potion3 != null) potion3.tickPotion(player, delta);
                    }
                }
            }
            while (delay_left > 0) {
                delta = Math.clamp(delay_left, 0, 0.1);
                time += delta;
                total_time += delta;
                delay_left -= delta;
                player.setMp(player.getMp() + player.getMp_regen() * delta);
                if (potion1 != null) potion1.checkPotion(player, delta);
                if (potion2 != null) potion2.checkPotion(player, delta);
                if (potion3 != null) potion3.checkPotion(player, delta);
            }
            if (delay_left <= 0) status = StatusType.respawn;
            if (status == StatusType.respawn) {
                min_time = Math.min(min_time, time);
                max_time = Math.max(max_time, time);
                min_casts = Math.min(min_casts, casts);
                max_casts = Math.max(max_casts, casts);
            }
            if (player.lvling) player.levelPassives(time);
            if (potion1 != null) {
                crafting_time += potion1.calc_time(crafting_lvl, alchemy_lvl);
            }
            if (potion2 != null) {
                crafting_time += potion2.calc_time(crafting_lvl, alchemy_lvl);
            }
            if (potion3 != null) {
                crafting_time += potion3.calc_time(crafting_lvl, alchemy_lvl);
            }
            switch (sim_type) {
                default -> {
                    if (cleared >= sim_limit) end = true;
                }
                case 2 -> {
                    if ((total_time + death_time + crafting_time) >= time_limit * 3600) end = true;
                }
                case 3 -> {
                    if (player.cl >= cl_limit) end = true;
                }
            }
        }

        result.append("Exp/h: ").append((int) (exp * player.milestone_exp_mult / (total_time + death_time) * 3600)).append(" (");
        result.append(df2.format(player.milestone_exp_mult * 100)).append("%)\n");
//        sb.append("Exp/h without milestones: ").append((int) (exp / (total_time + death_time) * 3600)).append("\n");
        if (potion1 != null) {
            result.append(potion1.getRecordedData(total_time + death_time));
        }
        if (potion2 != null) {
            result.append(potion2.getRecordedData(total_time + death_time));
        }
        if (potion3 != null) {
            result.append(potion3.getRecordedData(total_time + death_time));
        }
        if (crafting_time > 0) {
            result.append("Effective exp/h: ").append((int) (exp * player.milestone_exp_mult / (total_time + crafting_time + death_time) * 3600)).append("\n");
        }
        if (player.prepare != null) {
            result.append("Time preparing %: ").append(df2.format(prepare_time / total_time * 100)).append("% " +
                    "\n");
        }
        if (death_time > 0) {
            result.append("Time dead %: ").append(df2.format(death_time / (total_time + death_time) * 100)).append("% \n");
            //result.append("Spawning time: ").append(df2.format(respawning_time)).append("s \n");
            result.append("Exp/h without deaths: ").append((int) (exp * player.milestone_exp_mult / (total_time - ignore_deaths) * 3600)).append(
                    "\n");
        }
        if (oom_time > 0) {
            result.append("Time oom %: ").append(df2.format(oom_time / total_time * 100)).append("% " +
                    "\n");
        }
        result.append("Kills/h without deaths: ").append(df2.format(kills / (total_time - ignore_deaths) * 3600)).append(
                "\n");
        result.append("Time to clear: ").append(df2.format(min_time)).append("s - ").append(df2.format(max_time));
        result.append("s; avg: ").append(df2.format(total_time / cleared)).append("s \n");
        result.append("Skill casts: ").append(min_casts).append(" - ").append(max_casts);
        result.append("; avg: ").append(df2.format((double) total_casts / cleared)).append("\n");
        result.append("Average Overkill: ").append((int) (overkill / kills)).append(" \n");
        if (skill1 != null && skill1.hit > 0) result.append(skill1.getRecordedData());
        if (skill2 != null && skill2.hit > 0) result.append(skill2.getRecordedData());
        if (skill3 != null && skill3.hit > 0) result.append(skill3.getRecordedData());
        if (enemy_hits > 0) {
            result.append("Average enemy dmg: ").append(df2.format(enemy_dmg / enemy_hits)).append(" \n");
        }
        if (healed > 0) {
            result.append("Average heal per fight: ").append(df2.format(healed / cleared)).append(" \n");
        }
        for (Enemy enemy : zone.enemies) { //todo: save enemy skill data and report it
            if (enemy.enemy_skills != null) {
                for (ActiveSkill s : enemy.enemy_skills) {
//                result.append(s.name).append(" used with smoke: ").append(df2.format((double) s.used_debuffed / s.used_in_rotation * 100.0)).append("% \n");
                }
            }
        }
        result.append("\nSimulations: ").append(cleared).append("\n");
        result.append("Simulation time: ").append(Main.secToTime(total_time + crafting_time + death_time)).append("\n");
        if (crafting_time > 0) {
            result.append("Includes crafting: ").append(Main.secToTime(crafting_time)).append("\n");
        }
        if (player.milestone_exp_mult != player.old_milestone_exp_mult) {
            lvling_log.append("Milestone exp: ").append(df2.format(player.old_milestone_exp_mult * 100));
            lvling_log.append("% -> ").append(df2.format(player.milestone_exp_mult * 100)).append("%\n");
        }
        if (player.cl != player.old_cl)
            lvling_log.append("CL: ").append(player.old_cl).append(" -> ").append(player.cl).append(" \n");
        if (player.ml != player.old_ml)
            lvling_log.append("ML: ").append(player.old_ml).append(" -> ").append(player.ml).append(" \n");
        for (ActiveSkill a : player.active_skills.values()) {
            if (a.lvl != a.old_lvl)
                lvling_log.append(a.name).append(": ").append(a.old_lvl).append(" -> ").append(a.lvl).append(" \n");
        }
        for (PassiveSkill p : player.passives.values()) {
            if (p.lvl != p.old_lvl)
                lvling_log.append(p.name).append(": ").append(p.old_lvl).append(" -> ").append(p.lvl).append(" \n");
        }
        if (!lvling_log.isEmpty()) {
            result.append("Gained during simulation: \n").append(lvling_log);
        }
        essential_result = result.toString();
        full_result = setup + result.toString();
    }
}
