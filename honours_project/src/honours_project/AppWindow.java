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

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class AppWindow extends JFrame implements Observer {
	private Evolution evolution;
	
	private JPanel contentPane;
	private JTable table;
	private JPanel panel;
	private JPanel panel_1;
	private JPanel panel_2;
	private JCheckBox updateCheckBox;
	private JCheckBox timeCheckBox;
	private JLabel constraintLabel;
	private JTextField constraintTextField;
	private JLabel runsLabel;
	private JTextField runsTextField;
	private JButton startButton;
	private DefaultTableModel timetableModel;

	/**
	 * Create the frame.
	 */
	public AppWindow(Evolution evolution) {
		this.evolution = evolution; 
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
		
		panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setLayout(new GridLayout(0, 2, 0, 0));
		
		updateCheckBox = new JCheckBox("No Update");
		panel_1.add(updateCheckBox);
		
		timeCheckBox = new JCheckBox("Time Limit");
		panel_1.add(timeCheckBox);
		
		constraintLabel = new JLabel("Time/Update");
		panel_1.add(constraintLabel);
		
		constraintTextField = new JTextField();
		panel_1.add(constraintTextField);
		constraintTextField.setColumns(10);
		
		runsLabel = new JLabel("Runs");
		panel_1.add(runsLabel);
		
		runsTextField = new JTextField();
		panel_1.add(runsTextField);
		runsTextField.setColumns(10);
		
		startButton = new JButton("Run");
		startButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Thread t = new Thread(evolution);
				t.start();
			}
		});
		panel_1.add(startButton);
		
		panel_2 = new JPanel();
		panel.add(panel_2);
		
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
//			constraintLabel.setText(Integer.toString(best.getFitness()));
			updateTimetable(best.getRooms());
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
}
