package Disilon;

public enum SkillMod {
    Basic, Pow, Hit, Cheap, Fast, PowPow, Damage, SlowHit, Enemy;
    public static SkillMod[] getAvailableMods() {
        return new SkillMod[]{Basic, Pow, Hit, Cheap, Fast, Damage, SlowHit};
    }
}