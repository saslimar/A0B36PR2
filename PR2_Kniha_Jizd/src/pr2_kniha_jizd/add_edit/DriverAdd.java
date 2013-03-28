package pr2_kniha_jizd.add_edit;

import pr2_kniha_jizd.database.DbWrite;
import pr2_kniha_jizd.database.DbRead;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import pr2_kniha_jizd.Exception.MyException;
import pr2_kniha_jizd.Exception.MyExceptionDetector;

public class DriverAdd extends JDialog implements ActionListener, DocumentListener {

    private JPanel panel = new JPanel();
    private JTextField txtJmeno = new JTextField();
    private JTextField txtPrijmeni = new JTextField();
    private JTextField txtdatum = new JTextField();
    private int select;
    private boolean edit = false;

    public DriverAdd() {
        print();
    }

    public DriverAdd(String prikaz, int select) {
        this.select = select;
        edit = true;
        DbRead k = new DbRead(prikaz);
        String[][] data = k.getData();
        txtJmeno.setText(data[0][1]);
        txtPrijmeni.setText(data[0][2]);
        txtdatum.setText(data[0][3]);

        print();
    }

    private void print() {
        txtJmeno.getDocument().addDocumentListener(this);
        txtPrijmeni.getDocument().addDocumentListener(this);
        txtdatum.getDocument().addDocumentListener(this);

        panel.setLayout(new GridLayout(4, 2));
        panel.add(new Label("Jmeno :"));
        panel.add(txtJmeno);
        panel.add(new Label("příjmení :"));
        panel.add(txtPrijmeni);
        panel.add(new Label("datum :"));
        panel.add(txtdatum);


        JButton butt1 = new JButton("OK");
        butt1.addActionListener(this);
        panel.add(butt1);

        JButton butt2 = new JButton("Cancel");
        butt2.addActionListener(this);
        panel.add(butt2);

        panel.setPreferredSize(new Dimension(215, 116));

        this.add(panel);

        this.setResizable(false);
        this.setModal(true);
        this.pack();
        setLocationRelativeTo(getRootPane());

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("Cancel")) {
            this.setVisible(false);
        }
        if (ae.getActionCommand().equals("OK")) {
            ArrayList<JTextField> aray = new ArrayList<JTextField>();
            txtJmeno.setName("Jmeno");
            txtPrijmeni.setName("příjmení");
            txtdatum.setName("datum");
            aray.add(txtJmeno);
            aray.add(txtPrijmeni);
            aray.add(txtdatum);

            if (edit) {
                try {
                    new MyExceptionDetector(aray, select, MyExceptionDetector.DRIVER_EDIT);
                    String prikaz = "UPDATE \"APP\".\"DRIVER\" "
                            + "set JMENO='" + txtJmeno.getText() + "',"
                            + "PRIJMENI='" + txtPrijmeni.getText() + "',"
                            + "DATUM_NAROZENI={d '" + txtdatum.getText() + "'} "
                            + "WHERE DRIVERID = " + select;
                    new DbWrite(prikaz);
                    this.setVisible(false);
                } catch (MyException ex) {
                    if (ex.isShow()) {
                        JOptionPane.showMessageDialog(this, ex.getException());
                    }
                }
            } else {
                try {
                    new MyExceptionDetector(aray, MyExceptionDetector.DRIVER_ADD);
                    String prikaz = "INSERT INTO \"APP\".\"DRIVER\"(JMENO,PRIJMENI,DATUM_NAROZENI)"
                            + "VALUES("
                            + "'" + txtJmeno.getText() + "',"
                            + "'" + txtPrijmeni.getText() + "',"
                            + "{d '" + txtdatum.getText() + "'})";

                    new DbWrite(prikaz);
                    this.setVisible(false);

                } catch (MyException ex) {
                    if (ex.isShow()) {
                        JOptionPane.showMessageDialog(this, ex.getException());
                    }
                }
            }
        }

    }

    private void rePrintColor() {
        txtJmeno.setBackground(Color.WHITE);
        txtPrijmeni.setBackground(Color.WHITE);
        txtdatum.setBackground(Color.WHITE);
    }

    @Override
    public void insertUpdate(DocumentEvent de) {
        rePrintColor();
    }

    @Override
    public void removeUpdate(DocumentEvent de) {
        rePrintColor();
    }

    @Override
    public void changedUpdate(DocumentEvent de) {
        rePrintColor();
    }
}
