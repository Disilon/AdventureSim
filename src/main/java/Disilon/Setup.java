package Disilon;

import java.io.Serializable;

public class Setup implements Serializable {
    String playerclass;
    int ml;
    int cl;
    String potion1;
    int potion1_t;
    String potion2;
    int potion2_t;
    String potion3;
    int potion3_t;
    String skill1;
    String skill2;
    String skill3;
    SkillMod skill1_mod;
    SkillMod skill2_mod;
    SkillMod skill3_mod;
    int skill1_s;
    int skill2_s;
    int skill3_s;
    String pskill1;
    String pskill2;
    String pskill3;
    String mh_name;
    String mh_tier;
    int mh_lvl;
    String oh_name;
    String oh_tier;
    int oh_lvl;
    String helmet_name;
    String helmet_tier;
    int helmet_lvl;
    String chest_name;
    String chest_tier;
    int chest_lvl;
    String pants_name;
    String pants_tier;
    int pants_lvl;
    String bracer_name;
    String bracer_tier;
    int bracer_lvl;
    String boots_name;
    String boots_tier;
    int boots_lvl;
    String accessory1_name;
    int accessory1_lvl;
    String accessory1_tier;
    String accessory2_name;
    String accessory2_tier;
    int accessory2_lvl;
    String necklace_name;
    String necklace_tier;
    int necklace_lvl;
    String result_full;
    String result_essential;
    String stats;
    String enemy;
    Zone zone;
    int skill1_lvl;
    int skill2_lvl;
    int skill3_lvl;
    int pskill1_lvl;
    int pskill2_lvl;
    int pskill3_lvl;
    int simulations;
    String gameversion;
    int reroll;
    double milestone;
    int crafting_lvl;
    int alchemy_lvl;
    boolean setsetup;
    boolean setupinfo;

    public void setup() {
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public String getPlayerclass() {
        return playerclass;
    }

    public void setPlayerclass(String playerclass) {
        this.playerclass = playerclass;
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

    public String getPotion1() {
        return potion1;
    }

    public void setPotion1(String potion1) {
        this.potion1 = potion1;
    }

    public int getPotion1_t() {
        return potion1_t;
    }

    public void setPotion1_t(int potion1_t) {
        this.potion1_t = potion1_t;
    }

    public String getPotion2() {
        return potion2;
    }

    public void setPotion2(String potion2) {
        this.potion2 = potion2;
    }

    public int getPotion2_t() {
        return potion2_t;
    }

    public void setPotion2_t(int potion2_t) {
        this.potion2_t = potion2_t;
    }

    public String getPotion3() {
        return potion3;
    }

    public void setPotion3(String potion3) {
        this.potion3 = potion3;
    }

    public int getPotion3_t() {
        return potion3_t;
    }

    public void setPotion3_t(int potion3_t) {
        this.potion3_t = potion3_t;
    }

    public String getSkill1() {
        return skill1;
    }

    public void setSkill1(String skill1) {
        this.skill1 = skill1;
    }

    public String getSkill2() {
        return skill2;
    }

    public void setSkill2(String skill2) {
        this.skill2 = skill2;
    }

    public String getSkill3() {
        return skill3;
    }

    public void setSkill3(String skill3) {
        this.skill3 = skill3;
    }

    public SkillMod getSkill1_mod() {
        return skill1_mod;
    }

    public void setSkill1_mod(SkillMod skill1_mod) {
        this.skill1_mod = skill1_mod;
    }

    public SkillMod getSkill2_mod() {
        return skill2_mod;
    }

    public void setSkill2_mod(SkillMod skill2_mod) {
        this.skill2_mod = skill2_mod;
    }

    public SkillMod getSkill3_mod() {
        return skill3_mod;
    }

    public void setSkill3_mod(SkillMod skill3_mod) {
        this.skill3_mod = skill3_mod;
    }

    public int getSkill1_s() {
        return skill1_s;
    }

    public void setSkill1_s(int skill1_s) {
        this.skill1_s = skill1_s;
    }

    public int getSkill2_s() {
        return skill2_s;
    }

    public void setSkill2_s(int skill2_s) {
        this.skill2_s = skill2_s;
    }

    public int getSkill3_s() {
        return skill3_s;
    }

    public void setSkill3_s(int skill3_s) {
        this.skill3_s = skill3_s;
    }

    public String getPskill1() {
        return pskill1;
    }

    public void setPskill1(String pskill1) {
        this.pskill1 = pskill1;
    }

    public String getPskill2() {
        return pskill2;
    }

    public void setPskill2(String pskill2) {
        this.pskill2 = pskill2;
    }

    public String getPskill3() {
        return pskill3;
    }

    public void setPskill3(String pskill3) {
        this.pskill3 = pskill3;
    }

    public String getMh_name() {
        return mh_name;
    }

    public void setMh_name(String mh_name) {
        this.mh_name = mh_name;
    }

    public String getMh_tier() {
        return mh_tier;
    }

    public void setMh_tier(String mh_tier) {
        this.mh_tier = mh_tier;
    }

    public int getMh_lvl() {
        return mh_lvl;
    }

    public void setMh_lvl(int mh_lvl) {
        this.mh_lvl = mh_lvl;
    }

    public String getOh_name() {
        return oh_name;
    }

    public void setOh_name(String oh_name) {
        this.oh_name = oh_name;
    }

    public String getOh_tier() {
        return oh_tier;
    }

    public void setOh_tier(String oh_tier) {
        this.oh_tier = oh_tier;
    }

    public int getOh_lvl() {
        return oh_lvl;
    }

    public void setOh_lvl(int oh_lvl) {
        this.oh_lvl = oh_lvl;
    }

    public String getHelmet_name() {
        return helmet_name;
    }

    public void setHelmet_name(String helmet_name) {
        this.helmet_name = helmet_name;
    }

    public String getHelmet_tier() {
        return helmet_tier;
    }

    public void setHelmet_tier(String helmet_tier) {
        this.helmet_tier = helmet_tier;
    }

    public int getHelmet_lvl() {
        return helmet_lvl;
    }

    public void setHelmet_lvl(int helmet_lvl) {
        this.helmet_lvl = helmet_lvl;
    }

    public String getChest_name() {
        return chest_name;
    }

    public void setChest_name(String chest_name) {
        this.chest_name = chest_name;
    }

    public String getChest_tier() {
        return chest_tier;
    }

    public void setChest_tier(String chest_tier) {
        this.chest_tier = chest_tier;
    }

    public int getChest_lvl() {
        return chest_lvl;
    }

    public void setChest_lvl(int chest_lvl) {
        this.chest_lvl = chest_lvl;
    }

    public String getPants_name() {
        return pants_name;
    }

    public void setPants_name(String pants_name) {
        this.pants_name = pants_name;
    }

    public String getPants_tier() {
        return pants_tier;
    }

    public void setPants_tier(String pants_tier) {
        this.pants_tier = pants_tier;
    }

    public int getPants_lvl() {
        return pants_lvl;
    }

    public void setPants_lvl(int pants_lvl) {
        this.pants_lvl = pants_lvl;
    }

    public String getBracer_name() {
        return bracer_name;
    }

    public void setBracer_name(String bracer_name) {
        this.bracer_name = bracer_name;
    }

    public String getBracer_tier() {
        return bracer_tier;
    }

    public void setBracer_tier(String bracer_tier) {
        this.bracer_tier = bracer_tier;
    }

    public int getBracer_lvl() {
        return bracer_lvl;
    }

    public void setBracer_lvl(int bracer_lvl) {
        this.bracer_lvl = bracer_lvl;
    }

    public String getBoots_name() {
        return boots_name;
    }

    public void setBoots_name(String boots_name) {
        this.boots_name = boots_name;
    }

    public String getBoots_tier() {
        return boots_tier;
    }

    public void setBoots_tier(String boots_tier) {
        this.boots_tier = boots_tier;
    }

    public int getBoots_lvl() {
        return boots_lvl;
    }

    public void setBoots_lvl(int boots_lvl) {
        this.boots_lvl = boots_lvl;
    }

    public String getAccessory1_name() {
        return accessory1_name;
    }

    public void setAccessory1_name(String accessory1_name) {
        this.accessory1_name = accessory1_name;
    }

    public int getAccessory1_lvl() {
        return accessory1_lvl;
    }

    public void setAccessory1_lvl(int accessory1_lvl) {
        this.accessory1_lvl = accessory1_lvl;
    }

    public String getAccessory1_tier() {
        return accessory1_tier;
    }

    public void setAccessory1_tier(String accessory1_tier) {
        this.accessory1_tier = accessory1_tier;
    }

    public String getAccessory2_name() {
        return accessory2_name;
    }

    public void setAccessory2_name(String accessory2_name) {
        this.accessory2_name = accessory2_name;
    }

    public String getAccessory2_tier() {
        return accessory2_tier;
    }

    public void setAccessory2_tier(String accessory2_tier) {
        this.accessory2_tier = accessory2_tier;
    }

    public int getAccessory2_lvl() {
        return accessory2_lvl;
    }

    public void setAccessory2_lvl(int accessory2_lvl) {
        this.accessory2_lvl = accessory2_lvl;
    }

    public String getNecklace_name() {
        return necklace_name;
    }

    public void setNecklace_name(String necklace_name) {
        this.necklace_name = necklace_name;
    }

    public String getNecklace_tier() {
        return necklace_tier;
    }

    public void setNecklace_tier(String necklace_tier) {
        this.necklace_tier = necklace_tier;
    }

    public int getNecklace_lvl() {
        return necklace_lvl;
    }

    public void setNecklace_lvl(int necklace_lvl) {
        this.necklace_lvl = necklace_lvl;
    }

    public String getResult_full() {
        return result_full;
    }

    public void setResult_full(String result_full) {
        this.result_full = result_full;
    }

    public String getResult_essential() {
        return result_essential;
    }

    public void setResult_essential(String result_essential) {
        this.result_essential = result_essential;
    }

    public String getStats() {
        return stats;
    }

    public void setStats(String stats) {
        this.stats = stats;
    }

    public String getEnemy() {
        return enemy;
    }

    public void setEnemy(String enemy) {
        this.enemy = enemy;
    }

    public int getSkill1_lvl() {
        return skill1_lvl;
    }

    public void setSkill1_lvl(int skill1_lvl) {
        this.skill1_lvl = skill1_lvl;
    }

    public int getSkill2_lvl() {
        return skill2_lvl;
    }

    public void setSkill2_lvl(int skill2_lvl) {
        this.skill2_lvl = skill2_lvl;
    }

    public int getSkill3_lvl() {
        return skill3_lvl;
    }

    public void setSkill3_lvl(int skill3_lvl) {
        this.skill3_lvl = skill3_lvl;
    }

    public int getPskill1_lvl() {
        return pskill1_lvl;
    }

    public void setPskill1_lvl(int pskill1_lvl) {
        this.pskill1_lvl = pskill1_lvl;
    }

    public int getPskill2_lvl() {
        return pskill2_lvl;
    }

    public void setPskill2_lvl(int pskill2_lvl) {
        this.pskill2_lvl = pskill2_lvl;
    }

    public int getPskill3_lvl() {
        return pskill3_lvl;
    }

    public void setPskill3_lvl(int pskill3_lvl) {
        this.pskill3_lvl = pskill3_lvl;
    }

    public int getSimulations() {
        return simulations;
    }

    public void setSimulations(int simulations) {
        this.simulations = simulations;
    }

    public String getGameversion() {
        return gameversion;
    }

    public void setGameversion(String gameversion) {
        this.gameversion = gameversion;
    }

    public int getReroll() {
        return reroll;
    }

    public void setReroll(int reroll) {
        this.reroll = reroll;
    }

    public double getMilestone() {
        return milestone;
    }

    public void setMilestone(double milestone) {
        this.milestone = milestone;
    }

    public int getCrafting_lvl() {
        return crafting_lvl;
    }

    public void setCrafting_lvl(int crafting_lvl) {
        this.crafting_lvl = crafting_lvl;
    }

    public int getAlchemy_lvl() {
        return alchemy_lvl;
    }

    public void setAlchemy_lvl(int alchemy_lvl) {
        this.alchemy_lvl = alchemy_lvl;
    }

    public boolean isSetsetup() {
        return setsetup;
    }

    public void setSetsetup(boolean setsetup) {
        this.setsetup = setsetup;
    }

    public boolean isSetupinfo() {
        return setupinfo;
    }

    public void setSetupinfo(boolean setupinfo) {
        this.setupinfo = setupinfo;
    }
}
