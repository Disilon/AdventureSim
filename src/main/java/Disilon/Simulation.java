package Disilon;

import java.util.Iterator;
import java.util.LinkedHashMap;

import static Disilon.Main.df2;
import static Disilon.Main.df2p;

public class Simulation {
    enum StatusType {death, respawn, combat, prepare, rerolling, delay}

    int sim_limit;
    double time_limit;
    int cl_limit;
    Player player;
    StatusType status = StatusType.respawn;
    String title = "Default simulation.";
    int crafting_lvl = 20;
    int alchemy_lvl = 20;
    int sim_type = 1;
    double time_to_respawn = -1;
    String skills_info = "";
    String result_info = "";
    String lvling_info = "";

    public Simulation() {
    }

    public Player setupAndRun(Setup setup) {
        sim_type = setup.sim_type;
        sim_limit = setup.simulations;
        cl_limit = setup.sim_cl;
        time_limit = setup.sim_hours;
        player = new Player(setup);
        crafting_lvl = setup.crafting_lvl;
        alchemy_lvl = setup.alchemy_lvl;
        return run(setup.reroll);
    }

    public Player run(double reroll) {
        ActiveSkill skill1 = player.skill1;
        ActiveSkill skill2 = player.skill2;
        ActiveSkill skill3 = player.skill3;
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
        double dot_overkill = 0;
        double ignore_deaths = 0;
        double enemy_dmg = 0;
        double healed = 0;
        int kills = 0;
        int cleared = 0;
        int failed = 0;
        double oom_time = 0;
        player.dot_tracking = 0;
        title = player.zone.toString();
        time_to_respawn = player.zone.getTime_to_respawn();
        StringBuilder result = new StringBuilder();
        StringBuilder skills_log = new StringBuilder();
        StringBuilder lvling_log = new StringBuilder();

        player.debuffs.clear();
        player.buffs.clear();
        boolean end = false;
        while (!end) {
            Enemy target = null;
            double time = 0;
            double delta;
            int casts = 0;
            double delay_left = 0;
            int skill_cycle = 1;
            ActiveSkill previous_cast = null;
            status = StatusType.respawn;
            player.zone.respawn();
            player.checkAmbush();
            player.remove_charge = false;
            if (Main.game_version < 1535 && reroll >= 1) {
                while (player.zone.getRandomEnemy().getHp_max() >= reroll) { //won't work properly with multiple enemies
                    player.zone.respawn();
                    delta = time_to_respawn;
                    time += delta;
                    total_time += delta;
                    player.setMp(player.getMp() + player.getMp_regen() * delta);
                    player.checkPotion(delta);
                    failed++;
                }
            }
            if (skill1 != null) skill1.used_in_rotation = 0;
            if (skill2 != null) skill2.used_in_rotation = 0;
            if (skill3 != null) skill3.used_in_rotation = 0;
            while (time < time_to_respawn) {
                delta = Math.clamp(time_to_respawn - time, 0, 0.1);
                time += delta;
                total_time += delta;
                player.setMp(player.getMp() + player.getMp_regen() * delta);
                player.checkPotion(delta);
            }
            if (time >= time_to_respawn) {
                status = StatusType.combat;
            }
            while (status == StatusType.combat) {
                int cycled = 0;
                if (time > 3600) {
                    if (sim_type == 2 || sim_type == 3) sim_type = 1;
                    sim_limit = 100; //sim slow fights less so that we don't freeze
                }
                while (player.casting == null) {
                    switch (skill_cycle) {
                        case 1 -> {
                            if (skill1 != null && skill1.canCast(player) && skill1.shouldUse(player)) {
                                player.casting = skill1;
                                player.casting.startCastPlayer(player, player.zone);
                                cycled = 0;
                            } else {
                                if (skill1 != null) skill1.used_in_rotation = 0;
                                skill_cycle++;
                                cycled++;
                            }
                        }
                        case 2 -> {
                            if (skill2 != null && skill2.canCast(player) && skill2.shouldUse(player)) {
                                player.casting = skill2;
                                player.casting.startCastPlayer(player, player.zone);
                                cycled = 0;
                            } else {
                                if (skill2 != null) skill2.used_in_rotation = 0;
                                skill_cycle++;
                                cycled++;
                            }
                        }
                        case 3 -> {
                            if (skill3 != null && skill3.canCast(player) && skill3.shouldUse(player)) {
                                player.casting = skill3;
                                player.casting.startCastPlayer(player, player.zone);
                                cycled = 0;
                            } else {
                                if (skill3 != null) skill3.used_in_rotation = 0;
                                skill_cycle = 1;
                                cycled++;
                            }
                        }
                    }
                    if (cycled >= 4) {
                        player.casting = player.getWeakSkill();
                        player.casting.startCastPlayer(player, player.zone);
                        cycled = 0;
                        skill_cycle = 1;
                    }
                }
                for (Iterator<Enemy> iterator = player.zone.enemies.iterator(); iterator.hasNext(); ) {
                    Enemy enemy = iterator.next();
                    if (enemy.casting == null) {
                        ActiveSkill enemyCast = enemy.getCasting();
                        enemyCast.startCast(enemy, player);
                        if (player.name.equals("Assassin1") &&
                                enemy.name.equals("Lamia") && enemyCast.name.equals("Explosion") && enemy.getHp() > enemy.getHp_max() * 0.4) {
                            enemy.reroll(); //todo: add user option to run away from lamia explosion
                            player.casting = null;
                            status = StatusType.rerolling;
                            failed++;
                            break;
                        }
                    }
                }
                delta = 0.1;
                if (player.casting != null) delta = Math.min(delta, player.casting.calculate_delta(player));
                delta = Math.min(delta, player.zone.calculateDelta());
                time += delta;
                total_time += delta;
                player.setMp(player.getMp() + player.getMp_regen() * delta); //todo: implement mana properly for enemies
                player.checkPotion(delta);
                if (player.casting != null) {
                    if (player.casting.cast > 0) {
                        if (player.casting.progressCast(player, delta)) {
                            player.casting.used++;
                            if (player.casting.hit > 0) {
                                casts++;
                                total_casts++;
                                if (player.casting.aoe || player.multi_arrows) {
                                    for (Enemy enemy : player.zone.enemies) {
                                        double dmg = player.casting.attack(player, enemy, 0);
                                        if (dmg > 0) {
                                            enemy.setHp(enemy.getHp() - dmg);
                                            if (player.charge > 0) player.remove_charge = true;
                                        }
                                    }
                                } else {
                                    if (target == null) {
                                        target = player.zone.getRandomEnemy();
                                    }
                                    if (target != null) {
                                        if (player.casting.random_targets) {
                                            LinkedHashMap<Enemy, Integer> targets =
                                                    player.zone.getRandomTargets(player.casting.hits);
                                            targets.forEach((key, value) -> {
                                                double dmg = player.casting.attack(player, key, value);
                                                if (dmg > 0) {
                                                    key.setHp(key.getHp() - dmg);
                                                    if (player.charge > 0) player.remove_charge = true;
                                                }
                                            });
                                        } else {
                                            double dmg = player.casting.attack(player, target, 0);
                                            if (dmg > 0) {
                                                target.setHp(target.getHp() - dmg);
                                                if (player.charge > 0) player.remove_charge = true;
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
//                                System.out.println("Player casted " + player.casting.name + " at " + df2.format(time));
                            }
                            if (player.lvling) player.casting.gainExp();
                            if (player.isAmbushing()) player.setAmbushing(false);
                            player.setMp(player.getMp() - player.casting.calculate_manacost(player));
                            player.tick_debuffs();
                            player.tick_buffs();
                        }
                    } else if (player.casting.delay > 0) {
                        if (player.casting.progressDelay(player, delta)) {
                            player.casting = null;
                        }
                    }
                } else {
                    oom_time += delta;
                }
                Iterator<Enemy> iterator = player.zone.enemies.iterator();
                while (iterator.hasNext()) {
                    Enemy enemy = iterator.next();
                    if (enemy.casting != null) {
                        if (enemy.casting.cast > 0) {
                            if (enemy.casting.progressCast(enemy, delta)) {
                                if (enemy.casting.hit > 0) {
                                    double dmg = 0;
                                    if (previous_cast != null && (time - previous_cast.last_casted_at) < 0.5) {
                                        dmg = previous_cast.attack(enemy, player, enemy.casting.hits);
                                    } else {
                                        dmg = enemy.casting.attack(enemy, player, 0);
                                        previous_cast = enemy.casting;
                                        previous_cast.last_casted_at = time;
                                    }
                                    if (previous_cast == null || (time - previous_cast.last_casted_at) >= 0.5) {

                                    }
                                    if (dmg > 0) {
                                        player.setHp(player.getHp() - dmg);
                                        enemy_dmg += dmg;
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
                            if (enemy.casting.progressDelay(enemy, delta)) {
                                enemy.casting = null;
                            }
                        }
                    }
                    if (enemy.getHp() <= 0) {
                        overkill -= enemy.getHp();
//                        for (Debuff d : enemy.debuffs) {
//                            if (d.dmg > 0) dot_overkill += d.getMaxTotalDmg();
//                        }
                        double exp_gain = enemy.getExp() * player.getExp_mult() * player.milestone_exp_mult;
                        if (player.lvling) player.increment_exp(exp_gain);
                        exp += exp_gain;
                        kills++;
                        player.zone.stats.recordOverkill(enemy);
                        if (Main.game_version >= 1535 && player.lvling) player.levelActives();
//                        System.out.println("Enemy killed at " + df2.format(time) + " s \n");
                        if (target == enemy) target = null;
                        iterator.remove();
                    }
                }
                if (player.zone.enemies.isEmpty()) {
                    status = StatusType.delay;
                    cleared++;
                    if (player.casting != null) {
                        delay_left = player.casting.delay;
                        player.casting = null;
                    }
                }
                if (player.getHp() <= 0 || time >= 3600) {
                    status = StatusType.death;
                    death_time += 15 * 60;
                    player.setHp(player.getHp_max());
                    player.setMp(player.getMp_max());
                    player.resetPotionCd();
                    ignore_deaths += time;
                    failed++;
                    //System.out.println("Player died at " + time);
                }
            }
            if (player.prepare != null && (status == StatusType.respawn || status == StatusType.rerolling || status == StatusType.delay)) {
                delta = 0.1;
                while (player.getHp() / player.getHp_max() < player.prepare_threshold / 100 || player.getMp() / player.getMp_max() < player.prepare_threshold / 100) {
                    //status = StatusType.prepare;
                    total_time += delta;
                    prepare_time += delta;
                    delay_left -= delta;
                    if (player.lvling) player.prepare.gainExp(delta);
                    player.setHp(player.getHp() + player.getPrepare_hps() * delta);
                    player.setMp(player.getMp() + player.getPrepare_mps() * delta);
                    if (Main.game_version >= 1534) {
                        player.tickPotion(delta);
                    }
                }
            }
            while (delay_left > 0) {
                delta = Math.clamp(delay_left, 0, 0.1);
                time += delta;
                total_time += delta;
                delay_left -= delta;
                player.setMp(player.getMp() + player.getMp_regen() * delta);
                player.checkPotion(delta);
            }
            if (delay_left <= 0 && status == StatusType.delay) status = StatusType.respawn;
            if (status == StatusType.respawn) {
                min_time = Math.min(min_time, time);
                max_time = Math.max(max_time, time);
                min_casts = Math.min(min_casts, casts);
                max_casts = Math.max(max_casts, casts);
            }
            //if (status == StatusType.death) status = StatusType.respawn;
            if (player.lvling) player.levelPassives(time);
            if (player.potion1 != null) {
                crafting_time += player.potion1.calc_time(crafting_lvl, alchemy_lvl);
            }
            if (player.potion2 != null) {
                crafting_time += player.potion2.calc_time(crafting_lvl, alchemy_lvl);
            }
            if (player.potion3 != null) {
                crafting_time += player.potion3.calc_time(crafting_lvl, alchemy_lvl);
            }
            if ((cleared + failed) >= 100000) end = true;
            switch (sim_type) {
                default -> {
                    if ((cleared + failed) >= sim_limit) end = true;
                }
                case 2 -> {
                    if (crafting_lvl >= 30 && alchemy_lvl >= 30) {
                        if ((total_time + death_time) >= time_limit * 3600) end = true;
                    } else {
                        if ((total_time + death_time + crafting_time) >= time_limit * 3600) end = true;
                    }
                }
                case 3 -> {
                    if (player.cl >= cl_limit) end = true;
                }
            }
        }

        result.append("Exp/h: ").append((int) (exp / (total_time + death_time) * 3600)).append(" (");
        result.append(df2.format(player.milestone_exp_mult * 100)).append("%)\n");
//        result.append("Exp/h without milestones: ").append((int) (exp / player.milestone_exp_mult / (total_time + death_time) * 3600)).append
//        ("\n");
        if (player.potion1 != null) {
            result.append(player.potion1.getRecordedData(total_time + death_time));
        }
        if (player.potion2 != null) {
            result.append(player.potion2.getRecordedData(total_time + death_time));
        }
        if (player.potion3 != null) {
            result.append(player.potion3.getRecordedData(total_time + death_time));
        }
        if (crafting_time > 0) {
            result.append("Effective exp/h: ").append((int) (exp / (total_time + crafting_time + death_time) * 3600)).append("\n");
        }
        if (player.prepare != null) {
            result.append("Time preparing %: ").append(df2.format(prepare_time / total_time * 100)).append("% " +
                    "\n");
        }
        if (death_time > 0) {
            result.append("Time dead %: ").append(df2.format(death_time / (total_time + death_time) * 100)).append("% \n");
            //result.append("Spawning time: ").append(df2.format(respawning_time)).append("s \n");
            result.append("Exp/h without deaths: ").append((int) (exp / (total_time - ignore_deaths) * 3600)).append(
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
        skills_log.append("Damage skill casts: ").append(min_casts).append(" - ").append(max_casts);
        skills_log.append("; avg: ").append(df2.format((double) total_casts / cleared)).append("\n");
        skills_log.append("Average Overkill: ").append((int) (overkill / kills));
        if (dot_overkill > 0) skills_log.append("; DoT: ").append((int) (dot_overkill / kills));
        skills_log.append("\n");
        skills_log.append(player.getWeakAttackData());
        if (skill1 != null && !skill1.name.equals("Prepare"))
            skills_log.append(skill1.getRecordedData(cleared + failed));
        if (skill2 != null && !skill2.name.equals("Prepare"))
            skills_log.append(skill2.getRecordedData(cleared + failed));
        if (skill3 != null && !skill3.name.equals("Prepare"))
            skills_log.append(skill3.getRecordedData(cleared + failed));
        skills_log.append("\n");
        if (enemy_dmg > 0 && cleared > 0) {
            skills_log.append("Average enemy dmg per fight: ").append((int) enemy_dmg / cleared);
            if (player.dot_tracking > 0) skills_log.append("; DoT: ").append((int) (player.dot_tracking / cleared));
            skills_log.append("\n");
        }
        if (healed > 0 && cleared > 0) {
            skills_log.append("Average heal per fight: ").append((int) healed / cleared).append(" \n");
        }
        skills_log.append("\n");
        skills_log.append(player.zone.stats.getSkillData());
        for (Enemy enemy : player.zone.enemies) { //todo: save enemy skill data and report it
            if (enemy.enemy_skills != null) {
                for (ActiveSkill s : enemy.enemy_skills) {
//                result.append(s.name).append(" used with smoke: ").append(df2.format((double) s.used_debuffed / s.used_in_rotation * 100.0)).append("% \n");
                }
            }
        }
        result.append("\nSimulations: ").append(cleared).append("\n");
        result.append("Total sim time: ").append(Main.secToTime(total_time + crafting_time + death_time)).append("\n");
        result.append("Time in combat: ").append(Main.secToTime(total_time)).append("\n");
        if (death_time > 0) {
            result.append("Time dead: ").append(Main.secToTime(death_time)).append("\n");
        }
        if (crafting_time > 0) {
            result.append("Crafting time: ").append(Main.secToTime(crafting_time)).append("\n");
        }
        result.append("\n");
        result.append("Cores: \n");
        result.append(player.zone.stats.getCoreData(player, total_time));
        if (player.milestone_exp_mult != player.old_milestone_exp_mult) {
            lvling_log.append("Milestone exp: ").append(df2.format(player.old_milestone_exp_mult * 100));
            lvling_log.append("% -> ").append(df2.format(player.milestone_exp_mult * 100)).append("%\n");
        }
        if (player.cl != (int) player.old_cl)
            lvling_log.append("CL: ").append((int) player.old_cl).append(" -> ").append(player.cl).append(" \n");
        if (player.ml != (int) player.old_ml)
            lvling_log.append("ML: ").append((int) player.old_ml).append(" -> ").append(player.ml).append(" \n");
        for (ActiveSkill a : player.active_skills.values()) {
            if (a.lvl != (int) a.old_lvl)
                lvling_log.append(a.name).append(": ").append((int) a.old_lvl).append(" -> ").append(a.lvl).append(" " +
                        "\n");
        }
        for (PassiveSkill p : player.passives.values()) {
            if (p.lvl != (int) p.old_lvl)
                lvling_log.append(p.name).append(": ").append((int) p.old_lvl).append(" -> ").append(p.lvl).append(" " +
                        "\n");
        }
        lvling_info = "";
        if (!lvling_log.isEmpty()) {
            lvling_info = "Gained during simulation: \n" + lvling_log;
        }
        result_info = result.toString();
        skills_info = skills_log.toString();
        return player;
    }
}
