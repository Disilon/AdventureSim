package Disilon;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static Disilon.Core.getBonusType;
import static Disilon.Main.capitalizeFirst;
import static Disilon.Main.df2;
import static Disilon.Main.game_version;
import static Disilon.Main.log;
import static Disilon.Main.minIfNotZero;
import static Disilon.UserForm.maxResearchLvl;

public class Player extends Actor {
    public static String[] availableClasses = {"Newbie", "Squire", "Adventurer", "Student",
            "Thief", "Warrior", "Archer", "Fighter", "Mage", "Cleric",
            "Assassin", "Pyromancer", "Sniper",  "Knight", "Priest", "Hunter", "Rogue", "Geomancer", "Monk",
            "Onion Knight", "Scholar", "Alchemist",
            "Holy Archer",  "Ninja", "Tea Rogue"};

    double rp_balance;
    double old_rp;
    double research_slots_stat;
    double rp_drain;
    double hard_reward = 1;
    double total_exp_mult = 1;
    String previous_research;
    boolean decide_research = false;
    int alchemist_lvl = 0;
    boolean crafting_progress = false;
    boolean smithing_progress = false;
    boolean alchemy_progress = false;
    boolean alchemy_consumption_based = false;
    double maincrafting_time = 0;
    double potion_sidecraft_time = 0;
    double pill_sidecraft_time = 0;
    double combat_potion_sidecraft_time = 0;
    int[] skill_setting = {1,1,1,1};
    LinkedHashMap<String, Double> research_weight_sorted = new LinkedHashMap<>(32);

    public Player() {
        super();
        initializeSets();
    }

    public Player(Setup setup) {
        this();
        this.enemy_min_lvl_enabled = setup.enemy_min_lvl_increase;
        this.research_lvls = setup.research_lvls;
        this.research_old_lvls = new HashMap<>();
        this.research_old_lvls.putAll(setup.research_lvls);
        this.research_weight = setup.research_weight;
        this.bestiary = setup.bestiary;
        this.rp_balance = setup.rp_balance;
        this.old_rp = setup.rp_balance;
        this.pill = new Pill(setup.pill);
        this.setClass(setup.playerclass);
        this.setCLML(setup.cl, setup.ml);
        this.old_cl = setup.cl;
        this.old_ml = setup.ml;
        this.lvling = setup.leveling;
        this.zone = setup.zone;
        this.alchemist_lvl = setup.alchemist_lvl;
        crafting_progress = setup.crafting_progress;
        smithing_progress = setup.smithing_progress;
        alchemy_progress = setup.alchemy_progress;
        alchemy_consumption_based = setup.alchemy_consumption_based;
        if (setup.hard_hp > 0) zone.hard_hp = setup.hard_hp / 100;
        if (setup.hard_stats > 0) zone.hard_stats = setup.hard_stats / 100;
        if (setup.hard_reward > 0) hard_reward = setup.hard_reward / 100;
        this.zone.stats.clear_recorded_data();
        setupPotions(0, setup.potion1, setup.potion1_t);
        setupPotions(1, setup.potion2, setup.potion2_t);
        setupPotions(2, setup.potion3, setup.potion3_t);
        this.clear_skills_recorded_data();
        this.equipment.clear();
        this.setEquip("MH", setup.mh_name, setup.mh_tier, setup.mh_lvl);
        this.setEquip("OH", setup.oh_name, setup.oh_tier, setup.oh_lvl);
        this.setEquip("Helmet", setup.helmet_name, setup.helmet_tier, setup.helmet_lvl);
        this.setEquip("Chest", setup.chest_name, setup.chest_tier, setup.chest_lvl);
        this.setEquip("Pants", setup.pants_name, setup.pants_tier, setup.pants_lvl);
        this.setEquip("Bracer", setup.bracer_name, setup.bracer_tier, setup.bracer_lvl);
        this.setEquip("Boots", setup.boots_name, setup.boots_tier, setup.boots_lvl);
        this.setEquip("Accessory1", setup.accessory1_name, setup.accessory1_tier, setup.accessory1_lvl);
        this.setEquip("Accessory2", setup.accessory2_name, setup.accessory2_tier, setup.accessory2_lvl);
        this.setEquip("Necklace", setup.necklace_name, setup.necklace_tier, setup.necklace_lvl);
        this.cores = setup.cores;
        this.milestone_exp_mult = setup.milestone / 100;
        this.old_milestone_exp_mult = this.milestone_exp_mult;
        this.r_spd_bonus = setup.r_spd_bonus / 100;
        this.disableAllActives();
        for (String skill : setup.passives_lvls.keySet()) {
            if (passives.containsKey(skill)) {
                double lvl = setup.passives_lvls.get(skill);
                passives.get(skill).setLvl(lvl);
                passives.get(skill).old_lvl = lvl;
            }
        }
        for (String skill : setup.actives_lvls.keySet()) {
            if (active_skills.containsKey(skill) && (SkillData.potion_skills == null || !SkillData.potion_skills.contains(skill))) {
                double lvl = setup.actives_lvls.get(skill);
                active_skills.get(skill).setLvl(lvl);
                active_skills.get(skill).old_lvl = lvl;
            }
        }
        this.skill1 = getSkill(setup.skill1, setup.skill1_s);
        if (this.skill1 != null) {
            this.skill1.setSkill(setup.skill1_mod);
            skill_setting[0] = setup.skill1_s;
        }
        this.skill2 = getSkill(setup.skill2, setup.skill2_s);
        if (this.skill2 != null) {
            this.skill2.setSkill(setup.skill2_mod);
            skill_setting[1] = setup.skill2_s;
        }
        this.skill3 = getSkill(setup.skill3, setup.skill3_s);
        if (this.skill3 != null) {
            this.skill3.setSkill(setup.skill3_mod);
            skill_setting[2] = setup.skill3_s;
        }
        this.skill4 = getSkill(setup.skill4, setup.skill4_s);
        if (this.skill4 != null) {
            this.skill4.setSkill(setup.skill4_mod);
            skill_setting[3] = setup.skill4_s;
        }
        this.enablePassives(new String[]{setup.pskill1, setup.pskill2, setup.pskill3, setup.pskill4});
        apply_research_effects();
        this.zone.stats.setGradeCutoff(this);
    }

    public void setupPotions(int id, String type, int threshold) {
        if (!type.equals("None")) {
            potions[id] = new Potion(this, type, threshold);
        } else {
            potions[id] = null;
        }
    }

    public void checkPotion(double delta) {
        for (int i = 0; i < 3; i++) {
            if (potions[i] != null) potions[i].checkPotion(this, delta);
        }
    }

    public void tickPotion(double delta) {
        for (int i = 0; i < 3; i++) {
            if (potions[i] != null) potions[i].tickPotion(delta);
        }
    }

    public void resetPotionCd() {
        for (int i = 0; i < 3; i++) {
            if (potions[i] != null) potions[i].cooldown = 0;
        }
    }

    public void setClass(String name) {
        this.name = name;
        skills.disableAll();
        base_phys_res = 0;
        base_magic_res = 0;
        base_water_res = 0;
        base_fire_res = 0;
        base_wind_res = 0;
        base_earth_res = 0;
        base_light_res = 0;
        base_dark_res = 0;
        switch (name) {
            case "Assassin" -> {
                base_dark_res = 0.5;
                base_light_res = -0.5;
            }
            case "Monk" -> {
                base_dark_res = 0.5;
                base_water_res = -0.5;
            }
            case "Pyromancer" -> {
                base_fire_res = 0.5;
                base_water_res = -0.5;
            }
            case "Geomancer" -> {
                base_earth_res = 0.5;
                base_wind_res = -0.5;
            }
            case "Priest" -> {
                base_dark_res = -0.5;
                base_light_res = 0.5;
            }
            case "Scholar","Hunter" -> {
                base_fire_res = 0.2;
                base_water_res = 0.2;
                base_wind_res = 0.2;
                base_earth_res = 0.2;
            }
            case "Sniper" -> {
                base_wind_res = 0.5;
                base_fire_res = -0.5;
            }
            case "Knight" -> {
                base_phys_res = 0.1;
                base_fire_res = 0.1;
                base_water_res = 0.1;
                base_wind_res = 0.1;
                base_earth_res = 0.1;
                base_light_res = 0.1;
                base_dark_res = 0.1;
                base_magic_res = -0.5;
            }
            case "Rogue" -> {
                base_water_res = -0.5;
            }
            case "Holy Archer" -> {
                base_fire_res = -0.5;
                base_dark_res = -0.5;
            }
        }
        enableSkills(name);
    }

    public ArrayList<String> getAvailableActiveSkills() {
        ArrayList<String> v = new ArrayList<>();
        v.add("None");
        for (String skill : active_skills.keySet()) {
            if (active_skills.get(skill).available) {
                v.add(skill);
            }
        }
        return v;
    }

    public ArrayList<String> getAvailablePassiveSkills() {
        ArrayList<String> v = new ArrayList<>();
        v.add("None");
        for (String skill : passives.keySet()) {
            if (!skill.equals("Tsury Finke") && passives.get(skill).available) {
                v.add(skill);
            }
        }
        return v;
    }

    public ActiveSkill getSkill(String name, int setting) {
        if (name.equals("Prepare") && active_skills.containsKey(name)) {
            this.prepare = active_skills.get(name);
            this.prepare_threshold = setting;
        }
        ActiveSkill skill = active_skills.get(name);
        if (skill != null) {
            skill.use_setting = setting;
            skill.enabled = true;
        }
        return skill;
    }

    public void setCLML(double cl, double ml) {
        setCLML((int) cl, (int) ml);
        double next_cl_exp = exp_to_cl((int) cl);
        double next_ml_exp = exp_to_ml((int) ml);
        cl_exp = next_cl_exp * (cl - (int) cl);
        ml_exp = next_ml_exp * (ml - (int) ml);
    }

    @Override
    public void setCLML(int cl, int ml) {
        this.cl = cl;
        this.ml = ml;
        switch (name) {
            case "Assassin" -> {
                base_hp_max = (double) (90 * (cl + 100)) / 10000 * 30 * ml;
                base_atk = (double) (130 * (cl + 100)) / 10000 * 4 * ml;
                base_def = (double) (80 * (cl + 100)) / 10000 * 4 * ml;
                base_int = (double) (90 * (cl + 100)) / 10000 * 4 * ml;
                base_res = (double) (80 * (cl + 100)) / 10000 * 4 * ml;
                base_hit = (double) (100 * (cl + 100)) / 10000 * 4 * ml;
                base_speed = (double) (130 * (cl + 100)) / 10000 * 4 * ml;
            }
            case "Fighter" -> {
                base_hp_max = (double) (100 * (cl + 100)) / 10000 * 30 * ml;
                base_atk = (double) (120 * (cl + 100)) / 10000 * 4 * ml;
                base_def = (double) (110 * (cl + 100)) / 10000 * 4 * ml;
                base_int = (double) (50 * (cl + 100)) / 10000 * 4 * ml;
                base_res = (double) (70 * (cl + 100)) / 10000 * 4 * ml;
                base_hit = (double) (90 * (cl + 100)) / 10000 * 4 * ml;
                base_speed = (double) (100 * (cl + 100)) / 10000 * 4 * ml;
            }
            case "Warrior" -> {
                base_hp_max = (double) (110 * (cl + 100)) / 10000 * 30 * ml;
                base_atk = (double) (140 * (cl + 100)) / 10000 * 4 * ml;
                base_def = (double) (110 * (cl + 100)) / 10000 * 4 * ml;
                base_int = (double) (50 * (cl + 100)) / 10000 * 4 * ml;
                base_res = (double) (50 * (cl + 100)) / 10000 * 4 * ml;
                base_hit = (double) (80 * (cl + 100)) / 10000 * 4 * ml;
                base_speed = (double) (100 * (cl + 100)) / 10000 * 4 * ml;
            }
            case "Monk" -> {
                base_hp_max = (double) (90 * (cl + 100)) / 10000 * 30 * ml;
                base_atk = (double) (110 * (cl + 100)) / 10000 * 4 * ml;
                base_def = (double) (90 * (cl + 100)) / 10000 * 4 * ml;
                base_int = (double) (110 * (cl + 100)) / 10000 * 4 * ml;
                base_res = (double) (100 * (cl + 100)) / 10000 * 4 * ml;
                base_hit = (double) (100 * (cl + 100)) / 10000 * 4 * ml;
                base_speed = (double) (110 * (cl + 100)) / 10000 * 4 * ml;
            }
            case "Knight" -> {
                base_hp_max = (double) (130 * (cl + 100)) / 10000 * 30 * ml;
                base_atk = (double) (110 * (cl + 100)) / 10000 * 4 * ml;
                base_def = (double) (150 * (cl + 100)) / 10000 * 4 * ml;
                base_int = (double) (50 * (cl + 100)) / 10000 * 4 * ml;
                base_res = (double) (100 * (cl + 100)) / 10000 * 4 * ml;
                base_hit = (double) (90 * (cl + 100)) / 10000 * 4 * ml;
                base_speed = (double) (90 * (cl + 100)) / 10000 * 4 * ml;
            }
            case "Pyromancer" -> {
                base_hp_max = (double) (70 * (cl + 100)) / 10000 * 30 * ml;
                base_atk = (double) (80 * (cl + 100)) / 10000 * 4 * ml;
                base_def = (double) (70 * (cl + 100)) / 10000 * 4 * ml;
                base_int = (double) (180 * (cl + 100)) / 10000 * 4 * ml;
                base_res = (double) (120 * (cl + 100)) / 10000 * 4 * ml;
                base_hit = (double) (90 * (cl + 100)) / 10000 * 4 * ml;
                base_speed = (double) (90 * (cl + 100)) / 10000 * 4 * ml;
            }
            case "Geomancer" -> {
                base_hp_max = (double) (80 * (cl + 100)) / 10000 * 30 * ml;
                base_atk = (double) (70 * (cl + 100)) / 10000 * 4 * ml;
                base_def = (double) (110 * (cl + 100)) / 10000 * 4 * ml;
                base_int = (double) (150 * (cl + 100)) / 10000 * 4 * ml;
                base_res = (double) (120 * (cl + 100)) / 10000 * 4 * ml;
                base_hit = (double) (80 * (cl + 100)) / 10000 * 4 * ml;
                base_speed = (double) (90 * (cl + 100)) / 10000 * 4 * ml;
            }
            case "Scholar" -> {
                base_hp_max = (double) (70 * (cl + 100)) / 10000 * 30 * ml;
                base_atk = (double) (70 * (cl + 100)) / 10000 * 4 * ml;
                base_def = (double) (100 * (cl + 100)) / 10000 * 4 * ml;
                base_int = (double) (160 * (cl + 100)) / 10000 * 4 * ml;
                base_res = (double) (100 * (cl + 100)) / 10000 * 4 * ml;
                base_hit = (double) (100 * (cl + 100)) / 10000 * 4 * ml;
                base_speed = (double) (100 * (cl + 100)) / 10000 * 4 * ml;
            }
            case "Priest" -> {
                base_hp_max = (double) (90 * (cl + 100)) / 10000 * 30 * ml;
                base_atk = (double) (90 * (cl + 100)) / 10000 * 4 * ml;
                base_def = (double) (100 * (cl + 100)) / 10000 * 4 * ml;
                base_int = (double) (110 * (cl + 100)) / 10000 * 4 * ml;
                base_res = (double) (130 * (cl + 100)) / 10000 * 4 * ml;
                base_hit = (double) (90 * (cl + 100)) / 10000 * 4 * ml;
                base_speed = (double) (90 * (cl + 100)) / 10000 * 4 * ml;
            }
            case "Mage" -> {
                base_hp_max = (double) (70 * (cl + 100)) / 10000 * 30 * ml;
                base_atk = (double) (70 * (cl + 100)) / 10000 * 4 * ml;
                base_def = (double) (70 * (cl + 100)) / 10000 * 4 * ml;
                base_int = (double) (140 * (cl + 100)) / 10000 * 4 * ml;
                base_res = (double) (120 * (cl + 100)) / 10000 * 4 * ml;
                base_hit = (double) (80 * (cl + 100)) / 10000 * 4 * ml;
                base_speed = (double) (90 * (cl + 100)) / 10000 * 4 * ml;
            }
            case "Squire" -> {
                base_hp_max = (double) (110 * (cl + 100)) / 10000 * 30 * ml;
                base_atk = (double) (110 * (cl + 100)) / 10000 * 4 * ml;
                base_def = (double) (110 * (cl + 100)) / 10000 * 4 * ml;
                base_int = (double) (50 * (cl + 100)) / 10000 * 4 * ml;
                base_res = (double) (50 * (cl + 100)) / 10000 * 4 * ml;
                base_hit = (double) (80 * (cl + 100)) / 10000 * 4 * ml;
                base_speed = (double) (80 * (cl + 100)) / 10000 * 4 * ml;
            }
            case "Adventurer" -> {
                base_hp_max = (double) (90 * (cl + 100)) / 10000 * 30 * ml;
                base_atk = (double) (100 * (cl + 100)) / 10000 * 4 * ml;
                base_def = (double) (90 * (cl + 100)) / 10000 * 4 * ml;
                base_int = (double) (60 * (cl + 100)) / 10000 * 4 * ml;
                base_res = (double) (60 * (cl + 100)) / 10000 * 4 * ml;
                base_hit = (double) (90 * (cl + 100)) / 10000 * 4 * ml;
                base_speed = (double) (100 * (cl + 100)) / 10000 * 4 * ml;
            }
            case "Alchemist" -> {
                base_hp_max = (double) (90 * (cl + 100)) / 10000 * 30 * ml;
                base_atk = (double) (120 * (cl + 100)) / 10000 * 4 * ml;
                base_def = (double) (90 * (cl + 100)) / 10000 * 4 * ml;
                base_int = (double) (120 * (cl + 100)) / 10000 * 4 * ml;
                base_res = (double) (100 * (cl + 100)) / 10000 * 4 * ml;
                base_hit = (double) (90 * (cl + 100)) / 10000 * 4 * ml;
                base_speed = (double) (90 * (cl + 100)) / 10000 * 4 * ml;
            }
            case "Student" -> {
                base_hp_max = (double) (70 * (cl + 100)) / 10000 * 30 * ml;
                base_atk = (double) (70 * (cl + 100)) / 10000 * 4 * ml;
                base_def = (double) (70 * (cl + 100)) / 10000 * 4 * ml;
                base_int = (double) (110 * (cl + 100)) / 10000 * 4 * ml;
                base_res = (double) (110 * (cl + 100)) / 10000 * 4 * ml;
                base_hit = (double) (70 * (cl + 100)) / 10000 * 4 * ml;
                base_speed = (double) (90 * (cl + 100)) / 10000 * 4 * ml;
            }
            case "Sniper" -> {
                base_hp_max = (double) (90 * (cl + 100)) / 10000 * 30 * ml;
                base_atk = (double) (150 * (cl + 100)) / 10000 * 4 * ml;
                base_def = (double) (80 * (cl + 100)) / 10000 * 4 * ml;
                base_int = (double) (60 * (cl + 100)) / 10000 * 4 * ml;
                base_res = (double) (80 * (cl + 100)) / 10000 * 4 * ml;
                base_hit = (double) (160 * (cl + 100)) / 10000 * 4 * ml;
                base_speed = (double) (80 * (cl + 100)) / 10000 * 4 * ml;
            }
            case "Archer" -> {
                base_hp_max = (double) (80 * (cl + 100)) / 10000 * 30 * ml;
                base_atk = (double) (90 * (cl + 100)) / 10000 * 4 * ml;
                base_def = (double) (80 * (cl + 100)) / 10000 * 4 * ml;
                base_int = (double) (80 * (cl + 100)) / 10000 * 4 * ml;
                base_res = (double) (70 * (cl + 100)) / 10000 * 4 * ml;
                base_hit = (double) (130 * (cl + 100)) / 10000 * 4 * ml;
                base_speed = (double) (110 * (cl + 100)) / 10000 * 4 * ml;
            }
            case "Cleric" -> {
                base_hp_max = (double) (80 * (cl + 100)) / 10000 * 30 * ml;
                base_atk = (double) (70 * (cl + 100)) / 10000 * 4 * ml;
                base_def = (double) (90 * (cl + 100)) / 10000 * 4 * ml;
                base_int = (double) (100 * (cl + 100)) / 10000 * 4 * ml;
                base_res = (double) (130 * (cl + 100)) / 10000 * 4 * ml;
                base_hit = (double) (80 * (cl + 100)) / 10000 * 4 * ml;
                base_speed = (double) (90 * (cl + 100)) / 10000 * 4 * ml;
            }
            case "Thief" -> {
                base_hp_max = (double) (90 * (cl + 100)) / 10000 * 30 * ml;
                base_atk = (double) (110 * (cl + 100)) / 10000 * 4 * ml;
                base_def = (double) (80 * (cl + 100)) / 10000 * 4 * ml;
                base_int = (double) (60 * (cl + 100)) / 10000 * 4 * ml;
                base_res = (double) (70 * (cl + 100)) / 10000 * 4 * ml;
                base_hit = (double) (100 * (cl + 100)) / 10000 * 4 * ml;
                base_speed = (double) (130 * (cl + 100)) / 10000 * 4 * ml;
            }
            case "Hunter" -> {
                base_hp_max = (double) (100 * (cl + 100)) / 10000 * 30 * ml;
                base_atk = (double) (110 * (cl + 100)) / 10000 * 4 * ml;
                base_def = (double) (90 * (cl + 100)) / 10000 * 4 * ml;
                base_int = (double) (80 * (cl + 100)) / 10000 * 4 * ml;
                base_res = (double) (80 * (cl + 100)) / 10000 * 4 * ml;
                base_hit = (double) (150 * (cl + 100)) / 10000 * 4 * ml;
                base_speed = (double) (110 * (cl + 100)) / 10000 * 4 * ml;
            }
            case "Rogue" -> {
                base_hp_max = (double) (100 * (cl + 100)) / 10000 * 30 * ml;
                base_atk = (double) (125 * (cl + 100)) / 10000 * 4 * ml;
                base_def = (double) (100 * (cl + 100)) / 10000 * 4 * ml;
                base_int = (double) (60 * (cl + 100)) / 10000 * 4 * ml;
                base_res = (double) (80 * (cl + 100)) / 10000 * 4 * ml;
                base_hit = (double) (110 * (cl + 100)) / 10000 * 4 * ml;
                base_speed = (double) (125 * (cl + 100)) / 10000 * 4 * ml;
            }
            case "Ninja" -> {
                base_hp_max = (double) (130 * (cl + 100)) / 10000 * 30 * ml;
                base_atk = (double) (180 * (cl + 100)) / 10000 * 4 * ml;
                base_def = (double) (140 * (cl + 100)) / 10000 * 4 * ml;
                base_int = (double) (120 * (cl + 100)) / 10000 * 4 * ml;
                base_res = (double) (120 * (cl + 100)) / 10000 * 4 * ml;
                base_hit = (double) (120 * (cl + 100)) / 10000 * 4 * ml;
                base_speed = (double) (180 * (cl + 100)) / 10000 * 4 * ml;
            }
            case "Onion Knight" -> {
                if (cl >= 99) {
                    base_hp_max = (double) (120 * (cl + 100)) / 10000 * 30 * ml;
                    base_atk = (double) (120 * (cl + 100)) / 10000 * 4 * ml;
                    base_def = (double) (120 * (cl + 100)) / 10000 * 4 * ml;
                    base_int = (double) (120 * (cl + 100)) / 10000 * 4 * ml;
                    base_res = (double) (120 * (cl + 100)) / 10000 * 4 * ml;
                    base_hit = (double) (120 * (cl + 100)) / 10000 * 4 * ml;
                    base_speed = (double) (120 * (cl + 100)) / 10000 * 4 * ml;
                } else {
                    base_hp_max = (double) (60 * (cl + 100)) / 10000 * 30 * ml;
                    base_atk = (double) (60 * (cl + 100)) / 10000 * 4 * ml;
                    base_def = (double) (60 * (cl + 100)) / 10000 * 4 * ml;
                    base_int = (double) (60 * (cl + 100)) / 10000 * 4 * ml;
                    base_res = (double) (60 * (cl + 100)) / 10000 * 4 * ml;
                    base_hit = (double) (60 * (cl + 100)) / 10000 * 4 * ml;
                    base_speed = (double) (60 * (cl + 100)) / 10000 * 4 * ml;
                }
            }
            case "Tea Rogue" -> {
                if (cl >= 99) {
                    base_hp_max = (double) (180 * (cl + 100)) / 10000 * 30 * ml;
                    base_atk = (double) (180 * (cl + 100)) / 10000 * 4 * ml;
                    base_def = (double) (180 * (cl + 100)) / 10000 * 4 * ml;
                    base_int = (double) (180 * (cl + 100)) / 10000 * 4 * ml;
                    base_res = (double) (180 * (cl + 100)) / 10000 * 4 * ml;
                    base_hit = (double) (180 * (cl + 100)) / 10000 * 4 * ml;
                    base_speed = (double) (180 * (cl + 100)) / 10000 * 4 * ml;
                } else {
                    base_hp_max = (double) (90 * (cl + 100)) / 10000 * 30 * ml;
                    base_atk = (double) (90 * (cl + 100)) / 10000 * 4 * ml;
                    base_def = (double) (90 * (cl + 100)) / 10000 * 4 * ml;
                    base_int = (double) (90 * (cl + 100)) / 10000 * 4 * ml;
                    base_res = (double) (90 * (cl + 100)) / 10000 * 4 * ml;
                    base_hit = (double) (90 * (cl + 100)) / 10000 * 4 * ml;
                    base_speed = (double) (90 * (cl + 100)) / 10000 * 4 * ml;
                }
            }
            case "Holy Archer" -> {
                base_hp_max = (double) (120 * (cl + 100)) / 10000 * 30 * ml;
                base_atk = (double) (150 * (cl + 100)) / 10000 * 4 * ml;
                base_def = (double) (120 * (cl + 100)) / 10000 * 4 * ml;
                base_int = (double) (150 * (cl + 100)) / 10000 * 4 * ml;
                base_res = (double) (150 * (cl + 100)) / 10000 * 4 * ml;
                base_hit = (double) (180 * (cl + 100)) / 10000 * 4 * ml;
                base_speed = (double) (120 * (cl + 100)) / 10000 * 4 * ml;
            }
            case "Newbie" -> {
                base_hp_max = (double) (60 * (cl + 100)) / 10000 * 30 * ml;
                base_atk = (double) (60 * (cl + 100)) / 10000 * 4 * ml;
                base_def = (double) (60 * (cl + 100)) / 10000 * 4 * ml;
                base_int = (double) (60 * (cl + 100)) / 10000 * 4 * ml;
                base_res = (double) (60 * (cl + 100)) / 10000 * 4 * ml;
                base_hit = (double) (60 * (cl + 100)) / 10000 * 4 * ml;
                base_speed = (double) (60 * (cl + 100)) / 10000 * 4 * ml;
            }
        }
        refreshStats();
    }

    public void setPassiveLvl(String passive, double lvl) {
        passives.get(passive).setLvl(lvl);
        passives.get(passive).old_lvl = lvl;
    }

    public double getEblast() {
        return eblast_enabled ? getIntel() * 0.05 : 0;
    }

    @Override
    public double getFire() {
        double result = gear_fire;
        switch (name) {
            case "Pyromancer", "Monk" -> {
                result += getAvgAtkInt();
            }
            case "Alchemist" -> result += getAvgAtkInt() * 0.4;
            default -> {
            }
        }
        result += getAvgStats() * 2 * passives.get("Fire Boost").getBonus();
        result += (getAtk() + getIntel()) * 0.4 * passives.get("Fiery Aura").getBonus();
        if (active_skills.get("Aura Shot").enabled) {
            result += 0.15 * getAtk();
        }
        if (active_skills.get("Elemental Blast").enabled) {
            result += 0.05 * getIntel();
        }
        result += getEblast();
        return result;
    }

    @Override
    public double getWater() {
        double result = gear_water;
        switch (name) {
            case "Pyromancer" -> {
                result -= getAvgAtkInt();
            }
            case "Rogue", "Ninja", "Tea Rogue" -> {
                result += getAvgAtkInt();
            }
            case "Alchemist" -> result += getAvgAtkInt() * 0.4;
            default -> {
            }
        }
        result += getAvgStats() * 2 * passives.get("Water Boost").getBonus();
        if (active_skills.get("Elemental Blast").enabled) {
            result += 0.05 * getIntel();
        }
        return result;
    }

    @Override
    public double getWind() {
        double result = gear_wind;
        switch (name) {
            case "Sniper","Holy Archer" -> {
                result += getAvgAtkInt();
            }
            case "Geomancer" -> {
                result -= getAvgAtkInt();
            }
            case "Alchemist" -> result += getAvgAtkInt() * 0.4;
            default -> {
            }
        }
        if (active_skills.get("Elemental Blast").enabled) {
            result += 0.05 * getIntel();
        }
        return result;
    }

    @Override
    public double getEarth() {
        double result = gear_earth;
        switch (name) {
            case "Geomancer" -> {
                result += getAvgAtkInt();
            }
            case "Alchemist" -> result += getAvgAtkInt() * 0.4;
            default -> {
            }
        }
        result += getAvgStats() * 2 * passives.get("Earth Boost").getBonus();
        if (active_skills.get("Elemental Blast").enabled) {
            result += 0.05 * getIntel();
        }
        return result;
    }

    @Override
    public double getDark() {
        double result = gear_dark;
        switch (name) {
            case "Assassin","Ninja" -> {
                result += getAvgAtkInt();
            }
            case "Priest", "Monk" -> {
                result -= getAvgAtkInt();
            }
            case "Alchemist" -> result += getAvgAtkInt() * 0.4;
            default -> {
            }
        }
        return result;
    }

    @Override
    public double getLight() {
        double result = gear_light;
        switch (name) {
            case "Assassin", "Sniper" -> {
                result -= getAvgAtkInt();
            }
            case "Priest","Holy Archer" -> {
                result += getAvgAtkInt();
            }
            case "Alchemist" -> result += getAvgAtkInt() * 0.4;
            default -> {
            }
        }
        result += getAvgStats() * 2 * passives.get("Light Boost").getBonus();
        if (active_skills.get("Holy Light").enabled) {
            result += 0.25 * getResist();
        }
        if (active_skills.get("Prayer").enabled) {
            result += 0.25 * getResist();
        }
        if (active_skills.get("Aura Blade").enabled) {
            result += 0.1 * getAtk();
        }
        return result;
    }

    public void gearStat(StringBuilder sb, String label, double stat, double gear_stat, boolean show_percent,
                         boolean research_bonus) {
        if (research_bonus) {
            gear_stat *= 1 + 0.01 * getResearchLvl("Equip " + capitalizeFirst(label.toLowerCase()));
        }
        sb.append(label).append(" = ");
        sb.append(Math.round(stat));
        sb.append(" (").append(Math.round(gear_stat));
        if (show_percent) {
            sb.append(" = ").append(df2.format(gear_stat * 100 / stat)).append("%");
        }
        sb.append(")");
        sb.append("\n");
    }

    public String getAllStats() {
        StringBuilder sb = new StringBuilder();
        gearStat(sb, "HP", getHp_max(), gear_hp, true, true);
        sb.append("MP = ").append(Math.round(getMp_max())).append("\n");
        gearStat(sb, "ATK", getAtk(), gear_atk, true, true);
        gearStat(sb, "DEF", getDef(), gear_def, true, true);
        gearStat(sb, "INT", getIntel(), gear_int, true, true);
        gearStat(sb, "RES", getResist(), getGear_res(), true, false);
        gearStat(sb, "HIT", getHit(), getHit() - base_hit * hit_mult, true, false);
        gearStat(sb, "SPD", getSpeed(), gear_speed, true, true);
        sb.append("\n");
        if (getWater() != 0) {
            sb.append("Water = ").append(Math.round(getWater())).append(" (").append(Math.round(gear_water)).append(
                    ")\n");
        }
        if (getFire() != 0) {
            sb.append("Fire = ").append(Math.round(getFire())).append(" (").append(Math.round(gear_fire)).append(")\n");
        }
        if (getWind() != 0) {
            sb.append("Wind = ").append(Math.round(getWind())).append(" (").append(Math.round(gear_wind)).append(")\n");
        }
        if (getEarth() != 0) {
            sb.append("Earth = ").append(Math.round(getEarth())).append(" (").append(Math.round(gear_earth)).append(
                    ")\n");
        }
        if (getLight() != 0) {
            sb.append("Light = ").append(Math.round(getLight())).append(" (").append(Math.round(gear_light)).append(
                    ")\n");
        }
        if (getDark() != 0) {
            sb.append("Dark = ").append(Math.round(getDark())).append(" (").append(Math.round(gear_dark)).append(")\n");
        }
        if (gear_no_elem != 0) {
            sb.append("Non elem = ").append(Math.round(gear_no_elem)).append(" (").append(Math.round(gear_no_elem)).append(
                    ")\n");
        }
        sb.append("\n");
        if (getPhys_res() != 0) {
            sb.append("Physical mitigation = ").append(df2.format(getPhys_res() * 100)).append("%\n");
        }
        if (getMagic_res() != 0) {
            sb.append("Magic mitigation = ").append(df2.format(getMagic_res() * 100)).append("%\n");
        }
        if (getWater_res() != 0) {
            sb.append("Water mitigation = ").append(df2.format(getWater_res() * 100)).append("%\n");
        }
        if (getFire_res() != 0) {
            sb.append("Fire mitigation = ").append(df2.format(getFire_res() * 100)).append("%\n");
        }
        if (getWind_res() != 0) {
            sb.append("Wind mitigation = ").append(df2.format(getWind_res() * 100)).append("%\n");
        }
        if (getEarth_res() != 0) {
            sb.append("Earth mitigation = ").append(df2.format(getEarth_res() * 100)).append("%\n");
        }
        if (getLight_res() != 0) {
            sb.append("Light mitigation = ").append(df2.format(getLight_res() * 100)).append("%\n");
        }
        if (getDark_res() != 0) {
            sb.append("Dark mitigation = ").append(df2.format(getDark_res() * 100)).append("%\n");
        }
        if (getCrit_chance() > 0) sb.append("Crit = ").append(df2.format(getCrit_chance() * 100)).append("%\n");
        if (getCrit_chance() > 0) sb.append("Crit damage = ").append(df2.format(getCrit_damage() * 100)).append("%\n");
        if (gear_stun > 0) sb.append("Stun = ").append(df2.format(gear_stun * 100)).append("%\n");
        if (gear_analyze > 0) sb.append("Analyze = ").append(df2.format(gear_analyze * 100)).append("%\n");
        if (gear_barrier > 0) sb.append("Barrier buff = ").append(df2.format(gear_barrier * 100)).append("%\n");
        if (gear_potion > 0) sb.append("Potion buff = ").append(df2.format(gear_potion * 100)).append("%\n");
        if ((burn_mult + gear_burn) > 1)
            sb.append("Burn = ").append(df2.format((burn_mult + gear_burn) * 100 - 100)).append("%\n");
        if (set_hit > 1) sb.append("Set Hit = ").append(df2.format(set_hit * 100 - 100)).append("%\n");
        if (set_res > 1) sb.append("Set Res = ").append(df2.format(set_res * 100 - 100)).append("%\n");
        if (set_water > 1) sb.append("Set WaterDmg = ").append(df2.format(set_water * 100 - 100)).append("%\n");
        if (set_wind > 1) sb.append("Set WindDmg = ").append(df2.format(set_wind * 100 - 100)).append("%\n");
        if (set_fire > 1) sb.append("Set FireDmg = ").append(df2.format(set_fire * 100 - 100)).append("%\n");
        if (set_earth > 1) sb.append("Set EarthDmg = ").append(df2.format(set_earth * 100 - 100)).append("%\n");
        if (set_dark > 1) sb.append("Set DarkDmg = ").append(df2.format(set_dark * 100 - 100)).append("%\n");
        if (set_magicdmg > 1) sb.append("Set MagicDmg = ").append(df2.format(set_magicdmg * 100 - 100)).append("%\n");
        if (set_physdmg > 1) sb.append("Set PhysDmg = ").append(df2.format(set_physdmg * 100 - 100)).append("%\n");
        if (set_core > 0) {
            sb.append("Set Overkill/Manacost = ").append(df2.format(set_core * 100)).append("%");
            sb.append("\n");
            sb.append("Core drop = ").append(df2.format(set_core * 150)).append("%\n");
        }
        if (set_training > 0) {
            sb.append("Set Exp = ").append(df2.format(set_exp * 100)).append("%");
            sb.append("\n");
            sb.append("Dmg = -").append(df2.format(set_training * 100)).append("%\n");
        }
        if (set_mit1 > 0) sb.append("Set DmgMit = ").append(df2.format(set_mit1 * 100)).append("%\n");
        if (set_mit2 > 0) sb.append("Set DmgMit = ").append(df2.format(set_mit2 * 100)).append("%\n");
        if (set_mana > 0) sb.append("Set Manacost = ").append(df2.format(set_mana * -100)).append("%\n");
        if (set_squirrel_drop > 1) {
            sb.append("Set Squirrel reward = ").append(df2.format(set_squirrel_drop * 100 - 100)).append("%");
            sb.append("\n");
            sb.append("Squirrel spawn ").append(df2.format(set_squirrel_rate)).append("(")
                    .append(Math.round(set_squirrel_rate)).append(")").append("\n");
        }
        if (finke_bonus > 0) sb.append("Tsury Finke bonus = ").append(df2.format(finke_bonus * 100)).append("%\n");
        class CoreInfo {
            double rp;
            double bonus;
            double removal;
            CoreInfo(double rp, double bonus, double removal) {
                this.rp = rp;
                this.bonus = bonus;
                this.removal = removal;
            }
        }
        HashMap<String, CoreInfo> coreStats = new HashMap<>();
        double cost_mult = 1 - 0.01 * research_lvls.getOrDefault("Core removal cost", 0.0);
        for (int id : cores.keySet()) {
            Core core = cores.get(id);
            if (core.enabled && !core.name.equals("None")) {
                double rp = core.calcRpWorth();
                double bonus = core.getBonus(this, id);
                double removal = core.calcRemovalCost() * cost_mult;
                if (coreStats.containsKey(core.name)) {
                    coreStats.get(core.name).rp += rp;
                    coreStats.get(core.name).bonus += bonus;
                    coreStats.get(core.name).removal += removal;
                } else {
                    coreStats.put(core.name, new CoreInfo(rp, bonus, removal));
                }
            }
        }
        if (!coreStats.isEmpty()) {
            sb.append("\nCores:\n");
            for (String core : coreStats.keySet()) {
                sb.append(core).append(":").append(getBonusType(core, coreStats.get(core).bonus));
                if (coreStats.get(core).rp > 0) {
                    sb.append(", RP ").append((int) (coreStats.get(core).rp / 1000)).append("K");
                }
                sb.append(", removal ").append((int) (coreStats.get(core).removal)).append("\n");
            }
        }
        return sb.toString();
    }

    public void increment_exp(double exp) {
        cl_exp += exp;
        ml_exp += exp;
        double need_cl = exp_to_cl(cl);
        double need_ml = exp_to_ml(ml);
        while (cl_exp >= need_cl && cl < getMaxCl()) {
            cl += 1;
            cl_exp -= need_cl;
            setCLML(cl, ml);
            if (cl == cl_for_milestone()) {
                milestone_exp_mult += bonus_for_milestone();
            }
            need_cl = exp_to_cl(cl);
        }
        while (ml_exp >= need_ml) {
            ml += 1;
            ml_exp -= need_ml;
            setCLML(cl, ml);
            need_ml = exp_to_ml(ml);
        }
    }

    public void disableAllActives() {
        active_skills.forEach((key, value) -> value.enabled = false);
    }

    public void levelActives() {
        boolean uses_potion = false;
        for (ActiveSkill a : active_skills.values()) {
            if (a.enabled) {
                if (SkillData.potion_skills.contains(a.name)) {
                    uses_potion = true;
                } else {
                    a.gainExp(skill_exp_mult);
                }
            }
        }
        if (uses_potion) {
            active_skills.get("Throw Potion").gainExp(skill_exp_mult);
        }
    }

    public void levelTF(Enemy e) {
        if (passives.get("Tsury Finke").available && passives.get("Weapon Mastery").enabled) {
            if (equipment.get("MH") != null && equipment.get("MH").name.equals("Tsury Finke")) {
                if (!e.name.equals("Squirrel Mage")) {
                    passives.get("Tsury Finke").gainExpTF(e.base_lvl / 10);
                }
            }
        }
    }

    public void levelPassives(double time) {
        passives.forEach((key, value) -> {
            if (value.enabled) value.gainExp(time * skill_exp_mult);
        });
    }

    public PassiveSkill getLowestCrafting() {
        PassiveSkill c = passives.get("Crafting");
        PassiveSkill s = passives.get("Smithing");
        PassiveSkill a = passives.get("Alchemy");
        PassiveSkill result = crafting_progress ? c : null;
        if (result == null || (s.getLvl() < result.getLvl() && smithing_progress)) result = s;
        if (a.getLvl() < result.getLvl() && alchemy_progress) result = a;
        return result;
    }

    public void tick_research(double time) {
        int max_slots = research_lvls.getOrDefault("Research slot", 1.0).intValue();
        double r_spd = 1 + 0.01 * research_lvls.getOrDefault("Research spd", 0.0).intValue();
        r_spd *= 1 + r_spd_bonus;
        int can_sustain = Math.min(max_slots, calc_max_research_slots(rp_balance / time * 3600 / r_spd));
        can_sustain = Math.min(can_sustain, (int) rp_balance / 10); //to make sure we don't swing from 0 to max slots
        StringBuilder sb = new StringBuilder();
        int using = 0;
        if (game_version >= 1566) {
            if (max_slots < maxResearchLvl("Research slot") && research_weight.getOrDefault("Research slot", 0.0) > 0) {
                if (rp_balance > (research_slots_base_cost(max_slots + 1) - research_slots_base_cost(can_sustain)) * research_weight.get("Research slot")) {
                    research("Research slot", time * r_spd);
                    using++;
                    if (log.contains("research")) sb.append("Research slot; ");
                }
            }
            if (max_slots < maxResearchLvl("Research slot") && research_weight.getOrDefault("Research slot", 0.0) < 0) {
                if (max_slots < -1 * research_weight.getOrDefault("Research slot", 0.0).intValue()) {
                    research("Research slot", time * r_spd);
                    using++;
                    if (log.contains("research")) sb.append("Research slot; ");
                }
            }
        } else {
            int slot_w = research_weight.getOrDefault("Research slot", 0.0).intValue();
            if (max_slots < maxResearchLvl("Research slot") && slot_w < 0) {
                if (max_slots == -1 * slot_w) {
                    double cost = research_time("Research slot") / 3600 / 24 * 10000;
                    if (rp_balance > cost + 1000) {
                        rp_balance -= cost;
                        research_weight.put("Research slot", slot_w - 1.0);
                    }
                }
                if (max_slots < -1 * research_weight.getOrDefault("Research slot", 0.0).intValue()) {

                    research("Research slot", time * r_spd);
                    rp_balance += 250 * time / 3600 * r_spd; //since we're subtracting it later
                    using++;
                    if (log.contains("research")) sb.append("Research slot; ");
                }
            }
        }

        for (Map.Entry<String, Double> entry : research_weight_sorted.entrySet()) {
            if (entry.getValue() > 0 && can_sustain > using && (!entry.getKey().equals("Research spd") || rp_balance > 1000)) {
                if (log.contains("research")) sb.append(entry.getKey()).append("; ");
                research(entry.getKey(), time * r_spd);
                using++;
            }
        }
        rp_balance -= research_slots_base_cost(using) * time / 3600 * r_spd;
        research_slots_stat += using * time;
        rp_drain += research_slots_base_cost(using) * time / 3600 * r_spd;
        if (!sb.toString().equals(previous_research)) {
            previous_research = sb.toString();
            if (log.contains("research")) System.out.println(sb);
        }
        if (decide_research) {
            sortResearchWeights();
            decide_research = false;
        }
    }

    public void sortResearchWeights() {
        research_weight_sorted.clear();
        HashMap<String, Double> temp = new HashMap<>(32);
        for (Map.Entry<String, Double> entry : research_weight.entrySet()) {
            if (!entry.getKey().equals("Research slot")) {
                if (entry.getValue() > 0) {
                    if (research_lvls.getOrDefault(entry.getKey(), 0.0).intValue() < maxResearchLvl(entry.getKey())) {
                        temp.put(entry.getKey(), entry.getValue() * 36000 / research_time(entry.getKey()));
                    }
                }
                if (entry.getValue() < 0) {
                    if (research_lvls.getOrDefault(entry.getKey(), 0.0).intValue() < entry.getValue() * -1) {
                        temp.put(entry.getKey(), 3600000 / research_time(entry.getKey()));
                    }
                }
            }
        }
        research_weight_sorted = temp.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public void research(String name, double time) {
        int old_lvl = research_lvls.getOrDefault(name, 0.0).intValue();
        int new_lvl = (int) (research_lvls.getOrDefault(name, 0.0) + time / research_time(name));
        if (name.equals("Research slot")) {
            if (new_lvl > old_lvl && research_weight.getOrDefault(name, 0.0) > 0) {
                research_weight.put(name, research_weight.getOrDefault(name, 0.0) * 3); //we don't want to chain slots
            }
        }
        if (new_lvl > old_lvl) {
            decide_research = true;
            apply_research_effects();
//            System.out.println(name + " new_lvl=" + new_lvl);
        }
        research_lvls.put(name, research_lvls.getOrDefault(name, 0.0) + time / research_time(name));
    }

    public double research_time(String name) {
        int lvl = research_lvls.getOrDefault(name, 0.0).intValue();
        double base_time = switch(name) {
            case "Research slot" -> 259200;
            case "Research spd" -> 10800;
            case "Max skill lvl" -> 32400;
            case "Min pow" -> 7200;
            case "Enemy Min lvl" -> 18000;
            case "Reduce CL req" -> 36000;
            case "Crit chance" -> 28800;
            case "Crit damage" -> 14400;
            case "No overkill crit" -> 28800;
            case "Max CL" -> 14400;
            case "Exp gain" -> 14400;
            case "Core drop" -> 14400;
            case "Core quality" -> 1800;
            case "Skill exp" -> 3600;
            case "Sidecraft spd" -> 36000;
            case "Crafting spd" -> 14400;
            case "Alchemy spd" -> 14400;
            case "Smithing spd" -> 14400;
            case "Crafting exp" -> 7200;
            case "Alchemy exp" -> 7200;
            case "Smithing exp" -> 7200;
            case "E. Quality mult" -> 21600;
            case "E. Quality min" -> 21600;
            case "Drop rate" -> 14400;
            case "Equip HP" -> 7200;
            case "Equip Atk" -> 14400;
            case "Equip Def" -> 3600;
            case "Equip Int" -> 18000;
            case "Equip Res" -> 7200;
            case "Equip Hit" -> 7200;
            case "Equip Spd" -> 14400;
            case "God HP" -> 3600;
            case "God Atk" -> 3600;
            case "God Mystic" -> 3600;
            case "God Pet" -> 14400;
            case "God BS" -> 36000;
            case "God CS" -> 54000;
            case "God MS" -> 54000;
            case "God SD" -> 54000;
            case "God MV" -> 28800;
            case "Core removal cost" -> 7200;
            default -> 0;
        };
        if (name.equals("Research slot")) {
            return base_time * (Math.pow(lvl, 2) - Math.pow(lvl - 1, 2));
        }
        return base_time * (lvl + 1);
    }

    public int calc_max_research_slots(double base_rph) {
        for (int i = 15; i > 0; i--) {
            if (base_rph >= research_slots_base_cost(i)) return i;
        }
        return 0;
    }

    public double research_slots_base_cost(int slots) {
        if (slots < 1) return 0;
        if (game_version >= 1667) return (Math.pow(1.48, slots - 1) + slots - 2) * 180;
        return (Math.pow(1.4, slots - 1) + slots - 1) * 180;
    }

    public void apply_research_effects() {
        total_exp_mult = exp_mult * (1 + 0.01 * research_lvls.getOrDefault("Exp gain", 0.0).intValue());
        base_crit_chance = research_lvls.getOrDefault("Crit chance", 0.0).intValue()/100.0;
        base_crit_damage = 1.5 + research_lvls.getOrDefault("Crit damage", 0.0).intValue()/100.0;
        no_overkill_crit = research_lvls.getOrDefault("No overkill crit", 0.0).intValue()/100.0;
        max_skill_lvl = 20 + research_lvls.getOrDefault("Max skill lvl", 0.0).intValue();
        dmg_range = research_lvls.getOrDefault("Min pow", 0.0).intValue()/100.0;
        enemy_min_lvl = research_lvls.getOrDefault("Enemy Min lvl", 0.0).intValue();
        core_drop_research = research_lvls.getOrDefault("Core drop", 0.0).intValue()/100.0;
        core_quality_research = research_lvls.getOrDefault("Core quality", 0.0).intValue()/100.0;
        skill_exp_mult = 1 + research_lvls.getOrDefault("Skill exp", 0.0).intValue()/100.0;
    }

    public int getEnemyMinLvl() {
        return enemy_min_lvl_enabled ? enemy_min_lvl : 0;
    }

    public double exp_to_cl(int lvl) {
        double tier_mult = Math.pow(2, Math.min(tier, 3) - 1);
        if (tier >= 4) {
            tier_mult *= 1 + 0.5 * cl;
        }
        return (Math.pow(lvl, 5) / 340 + Math.pow(lvl, 2) * 50 + 10) * tier_mult;
    }

    public double exp_to_ml(int lvl) {
        return (Math.pow(lvl, 4) / 10 + Math.pow(lvl, 1.9) * 40 + 10);
    }

    public double bonus_for_milestone() {
        if (name.equals("Alchemist")) return 0.025;
        return switch (tier) {
            case 0 -> 0.025;
            case 1 -> 0.05;
            case 2 -> 0.075;
            case 3 -> 0.1;
            default -> 0;
        };
    }

    public int cl_for_milestone() {
        return switch (tier) {
            case 0 -> 10;
            case 1 -> 35;
            case 2 -> 55;
            case 3 -> name.equals("Onion Knight") ? 99 : 90;
            default -> 0;
        };
    }

    public String getWeakAttackData(int simulations) {
        StringBuilder sb = new StringBuilder();
        if (weak_a.used > 0) {
            sb.append(weak_a.getWeakRecordedData(simulations));
        }
        if (weak_i.used > 0) {
            sb.append(weak_i.getWeakRecordedData(simulations));
        }
        return sb.toString();
    }

    public double getCLpercent() {
        if (cl >= getMaxCl()) return 0;
        return cl_exp / exp_to_cl(cl) * 100;
    }

    public double getMLpercent() {
        return ml_exp / exp_to_ml(ml) * 100;
    }

    public int getMaxCl() {
        return 120 + Math.max(0, research_lvls.getOrDefault("Max CL", 0.0).intValue());
    }

    public double getNextPotionTime() {
        double time = 0;
        for (int i = 0; i < 3; i++) {
            if (potions[i] != null && potions[i].cooldown > 0) {
                time = minIfNotZero(time, potions[i].cooldown);
            }
        }
        return time;
    }

    public double getPredictedPrepareTime() {
        double time = 0;
        if (prepare != null) {
            double defecit = getHp_max() * prepare_threshold / 100 - hp;
            if (defecit > 0) {
                time = minIfNotZero(time, defecit / getPrepare_hps());
            }
            defecit = getMp_max() * prepare_threshold / 100 - mp;
            if (defecit > 0) {
                time = minIfNotZero(time, defecit / getPrepare_mps());

            }
        }
        return time;
    }

    public double getSquirrelMult(int lvl) {
        if (game_version < 1621) return 1;
        if (lvl < 25) return 0.5 + lvl * 0.01;
        if (game_version == 1621) return 1 + Math.pow(lvl, 1.6) * set_squirrel_drop / 1000;
        double mult = 1 + Math.pow(lvl, 1.45) * set_squirrel_drop / 1000;
        if (enemy_min_lvl_enabled && game_version > 1638) {
            mult /= 1 + 0.005 * enemy_min_lvl;
        }
        return mult;
    }

    public int getBestiaryMedals(double threshold) {
        int count = 0;
        for (double value : bestiary.values()) {
            if (value > threshold) {
                count++;
            }
        }
        return count;
    }

    public double getBestiaryBonus(String name) {
        double bonus = bestiary.getOrDefault(name, 0.0) / 500000.0;
        return Math.min(bonus, 0.1);
    }

    public double getExpMult() {
        double result = total_exp_mult * milestone_exp_mult * core_exp;
        result *= 1 + 0.01 * getBestiaryMedals(50000);
        return  result;
    }

    public void calcConsumables(double time) {
        double craft_spd = 1 + 0.01 * passives.get("Crafting").lvl;
        craft_spd *= (1 + 0.01 * research_lvls.getOrDefault("Crafting spd", 0.0).intValue());
        double alch_spd = 1 + 0.01 * passives.get("Alchemy").lvl;
        alch_spd *= (1 + 0.01 * research_lvls.getOrDefault("Alchemy spd", 0.0).intValue());
        if (alchemist_lvl >= 90) {
            alch_spd *= 1 + 0.0000125 * Math.pow(alchemist_lvl, 2);
        }
        double side_craft_spd = getSidecraftingSpeed();
        double craft_time = 0;
        double alchemy_time = 0;
        for (int i = 0; i < 3; i++) {
            if (potions[i] != null && potions[i].count < 0) {
                int need_to_craft = potions[i].count * -1;
                double t1 = potions[i].base_craft_time() * need_to_craft / craft_spd;
                double t2 = potions[i].base_alch_time() * need_to_craft / alch_spd;
                craft_time += t1;
                alchemy_time += t2;
                if (side_craft_spd > 0) {
                    potion_sidecraft_time += (t1 + t2) / side_craft_spd;
                } else {
                    maincrafting_time += t1 + t2;
                }
                potions[i].count = 0;
            }
        }
        if (pill != null) {
            double t2 = pill.getBaseCraftTime() * time / 7200.0 / alch_spd;
            alchemy_time += t2;
            if (side_craft_spd > 0) {
                pill_sidecraft_time += t2 / side_craft_spd;
            } else {
                maincrafting_time += t2;
            }
        }
        if (potions_thrown > 0) {
            double t1 = flask_used * 6.0 / craft_spd;
            double t2 = potions_thrown * 7.5 / alch_spd;
            if (side_craft_spd > 0) {
                combat_potion_sidecraft_time += (t1 + t2) / side_craft_spd;
            } else {
                maincrafting_time += t1 + t2;
            }
            potions_thrown = 0;
            flask_used = 0;
        }
        if (alchemy_consumption_based) {
            if (side_craft_spd > 0) {
                passives.get("Crafting").gainCraftingExp(craft_time / side_craft_spd);
                passives.get("Alchemy").gainCraftingExp(alchemy_time / side_craft_spd);
            } else {
                passives.get("Crafting").gainCraftingExp(craft_time);
                passives.get("Alchemy").gainCraftingExp(alchemy_time);
            }
        }
    }

    public double getSidecraftingSpeed() {
        double side_craft_spd = 0;
        int crafting_lvl = passives.get("Crafting").lvl;
        int smithing_lvl = passives.get("Smithing").lvl;
        int alchemy_lvl = passives.get("Alchemy").lvl;
        int min_lvl = Math.min(alchemy_lvl, Math.min(crafting_lvl, smithing_lvl));
        if (min_lvl >= 10) {
            side_craft_spd = 0.05 + 0.01 * research_lvls.getOrDefault("Sidecraft spd", 0.0).intValue();
        }
        if (min_lvl >= 20) side_craft_spd += 0.05;
        if (min_lvl >= 30) side_craft_spd += 0.1;
        if (min_lvl >= 40) side_craft_spd += 0.05;
        if (min_lvl >= 50) side_craft_spd += 0.05;
        if (min_lvl >= 60) side_craft_spd += 0.05;
        if (min_lvl >= 70) side_craft_spd += 0.05;
        if (min_lvl >= 80) side_craft_spd += 0.05;
        if (min_lvl >= 90) side_craft_spd += 0.05;
        return side_craft_spd;
    }

    public void enableSkills(String name) {
        switch (name) {
            case "Newbie" -> {
                tier = 1;
                skills.enableActive("Basic Attack");
                skills.enableActive("First Aid");
                skills.enablePassive("Exp Boost");
                skills.makeVisible("Potion Inventor");
                skills.makeVisible("Pill Inventor");
                skills.makeVisible("Toughness pill");
                skills.makeVisible("Speedy pill");
                skills.makeVisible("Berserk pill");
                skills.makeVisible("Wise pill");
                skills.makeVisible("Critical pill");
                skills.makeVisible("Ultimate pill");
                skills.makeVisible("Crafting");
                skills.makeVisible("Smithing");
                skills.makeVisible("Alchemy");
            }
            case "Adventurer" -> {
                enableSkills("Newbie");
                tier = 1;
                skills.enablePassive("Drop Boost");
                skills.enablePassive("Speed Boost");
                skills.enableActive("Bash");
                skills.enableActive("Prepare");
            }
            case "Squire" -> {
                enableSkills("Newbie");
                tier = 1;
                skills.enablePassive("Attack Boost");
                skills.enablePassive("Defense Boost");
                skills.enableActive("Attack");
                skills.enableActive("Quick Hit");
            }
            case "Student" -> {
                enableSkills("Newbie");
                tier = 1;
                skills.enablePassive("Int Boost");
                skills.enablePassive("Res Boost");
                skills.enableActive("Magic Arrow");
            }
            case "Thief" -> {
                enableSkills("Adventurer");
                tier = 2;
                skills.enablePassive("Dagger Mastery");
                skills.enablePassive("Dodge");
                skills.enableActive("Double Attack");
                skills.enableActive("Steal");
                skills.enableActive("Hide");
            }
            case "Archer" -> {
                enableSkills("Adventurer");
                tier = 2;
                skills.enablePassive("Bow Mastery");
                skills.enablePassive("Ambush");
                skills.enableActive("Double Shot");
                skills.enableActive("Arrow Rain");
                skills.enableActive("Trap");
            }
            case "Warrior" -> {
                enableSkills("Squire");
                tier = 2;
                skills.enablePassive("Sword Mastery");
                skills.enablePassive("HP Regen");
                skills.enableActive("Aura Blade");
                skills.enableActive("Defense Break");
                skills.enableActive("Sword Rush");
            }
            case "Fighter" -> {
                enableSkills("Squire");
                tier = 2;
                skills.enablePassive("Fist Mastery");
                skills.enablePassive("Counter Strike");
                skills.enableActive("Aura Shot");
                skills.enableActive("Dragon Punch");
                skills.enableActive("Whirling Foot");
            }
            case "Mage" -> {
                enableSkills("Student");
                tier = 2;
                skills.enablePassive("Casting Boost");
                skills.enablePassive("Wand Mastery");
                skills.enableActive("Elemental Blast");
                skills.enableActive("Push Blast");
                skills.enableActive("Magic Missile");
            }
            case "Cleric" -> {
                enableSkills("Student");
                tier = 2;
                skills.enablePassive("Book Mastery");
                skills.enablePassive("Ailment Res");
                skills.enableActive("Holy Light");
                skills.enableActive("Heal");
                skills.enableActive("Bless");
            }
            case "Sniper" -> {
                enableSkills("Archer");
                enableSkills("Warrior");
                tier = 3;
                skills.enablePassive("Concentration");
                skills.enablePassive("Hit Boost");
                skills.enableActive("Sharp Shooting");
                skills.enableActive("Mark Target");
                skills.enableActive("Charge Up");
            }
            case "Hunter" -> {
                enableSkills("Archer");
                tier = 3;
                skills.enablePassive("Multi Arrows");
                skills.enablePassive("Core Boost");
                skills.enableActive("Careful Shot");
                skills.enableActive("Weakening Shot");
                skills.enableActive("Aimed Shot");
            }
            case "Rogue" -> {
                enableSkills("Thief");
                enableSkills("Archer");
                tier = 3;
                skills.enablePassive("Extra Attack");
                skills.enablePassive("Dual Wield");
                skills.enableActive("Throw Sand");
                skills.enableActive("Binding Shot");
                skills.enableActive("Back Stab");
            }
            case "Assassin" -> {
                enableSkills("Fighter");
                enableSkills("Thief");
                tier = 3;
                skills.enablePassive("Stealth");
                skills.enablePassive("Poison Boost");
                skills.enableActive("Killing Strike");
                skills.enableActive("Poison Attack");
                skills.enableActive("Smoke Screen");
            }
            case "Monk" -> {
                enableSkills("Cleric");
                enableSkills("Fighter");
                tier = 3;
                skills.enablePassive("Might Aura");
                skills.enablePassive("Speed Aura");
                skills.enablePassive("Fiery Aura");
                skills.enableActive("Chain Combo");
                skills.enableActive("Asura Strike");
            }
            case "Knight" -> {
                enableSkills("Warrior");
                enableSkills("Fighter");
                tier = 3;
                skills.enablePassive("Spear Mastery");
                skills.enablePassive("HP Boost");
                skills.enableActive("Empower HP");
                skills.enableActive("Pierce");
                skills.enableActive("Rapid Stabs");
            }
            case "Pyromancer" -> {
                enableSkills("Mage");
                tier = 3;
                skills.enablePassive("Fire Boost");
                skills.enablePassive("Fire Resistance");
                skills.enableActive("Fire Pillar");
                skills.enableActive("Fire Ball");
                skills.enableActive("Explosion");
            }
            case "Geomancer" -> {
                enableSkills("Mage");
                tier = 3;
                skills.enablePassive("Earth Boost");
                skills.enablePassive("Earth Resistance");
                skills.enableActive("Rock Shot");
                skills.enableActive("Stone Barrier");
                skills.enableActive("Earth Quake");
            }
            case "Priest" -> {
                enableSkills("Cleric");
                tier = 3;
                skills.enablePassive("Light Boost");
                skills.enablePassive("Bless Mastery");
                skills.enableActive("Holy Ray");
                skills.enableActive("Dispel");
                skills.enableActive("Prayer");
            }
            case "Scholar" -> {
                enableSkills("Adventurer");
                enableSkills("Mage");
                tier = 3;
                skills.enableActive("Analyze");
                skills.enableActive("Taking Notes");
                skills.enablePassive("Safe Distance");
                skills.enablePassive("Speedy Analyze");
                skills.enablePassive("Smart Analyze");
            }
            case "Alchemist" -> {
                enableSkills("Adventurer");
                tier = 3;
                skills.enablePassive("Potion Inventor");
                skills.enablePassive("Potion Slots");
                skills.enablePassive("Pill Inventor");
                skills.enableActive("Throw Potion");
                skills.makeUnavailable("Throw Potion");
                skills.enableActive("Throw Burning");
                skills.enableActive("Throw Freezing");
                skills.enableActive("Throw Geo");
                skills.enableActive("Throw Acid");
                skills.enableActive("Throw Black");
                skills.enableActive("Throw Luminary");
                skills.makeInvisible("Throw Burning");
                skills.makeInvisible("Throw Freezing");
                skills.makeInvisible("Throw Geo");
                skills.makeInvisible("Throw Acid");
                skills.makeInvisible("Throw Black");
                skills.makeInvisible("Throw Luminary");
                skills.enableActive("Alchemic Reaction");
            }
            case "Onion Knight" -> {
                enableSkills("Newbie");
                tier = 3;
                skills.enablePassive("Attack Boost");
                skills.enablePassive("Weapon Mastery");
                skills.enablePassive("Tsury Finke");
                skills.enablePassive("Water Boost");
                skills.enableActive("Onion Slash");
                skills.enableActive("Onion Wave");
            }
            case "Ninja" -> {
                enableSkills("Assassin");
                enableSkills("Rogue");
                tier = 4;
                skills.enableActive("Poisoned Dagger");
                skills.enableActive("Poisoned Kunai");
//                skills.enableActive("Poison Burst?");
                skills.enablePassive("Critical Poison");
                skills.enablePassive("Killing Intent");
            }
            case "Holy Archer" -> {
                enableSkills("Priest");
                enableSkills("Sniper");
                tier = 4;
                skills.enableActive("Kyrie Eleyson");
                skills.enableActive("Celestial Arrow");
                skills.enableActive("Celestial Ray");
                skills.enablePassive("True Sight");
                skills.enablePassive("Divine Boost");
            }
            case "Tea Rogue" -> {
                enableSkills("Onion Knight");
                tier = 4;
            }
        }
    }
}
