package Disilon;

import javax.swing.table.DefaultTableModel;

public class SkillTableModel extends DefaultTableModel {

    public SkillTableModel() {
        super();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        return col >= 1;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if (col < 2) {
            super.setValueAt(value, row, col);
        } else {
            String sv = value.toString();
            double new_value = 0;
            if (sv.contains("/")) {
                new_value = Double.parseDouble(sv.split("/")[0]) / Double.parseDouble(sv.split("/")[1]) * 100.0;
            } else {
                new_value = Double.parseDouble(sv);
            }
            super.setValueAt(new_value, row, col);
        }
    }
}
