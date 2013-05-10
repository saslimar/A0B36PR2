package pr2_kniha_jizd;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import pr2_kniha_jizd.database.DbAccess;

public class Details extends JDialog implements ActionListener, DocumentListener {//třída vytahne z DB všechny související záznamy a zobrazí je
// s možnosti uložení do souboru s vlastnímy upravamy

    private JPanel panel = new JPanel();
    private JButton save = new JButton("Save");
    private JButton cancel = new JButton("Cancel");
    JTextArea text = new JTextArea("", 21, 19);
    private boolean edit = false;
    String[] colum;
    String[][] data;

    public Details(String tab_sel, Integer id) {// konstruktor příjmá data o kterou tabulku se jedná a jake je id zaznamu 
        print(tab_sel, id);
        text.getDocument().addDocumentListener(this);
    }

    private void print(String tab_sel, Integer id) {// metoda načte data z databáze a zobrazý je 
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        String prikaz = "select * from APP." + tab_sel + " where " + tab_sel + "ID = " + id;
        JPanel dataPanel = new JPanel();
        DbAccess db = new DbAccess(true, prikaz);// volání databáze z daným příkazem
        colum = db.getColum();// vrací hodnoty z databáze
        data = db.getData();// vrací hodnoty z databáze

        if ("RIDE".equals(tab_sel)) {// sežazení výpisu podle typu zobrazovaných dat
            printRide(id);
            printDriver(Integer.parseInt(data[0][data[0].length - 2]));
            printCar(Integer.parseInt(data[0][data[0].length - 1]));
        }
        if ("DRIVER".equals(tab_sel)) {
            printDriver(id);
            String[][] rideId = new DbAccess(true, "select RIDEID,CARID from APP.ride where DRIVERID =" + id).getData();
            if (rideId != null) {
                for (int x = 0; x < rideId.length; x++) {
                    printRide(Integer.parseInt(rideId[x][0]));
                    printCar(Integer.parseInt(rideId[x][1]));
                }
            }
        }
        if ("CAR".equals(tab_sel)) {
            printCar(id);
            String[][] rideId = new DbAccess(true, "select RIDEID,DRIVERID from APP.ride where CARID =" + id).getData();
            if (rideId != null) {
                for (int x = 0; x < rideId.length; x++) {
                    printRide(Integer.parseInt(rideId[x][0]));
                    printDriver(Integer.parseInt(rideId[x][1]));
                }
            }

        }
        JScrollPane k = new JScrollPane(text);
        k.createVerticalScrollBar();
        k.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        dataPanel.add(k);

        panel.add(dataPanel);


        JPanel btnPanel = new JPanel();
        btnPanel.add(save);
        save.addActionListener(this);

        btnPanel.add(cancel);
        cancel.addActionListener(this);

        panel.add(btnPanel);
        this.add(panel);
        this.setResizable(false);
        this.setModal(true);
        this.pack();
        this.setSize(262, 423);
        this.setLocation(WIDTH, HEIGHT);
        setLocationRelativeTo(getRootPane());

    }

    private void printDriver(Integer id) {// vipíše data o řidičovy s ID
        DbAccess db = new DbAccess(true, "select * from APP.DRIVER where DRIVERID =" + id);
        String[] colum = db.getColum();
        String[][] data = db.getData();
        text.setText(text.getText() + "ŘIDIČ\n_________________\n");
        for (int x = 1; x < 4; x++) {
            String data_ = data[0][x];
            if (data[0][x] == null) {
                data_ = "nevedeno";
            }
            text.setText(text.getText() + colum[x] + " : " + data_ + "\n");
        }
        text.setText(text.getText() + "\n");

    }

    private void printCar(Integer id) {// vipíše data o autě s ID
        DbAccess db = new DbAccess(true, "select * from APP.CAR where CARID =" + id);
        String[] colum = db.getColum();
        String[][] data = db.getData();
        text.setText(text.getText() + "AUTO\n_________________\n");
        for (int x = 1; x < 5; x++) {
            String data_ = data[0][x];
            if (data[0][x] == null) {
                data_ = "nevedeno";
            }
            text.setText(text.getText() + colum[x] + " : " + data_ + "\n");
        }
        text.setText(text.getText() + "\n");

    }

    private void printRide(Integer id) {// vipíše data o jízdě s ID
        DbAccess db = new DbAccess(true, "select * from APP.ride where RIDEID =" + id);
        String[] colum = db.getColum();
        String[][] data = db.getData();
        text.setText(text.getText() + "Jízda\n_________________\n");
        for (int x = 1; x < 6; x++) {
            String data_ = data[0][x];
            if (data[0][x] == null) {
                data_ = "nevedeno";
            }
            text.setText(text.getText() + colum[x] + " : " + data_ + "\n");
        }
        text.setText(text.getText() + "\n");

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("Cancel")) {// zmačnuli cancel tabulka nám zmizý
            this.setVisible(false);
        }
        if (ae.getActionCommand().equals("Save")) {// zmačknuli uložit
            if (edit) {// ovjeřím zda bylo editováno
                switch (JOptionPane.showConfirmDialog(this, "Data byla editována  \n Pokračovat?", "varování", JOptionPane.OK_CANCEL_OPTION)) {// zeptam se zda uložit s editací pro případ že by byla náhodnoá
                    case JOptionPane.CANCEL_OPTION:
                        break;
                    case JOptionPane.OK_OPTION:
                        String cesta = new Browse().saveDialog();
                        saveToTxt(cesta, text.getText());
                        this.setVisible(false);
                        break;
                }

            } else {// pokud editovano nebylo jednoduše uložím
                String cesta = new Browse().saveDialog();// načtu cestu k souboru
                saveToTxt(cesta, text.getText());// zavolám uložení
                this.setVisible(false);// a schovám zobrazovací okno
            }
        }
    }

    private void saveToTxt(String cesta, String data) {// metoda uloží data co souboru 
        if (cesta == null || cesta.equals("")) {
        } else {
            try {
                FileWriter outFile = new FileWriter(cesta, true);
                PrintWriter out = new PrintWriter(outFile);
                out.print(data);
                out.close();
            } catch (IOException e) {
            }
        }
    }

    @Override
    public void insertUpdate(DocumentEvent de) {// posluchač na editaci zobrazeneho textu\
        edit = true;
    }

    @Override
    public void removeUpdate(DocumentEvent de) {// posluchač na editaci zobrazeneho textu\
        edit = true;
    }

    @Override
    public void changedUpdate(DocumentEvent de) {// posluchač na editaci zobrazeneho textu\
        edit = true;
    }
}