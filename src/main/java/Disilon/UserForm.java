package Disilon;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import static Disilon.Main.df2;
import static Disilon.Main.df2p;
import static Disilon.Main.dfm;
import static Disilon.Main.dfs;

public class UserForm extends JFrame {
    private JPanel RootPanel;
    private JPanel LeftPanel;
    private JPanel RightPanel;
    private JComboBox PlayerClass;
    private JSpinner ML;
    private JSpinner CL;
    private JLabel ClassLabel;
    private JLabel MLLabel;
    private JLabel CLLabel;
    private JComboBox Potion1;
    private JSpinner Potion1_t;
    private JComboBox Potion2;
    private JSpinner Potion2_t;
    private JComboBox Potion3;
    private JSpinner Potion3_t;
    private JComboBox Skill1;
    private JComboBox Skill2;
    private JComboBox Skill3;
    private JComboBox Skill1_mod;
    private JComboBox Skill2_mod;
    private JComboBox Skill3_mod;
    private JSpinner Skill1_s;
    private JSpinner Skill2_s;
    private JSpinner Skill3_s;
    private JComboBox Pskill1;
    private JComboBox Pskill2;
    private JComboBox Pskill3;
    private JComboBox MH_name;
    private JComboBox MH_tier;
    private JSpinner MH_lvl;
    private JComboBox OH_name;
    private JComboBox OH_tier;
    private JSpinner OH_lvl;
    private JComboBox Helmet_name;
    private JComboBox Helmet_tier;
    private JSpinner Helmet_lvl;
    private JComboBox Chest_name;
    private JComboBox Chest_tier;
    private JSpinner Chest_lvl;
    private JComboBox Pants_name;
    private JComboBox Pants_tier;
    private JSpinner Pants_lvl;
    private JComboBox Bracer_name;
    private JComboBox Bracer_tier;
    private JSpinner Bracer_lvl;
    private JComboBox Boots_name;
    private JComboBox Boots_tier;
    private JSpinner Boots_lvl;
    private JComboBox Accessory1_name;
    private JSpinner Accessory1_lvl;
    private JComboBox Accessory1_tier;
    private JComboBox Accessory2_name;
    private JComboBox Accessory2_tier;
    private JSpinner Accessory2_lvl;
    private JComboBox Necklace_name;
    private JComboBox Necklace_tier;
    private JSpinner Necklace_lvl;
    private JTextArea Result;
    private JTextArea Stats;
    private JButton Save;
    private JButton Load;
    private JComboBox Enemy;
    private JSpinner Skill1_lvl;
    private JSpinner Skill2_lvl;
    private JSpinner Skill3_lvl;
    private JSpinner Pskill1_lvl;
    private JSpinner Pskill2_lvl;
    private JSpinner Pskill3_lvl;
    private JComboBox GameVersion;
    private JSpinner Reroll;
    private JSpinner Milestone;
    private JSpinner Crafting_lvl;
    private JSpinner Alchemy_lvl;
    private JButton Run;
    private JCheckBox SetSetup;
    private JCheckBox SetupInfo;
    private JFileChooser fileChooser = null;
    private ButtonGroup Sim_type;
    private JRadioButton Sim_num;
    private JRadioButton Sim_time;
    private JRadioButton Sim_lvl;
    private JSpinner Simulations;
    private JSpinner SimHours;
    private JSpinner SimCL;
    private JCheckBox Leveling;
    private JFormattedTextField ML_p;
    private JFormattedTextField CL_p;
    private JFormattedTextField Skill1_lvl_p;
    private JFormattedTextField Skill2_lvl_p;
    private JFormattedTextField Skill3_lvl_p;
    private JFormattedTextField Pskill1_lvl_p;
    private JFormattedTextField Pskill2_lvl_p;
    private JFormattedTextField Pskill3_lvl_p;
    private JButton Update_lvls;
    private JMenuBar Bar;
    private JButton New_tab;
    private JScrollPane LeftPane;
    private JTable ActiveSkills;
    private JTable PassiveSkills;
    DefaultTableModel activeSkillsModel;
    DefaultTableModel passiveSkillsModel;
    GridBagConstraints gbc = new GridBagConstraints();

    public Player player;
    public Enemy enemy;
    public Simulation simulation;
    public LinkedHashMap<String, Equipment> mh = new LinkedHashMap<>();
    public LinkedHashMap<String, Equipment> oh = new LinkedHashMap<>();
    public LinkedHashMap<String, Equipment> helmet = new LinkedHashMap<>();
    public LinkedHashMap<String, Equipment> chest = new LinkedHashMap<>();
    public LinkedHashMap<String, Equipment> pants = new LinkedHashMap<>();
    public LinkedHashMap<String, Equipment> bracers = new LinkedHashMap<>();
    public LinkedHashMap<String, Equipment> boots = new LinkedHashMap<>();
    public LinkedHashMap<String, Equipment> acc1 = new LinkedHashMap<>();
    public LinkedHashMap<String, Equipment> acc2 = new LinkedHashMap<>();
    public LinkedHashMap<String, Equipment> neck = new LinkedHashMap<>();
    public ArrayList<String> twohanded = new ArrayList<>();
    public LinkedHashMap<JMenu, Setup> tabs = new LinkedHashMap<>();
    public JMenu selected_tab;
    ActionListener itemListener;
    MenuListener menuListener;
    Gson gson = new Gson();

    public UserForm() throws URISyntaxException {
        class SampleMenuListener implements MenuListener {

            @Override
            public void menuSelected(MenuEvent event) {
                JMenu source = (JMenu) event.getSource();
                selectTab(source);
            }

            @Override
            public void menuDeselected(MenuEvent event) {
//                System.out.println("menuDeselected");
            }

            @Override
            public void menuCanceled(MenuEvent event) {
//                System.out.println("menuCanceled");
            }
        }
        class MenuItemActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent event) {
                JMenuItem source = (JMenuItem) event.getSource();
                JPopupMenu jpm = (JPopupMenu) source.getParent();
                JMenu tab = (JMenu) jpm.getInvoker();
                if (source.getText() == "Rename") {
                    String new_name = JOptionPane.showInputDialog(
                            UserForm.this,
                            "<html><h2>Enter new tab name:");
                    if (new_name != null && new_name.length() > 0) {
                        tab.setText(new_name);
                    }
                }
                if (source.getText() == "Delete") {
                    if (tabs.size() > 1) {
                        tabs.remove(tab);
                        selected_tab = tabs.lastEntry().getKey();
                        Bar.remove(tab);
                        selectTab(selected_tab);
                        Bar.updateUI();
                    }
                }
            }
        }
        menuListener = new SampleMenuListener();
        itemListener = new MenuItemActionListener();
        fileChooser = new JFileChooser(Main.getJarPath());
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "json", "json");
        fileChooser.setFileFilter(filter);
        player = new Player();
        enemy = new Enemy();
        simulation = new Simulation();
        RootPanel = new JPanel();
        LeftPanel = new JPanel();
        RightPanel = new JPanel();
        RightPanel.setLayout(new GridBagLayout());
        Bar = new JMenuBar();
        this.setJMenuBar(Bar);
        New_tab = new JButton("   +   ");
        New_tab.setBorder(null);
        New_tab.setFocusPainted(false);
        New_tab.setContentAreaFilled(true);
//        New_tab.setSize(30, 30);
//        New_tab.setPreferredSize(new Dimension(40, 20));
        New_tab.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tabs.size() < 10) {
                    JMenu new_tab = createTab();
                    loadSetup(tabs.get(selected_tab));
                }
            }
        });
        Bar.add(New_tab);

        CL = createCustomSpinner(62, 0, 1000, 1);
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 1;
        RightPanel.add(CL, gbc);
        CLLabel = new JLabel();
        CLLabel.setText("CL: ");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        RightPanel.add(CLLabel, gbc);
        ClassLabel = new JLabel();
        ClassLabel.setText("Class:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        RightPanel.add(ClassLabel, gbc);
        ML = createCustomSpinner(120, 1, 1000, 1);
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        RightPanel.add(ML, gbc);
        ML_p = new JFormattedTextField(df2p);
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 5;
//        gbc.ipady = 3;
        RightPanel.add(ML_p, gbc);
        CL_p = new JFormattedTextField(df2p);
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 5;
//        gbc.ipady = 3;
        RightPanel.add(CL_p, gbc);
        MLLabel = new JLabel();
        MLLabel.setText("ML: ");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        RightPanel.add(MLLabel, gbc);
        PlayerClass = new JComboBox(Player.availableClasses);
        PlayerClass.setMaximumRowCount(20);
        PlayerClass.setSelectedIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(PlayerClass, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("Potion setup:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        RightPanel.add(label1, gbc);
        Potion1 = new JComboBox<>(Potion.getAvailablePotions());
        Potion1.setMaximumRowCount(11);
        Potion1.setSelectedIndex(3);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Potion1, gbc);
        Potion1_t = new JSpinner(new SpinnerNumberModel(50, 25, 95, 5));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.ipadx = 12;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Potion1_t, gbc);
        Potion2 = new JComboBox<>(Potion.getAvailablePotions());
        Potion2.setMaximumRowCount(11);
        Potion2.setSelectedIndex(6);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Potion2, gbc);
        Potion2_t = new JSpinner(new SpinnerNumberModel(50, 25, 95, 5));
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 3;
        gbc.ipadx = 12;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Potion2_t, gbc);
        Potion3 = new JComboBox<>(Potion.getAvailablePotions());
        Potion3.setMaximumRowCount(11);
        Potion3.setSelectedIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Potion3, gbc);
        Potion3_t = new JSpinner(new SpinnerNumberModel(50, 25, 95, 5));
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 3;
        gbc.ipadx = 22;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Potion3_t, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Active skills:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 6;
        RightPanel.add(label2, gbc);
        Skill1 = new JComboBox();
        Skill1.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Skill1, gbc);
        Skill2 = new JComboBox();
        Skill2.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Skill2, gbc);
        Skill3 = new JComboBox();
        Skill3.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Skill3, gbc);
        Skill1_mod = new JComboBox<>(SkillMod.getAvailableMods());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Skill1_mod, gbc);
        Skill2_mod = new JComboBox<>(SkillMod.getAvailableMods());
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Skill2_mod, gbc);
        Skill3_mod = new JComboBox<>(SkillMod.getAvailableMods());
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Skill3_mod, gbc);
        Skill1_s = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Skill1_s, gbc);
        Skill2_s = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Skill2_s, gbc);
        Skill3_s = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Skill3_s, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Passive skills:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.gridwidth = 6;
        RightPanel.add(label3, gbc);
        Pskill1 = new JComboBox();
        Pskill1.setMaximumRowCount(16);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Pskill1, gbc);
        Pskill2 = new JComboBox();
        Pskill2.setMaximumRowCount(16);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Pskill2, gbc);
        Pskill3 = new JComboBox();
        Pskill3.setMaximumRowCount(16);
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Pskill3, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("Equipment:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 11;
        gbc.gridwidth = 6;
        RightPanel.add(label4, gbc);
        final JLabel label5 = new JLabel();
        label5.setText("Weapon MH");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 12;
        gbc.anchor = GridBagConstraints.WEST;
        RightPanel.add(label5, gbc);
        MH_name = new JComboBox<String>();
        MH_name.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 12;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(MH_name, gbc);
        MH_tier = new JComboBox<>(Equipment.Quality.values());
        MH_tier.setMaximumRowCount(16);
        MH_tier.setSelectedIndex(3);
        MH_tier.setToolTipText("Quality");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 12;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(MH_tier, gbc);
        MH_lvl = new JSpinner();
        MH_lvl.setToolTipText("Upgrade lvl");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 12;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(MH_lvl, gbc);
        final JLabel label6 = new JLabel();
        label6.setText("Offhand");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 13;
        gbc.anchor = GridBagConstraints.WEST;
        RightPanel.add(label6, gbc);
        OH_name = new JComboBox();
        OH_name.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 13;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(OH_name, gbc);
        OH_tier = new JComboBox<>(Equipment.Quality.values());
        OH_tier.setMaximumRowCount(16);
        OH_tier.setSelectedIndex(3);
        OH_tier.setToolTipText("Quality");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 13;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(OH_tier, gbc);
        OH_lvl = new JSpinner();
        OH_lvl.setToolTipText("Upgrade lvl");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 13;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(OH_lvl, gbc);
        final JLabel label7 = new JLabel();
        label7.setText("Helmet");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 15;
        gbc.anchor = GridBagConstraints.WEST;
        RightPanel.add(label7, gbc);
        Helmet_name = new JComboBox();
        Helmet_name.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 15;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Helmet_name, gbc);
        Helmet_tier = new JComboBox<>(Equipment.Quality.values());
        Helmet_tier.setMaximumRowCount(16);
        Helmet_tier.setSelectedIndex(3);
        Helmet_tier.setToolTipText("Quality");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 15;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Helmet_tier, gbc);
        Helmet_lvl = new JSpinner();
        Helmet_lvl.setToolTipText("Upgrade lvl");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 15;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Helmet_lvl, gbc);
        final JLabel label8 = new JLabel();
        label8.setText("Chest");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 16;
        gbc.anchor = GridBagConstraints.WEST;
        RightPanel.add(label8, gbc);
        Chest_name = new JComboBox();
        Chest_name.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 16;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Chest_name, gbc);
        Chest_tier = new JComboBox<>(Equipment.Quality.values());
        Chest_tier.setMaximumRowCount(16);
        Chest_tier.setSelectedIndex(3);
        Chest_tier.setToolTipText("Quality");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 16;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Chest_tier, gbc);
        Chest_lvl = new JSpinner();
        Chest_lvl.setToolTipText("Upgrade lvl");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 16;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Chest_lvl, gbc);
        final JLabel label9 = new JLabel();
        label9.setText("Pants");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 17;
        gbc.anchor = GridBagConstraints.WEST;
        RightPanel.add(label9, gbc);
        Pants_name = new JComboBox();
        Pants_name.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 17;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Pants_name, gbc);
        Pants_tier = new JComboBox<>(Equipment.Quality.values());
        Pants_tier.setMaximumRowCount(16);
        Pants_tier.setSelectedIndex(3);
        Pants_tier.setToolTipText("Quality");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 17;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Pants_tier, gbc);
        Pants_lvl = new JSpinner();
        Pants_lvl.setToolTipText("Upgrade lvl");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 17;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Pants_lvl, gbc);
        final JLabel label10 = new JLabel();
        label10.setText("Bracer");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 18;
        gbc.anchor = GridBagConstraints.WEST;
        RightPanel.add(label10, gbc);
        Bracer_name = new JComboBox();
        Bracer_name.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 18;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Bracer_name, gbc);
        Bracer_tier = new JComboBox<>(Equipment.Quality.values());
        Bracer_tier.setMaximumRowCount(16);
        Bracer_tier.setSelectedIndex(3);
        Bracer_tier.setToolTipText("Quality");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 18;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Bracer_tier, gbc);
        Bracer_lvl = new JSpinner();
        Bracer_lvl.setToolTipText("Upgrade lvl");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 18;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Bracer_lvl, gbc);
        final JLabel label11 = new JLabel();
        label11.setText("Boots");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 19;
        gbc.anchor = GridBagConstraints.WEST;
        RightPanel.add(label11, gbc);
        Boots_name = new JComboBox();
        Boots_name.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 19;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Boots_name, gbc);
        Boots_tier = new JComboBox<>(Equipment.Quality.values());
        Boots_tier.setMaximumRowCount(16);
        Boots_tier.setSelectedIndex(3);
        Boots_tier.setToolTipText("Quality");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 19;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Boots_tier, gbc);
        Boots_lvl = new JSpinner();
        Boots_lvl.setToolTipText("Upgrade lvl");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 19;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Boots_lvl, gbc);
        final JLabel label12 = new JLabel();
        label12.setText("Accessory 1");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 20;
        gbc.anchor = GridBagConstraints.WEST;
        RightPanel.add(label12, gbc);
        Accessory1_name = new JComboBox();
        Accessory1_name.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 20;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Accessory1_name, gbc);
        Accessory1_tier = new JComboBox<>(Equipment.Quality.values());
        Accessory1_tier.setMaximumRowCount(16);
        Accessory1_tier.setSelectedIndex(5);
        Accessory1_tier.setToolTipText("Quality");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 20;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Accessory1_tier, gbc);
        Accessory1_lvl = new JSpinner();
        Accessory1_lvl.setToolTipText("Upgrade lvl");
        Accessory1_lvl.setValue(25);
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 20;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Accessory1_lvl, gbc);
        final JLabel label13 = new JLabel();
        label13.setText("Accessory 2");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 21;
        gbc.anchor = GridBagConstraints.WEST;
        RightPanel.add(label13, gbc);
        Accessory2_name = new JComboBox();
        Accessory2_name.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 21;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Accessory2_name, gbc);
        Accessory2_tier = new JComboBox<>(Equipment.Quality.values());
        Accessory2_tier.setMaximumRowCount(16);
        Accessory2_tier.setSelectedIndex(3);
        Accessory2_tier.setToolTipText("Quality");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 21;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Accessory2_tier, gbc);
        Accessory2_lvl = new JSpinner();
        Accessory2_lvl.setToolTipText("Upgrade lvl");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 21;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Accessory2_lvl, gbc);
        final JLabel label14 = new JLabel();
        label14.setText("Neck");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 22;
        gbc.anchor = GridBagConstraints.WEST;
        RightPanel.add(label14, gbc);
        Necklace_name = new JComboBox();
        Necklace_name.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 22;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Necklace_name, gbc);
        Necklace_tier = new JComboBox<>(Equipment.Quality.values());
        Necklace_tier.setMaximumRowCount(16);
        Necklace_tier.setSelectedIndex(3);
        Necklace_tier.setToolTipText("Quality");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 22;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Necklace_tier, gbc);
        Necklace_lvl = new JSpinner();
        Necklace_lvl.setToolTipText("Upgrade lvl");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 22;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Necklace_lvl, gbc);
        Result = new JTextArea();
        Result.setEditable(false);
        Result.setMinimumSize(new Dimension(1, 300));
        Result.setText("Result will be here");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 23;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        RightPanel.add(Result, gbc);
        Stats = new JTextArea();
        Stats.setEditable(false);
        Stats.setMinimumSize(new Dimension(1, 300));
        Stats.setText("Stats will be shown after simulation");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 23;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        RightPanel.add(Stats, gbc);
        Save = new JButton();
        Save.setText("Save");
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Save, gbc);
        Load = new JButton();
        Load.setText("Load");
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Load, gbc);
        final JLabel label16 = new JLabel();
        label16.setText("Level:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        RightPanel.add(label16, gbc);
        final JLabel label17 = new JLabel();
        label17.setText("Level:");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        RightPanel.add(label17, gbc);
        final JLabel label18 = new JLabel();
        label18.setText("Level:");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        RightPanel.add(label18, gbc);
        final JLabel label19 = new JLabel();
        label19.setText("Level:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.WEST;
        RightPanel.add(label19, gbc);
        final JLabel label20 = new JLabel();
        label20.setText("Level:");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.WEST;
        RightPanel.add(label20, gbc);
        final JLabel label21 = new JLabel();
        label21.setText("Level:");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.WEST;
        RightPanel.add(label21, gbc);
        Skill1_lvl = createLvlSpinner();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 40, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Skill1_lvl, gbc);
        Skill2_lvl = createLvlSpinner();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 40, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Skill2_lvl, gbc);
        Skill3_lvl = createLvlSpinner();
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 40, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Skill3_lvl, gbc);
        Skill1_lvl_p = new JFormattedTextField(df2p);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 5;
//        gbc.ipady = 3;
        RightPanel.add(Skill1_lvl_p, gbc);
        Skill2_lvl_p = new JFormattedTextField(df2p);
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 5;
//        gbc.ipady = 3;
        RightPanel.add(Skill2_lvl_p, gbc);
        Skill3_lvl_p = new JFormattedTextField(df2p);
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 5;
//        gbc.ipady = 3;
        RightPanel.add(Skill3_lvl_p, gbc);
        Pskill1_lvl = createLvlSpinner();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.insets = new Insets(0, 40, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Pskill1_lvl, gbc);
        Pskill2_lvl = createLvlSpinner();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 10;
        gbc.insets = new Insets(0, 40, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Pskill2_lvl, gbc);
        Pskill3_lvl = createLvlSpinner();
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 10;
        gbc.insets = new Insets(0, 40, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Pskill3_lvl, gbc);
        Pskill1_lvl_p = new JFormattedTextField(df2p);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 5;
//        gbc.ipady = 3;
        RightPanel.add(Pskill1_lvl_p, gbc);
        Pskill2_lvl_p = new JFormattedTextField(df2p);
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 5;
//        gbc.ipady = 3;
        RightPanel.add(Pskill2_lvl_p, gbc);
        Pskill3_lvl_p = new JFormattedTextField(df2p);
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 5;
//        gbc.ipady = 3;
        RightPanel.add(Pskill3_lvl_p, gbc);
        final JLabel label15 = new JLabel();
        label15.setText("Enemy:");
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 2;
        RightPanel.add(label15, gbc);
        Enemy = new JComboBox(Zone.values());
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        Enemy.setMaximumRowCount(25);
        RightPanel.add(Enemy, gbc);
        final JLabel label23 = new JLabel();
        label23.setText("Game version:");
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 4;
        RightPanel.add(label23, gbc);
        GameVersion = new JComboBox<>(Main.availableVersions);
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(GameVersion, gbc);
        final JLabel Reroll_label = new JLabel();
        Reroll_label.setText("Reroll enemy if hp >");
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 6;
        RightPanel.add(Reroll_label, gbc);
        Reroll = new JSpinner(new SpinnerNumberModel(0, 0, 1000000, 1000));
        Reroll.setToolTipText("Can't reroll after version 1535");
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Reroll, gbc);
        final JLabel label25 = new JLabel();
        label25.setText("Milestone exp bonus (%)");
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 8;
        RightPanel.add(label25, gbc);
        Milestone = createCustomSpinner(155, 100, 222.5, 2.5);
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Milestone, gbc);
        final JLabel label26 = new JLabel();
        label26.setText("Crafting lvl:");
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 10;
        RightPanel.add(label26, gbc);
        Crafting_lvl = new JSpinner(new SpinnerNumberModel(22, 0, 100, 1));
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Crafting_lvl, gbc);
        final JLabel label27 = new JLabel();
        label27.setText("Alchemy lvl:");
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 12;
        RightPanel.add(label27, gbc);
        Alchemy_lvl = new JSpinner(new SpinnerNumberModel(22, 0, 100, 1));
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 13;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Alchemy_lvl, gbc);
        SetupInfo = new JCheckBox();
        SetupInfo.setSelected(false);
        SetupInfo.setText("Show setup info in results");
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 14;
        RightPanel.add(SetupInfo, gbc);
        Sim_type = new ButtonGroup();
        Sim_num = new JRadioButton();
        Sim_num.setText("Number of simulations:");
        Sim_type.add(Sim_num);
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 15;
        RightPanel.add(Sim_num, gbc);
        Sim_time = new JRadioButton();
        Sim_time.setText("Number of hours:");
        Sim_type.add(Sim_time);
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 16;
        RightPanel.add(Sim_time, gbc);
        Sim_lvl = new JRadioButton();
        Sim_lvl.setText("Until CL reached:");
        Sim_type.add(Sim_lvl);
        Sim_num.setSelected(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 17;
        RightPanel.add(Sim_lvl, gbc);

        Simulations = new JSpinner(new SpinnerNumberModel(1000, 1, 100000, 1) {
            @Override
            public Object getNextValue() {
                Object nextValue = super.getValue();
                int x = (int) Double.parseDouble(nextValue.toString()) * 10;
                if (x > 100000) x = 100000;
                //Object o = x;
                return x;
            }

            @Override
            public Object getPreviousValue() {
                Object nextValue = super.getValue();
                int x = (int) Double.parseDouble(nextValue.toString()) / 10;
                if (x < 1) x = 1;
                //Object o = x;
                return x;
            }
        });
        Simulations.setToolTipText("Maximum value 100000");
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 18;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Simulations, gbc);
        SimHours = createCustomSpinner(1.0, 0, 1000, 1);
        SimHours.setVisible(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 18;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(SimHours, gbc);
        SimCL = createCustomSpinner(90, 0, 1000, 1);
        SimCL.setVisible(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 18;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(SimCL, gbc);
        class RadioButtonActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent event) {
                JRadioButton button = (JRadioButton) event.getSource();
                if (button == Sim_num) {
                    Simulations.setVisible(true);
                    SimHours.setVisible(false);
                    SimCL.setVisible(false);
                } else if (button == Sim_time) {
                    Simulations.setVisible(false);
                    SimHours.setVisible(true);
                    SimCL.setVisible(false);
                } else if (button == Sim_lvl) {
                    Simulations.setVisible(false);
                    SimHours.setVisible(false);
                    SimCL.setVisible(true);
                    Leveling.setSelected(true);
                }
            }
        }
        RadioButtonActionListener actionListener = new RadioButtonActionListener();
        Sim_num.addActionListener(actionListener);
        Sim_time.addActionListener(actionListener);
        Sim_lvl.addActionListener(actionListener);

        Leveling = new JCheckBox();
        Leveling.setSelected(false);
        Leveling.setText("Gain exp during sim");
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 19;
        RightPanel.add(Leveling, gbc);
        Update_lvls = new JButton();
        Update_lvls.setText("Update lvls from sim data");
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 22;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Update_lvls, gbc);

        Run = new JButton();
        Run.setText("Simulate");
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 20;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Run, gbc);
        SetSetup = new JCheckBox();
        SetSetup.setSelected(true);
        SetSetup.setText("Use helmet values for full set");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 14;
        gbc.gridwidth = 6;
        RightPanel.add(SetSetup, gbc);
        //RightPanel.setPreferredSize(RightPanel.getPreferredSize());
        //RightPanel.setPreferredSize(new Dimension(700, 1050));

        activeSkillsModel = new DefaultTableModel(new String[] {"Skill", "Lvl", "exp %"}, 3);
        activeSkillsModel.setColumnIdentifiers(new String[] {"Skill", "Lvl", "exp %"});
        ActiveSkills = new JTable();
        ActiveSkills.setModel(activeSkillsModel);
        ActiveSkills.setTableHeader(new JTableHeader());
        ActiveSkills.getColumnModel().getColumn(1).setCellEditor(new SpinnerEditor());
        ActiveSkills.setRowHeight(24);
        ActiveSkills.getColumnModel().getColumn(0).setPreferredWidth(160);

        passiveSkillsModel = new DefaultTableModel();
        passiveSkillsModel.setColumnIdentifiers(new String[] {"Skill", "Lvl", "exp %"});
        PassiveSkills = new JTable();
        PassiveSkills.setModel(passiveSkillsModel);

        LeftPanel.add(ActiveSkills);
        LeftPanel.add(PassiveSkills);
        LeftPane = new JScrollPane(LeftPanel);

        RootPanel.setLayout(new BoxLayout(RootPanel, BoxLayout.LINE_AXIS));
        RootPanel.add(LeftPane, gbc);
        RootPanel.add(RightPanel, gbc);
        this.setContentPane(RootPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JScrollPane Scroll = new JScrollPane(RightPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        Scroll.getVerticalScrollBar().setUnitIncrement(16);
        Scroll.getHorizontalScrollBar().setUnitIncrement(16);
        this.add(Scroll);

        this.setPreferredSize(new Dimension(1300, 1050));
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        Save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.setDialogTitle("Save setup to json file");
                fileChooser.setSelectedFile(new File(Main.getJarPath() + "/" + selected_tab.getText()));
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showSaveDialog(UserForm.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String user_path = fileChooser.getSelectedFile().getAbsolutePath();
                    if (!user_path.endsWith(".json")) {
                        user_path += ".json";
                    }
                    saveFile(user_path);
                }
            }
        });
        Load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.setDialogTitle("Load setup from json file");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showOpenDialog(UserForm.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String path = fileChooser.getSelectedFile().getAbsolutePath();
                    Setup file = loadFile(path);
                    if (file != null) {
                        tabs.put(selected_tab, file);
                        selected_tab.setText(fileChooser.getSelectedFile().getName().replaceFirst("[.][^.]+$", ""));
                    }
                    loadTab(selected_tab);
                }
            }
        });
        Update_lvls.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (player == null || player.ml == 0) {
                    JOptionPane.showMessageDialog(RightPanel, "You need to sim first! No lvling data.", "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    double new_cl_p = player.cl_exp / player.exp_to_cl(player.cl) * 100;
                    double new_ml_p = player.ml_exp / player.exp_to_ml(player.ml) * 100;
                    StringBuilder info = new StringBuilder("<html>");
                    info.append("<style>table, th, td {border-style: hidden;text-align:center;}</style>");
                    info.append("<body><table style=\"width:100%;\">");
                    info.append("<tr><th colspan=\"3\">");
                    info.append("Lvls will be updated:</p>");
                    info.append("</th></tr>");
                    info.append("<tr><td colspan=\"3\">");
                    info.append("Milestone exp:");
                    info.append("</td></tr>");
                    info.append("<tr>");
                    info.append("<td style=\"width:40%;height:10px;\">");
                    info.append(dfm.format(Milestone.getValue())).append("%");
                    info.append("</td>");
                    info.append("<td style=\"width:20%;height:10px;\">");
                    info.append("->");
                    info.append("</td>");
                    info.append("<td style=\"width:40%;height:10px;\">");
                    info.append(dfm.format(player.milestone_exp_mult * 100)).append("%");
                    info.append("</td>");

                    info.append("<tr style=\"height:10px;\"><td colspan=\"3\">");
                    info.append("CL:");
                    info.append("</td></tr>");
                    info.append("<tr style=\"height:10px;\">");
                    info.append("<td>");
                    info.append((int) player.old_cl);
                    info.append("</td>");
                    info.append("<td>");
                    info.append("->");
                    info.append("</td>");
                    info.append("<td>");
                    info.append(player.cl);
                    info.append("</td>");
                    info.append("<tr style=\"height:10px;\">");
                    info.append("<td>");
                    info.append(df2.format((player.old_cl - (int) player.old_cl) * 100)).append("%");
                    info.append("</td>");
                    info.append("<td>");
                    info.append("</td>");
                    info.append("<td>");
                    info.append(df2.format(new_cl_p)).append("%");
                    info.append("</td>");

                    info.append("<tr style=\"height:10px;\"><td colspan=\"3\">");
                    info.append("ML:");
                    info.append("</td></tr>");
                    info.append("<tr style=\"height:10px;\">");
                    info.append("<td>");
                    info.append((int) player.old_ml);
                    info.append("</td>");
                    info.append("<td>");
                    info.append("->");
                    info.append("</td>");
                    info.append("<td>");
                    info.append(player.ml);
                    info.append("</td>");
                    info.append("<tr style=\"height:10px;\">");
                    info.append("<td>");
                    info.append(df2.format((player.old_ml - (int) player.old_ml) * 100)).append("%");
                    info.append("</td>");
                    info.append("<td>");
                    info.append("</td>");
                    info.append("<td>");
                    info.append(df2.format(new_ml_p)).append("%");
                    info.append("</td>");
                    for (ActiveSkill a : player.active_skills.values()) {
                        if (a.lvl != a.old_lvl) {
                            info.append("<tr style=\"height:10px;\"><td colspan=\"3\">");
                            info.append(a.name);
                            info.append("</td></tr>");
                            info.append("<tr style=\"height:10px;\">");
                            info.append("<td>");
                            info.append((int) a.old_lvl);
                            info.append("</td>");
                            info.append("<td>");
                            info.append("->");
                            info.append("</td>");
                            info.append("<td>");
                            info.append(a.lvl);
                            info.append("</td>");
                            info.append("<tr style=\"height:10px;\">");
                            info.append("<td>");
                            info.append(df2.format((a.old_lvl - (int) a.old_lvl) * 100)).append("%");
                            info.append("</td>");
                            info.append("<td>");
                            info.append("</td>");
                            info.append("<td>");
                            info.append(df2.format((a.exp / a.need_for_lvl(a.lvl)) * 100)).append("%");
                            info.append("</td>");
                        }
                    }
                    for (PassiveSkill a : player.passives.values()) {
                        if (a.lvl != a.old_lvl) {
                            info.append("<tr style=\"height:10px;\"><td colspan=\"3\">");
                            info.append(a.name);
                            info.append("</td></tr>");
                            info.append("<tr style=\"height:10px;\">");
                            info.append("<td>");
                            info.append((int) a.old_lvl);
                            info.append("</td>");
                            info.append("<td>");
                            info.append("->");
                            info.append("</td>");
                            info.append("<td>");
                            info.append(a.lvl);
                            info.append("</td>");
                            info.append("<tr style=\"height:10px;\">");
                            info.append("<td>");
                            info.append(df2.format((a.old_lvl - (int) a.old_lvl) * 100)).append("%");
                            info.append("</td>");
                            info.append("<td>");
                            info.append("</td>");
                            info.append("<td>");
                            info.append(df2.format((a.exp / a.need_for_lvl(a.lvl)) * 100)).append("%");
                            info.append("</td>");
                        }
                    }
                    info.append("</table>");
                    info.append("</body>");
                    info.append("</html>");
                    int selectedOption = JOptionPane.showConfirmDialog(RightPanel, info, "Confirm", JOptionPane.YES_NO_OPTION);
                    if (selectedOption == JOptionPane.YES_OPTION) {
                        Milestone.setValue(player.milestone_exp_mult * 100);
                        CL.setValue(player.cl);
                        ML.setValue(player.ml);
                        CL_p.setValue(new_cl_p);
                        ML_p.setValue(new_ml_p);
                        for (ActiveSkill a : player.active_skills.values()) {
                            if (a.lvl != a.old_lvl) {
                                if (Skill1.getSelectedItem().toString().equals(a.name)) {
                                    Skill1_lvl.setValue(a.lvl);
                                    Skill1_lvl_p.setValue((a.exp / a.need_for_lvl(a.lvl)) * 100);
                                }
                                if (Skill2.getSelectedItem().toString().equals(a.name)) {
                                    Skill2_lvl.setValue(a.lvl);
                                    Skill2_lvl_p.setValue((a.exp / a.need_for_lvl(a.lvl)) * 100);
                                }
                                if (Skill3.getSelectedItem().toString().equals(a.name)) {
                                    Skill3_lvl.setValue(a.lvl);
                                    Skill3_lvl_p.setValue((a.exp / a.need_for_lvl(a.lvl)) * 100);
                                }
                            }
                        }
                        for (PassiveSkill a : player.passives.values()) {
                            if (a.lvl != a.old_lvl) {
                                if (Pskill1.getSelectedItem().toString().equals(a.name)) {
                                    Pskill1_lvl.setValue(a.lvl);
                                    Pskill1_lvl_p.setValue((a.exp / a.need_for_lvl(a.lvl)) * 100);
                                }
                                if (Pskill2.getSelectedItem().toString().equals(a.name)) {
                                    Pskill2_lvl.setValue(a.lvl);
                                    Pskill2_lvl_p.setValue((a.exp / a.need_for_lvl(a.lvl)) * 100);
                                }
                                if (Pskill3.getSelectedItem().toString().equals(a.name)) {
                                    Pskill3_lvl.setValue(a.lvl);
                                    Pskill3_lvl_p.setValue((a.exp / a.need_for_lvl(a.lvl)) * 100);
                                }
                            }
                        }
                    }
                }
            }
        });
        Run.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Skill1.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(RightPanel, "You need to setup first active skill", "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    if (Sim_lvl.isSelected() && ((int) Double.parseDouble(CL.getValue().toString()) >= (int) Double.parseDouble(SimCL.getValue().toString()))) {
                        JOptionPane.showMessageDialog(RightPanel,
                                "You need to setup target CL higher than your current", "Warning",
                                JOptionPane.WARNING_MESSAGE);
                    } else {
                        long startTime = System.nanoTime();
                        Main.game_version = (int) Double.parseDouble(GameVersion.getSelectedItem().toString());
                        setupPotions();
                        simulation.alchemy_lvl = (int) Alchemy_lvl.getValue();
                        simulation.crafting_lvl = (int) Crafting_lvl.getValue();
                        player.milestone_exp_mult = (double) Double.parseDouble(Milestone.getValue().toString()) / 100;
                        player.old_milestone_exp_mult = player.milestone_exp_mult;
                        double cl =
                                (int) Double.parseDouble(CL.getValue().toString()) + Double.parseDouble(CL_p.getValue().toString()) / 100;
                        double ml =
                                (int) Double.parseDouble(ML.getValue().toString()) + Double.parseDouble(ML_p.getValue().toString()) / 100;
                        player.setCLML(cl, ml);
                        player.old_cl = cl;
                        player.old_ml = ml;
                        player.clear_skills_recorded_data();
                        simulation.time_to_respawn = -1;
                        simulation.sim_limit = (int) Double.parseDouble(Simulations.getValue().toString());
                        simulation.time_limit = (double) Double.parseDouble(SimHours.getValue().toString());
                        simulation.cl_limit = (int) Double.parseDouble(SimCL.getValue().toString());
                        simulation.player = player;
                        player.lvling = Leveling.isSelected();
                        if (Sim_num.isSelected()) simulation.sim_type = 1;
                        if (Sim_time.isSelected()) simulation.sim_type = 2;
                        if (Sim_lvl.isSelected()) {
                            simulation.sim_type = 3;
                            player.lvling = true;
                        }
                        Zone z = (Zone) Enemy.getSelectedItem();
                        simulation.zone = z;
                        player.zone = z;
                        player.zone.clear_recorded_data();
                        setupEquipment();
                        setupPassives();
                        simulation.run(Skill1.getSelectedItem().toString(),
                                (int) Double.parseDouble(Skill1_lvl.getValue().toString()) + Double.parseDouble(Skill1_lvl_p.getValue().toString()) / 100,
                                (SkillMod) Skill1_mod.getSelectedItem(),
                                (int) Double.parseDouble(Skill1_s.getValue().toString()),
                                Skill2.getSelectedItem().toString(),
                                (int) Double.parseDouble(Skill2_lvl.getValue().toString()) + Double.parseDouble(Skill2_lvl_p.getValue().toString()) / 100,
                                (SkillMod) Skill2_mod.getSelectedItem(),
                                (int) Double.parseDouble(Skill2_s.getValue().toString()),
                                Skill3.getSelectedItem().toString(),
                                (int) Double.parseDouble(Skill3_lvl.getValue().toString()) + Double.parseDouble(Skill3_lvl_p.getValue().toString()) / 100,
                                (SkillMod) Skill3_mod.getSelectedItem(),
                                (int) Double.parseDouble(Skill3_s.getValue().toString()),
                                (int) Double.parseDouble(Reroll.getValue().toString()));
                        if (SetupInfo.isSelected()) {
                            Result.setText(simulation.full_result);
                        } else {
                            Result.setText(simulation.essential_result);
                        }
                        Stats.setText(player.getAllStats());
                        // Calculate the execution time in milliseconds
                        long executionTime = (System.nanoTime() - startTime) / 1000000;
//                    System.out.println(executionTime + "ms");
                    }
                }

            }
        });
        SetupInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showResult();
            }
        });
        PlayerClass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectClass(PlayerClass.getSelectedItem().toString());
            }
        });
        MH_name.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (MH_name.getSelectedItem() != null) {
                    String item_name = MH_name.getSelectedItem().toString();
                    if (twohanded.contains(item_name)) {
                        OH_name.setSelectedItem("None");
                    }
                }
            }
        });
        Helmet_name.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SetSetup.isSelected() && Helmet_name.getSelectedItem() != null && Helmet_name.getSelectedItem() != "None") {
                    Equipment eq = helmet.get(Helmet_name.getSelectedItem().toString());
                    if (eq != null) {
                        String set = eq.displayName;
                        Chest_name.setSelectedItem(findItemFromSet(set, chest));
                        Pants_name.setSelectedItem(findItemFromSet(set, pants));
                        Bracer_name.setSelectedItem(findItemFromSet(set, bracers));
                        Boots_name.setSelectedItem(findItemFromSet(set, boots));
                    }
                }
            }
        });
        GameVersion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((int) Double.parseDouble(GameVersion.getSelectedItem().toString()) >= 1535) {
                    Reroll.setVisible(false);
                    Reroll_label.setVisible(false);
                } else {
                    Reroll.setVisible(true);
                    Reroll_label.setVisible(true);
                }
            }
        });
        Helmet_tier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SetSetup.isSelected()) {
                    Chest_tier.setSelectedItem(Helmet_tier.getSelectedItem());
                    Pants_tier.setSelectedItem(Helmet_tier.getSelectedItem());
                    Bracer_tier.setSelectedItem(Helmet_tier.getSelectedItem());
                    Boots_tier.setSelectedItem(Helmet_tier.getSelectedItem());
                }
            }
        });
        Helmet_lvl.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (SetSetup.isSelected()) {
                    Chest_lvl.setValue(Helmet_lvl.getValue());
                    Pants_lvl.setValue(Helmet_lvl.getValue());
                    Bracer_lvl.setValue(Helmet_lvl.getValue());
                    Boots_lvl.setValue(Helmet_lvl.getValue());
                }
            }
        });
        loadEquipment();
        selected_tab = createTab("default");
        Setup default_setup = loadFile(Main.getJarPath() + "/default.json");
        if (default_setup == null) default_setup = new Setup();
        tabs.put(selected_tab, default_setup);
        loadTab(selected_tab);
    }

    public static class SpinnerEditor extends DefaultCellEditor
    {
        JSpinner spinner;
        JSpinner.DefaultEditor editor;
        JTextField textField;
        boolean valueSet;

        // Initializes the spinner.
        public SpinnerEditor() {
            super(new JTextField());
            spinner = new JSpinner();
            editor = ((JSpinner.DefaultEditor)spinner.getEditor());
            textField = editor.getTextField();
            textField.addFocusListener( new FocusListener() {
                public void focusGained( FocusEvent fe ) {
                    //System.err.println("Got focus");
                    //textField.setSelectionStart(0);
                    //textField.setSelectionEnd(1);
                    SwingUtilities.invokeLater( new Runnable() {
                        public void run() {
                            if ( valueSet ) {
                                textField.setCaretPosition(1);
                            }
                        }
                    });
                }
                public void focusLost( FocusEvent fe ) {
                }
            });
            textField.addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent ae ) {
                    stopCellEditing();
                }
            });
        }

        // Prepares the spinner component and returns it.
        public Component getTableCellEditorComponent(
                JTable table, Object value, boolean isSelected, int row, int column
        ) {
            if ( !valueSet ) {
                spinner.setValue(value);
            }
            SwingUtilities.invokeLater( new Runnable() {
                public void run() {
                    textField.requestFocus();
                }
            });
            return spinner;
        }

        public boolean isCellEditable( EventObject eo ) {
            //System.err.println("isCellEditable");
            if ( eo instanceof KeyEvent ) {
                KeyEvent ke = (KeyEvent)eo;
                //System.err.println("key event: "+ke.getKeyChar());
                textField.setText(String.valueOf(ke.getKeyChar()));
                //textField.select(1,1);
                //textField.setCaretPosition(1);
                //textField.moveCaretPosition(1);
                valueSet = true;
            } else {
                valueSet = false;
            }
            return true;
        }

        // Returns the spinners current value.
        public Object getCellEditorValue() {
            return spinner.getValue();
        }

        public boolean stopCellEditing() {
            //System.err.println("Stopping edit");
            try {
                editor.commitEdit();
                spinner.commitEdit();
            } catch ( java.text.ParseException e ) {
//                JOptionPane.showMessageDialog(null,
//                        "Invalid value, discarding.");
            }
            return super.stopCellEditing();
        }
    }

    private String findItemFromSet(String name, LinkedHashMap<String, Equipment> set) {
        for (Equipment e : set.values()) {
            if (e.displayName.equals(name)) {
                return e.name;
            }
        }
        return "None";
    }

    private void selectClass(String name) {
        player.setClass(name);
        DefaultComboBoxModel<String> active1 =
                new DefaultComboBoxModel<>(new Vector<>(player.getAvailableActiveSkills()));
        DefaultComboBoxModel<String> active2 =
                new DefaultComboBoxModel<>(new Vector<>(player.getAvailableActiveSkills()));
        DefaultComboBoxModel<String> active3 =
                new DefaultComboBoxModel<>(new Vector<>(player.getAvailableActiveSkills()));
        DefaultComboBoxModel<String> passive1 =
                new DefaultComboBoxModel<>(new Vector<>(player.getAvailablePassiveSkills()));
        DefaultComboBoxModel<String> passive2 =
                new DefaultComboBoxModel<>(new Vector<>(player.getAvailablePassiveSkills()));
        DefaultComboBoxModel<String> passive3 =
                new DefaultComboBoxModel<>(new Vector<>(player.getAvailablePassiveSkills()));
        Skill1.setModel(active1);
        Skill2.setModel(active2);
        Skill3.setModel(active3);
        Pskill1.setModel(passive1);
        Pskill2.setModel(passive2);
        Pskill3.setModel(passive3);
        clearSelections();
        activeSkillsModel.setRowCount(0);
        for (String skill : player.active_skills.keySet()) {
            activeSkillsModel.addRow(new Object[]{skill, player.active_skills.get(skill).lvl,
                    player.active_skills.get(skill).exp});
        }

    }

    private void clearSelections() {
        Skill1.setSelectedIndex(1);
        Skill2.setSelectedIndex(0);
        Skill3.setSelectedIndex(0);
        Pskill1.setSelectedIndex(0);
        Pskill2.setSelectedIndex(0);
        Pskill3.setSelectedIndex(0);
    }

    private void setupPotions() {
        String type1 = null;
        String type2 = null;
        String type3 = null;
        int tier1 = 0;
        int tier2 = 0;
        int tier3 = 0;
        int threshold1 = 0;
        int threshold2 = 0;
        int threshold3 = 0;
        if (!Potion1.getSelectedItem().toString().equals("None")) {
            type1 = Potion1.getSelectedItem().toString().substring(0, 2);
            tier1 = (int) Double.parseDouble(Potion1.getSelectedItem().toString().substring(4));
            threshold1 = (int) Double.parseDouble(Potion1_t.getValue().toString());
        }
        if (!Potion2.getSelectedItem().toString().equals("None")) {
            type2 = Potion2.getSelectedItem().toString().substring(0, 2);
            tier2 = (int) Double.parseDouble(Potion2.getSelectedItem().toString().substring(4));
            threshold2 = (int) Double.parseDouble(Potion2_t.getValue().toString());
        }
        if (!Potion3.getSelectedItem().toString().equals("None")) {
            type3 = Potion3.getSelectedItem().toString().substring(0, 2);
            tier3 = (int) Double.parseDouble(Potion3.getSelectedItem().toString().substring(4));
            threshold3 = (int) Double.parseDouble(Potion3_t.getValue().toString());
        }
        simulation.setupPotions(type1, tier1, threshold1, type2, tier2, threshold2, type3, tier3, threshold3);
    }

    private void setupEquipment() {
        player.equipment.clear();
        player.equipment.put("MH", mh.get(MH_name.getSelectedItem().toString()));
        player.equipment.put("OH", oh.get(OH_name.getSelectedItem().toString()));
        player.equipment.put("Helmet", helmet.get(Helmet_name.getSelectedItem().toString()));
        player.equipment.put("Chest", chest.get(Chest_name.getSelectedItem().toString()));
        player.equipment.put("Pants", pants.get(Pants_name.getSelectedItem().toString()));
        player.equipment.put("Bracer", bracers.get(Bracer_name.getSelectedItem().toString()));
        player.equipment.put("Boots", boots.get(Boots_name.getSelectedItem().toString()));
        player.equipment.put("Accessory1", acc1.get(Accessory1_name.getSelectedItem().toString()));
        player.equipment.put("Accessory2", acc2.get(Accessory2_name.getSelectedItem().toString()));
        player.equipment.put("Necklace", neck.get(Necklace_name.getSelectedItem().toString()));
        mh.get(MH_name.getSelectedItem().toString()).setQualityLvl((Equipment.Quality) MH_tier.getSelectedItem(),
                (int) Double.parseDouble(MH_lvl.getValue().toString()));
        oh.get(OH_name.getSelectedItem().toString()).setQualityLvl((Equipment.Quality) OH_tier.getSelectedItem(),
                (int) Double.parseDouble(OH_lvl.getValue().toString()));
        helmet.get(Helmet_name.getSelectedItem().toString()).setQualityLvl((Equipment.Quality) Helmet_tier.getSelectedItem(),
                (int) Double.parseDouble(Helmet_lvl.getValue().toString()));
        chest.get(Chest_name.getSelectedItem().toString()).setQualityLvl((Equipment.Quality) Chest_tier.getSelectedItem(),
                (int) Double.parseDouble(Chest_lvl.getValue().toString()));
        pants.get(Pants_name.getSelectedItem().toString()).setQualityLvl((Equipment.Quality) Pants_tier.getSelectedItem(),
                (int) Double.parseDouble(Pants_lvl.getValue().toString()));
        bracers.get(Bracer_name.getSelectedItem().toString()).setQualityLvl((Equipment.Quality) Bracer_tier.getSelectedItem(),
                (int) Double.parseDouble(Bracer_lvl.getValue().toString()));
        boots.get(Boots_name.getSelectedItem().toString()).setQualityLvl((Equipment.Quality) Boots_tier.getSelectedItem(),
                (int) Double.parseDouble(Boots_lvl.getValue().toString()));
        acc1.get(Accessory1_name.getSelectedItem().toString()).setQualityLvl((Equipment.Quality) Accessory1_tier.getSelectedItem(),
                (int) Double.parseDouble(Accessory1_lvl.getValue().toString()));
        acc2.get(Accessory2_name.getSelectedItem().toString()).setQualityLvl((Equipment.Quality) Accessory2_tier.getSelectedItem(),
                (int) Double.parseDouble(Accessory2_lvl.getValue().toString()));
        neck.get(Necklace_name.getSelectedItem().toString()).setQualityLvl((Equipment.Quality) Necklace_tier.getSelectedItem(),
                (int) Double.parseDouble(Necklace_lvl.getValue().toString()));
    }

    private void setupPassives() {
        String[] passives = new String[3];
        if (!Pskill1.getSelectedItem().toString().equals("None")) {
            player.setPassiveLvl(Pskill1.getSelectedItem().toString(),
                    (int) Double.parseDouble(Pskill1_lvl.getValue().toString()) + Double.parseDouble(Pskill1_lvl_p.getValue().toString()) / 100);
            passives[0] = Pskill1.getSelectedItem().toString();
        }
        if (!Pskill2.getSelectedItem().toString().equals("None")) {
            player.setPassiveLvl(Pskill2.getSelectedItem().toString(),
                    (int) Double.parseDouble(Pskill2_lvl.getValue().toString()) + Double.parseDouble(Pskill2_lvl_p.getValue().toString()) / 100);
            passives[1] = Pskill2.getSelectedItem().toString();
        }
        if (!Pskill3.getSelectedItem().toString().equals("None")) {
            player.setPassiveLvl(Pskill3.getSelectedItem().toString(),
                    (int) Double.parseDouble(Pskill3_lvl.getValue().toString()) + Double.parseDouble(Pskill3_lvl_p.getValue().toString()) / 100);
            passives[2] = Pskill3.getSelectedItem().toString();
        }
        player.enablePassives(passives);
    }

    private void loadEquipment() {
        try {
            JsonReader weaponReader = new JsonReader(new FileReader("data/Weapons.json"));
            Map<String, Map> weaponDataMap = gson.fromJson(weaponReader, Map.class);
            JsonReader armorReader = new JsonReader(new FileReader("data/Armor.json"));
            Map<String, Map> armorDataMap = gson.fromJson(armorReader, Map.class);
            JsonReader accessoryReader = new JsonReader(new FileReader("data/Accessories.json"));
            Map<String, Map> accessoryDataMap = gson.fromJson(accessoryReader, Map.class);
            twohanded.clear();
            clearSlot(MH_name, mh);
            clearSlot(OH_name, oh);
            clearSlot(Helmet_name, helmet);
            clearSlot(Chest_name, chest);
            clearSlot(Pants_name, pants);
            clearSlot(Bracer_name, bracers);
            clearSlot(Boots_name, boots);
            clearSlot(Accessory1_name, acc1);
            clearSlot(Accessory2_name, acc2);
            clearSlot(Necklace_name, neck);
            weaponDataMap.forEach((slot, value) -> {
                for (Object item : value.entrySet()) {
                    String name = ((Map.Entry<String, Map>) item).getKey();
                    Map weapon_data = ((Map.Entry<String, Map>) item).getValue();
                    if (slot.equals("MH")) {
                        mh.put(name, new Equipment(name, slot, weapon_data));
                        MH_name.addItem(name);
                    }
                    if (slot.equals("2H")) {
                        mh.put(name, new Equipment(name, slot, weapon_data));
                        MH_name.addItem(name);
                        twohanded.add(name);
                    }
                    if (slot.equals("MH") || slot.equals("OH")) {
                        if (!(weapon_data.containsKey("MH_ONLY") && (boolean) weapon_data.get("MH_ONLY"))) {
                            oh.put(name, new Equipment(name, slot, weapon_data));
                            OH_name.addItem(name);
                        }
                    }
                }
            });
            armorDataMap.forEach((slot, value) -> {
                for (Object item : value.entrySet()) {
                    String name = ((Map.Entry<String, Map>) item).getKey();
                    Equipment e = new Equipment(name, slot,
                            ((Map.Entry<String, Map>) item).getValue());
                    if (slot.equals("HEADGEAR")) {
                        helmet.put(name, e);
                        Helmet_name.addItem(name);
                        if (e.displayName.equals("Leather")) Helmet_name.setSelectedItem(name);
                    }
                    if (slot.equals("BOOTS")) {
                        boots.put(name, e);
                        Boots_name.addItem(name);
                        if (e.displayName.equals("Leather")) Boots_name.setSelectedItem(name);
                    }
                    if (slot.equals("BRACERS")) {
                        bracers.put(name, e);
                        Bracer_name.addItem(name);
                        if (e.displayName.equals("Leather")) Bracer_name.setSelectedItem(name);
                    }
                    if (slot.equals("PANTS")) {
                        pants.put(name, e);
                        Pants_name.addItem(name);
                        if (e.displayName.equals("Leather")) Pants_name.setSelectedItem(name);
                    }
                    if (slot.equals("CHEST")) {
                        chest.put(name, e);
                        Chest_name.addItem(name);
                        if (e.displayName.equals("Leather")) Chest_name.setSelectedItem(name);
                    }
                }
            });
            accessoryDataMap.forEach((slot, value) -> {
                for (Object item : value.entrySet()) {
                    String name = ((Map.Entry<String, Map>) item).getKey();
                    if (slot.equals("NECK")) {
                        neck.put(name, new Equipment(name, slot,
                                ((Map.Entry<String, Map>) item).getValue()));
                        Necklace_name.addItem(name);
                        if (name.equals("Metal Necklace")) Necklace_name.setSelectedItem(name);
                    }
                    if (slot.equals("RING")) {
                        acc1.put(name, new Equipment(name, slot,
                                ((Map.Entry<String, Map>) item).getValue()));
                        Accessory1_name.addItem(name);
                        if (name.equals("Golden Belt")) Accessory1_name.setSelectedItem(name);
                        acc2.put(name, new Equipment(name, slot,
                                ((Map.Entry<String, Map>) item).getValue()));
                        Accessory2_name.addItem(name);
                        if (name.equals("Metal Ring")) Accessory2_name.setSelectedItem(name);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearSlot(JComboBox slot, LinkedHashMap<String, Equipment> hm) {
        slot.removeAllItems();
        slot.addItem("None");
        hm.clear();
        hm.put("None", new Equipment("None", "None"));
    }

    private void saveFile(String path) {
        try {
            JsonWriter writer = new JsonWriter(new FileWriter(path));
            gson.toJson(saveSetup(), Setup.class, writer);
            writer.close();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(RightPanel, "Some field has illegal value!", "Exception",
                    JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(RightPanel, ex.getMessage(), "Exception",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private Setup saveSetup() {
        Setup data = new Setup();
        data.accessory1_lvl = (int) Double.parseDouble(Accessory1_lvl.getValue().toString());
        data.accessory1_name = Accessory1_name.getSelectedItem().toString();
        data.accessory1_tier = (Equipment.Quality) Accessory1_tier.getSelectedItem();
        data.accessory2_lvl = (int) Double.parseDouble(Accessory2_lvl.getValue().toString());
        data.accessory2_name = Accessory2_name.getSelectedItem().toString();
        data.accessory2_tier = (Equipment.Quality) Accessory2_tier.getSelectedItem();
        data.alchemy_lvl = (int) Double.parseDouble(Alchemy_lvl.getValue().toString());
        data.boots_lvl = (int) Double.parseDouble(Boots_lvl.getValue().toString());
        data.boots_name = Boots_name.getSelectedItem().toString();
        data.boots_tier = (Equipment.Quality) Boots_tier.getSelectedItem();
        data.bracer_lvl = (int) Double.parseDouble(Bracer_lvl.getValue().toString());
        data.bracer_name = Bracer_name.getSelectedItem().toString();
        data.bracer_tier = (Equipment.Quality) Bracer_tier.getSelectedItem();
        data.chest_lvl = (int) Double.parseDouble(Chest_lvl.getValue().toString());
        data.chest_name = Chest_name.getSelectedItem().toString();
        data.chest_tier = (Equipment.Quality) Chest_tier.getSelectedItem();
        data.cl = ((int) Double.parseDouble(CL.getValue().toString()) + Double.parseDouble(CL_p.getValue().toString()) / 100);
        data.crafting_lvl = (int) Double.parseDouble(Crafting_lvl.getValue().toString());
        data.zone = (Zone) Enemy.getSelectedItem();
        data.gameversion = GameVersion.getSelectedItem().toString();
        data.helmet_lvl = (int) Double.parseDouble(Helmet_lvl.getValue().toString());
        data.helmet_name = Helmet_name.getSelectedItem().toString();
        data.helmet_tier = (Equipment.Quality) Helmet_tier.getSelectedItem();
        data.mh_lvl = (int) Double.parseDouble(MH_lvl.getValue().toString());
        data.mh_name = MH_name.getSelectedItem().toString();
        data.mh_tier = (Equipment.Quality) MH_tier.getSelectedItem();
        data.milestone = Double.parseDouble(Milestone.getValue().toString());
        data.ml = ((int) Double.parseDouble(ML.getValue().toString()) + Double.parseDouble(ML_p.getValue().toString()) / 100);
        data.necklace_lvl = (int) Double.parseDouble(Necklace_lvl.getValue().toString());
        data.necklace_name = Necklace_name.getSelectedItem().toString();
        data.necklace_tier = (Equipment.Quality) Necklace_tier.getSelectedItem();
        data.oh_lvl = (int) Double.parseDouble(OH_lvl.getValue().toString());
        data.oh_name = OH_name.getSelectedItem().toString();
        data.oh_tier = (Equipment.Quality) OH_tier.getSelectedItem();
        data.pants_lvl = (int) Double.parseDouble(Pants_lvl.getValue().toString());
        data.pants_name = Pants_name.getSelectedItem().toString();
        data.pants_tier = (Equipment.Quality) Pants_tier.getSelectedItem();
        data.playerclass = PlayerClass.getSelectedItem().toString();
        data.potion1 = Potion1.getSelectedItem().toString();
        data.potion1_t = (int) Double.parseDouble(Potion1_t.getValue().toString());
        data.potion2 = Potion2.getSelectedItem().toString();
        data.potion2_t = (int) Double.parseDouble(Potion2_t.getValue().toString());
        data.potion3 = Potion3.getSelectedItem().toString();
        data.potion3_t = (int) Double.parseDouble(Potion3_t.getValue().toString());
        data.pskill1 = Pskill1.getSelectedItem().toString();
        data.pskill1_lvl = ((int) Double.parseDouble(Pskill1_lvl.getValue().toString())
                + Double.parseDouble(Pskill1_lvl_p.getValue().toString()) / 100);
        data.pskill2 = Pskill2.getSelectedItem().toString();
        data.pskill2_lvl = ((int) Double.parseDouble(Pskill2_lvl.getValue().toString())
                + Double.parseDouble(Pskill2_lvl_p.getValue().toString()) / 100);
        data.pskill3 = Pskill3.getSelectedItem().toString();
        data.pskill3_lvl = ((int) Double.parseDouble(Pskill3_lvl.getValue().toString())
                + Double.parseDouble(Pskill3_lvl_p.getValue().toString()) / 100);
        data.reroll = (int) Double.parseDouble(Reroll.getValue().toString());
        data.result_essential = simulation.essential_result;
        data.result_full = simulation.full_result;
        data.setsetup = SetSetup.isSelected();
        data.setupinfo = SetupInfo.isSelected();
        data.skill1 = Skill1.getSelectedItem().toString();
        data.skill1_lvl = ((int) Double.parseDouble(Skill1_lvl.getValue().toString())
                + Double.parseDouble(Skill1_lvl_p.getValue().toString()) / 100);
        data.skill1_mod = (SkillMod) Skill1_mod.getSelectedItem();
        data.skill1_s = (int) Double.parseDouble(Skill1_s.getValue().toString());
        data.skill2 = Skill2.getSelectedItem().toString();
        data.skill2_lvl = ((int) Double.parseDouble(Skill2_lvl.getValue().toString())
                + Double.parseDouble(Skill2_lvl_p.getValue().toString()) / 100);
        data.skill2_mod = (SkillMod) Skill2_mod.getSelectedItem();
        data.skill2_s = (int) Double.parseDouble(Skill2_s.getValue().toString());
        data.skill3 = Skill3.getSelectedItem().toString();
        data.skill3_lvl = ((int) Double.parseDouble(Skill3_lvl.getValue().toString())
                + Double.parseDouble(Skill3_lvl_p.getValue().toString()) / 100);
        data.skill3_mod = (SkillMod) Skill3_mod.getSelectedItem();
        data.skill3_s = (int) Double.parseDouble(Skill3_s.getValue().toString());
        data.stats = Stats.getText();
        if (Sim_num.isSelected()) data.sim_type = 1;
        if (Sim_time.isSelected()) data.sim_type = 2;
        if (Sim_lvl.isSelected()) data.sim_type = 3;
        data.simulations = (int) Double.parseDouble(Simulations.getValue().toString());
        data.sim_hours = Double.parseDouble(SimHours.getValue().toString());
        data.sim_cl = (int) Double.parseDouble(SimCL.getValue().toString());
        data.leveling = Leveling.isSelected();
        return data;
    }

    private Setup loadFile(String path) {
        Setup file = null;
        try {
            File def = new File(path);
            JsonReader reader = new JsonReader(new FileReader(def));
            file = gson.fromJson(reader, Setup.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(RightPanel, ex.getMessage(), "Exception",
                    JOptionPane.WARNING_MESSAGE);
        }
        return file;
    }

    private void loadSetup(Setup data) {
        SetSetup.setSelected(data.setsetup);
        SetupInfo.setSelected(data.setupinfo);
        Helmet_lvl.setValue(data.helmet_lvl);
        Helmet_name.setSelectedItem(data.helmet_name);
        Helmet_tier.setSelectedItem(data.helmet_tier);
        Alchemy_lvl.setValue(data.alchemy_lvl);
        Accessory1_lvl.setValue(data.accessory1_lvl);
        Accessory1_name.setSelectedItem(data.accessory1_name);
        Accessory1_tier.setSelectedItem(data.accessory1_tier);
        Accessory2_lvl.setValue(data.accessory2_lvl);
        Accessory2_name.setSelectedItem(data.accessory2_name);
        Accessory2_tier.setSelectedItem(data.accessory2_tier);
        Boots_lvl.setValue(data.boots_lvl);
        Boots_name.setSelectedItem(data.boots_name);
        Boots_tier.setSelectedItem(data.boots_tier);
        Bracer_lvl.setValue(data.bracer_lvl);
        Bracer_name.setSelectedItem(data.bracer_name);
        Bracer_tier.setSelectedItem(data.bracer_tier);
        Chest_lvl.setValue(data.chest_lvl);
        Chest_name.setSelectedItem(data.chest_name);
        Chest_tier.setSelectedItem(data.chest_tier);
        CL.setValue((int) data.cl);
        ML.setValue((int) data.ml);
        CL_p.setValue((data.cl - (int) data.cl) * 100);
        ML_p.setValue((data.ml - (int) data.ml) * 100);
        Skill1_lvl_p.setValue((data.skill1_lvl - (int) data.skill1_lvl) * 100);
        Skill2_lvl_p.setValue((data.skill2_lvl - (int) data.skill2_lvl) * 100);
        Skill3_lvl_p.setValue((data.skill3_lvl - (int) data.skill3_lvl) * 100);
        Pskill1_lvl_p.setValue((data.pskill1_lvl - (int) data.pskill1_lvl) * 100);
        Pskill2_lvl_p.setValue((data.pskill2_lvl - (int) data.pskill2_lvl) * 100);
        Pskill3_lvl_p.setValue((data.pskill3_lvl - (int) data.pskill3_lvl) * 100);
        Crafting_lvl.setValue(data.crafting_lvl);
        if (data.enemy.length() > 0) {
            data.zone = switch (data.enemy) {
                case "Devil" -> Zone.z9;
                case "Shax" -> Zone.z10;
                case "Dagon" -> Zone.z11;
                case "Lamia" -> Zone.z12;
                default -> Zone.z8;
            };
        }
        Enemy.setSelectedItem(data.zone);
        GameVersion.setSelectedItem((int) Double.parseDouble(data.gameversion));
        MH_lvl.setValue(data.mh_lvl);
        MH_name.setSelectedItem(data.mh_name);
        MH_tier.setSelectedItem(data.mh_tier);
        Milestone.setValue(data.milestone);
        Necklace_lvl.setValue(data.necklace_lvl);
        Necklace_name.setSelectedItem(data.necklace_name);
        Necklace_tier.setSelectedItem(data.necklace_tier);
        OH_lvl.setValue(data.oh_lvl);
        OH_name.setSelectedItem(data.oh_name);
        OH_tier.setSelectedItem(data.oh_tier);
        Pants_lvl.setValue(data.pants_lvl);
        Pants_name.setSelectedItem(data.pants_name);
        Pants_tier.setSelectedItem(data.pants_tier);
        PlayerClass.setSelectedItem(data.playerclass);
        Potion1.setSelectedItem(data.potion1);
        Potion1_t.setValue(data.potion1_t);
        Potion2.setSelectedItem(data.potion2);
        Potion2_t.setValue(data.potion2_t);
        Potion3.setSelectedItem(data.potion3);
        Potion3_t.setValue(data.potion3_t);
        Pskill1.setSelectedItem(data.pskill1);
        Pskill1_lvl.setValue((int) data.pskill1_lvl);
        Pskill2.setSelectedItem(data.pskill2);
        Pskill2_lvl.setValue((int) data.pskill2_lvl);
        Pskill3.setSelectedItem(data.pskill3);
        Pskill3_lvl.setValue((int) data.pskill3_lvl);
        Reroll.setValue(data.reroll);
        simulation.essential_result = data.result_essential;
        simulation.full_result = data.result_full;
        SetSetup.setSelected(data.setsetup);
        SetupInfo.setSelected(data.setupinfo);
        Skill1.setSelectedItem(data.skill1);
        Skill1_lvl.setValue((int) data.skill1_lvl);
        Skill1_mod.setSelectedItem(data.skill1_mod != null ? data.skill1_mod : SkillMod.Basic);
        Skill1_s.setValue(data.skill1_s);
        Skill2.setSelectedItem(data.skill2);
        Skill2_lvl.setValue((int) data.skill2_lvl);
        Skill2_mod.setSelectedItem(data.skill2_mod != null ? data.skill2_mod : SkillMod.Basic);
        Skill2_s.setValue(data.skill2_s);
        Skill3.setSelectedItem(data.skill3);
        Skill3_lvl.setValue((int) data.skill3_lvl);
        Skill3_mod.setSelectedItem(data.skill3_mod != null ? data.skill3_mod : SkillMod.Basic);
        Skill3_s.setValue(data.skill3_s);
        Stats.setText(data.stats);
        switch (data.sim_type) {
            case 2:
                Sim_time.doClick();
                break;
            case 3:
                Sim_lvl.doClick();
                break;
            default:
                Sim_num.doClick();
                break;
        }
        Simulations.setValue(data.simulations);
        SimHours.setValue(data.sim_hours);
        SimCL.setValue(data.sim_cl);
        Leveling.setSelected(data.leveling);
        showResult();
    }

    private void showResult() {
        if (SetupInfo.isSelected()) {
            Result.setText(simulation.full_result);
        } else {
            Result.setText(simulation.essential_result);
        }
    }

    public JSpinner createCustomSpinner(double start, double min, double max, double step) {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(start, min, max, step));
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner, "####.####");
        DecimalFormat format = editor.getFormat();
        format.setDecimalFormatSymbols(dfs);
        spinner.setEditor(editor);
        return spinner;
    }

    public JSpinner createCustomSpinner(int start, int min, int max, int step) {
        return new JSpinner(new SpinnerNumberModel(start, min, max, step));
    }

    public JSpinner createLvlSpinner() {
        return createCustomSpinner(0, 0, 20, 1);
    }

    private JMenu createTab() {
        return createTab(String.valueOf(tabs.size() + 1));
    }

    private JMenu createTab(String name) {
        JMenu tab = new JMenu(name);
        JMenuItem rename = new JMenuItem("Rename");
        rename.addActionListener(itemListener);
        JMenuItem delete = new JMenuItem("Delete");
        delete.addActionListener(itemListener);
        tab.add(rename);
        tab.add(delete);
        tab.setOpaque(true);
        tabs.put(tab, new Setup());
        Bar.add(tab);
        tab.addMenuListener(menuListener);
        selectTab(tab);
        Bar.updateUI();
        return tab;
    }

    private void selectTab(JMenu source) {
        if (selected_tab != null) {
            tabs.put(selected_tab, saveSetup());
        }
        selected_tab = source;
        loadTab(source);
    }

    private void loadTab(JMenu source) {
        for (JMenu tab : tabs.keySet()) {
            tab.setBackground(null);
        }
        source.setBackground(Color.YELLOW);
        Setup show_setup = tabs.get(source);
        if (show_setup == null) show_setup = new Setup();
        loadSetup(show_setup);
    }
}
