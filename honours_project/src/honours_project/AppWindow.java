package honours_project;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class AppWindow extends JFrame implements Observer {
	private Evolution evolution;
	private Language language = new EnglishLanguage();
	
	private JPanel contentPane;
	private JTable table;
	private JPanel panel;
	private JPanel leftPanel;
	private JLabel constraintLabel;
	private JTextField constraintTextField;
	private JLabel runsLabel;
	private JTextField runsTextField;
	private JButton startButton;
	private DefaultTableModel timetableModel;
	private JPanel middlePanel;
	private JPanel panel_3;
	private JPanel panel_4;
	private JPanel panel_5;
	private JPanel panel_6;
	private JTextField loadFileTextField;
	private JButton loadFileButton;
	private JLabel missedEventsLabel;
	private JLabel missedEventsTxt;
	private JLabel fitnessLabel;
	private JLabel fitnessTxt;
	private JLabel singleLabel;
	private JLabel singleTxt;
	private JLabel threeLabel;
	private JLabel threeTxt;
	private JLabel endLabel;
	private JLabel endTxt;
	private JButton saveButton;
	private JPanel rightPanel;
	private JLabel logoLabel;

	/**
	 * Create the frame.
	 */
	public AppWindow(Evolution evolution) {
		this.evolution = evolution;
		this.setTitle(language.getTitle());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{1400, 0};
		gbl_contentPane.rowHeights = new int[]{185, 185, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		pack();
		
		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		contentPane.add(panel, gbc_panel);
		panel.setLayout(new GridLayout(0, 3, 0, 0));
		
		leftPanel = new JPanel();
		panel.add(leftPanel);
		leftPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		panel_3 = new JPanel();
		panel_3.setBorder(new EmptyBorder(10, 10, 10, 10));
		leftPanel.add(panel_3);
		panel_3.setLayout(new GridLayout(0, 1, 0, 0));
		
		loadFileTextField = new JTextField();
		panel_3.add(loadFileTextField);
		loadFileTextField.setColumns(10);
		
		loadFileButton = new JButton(language.getLoadFile());
		loadFileButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser fileLoader = new JFileChooser();
				int rVal = fileLoader.showOpenDialog(AppWindow.this);
				
				if (rVal == JFileChooser.APPROVE_OPTION) {
					loadFileTextField.setText(fileLoader.getCurrentDirectory() + "/" + fileLoader.getSelectedFile().getName());
				} else if (rVal == JFileChooser.CANCEL_OPTION) {
					loadFileTextField.setText("");
				}
			}
		});
		panel_3.add(loadFileButton);
		
		panel_5 = new JPanel();
		panel_5.setBorder(new EmptyBorder(10, 10, 10, 10));
		leftPanel.add(panel_5);
		panel_5.setLayout(new GridLayout(1, 0, 0, 0));
		
		saveButton = new JButton(language.getSave());
		panel_5.add(saveButton);
		saveButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (evolution.bestExists()) {
					JFileChooser saveFile = new JFileChooser();
					int rVal = saveFile.showSaveDialog(AppWindow.this);
					
					if (rVal == JFileChooser.APPROVE_OPTION) {
						String filename = saveFile.getSelectedFile().getName();
						String dir = saveFile.getCurrentDirectory().toString();
						evolution.saveSolution(dir + "/" + filename);
					}
				} else {
					JOptionPane.showMessageDialog(AppWindow.this, language.getBestError(), language.getErrorTitle(), JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		panel_4 = new JPanel();
		panel_4.setBorder(new EmptyBorder(10, 10, 10, 10));
		leftPanel.add(panel_4);
		panel_4.setLayout(new GridLayout(0, 2, 0, 0));
		
		constraintLabel = new JLabel(language.getRun());
		panel_4.add(constraintLabel);
		
		constraintTextField = new JTextField();
		panel_4.add(constraintTextField);
		constraintTextField.setColumns(10);
		
		runsLabel = new JLabel(language.getRuns());
		panel_4.add(runsLabel);
		
		runsTextField = new JTextField();
		panel_4.add(runsTextField);
		runsTextField.setColumns(10);
		
		panel_6 = new JPanel();
		panel_6.setBorder(new EmptyBorder(10, 10, 10, 10));
		leftPanel.add(panel_6);
		panel_6.setLayout(new GridLayout(0, 1, 0, 0));
		
		startButton = new JButton(language.getRun());
		panel_6.add(startButton);
		startButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String filename = loadFileTextField.getText();
				
				if (filename.length() > 0) {
					evolution.setFile(filename);
					
					if (startButton.getText() == language.getRun()) {
						startButton.setText(language.getStop());
						Thread evolve = new Thread(evolution);
						evolve.start();
					} else if (startButton.getText() == language.getStop()) {
						startButton.setText(language.getRun());
						evolution.stopEvolution();
					}
				} else {
					JOptionPane.showMessageDialog(AppWindow.this, language.getNoFileError(), language.getErrorTitle(), JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		middlePanel = new JPanel();
		middlePanel.setBorder(new EmptyBorder(0, 10, 0, 0));
		panel.add(middlePanel);
		middlePanel.setLayout(new GridLayout(0, 4, 0, 0));
		
		missedEventsLabel = new JLabel(language.getMissedEvents() + ":");
		middlePanel.add(missedEventsLabel);
		
		missedEventsTxt = new JLabel("0");
		middlePanel.add(missedEventsTxt);
		
		fitnessLabel = new JLabel(language.getFitness() + ":");
		middlePanel.add(fitnessLabel);
		
		fitnessTxt = new JLabel("0");
		middlePanel.add(fitnessTxt);
		
		singleLabel = new JLabel(language.getSingle() + ":");
		middlePanel.add(singleLabel);
		
		singleTxt = new JLabel("0");
		middlePanel.add(singleTxt);
		
		threeLabel = new JLabel(language.getThree() + ":");
		middlePanel.add(threeLabel);
		
		threeTxt = new JLabel("0");
		middlePanel.add(threeTxt);
		
		endLabel = new JLabel(language.getEnd() + ":");
		middlePanel.add(endLabel);
		
		endTxt = new JLabel("0");
		middlePanel.add(endTxt);
		
		rightPanel = new JPanel();
		rightPanel.setBorder(new EmptyBorder(0, 100, 0, 0));
		panel.add(rightPanel);
		rightPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		logoLabel = new JLabel();
		logoLabel.setIcon(new ImageIcon("src/logo.png"));
		rightPanel.add(logoLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(1200, 185));
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.anchor = GridBagConstraints.NORTH;
		gbc_scrollPane.fill = GridBagConstraints.HORIZONTAL;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		contentPane.add(scrollPane, gbc_scrollPane);
		table = new JTable();
		scrollPane.setViewportView(table);
		timetableModel = new DefaultTableModel(
				new Object[][] {
					{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
					{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
					{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
					{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
					{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
					{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
					{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
					{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
					{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
					{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				},
				new String[] {
					"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45"
				}
			) {
				boolean[] columnEditables = new boolean[] {
					false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			};
		table.setModel(timetableModel);
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(2).setResizable(false);
		table.getColumnModel().getColumn(3).setResizable(false);
		table.getColumnModel().getColumn(4).setResizable(false);
		table.getColumnModel().getColumn(5).setResizable(false);
		table.getColumnModel().getColumn(6).setResizable(false);
		table.getColumnModel().getColumn(7).setResizable(false);
		table.getColumnModel().getColumn(8).setResizable(false);
		table.getColumnModel().getColumn(9).setResizable(false);
		table.getColumnModel().getColumn(10).setResizable(false);
		table.getColumnModel().getColumn(11).setResizable(false);
		table.getColumnModel().getColumn(12).setResizable(false);
		table.getColumnModel().getColumn(13).setResizable(false);
		table.getColumnModel().getColumn(14).setResizable(false);
		table.getColumnModel().getColumn(15).setResizable(false);
		table.getColumnModel().getColumn(16).setResizable(false);
		table.getColumnModel().getColumn(17).setResizable(false);
		table.getColumnModel().getColumn(18).setResizable(false);
		table.getColumnModel().getColumn(19).setResizable(false);
		table.getColumnModel().getColumn(20).setResizable(false);
		table.getColumnModel().getColumn(21).setResizable(false);
		table.getColumnModel().getColumn(22).setResizable(false);
		table.getColumnModel().getColumn(23).setResizable(false);
		table.getColumnModel().getColumn(24).setResizable(false);
		table.getColumnModel().getColumn(25).setResizable(false);
		table.getColumnModel().getColumn(26).setResizable(false);
		table.getColumnModel().getColumn(27).setResizable(false);
		table.getColumnModel().getColumn(28).setResizable(false);
		table.getColumnModel().getColumn(29).setResizable(false);
		table.getColumnModel().getColumn(30).setResizable(false);
		table.getColumnModel().getColumn(31).setResizable(false);
		table.getColumnModel().getColumn(32).setResizable(false);
		table.getColumnModel().getColumn(33).setResizable(false);
		table.getColumnModel().getColumn(34).setResizable(false);
		table.getColumnModel().getColumn(35).setResizable(false);
		table.getColumnModel().getColumn(36).setResizable(false);
		table.getColumnModel().getColumn(37).setResizable(false);
		table.getColumnModel().getColumn(38).setResizable(false);
		table.getColumnModel().getColumn(39).setResizable(false);
		table.getColumnModel().getColumn(40).setResizable(false);
		table.getColumnModel().getColumn(41).setResizable(false);
		table.getColumnModel().getColumn(42).setResizable(false);
		table.getColumnModel().getColumn(43).setResizable(false);
		table.getColumnModel().getColumn(44).setResizable(false);
		
		
	}

	public void showWindow() {
		this.pack();
		this.setVisible(true);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof Individual) {
			Individual best = (Individual) arg;
			updateTimetable(best.getRooms());
			updateLabels(best);
		}
	}
	
	private void updateTimetable(List<Room> timetable) {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 45; j++) {
				Room room = (Room) timetable.get(i);
				Event event = room.getSlot(j);
				if (event != null) {
					timetableModel.setValueAt(event.getId(), i, j);
				} else {
					timetableModel.setValueAt(null, i, j);
				}
			}
		}
	}
	
	private void updateLabels(Individual best) {
		missedEventsTxt.setText(Integer.toString(best.unplacedEventsNumber()));
		fitnessTxt.setText(Integer.toString(best.getFitness()));
		singleTxt.setText(Integer.toString(best.getSingle()));
		threeTxt.setText(Integer.toString(best.getThree()));
		endTxt.setText(Integer.toString(best.getEnd()));
	}
}
