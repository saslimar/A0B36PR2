package pr2_kniha_jizd;

import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.table.AbstractTableModel;

public class TableModel  extends AbstractTableModel {// třída dědící z AbstractTableModel potřebná pro správné zobrazení dat v tabulce

    private ArrayList<String[]> data = new ArrayList<String[]>();// data později vypsaná v tabulce
    private String[] colum;// popisky dat (jednotlivých sloupců)

    public TableModel(String[] colum,String[][] data) { // konstruktor se vstupnímy daty
        this.colum = colum;
        if(data != null){
            this.data.addAll(Arrays.asList(data));//pokud nejsou data prázdná uložím je
        }
    }    
    public boolean addContact(String[] k) {    //pridat zaznam do tabulky
        return data.add(k);
    }
    public boolean delContact(String[] k) {// odebrat záznam
        return data.remove(k);
    }
    // getry setry 
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