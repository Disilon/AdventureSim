package Disilon;

public class Debuff {
    public int duration;
    public double dmg;
    public double effect;
    public String name;

    public Debuff(String name, int duration, double dmg) {
        this.name = name;
        this.duration = duration;
        this.dmg = dmg;
    }

    public Debuff(String name, int duration, double dmg, double effect) {
        this.name = name;
        this.duration = duration;
        this.dmg = dmg;
        this.effect = effect;
    }

    public double getMaxTotalDmg() {
        return dmg * duration;
    }
}
