package Disilon;

import java.util.LinkedHashMap;

public class SkillData {
    private LinkedHashMap<String, PassiveSkill> passives;
    private LinkedHashMap<String, ActiveSkill> active_skills;

    public SkillData(LinkedHashMap<String, ActiveSkill> active_skills, LinkedHashMap<String, PassiveSkill> passives) {
        this.active_skills = active_skills;
        this.passives = passives;
    }

    public void addActive(String name, double mp, double cast_mult, double delay_mult) {
        active_skills.put(name, new ActiveSkill(name, mp, cast_mult, delay_mult));
    }

    public void addActive(String name, int hits, double min, double max, double hit, double mp, double cast_mult, double delay_mult,
                          Scaling scaling, Element element, boolean aoe, boolean heal) {
        active_skills.put(name, new ActiveSkill(name, hits, min, max, hit, mp, cast_mult, delay_mult, scaling, element, aoe, heal));
    }

    public void addActiveHidden(String name, double mp, double cast_mult, double delay_mult, String link_name) {
        active_skills.put(link_name, new ActiveSkill(name, mp, cast_mult, delay_mult));
    }

    public void addPassive(String name, double base_bonus, double base_mp_add, double base_mp_mult) {
        passives.put(name, new PassiveSkill(name, base_bonus, base_mp_add, base_mp_mult));
    }

    public void disableAll() {
        for (PassiveSkill p : passives.values()) {
            p.available = false;
            p.setLvl(0);
        }
        for (ActiveSkill a : active_skills.values()) {
            a.available = false;
            a.setSkill(0, SkillMod.Basic);
            a.clear_recorded_data();
        }
    }

    public void enableActive(String name) {
        active_skills.get(name).available = true;
    }

    public void enablePassive(String name) {
        passives.get(name).available = true;
    }

    public void passiveSkillData() {
        addPassive("Attack Boost", 0.2, 10, 0.1);
        addPassive("Drop Boost", 0.3, 10, 0.1);
        addPassive("Dagger Mastery", 0.2, 0, 0);
        addPassive("Stealth", 0.2, 15, 0.15);
        addPassive("Speed Boost", 0.2, 5, 0.2);
        addPassive("Poison Boost", 0.5, 10, 0.1);
        addPassive("Defense Boost", 0.2, 5, 0.2);
        addPassive("Dodge", 0.25, 10, 0.1);
        addPassive("Fist Mastery", 0.2, 0, 0);
        addPassive("Counter Strike", 0.25, 15, 0.25);
        addPassive("Bow Mastery", 0.2, 0, 0);
        addPassive("Ambush", 0.2, 15, 0.25);
        addPassive("HP Regen", 0.02, 5, 0.2);
        addPassive("Concentration", 0.3, 15, 0.15);
        addPassive("Hit Boost", 0.2, 10, 0.1);
        addPassive("Sword Mastery", 0.2, 0, 0);
        addPassive("Spear Mastery", 0.2, 0, 0);
        addPassive("HP Boost", 0.25, 5, 0.2);
        addPassive("Int Boost", 0.2, 5, 0.3);
        addPassive("Res Boost", 0.2, 5, 0.3);
        addPassive("Wand Mastery", 0.2, 0, 0.0);
        addPassive("Casting Boost", 0.15, 10, 0.2);
        addPassive("Fire Boost", 0.3, 10, 0.3);
        addPassive("Fire Resistance", 0.5, 10, 0.3);
        addPassive("Earth Boost", 0.3, 10, 0.3);
        addPassive("Earth Resistance", 0.5, 10, 0.3);
        addPassive("Book Mastery", 0.2, 0, 0.0);
        addPassive("Ailment Res", 0.75, 10, 0.2);
        addPassive("Multi Arrows", 0.4, 10, 0.15);
        addPassive("Core Boost", 0.5, 10, 0.1);
        addPassive("Light Boost", 0.3, 10, 0.3);
        addPassive("Bless Mastery", 0.3, 10, 0.3);
        addPassive("Water Boost", 0.3, 10, 0.3);
        addPassive("Weapon Mastery", 0.2, 0, 0);
        addPassive("Tsury Finke", 0.0, 0, 0);
        addPassive("Extra Attack", 0.75, 15, 0.1);
        addPassive("Dual Wield", 0.25, 10, 0.1);
        addPassive("Exp Boost", 0.15, 10, 0.1);
    }

    public void activeSkillData() {
        addActive("Hide", 1, 0.3, 0, 0, 5, 0.5, 0.5, Scaling.atk, Element.none, false, false);
        addActive("Killing Strike", 1, 297, 363, 0.7, 80, 2, 2, Scaling.atk, Element.dark,
                false, false);
        addActive("Dragon Punch", 3, 76.5, 93.5, 0.8, 20, 1, 3, Scaling.atk, Element.phys,
                false, false);
        addActive("Whirling Foot", 3, 49.5, 60.5, 0.8, 30, 1.2, 2, Scaling.atk, Element.phys,
                true, false);
        addActive("Poison Attack", 1, 36, 44, 1, 4, 0.4, 0.9, Scaling.atk, Element.phys, false
                , false);
        active_skills.get("Poison Attack").addDebuff("Poison", 3, 0.15);
        active_skills.get("Poison Attack").weapon_required = false;
        addActive("Smoke Screen", 1, 0, 0, 0.85, 25, 0.8, 1, Scaling.atk, Element.none, true, false);
        active_skills.get("Smoke Screen").addDebuff("Smoke", 3, 0);
        addActive("Sharp Shooting", 1, 207, 253, 1.5, 80, 2, 3, Scaling.atkhit, Element.wind,
                false, false);
        addActive("Double Shot", 2, 76.5, 93.5, 1, 12, 1.1, 1.1, Scaling.atkhit, Element.phys,
                false, false);
        addActive("Arrow Rain", 5, 49.5, 60.5, 0.7, 25, 1.5,
                1.5, Scaling.atkhit, Element.phys, false, false);
        active_skills.get("Arrow Rain").random_targets = true;
        addActive("Bash", 1, 103.5, 126.5, 1, 10, 1.2, 1.1, Scaling.atk, Element.phys, false,
                false);
        active_skills.get("Bash").addDebuff("Defense Break", 3, 0.2);
        active_skills.get("Bash").weapon_required = false;
        addActive("Trap", 1, 90, 110, 1.0, 0, 1,1.0, Scaling.intel, Element.magic, false, false);
        active_skills.get("Trap").addDebuff("Stun", 0, 2.5);
        active_skills.get("Trap").weapon_required = false;
        addActive("Attack", 1, 90, 110, 1, 0, 1, 1, Scaling.atk, Element.phys, false,
                false);
        active_skills.get("Attack").weapon_required = false;
        addActive("Aura Shot", 1, 112.5, 137.5, 1, 15, 0.9, 0.9, Scaling.atkint, Element.fire,
                false,false);
        active_skills.get("Aura Shot").weapon_required = false;
        addActive("Defense Break", 1, 90, 110, 1, 10, 1, 1, Scaling.atk, Element.phys, false,
                false);
        active_skills.get("Defense Break").addDebuff("Defense Break", 3, 0.25);
        addActive("Quick Hit", 1, 76.5, 93.5, 1, 10, 0.7, 0.7, Scaling.atk, Element.phys, false,
                false);
        active_skills.get("Quick Hit").weapon_required = false;
        addActive("Aura Blade", 1, 121.5, 148.5, 1, 15, 1.3, 1.3, Scaling.atk, Element.light,
                false, false);
        addActive("Sword Rush", 3, 67.5, 82.5, 1, 15, 1.5, 1.5, Scaling.atk, Element.phys,
                false, false);
        addActive("Mark Target", 1, 0, 0, 1.5, 10, 0.5, 0.5, Scaling.atk, Element.none,
                false, false);
        active_skills.get("Mark Target").addDebuff("Mark", 1, 0.2);
        addActive("Charge Up", 1, 0, 0, 0, 50, 2, 2, Scaling.atk, Element.none, false,
                false);
        active_skills.get("Charge Up").addBuff("Charge Up", 1, 1.5);
        addActive("Fire Ball", 1, 99, 121, 1.35, 20, 1.15, 1, Scaling.intel, Element.fire,
                false, false);
        active_skills.get("Fire Ball").addDebuff("Burn", 3, 1);
        addActive("Fire Pillar", 1, 180, 220, 1.0, 50, 1.5,1.5, Scaling.intel, Element.fire, false, false);
        active_skills.get("Fire Pillar").addDebuff("Burn", 3, 1);
        addActive("Explosion", 1, 1350, 1650, 1.15, 500, 8, 30, Scaling.intel,
                Element.fire, true, false);
        active_skills.get("Explosion").addDebuff("Burn", 3, 1);
        addActive("Rock Shot", 1, 198, 242, 1.0, 50, 1.8,1.0, Scaling.intel, Element.earth, false, false);
        active_skills.get("Rock Shot").addDebuff("Stun", 0, 3);
        addActive("Earth Quake", 1, 315, 385, 1.2, 400, 3,6, Scaling.intel, Element.earth, true, false);
        active_skills.get("Earth Quake").addDebuff("Stun", 0, 2.5);
        addActive("Stone Barrier", 1, 0.8, 0, 0, 24, 1, 1, Scaling.atk, Element.none, false, false);
        addActive("Elemental Blast", 1, 117, 143, 1.0, 20, 1.2, 1.2, Scaling.intel,
                Element.eleblast, false, false);
        addActive("Magic Missile", 1, 148.5, 181.5, 1.5, 25, 1.5, 1.5, Scaling.intel,
                Element.magic, false, false);
        active_skills.get("Magic Missile").addDebuff("Resist Break", 3, 0.25);
        addActive("Magic Arrow", 1, 90, 110, 1, 15, 1, 1, Scaling.intel,
                Element.magic, false, false);
        addActive("Holy Light", 1, 112.5, 137.5, 1.0, 25, 1, 1.1, Scaling.resint,
                Element.light, false, false);
        addActive("Heal", 1, 250, 45, 0, 30, 0.8, 1.5, Scaling.atk, Element.none, false,
                true);
        addActive("Bless", 1, 0, 0, 0, 40, 0.5, 0.5, Scaling.atk, Element.none, false,
                false);
        active_skills.get("Bless").addBuff("Bless", 2, 0.3);
        addActive("Push Blast", 1, 99, 121, 0.9, 30, 1.3, 1.3, Scaling.intel,
                Element.magic, true, false);
        addActive("Double Attack", 2, 64.8, 79.2, 1, 7, 1, 1, Scaling.atk,
                Element.phys, false, false);
        active_skills.get("Double Attack").weapon_required = false;
        addActive("Empower HP", 1, 0, 0, 0, 60, 2, 2, Scaling.atk, Element.none, false,
                false);
        active_skills.get("Empower HP").addBuff("Empower HP", 7, 0.05);
        addActive("Rapid Stabs", 5, 67.5, 82.5, 1.1, 90, 1.7, 1,
                Scaling.atk, Element.phys, false, false);
        addActive("Pierce", 1, 67.5, 82.5, 1, 40, 1.2, 1.2,
                Scaling.atk, Element.phys, false, false);
        addActive("Careful Shot", 1, 58.5, 71.5, 1.5, 15, 0.6, 0.6, Scaling.atkhit,
                Element.phys, false, false);
        addActive("Weakening Shot", 1, 144, 176, 1, 50, 1.3, 1.3, Scaling.atkhit,
                Element.phys, false, false);
        active_skills.get("Weakening Shot").addDebuff("Weaken", 4, 0.25);
        addActive("Aimed Shot", 1, 270, 330, 1.5, 60, 1.5, 1.5, Scaling.atkhit,
                Element.phys, false, false);
        addActive("Prayer", 1, 30, 0, 0, 99, 1.5, 1.5, Scaling.atk, Element.none, false,
                false);
        addActive("Holy Ray", 1, 198, 242, 1.0, 66, 1.8, 1.8, Scaling.res,
                Element.light, false, false);
        addActive("Dispel", 1, 90, 110, 1, 50, 1, 1, Scaling.resint, Element.light, false,
                false);
        addActive("Onion Slash", 1, 252, 308, 1.25, 99, 1.4, 1.4, Scaling.atk,
                Element.water, false, false);
        addActive("Onion Wave", 1, 299.7, 366.3, 0.99, 333, 2.9, 5, Scaling.atk,
                Element.water, true, false);
        addActive("Throw Sand", 1, 0, 0, 0, 20, 0.8, 0.5, Scaling.atk, Element.phys,
                false, false);
        active_skills.get("Throw Sand").addDebuff("Smoke", 3, 0);
        active_skills.get("Throw Sand").addBuff("Elemental Buff", 3, 0.3);
        addActive("Back Stab", 1, 225, 275, 2, 80, 2, 2, Scaling.atk, Element.water,
                false, false);
        addActive("Binding Shot", 1, 45, 55, 1.5, 50, 0.7, 1, Scaling.atkhit,
                Element.phys, false, false);
        active_skills.get("Binding Shot").addDebuff("Bound", 3, 0.25);
        addActive("Water Punch", 1, 99, 121, 1, 10, 0.9, 0.9, Scaling.atkint, Element.water,
                false, false);
        active_skills.get("Water Punch").weapon_required = false;
        addActive("Tsunami", 1, 252/0.7, 308/0.7, 1, 150, 4, 6, Scaling.intel, Element.water, true, false);
        active_skills.get("Tsunami").addDebuff("Poison", 3, 0.15);
        addActive("Gust", 1, 56.7/0.7, 69.3/0.7, 1, 38, 0.8, 0.8, Scaling.intel, Element.wind,
                false, false);
        addActive("Air Compression", 1, 91.35/0.7, 111.65/0.7, 1, 90, 1.2, 1.2, Scaling.intel,
                Element.wind,false, false);
        active_skills.get("Air Compression").addDebuff("Slow", 3, 0.25);
        addActive("Soul Slash", 1, 2250, 2750, 2, 1000, 0.3, 75, Scaling.atk,
                Element.phys, true, false);
        active_skills.get("Soul Slash").weapon_required = false;
        addActive("Dark Slash", 1, 135, 165, 1, 35, 0.9, 0.9, Scaling.atk, Element.dark,
                false, false);
        active_skills.get("Dark Slash").weapon_required = false;
        addActive("Cupid Hard Love Shot", 1, 189/0.7, 231/0.7, 1.75, 0, 1.4, 1.4, Scaling.atkint, Element.physmagic,
                false, false);
        addActive("Blind Enemy", 1, 76.5, 93.5, 1, 15, 1, 1, Scaling.atk, Element.phys,
                false, false);
        active_skills.get("Blind Enemy").addDebuff("Smoke", 3, 0);
        active_skills.get("Blind Enemy").weapon_required = false;
        addActive("Charge Attack", 1, 270, 330, 1, 20, 2.5, 2, Scaling.atk, Element.phys,
                false, false);
        active_skills.get("Charge Attack").weapon_required = false;
        addActive("Arrow Of Light", 1, 220.5/0.7, 269.5/0.7, 1, 88, 2.7, 2.7, Scaling.atkhit,
                Element.light,false, false);
        addActive("Holy Slash", 1, 141.75/0.7, 173.25/0.7, 1, 50, 1.8, 1.8, Scaling.atk,
                Element.light,false, false);
        addActive("Holy Power Slash", 1, 252/0.7, 308/0.7, 1, 150, 3.4, 3.4, Scaling.atk,
                Element.light,false, false);
        addActive("Sense", 1, 90, 110, 2.5, 30, 0.5, 0.5, Scaling.atk,
                Element.magic,false, false);
        active_skills.get("Sense").addDebuff("Mark", 1, 0.2);
        active_skills.get("Sense").weapon_required = false;

        addActive("Basic Attack", 1, 81, 99, 1, 0, 1, 1, Scaling.atk,
                Element.phys, false, false);
        addActive("First Aid", 1, 15, 15, 0, 5, 0.9, 1.1, Scaling.atk, Element.none, false,
                true);
        addActive("Prepare", 0, 1, 1);
        addActive("Extra Attack", 1, 75, 75, 100, 0, 0, 0, Scaling.atk,
                Element.water,false, false);
    }


}
