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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.function.Function;
import java.util.stream.Collectors;

import static Disilon.Main.df2;
import static Disilon.Main.df2p;
import static Disilon.Main.dfm;
import static Disilon.Main.dfs;
import static Disilon.Main.equipmentData;

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
    private JComboBox Skill4;
    private JComboBox Skill1_mod;
    private JComboBox Skill2_mod;
    private JComboBox Skill3_mod;
    private JComboBox Skill4_mod;
    private JSpinner Skill1_s;
    private JSpinner Skill2_s;
    private JSpinner Skill3_s;
    private JSpinner Skill4_s;
    private JComboBox Pskill1;
    private JComboBox Pskill2;
    private JComboBox Pskill3;
    private JComboBox Pskill4;
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
    private JTextArea Result_skills;
    private JTextArea Result_lvling;
    private JTextArea Stats;
    private JButton Save;
    private JButton Load;
    private JComboBox Enemy;
    private JComboBox GameVersion;
    private JSpinner Milestone;
    private JSpinner Crafting_lvl;
    private JSpinner Alchemy_lvl;
    private JButton Run;
    private JCheckBox SetSetup;
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
    private JButton Update_lvls;
    private JMenuBar Bar;
    private JButton New_tab;
    private JScrollPane LeftPane;
    private JPanel BottomPanel;
    private JTable ActiveSkills;
    private JTable PassiveSkills;
    private JPanel TopPanel;
    SkillTableModel activeSkillsModel;
    SkillTableModel passiveSkillsModel;
    private JSpinner ResearchExp;
    private JSpinner ResearchCoreQuality;
    private JSpinner ResearchCoreDrop;
    private JSpinner ResearchCraftingSpd;
    private JSpinner ResearchAlchemySpd;
    private JSpinner ResearchMaxCl;
    private JSpinner ResearchGearHp;
    private JSpinner ResearchGearAtk;
    private JSpinner ResearchGearDef;
    private JSpinner ResearchGearInt;
    private JSpinner ResearchGearRes;
    private JSpinner ResearchGearHit;
    private JSpinner ResearchGearSpd;
    GridBagConstraints gbc = new GridBagConstraints();

    public Player player;
    public Simulation simulation;
    public Setup setup;
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
        setup = new Setup();
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

        MLLabel = new JLabel();
        MLLabel.setText("ML: ");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        RightPanel.add(MLLabel, gbc);
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
        CLLabel = new JLabel();
        CLLabel.setText("CL: ");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        RightPanel.add(CLLabel, gbc);
        CL = createCustomSpinner(62, 0, 1000, 1);
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 1;
        RightPanel.add(CL, gbc);
        CL_p = new JFormattedTextField(df2p);
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 5;
//        gbc.ipady = 3;
        RightPanel.add(CL_p, gbc);

        final JLabel label15 = new JLabel();
        label15.setText("Enemy:");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        RightPanel.add(label15, gbc);
        Enemy = new JComboBox(Zone.values());
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        Enemy.setMaximumRowCount(25);
        RightPanel.add(Enemy, gbc);

        ClassLabel = new JLabel();
        ClassLabel.setText("Class:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        RightPanel.add(ClassLabel, gbc);
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
        gbc.gridwidth = 6;
        RightPanel.add(label1, gbc);
        Potion1 = new JComboBox<>(Potion.getAvailablePotions());
        Potion1.setMaximumRowCount(21);
        Potion1.setSelectedIndex(6);
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
        Potion2.setMaximumRowCount(21);
        Potion2.setSelectedIndex(16);
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
        Potion3.setMaximumRowCount(21);
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
        Skill4 = new JComboBox();
        Skill4.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Skill4, gbc);
        Skill1_mod = new JComboBox<>(SkillMod.getAvailableMods());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Skill1_mod, gbc);
        Skill2_mod = new JComboBox<>(SkillMod.getAvailableMods());
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Skill2_mod, gbc);
        Skill3_mod = new JComboBox<>(SkillMod.getAvailableMods());
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Skill3_mod, gbc);
        Skill4_mod = new JComboBox<>(SkillMod.getAvailableMods());
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Skill4_mod, gbc);
        Skill1_s = new JSpinner(new SpinnerNumberModel(1, 0, 10000, 1));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Skill1_s, gbc);
        Skill2_s = new JSpinner(new SpinnerNumberModel(1, 0, 10000, 1));
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Skill2_s, gbc);
        Skill3_s = new JSpinner(new SpinnerNumberModel(1, 0, 10000, 1));
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Skill3_s, gbc);
        Skill4_s = new JSpinner(new SpinnerNumberModel(1, 0, 10000, 1));
        gbc = new GridBagConstraints();
        gbc.gridx = 8;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Skill4_s, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Passive skills:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.gridwidth = 6;
        gbc.insets = new Insets(10, 5, 0, 5);
        RightPanel.add(label3, gbc);
        Pskill1 = new JComboBox();
        Pskill1.setMaximumRowCount(16);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Pskill1, gbc);
        Pskill2 = new JComboBox();
        Pskill2.setMaximumRowCount(16);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Pskill2, gbc);
        Pskill3 = new JComboBox();
        Pskill3.setMaximumRowCount(16);
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Pskill3, gbc);
        Pskill4 = new JComboBox();
        Pskill4.setMaximumRowCount(16);
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Pskill4, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("Equipment:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.gridwidth = 6;
        gbc.insets = new Insets(10, 5, 0, 5);
        RightPanel.add(label4, gbc);
        final JLabel label5 = new JLabel();
        label5.setText("Weapon MH");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.WEST;
        RightPanel.add(label5, gbc);
        MH_name = new JComboBox<String>();
        MH_name.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 11;
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
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(MH_tier, gbc);
        MH_lvl = new JSpinner();
        MH_lvl.setToolTipText("Upgrade lvl");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(MH_lvl, gbc);
        final JLabel label6 = new JLabel();
        label6.setText("Offhand");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 12;
        gbc.anchor = GridBagConstraints.WEST;
        RightPanel.add(label6, gbc);
        OH_name = new JComboBox();
        OH_name.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 12;
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
        gbc.gridy = 12;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(OH_tier, gbc);
        OH_lvl = new JSpinner();
        OH_lvl.setToolTipText("Upgrade lvl");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 12;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(OH_lvl, gbc);
        final JLabel label7 = new JLabel();
        label7.setText("Helmet");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 14;
        gbc.anchor = GridBagConstraints.WEST;
        RightPanel.add(label7, gbc);
        Helmet_name = new JComboBox();
        Helmet_name.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 14;
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
        gbc.gridy = 14;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Helmet_tier, gbc);
        Helmet_lvl = new JSpinner();
        Helmet_lvl.setToolTipText("Upgrade lvl");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 14;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Helmet_lvl, gbc);
        final JLabel label8 = new JLabel();
        label8.setText("Chest");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 15;
        gbc.anchor = GridBagConstraints.WEST;
        RightPanel.add(label8, gbc);
        Chest_name = new JComboBox();
        Chest_name.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 15;
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
        gbc.gridy = 15;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Chest_tier, gbc);
        Chest_lvl = new JSpinner();
        Chest_lvl.setToolTipText("Upgrade lvl");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 15;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Chest_lvl, gbc);
        final JLabel label9 = new JLabel();
        label9.setText("Pants");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 16;
        gbc.anchor = GridBagConstraints.WEST;
        RightPanel.add(label9, gbc);
        Pants_name = new JComboBox();
        Pants_name.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 16;
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
        gbc.gridy = 16;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Pants_tier, gbc);
        Pants_lvl = new JSpinner();
        Pants_lvl.setToolTipText("Upgrade lvl");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 16;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Pants_lvl, gbc);
        final JLabel label10 = new JLabel();
        label10.setText("Bracer");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 17;
        gbc.anchor = GridBagConstraints.WEST;
        RightPanel.add(label10, gbc);
        Bracer_name = new JComboBox();
        Bracer_name.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 17;
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
        gbc.gridy = 17;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Bracer_tier, gbc);
        Bracer_lvl = new JSpinner();
        Bracer_lvl.setToolTipText("Upgrade lvl");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 17;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Bracer_lvl, gbc);
        final JLabel label11 = new JLabel();
        label11.setText("Boots");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 18;
        gbc.anchor = GridBagConstraints.WEST;
        RightPanel.add(label11, gbc);
        Boots_name = new JComboBox();
        Boots_name.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 18;
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
        gbc.gridy = 18;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Boots_tier, gbc);
        Boots_lvl = new JSpinner();
        Boots_lvl.setToolTipText("Upgrade lvl");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 18;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Boots_lvl, gbc);
        final JLabel label12 = new JLabel();
        label12.setText("Accessory 1");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 19;
        gbc.anchor = GridBagConstraints.WEST;
        RightPanel.add(label12, gbc);
        Accessory1_name = new JComboBox();
        Accessory1_name.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 19;
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
        gbc.gridy = 19;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Accessory1_tier, gbc);
        Accessory1_lvl = new JSpinner();
        Accessory1_lvl.setToolTipText("Upgrade lvl");
        Accessory1_lvl.setValue(25);
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 19;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Accessory1_lvl, gbc);
        final JLabel label13 = new JLabel();
        label13.setText("Accessory 2");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 20;
        gbc.anchor = GridBagConstraints.WEST;
        RightPanel.add(label13, gbc);
        Accessory2_name = new JComboBox();
        Accessory2_name.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 20;
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
        gbc.gridy = 20;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Accessory2_tier, gbc);
        Accessory2_lvl = new JSpinner();
        Accessory2_lvl.setToolTipText("Upgrade lvl");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 20;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Accessory2_lvl, gbc);
        final JLabel label14 = new JLabel();
        label14.setText("Neck");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 21;
        gbc.anchor = GridBagConstraints.WEST;
        RightPanel.add(label14, gbc);
        Necklace_name = new JComboBox();
        Necklace_name.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 21;
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
        gbc.gridy = 21;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Necklace_tier, gbc);
        Necklace_lvl = new JSpinner();
        Necklace_lvl.setToolTipText("Upgrade lvl");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 21;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Necklace_lvl, gbc);
        Save = new JButton();
        Save.setText("Save");
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 5, 0, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Save, gbc);
        Load = new JButton();
        Load.setText("Load");
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 5, 0, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Load, gbc);

//        final JLabel label15 = new JLabel();
//        label15.setText("Enemy:");
//        gbc = new GridBagConstraints();
//        gbc.gridx = 7;
//        gbc.gridy = 2;
//        RightPanel.add(label15, gbc);
//        Enemy = new JComboBox(Zone.values());
//        gbc = new GridBagConstraints();
//        gbc.gridx = 7;
//        gbc.gridy = 3;
//        gbc.anchor = GridBagConstraints.WEST;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        Enemy.setMaximumRowCount(25);
//        RightPanel.add(Enemy, gbc);
        Sim_type = new ButtonGroup();
        Sim_num = new JRadioButton();
        Sim_num.setText("Number of simulations:");
        Sim_type.add(Sim_num);
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 14;
        gbc.gridwidth = 2;
        RightPanel.add(Sim_num, gbc);
        Sim_time = new JRadioButton();
        Sim_time.setText("Number of hours:");
        Sim_type.add(Sim_time);
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 15;
        gbc.gridwidth = 2;
        RightPanel.add(Sim_time, gbc);
        Sim_lvl = new JRadioButton();
        Sim_lvl.setText("Until CL reached:");
        Sim_type.add(Sim_lvl);
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 16;
        gbc.gridwidth = 2;
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
        gbc.gridy = 17;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Simulations, gbc);
        SimHours = createCustomSpinner(1.0, 0, 1000, 1);
        SimHours.setVisible(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 17;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(SimHours, gbc);
        SimCL = createCustomSpinner(90, 0, 1000, 1);
        SimCL.setVisible(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 17;
        gbc.gridwidth = 2;
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
        Sim_num.setSelected(true);

        Leveling = new JCheckBox();
        Leveling.setSelected(false);
        Leveling.setText("Gain exp during sim");
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 18;
        gbc.gridwidth = 2;
        RightPanel.add(Leveling, gbc);
        Update_lvls = new JButton();
        Update_lvls.setText("Update lvls from sim data");
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 21;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 5, 0, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Update_lvls, gbc);

        Run = new JButton();
        Run.setText("Simulate");
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 19;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 0, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RightPanel.add(Run, gbc);
        SetSetup = new JCheckBox();
        SetSetup.setSelected(true);
        SetSetup.setText("Use helmet values for full set");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 13;
        gbc.gridwidth = 6;
        RightPanel.add(SetSetup, gbc);

        activeSkillsModel = new SkillTableModel();
        activeSkillsModel.setColumnIdentifiers(new String[]{"Active Skill", "lvl", "exp %"});
        ActiveSkills = new JTable();
        ActiveSkills.setModel(activeSkillsModel);
        ActiveSkills.setFont(new Font("Dialog", Font.BOLD, 12));
        ActiveSkills.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 12));
        ActiveSkills.getTableHeader().setReorderingAllowed(false);
        ActiveSkills.getColumnModel().getColumn(1).setCellEditor(new SpinnerEditor());
        ActiveSkills.getColumnModel().getColumn(2).setCellEditor(new StringEditor());
        ActiveSkills.setRowHeight(20);
        ActiveSkills.getColumnModel().getColumn(0).setPreferredWidth(160);
        ActiveSkills.getColumnModel().getColumn(1).setPreferredWidth(45);
        ActiveSkills.getColumnModel().getColumn(2).setPreferredWidth(55);

        passiveSkillsModel = new SkillTableModel();
        passiveSkillsModel.setColumnIdentifiers(new String[]{"Passive Skill", "lvl", "exp %"});
        PassiveSkills = new JTable();
        PassiveSkills.setModel(passiveSkillsModel);
        PassiveSkills.setFont(new Font("Dialog", Font.BOLD, 12));
        PassiveSkills.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 12));
        PassiveSkills.getColumnModel().getColumn(1).setCellEditor(new SpinnerEditor());
        PassiveSkills.getColumnModel().getColumn(2).setCellEditor(new StringEditor());
        PassiveSkills.setRowHeight(20);
        PassiveSkills.getColumnModel().getColumn(0).setPreferredWidth(160);
        PassiveSkills.getColumnModel().getColumn(1).setPreferredWidth(45);
        PassiveSkills.getColumnModel().getColumn(2).setPreferredWidth(55);

        LeftPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(5, 5, 0, 5);
        LeftPanel.add(ActiveSkills.getTableHeader(), gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTH;
        LeftPanel.add(ActiveSkills, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(5, 5, 0, 5);
        LeftPanel.add(PassiveSkills.getTableHeader(), gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.NORTH;
        LeftPanel.add(PassiveSkills, gbc);

        final JLabel label25 = new JLabel();
        label25.setText("Milestone exp bonus (%)");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(10, 5, 0, 5);
        LeftPanel.add(label25, gbc);
        Milestone = createCustomSpinner(155, 100, 1000, 2.5);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        LeftPanel.add(Milestone, gbc);
        final JLabel label26 = new JLabel();
        label26.setText("Crafting lvl:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(10, 5, 0, 5);
        LeftPanel.add(label26, gbc);
        Crafting_lvl = new JSpinner(new SpinnerNumberModel(22, 0, 100, 1));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        //gbc.insets = new Insets(10, 5, 0, 5);
        LeftPanel.add(Crafting_lvl, gbc);
        final JLabel label27 = new JLabel();
        label27.setText("Alchemy lvl:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(10, 5, 0, 5);
        LeftPanel.add(label27, gbc);
        Alchemy_lvl = new JSpinner(new SpinnerNumberModel(22, 0, 100, 1));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        LeftPanel.add(Alchemy_lvl, gbc);
        final JLabel label23 = new JLabel();
        label23.setText("Game version:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(10, 5, 0, 5);
        LeftPanel.add(label23, gbc);
        GameVersion = new JComboBox<>(Main.availableVersions);
        GameVersion.setMaximumRowCount(16);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.NORTH;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
        LeftPanel.add(GameVersion, gbc);

        final JLabel label28 = new JLabel();
        label28.setText("Research:");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(10, 5, 0, 5);
        LeftPanel.add(label28, gbc);

        final JLabel label29 = new JLabel("Exp");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 5);
        LeftPanel.add(label29, gbc);
        ResearchExp = createCustomSpinner(0, 0, 900, 1);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        LeftPanel.add(ResearchExp, gbc);
        final JLabel label30 = new JLabel("Core Drop");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 5);
        LeftPanel.add(label30, gbc);
        ResearchCoreDrop = createCustomSpinner(0, 0, 900, 1);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        LeftPanel.add(ResearchCoreDrop, gbc);
        final JLabel label31 = new JLabel("Core Quality");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 5);
        LeftPanel.add(label31, gbc);
        ResearchCoreQuality = createCustomSpinner(0, 0, 900, 1);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        LeftPanel.add(ResearchCoreQuality, gbc);
        final JLabel label32 = new JLabel("Craft spd");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 5);
        LeftPanel.add(label32, gbc);
        ResearchCraftingSpd = createCustomSpinner(0, 0, 900, 1);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        LeftPanel.add(ResearchCraftingSpd, gbc);
        final JLabel label33 = new JLabel("Alchemy spd");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 5);
        LeftPanel.add(label33, gbc);
        ResearchAlchemySpd = createCustomSpinner(0, 0, 900, 1);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        LeftPanel.add(ResearchAlchemySpd, gbc);
        final JLabel label41 = new JLabel("Max CL");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 5);
        LeftPanel.add(label41, gbc);
        ResearchMaxCl = createCustomSpinner(0, 0, 100, 1);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        LeftPanel.add(ResearchMaxCl, gbc);

        final JLabel label34 = new JLabel("Gear HP");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 5);
        LeftPanel.add(label34, gbc);
        ResearchGearHp = createCustomSpinner(0, 0, 900, 1);
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        LeftPanel.add(ResearchGearHp, gbc);
        final JLabel label35 = new JLabel("Gear Atk");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 5);
        LeftPanel.add(label35, gbc);
        ResearchGearAtk = createCustomSpinner(0, 0, 900, 1);
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        LeftPanel.add(ResearchGearAtk, gbc);
        final JLabel label36 = new JLabel("Gear Def");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 5);
        LeftPanel.add(label36, gbc);
        ResearchGearDef = createCustomSpinner(0, 0, 900, 1);
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        LeftPanel.add(ResearchGearDef, gbc);
        final JLabel label37 = new JLabel("Gear Int");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 5);
        LeftPanel.add(label37, gbc);
        ResearchGearInt = createCustomSpinner(0, 0, 900, 1);
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        LeftPanel.add(ResearchGearInt, gbc);
        final JLabel label38 = new JLabel("Gear Res");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 5);
        LeftPanel.add(label38, gbc);
        ResearchGearRes = createCustomSpinner(0, 0, 900, 1);
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        LeftPanel.add(ResearchGearRes, gbc);
        final JLabel label39 = new JLabel("Gear Hit");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 5);
        LeftPanel.add(label39, gbc);
        ResearchGearHit = createCustomSpinner(0, 0, 900, 1);
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        LeftPanel.add(ResearchGearHit, gbc);
        final JLabel label40 = new JLabel("Gear Spd");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 5);
        LeftPanel.add(label40, gbc);
        ResearchGearSpd = createCustomSpinner(0, 0, 900, 1);
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        LeftPanel.add(ResearchGearSpd, gbc);

        LeftPane = new JScrollPane(LeftPanel);
        LeftPane.setPreferredSize(new Dimension(550, 540));
        LeftPane.getVerticalScrollBar().setUnitIncrement(16);
        LeftPane.getHorizontalScrollBar().setUnitIncrement(16);
        RightPanel.setPreferredSize(new Dimension(640, 540));
        //RightPanel.setPreferredSize(RightPanel.getPreferredSize());
        TopPanel = new JPanel();
        TopPanel.setLayout(new BoxLayout(TopPanel, BoxLayout.X_AXIS));
        TopPanel.add(LeftPane);
        TopPanel.add(RightPanel);
        TopPanel.setPreferredSize(new Dimension(1200, 550));

        BottomPanel = new JPanel();
        BottomPanel.setLayout(new GridBagLayout());
        Stats = new JTextArea();
        Stats.setEditable(false);
        Stats.setText("Stats will be shown after simulation");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 6;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
//        Stats.setPreferredSize(new Dimension(300, 400));
        BottomPanel.add(Stats, gbc);

        Result = new JTextArea();
        Result.setEditable(false);
        Result.setText("Result will be here");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 6;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
//        Result.setPreferredSize(new Dimension(300, 400));
        BottomPanel.add(Result, gbc);

        Result_skills = new JTextArea();
        Result_skills.setEditable(false);
        Result_skills.setText("Info about used skills will be here");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 7;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
//        Result_skills.setPreferredSize(new Dimension(350, 400));
        BottomPanel.add(Result_skills, gbc);

        Result_lvling = new JTextArea();
        Result_lvling.setEditable(false);
        Result_lvling.setText("Leveling results will be here");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 5;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
//        Result_lvling.setPreferredSize(new Dimension(250, 400));
        BottomPanel.add(Result_lvling, gbc);

        BottomPanel.setPreferredSize(new Dimension(1150, 450));

        RootPanel.setLayout(new BoxLayout(RootPanel, BoxLayout.Y_AXIS));
        RootPanel.add(TopPanel);
        RootPanel.add(BottomPanel);
        this.setContentPane(RootPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        JScrollPane Scroll = new JScrollPane(BottomPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane
//                .HORIZONTAL_SCROLLBAR_AS_NEEDED);
//        Scroll.getVerticalScrollBar().setUnitIncrement(16);
//        Scroll.getHorizontalScrollBar().setUnitIncrement(16);
//        this.add(Scroll);

        this.setPreferredSize(new Dimension(1270, 910));
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
                    if (!path.endsWith(".json")) {
                        path += ".json";
                    }
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
                    info.append(df2.format(player.getCLpercent())).append("%");
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
                    info.append(df2.format(player.getMLpercent())).append("%");
                    info.append("</td>");
                    for (ActiveSkill a : player.active_skills.values()) {
                        if (a.enabled && a.old_lvl < 20) {
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
                        if (a.enabled && (a.old_lvl < 20 || a.name.equals("Tsury Finke"))) {
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
                        setup.milestone = player.milestone_exp_mult * 100;
                        setup.ml = player.getMl() + player.getMLpercent() / 100;
                        setup.cl = Math.min(player.getMaxCl(), player.getCl() + player.getCLpercent() / 100);
                        for (String s : player.active_skills.keySet()) {
                            setup.actives_lvls.put(s, Math.min(20, player.active_skills.get(s).getLvl()));
                        }
                        for (String s : player.passives.keySet()) {
                            int max_lvl = s.equals("Tsury Finke") ? 100 : 20;
                            setup.passives_lvls.put(s, Math.min(max_lvl, player.passives.get(s).getLvl()));
                        }
                        loadSetup(setup);
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
                        if (checkSameItemsCombo()) {
                            JOptionPane.showMessageDialog(RightPanel,
                                    "You're using some of your skills twice, this can cause bugs", "Warning",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                        long startTime = System.nanoTime();
                        try {
                            setup = saveSetup();
                            Main.game_version = (int) Double.parseDouble(setup.gameversion);
                            player = simulation.setupAndRun(setup);
                            showResult();
                            Stats.setText(simulation.player.getAllStats());
                        } catch (IllegalArgumentException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(RightPanel, "Some field has illegal value!", "Exception",
                                    JOptionPane.WARNING_MESSAGE);
                        }

                        // Calculate the execution time in milliseconds
                        long executionTime = (System.nanoTime() - startTime) / 1000000;
//                    System.out.println(executionTime + "ms");
                    }
                }

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
                    if (equipmentData.twohanded.contains(item_name)) {
                        OH_name.setSelectedItem("None");
                    }
                }
            }
        });
        OH_name.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (MH_name.getSelectedItem() != null) {
                    String item_name = MH_name.getSelectedItem().toString();
                    if (equipmentData.twohanded.contains(item_name)) {
                        OH_name.setSelectedItem("None");
                    }
                }
            }
        });
        Helmet_name.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SetSetup.isSelected() && Helmet_name.getSelectedItem() != null && Helmet_name.getSelectedItem() != "None") {
                    Equipment eq = equipmentData.items.get(Helmet_name.getSelectedItem().toString());
                    if (eq != null) {
                        String set = eq.displayName;
                        Chest_name.setSelectedItem(findItemFromSet(set, "CHEST"));
                        Pants_name.setSelectedItem(findItemFromSet(set, "PANTS"));
                        Bracer_name.setSelectedItem(findItemFromSet(set, "BRACERS"));
                        Boots_name.setSelectedItem(findItemFromSet(set, "BOOTS"));
                    }
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

    public class StringEditor extends DefaultCellEditor {
        JTextField textField;

        public StringEditor() {
            super(new JTextField());
            textField = (JTextField) getComponent();
            textField.setFont(new Font("Dialog", Font.BOLD, 12));
        }
    }

    public class SpinnerEditor extends DefaultCellEditor {
        JSpinner spinner;
        JSpinner.DefaultEditor editor;
        JTextField textField;
        boolean valueSet;

        // Initializes the spinner.
        public SpinnerEditor() {
            super(new JTextField());
            spinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
            editor = ((JSpinner.DefaultEditor) spinner.getEditor());
            textField = editor.getTextField();
            textField.setFont(new Font("Dialog", Font.BOLD, 12));
            textField.addFocusListener(new FocusListener() {
                public void focusGained(FocusEvent fe) {
                    //System.err.println("Got focus");
                    //textField.setSelectionStart(0);
                    //textField.setSelectionEnd(1);
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            if (valueSet) {
                                textField.setCaretPosition(1);
                            }
                        }
                    });
                }

                public void focusLost(FocusEvent fe) {
                }
            });
            textField.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    stopCellEditing();
                }
            });
        }

        // Prepares the spinner component and returns it.
        public Component getTableCellEditorComponent(
                JTable table, Object value, boolean isSelected, int row, int column
        ) {
            if (!valueSet) {
                spinner.setValue(value);
            }
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    textField.requestFocus();
                }
            });
            return spinner;
        }

        public boolean isCellEditable(EventObject eo) {
            //System.err.println("isCellEditable");
            if (eo instanceof KeyEvent) {
                KeyEvent ke = (KeyEvent) eo;
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
            } catch (java.text.ParseException e) {
//                JOptionPane.showMessageDialog(null,
//                        "Invalid value, discarding.");
            }
            return super.stopCellEditing();
        }
    }

    private boolean checkSameItemsCombo() {
        Map<Integer, Integer> elementCountMap = Arrays.stream(new Integer[]{Skill1.getSelectedIndex(), Skill2.getSelectedIndex(),
                        Skill3.getSelectedIndex(), Skill4.getSelectedIndex()}).filter(a -> a > 0)
                .collect(Collectors.toMap(Function.identity(), v -> 1, Integer::sum));
        for (int count : elementCountMap.values()) {
            if (count > 1) return true;
        }
        elementCountMap = Arrays.stream(new Integer[]{Pskill1.getSelectedIndex(), Pskill2.getSelectedIndex(),
                        Pskill3.getSelectedIndex(), Pskill4.getSelectedIndex()}).filter(a -> a > 0)
                .collect(Collectors.toMap(Function.identity(), v -> 1, Integer::sum));
        for (int count : elementCountMap.values()) {
            if (count > 1) return true;
        }
        return false;
    }

    private String findItemFromSet(String name, String slot) {
        for (Equipment e : equipmentData.items.values()) {
            if (e.displayName.equals(name) && e.slot.equals(slot)) {
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
        DefaultComboBoxModel<String> active4 =
                new DefaultComboBoxModel<>(new Vector<>(player.getAvailableActiveSkills()));
        DefaultComboBoxModel<String> passive1 =
                new DefaultComboBoxModel<>(new Vector<>(player.getAvailablePassiveSkills()));
        DefaultComboBoxModel<String> passive2 =
                new DefaultComboBoxModel<>(new Vector<>(player.getAvailablePassiveSkills()));
        DefaultComboBoxModel<String> passive3 =
                new DefaultComboBoxModel<>(new Vector<>(player.getAvailablePassiveSkills()));
        DefaultComboBoxModel<String> passive4 =
                new DefaultComboBoxModel<>(new Vector<>(player.getAvailablePassiveSkills()));
        Skill1.setModel(active1);
        Skill2.setModel(active2);
        Skill3.setModel(active3);
        Skill4.setModel(active4);
        Pskill1.setModel(passive1);
        Pskill2.setModel(passive2);
        Pskill3.setModel(passive3);
        Pskill4.setModel(passive4);
        clearSelections();
        new DefaultTableModel();
        activeSkillsModel.setRowCount(0);
        for (String skill : player.active_skills.keySet()) {
            activeSkillsModel.addRow(new Object[]{skill, player.active_skills.get(skill).lvl,
                    (player.active_skills.get(skill).getLvl() - player.active_skills.get(skill).lvl) * 100});
        }
        passiveSkillsModel.setRowCount(0);
        for (String skill : player.passives.keySet()) {
            passiveSkillsModel.addRow(new Object[]{skill, player.passives.get(skill).lvl,
                    (player.passives.get(skill).getLvl() - player.passives.get(skill).lvl) * 100});
        }
    }

    private void clearSelections() {
        Skill1.setSelectedIndex(1);
        Skill2.setSelectedIndex(0);
        Skill3.setSelectedIndex(0);
        Skill4.setSelectedIndex(0);
        Pskill1.setSelectedIndex(0);
        Pskill2.setSelectedIndex(0);
        Pskill3.setSelectedIndex(0);
        Pskill4.setSelectedIndex(0);
    }

    private void loadEquipment() {
        MH_name.addItem("None");
        OH_name.addItem("None");
        Helmet_name.addItem("None");
        Chest_name.addItem("None");
        Pants_name.addItem("None");
        Bracer_name.addItem("None");
        Boots_name.addItem("None");
        Accessory1_name.addItem("None");
        Accessory2_name.addItem("None");
        Necklace_name.addItem("None");
        equipmentData.loadEquipment();
        for (String name : equipmentData.items.keySet()) {
            String slot = equipmentData.items.get(name).slot;
            switch (slot) {
                case "MH":
                    MH_name.addItem(name);
                    if (!equipmentData.items.get(name).mh_only) {
                        OH_name.addItem(name);
                    }
                    break;
                case "2H":
                    MH_name.addItem(name);
                    break;
                case "OH":
                    if (!equipmentData.items.get(name).mh_only) {
                        OH_name.addItem(name);
                    }
                    break;
                case "HEADGEAR":
                    Helmet_name.addItem(name);
                    break;
                case "CHEST":
                    Chest_name.addItem(name);
                    break;
                case "PANTS":
                    Pants_name.addItem(name);
                    break;
                case "BRACERS":
                    Bracer_name.addItem(name);
                    break;
                case "BOOTS":
                    Boots_name.addItem(name);
                    break;
                case "RING":
                    Accessory1_name.addItem(name);
                    Accessory2_name.addItem(name);
                    break;
                case "NECK":
                    Necklace_name.addItem(name);
                    break;
            }
        }
    }

    private void clearSlot(JComboBox slot) {
        slot.removeAllItems();
        slot.addItem("None");
    }

    private void saveFile(String path) {
        try {
            Setup save = saveSetup();
            JsonWriter writer = new JsonWriter(new FileWriter(path));
            gson.toJson(save, Setup.class, writer);
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
        data.pskill2 = Pskill2.getSelectedItem().toString();
        data.pskill3 = Pskill3.getSelectedItem().toString();
        data.pskill4 = Pskill4.getSelectedItem().toString();
        data.result_essential = simulation.result_info;
        data.result_skills = simulation.skills_info;
        data.result_lvling = simulation.lvling_info;
        data.setsetup = SetSetup.isSelected();

        data.skill1 = Skill1.getSelectedItem().toString();
        data.skill1_mod = (SkillMod) Skill1_mod.getSelectedItem();
        data.skill1_s = (int) Double.parseDouble(Skill1_s.getValue().toString());
        data.skill2 = Skill2.getSelectedItem().toString();
        data.skill2_mod = (SkillMod) Skill2_mod.getSelectedItem();
        data.skill2_s = (int) Double.parseDouble(Skill2_s.getValue().toString());
        data.skill3 = Skill3.getSelectedItem().toString();
        data.skill3_mod = (SkillMod) Skill3_mod.getSelectedItem();
        data.skill3_s = (int) Double.parseDouble(Skill3_s.getValue().toString());
        data.skill4 = Skill4.getSelectedItem().toString();
        data.skill4_mod = (SkillMod) Skill4_mod.getSelectedItem();
        data.skill4_s = (int) Double.parseDouble(Skill4_s.getValue().toString());
        data.stats = Stats.getText();
        if (Sim_num.isSelected()) data.sim_type = 1;
        if (Sim_time.isSelected()) data.sim_type = 2;
        if (Sim_lvl.isSelected()) data.sim_type = 3;
        data.simulations = (int) Double.parseDouble(Simulations.getValue().toString());
        data.sim_hours = Double.parseDouble(SimHours.getValue().toString());
        data.sim_cl = (int) Double.parseDouble(SimCL.getValue().toString());
        data.leveling = Leveling.isSelected();
        for (int i = 0; i < ActiveSkills.getRowCount(); i++) {
            data.actives_lvls.put(ActiveSkills.getValueAt(i, 0).toString(),
                    Double.parseDouble(ActiveSkills.getValueAt(i, 1).toString()) +
                            Double.parseDouble(ActiveSkills.getValueAt(i, 2).toString()) / 100);
        }
        for (int i = 0; i < PassiveSkills.getRowCount(); i++) {
            data.passives_lvls.put(PassiveSkills.getValueAt(i, 0).toString(),
                    Double.parseDouble(PassiveSkills.getValueAt(i, 1).toString()) +
                            Double.parseDouble(PassiveSkills.getValueAt(i, 2).toString()) / 100);
        }
        data.research_lvls.put("Exp", (int) Double.parseDouble(ResearchExp.getValue().toString()));
        data.research_lvls.put("CoreDrop", (int) Double.parseDouble(ResearchCoreDrop.getValue().toString()));
        data.research_lvls.put("CoreQuality", (int) Double.parseDouble(ResearchCoreQuality.getValue().toString()));
        data.research_lvls.put("CraftSpd", (int) Double.parseDouble(ResearchCraftingSpd.getValue().toString()));
        data.research_lvls.put("AlchemySpd", (int) Double.parseDouble(ResearchAlchemySpd.getValue().toString()));
        data.research_lvls.put("Max CL", (int) Double.parseDouble(ResearchMaxCl.getValue().toString()));
        data.research_lvls.put("GearHp", (int) Double.parseDouble(ResearchGearHp.getValue().toString()));
        data.research_lvls.put("GearAtk", (int) Double.parseDouble(ResearchGearAtk.getValue().toString()));
        data.research_lvls.put("GearDef", (int) Double.parseDouble(ResearchGearDef.getValue().toString()));
        data.research_lvls.put("GearInt", (int) Double.parseDouble(ResearchGearInt.getValue().toString()));
        data.research_lvls.put("GearRes", (int) Double.parseDouble(ResearchGearRes.getValue().toString()));
        data.research_lvls.put("GearHit", (int) Double.parseDouble(ResearchGearHit.getValue().toString()));
        data.research_lvls.put("GearSpd", (int) Double.parseDouble(ResearchGearSpd.getValue().toString()));
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
        Pskill2.setSelectedItem(data.pskill2);
        Pskill3.setSelectedItem(data.pskill3);
        Pskill4.setSelectedItem(data.pskill4);
        simulation.result_info = data.result_essential;
        simulation.skills_info = data.result_skills;
        simulation.lvling_info = data.result_lvling;
        SetSetup.setSelected(data.setsetup);
        Skill1.setSelectedItem(data.skill1);
        Skill1_mod.setSelectedItem(data.skill1_mod != null ? data.skill1_mod : SkillMod.Basic);
        Skill1_s.setValue(data.skill1_s);
        Skill2.setSelectedItem(data.skill2);
        Skill2_mod.setSelectedItem(data.skill2_mod != null ? data.skill2_mod : SkillMod.Basic);
        Skill2_s.setValue(data.skill2_s);
        Skill3.setSelectedItem(data.skill3);
        Skill3_mod.setSelectedItem(data.skill3_mod != null ? data.skill3_mod : SkillMod.Basic);
        Skill3_s.setValue(data.skill3_s);
        Skill4.setSelectedItem(data.skill4);
        Skill4_mod.setSelectedItem(data.skill4_mod != null ? data.skill4_mod : SkillMod.Basic);
        Skill4_s.setValue(data.skill4_s);
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
        for (int i = 0; i < ActiveSkills.getRowCount(); i++) {
            String name = ActiveSkills.getValueAt(i, 0).toString();
            if (data.actives_lvls.containsKey(name)) {
                double lvl = data.actives_lvls.get(name);
                ActiveSkills.setValueAt((int) lvl, i, 1);
                ActiveSkills.setValueAt(df2.format((lvl - (int) lvl) * 100), i, 2);
            }
        }
        for (int i = 0; i < PassiveSkills.getRowCount(); i++) {
            String name = PassiveSkills.getValueAt(i, 0).toString();
            if (data.passives_lvls.containsKey(name)) {
                double lvl = data.passives_lvls.get(name);
                PassiveSkills.setValueAt((int) lvl, i, 1);
                PassiveSkills.setValueAt(df2.format((lvl - (int) lvl) * 100), i, 2);
            }
        }
        ResearchExp.setValue(data.research_lvls.getOrDefault("Exp", 0));
        ResearchCoreDrop.setValue(data.research_lvls.getOrDefault("CoreDrop", 0));
        ResearchCoreQuality.setValue(data.research_lvls.getOrDefault("CoreQuality", 0));
        ResearchCraftingSpd.setValue(data.research_lvls.getOrDefault("CraftSpd", 0));
        ResearchAlchemySpd.setValue(data.research_lvls.getOrDefault("AlchemySpd", 0));
        ResearchMaxCl.setValue(data.research_lvls.getOrDefault("Max CL", 0));
        ResearchGearHp.setValue(data.research_lvls.getOrDefault("GearHp", 0));
        ResearchGearAtk.setValue(data.research_lvls.getOrDefault("GearAtk", 0));
        ResearchGearDef.setValue(data.research_lvls.getOrDefault("GearDef", 0));
        ResearchGearInt.setValue(data.research_lvls.getOrDefault("GearInt", 0));
        ResearchGearRes.setValue(data.research_lvls.getOrDefault("GearRes", 0));
        ResearchGearHit.setValue(data.research_lvls.getOrDefault("GearHit", 0));
        ResearchGearSpd.setValue(data.research_lvls.getOrDefault("GearSpd", 0));
        showResult();
    }

    private void showResult() {
        Result.setText(simulation.result_info);
        Result_skills.setText(simulation.skills_info);
        Result_lvling.setText(simulation.lvling_info);
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
