package Disilon;

import java.util.Vector;

import static Disilon.Main.df2;

public class Player extends Actor {
    ActiveSkill hide = new ActiveSkill("Hide", 1, 0.3, 0, 0, 5, 0.5, 0.5, Scaling.atk, Element.none, false, false);
    ActiveSkill ks = new ActiveSkill("Killing Strike", 1, 297, 363, 0.7, 80, 2, 2, Scaling.atk, Element.dark,
            false, false);
    ActiveSkill dp = new ActiveSkill("Dragon Punch", 3, 76.5, 93.5, 0.8, 20, 1, 3, Scaling.atk, Element.phys,
            false, false);
    ActiveSkill wf = new ActiveSkill("Whirling Foot", 3, 49.5, 60.5, 0.8, 30, 1.2, 2, Scaling.atk, Element.phys,
            true, false);
    ActiveSkill pa = new ActiveSkill("Poison Attack", 1, 36, 44, 1, 4, 0.4, 0.9, Scaling.atk, Element.phys, false
            , false);
    ActiveSkill pa1541 = new ActiveSkill("Poison Attack", 1, 36, 44, 1, 4, 0.4, 0.9, Scaling.atk, Element.phys, false
            , false);
    ActiveSkill smoke = new ActiveSkill("Smoke Screen", 1, 0, 0, 0.85, 25, 0.8, 1, Scaling.atk, Element.none, true, false);
    ActiveSkill fa = new ActiveSkill("First Aid", 1, 15, 15, 0, 5, 0.9, 1.1, Scaling.atk, Element.none, false,
            true);
    ActiveSkill ss = new ActiveSkill("Sharp Shooting", 1, 207, 253, 1.5, 80, 2, 3, Scaling.atkhit, Element.wind,
            false, false);
    ActiveSkill ds = new ActiveSkill("Double Shot", 2, 76.5, 93.5, 1, 12, 1.1, 1.1, Scaling.atkhit, Element.phys,
            false, false);
    ActiveSkill ar = new ActiveSkill("Arrow Rain", 5, 49.5, 60.5, 0.7, 20, 1.5, 1.5,
            Scaling.atkhit, Element.phys,false,false);
    ActiveSkill ar1535 = new ActiveSkill("Arrow Rain", 5, 49.5, 60.5, 0.7, 25, 1.5,
            1.5,Scaling.atkhit, Element.phys,false,false);
    ActiveSkill bash = new ActiveSkill("Bash", 1, 103.5, 126.5, 1, 10, 1.2, 1.1, Scaling.atk, Element.phys, false,
            false);
    ActiveSkill db = new ActiveSkill("Defense Break", 1, 90, 110, 1, 10, 1, 1, Scaling.atk, Element.phys, false,
            false);
    ActiveSkill qh = new ActiveSkill("Quick Hit", 1, 76.5, 93.5, 1, 10, 0.7, 0.7, Scaling.atk, Element.phys, false,
            false);
    ActiveSkill ab = new ActiveSkill("Aura Blade", 1, 121.5, 148.5, 1, 15, 1.3, 1.3, Scaling.atk, Element.light,
            false,false);
    ActiveSkill sr = new ActiveSkill("Sword Rush", 3, 67.5, 82.5, 1, 15, 1.5, 1.5, Scaling.atk, Element.phys,
            false, false);
    ActiveSkill mark = new ActiveSkill("Mark Target", 1, 0, 0, 1.5, 10, 0.5, 0.5, Scaling.atk, Element.none,
            false,false);
    ActiveSkill charge_up = new ActiveSkill("Charge Up", 1, 0, 0, 0, 50, 2, 2, Scaling.atk, Element.none, false,
            false);
    ActiveSkill fball = new ActiveSkill("Fire Ball", 1, 99, 121, 1.35, 20, 1.15, 1, Scaling.intel, Element.fire,
            false, false);
    ActiveSkill fpillar = new ActiveSkill("Fire Pillar", 1, 180, 220, 1.0, 36, 1.5,
            1.5,
            Scaling.intel,
            Element.fire, false, false);
    ActiveSkill fpillar1537 = new ActiveSkill("Fire Pillar", 1, 180, 220, 1.0, 50, 1.5,
            1.5,
            Scaling.intel,
            Element.fire, false, false);
    ActiveSkill explosion = new ActiveSkill("Explosion", 1, 1350, 1650, 1.15, 500, 8, 30, Scaling.intel,
            Element.fire, true, false);
    ActiveSkill eblast = new ActiveSkill("Elemental Blast", 1, 117, 143, 1.0, 20, 1.2, 1.2, Scaling.intel,
            Element.eleblast, false, false);
    ActiveSkill mm = new ActiveSkill("Magic Missile", 1, 148.5, 181.5, 1.5, 25, 1.5, 1.5, Scaling.intel,
            Element.magic, false, false);
    ActiveSkill ma = new ActiveSkill("Magic Arrow", 1, 90, 110, 1, 15, 1, 1, Scaling.intel,
            Element.magic, false, false);
    ActiveSkill hlight = new ActiveSkill("Holy Light", 1, 112.5, 137.5, 1.0, 25, 1, 1.1, Scaling.resint,
            Element.light, false, false);
    ActiveSkill heal = new ActiveSkill("Heal", 1, 250, 45, 0, 30, 0.8, 1.5, Scaling.atk, Element.none, false,
            true);
    ActiveSkill bless = new ActiveSkill("Bless", 1, 0, 0, 0, 40, 0.5, 0.5, Scaling.atk, Element.none, false,
            false);
    ActiveSkill push = new ActiveSkill("Push Blast", 1, 99, 121, 0.9, 30, 1.3, 1.3, Scaling.intel,
            Element.magic, true, false);
    ActiveSkill doubleattack = new ActiveSkill("Double Attack", 2, 64.8, 79.2, 1, 7, 1, 1, Scaling.atk,
            Element.phys, false, false);
    ActiveSkill empowerhp = new ActiveSkill("Empower HP", 1, 0, 0, 0, 60, 2, 2, Scaling.atk, Element.none, false,
            false);
    ActiveSkill rapidstabs = new ActiveSkill("Rapid Stabs", 5, 67.5, 82.5, 0.9, 100, 1.7, 1,
            Scaling.atk, Element.phys,false,false);
    ActiveSkill pierce1551 = new ActiveSkill("Pierce", 1, 45, 55, 1, 25, 0.75, 1,
            Scaling.atk, Element.phys,false,false);
    ActiveSkill pierce1552 = new ActiveSkill("Pierce", 1, 67.5, 82.5, 1, 40, 1.2, 1.2,
            Scaling.atk, Element.phys,false,false);
    ActiveSkill careful = new ActiveSkill("Careful Shot", 1, 54, 66, 1.5, 15, 0.6, 0.6, Scaling.atkhit,
            Element.phys, false, false);
    ActiveSkill weakening = new ActiveSkill("Weakening Shot", 1, 112.5, 137.5, 1, 50, 1.3, 1.3, Scaling.atkhit,
            Element.phys, false, false);
    ActiveSkill aimed = new ActiveSkill("Aimed Shot", 1, 270, 330, 1.5, 75, 1.5, 1.5, Scaling.atkhit,
            Element.phys, false, false);
    ActiveSkill aimed1563 = new ActiveSkill("Aimed Shot", 1, 270, 330, 1.5, 60, 1.5, 1.5, Scaling.atkhit,
            Element.phys, false, false);

    ActiveSkill basic = new ActiveSkill("Basic Attack", 1, 81, 99, 1, 0, 1, 1, Scaling.atk,
            Element.phys, false, false);
    ActiveSkill onion_slash = new ActiveSkill("Onion Slash", 1, 252, 308, 1.25, 99, 1.4, 1.4, Scaling.atk,
            Element.water, false, false);
    ActiveSkill onion_wave = new ActiveSkill("Onion Wave", 1, 299.7, 366.3, 0.99, 333, 2.9, 5, Scaling.atk,
            Element.water, true, false);
    ActiveSkill prep = new ActiveSkill("Prepare");

    public static String[] availableClasses = {"Sniper", "Assassin", "Pyromancer", "Knight", "Hunter", "Onion Knight", "Cleric", "Mage", "Fighter",
            "Warrior", "Archer", "Student", "Thief"};

    public Player() {
        addSkillEffects();
        initializeSets();
    }

    public Player(Setup setup) {
        this();
        this.setClass(setup.playerclass);
        this.setCLML(setup.cl, setup.ml);
        this.old_cl = setup.cl;
        this.old_ml = setup.ml;
        this.lvling = setup.leveling;
        this.zone = setup.zone;
        this.zone.stats.clear_recorded_data();
        this.setupPotions(setup.potion1, setup.potion1_t, setup.potion2, setup.potion2_t, setup.potion3, setup.potion3_t);
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
        this.milestone_exp_mult = setup.milestone / 100;
        this.old_milestone_exp_mult = this.milestone_exp_mult;
        this.disableAllActives();
        for (String skill : setup.passives_lvls.keySet()) {
            if (passives.containsKey(skill)) {
                passives.get(skill).setLvl(setup.passives_lvls.get(skill));
                passives.get(skill).old_lvl = passives.get(skill).lvl;
            }
        }
        for (String skill : setup.actives_lvls.keySet()) {
            if (active_skills.containsKey(skill)) {
                active_skills.get(skill).setLvl(setup.actives_lvls.get(skill));
                active_skills.get(skill).old_lvl = active_skills.get(skill).lvl;
            }
        }
        this.skill1 = getSkill(setup.skill1, setup.skill1_s);
        if (this.skill1 != null) {
            this.skill1.setSkill(setup.skill1_mod);
        }
        this.skill2 = getSkill(setup.skill2, setup.skill2_s);
        if (this.skill2 != null) {
            this.skill2.setSkill(setup.skill2_mod);
        }
        this.skill3 = getSkill(setup.skill3, setup.skill3_s);
        if (this.skill3 != null) {
            this.skill3.setSkill(setup.skill3_mod);
        }
        this.enablePassives(new String[]{setup.pskill1, setup.pskill2, setup.pskill3, setup.pskill4});
    }

    public void addSkillEffects() {
        pa.addDebuff("Poison", 3, 0.1);
        pa1541.addDebuff("Poison", 3, 0.15);
        smoke.addDebuff("Smoke", 3, 0);
        bash.addDebuff("Defense Break", 3, 0.2);
        db.addDebuff("Defense Break", 3, 0.25);
        mm.addDebuff("Resist Break", 3, 0.25);
        mark.addDebuff("Mark", 1, 0.2);
        charge_up.addBuff("Charge Up", 1, 1.5);
        fball.addDebuff("Burn", 3, 1);
        fpillar.addDebuff("Burn", 3, 1);
        fpillar1537.addDebuff("Burn", 3, 1);
        explosion.addDebuff("Burn", 3, 1);
        bless.addBuff("Bless", 2, 0.3);
        empowerhp.addBuff("Empower HP", 7, 0.05);
        weakening.addDebuff("Weaken", 4, 0.25);
        ar.random_targets = true;
        careful.overkill = false;
        aimed.can_kill = false;
    }

    public void initializeSets() {
        sets.put("Cloth", new EquipmentSet("magicdmg", 5));
        sets.put("Leather", new EquipmentSet("hit", 5));
        sets.put("Dark", new EquipmentSet("physdmg", 5));
        sets.put("Metal", new EquipmentSet("mit1", 5));
        sets.put("Iron", new EquipmentSet("mit2", 5));
        sets.put("BronzeAcc", new EquipmentSet("mit1", 3));
    }

    public void setupPotions(String type1, int threshold1, String type2, int threshold2,
                             String type3, int threshold3) {
        if (!type1.equals("None")) {
            potion1 = new Potion(type1, threshold1);
        } else {
            potion1 = null;
        }
        if (!type2.equals("None")) {
            potion2 = new Potion(type2, threshold2);
        } else {
            potion2 = null;
        }
        if (!type3.equals("None")) {
            potion3 = new Potion(type3, threshold3);
        } else {
            potion3 = null;
        }
    }

    public void checkPotion(double delta) {
        if (potion1 != null) potion1.checkPotion(this, delta);
        if (potion2 != null) potion2.checkPotion(this, delta);
        if (potion3 != null) potion3.checkPotion(this, delta);
    }

    public void tickPotion(double delta) {
        if (potion1 != null) potion1.tickPotion(delta);
        if (potion2 != null) potion2.tickPotion(delta);
        if (potion3 != null) potion3.tickPotion(delta);
    }

    public void resetPotionCd() {
        if (potion1 != null) potion1.cooldown = 0;
        if (potion2 != null) potion2.cooldown = 0;
        if (potion3 != null) potion3.cooldown = 0;
    }

    public void setClass(String name) {
        this.name = name;
        passives.clear();
        active_skills.clear();
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
                tier = 3;
                base_dark_res = 0.5;
                base_light_res = -0.5;
                passives.put("Attack Boost", attackBoost);
                passives.put("Drop Boost", dropBoost);
                passives.put("Dagger Mastery", daggerMastery);
                passives.put("Stealth", stealth);
                passives.put("Speed Boost", speedBoost);
                passives.put("Poison Boost", poisonBoost);
                passives.put("Defense Boost", defenseBoost);
                passives.put("Dodge", dodge);
                passives.put("Fist Mastery", fistMastery);
                active_skills.put("Killing Strike", ks);
                active_skills.put("Hide", hide);
                active_skills.put("Dragon Punch", dp);
                active_skills.put("Whirling Foot", wf);
                if (Main.game_version < 1541) {
                    active_skills.put("Poison Attack", pa);
                } else {
                    active_skills.put("Poison Attack", pa1541);
                }
                active_skills.put("Smoke Screen", smoke);
                active_skills.put("First Aid", fa);
                active_skills.put("Prepare", prep);
            }
            case "Fighter" -> {
                tier = 2;
                passives.put("Attack Boost", attackBoost);
                passives.put("Defense Boost", defenseBoost);
                passives.put("Fist Mastery", fistMastery);
                active_skills.put("Quick Hit", qh);
                active_skills.put("Dragon Punch", dp);
                active_skills.put("Whirling Foot", wf);
                active_skills.put("First Aid", fa);
            }
            case "Pyromancer" -> {
                tier = 3;
                base_fire_res = 0.5;
                base_water_res = -0.5;
                passives.put("Int Boost", intBoost);
                passives.put("Res Boost", resBoost);
                passives.put("Wand Mastery", wandMastery);
                passives.put("Casting Boost", castBoost);
                passives.put("Fire Boost", fireBoost);
                passives.put("Fire Resistance", fireResist);
                if (Main.game_version >= 1537) {
                    active_skills.put("Fire Pillar", fpillar1537);
                } else {
                    active_skills.put("Fire Pillar", fpillar);
                }
                active_skills.put("Fireball", fball);
                active_skills.put("Explosion", explosion);
                active_skills.put("Elemental Blast", eblast);
                active_skills.put("Push Blast", push);
                active_skills.put("First Aid", fa);
            }
            case "Mage" -> {
                tier = 2;
                passives.put("Int Boost", intBoost);
                passives.put("Res Boost", resBoost);
                passives.put("Wand Mastery", wandMastery);
                passives.put("Casting Boost", castBoost);
                active_skills.put("Elemental Blast", eblast);
                active_skills.put("Push Blast", push);
                active_skills.put("Magic Arrow", ma);
                active_skills.put("Magic Missile", mm);
                active_skills.put("First Aid", fa);
            }
            case "Student" -> {
                tier = 1;
                passives.put("Int Boost", intBoost);
                passives.put("Res Boost", resBoost);
                active_skills.put("Magic Arrow", ma);
                active_skills.put("First Aid", fa);
            }
            case "Sniper" -> {
                tier = 3;
                base_wind_res = 0.5;
                base_fire_res = -0.5;
                passives.put("Attack Boost", attackBoost);
                passives.put("Drop Boost", dropBoost);
                passives.put("Bow Mastery", bowMastery);
                passives.put("Speed Boost", speedBoost);
                passives.put("Defense Boost", defenseBoost);
                passives.put("Ambush", ambush);
                passives.put("HP Regen", hpRegen);
                passives.put("Concentration", concentration);
                passives.put("Hit Boost", hitBoost);
                if (Main.game_version >= 1535) {
                    active_skills.put("Arrow Rain", ar1535);
                } else {
                    active_skills.put("Arrow Rain", ar);
                }
                active_skills.put("Sharpshooter", ss);
                active_skills.put("Mark", mark);
                active_skills.put("Charge Up", charge_up);
                active_skills.put("Defense Break", db);
                active_skills.put("First Aid", fa);
                active_skills.put("Prepare", prep);
                active_skills.put("Bash", bash);
                active_skills.put("Double Shot", ds);
                active_skills.put("Quick Hit", qh);
            }
            case "Archer" -> {
                tier = 2;
                passives.put("Drop Boost", dropBoost);
                passives.put("Bow Mastery", bowMastery);
                passives.put("Speed Boost", speedBoost);
                passives.put("Ambush", ambush);
                active_skills.put("Double Shot", ds);
                if (Main.game_version >= 1535) {
                    active_skills.put("Arrow Rain", ar1535);
                } else {
                    active_skills.put("Arrow Rain", ar);
                }
                active_skills.put("First Aid", fa);
                active_skills.put("Prepare", prep);
                active_skills.put("Bash", bash);
            }
            case "Knight" -> {
                tier = 3;
                base_phys_res = 0.1;
                base_fire_res = 0.1;
                base_water_res = 0.1;
                base_wind_res = 0.1;
                base_earth_res = 0.1;
                base_light_res = 0.1;
                base_dark_res = 0.1;
                base_magic_res = -0.5;
                passives.put("Attack Boost", attackBoost);
                passives.put("Defense Boost", defenseBoost);
                passives.put("HP Regen", hpRegen);
                passives.put("Sword Mastery", swordMastery);
                passives.put("Fist Mastery", fistMastery);
                passives.put("Spear Mastery", spearMastery);
                passives.put("HP Boost", hpBoost);
                active_skills.put("Empower HP", empowerhp);
                if (Main.game_version >= 1552) {
                    active_skills.put("Pierce", pierce1552);
                } else {
                    active_skills.put("Pierce", pierce1551);
                }
                active_skills.put("Rapid Stabs", rapidstabs);
                active_skills.put("Dragon Punch", dp);
                active_skills.put("Whirling Foot", wf);
                active_skills.put("Quick Hit", qh);
                active_skills.put("Aura Blade", ab);
                active_skills.put("Defense Break", db);
                active_skills.put("Sword Rush", sr);
                active_skills.put("First Aid", fa);
            }
            case "Warrior" -> {
                tier = 2;
                passives.put("Attack Boost", attackBoost);
                passives.put("Defense Boost", defenseBoost);
                passives.put("HP Regen", hpRegen);
                passives.put("Sword Mastery", swordMastery);
                active_skills.put("Quick Hit", qh);
                active_skills.put("Aura Blade", ab);
                active_skills.put("Defense Break", db);
                active_skills.put("Sword Rush", sr);
                active_skills.put("First Aid", fa);
            }
            case "Cleric" -> {
                tier = 2;
                passives.put("Int Boost", intBoost);
                passives.put("Res Boost", resBoost);
                passives.put("Book Mastery", bookMastery);
                passives.put("Ailment Res", ailmentRes);
                active_skills.put("Holy Light", hlight);
                active_skills.put("Magic Arrow", ma);
                active_skills.put("Heal", heal);
                active_skills.put("First Aid", fa);
                active_skills.put("Bless", bless);
            }
            case "Thief" -> {
                tier = 2;
                passives.put("Drop Boost", dropBoost);
                passives.put("Dagger Mastery", daggerMastery);
                passives.put("Speed Boost", speedBoost);
                passives.put("Dodge", dodge);
                active_skills.put("Bash", bash);
                active_skills.put("Double Attack", doubleattack);
                active_skills.put("Hide", hide);
                active_skills.put("Prepare", prep);
            }
            case "Hunter" -> {
                tier = 3;
                base_fire_res = 0.2;
                base_water_res = 0.2;
                base_wind_res = 0.2;
                base_earth_res = 0.2;
                passives.put("Drop Boost", dropBoost);
                passives.put("Bow Mastery", bowMastery);
                passives.put("Speed Boost", speedBoost);
                passives.put("Ambush", ambush);
                passives.put("Multi Arrows", multiArrows);
                passives.put("Core Boost", coreBoost);
                active_skills.put("Careful Shot", careful);
                active_skills.put("Weakening Shot", weakening);
                if (Main.game_version >= 1563) {
                    active_skills.put("Aimed Shot", aimed1563);
                } else {
                    active_skills.put("Aimed Shot", aimed);
                }
                active_skills.put("Double Shot", ds);
                if (Main.game_version >= 1535) {
                    active_skills.put("Arrow Rain", ar1535);
                } else {
                    active_skills.put("Arrow Rain", ar);
                }
                active_skills.put("First Aid", fa);
                active_skills.put("Prepare", prep);
                active_skills.put("Bash", bash);
            }
            case "Onion Knight" -> {
                tier = 3;
                passives.put("Weapon Mastery", weaponMastery);
                passives.put("Attack Boost", attackBoost);
                passives.put("Water Boost", waterBoost);
                active_skills.put("Basic Attack", basic);
                active_skills.put("Onion Slash", onion_slash);
                active_skills.put("Onion Wave", onion_wave);
            }
        }
    }

    public Vector<String> getAvailableActiveSkills() {
        Vector<String> v = new Vector<>(active_skills.keySet());
        v.insertElementAt("None", 0);
        return v;
    }

    public Vector<String> getAvailablePassiveSkills() {
        Vector<String> v = new Vector<>(passives.keySet());
        v.insertElementAt("None", 0);
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

    public ActiveSkill getWeakSkill() {
        if (atk > intel) {
            return weak_a;
        } else {
            return weak_i;
        }
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
            case "Mage" -> {
                base_hp_max = (double) (70 * (cl + 100)) / 10000 * 30 * ml;
                base_atk = (double) (70 * (cl + 100)) / 10000 * 4 * ml;
                base_def = (double) (70 * (cl + 100)) / 10000 * 4 * ml;
                base_int = (double) (140 * (cl + 100)) / 10000 * 4 * ml;
                base_res = (double) (120 * (cl + 100)) / 10000 * 4 * ml;
                base_hit = (double) (80 * (cl + 100)) / 10000 * 4 * ml;
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
            case "Pyromancer" -> {
                result += (getAtk() + getIntel()) * (fireBoost.enabled ? 0.5 + fireBoost.bonus : 0.5);
            }
            default -> {
            }
        }
        result += getEblast();
        return result;
    }

    @Override
    public double getWater() {
        double result = gear_water;
        switch (name) {
            case "Pyromancer" -> {
                result += (getAtk() + getIntel()) / -2;
            }
            default -> {
                result += (getAtk() + getIntel()) * (waterBoost.enabled ? waterBoost.bonus : 0);
            }
        }
        result += getEblast();
        return result;
    }

    @Override
    public double getWind() {
        double result = gear_wind;
        switch (name) {
            case "Sniper" -> {
                result += (getAtk() + getIntel()) / 2;
            }
            default -> {
            }
        }
        result += getEblast();
        return result;
    }

    @Override
    public double getEarth() {
        double result = gear_earth;
        switch (name) {
            default -> {
            }
        }
        result += getEblast();
        return result;
    }

    @Override
    public double getDark() {
        double result = gear_dark;
        switch (name) {
            case "Assassin" -> {
                result += (getAtk() + getIntel()) / 2;
            }
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
                result += (getAtk() + getIntel()) / -2;
            }
            default -> {
            }
        }
        result += (holylight_enabled ? getResist() * 0.25 : 0);
        result += (aurablade_enabled ? getAtk() * 0.1 : 0);
        return result;
    }

    public String getAllStats() {
        StringBuilder sb = new StringBuilder();
        sb.append("HP = ").append(Math.round(getHp_max())).append(" (").append(Math.round(gear_hp)).append(")\n");
        sb.append("MP = ").append(Math.round(getMp_max())).append("\n");
        sb.append("ATK = ").append(Math.round(getAtk())).append(" (").append(Math.round(gear_atk)).append(")\n");
        sb.append("DEF = ").append(Math.round(getDef())).append(" (").append(Math.round(gear_def)).append(")\n");
        sb.append("INT = ").append(Math.round(getIntel())).append(" (").append(Math.round(gear_int)).append(")\n");
        sb.append("RES = ").append(Math.round(getResist())).append(" (").append(Math.round(gear_res)).append(")\n");
        sb.append("HIT = ").append(Math.round(getHit())).append(" (").append(Math.round(getHit() - base_hit * hit_mult)).append(")\n");
        sb.append("SPD = ").append(Math.round(getSpeed())).append(" (").append(Math.round(gear_speed)).append(")\n\n");
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
        if (gear_crit > 0) sb.append("Crit = ").append(df2.format(gear_crit * 100)).append("%\n");
        if (gear_stun > 0) sb.append("Stun = ").append(df2.format(gear_stun * 100)).append("%\n");
        if ((burn_mult + gear_burn) > 1) sb.append("Burn = ").append(df2.format((burn_mult + gear_burn) * 100 - 100)).append("%\n");
        if (set_hit > 1) sb.append("Set Hit = ").append(df2.format(set_hit * 100 - 100)).append("%\n");
        if (set_magicdmg > 1) sb.append("Set MagicDmg = ").append(df2.format(set_magicdmg * 100 - 100)).append("%\n");
        if (set_physdmg > 1) sb.append("Set PhysDmg = ").append(df2.format(set_physdmg * 100 - 100)).append("%\n");
        if (set_mit1 > 0) sb.append("Set DmgMit = ").append(df2.format(set_mit1 * 100)).append("%\n");
        if (set_mit2 > 0) sb.append("Set DmgMit = ").append(df2.format(set_mit2 * 100)).append("%\n");
        return sb.toString();
    }

    public void increment_exp(double exp) {
        cl_exp += exp;
        ml_exp += exp;
        double need_cl = exp_to_cl(cl);
        double need_ml = exp_to_ml(ml);
        if (cl_exp >= need_cl && cl < 120) {
            cl += 1;
            cl_exp -= need_cl;
            setCLML(cl, ml);
            if (cl == cl_for_milestone()) {
                milestone_exp_mult += bonus_for_milestone();
            }
        }
        if (ml_exp >= need_ml) {
            ml += 1;
            ml_exp -= need_ml;
            setCLML(cl, ml);
        }
    }

    public void disableAllActives() {
        active_skills.forEach((key, value) -> value.enabled = false);
    }

    public void levelActives() {
        active_skills.forEach((key, value) -> value.gainExp());
    }

    public void levelPassives(double time) {
        passives.forEach((key, value) -> value.gainExp(time));
    }

    public double exp_to_cl(int lvl) {
        return (Math.pow(lvl, 5) / 340 + Math.pow(lvl, 2) * 50 + 10) * Math.pow(2, tier - 1);
    }

    public double exp_to_ml(int lvl) {
        return (Math.pow(lvl, 4) / 10 + Math.pow(lvl, 1.9) * 40 + 10);
    }

    public double bonus_for_milestone() {
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
            case 3 -> Main.game_version >= 1532 ? 90 : 75;
            default -> 0;
        };
    }

    public String getWeakAttackData() {
        StringBuilder sb = new StringBuilder();
        if (weak_a.used > 0) {
            sb.append(weak_a.getWeakRecordedData());
        }
        if (weak_i.used > 0) {
            sb.append(weak_i.getWeakRecordedData());
        }
        return sb.toString();
    }
}
