package Disilon;

import java.io.File;

public class Main {
    public static int game_version = 1531;

    public static void main(String[] args) {
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
}