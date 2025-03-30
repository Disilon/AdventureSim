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
}
