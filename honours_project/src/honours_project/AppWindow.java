package honours_project;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.text.SimpleDateFormat;
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
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import honours_project.Evolution.State;

public class AppWindow extends JFrame implements Observer {
	private Evolution evolution;
	private Language language = new EnglishLanguage();
	
	String filename;
	long runTime;

	private XYSeriesCollection dataset;
	private int seriesCounter;
	
	private Timer timer = new Timer(1000, new TimerActionListener());;
	private long elapsedTime;
	
	ChartPanel chartPanel;
	
	private JPanel contentPane;
	private JTable eventsTable;
	private JPanel panel;
	private JPanel leftPanel;
	private JLabel runTimeLabel;
	private JTextField runTimeTextField;
	private JLabel runsLabel;
	private JTextField runsTextField;
	private JButton startButton;
	private DefaultTableModel timetableModel;
	private DefaultTableModel studentsTimetableModel;
	private JPanel innerPanel;
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
	private JPanel middlePanel;
	private JLabel populationSizeLabel;
	private JTextField populationSizeTextField;
	private JLabel generationLabel;
	private JLabel generationTxt;
	private JPanel bottomPanel;
	private JPanel panel_1;
	private JPanel rightPanel;
	private JLabel logoLabel;
	private JScrollPane scrollPane_1;
	private JTable studentsTable;
	private JPanel panel_2;
	private JPanel panel_7;
	private JPanel panel_8;
	private JPanel panel_9;
	private JButton clearButton;
	private JLabel runNumberLabel;
	private JLabel runNumberTxt;
	private JLabel runElapsedTimeLabel;
	private JLabel runElapsedTimeTxt;

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
		pack();
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));
		
		panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(new GridLayout(0, 3, 0, 0));
		
		leftPanel = new JPanel();
		panel.add(leftPanel);
		leftPanel.setLayout(new GridLayout(2, 1, 0, 0));
		
		panel_3 = new JPanel();
		panel_3.setBorder(new EmptyBorder(10, 10, 10, 10));
		leftPanel.add(panel_3);
		panel_3.setLayout(new GridLayout(0, 2, 0, 0));
		
		panel_1 = new JPanel();
		panel_1.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel_3.add(panel_1);
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		loadFileTextField = new JTextField();
		panel_1.add(loadFileTextField);
		loadFileTextField.setColumns(10);
		
		loadFileButton = new JButton(language.getLoadFile());
		panel_1.add(loadFileButton);
		loadFileButton.addMouseListener(new LoadButtonMouseAdapter());
		
		panel_5 = new JPanel();
		panel_3.add(panel_5);
		panel_5.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel_5.setLayout(new GridLayout(0, 1, 0, 0));
		
		saveButton = new JButton(language.getSave());
		panel_5.add(saveButton);
		
		clearButton = new JButton(language.getClear());
		clearButton.addMouseListener(new ClearButtonMouseAdapter());
		panel_5.add(clearButton);
		
		panel_4 = new JPanel();
		panel_3.add(panel_4);
		panel_4.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel_4.setLayout(new GridLayout(0, 2, 0, 0));
		
		runTimeLabel = new JLabel(language.getRunTime() + " (s):");
		panel_4.add(runTimeLabel);
		
		runTimeTextField = new JTextField();
		panel_4.add(runTimeTextField);
		runTimeTextField.setColumns(10);
		
		runsLabel = new JLabel(language.getRuns() + ":");
		panel_4.add(runsLabel);
		
		runsTextField = new JTextField();
		panel_4.add(runsTextField);
		runsTextField.setColumns(10);
		
		populationSizeLabel = new JLabel(language.getPopulationSize() + ":");
		panel_4.add(populationSizeLabel);
		
		populationSizeTextField = new JTextField();
		panel_4.add(populationSizeTextField);
		populationSizeTextField.setColumns(10);
		
		panel_6 = new JPanel();
		panel_3.add(panel_6);
		panel_6.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel_6.setLayout(new GridLayout(0, 1, 0, 0));
		
		startButton = new JButton(language.getRun());
		panel_6.add(startButton);
		
		innerPanel = new JPanel();
		leftPanel.add(innerPanel);
		innerPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
		innerPanel.setLayout(new GridLayout(0, 4, 0, 0));
		
		runNumberLabel = new JLabel(language.getRun());
		innerPanel.add(runNumberLabel);
		
		runNumberTxt = new JLabel("0");
		innerPanel.add(runNumberTxt);
		
		runElapsedTimeLabel = new JLabel(language.getElapsedTime());
		innerPanel.add(runElapsedTimeLabel);
		
		runElapsedTimeTxt = new JLabel("00:00");
		innerPanel.add(runElapsedTimeTxt);
		
		missedEventsLabel = new JLabel(language.getMissedEvents() + ":");
		innerPanel.add(missedEventsLabel);
		
		missedEventsTxt = new JLabel("0");
		innerPanel.add(missedEventsTxt);
		
		fitnessLabel = new JLabel(language.getFitness() + ":");
		innerPanel.add(fitnessLabel);
		
		fitnessTxt = new JLabel("0");
		innerPanel.add(fitnessTxt);
		
		singleLabel = new JLabel(language.getSingle() + ":");
		innerPanel.add(singleLabel);
		
		singleTxt = new JLabel("0");
		innerPanel.add(singleTxt);
		
		threeLabel = new JLabel(language.getThree() + ":");
		innerPanel.add(threeLabel);
		
		threeTxt = new JLabel("0");
		innerPanel.add(threeTxt);
		
		endLabel = new JLabel(language.getEnd() + ":");
		innerPanel.add(endLabel);
		
		endTxt = new JLabel("0");
		innerPanel.add(endTxt);
		
		generationLabel = new JLabel(language.getGeneration() + ":");
		innerPanel.add(generationLabel);
		
		generationTxt = new JLabel("0");
		innerPanel.add(generationTxt);
		
		startButton.addMouseListener(new StartBtnMouseAdapter());
		saveButton.addMouseListener(new SaveButtonMouseAdapter());
		
		middlePanel = new JPanel();
		middlePanel.setBorder(new EmptyBorder(0, 100, 0, 0));
		panel.add(middlePanel);
		middlePanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		bottomPanel = new JPanel();
		bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.add(bottomPanel);
		bottomPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		panel_8 = new JPanel();
		panel_8.setBorder(new EmptyBorder(20, 0, 26, 0));
		bottomPanel.add(panel_8);
		panel_8.setLayout(new GridLayout(0, 1, 0, 0));
		
		panel_2 = new JPanel();
		panel_8.add(panel_2);
		panel_2.setBorder(new TitledBorder(null, language.getEvents(), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setLayout(new GridLayout(0, 1, 0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_2.add(scrollPane);
		scrollPane.setPreferredSize(new Dimension(1200, 182));
		eventsTable = new JTable();
		scrollPane.setViewportView(eventsTable);
		
		panel_9 = new JPanel();
		panel_9.setBorder(new EmptyBorder(20, 0, 26, 0));
		bottomPanel.add(panel_9);
		panel_9.setLayout(new GridLayout(0, 1, 0, 0));
		
		panel_7 = new JPanel();
		panel_9.add(panel_7);
		panel_7.setBorder(new TitledBorder(null, language.getEvents(), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_7.setLayout(new GridLayout(0, 1, 0, 0));
		
		scrollPane_1 = new JScrollPane();
		panel_7.add(scrollPane_1);
		
		studentsTable = new JTable();
		scrollPane_1.add(studentsTable);
		scrollPane_1.setViewportView(studentsTable);
		
		// GRAPH
		chartPanel = new ChartPanel(null);
		chartPanel.setChart(createChart());
		
		middlePanel.add(chartPanel);
		
		rightPanel = new JPanel();
		rightPanel.setBorder(new EmptyBorder(0, 130, 0, 0));
		panel.add(rightPanel);
		rightPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		logoLabel = new JLabel();
		URL logoURL = getClass().getResource("/logo.png");
		logoLabel.setIcon(new ImageIcon(logoURL));
		rightPanel.add(logoLabel);
	}
	
	private void addSeries() {
		XYSeries s = new XYSeries(language.getFeasibility() + seriesCounter);
		dataset.addSeries(s);
		seriesCounter++;
	}
	
	private void clearData() {
		clearLabels();
		clearChart();
	}
	
	private void clearLabels() {
		generationTxt.setText("0");
		missedEventsTxt.setText("0");
		singleTxt.setText("0");
		fitnessTxt.setText("0");
		threeTxt.setText("0");
		endTxt.setText("0");
		runNumberTxt.setText("0");
		runElapsedTimeTxt.setText("00:00");
	}
	
	private void clearChart() {
		dataset.removeAllSeries();
		seriesCounter = 0;
	}

	private JFreeChart createChart() {
		dataset = new XYSeriesCollection();
		JFreeChart chart = ChartFactory.createXYLineChart(language.getFeasibility(), language.getGeneration(), language.getFeasibility(), dataset);
		XYPlot plot = chart.getXYPlot();
		NumberAxis xAxis = new NumberAxis();
		plot.setDomainAxis(xAxis);
		
		chart.setBackgroundPaint(null);
		
		return chart;
	}

	public void showWindow() {
		this.pack();
		this.setVisible(true);
	}
	
	private void initialize() {
		startButton.setEnabled(false);
		clearButton.setEnabled(false);
		runNumberTxt.setText(Integer.toString(seriesCounter + 1));
	}
	
	private void starting() {
		elapsedTime = 0;
		startTime();
		timetableModel = (DefaultTableModel) createTable();
		eventsTable.setModel(timetableModel);
		
		studentsTimetableModel = (DefaultTableModel) createTable();
		studentsTable.setModel(studentsTimetableModel);
		addSeries();
	}

	private void running() {
		startButton.setText(language.getStop());
		startButton.setEnabled(true);
	}
	
	private void finishing() {
		startButton.setEnabled(false);
	}
	
	private void stoping() {
		startButton.setText(language.getRun());
		startButton.setEnabled(true);
		clearButton.setEnabled(true);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (Evolution.state == State.INITIALIZE) {
			initialize();
		} else if (Evolution.state == State.STARTING) {
			starting();
		} else if (Evolution.state == State.RUNNING) {
			running();
		} else if (Evolution.state == State.FINISHING) { 
			finishing();
		} else if (Evolution.state == State.STOP) {
			stoping();
		}
		
		if (arg instanceof Individual) {
			Individual best = (Individual) arg;
			updateTimetables(best.getRooms());
			updateLabels(best);
			dataset.getSeries(seriesCounter-1).add(evolution.getGeneration(), best.getFitness());
		}
	}
	
	private void updateTimetables(List<Room> timetable) {
		
		for (int i = 0; i < Evolution.ROOMS_NUMBER; i++) {
			for (int j = 0; j < Evolution.SLOTS_NUMBER; j++) {
				Room room = (Room) timetable.get(i);
				Event event = room.getSlot(j);
				if (event != null) {
					timetableModel.setValueAt(event.getId(), i, j);
					studentsTimetableModel.setValueAt(event.getStudents().size(), i, j);
				} else {
					timetableModel.setValueAt(null, i, j);
					studentsTimetableModel.setValueAt(null, i, j);
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
		generationTxt.setText(Integer.toString(evolution.getGeneration()));
	}
	
	private DefaultTableModel createTable() {
		String[] headers = new String[] {
				"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45"
			};
		
		DefaultTableModel table = new DefaultTableModel(new Object[Evolution.ROOMS_NUMBER][Evolution.EVENTS_NUMBER], headers) {
			boolean[] columnEditables = new boolean[Evolution.EVENTS_NUMBER];
			
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		
		return table;
	}
	
	private void startTime() {
		timer.start();
	}
	
	private class SaveButtonMouseAdapter extends MouseAdapter {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if (saveButton.isEnabled()) {
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
		}
	}
	
	private class LoadButtonMouseAdapter extends MouseAdapter {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if (loadFileButton.isEnabled()) {
				JFileChooser fileLoader = new JFileChooser();
				int rVal = fileLoader.showOpenDialog(AppWindow.this);
				
				if (rVal == JFileChooser.APPROVE_OPTION) {
					loadFileTextField.setText(fileLoader.getCurrentDirectory() + "/" + fileLoader.getSelectedFile().getName());
				} else if (rVal == JFileChooser.CANCEL_OPTION) {
					loadFileTextField.setText("");
				}
			}
		}
	}
	
	private class ClearButtonMouseAdapter extends MouseAdapter {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if (clearButton.isEnabled()) {
				clearData();
			}
		}
	}
	
	private boolean validateInput() {
		filename = loadFileTextField.getText();
		
		String runTime = runTimeTextField.getText();
		String runsNumber = runsTextField.getText();
		
		if (!runTime.equals("") && runsNumber.equals("") || runTime.equals("") && runsNumber.equals("")) {
			evolution.setRunsNumber(1);
		}
		
		if (!runTime.equals("") && !runsNumber.equals("")) {
			try {
				evolution.setRunsNumber(Integer.parseInt(runsNumber));
			} catch (NumberFormatException ec) {
				JOptionPane.showMessageDialog(AppWindow.this, language.getNoNumberError(), language.getErrorTitle(), JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		
		if (!runTime.equals("")) {
			try {
				this.runTime = Integer.parseInt(runTime) * 1000;
				evolution.setRunTime(this.runTime);
			} catch (NumberFormatException ec) {
				JOptionPane.showMessageDialog(AppWindow.this, language.getNoNumberError(), language.getErrorTitle(), JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		
		
		try {
			evolution.setPopulatinoSize(Integer.parseInt(populationSizeTextField.getText()));
		} catch (NumberFormatException ec) {
			JOptionPane.showMessageDialog(AppWindow.this, language.getNoNumberError(), language.getErrorTitle(), JOptionPane.ERROR_MESSAGE);
			return false;
		}	
		
		if (filename.length() < 1) {
			JOptionPane.showMessageDialog(AppWindow.this, language.getNoFileError(), language.getErrorTitle(), JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		return true;
	}
	
	private class StartBtnMouseAdapter extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (startButton.isEnabled()) {
				if (validateInput()) {
					if (startButton.getText() == language.getRun()) {
						evolution.setFile(filename);
						Thread evolve = new Thread(evolution);
						evolve.start();
					} else if (startButton.getText() == language.getStop()) {
						evolution.setRunsNumber(0);
						evolution.stopEvolution();
					}
				}
			}
		}
	}
	
	private class TimerActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			elapsedTime += 1000;
			SimpleDateFormat df = new SimpleDateFormat("mm:ss");
			runElapsedTimeTxt.setText(df.format(elapsedTime));
			
			if (Evolution.state == State.STOP) {
				timer.stop();
			}
		}
	};
}
