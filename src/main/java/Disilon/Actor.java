package Disilon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Actor {
    protected String name;
    protected int ml;
    protected int cl;
    protected double hp_max;
    protected double hp;
    protected double mp_max;
    protected double mp;
    protected double atk;
    protected double def;
    protected double intel;
    protected double resist;
    protected double hit;
    protected double speed;
    protected double water;
    protected double fire;
    protected double wind;
    protected double earth;
    protected double light;
    protected double dark;
    protected double burn;

    protected double base_hp_max;
    protected double base_atk;
    protected double base_def;
    protected double base_int;
    protected double base_res;
    protected double base_hit;
    protected double base_speed;
    protected double base_fire;
    protected double base_water;
    protected double base_wind;
    protected double base_earth;
    protected double base_light;
    protected double base_dark;
    protected double base_exp;
    protected double base_water_res;
    protected double base_fire_res;
    protected double base_wind_res;
    protected double base_earth_res;
    protected double base_light_res;
    protected double base_dark_res;
    protected double base_phys_res;
    protected double base_magic_res;

    protected double gear_atk;
    protected double gear_def;
    protected double gear_int;
    protected double gear_res;
    protected double gear_hit;
    protected double gear_speed;
    protected double gear_water;
    protected double gear_fire;
    protected double gear_wind;
    protected double gear_earth;
    protected double gear_light;
    protected double gear_dark;
    protected double gear_crit;
    protected double gear_burn;

    protected double set_hit = 1;
    protected double set_magicdmg = 1;
    protected double set_physdmg = 1;
    protected double set_mit1 = 1;
    protected double set_mit2 = 1;

    protected double hp_mult = 1;
    protected double mp_mult = 1;
    protected double atk_mult = 1;
    protected double int_mult = 1;
    protected double def_mult = 1;
    protected double res_mult = 1;
    protected double hit_mult = 1;
    protected double speed_mult = 1;
    protected double dodge_mult = 1;

    protected double water_res;
    protected double fire_res;
    protected double wind_res;
    protected double earth_res;
    protected double light_res;
    protected double dark_res;
    protected double phys_res;
    protected double magic_res;

    protected double dmg_mult = 1;
    protected double poison_mult = 1;
    protected double fire_mult = 1;
    protected double ailment_res = 1;
    protected ActiveSkill prepare;
    protected double exp;
    protected double exp_mult = 1;
    protected double mp_cost_add;
    protected double mp_cost_mult = 1;
    protected double cast_speed_mult = 1;
    protected double delay_speed_mult = 1;

    protected boolean hidden = false;
    protected boolean smoked = false;
    protected boolean ambushing = false;
    protected Actor ambush_target = null;
    protected double charge;
    protected double def_break = 0;
    protected double res_break = 0;
    protected double mark = 0;
    protected double hp_regen = 0;
    protected double blessed = 0;
    public double cl_exp;
    public double ml_exp;
    public boolean lvling = false;
    protected int old_ml;
    protected int old_cl;
    public boolean counter_dodge = false;
    public boolean counter_heal = false;

    protected LinkedHashMap<String, PassiveSkill> passives = new LinkedHashMap<String, PassiveSkill>();
    protected LinkedHashMap<String, ActiveSkill> active_skills = new LinkedHashMap<String, ActiveSkill>();
    protected LinkedHashMap<String, Equipment> equipment = new LinkedHashMap<String, Equipment>();
    protected LinkedHashMap<String, EquipmentSet> sets = new LinkedHashMap<String, EquipmentSet>();

    protected PassiveSkill attackBoost = new PassiveSkill("Attack Boost", 0.2, 10, 0.1);
    protected PassiveSkill dropBoost = new PassiveSkill("Drop Boost", 0.15, 10, 0.1);
    protected PassiveSkill daggerMastery = new PassiveSkill("Dagger Mastery", 0.2, 0, 0);
    protected PassiveSkill stealth = new PassiveSkill("Stealth", 0.2, 15, 0.15);
    protected PassiveSkill speedBoost = new PassiveSkill("Speed Boost", 0.2, 5, 0.2);
    protected PassiveSkill poisonBoost = new PassiveSkill("Poison Boost", 0.5, 10, 0.1);
    protected PassiveSkill defenseBoost = new PassiveSkill("Defense Boost", 0.2, 5, 0.2);
    protected PassiveSkill dodge = new PassiveSkill("Dodge", 0.25, 10, 0.1);
    protected PassiveSkill fistMastery = new PassiveSkill("Fist Mastery", 0.2, 0, 0);

    protected PassiveSkill bowMastery = new PassiveSkill("Bow Mastery", 0.2, 0, 0);
    protected PassiveSkill ambush = new PassiveSkill("Ambush", 0.2, 15, 0.25);
    protected PassiveSkill hpRegen = new PassiveSkill("HP Regen", 0.02, 5, 0.2);
    protected PassiveSkill concentration = new PassiveSkill("Concentration", 0.3, 15, 0.15);
    protected PassiveSkill hitBoost = new PassiveSkill("Hit Boost", 0.2, 10, 0.1);
    protected PassiveSkill swordMastery = new PassiveSkill("Sword Mastery", 0.2, 0, 0);

    protected PassiveSkill intBoost = new PassiveSkill("Int Boost", 0.2, 5, 0.3);
    protected PassiveSkill resBoost = new PassiveSkill("Res Boost", 0.2, 5, 0.3);
    protected PassiveSkill wandMastery = new PassiveSkill("Wand Mastery", 0.2, 0, 0.0);
    protected PassiveSkill castBoost = new PassiveSkill("Casting Boost", 0.15, 10, 0.2);
    protected PassiveSkill fireBoost = new PassiveSkill("Fire Boost", 0.3, 10, 0.3);
    protected PassiveSkill fireResist = new PassiveSkill("Fire Resistance", 0.5, 10, 0.3);

    protected PassiveSkill bookMastery = new PassiveSkill("Book Mastery", 0.2, 0, 0.0);
    protected PassiveSkill ailmentRes = new PassiveSkill("Ailment Res", 0.75, 10, 0.2);

    protected ActiveSkill casting;
    protected ArrayList<ActiveSkill> enemy_skills = new ArrayList<ActiveSkill>();

    public ArrayList<Debuff> debuffs = new ArrayList<Debuff>();
    public ArrayList<Buff> buffs = new ArrayList<Buff>();
    public Zone zone = null;

    public void tick_debuffs() {
        smoked = false;
        def_break = 0;
        mark = 0;
        Iterator<Debuff> debuff_iterator = debuffs.iterator();
        while (debuff_iterator.hasNext()) {
            Debuff d = debuff_iterator.next();
            if (!Objects.equals(d.name, "Mark")) d.duration--;
            if (Objects.equals(d.name, "Smoke") && d.duration > 0) smoked = true;
            if (Objects.equals(d.name, "Defense Break") && d.duration > 0) def_break = d.effect;
            if (Objects.equals(d.name, "Res Break") && d.duration > 0) res_break = d.effect;
            if (Objects.equals(d.name, "Mark") && d.duration > 0) mark = d.effect;
            this.hp -= d.dmg;
//            if (d.dmg > 0) System.out.println(name + " taken dot dmg: " + (int) d.dmg);
            if (d.duration <= 0) debuff_iterator.remove();
        }
    }

    public void tick_buffs() {
        charge = 0;
        blessed = 0;
        Iterator<Buff> buff_iterator = buffs.iterator();
        while (buff_iterator.hasNext()) {
            Buff b = buff_iterator.next();
            if (Objects.equals(b.name, "Charge Up") && b.duration > 0) charge = b.effect;
            if (Objects.equals(b.name, "Bless") && b.duration > 0) blessed = b.effect;
            b.duration--;
            if (b.duration <= 0) buff_iterator.remove();
        }
    }

    public void remove_mark() {
        Iterator<Debuff> debuff_iterator = debuffs.iterator();
        while (debuff_iterator.hasNext()) {
            Debuff d = debuff_iterator.next();
            if (Objects.equals(d.name, "Mark")) d.duration--;
            if (d.duration <= 0) debuff_iterator.remove();
        }
    }

    public void enableSet(String bonus, Equipment.Quality quality, int upgrade) {
        double tier = quality.getMult();
        switch (bonus.toLowerCase()) {
            case "hit" -> set_hit = 1 + ((5 + upgrade / 2.0) * (0.5 + tier / 2.0)) / 100.0;
            case "magicdmg" -> set_magicdmg = 1 + ((5 + upgrade / 2.0) * (0.5 + tier / 2.0)) / 100.0;
            case "physdmg" -> set_physdmg = 1 + ((5 + upgrade / 2.0) * (0.5 + tier / 2.0)) / 100.0;
            case "mit1" -> set_mit1 = Math.clamp((5 + upgrade / 6.0) * (0.5 + tier / 2.0), 5, 50) / 100.0;
            case "mit2" -> set_mit2 = Math.clamp((10 + upgrade / 5.0) * (0.5 + tier / 2.0), 10, 55) / 100.0;
        }
    }

    public void disableSet() {
        set_hit = 1;
        set_magicdmg = 1;
        set_physdmg = 1;
        set_mit1 = 0;
        set_mit2 = 0;
        for (EquipmentSet set : sets.values()) {
            set.current_items = 0;
        }
    }

    public void checkAmbush() {
        if (ambush.enabled) {
            setAmbushing(true);
        }
    }

    public void enablePassives(String[] names) {
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
        fire_mult = 1;
        ailment_res = 1;
        exp_mult = 1;
        cast_speed_mult = 1;
        delay_speed_mult = 1;
        Set<String> keys = passives.keySet();
        for (String key : keys) {
            passives.get(key).enabled = false;
        }
        for (String name : names) {
            if (passives.containsKey(name)) {
                passives.get(name).enabled = true;
            }
        }
        poison_mult *= 1.0 + poisonBoost.bonus(passives);
        dmg_mult *= 1.0 + daggerMastery.bonus(passives);
        dmg_mult *= 1.0 + fistMastery.bonus(passives);
        dmg_mult *= 1.0 + swordMastery.bonus(passives);
        dmg_mult *= 1.0 + bowMastery.bonus(passives);
        dmg_mult *= 1.0 + wandMastery.bonus(passives);
        dmg_mult *= 1.0 + bookMastery.bonus(passives);
        dmg_mult *= 1.0 + concentration.bonus(passives);
        if (Main.game_version >= 1534) {
            dmg_mult *= 1.0 + stealth.bonus(passives);
            poison_mult *= 1.0 + stealth.bonus(passives);
        } else {
            atk_mult *= 1.0 + stealth.bonus(passives);
        }
        speed_mult *= 1.0 + speedBoost.bonus(passives);
        exp_mult *= 1.0 + dropBoost.bonus(passives);
        atk_mult *= 1.0 + attackBoost.bonus(passives);
        def_mult *= 1.0 + defenseBoost.bonus(passives);
        dodge_mult *= 1.0 + dodge.bonus(passives);
        hit_mult *= 1.0 + hitBoost.bonus(passives);
        hit_mult *= 1.0 + concentration.bonus(passives);
        int_mult *= 1.0 + intBoost.bonus(passives);
        res_mult *= 1.0 + resBoost.bonus(passives);
        ailment_res *= 1.0 + ailmentRes.bonus(passives);
        cast_speed_mult /= 1.0 + castBoost.bonus(passives);
        cast_speed_mult *= 1.0 + (concentration.bonus(passives) > 0 ? 0.25 : 0);
        delay_speed_mult *= 1.0 + (concentration.bonus(passives) > 0 ? 0.25 : 0);
        hp_regen = hpRegen.bonus(passives);
        refreshStats();
        if (fireResist.enabled) {
            add_resist("Fire", fireResist.bonus(passives));
        }
    }

    public void refreshStats() {
        mp_cost_add = 0;
        mp_cost_mult = 1;
        clear_gear_stats();
        for (Map.Entry<String, Equipment> slot : equipment.entrySet()) {
            Equipment item = slot.getValue();
            if (item.name != null && !item.name.equals("None")) {
                gear_atk += item.atk;
                gear_def += item.def;
                gear_hit += item.hit;
                gear_speed += item.speed;
                gear_int += item.intel;
                gear_res += item.resist;
                gear_water += item.water;
                gear_fire += item.fire;
                gear_earth += item.earth;
                gear_wind += item.wind;
                gear_light += item.light;
                gear_dark += item.dark;
                gear_crit += item.crit;
                gear_burn *= 1 + item.burn;
                add_resist("Fire", item.fire_res * 0.01);
                add_resist("Water", item.water_res * 0.01);
                add_resist("Wind", item.wind_res * 0.01);
                add_resist("Earth", item.earth_res * 0.01);
                add_resist("Light", item.light_res * 0.01);
                add_resist("Dark", item.dark_res * 0.01);
                add_resist("Magic", item.magic_res * 0.01);
                add_resist("Physical", item.phys_res * 0.01);
                String set_type = item.displayName;
                if (set_type.equals("Blazing")) set_type = "Cloth";
                if (set_type.equals("Windy")) set_type = "Leather";
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
        for (Map.Entry<String, PassiveSkill> passive : passives.entrySet()) {
            if (passive.getValue().enabled) {
                mp_cost_add += passive.getValue().mp_add;
                mp_cost_mult *= 1 + passive.getValue().mp_mult;
            }
        }
        atk = (base_atk + gear_atk) * getAtk_mult();
        def = (base_def + gear_def) * getDef_mult();
        intel = (base_int + gear_int) * getInt_mult();
        resist = (base_res + gear_res) * getRes_mult();
        hit = (base_hit + gear_hit) * getHit_mult() * set_hit;
        speed = (base_speed + gear_speed) * getSpeed_mult();
        hp_max = (base_hp_max) * getHp_mult();
        mp_max = (resist * 3 + intel) * getMp_mult();
        hp = hp_max;
        mp = mp_max;
        burn = gear_burn;
        if (set_mit1 > 0) add_resist("All", set_mit1);
        if (set_mit2 > 0) add_resist("All", set_mit2);
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
        gear_water = 0;
        gear_fire = 0;
        gear_earth = 0;
        gear_wind = 0;
        gear_light = 0;
        gear_dark = 0;
        gear_crit = 0;
        gear_burn = 1;
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
        for (ActiveSkill skill : enemy_skills) {
            if (skill != null) {
                skill.clear_recorded_data();
            }
        }
        for (ActiveSkill skill : active_skills.values()) {
            if (skill != null) {
                skill.clear_recorded_data();
            }
        }
    }

    public void setCLML(int cl, int ml) {
    }

    public void reroll() {
    }

    public double getPrepare_hps() {
        return getHp_max() * (0.011 + 0.000225 * prepare.lvl);
    }

    public double getPrepare_mps() {
        return getMp_regen() + getMp_max() * (0.0056 + 0.0001 * prepare.lvl);
    }

    public double getMp_regen() {
        return getMp_max() / 360;
    }

    public double getHp_max() {
        return hp_max;
    }

    public void setHp_max(double hp_max) {
        this.hp_max = hp_max;
    }

    public double getHp() {
        return hp;
    }

    public void setHp(double hp) {
        this.hp = Math.min(getHp_max(), hp);
    }

    public double getMp_max() {
        return getResist() * 3 + getIntel();
    }

    public void setMp_max(double mp_max) {
        this.mp_max = mp_max;
    }

    public double getMp() {
        return mp;
    }

    public void setMp(double mp) {
        this.mp = Math.min(getMp_max(), mp);
    }

    public double getAtk() {
        return atk + (base_atk + gear_atk) * blessed;
    }

    public void setAtk(double atk) {
        this.atk = atk;
    }

    public double getDef() {
        return def * (1.0 - def_break) * (1.0 - mark) + (base_def + gear_def) * blessed;
    }

    public void setDef(double def) {
        this.def = def;
    }

    public double getIntel() {
        return intel + (base_int + gear_int) * blessed;
    }

    public void setIntel(double intel) {
        this.intel = intel;
    }

    public double getResist() {
        return resist * (1.0 - mark) * (1.0 - res_break) + (base_res + gear_res) * blessed;
    }

    public void setResist(double resist) {
        this.resist = resist;
    }

    public double getHit() {
        return hit;
    }

    public void setHit(double hit) {
        this.hit = hit;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDark() {
        return dark;
    }

    public void setDark(double dark) {
        this.dark = dark;
    }

    public double getWater() {
        return water;
    }

    public void setWater(double water) {
        this.water = water;
    }

    public double getFire() {
        return fire;
    }

    public void setFire(double fire) {
        this.fire = fire;
    }

    public double getWind() {
        return wind;
    }

    public void setWind(double wind) {
        this.wind = wind;
    }

    public double getEarth() {
        return earth;
    }

    public void setEarth(double earth) {
        this.earth = earth;
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

    public void setWater_res(double water_res) {
        this.water_res = water_res;
    }

    public double getFire_res() {
        return fire_res;
    }

    public void setFire_res(double fire_res) {
        this.fire_res = fire_res;
    }

    public double getWind_res() {
        return wind_res;
    }

    public void setWind_res(double wind_res) {
        this.wind_res = wind_res;
    }

    public double getEarth_res() {
        return earth_res;
    }

    public void setEarth_res(double earth_res) {
        this.earth_res = earth_res;
    }

    public double getLight_res() {
        return light_res;
    }

    public void setLight_res(double light_res) {
        this.light_res = light_res;
    }

    public double getDark_res() {
        return dark_res;
    }

    public void setDark_res(double dark_res) {
        this.dark_res = dark_res;
    }

    public double getPhys_res() {
        return phys_res;
    }

    public void setPhys_res(double phys_res) {
        this.phys_res = phys_res;
    }

    public double getMagic_res() {
        return magic_res;
    }

    public void setMagic_res(double magic_res) {
        this.magic_res = magic_res;
    }

    public double getDmg_mult() {
        double mult = 1.0;
        mult *= 1.0 + charge;
        return dmg_mult * mult;
    }

    public void setDmg_mult(double dmg_mult) {
        this.dmg_mult = dmg_mult;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public double stealthDelay() {
        if (Main.game_version >= 1534) {
            return stealth.enabled ? 1.2 * (1 + 0.02 * stealth.lvl) : 0;
        } else {
            return stealth.enabled ? 1 : 0;
        }
    }

    public boolean isPoison_boost() {
        return poisonBoost.enabled;
    }

    public double getHp_mult() {
        return hp_mult;
    }

    public void setHp_mult(double hp_mult) {
        this.hp_mult = hp_mult;
    }

    public double getMp_mult() {
        return mp_mult;
    }

    public void setMp_mult(double mp_mult) {
        this.mp_mult = mp_mult;
    }

    public double getAtk_mult() {
        return atk_mult;
    }

    public void setAtk_mult(double atk_mult) {
        this.atk_mult = atk_mult;
    }

    public double getInt_mult() {
        return int_mult;
    }

    public void setInt_mult(double int_mult) {
        this.int_mult = int_mult;
    }

    public double getDef_mult() {
        return def_mult;
    }

    public void setDef_mult(double def_mult) {
        this.def_mult = def_mult;
    }

    public double getRes_mult() {
        return res_mult;
    }

    public void setRes_mult(double res_mult) {
        this.res_mult = res_mult;
    }

    public double getHit_mult() {
        return hit_mult;
    }

    public void setHit_mult(double hit_mult) {
        this.hit_mult = hit_mult;
    }

    public double getSpeed_mult() {
        return speed_mult;
    }

    public void setSpeed_mult(double speed_mult) {
        this.speed_mult = speed_mult;
    }

    public double getPoison_mult() {
        return poison_mult;
    }

    public void setPoison_mult(double poison_mult) {
        this.poison_mult = poison_mult;
    }

    public double getFire_mult() {
        return fire_mult;
    }

    public void setFire_mult(double fire_mult) {
        this.fire_mult = fire_mult;
    }

    public double getAilment_res() {
        return ailment_res;
    }

    public void setAilment_res(double ailment_res) {
        this.ailment_res = ailment_res;
    }

    public boolean isSmoked() {
        return smoked;
    }

    public void setSmoked(boolean smoked) {
        this.smoked = smoked;
    }

    public double getDef_break() {
        return def_break;
    }

    public void setDef_break(double def_break) {
        this.def_break = def_break;
    }

    public double getExp() {
        return exp;
    }

    public void setExp(double exp) {
        this.exp = exp;
    }

    public double getExp_mult() {
        return exp_mult;
    }

    public void setExp_mult(double exp_mult) {
        this.exp_mult = exp_mult;
    }

    public ActiveSkill getCasting() {
        return casting;
    }

    public void setCasting(ActiveSkill casting) {
        this.casting = casting;
    }

    public double getMp_cost_add() {
        return mp_cost_add;
    }

    public void setMp_cost_add(double mp_cost_add) {
        this.mp_cost_add = mp_cost_add;
    }

    public double getMp_cost_mult() {
        return mp_cost_mult;
    }

    public void setMp_cost_mult(double mp_cost_mult) {
        this.mp_cost_mult = mp_cost_mult;
    }

    public double getCast_speed_mult() {
        return cast_speed_mult;
    }

    public void setCast_speed_mult(double cast_speed_mult) {
        this.cast_speed_mult = cast_speed_mult;
    }

    public boolean isAmbushing() {
        return ambushing;
    }

    public void setAmbushing(boolean ambushing) {
        this.ambushing = ambushing;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSet_magicdmg() {
        return set_magicdmg;
    }

    public void setSet_magicdmg(double set_magicdmg) {
        this.set_magicdmg = set_magicdmg;
    }

    public double getSet_physdmg() {
        return set_physdmg;
    }

    public void setSet_physdmg(double set_physdmg) {
        this.set_physdmg = set_physdmg;
    }

    public double getSet_mit1() {
        return set_mit1;
    }

    public void setSet_mit1(double set_mit1) {
        this.set_mit1 = set_mit1;
    }

    public double getSet_mit2() {
        return set_mit2;
    }

    public void setSet_mit2(double set_mit2) {
        this.set_mit2 = set_mit2;
    }

    public int getMl() {
        return ml;
    }

    public void setMl(int ml) {
        this.ml = ml;
    }

    public int getCl() {
        return cl;
    }

    public void setCl(int cl) {
        this.cl = cl;
    }

    public double getDelay_speed_mult() {
        return delay_speed_mult;
    }

    public void setDelay_speed_mult(double delay_speed_mult) {
        this.delay_speed_mult = delay_speed_mult;
    }

    public double getDodge_mult() {
        return dodge_mult * (1.0 - mark);
    }

    public void setDodge_mult(double dodge_mult) {
        this.dodge_mult = dodge_mult;
    }
}
