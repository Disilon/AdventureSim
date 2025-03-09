package Disilon;

public class EquipmentSet {
    String bonus;
    int required_items;
    int current_items;
    String min_quality;
    int min_upgrade = -1;

    public EquipmentSet(String bonus, int required_items) {
        this.bonus = bonus;
        this.required_items = required_items;
    }

    public void addItem(String quality, int upgrade) {
        current_items++;
        if (min_upgrade == -1 || upgrade < min_upgrade) {
            min_upgrade = upgrade;
        }
        if (min_quality == null || Equipment.multiplier_from_tier(quality) < Equipment.multiplier_from_tier(min_quality)) {
            min_quality = quality;
        }
    }

    public boolean completed() {
        return current_items >= required_items;
    }
}
