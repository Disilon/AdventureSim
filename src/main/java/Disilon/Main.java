package Disilon;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DecimalStyle;
import java.util.Locale;
import java.util.Random;

public class Main {
    public static int game_version = 1531;
    public static Integer[] availableVersions = {1531, 1532, 1534, 1535, 1537};
    public static DecimalFormatSymbols dfs;
    public static DecimalFormat df2;
    public static DecimalFormat df2p;
    public static DecimalFormat dfm;
    public static final Random random = new Random();

    public static void main(String[] args) {
        dfs = DecimalFormatSymbols.getInstance(Locale.UK);
        dfs.setDecimalSeparator(DecimalStyle.STANDARD.getDecimalSeparator());
        df2 = new DecimalFormat("#.##", Main.dfs);
        dfm = new DecimalFormat("#.0", Main.dfs);
        df2p = new DecimalFormat("#00.00", Main.dfs);
        df2p.setMinimumIntegerDigits(2);
        df2p.setMaximumFractionDigits(2);
        df2p.setMinimumFractionDigits(2);
        try {
            UserForm uf = new UserForm();
        } catch (Exception e) {
            e.printStackTrace();
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
}