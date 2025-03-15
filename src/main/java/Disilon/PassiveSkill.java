package Disilon;

import java.util.Map;

public class PassiveSkill {
    public int lvl;
    public String name;
    public double base_bonus;
    public double base_mp_add;
    public double base_mp_mult;
    public double bonus;
    public double mp_add;
    public double mp_mult;
    public boolean enabled;
    public double exp;
    public double old_lvl;

    public PassiveSkill(String name, double base_bonus, double base_mp_add, double base_mp_mult) {
        this.name = name;
        this.base_bonus = base_bonus;
        this.base_mp_add = base_mp_add;
        this.base_mp_mult = base_mp_mult;
    }

    public void setLvl(double lvl) {
        setLvl((int) lvl);
        double next_lvl_exp = need_for_lvl((int) lvl);
        exp = next_lvl_exp * (lvl - (int) lvl);
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
        if (name.equals("Fire Resist")) {
            this.bonus = this.base_bonus * (1 + 0.03 * lvl);
            this.mp_add = this.base_mp_add * (1 + 0.03 * lvl);
            this.mp_mult = this.base_mp_mult * (1 + 0.03 * lvl);
        } else {
            this.bonus = this.base_bonus * (1 + 0.02 * lvl);
            this.mp_add = this.base_mp_add * (1 + 0.02 * lvl);
            this.mp_mult = this.base_mp_mult * (1 + 0.02 * lvl);
        }
    }

    public double bonus(Map passives) {
        if (!passives.containsValue(this)) {
            return 0;
        }
        return enabled ? bonus : 0;
    }

    public void gainExp(double time) {
        if (enabled) {
            exp += time;
            int need = need_for_lvl(lvl);
            if (exp >= need && lvl < 20) {
                lvl++;
                exp -= need;
                setLvl(lvl);
            }
        } else {
            lvl = (int) old_lvl;
        }
    }

    public int need_for_lvl(int lvl) {
        return (int) ((Math.pow(lvl, 2)) * 3000);
    }
}
