package ui;

import java.awt.Color;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CustomTable extends JTable{
private static final long serialVersionUID = 1L;
	
	public CustomTable() {
		
		setShowVerticalLines(true);
		setRowHeight(25);
		getTableHeader().setReorderingAllowed(false);
		getTableHeader().setBackground(new Color(255,250,210));
		setSelectionBackground(new Color(255 ,231, 186));
//		setGridColor(new Color(155,54,46));
		setAutoCreateRowSorter(false);
		setFont(UIConstant.NORMAL_FONT);
		

//		setRowHeight(25);
//		getTableHeader().setReorderingAllowed(false);
//		setSelectionBackground(new Color(0xA1A3A3));
//		setGridColor(new Color(0xA1A3A3));
//		setAutoCreateRowSorter(false);
//		setFont(UIConstant.NORMAL_FONT);
	}
	
	public CustomTable(DefaultTableModel tableModel) {
		this();
		setModel(tableModel);
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		// TODO Auto-generated method stub
		return false;
	}
}