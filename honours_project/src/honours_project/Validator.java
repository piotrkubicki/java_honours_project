package honours_project;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;

public class Validator {

	public enum Rules { INTEGER, DOUBLE, REQUIRED, REQUIRED_WITH };
	
	private Language language;
	private JFrame frame;
	
	public Validator(JFrame frame, Language language) {
		this.frame = frame;
		this.language = language;
	};
	
	public boolean validate(Hashtable<List<JTextField>, List<Rules>> input) {
		Hashtable<List<JTextField>, List<Rules>> result = check(input);
		clearErrors(input.keySet());
		showErrors(result);

		if (result.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	
	private void showErrors(Hashtable<List<JTextField>, List<Rules>> result) {
		Iterator iter = result.entrySet().iterator();
		
		while (iter.hasNext()) {
			Map.Entry pair = (Map.Entry) iter.next();
			
			List<Rules> rules = (List<Rules>) pair.getValue();
			
			for (Rules rule : rules) {
				List<JTextField> key = (List<JTextField>) pair.getKey();
				
				if (rule == Rules.INTEGER) {
					showError(key.get(0));
					JOptionPane.showMessageDialog(frame, language.getNoNumberError(), language.getErrorTitle(), JOptionPane.ERROR_MESSAGE);
				} else if (rule == Rules.DOUBLE) {
					showError(key.get(0));
					JOptionPane.showMessageDialog(frame, language.getNoNumberError(), language.getErrorTitle(), JOptionPane.ERROR_MESSAGE);
				} else if (rule == Rules.REQUIRED) {
					showError(key.get(0));
					JOptionPane.showMessageDialog(frame, language.getRequiredError(), language.getErrorTitle(), JOptionPane.ERROR_MESSAGE);
				} else if (rule == Rules.REQUIRED_WITH) {
					for (int i = 1; i < key.size(); i++)
						showError(key.get(i));
				}
			}
		}
	}
	
	private void clearErrors(Set<List<JTextField>> set) {
		
		for (List<JTextField> fields : set) {
			for (JTextField field : fields) {
				SwingUtilities.updateComponentTreeUI(field);
			}
		}
	}
	
	private void showError(JTextField field) {
		Border redBorder = new BorderUIResource(BorderFactory.createLineBorder(Color.RED));
		field.setBorder(redBorder);
	}
	
	private Hashtable<List<JTextField>, List<Rules>> check(Hashtable<List<JTextField>, List<Rules>> input) {
		Hashtable<List<JTextField>, List<Rules>> result = new Hashtable<List<JTextField>, List<Rules>>();
		Iterator iter = input.entrySet().iterator();
		
		while (iter.hasNext()) {
			List<Rules> subResults = new ArrayList<Rules>();
			Map.Entry pair = (Map.Entry) iter.next();
			
			List<Rules> rules = (List<Rules>) pair.getValue();
			
			for (Rules rule : rules) {
				List<JTextField> key = (List<JTextField>) pair.getKey();
				
				if (rule == Rules.INTEGER) {
					if (!isInt(key)) {
						subResults.add(Rules.INTEGER);
					}
				} else if (rule == Rules.DOUBLE) {
					if (!isDouble(key)) {
						subResults.add(Rules.DOUBLE);
					}
				} else if (rule == Rules.REQUIRED) {
					if (!isRequired(key)) {
						subResults.add(Rules.REQUIRED);
					}
				} else if (rule == Rules.REQUIRED_WITH) {
					if (!requiredWith(key)) {
						subResults.add(Rules.REQUIRED_WITH);
					}
				}
				
				if (!subResults.isEmpty()) {
					result.put(key, subResults);
				}
			}
		}
		
		return result;
	}
	
	private boolean isInt(List<JTextField> input) {
		String string = input.get(0).getText();
			
		if (string.equals("")) {
			return true;
		}
		
		try {
			Integer.parseInt(string);
			return true;
		} catch (NumberFormatException ec) {
			return false;
		}
	}
	
	private boolean isDouble(List<JTextField> input) {
		String string = input.get(0).getText();
		
		try {
			Boolean.parseBoolean(string);
			return true;
		} catch (NumberFormatException ec) {
			return false;
		}
	}
	
	private boolean isRequired(List<JTextField> input) {
		String string = input.get(0).getText();
		
		if (string.equals("")) {
			return false;
		} else {
			return true;
		}
	}
	
	private boolean requiredWith(List<JTextField> input) {
		String string = input.get(0).getText();
	
		if (string.equals("")) {
			return true;
		}
		
		for (int i = 1; i < input.size(); i++) {
			if (input.get(i).getText().equals("")) {
				return false;
			}
		}
		
		return true;
	}
}
