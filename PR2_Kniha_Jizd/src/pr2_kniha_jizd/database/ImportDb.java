package pr2_kniha_jizd.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import pr2_kniha_jizd.Browse;

public class ImportDb {

    public ImportDb() {
        String cesta = new Browse().loadDialog();
        if (cesta == null || cesta.equals("")) {
        } else {
            load(cesta);
        }

    }

    private void load(String cesta) {
        try {
            String[] data = read_from_txt(cesta);
            for (String s : data) {
                addToDb(s);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JDialog(), "cyba v načítání databáze");
        }
    }

    static String[] read_from_txt(String cesta) throws Exception {
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

    private void addToDb(String data) {
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
            if (new DbRead(is).getData() == null) {
                new DbWrite(data);
            } else {
            }
        } else if (data.indexOf("\"APP\".\"RIDE\"") != -1) {
            new DbWrite(data);
        } else {
        }
    }
}
