package Disilon;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static Disilon.Main.df2;
import static Disilon.Main.df4;
import static Disilon.Main.game_version;
import static Disilon.Main.minIfNotZero;
import static Disilon.Main.minTickTime;
import static Disilon.Main.shorthand;
import static Disilon.Main.log;

public class Simulation {
    enum StatusType {death, respawn, combat, prepare, rerolling, delay}

    int sim_limit;
    double time_limit;
    int cl_limit;
    boolean offline;
    double time_mult = 1;
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
        if (sim_type == 3) setup.leveling = true;
        sim_limit = setup.simulations;
        cl_limit = setup.sim_cl;
        time_limit = setup.sim_hours;
        player = new Player(setup);
        crafting_lvl = setup.crafting_lvl;
        alchemy_lvl = setup.alchemy_lvl;
        offline = setup.offline;
        return run();
    }

    public Player run() {
        long startTime = System.nanoTime();
        ActiveSkill skill1 = player.skill1;
        ActiveSkill skill2 = player.skill2;
        ActiveSkill skill3 = player.skill3;
        ActiveSkill skill4 = player.skill4;
        double exp = 0;
        double total_time = 0;
        double death_time = 0;
        double prepare_time = 0;
        double crafting_time = 0;
        double sidecraft_time = 0;
        double min_time = 9999;
        double max_time = 0;
        int total_casts = 0;
        int min_casts = 999;
        int max_casts = 0;
        double overkill = 0;
        double dot_overkill = 0;
        double ignore_deaths = 0;
        double healed = 0;
        int kills = 0;
        double kills_drop = 0;
        int cleared = 0;
        int failed = 0;
        double delta;
        double delta_sum = 0;
        int delta_count = 0;
        double oom_time = 0;
        player.damage_taken = 0;
        player.dot_tracking = 0;
        player.research_slots_stat = 0;
        player.rp_drain = 0;
        title = player.zone.toString();
        time_to_respawn = player.zone.getTime_to_respawn();
        time_mult = offline ? player.zone.getZoneOfflineMult() : 1;
        time_limit = time_limit * time_mult;
        StringBuilder result = new StringBuilder();
        StringBuilder skills_log = new StringBuilder();
        StringBuilder lvling_log = new StringBuilder();
        player.setHp(player.getHp_max());
        player.setMp(player.getMp_max());
        player.debuffs.clear();
        player.buffs.clear();
        player.check_buffs();
        player.check_debuffs();
        player.zone.rerollSeed();
        player.sortResearchWeights();
        boolean end = false;
        if (offline && player.zone.getZoneTimeCap() > 0) {
            time_limit /= (120*60 + player.zone.getZoneTimeCap()) / (120*60);
        }
        delta = offline ? 0.180 : 0.030;
        while (!end) {
            Enemy target = null;
            double time = 0;
            double cap_time = 0;
            int casts = 0;
            double first_kill = 0;
            double delay_left = 0;
            int skill_cycle = 1;
            ActiveSkill previous_cast = null;
            status = StatusType.respawn;
            player.zone.respawn();
            player.checkAmbush();
            player.remove_charge = false;
            if (skill1 != null) skill1.used_in_rotation = 0;
            if (skill2 != null) skill2.used_in_rotation = 0;
            if (skill3 != null) skill3.used_in_rotation = 0;
            if (skill4 != null) skill4.used_in_rotation = 0;
            while (time < time_to_respawn) {
                player.checkPotion(0); //just in case
                delta = minIfNotZero(time_to_respawn - time, player.getNextPotionTime());
                delta = minTickTime(delta, offline);
                time += delta;
                total_time += delta;
                delta_sum += delta;
                delta_count++;
                player.setMp(player.getMp() + player.getMp_regen() * delta);
                player.checkPotion(delta);
            }
            if (time >= time_to_respawn) {
                status = StatusType.combat;
            }
            if ((System.nanoTime() - startTime) / 1000000 > 10000) {
                end = true;
            }
            while (status == StatusType.combat) {
                int cycled = 0;
                if (time > 3600 || total_time > 1e10) {
                    end = true; //stop simulations so that we don't freeze
                }
                while (player.casting == null) {
                    switch (skill_cycle) {
                        case 1 -> {
                            if (skill1 != null && skill1.shouldUse(player)) {
                                if (skill1.canCast(player)) {
                                    player.casting = skill1;
                                } else {
                                    player.casting = player.getWeakSkill();
                                }
                                skill1.used_in_rotation++;
                                cycled = 0;
                            } else {
                                if (skill1 != null) skill1.used_in_rotation = 0;
                                if (log.contains("skill_rotation")) System.out.println("Skill 1 skipped");
                                skill_cycle++;
                                cycled++;
                            }
                        }
                        case 2 -> {
                            if (skill2 != null && skill2.shouldUse(player)) {
                                if (skill2.canCast(player)) {
                                    player.casting = skill2;
                                } else {
                                    player.casting = player.getWeakSkill();
                                }
                                skill2.used_in_rotation++;
                                cycled = 0;
                            } else {
                                if (skill2 != null) skill2.used_in_rotation = 0;
                                if (log.contains("skill_rotation")) System.out.println("Skill 2 skipped");
                                skill_cycle++;
                                cycled++;
                            }
                        }
                        case 3 -> {
                            if (skill3 != null && skill3.shouldUse(player)) {
                                if (skill3.canCast(player)) {
                                    player.casting = skill3;
                                } else {
                                    player.casting = player.getWeakSkill();
                                }
                                skill3.used_in_rotation++;
                                cycled = 0;
                            } else {
                                if (skill3 != null) skill3.used_in_rotation = 0;
                                if (log.contains("skill_rotation")) System.out.println("Skill 3 skipped");
                                skill_cycle++;
                                cycled++;
                            }
                        }
                        case 4 -> {
                            if (skill4 != null && skill4.shouldUse(player)) {
                                if (skill4.canCast(player)) {
                                    player.casting = skill4;
                                } else {
                                    player.casting = player.getWeakSkill();
                                }
                                skill4.used_in_rotation++;
                                cycled = 0;
                            } else {
                                if (skill4 != null) skill4.used_in_rotation = 0;
                                if (log.contains("skill_rotation")) System.out.println("Skill 4 skipped");
                                skill_cycle = 1;
                                cycled++;
                            }
                        }
                    }
                    if (cycled >= 5) {
                        player.casting = player.getWeakSkill();
                        cycled = 0;
                        skill_cycle = 1;
                    }
                    if (skill1 != null && skill1.canCast(player)) {
                        if ((skill1.name.equals("Careful Shot") && player.zone.getMaxEnemyHp() < skill1.use_setting) ||
                                (skill1.name.equals("Dispel") && player.zone.getEnemyBuffCount() > 0)) {
                            player.casting = skill1;
                            switch (skill_cycle) {
                                case 1 -> {
                                    if (skill1 != null) skill1.used_in_rotation++;
                                }
                                case 2 -> {
                                    if (skill2 != null) skill2.used_in_rotation++;
                                }
                                case 3 -> {
                                    if (skill3 != null) skill3.used_in_rotation++;
                                }
                                case 4 -> {
                                    if (skill4 != null) skill4.used_in_rotation++;
                                }
                            }
//                            skill_cycle++;
//                            if (skill_cycle > 4) skill_cycle = 1;
//                            skill_cycle = 1; //dispel resets rotation, should be fine for CS
//                            if (skill1 != null) skill1.used_in_rotation = 0;
//                            if (skill2 != null) skill2.used_in_rotation = 0;
//                            if (skill3 != null) skill3.used_in_rotation = 0;
//                            if (skill4 != null) skill4.used_in_rotation = 0;
                        }
                    }
                    if (player.casting != null) {
                        player.casting.startCastPlayer(player, offline, time, total_time);
                    }
                }
                for (int i = 0; i < 9; i++) {
                    Enemy enemy = player.zone.enemies[i];
                    if (enemy.active) {
                        if (enemy.casting == null) {
                            ActiveSkill enemyCast = enemy.getCasting(player);
                            enemyCast.startCast(enemy, player, offline, time, total_time);
                        }
                    }
                }
                if (player.casting != null) delta = Math.min(delta, player.casting.calculate_delta(player));
                delta = Math.min(delta, player.zone.calculateDelta());
                delta = minIfNotZero(delta, player.getNextPotionTime());
                delta = minTickTime(delta, offline);
                time += delta;
                total_time += delta;
                delta_sum += delta;
                delta_count++;
                player.setMp(player.getMp() + player.getMp_regen() * delta); //todo: implement mana properly for enemies
                player.checkPotion(delta);
                if (player.casting != null) {
                    if (player.casting.cast > 0) {
                        if (player.casting.progressCast(player, delta)) {
                            player.casting.used++;
                            player.current_skill_hit = false;
                            player.check_debuffs();
                            player.check_buffs();
                            if (player.casting.hit > 0) {
                                casts++;
                                total_casts++;
                                if (player.casting.aoe || player.isMulti_hit_override(player.casting.name)) {
                                    for (Enemy enemy : player.zone.enemies) {
                                        if (enemy.active) {
                                            double dmg = player.casting.attack(player, enemy, 0, time);
                                            if (player.extra_attack.enabled) {
                                                dmg += player.extra_attack_proc.attack(player, enemy, 0, time);
                                            }
                                            if (dmg > 0) {
                                                enemy.setHp(enemy.hp - dmg);
                                                if (player.casting.name.equals("Careful Shot") && enemy.hp <= 0) {
                                                    kills_drop += 0.5;
                                                }
                                                if (player.charge > 0) player.remove_charge = true;
                                            }
                                        }
                                    }
                                } else {
                                    if (target == null) {
                                        target = player.zone.getRandomEnemy(player.casting.name);
                                    }
                                    if (target != null) {
                                        if (player.casting.random_targets) {
                                            LinkedHashMap<Enemy, Integer> targets =
                                                    player.zone.getRandomTargets(player.casting.hits);
                                            targets.forEach((key, value) -> {
                                                double dmg = player.casting.attack(player, key, value, 0);
                                                if (player.extra_attack.enabled) {
                                                    dmg += player.extra_attack_proc.attack(player, key, 0, 0);
                                                }
                                                if (dmg > 0) {
                                                    key.setHp(key.hp - dmg);
                                                    if (player.charge > 0) player.remove_charge = true;
                                                }
                                            });
                                        } else {
                                            double dmg = player.casting.attack(player, target, 0, time);
                                            if (player.extra_attack.enabled) {
                                                dmg += player.extra_attack_proc.attack(player, target, 0, time);
                                            }
                                            if (dmg > 0) {
                                                target.setHp(target.hp - dmg);
                                                if (player.casting.name.equals("Careful Shot") && target.hp <= 0) {
                                                    kills_drop += 0.5;
                                                }
                                                if (player.charge > 0) player.remove_charge = true;
                                            }
                                        }
                                        target = null;
                                    }
                                }
                            } else {
                                double previous_hp = player.hp;
                                player.casting.use(player, time);
                                if (player.casting.heal) {
                                    healed += player.hp - previous_hp;
                                }
                            }
                            if (player.lvling) player.casting.gainExp(1);
                            if (player.current_skill_hit) player.ambush_bonus = 0;
                            if (player.ambushing) player.ambushing = false;
                            player.tick_debuffs();
                            player.tick_buffs();
                            player.casting.pay_manacost(player);
                        }
                    } else if (player.casting.delay > 0) {
                        if (player.casting.progressDelay(player, delta)) {
                            player.casting = null;
                        }
                    }
                } else {
                    oom_time += delta;
                }
                for (int i = 0; i < 9; i++) {
                    Enemy enemy = player.zone.enemies[i];
                    if (enemy.active) {
                        if (enemy.hp <= 0) {
                            if (first_kill == 0) first_kill = time;
                            if (player.last_skill.isSingleTarget() && !player.isMulti_hit_override(player.last_skill.name) && player.set_core > 0 && player.set_core > Math.random()) {
                                enemy.setHp(0.0);
                            } else {
                                player.overkill -= enemy.hp;
                            }
//                        for (Debuff d : enemy.debuffs) {
//                            if (d.dmg > 0) dot_overkill += d.getMaxTotalDmg();
//                        }
                            double exp_gain =
                                    enemy.exp * player.total_exp_mult * player.milestone_exp_mult * player.hard_reward;
                            if (player.lvling) player.increment_exp(exp_gain);
                            exp += exp_gain;
                            kills++;
                            player.zone.stats.recordOverkill(enemy, player);
                            if (player.lvling) {
                                player.levelActives();
                                player.levelTF(enemy);
                            }
                            if (log.contains("enemy_death_stat") && enemy.casting != null) System.out.println(enemy.name +
                                    " died while " +
                                    "casting " + enemy.casting.name + " cast time left: " + df4.format(enemy.casting.cast) + " delay left: " + df4.format(enemy.casting.delay) + " at " + df2.format(time));
                            if (target == enemy) target = null;
                            enemy.active = false;
                        }
                        if (enemy.active && enemy.casting != null) {
                            if (enemy.casting.cast > 0) {
                                if (enemy.casting.progressCast(enemy, delta)) {
                                    enemy.check_debuffs();
                                    enemy.check_buffs();
                                    if (enemy.casting.hit > 0) {
                                        double dmg = 0;
                                        if (previous_cast != null && (time - previous_cast.last_casted_at) < 0.5) {
                                            dmg = previous_cast.attack(enemy, player, enemy.casting.hits, time);
                                        } else {
                                            dmg = enemy.casting.attack(enemy, player, 0, time);
                                            previous_cast = enemy.casting;
                                            previous_cast.last_casted_at = time;
                                        }
                                        if (dmg > 0) {
                                            player.setHp(player.hp - dmg);
                                            player.damage_taken += dmg;
                                            if (enemy.charge > 0) enemy.remove_charge = true;
                                        }
//                                System.out.println("Player: " + (int) player.hp + "/" + (int) player.getHp_max() + " " + (int) player.getMp() + "/" + (int) player.getMp_max() + "; Enemy: " + (int) enemy.hp + "/" + (int) enemy.getHp_max());
                                    } else {
                                        enemy.casting.use(enemy, time);
                                        player.zone.stats.incrementStats(enemy.name, enemy.casting.name, 0,
                                                0, 0, 1, 0,0);
                                    }
                                    enemy.tick_debuffs();
                                    enemy.tick_buffs();
                                    enemy.casting.pay_manacost(enemy);
                                }
                            } else if (enemy.casting.delay > 0) {
                                if (enemy.casting.progressDelay(enemy, delta)) {
                                    enemy.casting = null;
                                }
                            }
                        }
                    }
                }
                if (player.hp <= 0 || time >= 3600) {
                    status = StatusType.death;
                    death_time += 15 * 60;
                    player.setHp(player.getHp_max());
                    player.setMp(player.getMp_max());
                    player.tick_research(15 * 60);
                    player.resetPotionCd();
                    ignore_deaths += time;
                    failed++;
                    if (log.contains("death") || log.contains("player_death_stat")) {
                        if (player.casting != null && log.contains("player_death_stat")) {
                            System.out.println("Player died while casting " +
                                    player.casting.name + " time left: " + df4.format(player.casting.cast) + " delay left: " + df4.format(player.casting.delay) +
                                    " at " + df2.format(time));
                        } else {
                            System.out.println("Player died at " + df2.format(time));
                        }
                    }
                }
                if (player.zone.cleared()) {
                    status = StatusType.delay;
                    cleared++;
                    if (player.casting != null) {
                        delay_left = player.casting.delay;
                        player.casting = null;
                    }
                }
            }
            if (player.prepare != null && (status == StatusType.respawn || status == StatusType.rerolling || status == StatusType.delay)) {
                while (player.getPredictedPrepareTime() > 0) {
                    delta = minIfNotZero(player.getPredictedPrepareTime(), player.getNextPotionTime());
                    delta = minTickTime(delta, offline);
                    total_time += delta;
                    time += delta;
                    delta_sum += delta;
                    delta_count++;
                    prepare_time += delta;
                    delay_left = Math.max(delay_left - delta, 0);
                    if (player.lvling) player.prepare.gainExp(delta);
                    player.setHp(player.hp + player.getPrepare_hps() * delta);
                    player.setMp(player.getMp() + player.getPrepare_mps() * delta);
                    if (Main.game_version >= 1534) {
                        player.tickPotion(delta);
                    }
                }
            }
            if (game_version >= 1573 && player.zone.getZoneTimeCap() > 0) {
                if ((time + delay_left + cap_time) < player.zone.getZoneTimeCap())
                    cap_time += player.zone.getZoneTimeCap() - time - delay_left;
            }
            if (cap_time > 0 && player.onion_wave.enabled) {
                cap_time = Math.min(cap_time, 10 - player.zone.getTime_to_respawn());
            }
            delay_left += cap_time;
            while (delay_left > 0) {
                delta = minIfNotZero(delay_left, player.getNextPotionTime());
                delta = minTickTime(delta, offline);
                time += delta;
                total_time += delta;
                delay_left -= delta;
                delta_sum += delta;
                delta_count++;
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
            if (player.lvling) {
                player.levelPassives(time);
                player.tick_research(time);
            }
            int research_craft = player.research_lvls.getOrDefault("Crafting spd", 0.0).intValue();
            int research_alch = player.research_lvls.getOrDefault("Alchemy spd", 0.0).intValue();
            double side_craft_spd = 0;
            if (game_version >= 1573) {
                if (crafting_lvl >= 10 && alchemy_lvl >= 10) {
                    side_craft_spd = 0.05 + 0.01 * player.research_lvls.getOrDefault("Sidecraft spd", 0.0).intValue();
                }
                if (crafting_lvl >= 20 && alchemy_lvl >= 20) side_craft_spd += 0.05;
                if (crafting_lvl >= 30 && alchemy_lvl >= 30) side_craft_spd += 0.1;
                if (crafting_lvl >= 40 && alchemy_lvl >= 40) side_craft_spd += 0.05;
                if (crafting_lvl >= 50 && alchemy_lvl >= 50) side_craft_spd += 0.05;
                if (crafting_lvl >= 60 && alchemy_lvl >= 60) side_craft_spd += 0.05;
                if (crafting_lvl >= 70 && alchemy_lvl >= 70) side_craft_spd += 0.05;
                if (crafting_lvl >= 80 && alchemy_lvl >= 80) side_craft_spd += 0.05;
                if (crafting_lvl >= 90 && alchemy_lvl >= 90) side_craft_spd += 0.05;
            } else {
                if (crafting_lvl >= 30 && alchemy_lvl >= 30) {
                    side_craft_spd = game_version >= 1568 ? 0.2 + 0.01 * player.research_lvls.getOrDefault("Sidecraft spd", 0.0).intValue() : 0.2;
                }
            }
            double add_time = 0;
            if (player.potion1 != null) {
                add_time += player.potion1.calc_time(crafting_lvl, alchemy_lvl, research_craft, research_alch);
            }
            if (player.potion2 != null) {
                add_time += player.potion2.calc_time(crafting_lvl, alchemy_lvl, research_craft, research_alch);
            }
            if (player.potion3 != null) {
                add_time += player.potion3.calc_time(crafting_lvl, alchemy_lvl, research_craft, research_alch);
            }
            if (add_time > 0 && side_craft_spd > 0) {
                sidecraft_time += add_time / side_craft_spd;
            } else {
                crafting_time += add_time;
            }
            if ((cleared + failed) >= 10000000) end = true;
            switch (sim_type) {
                default -> {
                    if ((cleared + failed) >= sim_limit) end = true;
                }
                case 2 -> {
                    if (side_craft_spd > 0) {
                        if ((total_time + death_time) >= time_limit * 3600) end = true;
                    } else {
                        if ((total_time + death_time + crafting_time) >= time_limit * 3600) end = true;
                    }
                }
                case 3 -> {
                    if (player.cl >= cl_limit) end = true;
                }
            }
            if (log.contains("fight_end")) System.out.println("Fight ended\n");
        }
        if (offline && player.zone.getZoneTimeCap() > 0) {
            total_time *= (120*60 + player.zone.getZoneTimeCap()) / (120*60);
        }

        total_time /= time_mult;
        min_time /= time_mult;
        max_time /= time_mult;
        double exph = (exp / (total_time + death_time) * 3600);
        double exp_total_bonus = player.total_exp_mult * player.milestone_exp_mult;
        result.append("Exp/h: ").append(shorthand(exph)).append(" (");
        result.append(df2.format(exp_total_bonus * 100)).append("%; ");
        result.append(shorthand(exph / (player.total_exp_mult * player.milestone_exp_mult))).append(" at 100%)\n");
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
            result.append("Effective exp/h: ").append(shorthand((exp / (total_time + crafting_time + death_time) * 3600))).append("\n");
        }
        if (player.prepare != null) {
            result.append("Time preparing %: ").append(df2.format(prepare_time / total_time * 100)).append("% " +
                    "\n");
        }
        if (death_time > 0) {
            result.append("Time dead %: ").append(df2.format(death_time / (total_time + death_time) * 100)).append("% \n");
            //result.append("Spawning time: ").append(df2.format(respawning_time)).append("s \n");
            result.append("Exp/h without deaths: ").append(shorthand((exp / (total_time - ignore_deaths) * 3600))).append(
                    "\n");
        }
        if (oom_time > 0) {
            result.append("Time oom %: ").append(df2.format(oom_time / total_time * 100)).append("% " +
                    "\n");
        }
        result.append("Kills/h without deaths: ").append(df2.format(kills / (total_time - ignore_deaths) * 3600)).append(
                "\n");
        double item_drop = 1 + 0.01 * player.research_lvls.getOrDefault("Drop rate", 0.0).intValue();
        item_drop *= player.drop_mult;
        if (kills_drop > 0 || item_drop > 1) {
            result.append("Item drop mult: ").append(df2.format((kills + kills_drop) / kills * item_drop)).append(
                    "\n");
        }
        result.append("Time to clear: ").append(df2.format(min_time)).append("s - ").append(df2.format(max_time));
        result.append("s; avg: ").append(df2.format(total_time / cleared)).append("s \n");
        skills_log.append("Damage skill casts: ").append(min_casts).append(" - ").append(max_casts);
        skills_log.append("; avg: ").append(df2.format((double) total_casts / cleared)).append("\n");
        skills_log.append("Average Overkill: ").append((int) (player.overkill / kills)).append("(");
        skills_log.append(df2.format(player.zone.stats.overkill_sum / player.zone.stats.kills)).append("%)");
        if (dot_overkill > 0) skills_log.append("; DoT: ").append((int) (dot_overkill / kills));
        skills_log.append("\n");
        skills_log.append(player.getWeakAttackData(cleared + failed));
        if (skill1 != null && !skill1.name.equals("Prepare"))
            skills_log.append(skill1.getRecordedData(cleared + failed));
        if (skill2 != null && !skill2.name.equals("Prepare"))
            skills_log.append(skill2.getRecordedData(cleared + failed));
        if (skill3 != null && !skill3.name.equals("Prepare"))
            skills_log.append(skill3.getRecordedData(cleared + failed));
        if (skill4 != null && !skill4.name.equals("Prepare"))
            skills_log.append(skill4.getRecordedData(cleared + failed));
        if (player.extra_attack_proc.used > 0)
            skills_log.append(player.extra_attack_proc.getRecordedData(cleared + failed));
        if (player.counter_strike_log.used > 0)
            skills_log.append(player.counter_strike_log.getRecordedData(cleared + failed));
        if (player.counter_dodge_log.used > 0)
            skills_log.append(player.counter_dodge_log.getRecordedData(cleared + failed));
        skills_log.append("\n");
        if (player.damage_taken > 0 && cleared > 0) {
            skills_log.append("Average enemy dmg per fight: ").append((int) player.damage_taken / cleared);
            if (player.dot_tracking > 0) skills_log.append("; DoT: ").append((int) (player.dot_tracking / cleared));
            skills_log.append("\n");
        }
        if (healed > 0 && cleared > 0) {
            skills_log.append("Average heal per fight: ").append((int) healed / cleared).append(" \n");
        }
        skills_log.append("\n");
        if (player.zone.max_enemies > 1) {
            skills_log.append("Initial number of enemies seed: ").append(player.zone.initial_seed).append("\n");
//            System.out.println("seed = " + player.zone.initial_seed + " exph = " + shorthand(exph));
        }
        skills_log.append(player.zone.stats.getSkillData(cleared + failed));
        result.append("\nSimulations: ").append(cleared).append("\n");
        result.append("Kills: ").append(kills).append("\n");
        result.append("Total sim time: ").append(Main.secToTime(total_time + crafting_time + death_time)).append("\n");
        result.append("Time in combat: ").append(Main.secToTime(total_time));
        result.append(" (").append(df2.format(total_time / 3600)).append(" h)\n");
        if (death_time > 0) {
            result.append("Time dead: ").append(Main.secToTime(death_time)).append("\n");
        }
        if (crafting_time > 0) {
            result.append("Crafting time: ").append(Main.secToTime(crafting_time)).append("\n");
        }
        if (sidecraft_time > 0) {
            result.append("Sidecrafting time: ").append(Main.secToTime(sidecraft_time));
            result.append(" (").append(df2.format(sidecraft_time/(total_time + crafting_time + death_time) * 100));
            result.append("%)\n");
            double diff = (total_time + crafting_time + death_time) - sidecraft_time;
            if (diff > 0) {
                result.append("Free sidecrafting time: ").append(df2.format(diff / 3600)).append(" hours\n");
            } else {
                result.append("Deficient sidecrafting time: ").append(df2.format(-diff / 3600)).append(" hours\n");
            }
        }
        long executionTime = (System.nanoTime() - startTime) / 1000000;
        result.append("\nSim run time: ").append(executionTime).append("\n");
//        result.append("\nAverage delta: ").append((int) (delta_sum * 1000 / delta_count)).append("\n");
        result.append("\n");
        result.append("Cores: \n");
        result.append(player.zone.stats.getCoreData(player, total_time, offline));
        if (player.lvling) {
            if (player.milestone_exp_mult != player.old_milestone_exp_mult) {
                lvling_log.append("Milestone exp: ").append(df2.format(player.old_milestone_exp_mult * 100));
                lvling_log.append("% -> ").append(df2.format(player.milestone_exp_mult * 100)).append("%\n");
            }
            if ((int) player.old_cl <= player.getMaxCl()) {
                lvling_log.append("CL: ").append((int) player.old_cl).append(" -> ").append(player.cl).append(" (");
                lvling_log.append(df2.format(player.getCLpercent())).append("%)\n");
            }
            lvling_log.append("ML: ").append((int) player.old_ml).append(" -> ").append(player.ml).append(" (");
            lvling_log.append(df2.format(player.getMLpercent())).append("%)\n");
            for (ActiveSkill a : player.active_skills.values()) {
                if (a.enabled && a.old_lvl < 20) {
                    lvling_log.append(a.name).append(": ").append((int) a.old_lvl).append(" -> ").append(a.lvl).append(" (");
                    lvling_log.append(df2.format(a.getLpercent())).append("%)\n");
                }
            }
            for (PassiveSkill p : player.passives.values()) {
                if ((p.enabled && p.old_lvl < 20) || p.name.equals("Tsury Finke")) {
                    lvling_log.append(p.name).append(": ").append((int) p.old_lvl).append(" -> ").append(p.lvl).append(" (");
                    lvling_log.append(df2.format(p.getLpercent())).append("%)\n");
                }
            }
            lvling_log.append("RP: ").append((int) player.rp_balance).append("\n");
            lvling_log.append("Research slots used: ").append(df2.format(player.research_slots_stat / (total_time + death_time))).append("\n");
            lvling_log.append("Cost per hour: ").append(df2.format(player.rp_drain / (total_time + death_time) * 3600)).append("\n");
            for (Map.Entry<String, Double> entry : player.research_lvls.entrySet()) {
                double change = entry.getValue() - player.research_old_lvls.getOrDefault(entry.getKey(), 0.0);
                if (change > 0.01) {
                    lvling_log.append(entry.getKey()).append(" +").append(df2.format(change)).append("\n");
                }
            }
//            StringBuilder research = new StringBuilder();
//            research.append(df2.format(player.research_lvls.get("Research slot"))).append("\t");
//            research.append(df2.format(player.research_lvls.get("Research spd"))).append("\t");
//            research.append(df2.format(player.research_lvls.get("Max CL"))).append("\t");
//            research.append(df2.format(player.research_lvls.get("Exp gain"))).append("\t");
//            research.append(df2.format(player.research_lvls.get("Core drop"))).append("\t");
//            research.append(df2.format(player.research_lvls.get("Core quality"))).append("\t");
//            research.append(df2.format(player.research_lvls.get("Sidecraft spd"))).append("\t");
//            research.append(df2.format(player.research_lvls.get("E. Quality mult"))).append("\t");
//            System.out.println(research);
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
