package Disilon;

public class PassiveSkill {
    public ActorStats owner;
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
    public boolean available;
    public double exp;
    public double old_lvl;

    public PassiveSkill(ActorStats owner, String name, double base_bonus, double base_mp_add, double base_mp_mult) {
        this.owner = owner;
        this.name = name;
        this.base_bonus = base_bonus;
        this.base_mp_add = base_mp_add;
        this.base_mp_mult = base_mp_mult;
        if (name.equals("Core Boost")) {
            base_bonus2 = 0.125;
        }
    }

    public double getLvl() {
        double fraction = exp / need_for_lvl(lvl);
        return lvl + fraction;
    }

    public double getLpercent() {
        return exp / need_for_lvl(lvl) * 100;
    }

    public void setLvl(double lvl) {
        setLvl((int) lvl);
        double next_lvl_exp = need_for_lvl((int) lvl);
        exp = next_lvl_exp * (lvl - (int) lvl);
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
        switch (name) {
            case "Fire Resist", "Earth Resist" -> {
                this.bonus = this.base_bonus * (1 + 0.03 * lvl);
                this.mp_add = this.base_mp_add * (1 + 0.03 * lvl);
                this.mp_mult = this.base_mp_mult * (1 + 0.03 * lvl);
            }
            case "Multi Arrows" -> {
                this.bonus = this.base_bonus * (1 + 0.03 * lvl);
                this.mp_add = this.base_mp_add * (1 + 0.02 * lvl);
                this.mp_mult = this.base_mp_mult * (1 + 0.02 * lvl);
            }
            case "Bless Mastery" -> {
                this.bonus = this.base_bonus * (1 + 0.04 * lvl);
                this.mp_add = this.base_mp_add * (1 + 0.02 * lvl);
                this.mp_mult = this.base_mp_mult * (1 + 0.02 * lvl);
                this.bonus2 = Main.game_version >= 1605 ? 1 + (int) (lvl / 10) : 1;
            }
            case "Core Boost" -> {
                if (Main.game_version >= 1638) {
                    this.base_bonus = 0.75;
                    this.bonus = this.base_bonus * (1 + 0.02 * lvl);
                } else {
                    if (Main.game_version >= 1573) {
                        this.base_bonus = 0.6;
                        this.bonus = this.base_bonus * (1 + 0.02 * lvl);
                    } else {
                        this.bonus = this.base_bonus * (1 + 0.05 * lvl);
                        this.bonus2 = this.base_bonus2 * (1 + 0.05 * lvl);
                    }
                }
                this.mp_add = this.base_mp_add * (1 + 0.02 * lvl);
                this.mp_mult = this.base_mp_mult * (1 + 0.02 * lvl);
            }
            case "HP Boost" -> {
                if (Main.game_version >= 1580) {
                    this.base_mp_mult = 0.1;
                } else {
                    this.base_mp_mult = 0.2;
                }
                this.bonus = this.base_bonus * (1 + 0.02 * lvl);
                this.bonus2 = this.base_bonus2 * (1 + 0.02 * lvl);
                this.mp_add = this.base_mp_add * (1 + 0.02 * lvl);
                this.mp_mult = this.base_mp_mult * (1 + 0.02 * lvl);
            }
            case "Counter Strike" -> {
                if (Main.game_version >= 1580) {
                    this.base_mp_mult = 0;
                    this.base_mp_add = 0;
                    this.base_bonus = 0.3;
                } else {
                    this.base_mp_mult = 0.25;
                    this.base_mp_add = 15;
                    this.base_bonus = 0.25;
                }
                this.bonus = this.base_bonus * (1 + 0.02 * lvl);
                this.bonus2 = this.base_bonus2 * (1 + 0.02 * lvl);
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

    public double getBonus() {
        return enabled ? bonus : 0;
    }

    public double getBonus2() {
        return enabled ? bonus2 : 0;
    }

    public void gainExp(double time) {
        if (enabled && !name.equals("Tsury Finke")) {
            exp += time;
            int need = need_for_lvl(lvl);
            if (exp >= need && lvl < owner.max_skill_lvl) {
                lvl++;
                exp -= need;
                setLvl(lvl);
            }
        }
    }

    public void gainExpTF(double gain) {
        if (name.equals("Tsury Finke")) {
            exp += gain;
            int need = need_for_lvl(lvl);
            if (exp >= need && lvl < 100) {
                lvl++;
                exp -= need;
//                setLvl(lvl);
            }
        }
    }

    public int need_for_lvl(int lvl) {
        if (name.equals("Tsury Finke")) {
            return 10000 * (1 + lvl);
        }
        return (int) ((Math.pow(Math.max(lvl, 1), 2)) * 3000);
    }
}
