package Disilon;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Settings implements Serializable {
    ArrayList<String> default_setups;
    int window_size_x;
    int window_size_y;
    int simulation_time_limit;

    public Settings() {
        this.default_setups = new ArrayList<>();
        this.default_setups.add("Default.json");
        this.window_size_x = 1350;
        this.window_size_y = 1060;
        this.simulation_time_limit = 60000;
    }

    public Dimension getWindowSize() {
        return new Dimension(this.window_size_x, this.window_size_y);
    }

    public ArrayList<String> getDefault_setups() {
        return default_setups;
    }

    public void setDefault_setups(ArrayList<String> default_setups) {
        this.default_setups = default_setups;
    }

    public int getWindow_size_x() {
        return window_size_x;
    }

    public void setWindow_size_x(int window_size_x) {
        this.window_size_x = window_size_x;
    }

    public int getWindow_size_y() {
        return window_size_y;
    }

    public void setWindow_size_y(int window_size_y) {
        this.window_size_y = window_size_y;
    }

    public int getSimulation_time_limit() {
        return simulation_time_limit;
    }

    public void setSimulation_time_limit(int simulation_time_limit) {
        this.simulation_time_limit = simulation_time_limit;
    }
}
