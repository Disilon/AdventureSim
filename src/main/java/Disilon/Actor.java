package Disilon;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static Disilon.Main.df2;
import static Disilon.Main.dfm;
import static Disilon.Main.game_version;
import static Disilon.Main.log;

public class Actor extends ActorStats {
    ActiveSkill weak_a = new ActiveSkill(this, "Weak Attack", 1, 54, 66, 1, 0, 1, 1, Scaling.atk, Element.phys,
            false, false);
    ActiveSkill weak_i = new ActiveSkill(this, "Weak Magic Arrow", 1, 55.8, 68.2, 1, 1, 1, 1, Scaling.intel, Element.magic,
            false, false);
    ActiveSkill counter_strike_log = new ActiveSkill(this, "Counter Strike");
    ActiveSkill counter_dodge_log = new ActiveSkill(this, "Counter Dodge");

    public Actor() {
        skills.passiveSkillData();
        skills.activeSkillData();
    }

    public void check_debuffs() {
        smoked = false;
        def_break = 0;
        res_break = 0;
        mark = 0;
        weaken = 0;
        slow = 1;
        bound = 0;
        for (Debuff d : debuffs) {
            if (d.name.equals("Smoke")) smoked = true;
            if (d.name.equals("Defense Break")) def_break = d.effect;
            if (d.name.equals("Res Break")) res_break = d.effect;
            if (d.name.equals("Weaken")) weaken = d.effect;
            if (d.name.equals("Mark")) mark = d.effect;
            if (d.name.equals("Bound")) bound = d.effect;
            if (d.name.equals("Slow")) slow *= 1 - d.effect;
        }
    }

    public void tick_debuffs() {
        Iterator<Debuff> debuff_iterator = debuffs.iterator();
        while (debuff_iterator.hasNext()) {
            Debuff d = debuff_iterator.next();
            if (!d.name.equals("Mark")) d.duration--;
            this.hp -= d.dmg;
            dot_tracking += d.dmg;
            if (d.dmg > 0 && log.contains("dot_dmg")) System.out.println(name + " taken dot dmg: " + (int) d.dmg);
            if (d.duration <= 0) {
                if (log.contains("debuff_removed")) {
                    System.out.println("Debuff " + d.name + " was removed from " + name);
                }
                debuff_iterator.remove();
            }
        }
        check_debuffs();
        if (hp < 0) hp *= 0.5; //dots give 50% overkill
    }

    public void check_buffs() {
        charge = 0;
        blessed = 0;
        empower_hp = 0;
        elemental_buff = 0;
        analyze_buff = 0;
        for (Buff b : buffs) {
            if (b.name.equals("Charge Up")) charge = b.effect;
            if (b.name.equals("Bless")) blessed = b.effect;
            if (b.name.equals("Empower HP")) empower_hp = b.effect;
            if (b.name.equals("Elemental Buff")) elemental_buff = b.effect;
            if (b.name.equals("Taking Notes")) analyze_buff = b.effect;
        }
    }

    public void tick_buffs(boolean attack) {
        Iterator<Buff> buff_iterator = buffs.iterator();
        while (buff_iterator.hasNext()) {
            Buff b = buff_iterator.next();
            if (log.contains("buff_duration")) System.out.println(b.name + " duration = " + b.duration);
            switch (b.name) {
                case "Charge Up", "Elemental Buff", "Taking Notes" -> b.duration = b.duration - (attack ? 1 : 0);
                case "Stone Barrier" -> {}
                default -> b.duration--;
            }
//            if (!b.name.equals("Charge Up") && !b.name.contains("Barrier")) {
//                b.duration--;
//            }
//            if (b.name.equals("Charge Up") && remove_charge) {
//                b.duration--;
//                remove_charge = false;
//            }
            if (b.duration <= 0) {
                if (log.contains("buff_removed")) {
                    System.out.println(b.name + " was removed from " + name);
                }
                buff_iterator.remove();
            }
        }
        check_buffs();
    }

    public void applyPendingEffects() {
        if (!pending_buffs.isEmpty()) {
            buffs.addAll(pending_buffs);
            pending_buffs.clear();
            check_buffs();
        }
        if (!pending_debuffs.isEmpty()) {
            debuffs.addAll(pending_debuffs);
            pending_debuffs.clear();
            check_debuffs();
        }
    }

    public void applyBuff(String buff_name, int duration, double effect) {
        if (buff_name != null) {
            pending_buffs.add(new Buff(buff_name, duration, effect));
            if (log.contains("buff_applied")) {
                String str = buff_name + " was applied to " + name + " duration = " + duration;
                if (effect > 0) str += " effect = " + df2.format(effect);
                System.out.println(str);
            }
        }
    }

    public void applyDebuff(String debuff_name, int duration, double dmg, double effect) {
        switch (debuff_name) {
            case "Stun" -> {
                stun_time += effect;
            }
            default -> pending_debuffs.add(new Debuff(debuff_name, duration, dmg, effect));
        }
        check_debuffs();
    }

    public void remove_buff(String name) {
        buffs.removeIf(b -> b.name.equals(name));
    }

    public boolean hasBuff(String name) {
        for (Buff b : buffs) {
            if (b.name.equals(name)) return true;
        }
        return false;
    }

    public boolean hasDebuff(String name) {
        for (Debuff d : debuffs) {
            if (d.name.equals(name)) return true;
        }
        return false;
    }

    public double getBarrier() {
        double barrier = 0;
        boolean elem_buff = false;
        Iterator<Buff> buff_iterator = buffs.iterator();
        while (buff_iterator.hasNext()) {
            Buff b = buff_iterator.next();
            if (b.name.contains("Barrier")) {
                barrier = b.effect;
                buff_iterator.remove();
                if (b.name.equals("Stone Barrier")) {
                    elem_buff = true;
                }
            }
        }
        if (elem_buff) {
            applyBuff("Elemental Buff", 1, 0.3 * (1 + gear_barrier));
        }
//        if (barrier > 0 ) System.out.println(name + " was defended by barrier " + (int) barrier);
        return barrier;
    }

    public int buff_count() {
        return buffs.size();
    }

    public void remove_mark() {
        Iterator<Debuff> debuff_iterator = debuffs.iterator();
        while (debuff_iterator.hasNext()) {
            Debuff d = debuff_iterator.next();
            if (Objects.equals(d.name, "Mark")) d.duration--;
            if (d.duration <= 0) {
                debuff_iterator.remove();
                mark = 0; //just in case
            }
        }
    }

    public void setEquip(String slot, String name, Equipment.Quality quality, int lvl) {
        if (name.equals("None")) return;
        Equipment e = Main.equipmentData.items.get(name).clone();
        e.setQualityLvl(quality, lvl);
        equipment.put(slot, e);
    }

    public void initializeSets() {
        sets.put("Cloth", new EquipmentSet("magicdmg", 5));
        sets.put("Blazing", new EquipmentSet("fire", 5));
        sets.put("Earthen", new EquipmentSet("earth", 5));
        sets.put("Leather", new EquipmentSet("hit", 5));
        sets.put("Dark", new EquipmentSet("physdmg", 5));
        sets.put("Metal", new EquipmentSet("mit1", 5));
        sets.put("Iron", new EquipmentSet("mit2", 5));
        sets.put("Holy", new EquipmentSet("res", 5));
        sets.put("Hunter", new EquipmentSet("core", 5));
        sets.put("Training", new EquipmentSet("training", 5));
        sets.put("Aquatic", new EquipmentSet("water", 5));
        sets.put("BronzeAcc", new EquipmentSet("mit1", 3));
        sets.put("CobaltAcc", new EquipmentSet("mana", 3));
        sets.put("Squirrel", new EquipmentSet("squirrel", 3));
    }

    public void enableSet(String bonus, Equipment.Quality quality, int upgrade) {
        double tier = 0.5 + quality.getMult() / 2;
        double stat_scaling;
        double dmg_scaling;
        if (Main.game_version < 1574) {
            stat_scaling = Math.clamp((5 + 0.5 * upgrade) * tier, 5, 100) / 100.0;
        } else {
            stat_scaling = Math.clamp((10 + 0.5 * upgrade) * tier, 10, 100) / 100.0;
        }
        dmg_scaling = Math.clamp((5 + 0.5 * upgrade) * tier, 5, 30) / 100.0;
        switch (bonus.toLowerCase()) {
            case "hit" -> set_hit = 1 + stat_scaling;
            case "res" -> set_res = 1 + stat_scaling;
            case "magicdmg" -> set_magicdmg = 1 + dmg_scaling;
            case "physdmg" -> set_physdmg = 1 + dmg_scaling;
            case "mit1" -> {
                if (Main.game_version < 1566) {
                    set_mit1 = Math.clamp((5 + upgrade / 6.0) * tier, 5, 50) / 100.0;
                } else {
                    set_mit1 = Math.clamp((8 + 0.2 * upgrade) * tier, 8, 60) / 100.0;
                }
            }
            case "mit2" -> {
                if (Main.game_version < 1566) {
                    set_mit2 = Math.clamp((10 + upgrade / 5.0) * tier, 10, 55) / 100.0;
                } else {
                    set_mit2 = Math.clamp((13 + 0.25 * upgrade) * tier, 13, 70) / 100.0;
                }
            }
            case "core" -> set_core = Math.clamp((10 + 0.4 * upgrade) * tier, 10, 100) / 100.0;
            case "water" -> set_water = 1 + stat_scaling;
            case "fire" -> set_fire = 1 + stat_scaling;
            case "earth" -> set_earth = 1 + stat_scaling;
            case "training" -> {
                set_exp = Math.clamp((15 + 0.5 * upgrade) * tier, 15, 150) / 100.0;
                set_training = Math.clamp((5 + 0.1 * upgrade) * tier, 5, 25) / 100.0;
            }
            case "mana" -> {
                set_mana = Math.clamp((10 + 0.25 * upgrade) * tier, 10, 75) / 100.0;
            }
            case "squirrel" -> {
                set_squirrel_drop = 1 + Math.clamp((5 + 0.45 * upgrade) * tier, 5, 100) / 100.0;
                set_squirrel_rate = Math.clamp(95 - (0.25 * upgrade) * tier, 50, 95);
            }
        }
    }

    public void disableSet() {
        set_hit = 1;
        set_res = 1;
        set_magicdmg = 1;
        set_physdmg = 1;
        set_mit1 = 0;
        set_mit2 = 0;
        set_core = 0;
        set_water = 1;
        set_fire = 1;
        set_earth = 1;
        set_exp = 0;
        set_training = 0;
        set_mana = 0;
        set_squirrel_drop = 1;
        set_squirrel_rate = 100;
        for (EquipmentSet set : sets.values()) {
            set.current_items = 0;
            set.min_quality = null;
            set.min_upgrade = -1;
        }
    }

    public void checkAmbush() {
        if (passives.get("Ambush").enabled) {
            ambushing = true;
            ambush_bonus = passives.get("Ambush").getBonus();
        }
    }

    public void enablePassives(String[] names) {
        Set<String> keys = passives.keySet();
        for (String key : keys) {
            passives.get(key).enabled = false;
        }
        for (String name : names) {
            if (passives.containsKey(name)) {
                passives.get(name).enabled = true;
            }
        }
        refreshStats();
    }

    public void applyGear() {
        clear_gear_stats();
        if (name.equals("Onion Knight") && equipment.get("MH") == null) {
            if (passives.get("Tsury Finke").available) {
                int tf_lvl = passives.get("Tsury Finke").lvl;
                double quality = switch (tf_lvl / 10) {
                    case 0 -> 0.5;
                    case 1 -> 0.75;
                    case 2 -> 1;
                    case 3 -> 1.25;
                    case 4 -> 1.5;
                    case 5 -> 2;
                    case 6 -> 2.5;
                    case 7 -> 3;
                    case 8 -> 4;
                    case 9, 10 -> 5;
                    default -> 0;
                };
                gear_atk += 24 * quality * (1 + 0.1 * tf_lvl);
                gear_hit += 8 * quality * (1 + 0.1 * tf_lvl);
                gear_speed += 8 * quality * (1 + 0.1 * tf_lvl);
                gear_water += 20 * quality * (1 + 0.1 * tf_lvl);
                finke_bonus = (5 + (2.5 * (0.5 + quality / 2) * tf_lvl * 0.1)) * 0.01;
            }
        }
        for (Map.Entry<String, Equipment> slot : equipment.entrySet()) {
            Equipment item = slot.getValue();
            if (item.name != null && !item.name.equals("None")) {
                gear_atk += item.atk * (1 + 0.01 * research_lvls.getOrDefault("Equip Atk", 0.0).intValue());
                gear_def += item.def * (1 + 0.01 * research_lvls.getOrDefault("Equip Def", 0.0).intValue());
                gear_hit += item.hit * (1 + 0.01 * research_lvls.getOrDefault("Equip Hit", 0.0).intValue());
                gear_speed += item.speed * (1 + 0.01 * research_lvls.getOrDefault("Equip Spd", 0.0).intValue());
                gear_int += item.intel * (1 + 0.01 * research_lvls.getOrDefault("Equip Int", 0.0).intValue());
                gear_res += item.resist * (1 + 0.01 * research_lvls.getOrDefault("Equip Res", 0.0).intValue());
                gear_hp += item.hp * (1 + 0.01 * research_lvls.getOrDefault("Equip Hp", 0.0).intValue());
                gear_water += item.water;
                gear_fire += item.fire;
                gear_earth += item.earth;
                gear_wind += item.wind;
                gear_light += item.light;
                gear_dark += item.dark;
                gear_crit += item.crit;
                gear_burn += item.burn;
                gear_stun += item.stun;
                gear_analyze += item.analyze;
                gear_barrier += item.barrier;
                add_resist("Fire", item.fire_res * 0.01);
                add_resist("Water", item.water_res * 0.01);
                add_resist("Wind", item.wind_res * 0.01);
                add_resist("Earth", item.earth_res * 0.01);
                add_resist("Light", item.light_res * 0.01);
                add_resist("Dark", item.dark_res * 0.01);
                add_resist("Magic", item.magic_res * 0.01);
                add_resist("Physical", item.phys_res * 0.01);
                String set_type = item.displayName;
                if (set_type.equals("Blazing")) {
                    if (game_version >= 1591) {
                        set_type = "Blazing";
                    } else {
                        set_type = "Cloth";
                    }
                }
                if (set_type.equals("Windy")) set_type = "Leather";
                if (set_type.equals("Bronze")) set_type = "Iron";
                if (sets.containsKey(set_type)) {
                    sets.get(set_type).addItem(item.quality, item.upgrade);
                }
            }
        }
        for (EquipmentSet set : sets.values()) {
            if (set.completed()) {
                enableSet(set.bonus, set.min_quality, set.min_upgrade);
            }
        }
        if (set_mit1 > 0) add_resist("All", set_mit1);
        if (set_mit2 > 0) add_resist("All", set_mit2);
    }

    public void refreshStats() {
        hp_mult = 1;
        mp_mult = 1;
        atk_mult = 1;
        int_mult = 1;
        def_mult = 1;
        res_mult = 1;
        hit_mult = 1;
        speed_mult = 1;
        dodge_mult = 1;
        dmg_mult = 1;
        poison_mult = 1;
        ailment_res = 1;
        exp_mult = 1;
        cast_speed_mult = 1;
        delay_speed_mult = 1;
        core_mult = 0;
        counter_strike = 0;
        multi_arrows = 0;
        bless_boost = 1;
        bless_duration = 0;
        finke_bonus = 0;
        analyze_mult = 0;
        analyze_speed = 1;

        atk_mult *= 1.0 + passives.get("Attack Boost").getBonus();
        poison_mult *= 1.0 + passives.get("Poison Boost").getBonus();
        dmg_mult *= 1.0 + passives.get("Dagger Mastery").getBonus();
        dmg_mult *= 1.0 + passives.get("Fist Mastery").getBonus();
        dmg_mult *= 1.0 + passives.get("Sword Mastery").getBonus();
        dmg_mult *= 1.0 + passives.get("Spear Mastery").getBonus();
        dmg_mult *= 1.0 + passives.get("Bow Mastery").getBonus();
        dmg_mult *= 1.0 + passives.get("Wand Mastery").getBonus();
        dmg_mult *= 1.0 + passives.get("Book Mastery").getBonus();
        dmg_mult *= 1.0 + passives.get("Weapon Mastery").getBonus();
        dmg_mult *= 1.0 + passives.get("Dual Wield").getBonus();
        dmg_mult *= 1.0 + passives.get("Concentration").getBonus();
        dmg_mult *= 1.0 + passives.get("Stealth").getBonus();
        poison_mult *= 1.0 + passives.get("Stealth").getBonus();
        speed_mult *= 1.0 + passives.get("Speed Boost").getBonus();
        exp_mult *= 1.0 + passives.get("Exp Boost").getBonus();
        def_mult *= 1.0 + passives.get("Defense Boost").getBonus();
        dodge_mult *= 1.0 + passives.get("Dodge").getBonus();
        counter_strike = passives.get("Counter Strike").getBonus();
        hit_mult *= 1.0 + passives.get("Hit Boost").getBonus();
        hit_mult *= 1.0 + passives.get("Concentration").getBonus();
        int_mult *= 1.0 + passives.get("Int Boost").getBonus();
        res_mult *= 1.0 + passives.get("Res Boost").getBonus();
        ailment_res *= 1.0 + passives.get("Ailment Res").getBonus();
        analyze_mult += passives.get("Smart Analyze").getBonus();
        analyze_speed *= 1.0 - passives.get("Speedy Analyze").getBonus();
        cast_speed_mult *= 1.0 - passives.get("Casting Boost").getBonus();
        cast_speed_mult *= 1.0 + (passives.get("Concentration").getBonus() > 0 ? 0.25 : 0);
        delay_speed_mult *= 1.0 + (passives.get("Concentration").getBonus() > 0 ? 0.25 : 0);
        bless_boost *= 1.0 + passives.get("Bless Mastery").getBonus();
        bless_duration = passives.get("Bless Mastery").getBonus2();
        hp_mult *= 1.0 + passives.get("HP Boost").getBonus();
        hp_regen = passives.get("HP Regen").getBonus();
        drop_mult = 1 + passives.get("Drop Boost").getBonus();
        core_mult = passives.get("Drop Boost").getBonus();
        if (passives.get("Multi Arrows").enabled) {
            multi_arrows = passives.get("Multi Arrows").getBonus();
        }
        if (passives.get("Core Boost").enabled) {
            core_mult = passives.get("Core Boost").getBonus();
        }
        mp_cost_add = 0;
        mp_cost_mult = 1;
        applyGear();
        for (Map.Entry<String, PassiveSkill> passive : passives.entrySet()) {
            if (passive.getValue().enabled) {
                mp_cost_add += passive.getValue().mp_add;
                mp_cost_mult *= 1 + passive.getValue().mp_mult;
            }
        }
        for (Map.Entry<String, ActiveSkill> active : active_skills.entrySet()) {
            if (active.getValue().enabled) {
                if (active.getValue().name.equals("Elemental Blast")) eblast_enabled = true;
                if (active.getValue().name.equals("Holy Light"))
                    holylight_enabled = true;
                if (active.getValue().name.equals("Prayer") && Main.game_version >= 1568)
                    prayer_enabled = true;
                if (active.getValue().name.equals("Aura Blade")) aurablade_enabled = true;
            }
        }
        atk = (base_atk + gear_atk);
        def = (base_def + gear_def);
        intel = (base_int + gear_int);
        resist = (base_res + gear_res) * set_res;
        hit = (base_hit + gear_hit) * set_hit;
        speed = (base_speed + gear_speed);
        hp_max = (base_hp_max + gear_hp);
        mp_max = (resist * 3 + intel) * mp_mult;

        if (research_lvls != null) {
            if (tier >= 3) {
                exp_mult *= 1.0 + set_exp * (1 + 0.01 * Math.max(0,
                        120 + research_lvls.getOrDefault("Max CL", 0.0).intValue() - cl));
            } else {
                exp_mult *= 1.0 + set_exp;
            }
        }
        if (passives.get("Fire Resistance").enabled) {
            add_resist("Fire", passives.get("Fire Resistance").getBonus());
        }
        if (passives.get("Earth Resistance").enabled) {
            add_resist("Earth", passives.get("Earth Resistance").getBonus());
        }
    }

    public void add_resist(String type, double value) {
        switch (type) {
            case "Physical" -> phys_res = 1.0 - (1.0 - phys_res) * (1.0 - value);
            case "Magic" -> magic_res = 1.0 - (1.0 - magic_res) * (1.0 - value);
            case "Fire" -> fire_res = 1.0 - (1.0 - fire_res) * (1.0 - value);
            case "Water" -> water_res = 1.0 - (1.0 - water_res) * (1.0 - value);
            case "Earth" -> earth_res = 1.0 - (1.0 - earth_res) * (1.0 - value);
            case "Wind" -> wind_res = 1.0 - (1.0 - wind_res) * (1.0 - value);
            case "Light" -> light_res = 1.0 - (1.0 - light_res) * (1.0 - value);
            case "Dark" -> dark_res = 1.0 - (1.0 - dark_res) * (1.0 - value);
            case "All" -> {
                phys_res = 1.0 - (1.0 - phys_res) * (1.0 - value);
                magic_res = 1.0 - (1.0 - magic_res) * (1.0 - value);
                fire_res = 1.0 - (1.0 - fire_res) * (1.0 - value);
                water_res = 1.0 - (1.0 - water_res) * (1.0 - value);
                earth_res = 1.0 - (1.0 - earth_res) * (1.0 - value);
                wind_res = 1.0 - (1.0 - wind_res) * (1.0 - value);
                light_res = 1.0 - (1.0 - light_res) * (1.0 - value);
                dark_res = 1.0 - (1.0 - dark_res) * (1.0 - value);
            }
        }
    }

    public void clear_gear_stats() {
        gear_atk = 0;
        gear_def = 0;
        gear_hit = 0;
        gear_speed = 0;
        gear_int = 0;
        gear_res = 0;
        gear_hp = 0;
        gear_water = 0;
        gear_fire = 0;
        gear_earth = 0;
        gear_wind = 0;
        gear_light = 0;
        gear_dark = 0;
        gear_crit = 0;
        gear_burn = 0;
        gear_stun = 0;
        gear_analyze = 0;
        gear_barrier = 0;
        phys_res = base_phys_res;
        magic_res = base_magic_res;
        water_res = base_water_res;
        fire_res = base_fire_res;
        earth_res = base_earth_res;
        wind_res = base_wind_res;
        light_res = base_light_res;
        dark_res = base_dark_res;
        disableSet();
    }

    public void clear_skills_recorded_data() {
        for (ActiveSkill skill : active_skills.values()) {
            if (skill != null) {
                skill.clear_recorded_data();
            }
        }
        weak_a.clear_recorded_data();
        weak_i.clear_recorded_data();
        counter_dodge_log.clear_recorded_data();
        counter_strike_log.clear_recorded_data();
    }

    public void setCLML(int cl, int ml) {
    }

    public ActiveSkill getWeakSkill() {
        if (atk > intel) {
            return weak_a;
        } else {
            return weak_i;
        }
    }

    public double getPrepare_hps() {
        return getHp_max() * (0.011 + 0.000225 * prepare.lvl);
    }

    public double getPrepare_mps() {
        return getMp_regen() + getMp_max() * (0.0056 + 0.00011 * prepare.lvl);
    }

    public double getMp_regen() {
        return getMp_max() / 360.0;
    }

    public double getHp_max() {
        return hp_max * hp_mult;
    }

    public String getHp_max_string() {
        double num = getHp_max();
        String pretty;
        if (num > 15000) {
            pretty = dfm.format(num / 1000) + "k";
        } else {
            pretty = String.valueOf((int) num);
        }
        return pretty;
    }

    public void setHp(double hp) {
        this.hp = Math.min(getHp_max(), hp);
    }

    public void setHp(double hp, double overheal) {
        this.hp = Math.min(getHp_max() * (1 + overheal), hp);
    }

    public double getMp_max() {
        return getResist() * 3 + getIntel();
    }

    public double getMp_max_no_buffs() {
        return getResist_no_buffs() * 3 + getIntel_no_buffs();
    }

    public void setMp(double mp) {
        this.mp = Math.min(getMp_max(), mp);
    }

    public double getAtk() {
        return atk * (atk_mult + blessed) + empower_hp * getHp_max();
    }

    public double getAtk_no_buffs() {
        return atk * (atk_mult);
    }

    public double getDef() {
        return def * (def_mult + blessed - def_break - mark);
    }

    public double getDef_no_buffs() {
        return def * (def_mult);
    }

    public double getIntel() {
        return intel * (int_mult + blessed);
    }

    public double getIntel_no_buffs() {
        return intel * (int_mult);
    }

    public double getResist() {
        return resist * (res_mult - mark - res_break + blessed);
    }

    public double getResist_no_buffs() {
        return resist * (res_mult);
    }

    public double getGear_res() {
        return getResist() - base_res * (res_mult - mark - res_break + blessed);
    }

    public double getHit() {
        return hit * hit_mult;
    }

    public double getHit_no_buffs() {
        return hit * hit_mult;
    }

    public double getSpeed() {
        return speed * speed_mult * slow;
    }

    public double getSpeed_no_buffs() {
        return speed * speed_mult;
    }

    public double getDark() {
        return dark;
    }

    public double getWater() {
        return water;
    }

    public double getFire() {
        return fire;
    }

    public double getWind() {
        return wind;
    }

    public double getEarth() {
        return earth;
    }

    public double getLight() {
        return light;
    }

    public void setLight(double light) {
        this.light = light;
    }

    public double getWater_res() {
        return water_res;
    }

    public double getFire_res() {
        return fire_res;
    }

    public double getWind_res() {
        return wind_res;
    }

    public double getEarth_res() {
        return earth_res;
    }

    public double getLight_res() {
        return light_res;
    }

    public double getDark_res() {
        return dark_res;
    }

    public double getPhys_res() {
        return phys_res;
    }

    public double getMagic_res() {
        return magic_res;
    }

    public double getDmg_mult() {
        double mult = 1.0;
        mult *= 1.0 + charge;
        mult *= 1.0 - weaken;
        return dmg_mult * mult;
    }

    public double stealthDelay() {
        double delay = passives.get("Stealth").enabled ? 1.2 * (1 + 0.02 * passives.get("Stealth").lvl) : 0;
        delay += passives.get("Safe Distance").enabled ? passives.get("Safe Distance").getBonus() : 0;
        return delay;
    }

    public double getDodge_mult() {

        return dodge_mult * (1.0 - mark) * (1.0 - bound);
    }

    public boolean isMulti_hit_override(String skill_name) {
        if (Main.game_version < 1566) return (multi_arrows > 0);
        switch (skill_name) {
            case "Arrow Rain":
            case "Double Shot":
            case "Careful Shot":
            case "Aimed Shot":
            case "Weakening Shot":
                return (multi_arrows > 0);
            default:
                return false;
        }
    }

    public double getAvgStats() {
        return (getAtk() + getDef() + getIntel() + getResist() + getSpeed() + getHit()) / 6;
    }

    public double getAvgAtkInt() {
        return (getAtk() + getIntel()) / 2;
    }

    public double getAvgStats_no_buffs() {
        return (getAtk_no_buffs() + getDef_no_buffs() + getIntel_no_buffs() + getResist_no_buffs() + getSpeed_no_buffs() + getHit_no_buffs()) / 6;
    }

    public double getAvgAtkInt_no_buffs() {
        return (getAtk_no_buffs() + getIntel_no_buffs()) / 2;
    }

    public boolean checkLastSkill(String name) {
        if (last_skill == null) return false;
        return last_skill.name.equals(name);
    }
}
