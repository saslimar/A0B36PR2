package pr2_kniha_jizd;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.JPanel;
import pr2_kniha_jizd.database.DbAccess;

public class TabelPanel extends JPanel {// panel z tabulkou

    private JTable table; //mýstní tabulka

    public TabelPanel(String tab, String tab2) {// konstruktor načte nazvy 2 tabulek vytahne z nich data spracuje je a zobrazí
        DbAccess k = new DbAccess();
        String s = "SELECT APP." + tab + ".ZNACKA AS ZNACKAJMENO,APP." + tab + ".SPZ AS INFO,APP." + tab + ".CARID AS VYUZITO FROM APP." + tab + "";//"SELECT ZNACKA AS \"ZNACKA/JMENO\",SPZ AS\"SPZ/DATUM\",CARID AS\"VYUZITO\" FROM APP.CAR;";
        System.out.println(s);
        k.DbRead(s);
        String[] colum = k.getColum();
        String[][] datCar = k.getData();
        System.out.println(colum.length);
        for (int x = 0; x < datCar.length; x++) {
            k.DbRead("SELECT COUNT(*) AS Rides FROM APP.RIDE WHERE CARID =" + datCar[x][2]);
            datCar[x][2] = (k.getData()[0][0] + " x");
            datCar[x][0] = "Car     : " + datCar[x][0];
        }
        s = "SELECT (APP." + tab2 + ".PRIJMENI||' '||APP." + tab2 + ".jmeno) ,APP." + tab2 + ".DATUM_NAROZENI ,APP." + tab2 + ".DRIVERID from APP." + tab2 + "";//"SELECT ZNACKA AS \"ZNACKA/JMENO\",SPZ AS\"SPZ/DATUM\",CARID AS\"VYUZITO\" FROM APP.CAR;";
        System.out.println(s);
        k.DbRead(s);
        String[][] datDriver = k.getData();
        System.out.println(colum.length);
        for (int x = 0; x < datDriver.length; x++) {
            k.DbRead("SELECT COUNT(*) AS Rides FROM APP.RIDE WHERE DRIVERID =" + datDriver[x][2]);
            datDriver[x][2] = (k.getData()[0][0] + " x");
            datDriver[x][0] = "Driver : " + datDriver[x][0];
        }
        show(sjednot(datCar, datDriver), colum);

    }

    private String[][] sjednot(String[][] s1, String[][] s2) {
        String[][] s = new String[s1.length + s2.length][s1.length];
        int a = 0;
        for (int x = 0; x < s1.length; x++) {
            s[a] = s1[x];
            a++;
        }
        for (int x = 0; x < s2.length; x++) {
            s[a] = s2[x];
            a++;
        }
        return s;
    }

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