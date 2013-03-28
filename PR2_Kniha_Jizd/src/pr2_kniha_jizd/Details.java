package pr2_kniha_jizd;

import pr2_kniha_jizd.database.DbRead;
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
public class Details extends JDialog implements ActionListener, DocumentListener {

    private JPanel panel = new JPanel();
    private JButton save = new JButton("Save");
    private JButton cancel = new JButton("Cancel");
    JTextArea text = new JTextArea("", 21, 19);
    private boolean edit = false;
    String[] colum;
    String[][] data;

    public Details(String tab_sel, Integer id) {
        print(tab_sel, id);
        text.getDocument().addDocumentListener(this);
    }

    private void print(String tab_sel, Integer id) {
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        String prikaz = "select * from APP." + tab_sel + " where " + tab_sel + "ID = " + id;
        JPanel dataPanel = new JPanel();
        DbRead db = new DbRead(prikaz);
        colum = db.getColum();
        data = db.getData();

        if ("RIDE".equals(tab_sel)) {
            printRide(id);
            printDriver(Integer.parseInt(data[0][data[0].length - 2]));
            printCar(Integer.parseInt(data[0][data[0].length - 1]));
        }
        if ("DRIVER".equals(tab_sel)) {
            printDriver(id);
            String[][] rideId = new DbRead("select RIDEID,CARID from APP.ride where DRIVERID =" + id).getData();
            if (rideId != null) {
                for (int x = 0; x < rideId.length; x++) {
                    printRide(Integer.parseInt(rideId[x][0]));
                    printCar(Integer.parseInt(rideId[x][1]));
                }
            }
        }
        if ("CAR".equals(tab_sel)) {
            printCar(id);
            String[][] rideId = new DbRead("select RIDEID,DRIVERID from APP.ride where CARID =" + id).getData();
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

    private void printDriver(Integer id) {
        DbRead db = new DbRead("select * from APP.DRIVER where DRIVERID =" + id);
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

    private void printCar(Integer id) {
        DbRead db = new DbRead("select * from APP.CAR where CARID =" + id);
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

    private void printRide(Integer id) {
        DbRead db = new DbRead("select * from APP.ride where RIDEID =" + id);
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
        if (ae.getActionCommand().equals("Cancel")) {
            this.setVisible(false);
        }
        if (ae.getActionCommand().equals("Save")) {
            if (edit) {
                switch (JOptionPane.showConfirmDialog(this, "Data byla editována  \n Pokračovat?", "varování", JOptionPane.OK_CANCEL_OPTION)) {
                    case JOptionPane.CANCEL_OPTION:
                        break;
                    case JOptionPane.OK_OPTION:
                        String cesta = new Browse().saveDialog();
                        saveToTxt(cesta, text.getText());
                        this.setVisible(false);
                        break;
                }

            } else {
                String cesta = new Browse().saveDialog();
                saveToTxt(cesta, text.getText());
                this.setVisible(false);
            }
        }
    }

    private void saveToTxt(String cesta, String data) {
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
    public void insertUpdate(DocumentEvent de) {
        edit = true;
    }

    @Override
    public void removeUpdate(DocumentEvent de) {
        edit = true;
    }

    @Override
    public void changedUpdate(DocumentEvent de) {
        edit = true;
    }
}