package pr2_kniha_jizd;

import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.table.AbstractTableModel;

public class TableModel  extends AbstractTableModel {

    private ArrayList<String[]> data = new ArrayList<String[]>();
    private String[] colum;

    public TableModel(String[] colum,String[][] data) {
        this.colum = colum;
        if(data != null){
            this.data.addAll(Arrays.asList(data));
        }
    }    
    public boolean addContact(String[] k) {
        return data.add(k);
    }

    public boolean delContact(String[] k) {
        return data.remove(k);
    }

    @Override
    public String getColumnName(int column) {
        return colum[column];
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return colum.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }

}