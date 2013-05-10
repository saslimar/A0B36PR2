package pr2_kniha_jizd;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.JPanel;
import pr2_kniha_jizd.database.DbAccess;

public class TabelPanel extends JPanel {// panel z tabulkou

    private JTable table; //mýstní tabulka

    public TabelPanel(String prikaz) {// konstruktor jako vstupní promnená je databázový příkaz
        DbAccess k = new DbAccess();// volání třídy která vytáhne data z databáze
        k.DbRead(prikaz);
        show(k.getData(), k.getColum());// 
    }

    private void show(String[][] data, String[] colum) {// metoda která vezme data a zobrazý je do tabulky 
        table = new JTable(new TableModel(colum, data));
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
        table.setGridColor(Color.BLACK);
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        this.setLayout(new BorderLayout());
        this.add(table.getTableHeader(), BorderLayout.PAGE_START);
        this.add(table, BorderLayout.CENTER);
    }

    public JTable getTable() {/// getr tabulky
        return table;
    }
}