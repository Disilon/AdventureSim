package Disilon;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class Main {
    public static int game_version = 1531;

    public static void main(String[] args) {
        try {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader("data/Weapons.json"));
            Map<String, Map> WeaponDataMap = gson.fromJson(reader, Map.class);

            //Consider setting Weapons into 2 categories. One Handed and Two Handed. Then again, I guess a couple are main hand only and offhand only... Maybe just make the user not be stupid until we fix it.
            System.out.println(WeaponDataMap.get("BEECH_BOW"));
            Equipment testBow = new Equipment(WeaponDataMap.get("BEECH_BOW"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //UserForm uf = new UserForm();
    }

}