package honours_project;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ColourCellRenderer extends DefaultTableCellRenderer {
	
	private int[][] penalties;
	
	public ColourCellRenderer(int[][] penaltiesTable) {
		this.penalties = penaltiesTable;
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

		//Cells are by default rendered as a JLabel.
	    JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
//	    int v = (penalties[row][col] == 0) ? 1 : penalties[row][col]; 
//	    l.setBackground(new Color(row * col / 2, row * col/ 2, row * col / 2));
//	    if (col % 2 == 0)
//	    	l.setBackground(Color.RED);
//	    else
//	    	l.setBackground(Color.BLUE);
//	    //Get the status for the current row.
//	    DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
//	    if (tableModel.getStatus(row) == DefaultTableModel.APPROVED) {
//	      l.setBackground(Color.GREEN);
//	    } else {
//	      l.setBackground(Color.RED);
//	    }

	    //Return the JLabel which renders the cell.
	    return l;
	}
}
