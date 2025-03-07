import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

    protected double base_hp_max;
    protected double base_atk;
    protected double base_def;
    protected double base_int;
    protected double base_res;
    protected double base_hit;
    protected double base_speed;

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
    protected double burn_mult = 1;
    protected double fire_mult = 1;
    protected double ailment_res = 1;
    protected int prepare_lvl;
    protected double exp;
    protected double exp_mult = 1;
    protected double mp_cost_add;
    protected double mp_cost_mult = 1;
    protected double cast_speed_mult = 1;
    protected double delay_speed_mult = 1;

    protected boolean hidden = false;
    protected boolean smoked = false;
    protected boolean ambushing = false;
    protected double charge;
    protected double def_break = 0;
    protected double mark = 0;
    protected double hp_regen = 0;
    protected double blessed = 0;
    public double cl_exp;
    public double ml_exp;
    public boolean lvling = false;
    public boolean counter_dodge = false;

    protected HashMap<String, PassiveSkill> passives = new HashMap<String, PassiveSkill>();

    protected PassiveSkill attackBoost = new PassiveSkill("Attack Boost", 0.2, 10, 0.1);
    protected PassiveSkill dropBoost = new PassiveSkill("Drop Boost", 0.15, 10, 0.1);
    protected PassiveSkill daggerMastery = new PassiveSkill("Dagger Mastery", 0.2, 0, 0);
    protected PassiveSkill stealth = new PassiveSkill("Stealth", 0.2, 15, 0.15);
    protected PassiveSkill speedBoost = new PassiveSkill("Speed Boost", 0.2, 5, 0.2);
    protected PassiveSkill poisonBoost = new PassiveSkill("Poison Boost", 0.5, 10, 0.1);
    protected PassiveSkill defenseBoost = new PassiveSkill("Defense Boost", 0.2, 5, 0.2);
    protected PassiveSkill dodge = new PassiveSkill("Dodge", 0.25, 10, 0.1);

    protected PassiveSkill bowMastery = new PassiveSkill("Bow Mastery", 0.2, 0, 0);
    protected PassiveSkill ambush = new PassiveSkill("Ambush", 0.2, 15, 0.25);
    protected PassiveSkill hpRegen = new PassiveSkill("HP Regen", 0.02, 5, 0.2);
    protected PassiveSkill concentration = new PassiveSkill("Concentration", 0.3, 15, 0.15);
    protected PassiveSkill hitBoost = new PassiveSkill("Hit Boost", 0.2, 10, 0.1);

    protected PassiveSkill intBoost = new PassiveSkill("Int Boost", 0.2, 5, 0.3);
    protected PassiveSkill resBoost = new PassiveSkill("Res Boost", 0.2, 5, 0.3);
    protected PassiveSkill wandMastery = new PassiveSkill("Wand Mastery", 0.2, 0, 0.0);
    protected PassiveSkill castBoost = new PassiveSkill("Casting Boost", 0.15, 10, 0.2);
    protected PassiveSkill fireBoost = new PassiveSkill("Fire Boost", 0.3, 10, 0.3);
    protected PassiveSkill fireResist = new PassiveSkill("Fire Resistance", 0.5, 10, 0.3);

    protected PassiveSkill bookMastery = new PassiveSkill("Book Mastery", 0.2, 0, 0.0);
    protected PassiveSkill ailmentRes = new PassiveSkill("Ailment Res", 0.75, 10, 0.2);

    protected ActiveSkill casting;
    protected ArrayList<ActiveSkill> skills = new ArrayList<ActiveSkill>();

    public ArrayList<Debuff> debuffs = new ArrayList<Debuff>();
    public ArrayList<Buff> buffs = new ArrayList<Buff>();

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

    public void enableSet(String bonus, String quality, int upgrade) {
        double tier = switch (quality.toLowerCase()) {
            case "poor" -> 0.5;
            case "flawed" -> 0.75;
            case "normal" -> 1;
            case "good" -> 1.25;
            case "superior" -> 1.5;
            case "exceptional" -> 2;
            case "divine" -> 2.5;
            case "legendary" -> 3;
            case "mythic" -> 4;
            case "godly" -> 5;
            default -> 1;
        };
        switch (bonus.toLowerCase()) {
            case "hit" -> set_hit = 1 + ((5 + upgrade / 2.0) * (0.5 + tier / 2.0)) / 100.0;
            case "magicdmg" -> set_magicdmg = 1 + ((5 + upgrade / 2.0) * (0.5 + tier / 2.0)) / 100.0;
            case "physdmg" -> set_physdmg = 1 + ((5 + upgrade / 2.0) * (0.5 + tier / 2.0)) / 100.0;
            case "mit1" -> set_mit1 = 1 + Math.clamp((5 + upgrade / 6.0) * (0.5 + tier / 2.0), 5, 50) / 100.0;
            case "mit2" -> set_mit2 = 1 + Math.clamp((10 + upgrade / 5.0) * (0.5 + tier / 2.0), 10, 55) / 100.0;
        }
    }

    public void disableSet(String bonus) {
        switch (bonus.toLowerCase()) {
            case "hit":
                set_hit = 1;
                break;
            case "magicdmg":
                set_magicdmg = 1;
                break;
            case "physdmg":
                set_physdmg = 1;
                break;
            case "mit1":
                set_mit1 = 1;
                break;
            case "mit2":
                set_mit2 = 1;
                break;
        }
    }

    public void disableSet() {
        set_hit = 1;
        set_magicdmg = 1;
        set_physdmg = 1;
        set_mit1 = 1;
        set_mit2 = 1;
    }

    public void add_crit(double chance) {
        gear_crit += chance;
    }

    public void checkAmbush() {
        if (ambush.enabled) {
            setAmbushing(true);
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
        poison_mult *= 1.0 + (poisonBoost.enabled ? poisonBoost.bonus : 0);
        dmg_mult *= 1.0 + (daggerMastery.enabled ? daggerMastery.bonus : 0);
        dmg_mult *= 1.0 + (bowMastery.enabled ? bowMastery.bonus : 0);
        dmg_mult *= 1.0 + (wandMastery.enabled ? wandMastery.bonus : 0);
        dmg_mult *= 1.0 + (bookMastery.enabled ? bookMastery.bonus : 0);
        dmg_mult *= 1.0 + (concentration.enabled ? concentration.bonus : 0);
        if (Main.game_version >= 1534) {
            dmg_mult *= 1.0 + (stealth.enabled ? stealth.bonus : 0);
            poison_mult *= 1.0 + (stealth.enabled ? stealth.bonus : 0);
        } else {
            atk_mult *= 1.0 + (stealth.enabled ? stealth.bonus : 0);
        }
        speed_mult *= 1.0 + (speedBoost.enabled ? speedBoost.bonus : 0);
        exp_mult *= 1.0 + (dropBoost.enabled ? dropBoost.bonus : 0);
        atk_mult *= 1.0 + (attackBoost.enabled ? attackBoost.bonus : 0);
        def_mult *= 1.0 + (defenseBoost.enabled ? defenseBoost.bonus : 0);
        dodge_mult *= 1.0 + (dodge.enabled ? dodge.bonus : 0);
        hit_mult *= 1.0 + (hitBoost.enabled ? hitBoost.bonus : 0);
        hit_mult *= 1.0 + (concentration.enabled ? concentration.bonus : 0);
        int_mult *= 1.0 + (intBoost.enabled ? intBoost.bonus : 0);
        res_mult *= 1.0 + (resBoost.enabled ? resBoost.bonus : 0);
        ailment_res *= 1.0 + (ailmentRes.enabled ? ailmentRes.bonus : 0);
        cast_speed_mult /= 1.0 + (castBoost.enabled ? castBoost.bonus : 0);
        cast_speed_mult *= 1.0 + (concentration.enabled ? 0.25 : 0);
        delay_speed_mult *= 1.0 + (concentration.enabled ? 0.25 : 0);
        hp_regen = hpRegen.enabled ? hpRegen.bonus : 0;
        refreshStats();
    }

    public void refreshStats() {
        mp_cost_add = 0;
        mp_cost_mult = 1;
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
    }

    public void add_stats(double atk, double def, double intel, double resist, double hit, double speed) {
        gear_atk += atk;
        gear_def += def;
        gear_int += intel;
        gear_res += resist;
        gear_hit += hit;
        gear_speed += speed;
    }

    public void clear_gear_stats() {
        gear_atk = 0;
        gear_def = 0;
        gear_int = 0;
        gear_res = 0;
        gear_hit = 0;
        gear_speed = 0;
        gear_wind = 0;
        gear_light = 0;
        gear_dark = 0;
        gear_fire = 0;
        gear_water = 0;
        gear_earth = 0;
        disableSet();
    }

    public void increment_exp(double exp) {
        cl_exp += exp;
        ml_exp += exp;
        double need_cl = exp_to_cl(cl, 3);
        double need_ml = exp_to_ml(ml);
        if (cl_exp >= need_cl && cl < 100) {
            cl += 1;
            cl_exp -= need_cl;
            setCLML(cl, ml);
        }
        if (ml_exp >= need_ml) {
            ml += 1;
            ml_exp -= need_ml;
            setCLML(cl, ml);
        }
    }

    public double exp_to_cl(int lvl, int tier) {
        return (Math.pow(lvl, 5) / 340 + Math.pow(lvl, 2) * 50 + 10) * Math.pow(2, tier - 1);
    }

    public double exp_to_ml(int lvl) {
        return (Math.pow(lvl, 4) / 10 + Math.pow(lvl, 1.9) * 40 + 10);
    }

    public void setCLML(int cl, int ml) {
    }

    public void reroll() {
    }

    public double getPrepare_hps() {
        return getHp_max() * (0.011 + 0.000225 * prepare_lvl);
    }

    public double getPrepare_mps() {
        return getMp_regen() + getMp_max() * (0.0056 + 0.0001 * prepare_lvl);
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
        return atk * (1 + blessed);
    }

    public void setAtk(double atk) {
        this.atk = atk;
    }

    public double getDef() {
        return def * (1.0 - def_break) * (1.0 - mark) * (1 + blessed);
    }

    public void setDef(double def) {
        this.def = def;
    }

    public double getIntel() {
        return intel * (1 + blessed);
    }

    public void setIntel(double intel) {
        this.intel = intel;
    }

    public double getResist() {
        return resist * (1.0 - mark) * (1 + blessed);
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
        mult *= 1.0 + (ambushing ? ambush.bonus : 0);
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

    public int getPrepare_lvl() {
        return prepare_lvl;
    }

    public void setPrepare_lvl(int prepare_lvl) {
        this.prepare_lvl = prepare_lvl;
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
