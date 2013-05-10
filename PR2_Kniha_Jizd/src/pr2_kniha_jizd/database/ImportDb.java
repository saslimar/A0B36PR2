package pr2_kniha_jizd.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import pr2_kniha_jizd.Browse;

public class ImportDb {

    public ImportDb() {// třída dosti podobná export db 
        String cesta = new Browse().loadDialog();// načte si cestu k souboru
        if (cesta == null || cesta.equals("")) {// skontroluje jí
        } else {
            load(cesta);// vola funkci pro načtení dat
        }
    }

    private void load(String cesta) {
        try {
            String[] data = read_from_txt(cesta);// přečte data z textoveho souboru
            for (String s : data) {// po řádku
                addToDb(s);// volá jejich vkládání do databáze
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JDialog(), "cyba v načítání databáze");
        }
    }

    static String[] read_from_txt(String cesta) throws Exception {// metoda čte řádek po řádku ze souboru a vrací poe stringů
        BufferedReader in = new BufferedReader(new FileReader(cesta));
        String str;
        ArrayList<String> data = new ArrayList<String>();

        while ((str = in.readLine()) != null) {
            data.add(str);
        }
        in.close();
        String[] dataRetrn = new String[data.size()];
        for (int x = 0; x < data.size(); x++) {
            dataRetrn[x] = data.get(x);
        }
        return dataRetrn;
    }

    private void addToDb(String data) {// metoda pro zápis příslušného řádku do databáze kontroluje zda zadaný záznam jiš jednou v databázy není a pak jej přidá nebo nepřidá
        String is = "SELECT * FROM " + (data.substring(12, data.indexOf("\"(") + 1) + " WHERE ");
        String q = data.substring(data.indexOf("\"(") + 2, data.indexOf(")"));
        String h = data.substring(data.indexOf("(", data.indexOf("(") + 1) + 1, data.indexOf(")", data.indexOf(")") + 1));
        String[] qPole = q.split(",");
        String[] hPole = h.split(",");
        if (qPole.length == hPole.length) {
            for (int x = 0; x < qPole.length; x++) {
                if (!hPole[x].equals("null")) {
                    is += qPole[x] + " = " + hPole[x];
                    if (x < qPole.length - 1) {
                        is += " and ";
                    }
                }
            }
            if (new DbAccess(true, is).getData() == null) {
                new DbAccess(false, data);
            } else {
            }
        } else if (data.indexOf("\"APP\".\"RIDE\"") != -1) {
            new DbAccess(false, data);
        } else {
        }
    }
}
