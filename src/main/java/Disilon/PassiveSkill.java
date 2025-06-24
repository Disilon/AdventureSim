package Disilon;

import java.util.Map;

public class PassiveSkill {
    public int lvl;
    public String name;
    public double base_bonus;
    public double base_bonus2;
    public double base_mp_add;
    public double base_mp_mult;
    public double bonus;
    public double bonus2;
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

    public double getLvl() {
        double fraction = exp / need_for_lvl(lvl);
        return lvl + fraction;
    }

    public void setLvl(double lvl) {
        setLvl((int) lvl);
        double next_lvl_exp = need_for_lvl((int) lvl);
        exp = next_lvl_exp * (lvl - (int) lvl);
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
        switch (name) {
            case "Fire Resist" -> {
                this.bonus = this.base_bonus * (1 + 0.03 * lvl);
                this.mp_add = this.base_mp_add * (1 + 0.03 * lvl);
                this.mp_mult = this.base_mp_mult * (1 + 0.03 * lvl);
            }
            case "Multi Arrows" -> {
                this.bonus = this.base_bonus * (1 + 0.03 * lvl);
                this.mp_add = this.base_mp_add * (1 + 0.02 * lvl);
                this.mp_mult = this.base_mp_mult * (1 + 0.02 * lvl);
            }
            case "Core Boost" -> {
                this.bonus = this.base_bonus * (1 + 0.05 * lvl);
                this.bonus2 = this.base_bonus2 * (1 + 0.05 * lvl);
                this.mp_add = this.base_mp_add * (1 + 0.02 * lvl);
                this.mp_mult = this.base_mp_mult * (1 + 0.02 * lvl);
            }
            default -> {
                this.bonus = this.base_bonus * (1 + 0.02 * lvl);
                this.bonus2 = this.base_bonus2 * (1 + 0.02 * lvl);
                this.mp_add = this.base_mp_add * (1 + 0.02 * lvl);
                this.mp_mult = this.base_mp_mult * (1 + 0.02 * lvl);
            }
        }
    }

    public double bonus(Map passives) {
        if (!passives.containsValue(this)) {
            return 0;
        }
        return enabled ? bonus : 0;
    }

    public double bonus2(Map passives) {
        if (!passives.containsValue(this)) {
            return 0;
        }
        return enabled ? bonus2 : 0;
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
        return (int) ((Math.pow(Math.max(lvl, 1), 2)) * 3000);
    }
}
