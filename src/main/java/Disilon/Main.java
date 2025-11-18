package Disilon;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DecimalStyle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

public class Main {
    public static Vector<Integer> availableVersions = new Vector<>(List.of(1575, 1580, 1587, 1591, 1605));
    public static int game_version = availableVersions.getLast();
    public static DecimalFormatSymbols dfs;
    public static DecimalFormat df2;
    public static DecimalFormat df4;
    public static DecimalFormat df2p;
    public static DecimalFormat dfm;
    public static final Random random = new Random();
    public static EquipmentData equipmentData = new EquipmentData();
    public static final HashSet<String> log = new HashSet<>();

    public static void main(String[] args) {
        dfs = DecimalFormatSymbols.getInstance(Locale.UK);
        dfs.setDecimalSeparator(DecimalStyle.STANDARD.getDecimalSeparator());
        df2 = new DecimalFormat("#.##", Main.dfs);
        df4 = new DecimalFormat("#.####", Main.dfs);
        dfm = new DecimalFormat("#.0", Main.dfs);
        df2p = new DecimalFormat("#00.00", Main.dfs);
        df2p.setMinimumIntegerDigits(2);
        df2p.setMaximumFractionDigits(2);
        df2p.setMinimumFractionDigits(2);
//        log.add("skill_rotation");
//        log.add("fight_end");
//        log.add("death");
//        log.add("enemy_death_stat");
//        log.add("player_death_stat");
//        log.add("buff_duration");
//        log.add("buff_remove");
//        log.add("dot_dmg");
//        log.add("skill_cast_start");
//        log.add("skill_use");
//        log.add("skill_attack");
//        log.add("skill_enough_mp");
//        log.add("research");
        try {
            UserForm uf = new UserForm();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class EquipmentData {
        public LinkedHashMap<String, Equipment> items = new LinkedHashMap<>();
        public ArrayList<String> twohanded = new ArrayList<>();
        private final Gson gson = new Gson();

        public void loadEquipment() {
            try {
                JsonReader weaponReader = new JsonReader(new FileReader("data/Weapons.json"));
                Map<String, Map> weaponDataMap = gson.fromJson(weaponReader, Map.class);
                JsonReader armorReader = new JsonReader(new FileReader("data/Armor.json"));
                Map<String, Map> armorDataMap = gson.fromJson(armorReader, Map.class);
                JsonReader accessoryReader = new JsonReader(new FileReader("data/Accessories.json"));
                Map<String, Map> accessoryDataMap = gson.fromJson(accessoryReader, Map.class);
                twohanded.clear();
                items.clear();
                weaponDataMap.forEach((slot, value) -> {
                    for (Object item : value.entrySet()) {
                        String name = ((Map.Entry<String, Map>) item).getKey();
                        Map weapon_data = ((Map.Entry<String, Map>) item).getValue();
                        items.put(name, new Equipment(name, slot, weapon_data));
                        if (slot.equals("2H")) {
                            twohanded.add(name);
                        }
                    }
                });
                armorDataMap.forEach((slot, value) -> {
                    for (Object item : value.entrySet()) {
                        String name = ((Map.Entry<String, Map>) item).getKey();
                        Equipment e = new Equipment(name, slot,
                                ((Map.Entry<String, Map>) item).getValue());
                        items.put(name, e);
                    }
                });
                accessoryDataMap.forEach((slot, value) -> {
                    for (Object item : value.entrySet()) {
                        String name = ((Map.Entry<String, Map>) item).getKey();
                        items.put(name, new Equipment(name, slot,
                                ((Map.Entry<String, Map>) item).getValue()));
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getJarPath() {
        try {
            return new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String secToTime(double time) {
        int sec = (int) time;
        int seconds = sec % 60;
        int minutes = sec / 60;
        if (minutes >= 60) {
            int hours = minutes / 60;
            minutes %= 60;
            if (hours >= 24) {
                int days = hours / 24;
                return String.format("%d days %02d:%02d:%02d", days, hours % 24, minutes, seconds);
            }
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format("00:%02d:%02d", minutes, seconds);
    }

    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }

    public static String shorthand(double number) {
        if (number < 1e6) {
            return String.valueOf((int) number);
        }
        if (number < 1e9) {
            return String.valueOf((int) number / 1000) + "k";
        }
        return String.valueOf((int) number / 1000000) + "m";
    }

    public static double minIfNotZero(double a, double b) {
        if (a == 0) return b;
        if (b == 0) return a;
        return Math.min(a, b);
    }

    public static double minTickTime(double time, boolean offline) {
        double tick = offline ? 0.18 : 0.03;
        int n = (int) Math.ceil(time / tick);
        if (n == 0) n = 1;
        return n * tick;
    }
}