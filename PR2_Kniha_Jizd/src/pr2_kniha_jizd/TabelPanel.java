package pr2_kniha_jizd;

import pr2_kniha_jizd.database.DbRead;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.JPanel;

public class TabelPanel extends JPanel {

    private JTable table;

    public TabelPanel(String prikaz) {
        DbRead k = new DbRead(prikaz);
        show(k.getData(), k.getColum());
    }

    private void show(String[][] data, String[] colum) {
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

    public JTable getTable() {
        return table;
    }
}