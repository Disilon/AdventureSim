package Disilon;

import Disilon.Equipment.Quality;

import java.io.Serializable;
import java.util.HashMap;

public class Setup implements Serializable {
    String playerclass;
    double ml;
    double cl;
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
    Quality mh_tier;
    int mh_lvl;
    String oh_name;
    Quality oh_tier;
    int oh_lvl;
    String helmet_name;
    Quality helmet_tier;
    int helmet_lvl;
    String chest_name;
    Quality chest_tier;
    int chest_lvl;
    String pants_name;
    Quality pants_tier;
    int pants_lvl;
    String bracer_name;
    Quality bracer_tier;
    int bracer_lvl;
    String boots_name;
    Quality boots_tier;
    int boots_lvl;
    String accessory1_name;
    Quality accessory1_tier;
    int accessory1_lvl;
    String accessory2_name;
    Quality accessory2_tier;
    int accessory2_lvl;
    String necklace_name;
    Quality necklace_tier;
    int necklace_lvl;
    String result_essential;
    String result_skills;
    String result_lvling;
    String stats;
    String enemy;
    Zone zone;
    double skill1_lvl;
    double skill2_lvl;
    double skill3_lvl;
    double pskill1_lvl;
    double pskill2_lvl;
    double pskill3_lvl;
    String gameversion;
    int reroll;
    double milestone;
    int crafting_lvl;
    int alchemy_lvl;
    boolean setsetup;
    boolean leveling;
    int sim_type;
    int simulations;
    double sim_hours;
    int sim_cl;
    HashMap<String, Double> passives_lvls;
    HashMap<String, Double> actives_lvls;

    public Setup() {
        this.passives_lvls = new HashMap<>();
        this.actives_lvls = new HashMap<>();
        this.playerclass = "Sniper";
        this.ml = 140;
        this.cl = 75;
        this.potion1 = "HP T3";
        this.potion1_t = 50;
        this.potion2 = "MP T3";
        this.potion2_t = 50;
        this.potion3 = "None";
        this.potion3_t = 50;
        this.skill1 = "None";
        this.skill2 = "None";
        this.skill3 = "None";
        this.skill1_mod = SkillMod.Damage;
        this.skill2_mod = SkillMod.Basic;
        this.skill3_mod = SkillMod.Basic;
        this.skill1_s = 1;
        this.skill2_s = 1;
        this.skill3_s = 1;
        this.pskill1 = "None";
        this.pskill2 = "None";
        this.pskill3 = "None";
        this.mh_name = "None";
        this.mh_tier = Quality.Good;
        this.mh_lvl = 0;
        this.oh_name = "None";
        this.oh_tier = Quality.Good;
        this.oh_lvl = 0;
        this.helmet_name = "None";
        this.helmet_tier = Quality.Good;
        this.helmet_lvl = 0;
        this.chest_name = "None";
        this.chest_tier = Quality.Good;
        this.chest_lvl = 0;
        this.pants_name = "None";
        this.pants_tier = Quality.Good;
        this.pants_lvl = 0;
        this.bracer_name = "None";
        this.bracer_tier = Quality.Good;
        this.bracer_lvl = 0;
        this.boots_name = "None";
        this.boots_tier = Quality.Good;
        this.boots_lvl = 0;
        this.accessory1_name = "Golden Belt";
        this.accessory1_tier = Quality.Exceptional;
        this.accessory1_lvl = 25;
        this.accessory2_name = "None";
        this.accessory2_tier = Quality.Good;
        this.accessory2_lvl = 0;
        this.necklace_name = "None";
        this.necklace_tier = Quality.Good;
        this.necklace_lvl = 0;
        this.result_essential = "Click simulate to get sim data";
        this.stats = "Click simulate to calculate stats";
        this.enemy = "";
        this.zone = Zone.z9;
        this.skill1_lvl = 0;
        this.skill2_lvl = 0;
        this.skill3_lvl = 0;
        this.pskill1_lvl = 0;
        this.pskill2_lvl = 0;
        this.pskill3_lvl = 0;
        this.gameversion = "1541";
        this.reroll = 0;
        this.milestone = 162.5;
        this.crafting_lvl = 20;
        this.alchemy_lvl = 20;
        this.setsetup = true;
        this.leveling = false;
        this.sim_type = 1;
        this.simulations = 1000;
        this.sim_hours = 12;
        this.sim_cl = 90;
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

    public double getMl() {
        return ml;
    }

    public void setMl(double ml) {
        this.ml = ml;
    }

    public double getCl() {
        return cl;
    }

    public void setCl(double cl) {
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

    public Quality getMh_tier() {
        return mh_tier;
    }

    public void setMh_tier(Quality mh_tier) {
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

    public Quality getOh_tier() {
        return oh_tier;
    }

    public void setOh_tier(Quality oh_tier) {
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

    public Quality getHelmet_tier() {
        return helmet_tier;
    }

    public void setHelmet_tier(Quality helmet_tier) {
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

    public Quality getChest_tier() {
        return chest_tier;
    }

    public void setChest_tier(Quality chest_tier) {
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

    public Quality getPants_tier() {
        return pants_tier;
    }

    public void setPants_tier(Quality pants_tier) {
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

    public Quality getBracer_tier() {
        return bracer_tier;
    }

    public void setBracer_tier(Quality bracer_tier) {
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

    public Quality getBoots_tier() {
        return boots_tier;
    }

    public void setBoots_tier(Quality boots_tier) {
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

    public Quality getAccessory1_tier() {
        return accessory1_tier;
    }

    public void setAccessory1_tier(Quality accessory1_tier) {
        this.accessory1_tier = accessory1_tier;
    }

    public String getAccessory2_name() {
        return accessory2_name;
    }

    public void setAccessory2_name(String accessory2_name) {
        this.accessory2_name = accessory2_name;
    }

    public Quality getAccessory2_tier() {
        return accessory2_tier;
    }

    public void setAccessory2_tier(Quality accessory2_tier) {
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

    public Quality getNecklace_tier() {
        return necklace_tier;
    }

    public void setNecklace_tier(Quality necklace_tier) {
        this.necklace_tier = necklace_tier;
    }

    public int getNecklace_lvl() {
        return necklace_lvl;
    }

    public void setNecklace_lvl(int necklace_lvl) {
        this.necklace_lvl = necklace_lvl;
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

    public double getSkill1_lvl() {
        return skill1_lvl;
    }

    public void setSkill1_lvl(double skill1_lvl) {
        this.skill1_lvl = skill1_lvl;
    }

    public double getSkill2_lvl() {
        return skill2_lvl;
    }

    public void setSkill2_lvl(double skill2_lvl) {
        this.skill2_lvl = skill2_lvl;
    }

    public double getSkill3_lvl() {
        return skill3_lvl;
    }

    public void setSkill3_lvl(double skill3_lvl) {
        this.skill3_lvl = skill3_lvl;
    }

    public double getPskill1_lvl() {
        return pskill1_lvl;
    }

    public void setPskill1_lvl(double pskill1_lvl) {
        this.pskill1_lvl = pskill1_lvl;
    }

    public double getPskill2_lvl() {
        return pskill2_lvl;
    }

    public void setPskill2_lvl(double pskill2_lvl) {
        this.pskill2_lvl = pskill2_lvl;
    }

    public double getPskill3_lvl() {
        return pskill3_lvl;
    }

    public void setPskill3_lvl(double pskill3_lvl) {
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

    public boolean isLeveling() {
        return leveling;
    }

    public void setLeveling(boolean leveling) {
        this.leveling = leveling;
    }

    public int getSim_type() {
        return sim_type;
    }

    public void setSim_type(int sim_type) {
        this.sim_type = sim_type;
    }

    public double getSim_hours() {
        return sim_hours;
    }

    public void setSim_hours(double sim_hours) {
        this.sim_hours = sim_hours;
    }

    public int getSim_cl() {
        return sim_cl;
    }

    public void setSim_cl(int sim_cl) {
        this.sim_cl = sim_cl;
    }

    public HashMap<String, Double> getPassives_lvls() {
        return passives_lvls;
    }

    public void setPassives_lvls(HashMap<String, Double> passives_lvls) {
        this.passives_lvls = passives_lvls;
    }

    public HashMap<String, Double> getActives_lvls() {
        return actives_lvls;
    }

    public void setActives_lvls(HashMap<String, Double> actives_lvls) {
        this.actives_lvls = actives_lvls;
    }

    public String getResult_skills() {
        return result_skills;
    }

    public void setResult_skills(String result_skills) {
        this.result_skills = result_skills;
    }

    public String getResult_lvling() {
        return result_lvling;
    }

    public void setResult_lvling(String result_lvling) {
        this.result_lvling = result_lvling;
    }
}
