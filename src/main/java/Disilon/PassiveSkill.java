package Disilon;

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

    public PassiveSkill(String name, double base_bonus, double base_mp_add, double base_mp_mult) {
        this.name = name;
        this.base_bonus = base_bonus;
        this.base_mp_add = base_mp_add;
        this.base_mp_mult = base_mp_mult;
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

    public double bonus() {
        return enabled ? bonus : 0;
    }
}
