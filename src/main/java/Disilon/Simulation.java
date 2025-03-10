package Disilon;

import java.text.DecimalFormat;
import java.util.Map;

public class Simulation {
    enum StatusType {death, respawn, combat, prepare, rerolling, delay}

    int simulations;
    Actor player;
    Actor enemy;
    Potion potion1;
    Potion potion2;
    Potion potion3;
    StatusType status = StatusType.respawn;
    String title = "Default simulation.";
    int crafting_lvl = 20;
    int alchemy_lvl = 20;
    double milestone_exp_mult = 1;
    double time_to_respawn = -1;
    double hard_dmg_min_detect = 500;
    DecimalFormat df2 = new DecimalFormat("#.##");
    String full_result = "";
    String essential_result = "";

    public Simulation() {
    }

    public Simulation(int simulations, Actor player, Actor enemy) {
        this.simulations = simulations;
        this.player = player;
        this.enemy = enemy;
        time_to_respawn = getTime_to_respawn();
    }

    public Simulation(int simulations, Actor player, Actor enemy, double time_to_respawn) {
        this.simulations = simulations;
        this.player = player;
        this.enemy = enemy;
        this.time_to_respawn = time_to_respawn;
    }

    public void setupPotions(String type1, int tier1, int threshold1, String type2, int tier2, int threshold2) {
        if (type1 != null) potion1 = new Potion(type1, tier1, threshold1);
        if (type2 != null) potion2 = new Potion(type2, tier2, threshold2);
    }

    public void setupPotions(String type1, int tier1, int threshold1, String type2, int tier2, int threshold2,
                             String type3, int tier3, int threshold3) {
        if (type1 != null) potion1 = new Potion(type1, tier1, threshold1);
        if (type2 != null) potion2 = new Potion(type2, tier2, threshold2);
        if (type3 != null) potion3 = new Potion(type3, tier3, threshold3);
    }

    public void setupPotions(String type1, int tier1, int threshold1, String type2, int tier2, int threshold2,
                             int count1, int count2) {
        if (type1 != null) potion1 = new Potion(type1, tier1, threshold1, count1);
        if (type2 != null) potion2 = new Potion(type2, tier2, threshold2, count2);
    }

    public void run(ActiveSkill skill1, int setting1, ActiveSkill skill2, int setting2, ActiveSkill skill3,
                    int setting3, boolean prepare,
                    double prepare_threshold) {
        this.run(skill1, setting1, skill2, setting2, skill3, setting3, prepare, prepare_threshold, Double.MAX_VALUE);
    }

    public double getTime_to_respawn() {
        return switch (enemy.name) {
            case "Devil" -> 5;
            case "Shax", "Dagon", "Lamia" -> 6;
            default -> 5;
        };
    }

    public void run(String skill1, int lvl1, SkillMod mod1, int setting1, String skill2, int lvl2, SkillMod mod2,
                    int setting2, String skill3, int lvl3, SkillMod mod3,
                    int setting3) {
        run(skill1, lvl1, mod1, setting1, skill2, lvl2, mod2, setting2, skill3, lvl3, mod3, setting3, 0);
    }

    public void run(String skill1, int lvl1, SkillMod mod1, int setting1, String skill2, int lvl2, SkillMod mod2,
                    int setting2, String skill3, int lvl3, SkillMod mod3,
                    int setting3, double reroll) {
        boolean prepare = false;
        int prepare_threshold = 0;
        Player pl = (Player) player;
        pl.eblast_enabled = skill1.equals("Elemental Blast") || skill2.equals("Elemental Blast") || skill3.equals("Elemental Blast");
        pl.holylight_enabled = skill1.equals("Holy Light") || skill2.equals("Holy Light") || skill3.equals("Holy Light");
        pl.aurablade_enabled = skill1.equals("Aura Blade") || skill2.equals("Aura Blade") || skill3.equals("Aura Blade");
        ActiveSkill s1 = pl.getSkill(skill1);
        s1.setSkill(lvl1, mod1);
        ActiveSkill s2 = null;
        if (!skill2.equals("Prepare")) {
            s2 = pl.getSkill(skill2);
            if (s2 != null) s2.setSkill(lvl2, mod2);
        } else {
            prepare = true;
            prepare_threshold = setting2;
        }
        ActiveSkill s3 = null;
        if (!skill3.equals("Prepare")) {
            s3 = pl.getSkill(skill3);
            if (s3 != null) s3.setSkill(lvl3, mod3);
        } else {
            prepare = true;
            prepare_threshold = setting3;
        }
        run(s1, setting1, s2, setting2, s3, setting3, prepare, prepare_threshold,
                reroll);
    }

    public void run(ActiveSkill skill1, int setting1, ActiveSkill skill2, int setting2, ActiveSkill skill3,
                    int setting3, boolean prepare,
                    double prepare_threshold, double reroll) {
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
        double hard_dmg = 0;
        int hard_hits = 0;
        double enemy_dmg = 0;
        int enemy_hits = 0;
        double healed = 0;
        int kills = 0;
        double oom_time = 0;
        title = enemy.getName();
        if (time_to_respawn == -1) time_to_respawn = getTime_to_respawn();
        if (potion1 != null) potion1.used = 0;
        if (potion2 != null) potion2.used = 0;
        if (potion3 != null) potion3.used = 0;
        StringBuilder result = new StringBuilder();
        StringBuilder setup = new StringBuilder();
        setup.append(title).append(" Simulations: ").append(simulations).append("\n");
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
        if (prepare) setup.append("Prepare threshold: ").append(prepare_threshold).append("\n");
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
        for (int i = 0; i < simulations; i++) {
            double time = 0;
            double delta;
            int casts = 0;
            double delay_left = 0;
            status = StatusType.respawn;
            enemy.reroll();
            player.checkAmbush();
            if (Main.game_version < 1535 && reroll >= 1) {
                while (enemy.getHp_max() >= reroll) {
                    enemy.reroll();
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
                    player.casting.startCast(player, enemy);
                    if (skill2 != null) skill2.used_in_rotation = 0;
                    if (skill3 != null) skill3.used_in_rotation = 0;
                }
                if (player.casting == null && skill2 != null && skill2.canCast(player) && skill2.shouldUse(player,
                        setting2)) {
                    player.casting = skill2;
                    player.casting.startCast(player, enemy);
                    //skill1.used_in_rotation = 0;
                }
                if (player.casting == null && skill3 != null && skill3.canCast(player) && skill3.shouldUse(player,
                        setting3)) {
                    player.casting = skill3;
                    player.casting.startCast(player, enemy);
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
                delta = 0.1;
                if (player.casting != null) delta = Math.min(delta, player.casting.calculate_delta());
                if (enemy.casting != null) delta = Math.min(delta, enemy.casting.calculate_delta());
                time += delta;
                total_time += delta;
                player.setMp(player.getMp() + player.getMp_regen() * delta);
                enemy.setMp(enemy.getMp() + enemy.getMp_regen() * delta);
                if (potion1 != null) potion1.checkPotion(player, delta);
                if (potion2 != null) potion2.checkPotion(player, delta);
                if (potion3 != null) potion3.checkPotion(player, delta);
                if (player.casting != null) {
                    if (player.casting.cast > 0) {
                        if (player.casting.progressCast(delta)) {
                            if (player.casting.hit > 0) {
                                casts++;
                                total_casts++;
                                double dmg = player.casting.attack(player, enemy);
                                if (dmg > 0) {
                                    if (dmg > hard_dmg_min_detect) {
                                        hard_hits++;
                                        hard_dmg += dmg;
                                    }
                                    enemy.setHp(enemy.getHp() - dmg);
//                                    System.out.println("Player dealt " + (int) dmg + " damage with " + player.casting.name);
                                } else {
//                                    System.out.println("Player missed with " + player.casting.name);
                                }
                            } else {
                                double previous_hp = player.getHp();
                                player.casting.use(player, enemy);
                                if (player.casting.heal) {
                                    healed += player.getHp() - previous_hp;
                                }
//                                System.out.println("Player casted " + player.casting.name);
                            }
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
                if (enemy.casting != null) {
                    if (enemy.casting.cast > 0) {
                        if (enemy.casting.progressCast(delta)) {
                            if (enemy.casting.hit > 0) {
                                double dmg = enemy.casting.attack(enemy, player);
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
                                enemy.casting.use(enemy, player);
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
                    status = StatusType.delay;
                    overkill -= enemy.getHp();
                    for (Debuff d : enemy.debuffs) {
                        if (d.dmg > 0) overkill += d.dmg;
                    }
                    double exp_gain = enemy.getExp() * player.getExp_mult();
                    if (player.lvling) player.increment_exp(exp_gain * milestone_exp_mult);
                    exp += exp_gain;
                    kills++;
//                    System.out.println("Enemy killed at " + df2.format(time) + " s");
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
            if (prepare && (status == StatusType.respawn || status == StatusType.rerolling || status == StatusType.delay)) {
                delta = 0.1;
                while (player.getHp() / player.getHp_max() < prepare_threshold / 100 || player.getMp() / player.getMp_max() < prepare_threshold / 100) {
                    //status = StatusType.prepare;
                    total_time += delta;
                    prepare_time += delta;
                    delay_left -= delta;
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

        }

        result.append("Exp/h: ").append((int) (exp * milestone_exp_mult / (total_time + death_time) * 3600)).append(" (").append(df2.format(milestone_exp_mult * 100)).append(
                "%)\n");
//        sb.append("Exp/h without milestones: ").append((int) (exp / (total_time + death_time) * 3600)).append("\n");
        if (potion1 != null) {
            result.append(potion1.type.toUpperCase()).append(" potion tier: ").append(potion1.tier).append(", used: ").append(potion1.used).append(", per hour: ").append(df2.format(potion1.used / (total_time + death_time) * 3600)).append(
                    "\n");
            crafting_time += potion1.craft_time(crafting_lvl, alchemy_lvl) * potion1.used;
        }
        if (potion2 != null) {
            result.append(potion2.type.toUpperCase()).append(" potion tier: ").append(potion2.tier).append(", used: ").append(potion2.used).append(", per hour: ").append(df2.format(potion2.used / (total_time + death_time) * 3600)).append(
                    "\n");
            crafting_time += potion2.craft_time(crafting_lvl, alchemy_lvl) * potion2.used;
        }
        if (potion3 != null) {
            result.append(potion3.type.toUpperCase()).append(" potion tier: ").append(potion3.tier).append(", used: ").append(potion3.used).append(", per hour: ").append(df2.format(potion3.used / (total_time + death_time) * 3600)).append(
                    "\n");
            crafting_time += potion3.craft_time(crafting_lvl, alchemy_lvl) * potion3.used;
        }
        if (crafting_time > 0) {
            //sb.append("Crafting time: ").append((int) crafting_time).append(" seconds \n");
            result.append("Effective exp/h: ").append((int) (exp * milestone_exp_mult / (total_time + crafting_time + death_time) * 3600)).append("\n");
        }
        if (prepare) {
            result.append("Time preparing %: ").append(df2.format(prepare_time / total_time * 100)).append("% " +
                    "\n");
        }
        if (death_time > 0) {
            result.append("Time dead %: ").append(df2.format(death_time / (total_time + death_time) * 100)).append("% \n");
            //sb.append("Spawning time: ").append(df2.format(respawning_time)).append("s \n");
            result.append("Exp/h without deaths: ").append((int) (exp * milestone_exp_mult / (total_time - ignore_deaths) * 3600)).append(
                    "\n");
        }
        if (oom_time > 0) {
            result.append("Time oom %: ").append(df2.format(oom_time / total_time  * 100)).append("% " +
                    "\n");
        }
        result.append("Kills/h without deaths: ").append(df2.format(kills / (total_time - ignore_deaths) * 3600)).append(
                "\n");
        result.append("Min time: ").append(df2.format(min_time)).append("s \n");
        result.append("Max time: ").append(df2.format(max_time)).append("s \n");
        result.append("Average time to kill + respawn: ").append(df2.format(total_time / kills)).append("s \n");
        result.append("Min skill casts: ").append(min_casts).append(" \n");
        result.append("Max skill casts: ").append(max_casts).append(" \n");
        result.append("Average skill casts: ").append(df2.format((double) total_casts / kills)).append(" \n");
        result.append("Average Overkill: ").append(df2.format(overkill / kills)).append(" \n");
        result.append("Average hard dmg: ").append(df2.format(hard_dmg / hard_hits)).append(" \n");
        if (skill1 != null && skill1.hit > 0) {
            result.append(skill1.name).append(" average hit chance: ").append(df2.format(skill1.average_hit_chance())).append(" \n");
        }
        if (skill2 != null && skill2.hit > 0) {
            result.append(skill2.name).append(" average hit chance: ").append(df2.format(skill2.average_hit_chance())).append(" \n");
        }
        if (skill3 != null && skill3.hit > 0) {
            result.append(skill3.name).append(" average hit chance: ").append(df2.format(skill3.average_hit_chance())).append(" \n");
        }
        if (enemy_hits > 0) {
            result.append("Average enemy dmg: ").append(df2.format(enemy_dmg / enemy_hits)).append(" \n");
        }
        if (healed > 0) {
            result.append("Average heal per fight: ").append(df2.format(healed / kills)).append(" \n");
        }
        if (enemy.skills != null) {
            for (ActiveSkill s : enemy.skills) {
//                sb.append(s.name).append(" used with smoke: ").append(df2.format((double) s.used_debuffed / s.used_in_rotation * 100.0)).append("% \n");
            }
        }
        essential_result = result.toString();
        full_result = setup + result.toString();
    }

    /**
     * Deprecated method for long-time simulations, to include CL/ML and active skills lvl-ups.
     * Probably won't work at all now, since it wasn't updated for many versions.
     */
    public void run_days(ActiveSkill skill1, int setting1, ActiveSkill skill2, int setting2, ActiveSkill skill3,
                         int setting3, boolean prepare,
                         double prepare_threshold, int days) {
        double total_time = 0;
        double leftover_time = 0;
        player.lvling = true;
        for (int day = 0; day < days; day++) {

            double exp = 0;

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
            double hard_dmg = 0;
            int hard_hits = 0;
            potion1.used = 0;
            potion2.used = 0;
            System.out.println(title + " Day: " + (day + 1));
            StringBuilder sb = new StringBuilder();
            sb.append(player.getName()).append(" ML/CL ").append(df2.format(player.getMl() + (player.ml_exp / player.exp_to_ml(player.getMl())))).append(
                    "/").append(df2.format(player.getCl() + (player.cl_exp / player.exp_to_cl(player.getCl(), 3)))).append("\n");
            sb.append("Active Skills: \n").append(skill1.name).append(" lvl ").append(skill1.lvl).append(" ").append(skill1.skillMod).append(" / ").append(setting1).append("\n");
            if (skill2 != null)
                sb.append(skill2.name).append(" lvl ").append(skill2.lvl).append(" ").append(skill2.skillMod).append(" / ").append(setting2).append("\n");
            if (skill3 != null)
                sb.append(skill3.name).append(" lvl ").append(skill3.lvl).append(" ").append(skill3.skillMod).append(" / ").append(setting3).append("\n");
//        sb.append("Passive skills:\n");
//        for(Map.Entry<String, PassiveSkill> passive : player.passives.entrySet()) {
//            if (passive.getValue().enabled) {
//                sb.append(passive.getValue().name).append(" ").append(passive.getValue().lvl).append("\n");
//            }
//        }
//        if (prepare) sb.append("Prepare threshold: ").append(prepare_threshold).append("\n");
//        if (potion1 != null)
//            sb.append(potion1.type.toUpperCase()).append(" potion tier: ").append(potion1.tier).append(", threshold: ").append(potion1.threshold).append(
//                    "\n");
//        if (potion2 != null)
//            sb.append(potion2.type.toUpperCase()).append(" potion tier: ").append(potion2.tier).append(", threshold: ").append(potion2.threshold).append(
//                    "\n");
            player.refreshStats();
            int sim_num = 0;
            while (sim_num < simulations && (total_time + crafting_time + death_time) < (3600 * 24 - leftover_time)) {
                sim_num++;
                double time = 0;
                double delta;
                int casts = 0;
                status = StatusType.respawn;
                enemy.reroll();

                skill1.used_in_rotation = 0;
                if (skill2 != null) skill2.used_in_rotation = 0;
                if (skill3 != null) skill3.used_in_rotation = 0;
                while (time < time_to_respawn) {
                    delta = Math.min(0.1, time_to_respawn - time);
                    time += delta;
                    total_time += delta;
                    player.setMp(player.getMp() + player.getMp_regen() * delta);
                    potion1.checkPotion(player, delta);
                    potion2.checkPotion(player, delta);
                }
                if (time >= time_to_respawn) {
                    status = StatusType.combat;
                }
                while (status == StatusType.combat && time < 3600) {
                    if (player.casting == null && skill1.canCast(player) && ((skill3 == null && skill2 == null) || skill1.shouldUse(player,
                            setting1))) {
                        player.casting = skill1;
                        player.casting.startCast(player, enemy);
                        if (skill2 != null) skill2.used_in_rotation = 0;
                        if (skill3 != null) skill3.used_in_rotation = 0;
                    }
                    if (player.casting == null && skill2 != null && skill2.canCast(player) && skill2.shouldUse(player,
                            setting2)) {
                        player.casting = skill2;
                        player.casting.startCast(player, enemy);
                    }
                    if (player.casting == null && skill3 != null && skill3.canCast(player) && skill3.shouldUse(player,
                            setting3)) {
                        player.casting = skill3;
                        player.casting.startCast(player, enemy);
                    }
                    if (skill3 != null && skill3.used_in_rotation >= setting3) {
                        skill1.used_in_rotation = 0;
                        skill2.used_in_rotation = 0;
                        skill3.used_in_rotation = 0;
                    }
                    if (skill3 == null && skill2 != null && skill2.used_in_rotation >= setting2) {
                        skill1.used_in_rotation = 0;
                        skill2.used_in_rotation = 0;
                    }

                    if (enemy.casting == null) {
                        ActiveSkill enemyCast = enemy.getCasting();
                        enemyCast.startCast(enemy, player);
                    }
                    delta = 0.1;
                    if (player.casting != null) delta = Math.min(delta, player.casting.calculate_delta());
                    if (enemy.casting != null) delta = Math.min(delta, enemy.casting.calculate_delta());
                    time += delta;
                    total_time += delta;
                    player.setMp(player.getMp() + player.getMp_regen() * delta);
                    enemy.setMp(enemy.getMp() + enemy.getMp_regen() * delta);
                    potion1.checkPotion(player, delta);
                    potion2.checkPotion(player, delta);
                    if (player.casting != null) {
                        if (player.casting.cast > 0) {
                            if (player.casting.progressCast(delta)) {
                                if (player.casting.hit > 0) {
                                    casts++;
                                    total_casts++;
                                    double dmg = player.casting.attack(player, enemy);
                                    if (dmg > 0) {
                                        if (dmg > 1200) {
                                            hard_hits++;
                                            hard_dmg += dmg;
                                        }
                                        enemy.setHp(enemy.getHp() - dmg);
                                        //System.out.println("Player dealt " + dmg + " damage with " + player.casting.name);
                                    } else {
                                        //System.out.println("Player missed with " + player.casting.name);
                                    }
                                } else {
                                    player.casting.use(player, enemy);
                                    //System.out.println("Player casted " + player.casting.name);
                                }
                                player.setMp(player.getMp() - player.casting.calculate_manacost(player));
                                player.tick_debuffs();
                                enemy.tick_buffs();
                            }
                        } else if (player.casting.delay > 0) {
                            if (player.casting.progressDelay(delta)) {
                                player.casting = null;
                            }
                        }
                    }
                    if (enemy.casting != null) {
                        if (enemy.casting.cast > 0) {
                            if (enemy.casting.progressCast(delta)) {
                                if (enemy.casting.hit > 0) {
                                    double dmg = enemy.casting.attack(enemy, player);
                                    if (dmg > 0) {
                                        player.setHp(player.getHp() - dmg);
                                        //System.out.println("Enemy dealt " + dmg + " damage with " + enemy.casting.name);
                                    } else {
                                        //System.out.println("Enemy missed with " + enemy.casting.name);
                                    }
                                    enemy.setMp(enemy.getMp() - enemy.casting.calculate_manacost(enemy));
                                    enemy.tick_debuffs();
                                    player.tick_buffs();
                                } else {
                                    enemy.casting.use(enemy, player);
                                }
                            }
                        } else if (enemy.casting.delay > 0) {
                            if (enemy.casting.progressDelay(delta)) {
                                enemy.casting = null;
                            }
                        }
                    }
                    if (enemy.getHp() <= 0) {
                        status = StatusType.respawn;
                        overkill -= enemy.getHp();
                        for (Debuff d : enemy.debuffs) {
                            if (d.dmg > 0) overkill += d.dmg;
                        }
                        double exp_gain = enemy.getExp() * player.getExp_mult();
                        if (player.lvling) player.increment_exp(exp_gain * milestone_exp_mult);
                        exp += exp_gain;
                    }
                    if (player.getHp() <= 0) {
                        status = StatusType.death;
                        death_time += 15 * 60;
                        player.setHp(player.getHp_max());
                        player.setMp(player.getMp_max());
                        potion1.cooldown = 0;
                        potion2.cooldown = 0;
                        ignore_deaths += time;
                    }
                }

                if (status == StatusType.respawn && prepare) {
                    delta = 0.1;
                    while (player.getHp() / player.getHp_max() < prepare_threshold / 100 || player.getMp() / player.getMp_max() < prepare_threshold / 100) {
                        status = StatusType.prepare;
                        total_time += delta;
                        prepare_time += delta;
                        player.setHp(player.getHp() + player.getPrepare_hps() * delta);
                        player.setMp(player.getMp() + player.getPrepare_mps() * delta);
                    }
                    status = StatusType.respawn;
                }
                min_time = Math.min(min_time, time);
                max_time = Math.max(max_time, time);
                min_casts = Math.min(min_casts, casts);
                max_casts = Math.max(max_casts, casts);
            }

            sb.append("Exp/h: ").append((int) (exp * milestone_exp_mult / (total_time + death_time + crafting_time) * 3600)).append("\n");
//        sb.append("Exp/h without milestones: ").append((int) (exp / (total_time + death_time) * 3600)).append("\n");
            if (potion1 != null) {
                sb.append(potion1.type.toUpperCase()).append(" potion tier: ").append(potion1.tier).append(", used: ").append(potion1.used).append(", per hour: ").append(df2.format(potion1.used / (total_time + death_time) * 3600)).append(
                        "\n");
                int need_to_craft = Math.max(0, potion1.used - potion1.count);
                potion1.count = Math.max(0, potion1.count - potion1.used);
                potion1.used = 0;
                crafting_time += potion1.craft_time(crafting_lvl, alchemy_lvl) * need_to_craft;
            }
            if (potion2 != null) {
                sb.append(potion2.type.toUpperCase()).append(" potion tier: ").append(potion2.tier).append(", used: ").append(potion2.used).append(", per hour: ").append(df2.format(potion2.used / (total_time + death_time) * 3600)).append(
                        "\n");
                int need_to_craft = Math.max(0, potion2.used - potion2.count);
                potion2.count = Math.max(0, potion2.count - potion2.used);
                potion2.used = 0;
                crafting_time += potion2.craft_time(crafting_lvl, alchemy_lvl) * need_to_craft;
            }
            sb.append("Combat time: ").append((int) total_time).append(" seconds \n");
            if (crafting_time > 0) {
                sb.append("Crafting time: ").append((int) crafting_time).append(" seconds \n");
                sb.append("Effective exp/h: ").append((int) (exp * milestone_exp_mult / (total_time + crafting_time + death_time) * 3600)).append("\n");
            }
            if (prepare) {
                sb.append("Time preparing %: ").append(df2.format(prepare_time / total_time * 100)).append("% " +
                        "\n");
            }
            sb.append("Time dead %: ").append(df2.format(death_time / (total_time + death_time) * 100)).append("% \n");
            sb.append("Average skill casts: ").append(df2.format((double) total_casts / sim_num)).append(" \n");
            sb.append("Kills: ").append(sim_num).append(" \n");
            System.out.println(sb);
            leftover_time = total_time + crafting_time + death_time + leftover_time - 3600 * 24;
            //System.out.println(leftover_time);
            total_time = 0;
        }

    }
}
