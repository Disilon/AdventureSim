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
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.HashMap;
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
    private JPanel SetupPanel;
    private JPanel SkillPanel;
    private JPanel EquipPanel;
    private JPanel SimPanel;
    private JScrollPane LeftPane;
    private JScrollPane RightPane;
    private JPanel BottomPanel;
    private JPanel TopPanel;
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
    private JComboBox Pill;
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
    private JTextArea Result_details;
    private JTextArea Result_lvling;
    private JScrollPane Result_p;
    private JScrollPane Result_details_p;
    private JScrollPane Result_lvling_p;
    private JButton Save;
    private JButton Load;
    private JButton LoadResearch;
    private JComboBox Enemy;
    private JComboBox GameVersion;
    private JSpinner Milestone;
    private JSpinner Alchemist_CL;
    private JSpinner R_spd_bonus;
    private JSpinner Rp_balance;
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
    private JCheckBox Offline;
    private JFormattedTextField ML_p;
    private JFormattedTextField CL_p;
    private JButton Update_lvls;
    private JButton SkillSwitch1_2;
    private JButton SkillSwitch2_3;
    private JButton SkillSwitch3_4;
    private JSpinner Hard_hp;
    private JSpinner Hard_stats;
    private JSpinner Hard_reward;
    private JSpinner Highest_cl;
    private JCheckBox EnemyMinLvlIncrease;
    private JCheckBox Balance1;
    private JCheckBox Balance2;
    private JCheckBox Balance3;
    private JCheckBox CraftingProgress;
    private JCheckBox SmithingProgress;
    private JCheckBox AlchemyProgress;
    private JCheckBox AlchemyConsumptionBased;
    private JMenuBar Bar;
    private JButton New_tab;
    private JTable ActiveSkills;
    private JTable PassiveSkills;

    SkillTableModel activeSkillsModel;
    SkillTableModel passiveSkillsModel;
    GridBagConstraints gbc = new GridBagConstraints();

    public Player player;
    public Simulation simulation;
    public Setup setup;
    public HashMap<String, Double> passives_lvls = new HashMap<>(64);
    public HashMap<String, Double> actives_lvls = new HashMap<>(64);
    public LinkedHashMap<JMenu, Setup> tabs = new LinkedHashMap<>();
    public HashMap<String, JSpinner> research_l = new HashMap<>(32);
    public HashMap<String, JSpinner> research_w = new HashMap<>(32);
    public HashMap<String, JSpinner> bestiary = new HashMap<>(16);
    public HashMap<Integer, CoreUI> cores = new HashMap<>(15);
    public JMenu selected_tab;
    ActionListener itemListener;
    MenuListener menuListener;
    Gson gson = new Gson();
    Font default_font = new Font("Courier", Font.BOLD, 12);
    Font disabled_font = new Font("Courier", Font.BOLD, 12);
    HashMap<Integer, int[]> core_slaves = new HashMap<>();

    public class CoreUI {
        public JComboBox name;
        public JComboBox grade;
        public JSpinner lvl;
        public boolean enabled;
    }

    public UserForm(String title) throws URISyntaxException {
        super(title);
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
                        selected_tab = null;
                        Bar.remove(tab);
                        selectTab(tabs.lastEntry().getKey());
                        Bar.updateUI();
                    }
                }
            }
        }
        core_slaves.put(0, new int[]{10});
        core_slaves.put(1, new int[]{11});
        core_slaves.put(20, new int[]{30, 40});
        core_slaves.put(50, new int[]{60});
        core_slaves.put(21, new int[]{31, 41});
        core_slaves.put(70, new int[]{80});
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
        SetupPanel = new JPanel();
        SetupPanel.setLayout(new GridBagLayout());
        SkillPanel = new JPanel();
        SkillPanel.setLayout(new GridBagLayout());
        EquipPanel = new JPanel();
        EquipPanel.setLayout(new GridBagLayout());
        SimPanel = new JPanel();
        SimPanel.setLayout(new GridBagLayout());
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
                if (tabs.size() < 15) {
                    JMenu new_tab = createTab();
                    loadSetup(tabs.get(selected_tab));
                }
            }
        });
        Bar.add(New_tab);

        Map attributes = disabled_font.getAttributes();
        attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
        disabled_font = new Font(attributes);

        MLLabel = new JLabel();
        MLLabel.setText("ML: ");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        SkillPanel.add(MLLabel, gbc);
        ML = createCustomSpinner(120, 1, 10000, 1);
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        SkillPanel.add(ML, gbc);
        ML_p = new JFormattedTextField(df2p);
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 5;
//        gbc.ipady = 3;
        SkillPanel.add(ML_p, gbc);
        CLLabel = new JLabel();
        CLLabel.setText("CL: ");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        SkillPanel.add(CLLabel, gbc);
        CL = createCustomSpinner(62, 0, 10000, 1);
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 1;
        SkillPanel.add(CL, gbc);
        CL_p = new JFormattedTextField(df2p);
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 5;
//        gbc.ipady = 3;
        SkillPanel.add(CL_p, gbc);

        ClassLabel = new JLabel();
        ClassLabel.setText("Class:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        SkillPanel.add(ClassLabel, gbc);
        PlayerClass = new JComboBox(Player.availableClasses);
        PlayerClass.setMaximumRowCount(40);
        PlayerClass.setSelectedIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SkillPanel.add(PlayerClass, gbc);

        final JLabel label15 = new JLabel();
        label15.setText("Enemy:");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        SkillPanel.add(label15, gbc);
        Enemy = new JComboBox(Zone.values());
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        Enemy.setMaximumRowCount(25);
        SkillPanel.add(Enemy, gbc);

        Save = new JButton();
        Save.setText("Save");
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 5, 0, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SkillPanel.add(Save, gbc);
        Load = new JButton();
        Load.setText("Load");
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 5, 0, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SkillPanel.add(Load, gbc);

        int element_row = 2;
        final JLabel label1 = new JLabel();
        label1.setText("Potion setup:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = element_row;
        SkillPanel.add(label1, gbc);
        final JLabel label1_1 = new JLabel("Pill: ");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.EAST;
        SkillPanel.add(label1_1, gbc);
        Pill = new JComboBox<>(Disilon.Pill.getAvailablePills().toArray());
        Pill.setMaximumRowCount(21);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = element_row;
        SkillPanel.add(Pill, gbc);
        element_row++;
        Potion1 = new JComboBox<>(Potion.getAvailablePotions());
        Potion1.setMaximumRowCount(21);
        Potion1.setSelectedIndex(6);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SkillPanel.add(Potion1, gbc);
        Potion1_t = new JSpinner(new SpinnerNumberModel(50, 25, 95, 5));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = element_row;
        gbc.ipadx = 12;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SkillPanel.add(Potion1_t, gbc);
        Potion2 = new JComboBox<>(Potion.getAvailablePotions());
        Potion2.setMaximumRowCount(21);
        Potion2.setSelectedIndex(16);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SkillPanel.add(Potion2, gbc);
        Potion2_t = new JSpinner(new SpinnerNumberModel(50, 25, 95, 5));
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = element_row;
        gbc.ipadx = 12;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SkillPanel.add(Potion2_t, gbc);
        Potion3 = new JComboBox<>(Potion.getAvailablePotions());
        Potion3.setMaximumRowCount(21);
        Potion3.setSelectedIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SkillPanel.add(Potion3, gbc);
        Potion3_t = new JSpinner(new SpinnerNumberModel(50, 25, 95, 5));
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = element_row;
        gbc.ipadx = 22;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SkillPanel.add(Potion3_t, gbc);
        element_row++;
        final JLabel label2 = new JLabel();
        label2.setText("Active skills:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = element_row;
        gbc.gridwidth = 6;
        SkillPanel.add(label2, gbc);
        element_row++;
        Skill1 = new JComboBox();
        Skill1.setMaximumRowCount(30);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = element_row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SkillPanel.add(Skill1, gbc);
        Skill2 = new JComboBox();
        Skill2.setMaximumRowCount(30);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = element_row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SkillPanel.add(Skill2, gbc);
        Skill3 = new JComboBox();
        Skill3.setMaximumRowCount(30);
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = element_row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SkillPanel.add(Skill3, gbc);
        Skill4 = new JComboBox();
        Skill4.setMaximumRowCount(30);
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = element_row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SkillPanel.add(Skill4, gbc);
        element_row++;
        Skill1_mod = new JComboBox<>(SkillMod.getAvailableMods());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SkillPanel.add(Skill1_mod, gbc);
        Skill2_mod = new JComboBox<>(SkillMod.getAvailableMods());
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SkillPanel.add(Skill2_mod, gbc);
        Skill3_mod = new JComboBox<>(SkillMod.getAvailableMods());
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SkillPanel.add(Skill3_mod, gbc);
        Skill4_mod = new JComboBox<>(SkillMod.getAvailableMods());
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SkillPanel.add(Skill4_mod, gbc);
        Skill1_s = new JSpinner(new SpinnerNumberModel(1, 0, 1000000, 1));
        Skill1_s.setPreferredSize(new Dimension(90, 20));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SkillPanel.add(Skill1_s, gbc);
        Skill2_s = new JSpinner(new SpinnerNumberModel(1, 0, 1000000, 1));
        Skill2_s.setPreferredSize(new Dimension(90, 20));
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SkillPanel.add(Skill2_s, gbc);
        Skill3_s = new JSpinner(new SpinnerNumberModel(1, 0, 1000000, 1));
        Skill3_s.setPreferredSize(new Dimension(90, 20));
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SkillPanel.add(Skill3_s, gbc);
        Skill4_s = new JSpinner(new SpinnerNumberModel(1, 0, 1000000, 1));
        Skill4_s.setPreferredSize(new Dimension(90, 20));
        gbc = new GridBagConstraints();
        gbc.gridx = 8;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SkillPanel.add(Skill4_s, gbc);
        element_row++;

        SkillSwitch1_2 = new JButton("<->");
        SkillSwitch1_2.setToolTipText("Switch skill order");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = element_row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
        SkillPanel.add(SkillSwitch1_2, gbc);
        SkillSwitch2_3 = new JButton("<->");
        SkillSwitch2_3.setToolTipText("Switch skill order");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = element_row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
        SkillPanel.add(SkillSwitch2_3, gbc);
        SkillSwitch3_4 = new JButton("<->");
        SkillSwitch3_4.setToolTipText("Switch skill order");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = element_row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
        SkillPanel.add(SkillSwitch3_4, gbc);

        element_row++;
        final JLabel label3 = new JLabel();
        label3.setText("Passive skills:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = element_row;
        gbc.gridwidth = 6;
        gbc.insets = new Insets(10, 5, 0, 5);
        SkillPanel.add(label3, gbc);
        element_row++;
        Pskill1 = new JComboBox();
        Pskill1.setMaximumRowCount(30);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = element_row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SkillPanel.add(Pskill1, gbc);
        Pskill2 = new JComboBox();
        Pskill2.setMaximumRowCount(30);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = element_row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SkillPanel.add(Pskill2, gbc);
        Pskill3 = new JComboBox();
        Pskill3.setMaximumRowCount(30);
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = element_row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SkillPanel.add(Pskill3, gbc);
        Pskill4 = new JComboBox();
        Pskill4.setMaximumRowCount(30);
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = element_row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SkillPanel.add(Pskill4, gbc);

        element_row = 0;
        final JLabel label4 = new JLabel();
        label4.setText("Equipment:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = element_row;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(10, 5, 0, 5);
        EquipPanel.add(label4, gbc);
        element_row++;
        final JLabel label5 = new JLabel();
        label5.setText("Weapon MH");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        EquipPanel.add(label5, gbc);
        MH_name = new JComboBox<String>();
        MH_name.setMaximumRowCount(30);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(MH_name, gbc);
        MH_tier = new JComboBox<>(Equipment.Quality.values());
        MH_tier.setMaximumRowCount(16);
        MH_tier.setSelectedIndex(3);
        MH_tier.setToolTipText("Quality");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(MH_tier, gbc);
        MH_lvl = new JSpinner(new SpinnerNumberModel(0, 0, 900, 1));
        MH_lvl.setToolTipText("Upgrade lvl");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(MH_lvl, gbc);
        element_row++;
        final JLabel label6 = new JLabel();
        label6.setText("Offhand");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        EquipPanel.add(label6, gbc);
        OH_name = new JComboBox();
        OH_name.setMaximumRowCount(30);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(OH_name, gbc);
        OH_tier = new JComboBox<>(Equipment.Quality.values());
        OH_tier.setMaximumRowCount(16);
        OH_tier.setSelectedIndex(3);
        OH_tier.setToolTipText("Quality");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(OH_tier, gbc);
        OH_lvl = new JSpinner(new SpinnerNumberModel(0, 0, 900, 1));
        OH_lvl.setToolTipText("Upgrade lvl");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(OH_lvl, gbc);
        element_row++;
        SetSetup = new JCheckBox();
        SetSetup.setSelected(true);
        SetSetup.setText("Use chest and accessory 1 values for full set");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = element_row;
        gbc.gridwidth = 4;
        EquipPanel.add(SetSetup, gbc);
        element_row++;

        final JLabel label8 = new JLabel();
        label8.setText("Chest");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        EquipPanel.add(label8, gbc);
        Chest_name = new JComboBox();
        Chest_name.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(Chest_name, gbc);
        Chest_tier = new JComboBox<>(Equipment.Quality.values());
        Chest_tier.setMaximumRowCount(16);
        Chest_tier.setSelectedIndex(3);
        Chest_tier.setToolTipText("Quality");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(Chest_tier, gbc);
        Chest_lvl = new JSpinner(new SpinnerNumberModel(0, 0, 900, 1));
        Chest_lvl.setToolTipText("Upgrade lvl");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(Chest_lvl, gbc);
        element_row++;

        final JLabel label9 = new JLabel();
        label9.setText("Pants");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        EquipPanel.add(label9, gbc);
        Pants_name = new JComboBox();
        Pants_name.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(Pants_name, gbc);
        Pants_tier = new JComboBox<>(Equipment.Quality.values());
        Pants_tier.setMaximumRowCount(16);
        Pants_tier.setSelectedIndex(3);
        Pants_tier.setToolTipText("Quality");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(Pants_tier, gbc);
        Pants_lvl = new JSpinner(new SpinnerNumberModel(0, 0, 900, 1));
        Pants_lvl.setToolTipText("Upgrade lvl");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(Pants_lvl, gbc);
        element_row++;

        final JLabel label7 = new JLabel();
        label7.setText("Helmet");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        EquipPanel.add(label7, gbc);
        Helmet_name = new JComboBox();
        Helmet_name.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(Helmet_name, gbc);
        Helmet_tier = new JComboBox<>(Equipment.Quality.values());
        Helmet_tier.setMaximumRowCount(16);
        Helmet_tier.setSelectedIndex(3);
        Helmet_tier.setToolTipText("Quality");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(Helmet_tier, gbc);
        Helmet_lvl = new JSpinner(new SpinnerNumberModel(0, 0, 900, 1));
        Helmet_lvl.setToolTipText("Upgrade lvl");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(Helmet_lvl, gbc);
        element_row++;

        final JLabel label10 = new JLabel();
        label10.setText("Bracer");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        EquipPanel.add(label10, gbc);
        Bracer_name = new JComboBox();
        Bracer_name.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(Bracer_name, gbc);
        Bracer_tier = new JComboBox<>(Equipment.Quality.values());
        Bracer_tier.setMaximumRowCount(16);
        Bracer_tier.setSelectedIndex(3);
        Bracer_tier.setToolTipText("Quality");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(Bracer_tier, gbc);
        Bracer_lvl = new JSpinner(new SpinnerNumberModel(0, 0, 900, 1));
        Bracer_lvl.setToolTipText("Upgrade lvl");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(Bracer_lvl, gbc);
        element_row++;
        final JLabel label11 = new JLabel();
        label11.setText("Boots");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        EquipPanel.add(label11, gbc);
        Boots_name = new JComboBox();
        Boots_name.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(Boots_name, gbc);
        Boots_tier = new JComboBox<>(Equipment.Quality.values());
        Boots_tier.setMaximumRowCount(16);
        Boots_tier.setSelectedIndex(3);
        Boots_tier.setToolTipText("Quality");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(Boots_tier, gbc);
        Boots_lvl = new JSpinner(new SpinnerNumberModel(0, 0, 900, 1));
        Boots_lvl.setToolTipText("Upgrade lvl");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(Boots_lvl, gbc);
        element_row++;
        final JLabel label12 = new JLabel();
        label12.setText("Accessory 1 ");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        EquipPanel.add(label12, gbc);
        Accessory1_name = new JComboBox();
        Accessory1_name.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(Accessory1_name, gbc);
        Accessory1_tier = new JComboBox<>(Equipment.Quality.values());
        Accessory1_tier.setMaximumRowCount(16);
        Accessory1_tier.setSelectedIndex(5);
        Accessory1_tier.setToolTipText("Quality");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(Accessory1_tier, gbc);
        Accessory1_lvl = new JSpinner(new SpinnerNumberModel(0, 0, 900, 1));
        Accessory1_lvl.setToolTipText("Upgrade lvl");
        Accessory1_lvl.setValue(25);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(Accessory1_lvl, gbc);
        element_row++;
        final JLabel label13 = new JLabel();
        label13.setText("Accessory 2 ");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        EquipPanel.add(label13, gbc);
        Accessory2_name = new JComboBox();
        Accessory2_name.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(Accessory2_name, gbc);
        Accessory2_tier = new JComboBox<>(Equipment.Quality.values());
        Accessory2_tier.setMaximumRowCount(16);
        Accessory2_tier.setSelectedIndex(3);
        Accessory2_tier.setToolTipText("Quality");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(Accessory2_tier, gbc);
        Accessory2_lvl = new JSpinner(new SpinnerNumberModel(0, 0, 900, 1));
        Accessory2_lvl.setToolTipText("Upgrade lvl");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(Accessory2_lvl, gbc);
        element_row++;
        final JLabel label14 = new JLabel();
        label14.setText("Neck");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        EquipPanel.add(label14, gbc);
        Necklace_name = new JComboBox();
        Necklace_name.setMaximumRowCount(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(Necklace_name, gbc);
        Necklace_tier = new JComboBox<>(Equipment.Quality.values());
        Necklace_tier.setMaximumRowCount(16);
        Necklace_tier.setSelectedIndex(3);
        Necklace_tier.setToolTipText("Quality");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(Necklace_tier, gbc);
        Necklace_lvl = new JSpinner(new SpinnerNumberModel(0, 0, 900, 1));
        Necklace_lvl.setToolTipText("Upgrade lvl");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = element_row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(Necklace_lvl, gbc);

        element_row = 0;
        int element_column = 4;
        final JLabel label4_1 = new JLabel("Cores:");
        gbc = new GridBagConstraints();
        gbc.gridx = element_column;
        gbc.gridy = element_row;
        gbc.gridwidth = 6;
        gbc.insets = new Insets(10, 5, 0, 5);
        EquipPanel.add(label4_1, gbc);
        element_row++;
        for (int i = 0; i < 10; i++) {
            int id = i * 10;
            int x = element_column;
//            int y = i < 3 ? element_row + i : element_row + i + 1;
            int y = i < 2 ? element_row + i : element_row + i + 1;
            addCore(id, x, y);
            if (i < 4) addCore(id + 1, x + 3, y); //weapons, chest, pants
            if (i == 2) addCore(id + 2, x + 6, y); //chest
        }

        element_column = 0;
        element_row = 0;
        EnemyMinLvlIncrease = new JCheckBox("Enemy Min Lvl Increase");
        gbc = new GridBagConstraints();
        gbc.gridx = element_column;
        gbc.gridy = element_row;
        element_row++;
        SimPanel.add(EnemyMinLvlIncrease, gbc);

        Offline = new JCheckBox();
        Offline.setSelected(false);
        Offline.setText("Offline calc");
        gbc = new GridBagConstraints();
        gbc.gridx = element_column;
        gbc.gridy = element_row;
        element_row++;
        SimPanel.add(Offline, gbc);

        Sim_type = new ButtonGroup();
        Sim_num = new JRadioButton();
        Sim_num.setText("Number of simulations:");
        Sim_type.add(Sim_num);
        gbc = new GridBagConstraints();
        gbc.gridx = element_column;
        gbc.gridy = element_row;
        element_row++;
        SimPanel.add(Sim_num, gbc);
        Sim_time = new JRadioButton();
        Sim_time.setText("Number of hours:");
        Sim_type.add(Sim_time);
        gbc = new GridBagConstraints();
        gbc.gridx = element_column;
        gbc.gridy = element_row;
        element_row++;
        SimPanel.add(Sim_time, gbc);
        Sim_lvl = new JRadioButton();
        Sim_lvl.setText("Until CL reached:");
        Sim_type.add(Sim_lvl);
        gbc = new GridBagConstraints();
        gbc.gridx = element_column;
        gbc.gridy = element_row;
        element_row++;
        SimPanel.add(Sim_lvl, gbc);

        Simulations = new JSpinner(new SpinnerNumberModel(1000, 1, 1000000, 1) {
            @Override
            public Object getNextValue() {
                Object nextValue = super.getValue();
                int x = (int) Double.parseDouble(nextValue.toString()) * 10;
                if (x > 1000000) x = 1000000;
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
        Simulations.setToolTipText("Maximum value 1000000");
        gbc = new GridBagConstraints();
        gbc.gridx = element_column;
        gbc.gridy = element_row;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SimPanel.add(Simulations, gbc);
        SimHours = createCustomSpinner(1.0, 0, 10000, 1);
        SimHours.setVisible(false);
        gbc = new GridBagConstraints();
        gbc.gridx = element_column;
        gbc.gridy = element_row;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SimPanel.add(SimHours, gbc);
        SimCL = createCustomSpinner(90, 0, 1000, 1);
        SimCL.setVisible(false);
        gbc = new GridBagConstraints();
        gbc.gridx = element_column;
        gbc.gridy = element_row;
        element_row++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SimPanel.add(SimCL, gbc);
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
        gbc.gridx = element_column;
        gbc.gridy = element_row;
        element_row++;
        SimPanel.add(Leveling, gbc);

        Run = new JButton();
        Run.setText("Simulate");
        gbc = new GridBagConstraints();
        gbc.gridx = element_column;
        gbc.gridy = element_row;
        element_row++;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SimPanel.add(Run, gbc);

        Update_lvls = new JButton();
        Update_lvls.setText("Update lvls from sim");
        gbc = new GridBagConstraints();
        gbc.gridx = element_column;
        gbc.gridy = element_row;
        element_row++;
        gbc.insets = new Insets(0, 5, 0, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SimPanel.add(Update_lvls, gbc);

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
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(5, 5, 0, 5);
        LeftPanel.add(ActiveSkills.getTableHeader(), gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.NORTH;
        LeftPanel.add(ActiveSkills, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(5, 5, 0, 5);
        LeftPanel.add(PassiveSkills.getTableHeader(), gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.NORTH;
        LeftPanel.add(PassiveSkills, gbc);

        LoadResearch = new JButton("Load research/skills");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridheight = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.insets = new Insets(10, 5, 0, 5);
//        gbc.fill = GridBagConstraints.HORIZONTAL;
        LeftPanel.add(LoadResearch, gbc);
        final JLabel label23 = new JLabel("Game version");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(5, 5, 0, 5);
        LeftPanel.add(label23, gbc);
        GameVersion = new JComboBox<>(Main.availableVersions);
        GameVersion.setMaximumRowCount(24);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.NORTH;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
        LeftPanel.add(GameVersion, gbc);

        int column = 0;
        int row = 4;
        final JLabel label25 = new JLabel("Milestone exp %");
        gbc = new GridBagConstraints();
        gbc.gridx = column;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(5, 5, 0, 5);
        LeftPanel.add(label25, gbc);
        Milestone = createCustomSpinner(155, 100, 1000, 2.5);
        gbc = new GridBagConstraints();
        gbc.gridx = column;
        gbc.gridy = row + 1;
        gbc.anchor = GridBagConstraints.NORTH;
        LeftPanel.add(Milestone, gbc);
        column += 1;
        CraftingProgress = new JCheckBox("Crafting leveling");
        gbc = new GridBagConstraints();
        gbc.gridx = column;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTH;
//        gbc.insets = new Insets(5, 5, 0, 5);
        LeftPanel.add(CraftingProgress, gbc);
        row += 1;
        SmithingProgress = new JCheckBox("Smithing leveling");
        gbc = new GridBagConstraints();
        gbc.gridx = column;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTH;
//        gbc.insets = new Insets(5, 5, 0, 5);
        LeftPanel.add(SmithingProgress, gbc);
        row += 1;
        AlchemyProgress = new JCheckBox("Alchemy leveling");
        gbc = new GridBagConstraints();
        gbc.gridx = column;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTH;
//        gbc.insets = new Insets(5, 5, 0, 5);
        LeftPanel.add(AlchemyProgress, gbc);
        row += 1;
        AlchemyConsumptionBased = new JCheckBox("Use cons. progress");
        gbc = new GridBagConstraints();
        gbc.gridx = column;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTH;
//        gbc.insets = new Insets(5, 5, 0, 5);
        LeftPanel.add(AlchemyConsumptionBased, gbc);
        row += 1;

        final JLabel label27_1 = new JLabel("Alchemist CL");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(5, 5, 0, 5);
        LeftPanel.add(label27_1, gbc);
        Alchemist_CL = new JSpinner(new SpinnerNumberModel(0, 0, 200, 1));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.NORTH;
        LeftPanel.add(Alchemist_CL, gbc);

        final JLabel r_spd = new JLabel("RS statue %");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(5, 5, 0, 5);
        LeftPanel.add(r_spd, gbc);
        R_spd_bonus = createCustomSpinner(0, 0, 1000, 0.5);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        LeftPanel.add(R_spd_bonus, gbc);

        final JLabel rp_balance_l = new JLabel("Current RP");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(5, 5, 0, 5);
        LeftPanel.add(rp_balance_l, gbc);
        Rp_balance = new JSpinner(new SpinnerNumberModel(1000, 0, 1000000000, 1000));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTH;
        LeftPanel.add(Rp_balance, gbc);

        column = 0;
        row = 10;
        final JLabel Hard_hp_l = new JLabel("Hard hp %");
        gbc = new GridBagConstraints();
        gbc.gridx = column;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(5, 5, 0, 5);
        LeftPanel.add(Hard_hp_l, gbc);
        Hard_hp = createCustomSpinner(100, 5, 2000, 10);
        gbc = new GridBagConstraints();
        gbc.gridx = column;
        gbc.gridy = row + 1;
        gbc.anchor = GridBagConstraints.NORTH;
        LeftPanel.add(Hard_hp, gbc);
        final JLabel Hard_stats_l = new JLabel("Hard stats %");
        gbc = new GridBagConstraints();
        gbc.gridx = column + 1;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(5, 5, 0, 5);
        LeftPanel.add(Hard_stats_l, gbc);
        Hard_stats = createCustomSpinner(100, 5, 1000, 5);
        gbc = new GridBagConstraints();
        gbc.gridx = column + 1;
        gbc.gridy = row + 1;
        gbc.anchor = GridBagConstraints.NORTH;
        //gbc.insets = new Insets(10, 5, 0, 5);
        LeftPanel.add(Hard_stats, gbc);
        final JLabel Hard_reward_l = new JLabel("Hard reward %");
        gbc = new GridBagConstraints();
        gbc.gridx = column + 2;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(5, 5, 0, 5);
        LeftPanel.add(Hard_reward_l, gbc);
        Hard_reward = createCustomSpinner(100, 5, 1000, 5);
        gbc = new GridBagConstraints();
        gbc.gridx = column + 2;
        gbc.gridy = row + 1;
        gbc.anchor = GridBagConstraints.NORTH;
        LeftPanel.add(Hard_reward, gbc);

        row = 12;
//        Balance1 = new JCheckBox("Extra attack overkill overwrite");
//        gbc = new GridBagConstraints();
//        gbc.gridx = column;
//        gbc.gridy = row;
//        gbc.gridwidth = 3;
//        gbc.anchor = GridBagConstraints.NORTH;
//        LeftPanel.add(Balance1, gbc);
//        row += 1;
//        Balance2 = new JCheckBox("Extra attack backstab dmg mult");
//        gbc = new GridBagConstraints();
//        gbc.gridx = column;
//        gbc.gridy = row;
//        gbc.gridwidth = 3;
//        gbc.anchor = GridBagConstraints.NORTH;
//        LeftPanel.add(Balance2, gbc);
//        row += 1;
//        Balance3 = new JCheckBox("Reduced overkill for crits");
//        Balance3.setSelected(false);
//        Balance3.setVisible(false);
//        gbc = new GridBagConstraints();
//        gbc.gridx = column;
//        gbc.gridy = row;
//        gbc.gridwidth = 3;
//        gbc.anchor = GridBagConstraints.NORTH;
//        LeftPanel.add(Balance3, gbc);

        final JLabel Highest_cl_l = new JLabel("Highest CL");
        gbc = new GridBagConstraints();
        gbc.gridx = column + 2;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(5, 5, 0, 5);
        LeftPanel.add(Highest_cl_l, gbc);
        row++;
        Highest_cl = createCustomSpinner(0, 0, 200, 1);
        Highest_cl.setToolTipText("used for Training set secondary bonus");
        gbc = new GridBagConstraints();
        gbc.gridx = column + 2;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.NORTH;
        LeftPanel.add(Highest_cl, gbc);

        row++;
        final JLabel label29 = new JLabel("Bestiary");
        gbc = new GridBagConstraints();
        gbc.gridx = column;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 5, 0);
        LeftPanel.add(label29, gbc);
        row++;
        for (String name : getAllBestiary()) {
            row = addBestiary(name, column, row);
        }


        row = 2;
        final JLabel label28 = new JLabel("Research");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 0, 0, 0);
        LeftPanel.add(label28, gbc);
        final JLabel label28_1 = new JLabel("lvl");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 0, 0, 0);
        LeftPanel.add(label28_1, gbc);
        final JLabel label28_2 = new JLabel("weight");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 0, 0);
        LeftPanel.add(label28_2, gbc);

        row++;
        for (String name : getAllResearches()) {
            row = addResearch(name, 3, row);
        }

        LeftPane = new JScrollPane(LeftPanel);
        LeftPane.setPreferredSize(new Dimension(590, 575));
        LeftPane.getVerticalScrollBar().setUnitIncrement(16);
        LeftPane.getHorizontalScrollBar().setUnitIncrement(16);
        LeftPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        LeftPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        SetupPanel.add(SkillPanel, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        SetupPanel.add(SimPanel, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        SetupPanel.add(EquipPanel, gbc);

        RightPane = new JScrollPane(SetupPanel);
        RightPane.setPreferredSize(new Dimension(995, 585));
        RightPane.getVerticalScrollBar().setUnitIncrement(16);
        RightPane.getHorizontalScrollBar().setUnitIncrement(16);
        RightPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        RightPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        BottomPanel = new JPanel();
        BottomPanel.setLayout(new GridBagLayout());

        Result = new JTextArea();
        Result.setEditable(false);
        Result.setLineWrap(true);
        Result.setText("Result will be here");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 6;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        Result_p = new JScrollPane(Result, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        Result_p.getVerticalScrollBar().setUnitIncrement(16);
        Result_p.setMinimumSize(new Dimension(50, 50));
        Result_p.setPreferredSize(new Dimension(320, 400));
        BottomPanel.add(Result_p, gbc);

        Result_details = new JTextArea();
        Result_details.setEditable(false);
        Result_details.setLineWrap(true);
        Result_details.setText("Info about used skills will be here");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 7;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        Result_details_p = new JScrollPane(Result_details, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        Result_details_p.getVerticalScrollBar().setUnitIncrement(16);
        Result_details_p.setMinimumSize(new Dimension(50, 50));
        Result_details_p.setPreferredSize(new Dimension(380, 400));
        BottomPanel.add(Result_details_p, gbc);

        Result_lvling = new JTextArea();
        Result_lvling.setEditable(false);
        Result_lvling.setLineWrap(true);
        Result_lvling.setText("Leveling results will be here");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 5;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        Result_lvling_p = new JScrollPane(Result_lvling, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        Result_lvling_p.getVerticalScrollBar().setUnitIncrement(16);
        Result_lvling_p.setMinimumSize(new Dimension(50, 50));
        Result_lvling_p.setPreferredSize(new Dimension(280, 400));
        BottomPanel.add(Result_lvling_p, gbc);

        RightPanel = new JPanel();
        RightPanel.setLayout(new BoxLayout(RightPanel, BoxLayout.Y_AXIS));
        RightPanel.add(RightPane);
        RightPanel.add(BottomPanel);

        RootPanel.setLayout(new BoxLayout(RootPanel, BoxLayout.X_AXIS));
        RootPanel.add(LeftPane);
        RootPanel.add(RightPanel);
        this.setContentPane(RootPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setPreferredSize(loadSettings(Main.getJarPath() + "/Settings.ini").getWindowSize());
        Main.time_limit = loadSettings(Main.getJarPath() + "/Settings.ini").getSimulation_time_limit();
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
        LoadResearch.addActionListener(new ActionListener() {
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
                        Setup show_setup = file;
                        if (show_setup != null) loadResearch(show_setup);
                    }
                }
            }
        });
        Update_lvls.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (player == null || player.ml == 0) {
                    JOptionPane.showMessageDialog(SetupPanel, "You need to sim first! No lvling data.", "Warning",
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
                    info.append("<td style=\"width:45%;height:10px;\">");
                    info.append(dfm.format(Milestone.getValue())).append("%");
                    info.append("</td>");
                    info.append("<td style=\"width:10%;height:10px;\">");
                    info.append("->");
                    info.append("</td>");
                    info.append("<td style=\"width:45%;height:10px;\">");
                    info.append(dfm.format(player.milestone_exp_mult * 100)).append("%");
                    info.append("</td>");

                    info.append("<tr style=\"height:10px;\"><td colspan=\"3\">");
                    info.append("CL:");
                    info.append("</td></tr>");
                    info.append("<tr style=\"height:10px;\">");
                    info.append("<td>");
                    info.append((int) player.old_cl).append(" (");
                    info.append(df2.format((player.old_cl - (int) player.old_cl) * 100)).append("%)");
                    info.append("</td>");
                    info.append("<td>");
                    info.append("->");
                    info.append("</td>");
                    info.append("<td>");
                    info.append(player.cl).append(" (");
                    info.append(df2.format(player.getCLpercent())).append("%)");
                    info.append("</td>");

                    info.append("<tr style=\"height:10px;\"><td colspan=\"3\">");
                    info.append("ML:");
                    info.append("</td></tr>");
                    info.append("<tr style=\"height:10px;\">");
                    info.append("<td>");
                    info.append((int) player.old_ml).append(" (");
                    info.append(df2.format((player.old_ml - (int) player.old_ml) * 100)).append("%)");
                    info.append("</td>");
                    info.append("<td>");
                    info.append("->");
                    info.append("</td>");
                    info.append("<td>");
                    info.append(player.ml).append(" (");
                    info.append(df2.format(player.getMLpercent())).append("%)");
                    info.append("</td>");
                    for (ActiveSkill a : player.active_skills.values()) {
                        if (a.checkEnabled() && a.old_lvl < player.max_skill_lvl) {
                            info.append("<tr style=\"height:10px;\"><td colspan=\"3\">");
                            info.append(a.name);
                            info.append("</td></tr>");
                            info.append("<tr style=\"height:10px;\">");
                            info.append("<td>");
                            info.append((int) a.old_lvl).append(" (");
                            info.append(df2.format((a.old_lvl - (int) a.old_lvl) * 100)).append("%)");
                            info.append("</td>");
                            info.append("<td>");
                            info.append("->");
                            info.append("</td>");
                            info.append("<td>");
                            info.append(a.lvl).append(" (");
                            info.append(df2.format((a.exp / a.need_for_lvl(a.lvl)) * 100)).append("%)");
                            info.append("</td>");
                        }
                    }
                    for (PassiveSkill a : player.passives.values()) {
                        if ((a.enabled && a.old_lvl < player.max_skill_lvl) || (a.available && a.name.equals("Tsury Finke")) ||
                                a.name.equals("Crafting") || a.name.equals("Smithing") || a.name.equals("Alchemy")) {
                            info.append("<tr style=\"height:10px;\"><td colspan=\"3\">");
                            info.append(a.name);
                            info.append("</td></tr>");
                            info.append("<tr style=\"height:10px;\">");
                            info.append("<td>");
                            info.append((int) a.old_lvl).append(" (");
                            info.append(df2.format((a.old_lvl - (int) a.old_lvl) * 100)).append("%)");
                            info.append("</td>");
                            info.append("<td>");
                            info.append("->");
                            info.append("</td>");
                            info.append("<td>");
                            info.append(a.lvl).append(" (");
                            info.append(df2.format((a.exp / a.need_for_lvl(a.lvl)) * 100)).append("%)");
                            info.append("</td>");
                        }
                    }
                    info.append("<tr style=\"height:10px;\"><td colspan=\"3\">");
                    info.append("Research:");
                    info.append("</td></tr>");
                    for (String r : player.research_lvls.keySet()) {
                        int change = player.research_lvls.getOrDefault(r, 0.0).intValue() -
                                player.research_old_lvls.getOrDefault(r, 0.0).intValue();
                        if (change > 0) {
                            info.append("<tr style=\"height:10px;\"><td colspan=\"3\">");
                            info.append(r);
                            info.append(" +");
                            info.append(change);
                            info.append("</td></tr>");
                        }
                    }
                    info.append("</table>");
                    info.append("</body>");
                    info.append("</html>");
                    int selectedOption = JOptionPane.showConfirmDialog(SetupPanel, info, "Confirm", JOptionPane.YES_NO_OPTION);
                    if (selectedOption == JOptionPane.YES_OPTION) {
                        setup.milestone = player.milestone_exp_mult * 100;
                        setup.ml = player.ml + player.getMLpercent() / 100;
                        setup.cl = Math.min(player.getMaxCl(), player.cl + player.getCLpercent() / 100);
                        for (String s : player.active_skills.keySet()) {
                            setup.actives_lvls.put(s, Math.min(player.max_skill_lvl, player.active_skills.get(s).getLvl()));
                        }
                        for (String s : player.passives.keySet()) {
                            int max_lvl = s.equals("Tsury Finke") ? 100 : player.max_skill_lvl;
                            if (s.endsWith(" pill")) max_lvl = 1000;
                            if (s.equals("Crafting") || s.equals("Smithing") || s.equals("Alchemy")) {
                                max_lvl = 100;
                            }
                            setup.passives_lvls.put(s, Math.min(max_lvl, player.passives.get(s).getLvl()));
                        }
                        setup.rp_balance = (int) player.rp_balance;
                        if (simulation != null) {
                            setup.result_essential = simulation.result_info;
                            setup.result_skills = simulation.skills_info;
                            setup.result_lvling = simulation.lvling_info;
                            setup.stats = simulation.player.getAllStats();
                            setup.alchemist_lvl = simulation.player.alchemist_lvl;
                            setup.highest_cl = simulation.player.highest_cl;
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
                    JOptionPane.showMessageDialog(SetupPanel, "You need to setup first active skill", "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    if (Sim_lvl.isSelected() && ((int) Double.parseDouble(CL.getValue().toString()) >= (int) Double.parseDouble(SimCL.getValue().toString()))) {
                        JOptionPane.showMessageDialog(SetupPanel,
                                "You need to setup target CL higher than your current", "Warning",
                                JOptionPane.WARNING_MESSAGE);
                    } else {
                        if (checkSameItemsCombo()) {
                            JOptionPane.showMessageDialog(SetupPanel,
                                    "You're using some of your passive skills twice!", "Warning",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                        try {
                            setup = saveSetup();
                            Main.game_version = (int) Double.parseDouble(setup.gameversion);
                            player = simulation.setupAndRun(setup);
                            showResult();
//                            Stats.setText(simulation.player.getAllStats());
//                            Stats.setCaretPosition(0);
                            Result.setCaretPosition(0);
                            Result_details.setCaretPosition(0);
                            Result_lvling.setCaretPosition(0);
                        } catch (IllegalArgumentException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(SetupPanel, "Some field has illegal value!", "Exception",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            }
        });
        SkillSwitch1_2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object temp;
                temp = Skill1.getSelectedItem().toString();
                Skill1.setSelectedItem(Skill2.getSelectedItem().toString());
                Skill2.setSelectedItem(temp);
                temp = Skill1_mod.getSelectedItem();
                Skill1_mod.setSelectedItem(Skill2_mod.getSelectedItem());
                Skill2_mod.setSelectedItem(temp);
                temp = Skill1_s.getValue();
                Skill1_s.setValue(Skill2_s.getValue());
                Skill2_s.setValue(temp);
            }
        });
        SkillSwitch2_3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object temp;
                temp = Skill2.getSelectedItem().toString();
                Skill2.setSelectedItem(Skill3.getSelectedItem().toString());
                Skill3.setSelectedItem(temp);
                temp = Skill2_mod.getSelectedItem();
                Skill2_mod.setSelectedItem(Skill3_mod.getSelectedItem());
                Skill3_mod.setSelectedItem(temp);
                temp = Skill2_s.getValue();
                Skill2_s.setValue(Skill3_s.getValue());
                Skill3_s.setValue(temp);
            }
        });
        SkillSwitch3_4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object temp;
                temp = Skill3.getSelectedItem().toString();
                Skill3.setSelectedItem(Skill4.getSelectedItem().toString());
                Skill4.setSelectedItem(temp);
                temp = Skill3_mod.getSelectedItem();
                Skill3_mod.setSelectedItem(Skill4_mod.getSelectedItem());
                Skill4_mod.setSelectedItem(temp);
                temp = Skill3_s.getValue();
                Skill3_s.setValue(Skill4_s.getValue());
                Skill4_s.setValue(temp);
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
                updateWeapons();
            }
        });
        MH_tier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateWeapons();
            }
        });
        OH_name.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateWeapons();
            }
        });
        OH_tier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateWeapons();
            }
        });
        Chest_name.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SetSetup.isSelected() && Chest_name.getSelectedItem() != null && Chest_name.getSelectedItem() != "None") {
                    Equipment eq = equipmentData.items.get(Chest_name.getSelectedItem().toString());
                    if (eq != null) {
                        String set = eq.displayName;
                        Helmet_name.setSelectedItem(findItemFromSet(set, "HEADGEAR"));
                        Pants_name.setSelectedItem(findItemFromSet(set, "PANTS"));
                        Bracer_name.setSelectedItem(findItemFromSet(set, "BRACERS"));
                        Boots_name.setSelectedItem(findItemFromSet(set, "BOOTS"));
                    }
                }
                int slots = 3;
                String item_name = Chest_name.getSelectedItem().toString();
                if (equipmentData.less_slots.contains(item_name)) slots--;
                if (item_name.equals("None")) slots = 0;
                cores.get(20).enabled = slots >= 1;
                cores.get(22).enabled = slots >= 2;
                cores.get(21).enabled = slots >= 3;
                updateUI();
            }
        });
        Chest_tier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SetSetup.isSelected()) {
                    Helmet_tier.setSelectedItem(Chest_tier.getSelectedItem());
                    Pants_tier.setSelectedItem(Chest_tier.getSelectedItem());
                    Bracer_tier.setSelectedItem(Chest_tier.getSelectedItem());
                    Boots_tier.setSelectedItem(Chest_tier.getSelectedItem());
                }
            }
        });
        Chest_lvl.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (SetSetup.isSelected()) {
                    Helmet_lvl.setValue(Chest_lvl.getValue());
                    Pants_lvl.setValue(Chest_lvl.getValue());
                    Bracer_lvl.setValue(Chest_lvl.getValue());
                    Boots_lvl.setValue(Chest_lvl.getValue());
                }
            }
        });
        Pants_name.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int slots = 2;
                String item_name = Pants_name.getSelectedItem().toString();
                if (equipmentData.less_slots.contains(item_name)) slots--;
                if (item_name.equals("None")) slots = 0;
                cores.get(30).enabled = slots >= 1;
                cores.get(31).enabled = slots >= 2;
                updateUI();
            }
        });
        Helmet_name.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int slots = 1;
                String item_name = Helmet_name.getSelectedItem().toString();
                if (equipmentData.less_slots.contains(item_name)) slots--;
                if (item_name.equals("None")) slots = 0;
                cores.get(40).enabled = slots >= 1;
                updateUI();
            }
        });
        Bracer_name.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int slots = 1;
                String item_name = Bracer_name.getSelectedItem().toString();
                if (equipmentData.less_slots.contains(item_name)) slots--;
                if (item_name.equals("None")) slots = 0;
                cores.get(50).enabled = slots >= 1;
                updateUI();
            }
        });
        Boots_name.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int slots = 1;
                String item_name = Boots_name.getSelectedItem().toString();
                if (equipmentData.less_slots.contains(item_name)) slots--;
                if (item_name.equals("None")) slots = 0;
                cores.get(60).enabled = slots >= 1;
                updateUI();
            }
        });
        Accessory1_name.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SetSetup.isSelected() && Accessory1_name.getSelectedItem() != null && Accessory1_name.getSelectedItem() != "None") {
                    Equipment eq = equipmentData.items.get(Accessory1_name.getSelectedItem().toString());
                    if (eq != null) {
                        String set = eq.displayName;
                        Accessory2_name.setSelectedItem(findItemFromSet(set, "RING"));
                        Necklace_name.setSelectedItem(findItemFromSet(set, "NECK"));
                    }
                }
                int slots = 1;
                String item_name = Accessory1_name.getSelectedItem().toString();
                if (equipmentData.less_slots.contains(item_name)) slots--;
                if (item_name.equals("None")) slots = 0;
                cores.get(70).enabled = slots >= 1;
                updateUI();
            }
        });
        Accessory1_tier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SetSetup.isSelected()) {
                    Accessory2_tier.setSelectedItem(Accessory1_tier.getSelectedItem());
                    Necklace_tier.setSelectedItem(Accessory1_tier.getSelectedItem());
                }
            }
        });
        Accessory1_lvl.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (SetSetup.isSelected()) {
                    Accessory2_lvl.setValue(Accessory1_lvl.getValue());
                    Necklace_lvl.setValue(Accessory1_lvl.getValue());
                }
            }
        });
        Accessory2_name.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int slots = 1;
                String item_name = Accessory2_name.getSelectedItem().toString();
                if (equipmentData.less_slots.contains(item_name)) slots--;
                if (item_name.equals("None")) slots = 0;
                cores.get(80).enabled = slots >= 1;
                updateUI();
            }
        });
        Necklace_name.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int slots = 1;
                String item_name = Necklace_name.getSelectedItem().toString();
                if (equipmentData.less_slots.contains(item_name)) slots--;
                if (item_name.equals("None")) slots = 0;
                cores.get(90).enabled = slots >= 1;
                updateUI();
            }
        });
        for (Integer key : core_slaves.keySet()) {
            CoreUI core = cores.get(key);
            if (core != null) {
                CoreUI master = core;
                int[] slaves = core_slaves.get(key);
                master.name.addActionListener(e -> {
                    if (SetSetup.isSelected()) {
                        for (int i = 0; i < slaves.length; i++) {
                            CoreUI slaveCore = cores.get(slaves[i]);
                            if (slaveCore != null) {
                                slaveCore.name.setSelectedIndex(master.name.getSelectedIndex());
                            }
                        }
                    }
                });
                master.grade.addActionListener(e -> {
                    if (SetSetup.isSelected()) {
                        for (int i = 0; i < slaves.length; i++) {
                            CoreUI slaveCore = cores.get(slaves[i]);
                            if (slaveCore != null) {
                                slaveCore.grade.setSelectedIndex(master.grade.getSelectedIndex());
                            }
                        }
                    }
                });
                master.lvl.addChangeListener(e -> {
                    if (SetSetup.isSelected()) {
                        for (int i = 0; i < slaves.length; i++) {
                            CoreUI slaveCore = cores.get(slaves[i]);
                            if (slaveCore != null) {
                                slaveCore.lvl.setValue(master.lvl.getValue());
                            }
                        }
                    }
                });
            }
        }
        loadEquipment();
        ArrayList<String> load_list = loadSettings(Main.getJarPath() + "/Settings.ini").getDefault_setups();
        for (String s : load_list) {
            selected_tab = createTab(s.replace(".json", ""));
            Setup default_setup = loadFile(Main.getJarPath() + "/" + s);
            if (default_setup == null) default_setup = new Setup();
            tabs.put(selected_tab, default_setup);
            loadTab(selected_tab);
        }
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
            spinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
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
            } catch (ParseException e) {
//                JOptionPane.showMessageDialog(null,
//                        "Invalid value, discarding.");
            }
            return super.stopCellEditing();
        }
    }

    private boolean checkSameItemsCombo() {
//        Map<Integer, Integer> elementCountMap = Arrays.stream(new Integer[]{Skill1.getSelectedIndex(), Skill2.getSelectedIndex(),
//                        Skill3.getSelectedIndex(), Skill4.getSelectedIndex()}).filter(a -> a > 0)
//                .collect(Collectors.toMap(Function.identity(), v -> 1, Integer::sum));
//        for (int count : elementCountMap.values()) {
//            if (count > 1) return true;
//        }
        Map<Integer, Integer> elementCountMap = Arrays.stream(new Integer[]{Pskill1.getSelectedIndex(), Pskill2.getSelectedIndex(),
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
        for (Equipment e : equipmentData.items.values()) {
            if (e.displayName.equals(name.replaceFirst("_.?","")) && e.slot.equals(slot)) {
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
//        new DefaultTableModel();
        saveSkillLvls();
        activeSkillsModel.setRowCount(0);
        for (String skill : player.active_skills.keySet()) {
            if (player.active_skills.get(skill).visible) {
                activeSkillsModel.addRow(new Object[]{skill, player.active_skills.get(skill).lvl,
                        (player.active_skills.get(skill).getLvl() - player.active_skills.get(skill).lvl) * 100});
            }
        }
        passiveSkillsModel.setRowCount(0);
        for (String skill : player.passives.keySet()) {
            if (player.passives.get(skill).visible) {
                passiveSkillsModel.addRow(new Object[]{skill, player.passives.get(skill).lvl,
                        (player.passives.get(skill).getLvl() - player.passives.get(skill).lvl) * 100});
            }
        }
        loadSkillLvls();
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
            JOptionPane.showMessageDialog(SetupPanel, "Some field has illegal value!", "Exception",
                    JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(SetupPanel, ex.getMessage(), "Exception",
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
        data.alchemist_lvl = (int) Double.parseDouble(Alchemist_CL.getValue().toString());
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
        data.zone = (Zone) Enemy.getSelectedItem();
        data.gameversion = GameVersion.getSelectedItem().toString();
        data.helmet_lvl = (int) Double.parseDouble(Helmet_lvl.getValue().toString());
        data.helmet_name = Helmet_name.getSelectedItem().toString();
        data.helmet_tier = (Equipment.Quality) Helmet_tier.getSelectedItem();
        data.mh_lvl = (int) Double.parseDouble(MH_lvl.getValue().toString());
        data.mh_name = MH_name.getSelectedItem().toString();
        data.mh_tier = (Equipment.Quality) MH_tier.getSelectedItem();
        data.milestone = Double.parseDouble(Milestone.getValue().toString());
        data.r_spd_bonus = Double.parseDouble(R_spd_bonus.getValue().toString());
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
        data.pill = Pill.getSelectedItem().toString();
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
//        data.extra_atk_overkill = Balance1.isSelected();
//        data.extra_atk_backstab_mult = Balance2.isSelected();
//        data.crit_overkill_reduced = Balance3.isSelected();
        data.crafting_progress = CraftingProgress.isSelected();
        data.smithing_progress = SmithingProgress.isSelected();
        data.alchemy_progress = AlchemyProgress.isSelected();
        data.alchemy_consumption_based = AlchemyConsumptionBased.isSelected();

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
//        data.stats = Stats.getText();
        if (Sim_num.isSelected()) data.sim_type = 1;
        if (Sim_time.isSelected()) data.sim_type = 2;
        if (Sim_lvl.isSelected()) data.sim_type = 3;
        data.simulations = (int) Double.parseDouble(Simulations.getValue().toString());
        data.sim_hours = Double.parseDouble(SimHours.getValue().toString());
        data.sim_cl = (int) Double.parseDouble(SimCL.getValue().toString());
        data.leveling = Leveling.isSelected();
        data.offline = Offline.isSelected();
        data.enemy_min_lvl_increase = EnemyMinLvlIncrease.isSelected();
        saveSkillLvls();
        data.actives_lvls = actives_lvls;
        data.passives_lvls = passives_lvls;
        data.rp_balance = (int) Double.parseDouble(Rp_balance.getValue().toString());
        for (String name : getAllResearches()) {
            JSpinner l = research_l.get(name);
            if (l != null) data.research_lvls.put(name, Double.parseDouble(l.getValue().toString()));
            JSpinner w = research_w.get(name);
            if (w != null) data.research_weight.put(name, Double.parseDouble(w.getValue().toString()));
        }
        for (String name : getAllBestiary()) {
            JSpinner l = bestiary.get(name);
            if (l != null) data.bestiary.put(name, Double.parseDouble(l.getValue().toString()));
        }
        for (int key : cores.keySet()) {
            CoreUI ui = cores.get(key);
            Core core = new Core(ui.name.getSelectedItem().toString(), ui.grade.getSelectedIndex(),
                    (int) Double.parseDouble(ui.lvl.getValue().toString()), ui.enabled);
            data.cores.put(key, core);
        }
        data.hard_hp = Double.parseDouble(Hard_hp.getValue().toString());
        data.hard_stats = Double.parseDouble(Hard_stats.getValue().toString());
        data.hard_reward = Double.parseDouble(Hard_reward.getValue().toString());
        data.highest_cl = Integer.parseInt(Highest_cl.getValue().toString());
        return data;
    }

    private void saveSkillLvls() {
        for (int i = 0; i < ActiveSkills.getRowCount(); i++) {
            actives_lvls.put(ActiveSkills.getValueAt(i, 0).toString(),
                    Double.parseDouble(ActiveSkills.getValueAt(i, 1).toString()) +
                            Double.parseDouble(ActiveSkills.getValueAt(i, 2).toString()) / 100);
        }
        for (int i = 0; i < PassiveSkills.getRowCount(); i++) {
            passives_lvls.put(PassiveSkills.getValueAt(i, 0).toString(),
                    Double.parseDouble(PassiveSkills.getValueAt(i, 1).toString()) +
                            Double.parseDouble(PassiveSkills.getValueAt(i, 2).toString()) / 100);
        }
    }

    private void migrateSetup(Setup data) {
        if (data.playerclass.equals("Priest")) {
            if (data.pskill1.equals("Buff Mastery")) data.pskill1 = "Bless Mastery";
            if (data.pskill2.equals("Buff Mastery")) data.pskill2 = "Bless Mastery";
            if (data.pskill3.equals("Buff Mastery")) data.pskill3 = "Bless Mastery";
            if (data.pskill4.equals("Buff Mastery")) data.pskill4 = "Bless Mastery";
        }
        if (data.playerclass.equals("Pyromancer")) {
            if (data.skill1.equals("Fireball")) data.skill1 = "Fire Ball";
            if (data.skill2.equals("Fireball")) data.skill2 = "Fire Ball";
            if (data.skill3.equals("Fireball")) data.skill3 = "Fire Ball";
            if (data.skill4.equals("Fireball")) data.skill4 = "Fire Ball";
        }
        if (data.passives_lvls.containsKey("Buff Mastery")) {
            data.passives_lvls.put("Bless Mastery", data.passives_lvls.get("Buff Mastery"));
        }
        if (data.actives_lvls.containsKey("Fireball")) {
            data.actives_lvls.put("Fire Ball", data.actives_lvls.get("Fireball"));
        }
        if (data.stats != null) {
            data.result_skills = data.stats + "\n" + data.result_skills;
            data.stats = null;
        }
        if (data.chest_name.equals("Windy Chest Piece")) data.chest_name = "Aim Chest Piece";
        if (data.pants_name.equals("Windy Pants")) data.pants_name = "Aim Pants";
        if (data.helmet_name.equals("Windy Hat")) data.helmet_name = "Aim Hat";
        if (data.bracer_name.equals("Windy Bracers")) data.bracer_name = "Aim Bracers";
        if (data.boots_name.equals("Windy Boots")) data.boots_name = "Aim Boots";
        if (data.crafting_lvl > 0 && data.passives_lvls.getOrDefault("Crafting", 0.0) == 0) {
            data.passives_lvls.put("Crafting", Double.valueOf(data.crafting_lvl));
            data.passives_lvls.put("Smithing", Double.valueOf(Math.min(data.crafting_lvl, data.alchemy_lvl)));
            data.passives_lvls.put("Alchemy", Double.valueOf(data.alchemy_lvl));
            data.crafting_lvl = 0;
            data.alchemy_lvl = 0;
        }
    }

    private Setup loadFile(String path) {
        Setup file = null;
        try {
            File def = new File(path);
            JsonReader reader = new JsonReader(new FileReader(def));
            file = gson.fromJson(reader, Setup.class);
            migrateSetup(file);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(SetupPanel, ex.getMessage(), "Exception",
                    JOptionPane.WARNING_MESSAGE);
        }
        return file;
    }

    private Settings loadSettings(String path) {
        Settings file = null;
        try {
            File def = new File(path);
            JsonReader reader = new JsonReader(new FileReader(def));
            file = gson.fromJson(reader, Settings.class);
        } catch (Exception ignored) {
            try {
                file = new Settings();
                JsonWriter writer = new JsonWriter(new FileWriter(path));
                gson.toJson(file, Settings.class, writer);
                writer.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(SetupPanel, ex.getMessage(), "Exception",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
        return file;
    }

    private void loadSetup(Setup data) {
        CL.setValue((int) data.cl);
        CL_p.setValue((data.cl - (int) data.cl) * 100);
        Enemy.setSelectedItem(data.zone);
        GameVersion.setSelectedItem((int) Double.parseDouble(data.gameversion));
        simulation.result_info = data.result_essential;
        simulation.skills_info = data.result_skills;
        simulation.lvling_info = data.result_lvling;
//        Balance1.setSelected(data.extra_atk_overkill);
//        Balance2.setSelected(data.extra_atk_backstab_mult);
//        Balance3.setSelected(data.crit_overkill_reduced);
        CraftingProgress.setSelected(data.crafting_progress);
        SmithingProgress.setSelected(data.smithing_progress);
        AlchemyProgress.setSelected(data.alchemy_progress);
        AlchemyConsumptionBased.setSelected(data.alchemy_consumption_based);
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
        Offline.setSelected(data.offline);
        EnemyMinLvlIncrease.setSelected(data.enemy_min_lvl_increase);
        Hard_hp.setValue(data.hard_hp);
        Hard_stats.setValue(data.hard_stats);
        Hard_reward.setValue(data.hard_reward);
        loadResearch(data);
        PlayerClass.setSelectedItem(data.playerclass);
        Pskill1.setSelectedItem(data.pskill1);
        Pskill2.setSelectedItem(data.pskill2);
        Pskill3.setSelectedItem(data.pskill3);
        Pskill4.setSelectedItem(data.pskill4);
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
        for (int key : cores.keySet()) {
            CoreUI ui = cores.get(key);
            if (data.cores != null && data.cores.get(key) != null) {
                Core core = data.cores.get(key);
                ui.name.setSelectedItem(core.name);
                ui.grade.setSelectedIndex(core.grade);
                ui.lvl.setValue(core.lvl);
                ui.enabled = core.enabled;
            } else {
                ui.name.setSelectedIndex(0);
                ui.grade.setSelectedIndex(6);
                ui.lvl.setValue(5);
            }
        }
        Pill.setSelectedItem(data.pill);
        Potion1.setSelectedItem(data.potion1);
        Potion1_t.setValue(data.potion1_t);
        Potion2.setSelectedItem(data.potion2);
        Potion2_t.setValue(data.potion2_t);
        Potion3.setSelectedItem(data.potion3);
        Potion3_t.setValue(data.potion3_t);

        MH_lvl.setValue(data.mh_lvl);
        MH_name.setSelectedItem(data.mh_name);
        MH_tier.setSelectedItem(data.mh_tier);
        OH_lvl.setValue(data.oh_lvl);
        OH_name.setSelectedItem(data.oh_name);
        OH_tier.setSelectedItem(data.oh_tier);
        Chest_lvl.setValue(data.chest_lvl);
        Chest_name.setSelectedItem(data.chest_name);
        Chest_tier.setSelectedItem(data.chest_tier);
        Pants_lvl.setValue(data.pants_lvl);
        Pants_name.setSelectedItem(data.pants_name);
        Pants_tier.setSelectedItem(data.pants_tier);
        Helmet_lvl.setValue(data.helmet_lvl);
        Helmet_name.setSelectedItem(data.helmet_name);
        Helmet_tier.setSelectedItem(data.helmet_tier);
        Bracer_lvl.setValue(data.bracer_lvl);
        Bracer_name.setSelectedItem(data.bracer_name);
        Bracer_tier.setSelectedItem(data.bracer_tier);
        Boots_lvl.setValue(data.boots_lvl);
        Boots_name.setSelectedItem(data.boots_name);
        Boots_tier.setSelectedItem(data.boots_tier);
        Accessory1_lvl.setValue(data.accessory1_lvl);
        Accessory1_name.setSelectedItem(data.accessory1_name);
        Accessory1_tier.setSelectedItem(data.accessory1_tier);
        Accessory2_lvl.setValue(data.accessory2_lvl);
        Accessory2_name.setSelectedItem(data.accessory2_name);
        Accessory2_tier.setSelectedItem(data.accessory2_tier);
        Necklace_lvl.setValue(data.necklace_lvl);
        Necklace_name.setSelectedItem(data.necklace_name);
        Necklace_tier.setSelectedItem(data.necklace_tier);
        SetSetup.setSelected(data.setsetup);
//        Stats.setText(data.stats);
        showResult();
        Result.setCaretPosition(0);
        Result_details.setCaretPosition(0);
        Result_lvling.setCaretPosition(0);
        updateUI();
    }

    private void loadResearch(Setup data) {
        Alchemist_CL.setValue(data.alchemist_lvl);
        Milestone.setValue(data.milestone);
        R_spd_bonus.setValue(data.r_spd_bonus);
        ML.setValue((int) data.ml);
        ML_p.setValue((data.ml - (int) data.ml) * 100);
        actives_lvls = cloneIfPresent(actives_lvls, data.actives_lvls);
        passives_lvls = cloneIfPresent(passives_lvls, data.passives_lvls);
        Highest_cl.setValue(data.highest_cl);
        loadSkillLvls();
        Rp_balance.setValue(data.rp_balance);
        for (String name : getAllResearches()) {
            JSpinner l = research_l.get(name);
            double def_value = name.equals("Research slot") ? 1.0 : 0.0;
            if (l != null) l.setValue(data.research_lvls.getOrDefault(name, def_value));
            JSpinner w = research_w.get(name);
            if (w != null) w.setValue(data.research_weight.getOrDefault(name, 0.0));
        }
        for (String name : getAllBestiary()) {
            JSpinner l = bestiary.get(name);
            if (l != null) l.setValue(data.bestiary.getOrDefault(name, 0.0));
        }
    }

    public HashMap<String, Double> cloneIfPresent(HashMap<String, Double> original, HashMap<String, Double> new_data) {
        HashMap<String, Double> result = new HashMap<>(64);
        result.putAll(original);
        for (Map.Entry<String, Double> mapEntry : new_data.entrySet()) {
            result.put(mapEntry.getKey(), mapEntry.getValue());
        }
        return result;
    }

    private void loadSkillLvls() {
        for (int i = 0; i < ActiveSkills.getRowCount(); i++) {
            String name = ActiveSkills.getValueAt(i, 0).toString();
            if (actives_lvls.containsKey(name)) {
                double lvl = actives_lvls.get(name);
                ActiveSkills.setValueAt((int) lvl, i, 1);
                ActiveSkills.setValueAt(df2.format((lvl - (int) lvl) * 100), i, 2);
            }
        }
        for (int i = 0; i < PassiveSkills.getRowCount(); i++) {
            String name = PassiveSkills.getValueAt(i, 0).toString();
            if (passives_lvls.containsKey(name)) {
                double lvl = passives_lvls.get(name);
                PassiveSkills.setValueAt((int) lvl, i, 1);
                PassiveSkills.setValueAt(df2.format((lvl - (int) lvl) * 100), i, 2);
            }
        }
    }

    private void showResult() {
        Result.setText(simulation.result_info);
        Result_details.setText(simulation.skills_info);
        Result_lvling.setText(simulation.lvling_info);
    }

    private JSpinner createCustomSpinner(double start, double min, double max, double step) {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(start, min, max, step));
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner, "#.##");
        DecimalFormat format = editor.getFormat();
        format.setDecimalFormatSymbols(dfs);
        spinner.setEditor(editor);
        return spinner;
    }

    private int addBestiary(String name, int x, int y) {
        JLabel label = new JLabel(name);
        gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 5);
        LeftPanel.add(label, gbc);
        JSpinner spinner_lvl = createCustomSpinner(0, 0, 1000000, 1.0);
        spinner_lvl.setName(name);
        bestiary.put(name, spinner_lvl);
        gbc = new GridBagConstraints();
        gbc.gridx = x + 1;
        gbc.gridy = y;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        LeftPanel.add(spinner_lvl, gbc);
        return y + 1;
    }

    private int addResearch(String name, int x, int y) {
        JLabel label = new JLabel(name);
        gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 5);
        LeftPanel.add(label, gbc);
        double start_value = 0;
        double min_value = 0;
        double max_value = maxResearchLvl(name);
        if (name.equals("Research slot")) {
            start_value = 1;
        }
        if (name.equals("Max CL")) {
            min_value = -30;
        }
        JSpinner spinner_lvl = createCustomSpinner(start_value, min_value, max_value, 1.0);
        spinner_lvl.setName(name);
        research_l.put(name, spinner_lvl);
        if (name.equals("Max CL")) spinner_lvl.setToolTipText("Also used as your max CL for training set bonus");
        gbc = new GridBagConstraints();
        gbc.gridx = x + 1;
        gbc.gridy = y;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        LeftPanel.add(spinner_lvl, gbc);
        min_value = -999;
        max_value = 999;
//        if (name.equals("Research slot")) {
//            min_value = -15;
//        }
        JSpinner spinner_w = createCustomSpinner(0, min_value, max_value, 1.0);
        spinner_w.setName(name + "_w");
        research_w.put(name, spinner_w);
        if (name.equals("Research slot")) {
            spinner_w.setToolTipText("+1 slot if current RP > hours(this) * additional cost. \n" +
                    "Negative numbers will prioritize this up to -weight lvl");
        }
        if (name.equals("Research speed")) spinner_w.setToolTipText("Won't be researched if rp < 1000");
        gbc = new GridBagConstraints();
        gbc.gridx = x + 2;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.WEST;
        LeftPanel.add(spinner_w, gbc);
        return y + 1;
    }

    private void addCore(int id, int x, int y) {
        CoreUI c = new CoreUI();
        String[] weapon_cores = {"None", "Slime", "Goblin", "Devil", "Lamia", "Fire Lizard", "Blood Lizard", "Asura"};
        String[] armor_cores = {"None", "Imp", "Ghoul", "Wraith", "Shinigami", "Astaroth", "Amon",
                "Shax", "Dagon", "Tyrant", "Fairy", "Raum", "Tree Golem", "Empress", "Dark Reaper"};
        String[] acc_cores = {"None", "Tengu", "Akuma", "Squirrel Mage", "Gloom Flower"};

        String[] types = {"None"};
        String tooltip = "Core type";
        if (id < 20) {
            types = weapon_cores;
            tooltip = "Weapon core type";
        } else {
            if (id < 70) {
                types = armor_cores;
                tooltip = "Armor core type";
            } else {
                types = acc_cores;
                tooltip = "Accessory core type";
            }
        }
        tooltip = switch ((int) (id / 10)) {
            case 0 -> "MH weapon core";
            case 1 -> "OH weapon core";
            case 2 -> "Chest core";
            case 3 -> "Pants core";
            case 4 -> "Helmet core";
            case 5 -> "Bracer core";
            case 6 -> "Boots core";
            case 7 -> "Accessory 1 core";
            case 8 -> "Accessory 2 core";
            case 9 -> "Necklace core";
            default -> "Unknown id";
        };
        JComboBox name = new JComboBox<>(types);
        c.name = name;
        name.setMaximumRowCount(32);
        name.setToolTipText(tooltip);
        gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.insets = new Insets(0, 5, 0, 0);
        EquipPanel.add(name, gbc);

        JComboBox grade = new JComboBox<>(MonsterStatData.CoreGrade.values());
        grade.setSelectedIndex(6);
        c.grade = grade;
        grade.setMaximumRowCount(12);
        grade.setToolTipText("Core grade");
        gbc = new GridBagConstraints();
        gbc.gridx = x + 1;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(grade, gbc);

        JSpinner lvl = new JSpinner(new SpinnerNumberModel(5, 1, 15, 1));
        c.lvl = lvl;
        grade.setToolTipText("Core lvl");
        gbc = new GridBagConstraints();
        gbc.gridx = x + 2;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        EquipPanel.add(lvl, gbc);

        cores.put(id, c);
    }

    public ArrayList<String> getAllBestiary() {
        return new ArrayList<>(Arrays.asList(
                "Slime", "Goblin", "Imp", "Ghoul", "Wraith",
                "Shinigami", "Astaroth", "Tengu", "Amon", "Akuma", "Devil", "Shax", "Dagon", "Lamia",
                "Tyrant", "Fairy", "Fire Lizard", "Blood Lizard", "Raum", "Asura", "Squirrel Mage",
                "Empress", "Tree Golem", "Gloom Flower", "Dark Reaper"
        ));
    }

    public ArrayList<String> getAllResearches() {
        return new ArrayList<>(Arrays.asList(
                "Research slot",
                "Research spd",
                "Core drop",
                "Core quality",
                "Drop rate",
                "Exp gain",
                "Max CL",
                "Max skill lvl",
                "Skill exp",
                "Min pow",
                "Enemy Min lvl",
                "Reduce CL req",
                "Crit chance",
                "Crit damage",
                "No overkill crit",
                "Crafting exp",
                "Crafting spd",
                "Smithing exp",
                "Smithing spd",
                "Alchemy exp",
                "Alchemy spd",
                "E. Quality min",
                "E. Quality mult",
                "Sidecraft spd",
                "Equip HP",
                "Equip Atk",
                "Equip Def",
                "Equip Int",
                "Equip Res",
                "Equip Hit",
                "Equip Spd",
                "Core removal cost",
                "God HP",
                "God Atk",
                "God Mystic",
                "God Pet",
                "God BS",
                "God CS",
                "God MS",
                "God SD",
                "God MV"
        ));
    }

    public static double maxResearchLvl(String name) {
        return switch(name) {
            case "Research slot" -> 15;
            case "Max skill lvl" -> 20;
            case "Reduce CL req" -> 20;
            case "Min pow" -> 100;
            case "Core quality" -> 800;
            case "Max CL" -> 80;
            case "Crit chance" -> 35;
            case "No overkill crit" -> 100;
            case "Sidecraft spd" -> 50;
            case "E. Quality min" -> 100;
            case "Core removal cost" -> 100;
            default -> 9999;
        };
    }

    private JSpinner createCustomSpinner(int start, int min, int max, int step) {
        return new JSpinner(new SpinnerNumberModel(start, min, max, step));
    }

    private JSpinner createLvlSpinner() {
        return createCustomSpinner(0, 0, 40, 1);
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
        if (show_setup == null) {
            show_setup = new Setup();
        }
        loadSetup(show_setup);
    }

    public void updateWeapons() {
        int slots = 0;
        int oh_slots = 1;
        if (MH_name.getSelectedItem() != null) {
            slots = 1;
            String item_name = MH_name.getSelectedItem().toString();
            if (MH_tier.getSelectedIndex() > 5) slots++;
            if (equipmentData.twohanded.contains(item_name)) {
                OH_name.setSelectedItem("None");
                slots *= 2;
            }
            if (equipmentData.less_slots.contains(item_name)) slots--;
            if (item_name.equals("Cauldron") || item_name.equals("Tsury Finke")) slots--;
            if (item_name.equals("None")) slots = 0;

            if (equipmentData.twohanded.contains(item_name)) {

            }
        }
        if (OH_name.getSelectedItem() != null) {
            String item_name = OH_name.getSelectedItem().toString();
            if (OH_tier.getSelectedIndex() > 5) oh_slots++;
            if (equipmentData.less_slots.contains(item_name)) oh_slots--;
            if (item_name.equals("None")) oh_slots = 0;
        }
        cores.get(0).enabled = slots >= 1;
        cores.get(1).enabled = slots >= 2;
        cores.get(10).enabled = slots >= 3 || oh_slots >= 1;
        cores.get(11).enabled = slots >= 4 || oh_slots >= 2;
        updateUI();
    }

    public void updateUI() {
        for (CoreUI core : cores.values()) {
            core.name.setVisible(core.enabled);
            core.grade.setVisible(core.enabled);
            core.lvl.setVisible(core.enabled);
        }
    }
}
