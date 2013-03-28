package pr2_kniha_jizd.add_edit;

import pr2_kniha_jizd.database.DbWrite;
import pr2_kniha_jizd.database.DbRead;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import pr2_kniha_jizd.Exception.MyException;
import pr2_kniha_jizd.Exception.MyExceptionDetector;

public class CarAdd extends JDialog implements ActionListener, DocumentListener {

    private JPanel panel = new JPanel();
    private JTextField txtSpz = new JTextField();
    private JTextField txtZnacka = new JTextField();
    private JTextField txtTyp = new JTextField();
    private JCheckBox firemni = new JCheckBox();
    private int select;
    private boolean edit = false;

    public CarAdd() {
        print();
    }

    public CarAdd(String prikaz, int select) {

        this.select = select;
        edit = true;
        DbRead k = new DbRead(prikaz);
        String[][] data = k.getData();
        txtZnacka.setText(data[0][1]);
        txtSpz.setText(data[0][2]);
        txtTyp.setText(data[0][3]);

        if ("ano".equals(data[0][4])) {
            firemni.setSelected(true);
        }
        print();
    }

    private void print() {
        txtSpz.getDocument().addDocumentListener(this);
        txtTyp.getDocument().addDocumentListener(this);
        txtZnacka.getDocument().addDocumentListener(this);

        panel.setLayout(new GridLayout(5, 2));

        panel.add(new Label("SPZ :"));
        panel.add(txtSpz);
        panel.add(new Label("Značka :"));
        panel.add(txtZnacka);
        panel.add(new Label("typ vozidla :"));
        panel.add(txtTyp);


        panel.add(new Label("firemní vůz"));
        panel.add(firemni);

        JButton butt1 = new JButton("OK");
        butt1.addActionListener(this);
        panel.add(butt1);

        JButton butt2 = new JButton("Cancel");
        butt2.addActionListener(this);
        panel.add(butt2);

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
            txtSpz.setName("SPZ");
            txtZnacka.setName("Značka");
            aray.add(txtSpz);
            aray.add(txtZnacka);
            String typtext = "null";
            if (!"".equals(txtTyp.getText())) {
                typtext = "'" + txtTyp.getText() + "'";
            }
            if (edit) {
                try {
                    new MyExceptionDetector(aray, select, MyExceptionDetector.CAR_EDIT);
                    String prikaz = "UPDATE \"APP\".\"CAR\" "
                            + "set ZNACKA='" + txtZnacka.getText() + "',"
                            + "SPZ='" + txtSpz.getText() + "',"
                            + "DRUH=" + typtext + " "
                            + ",firemni = '" + (firemni.isSelected() ? "ano" : "ne") + "'"
                            + "WHERE CARID =" + select;
                    new DbWrite(prikaz);
                    this.setVisible(false);
                } catch (MyException ex) {
                    if(ex.isShow())
                    {
                         JOptionPane.showMessageDialog(this, ex.getException());
                    }
                }
            } else {
                try {
                    new MyExceptionDetector(aray, MyExceptionDetector.CAR_ADD);
                    String prikaz = "INSERT INTO \"APP\".\"CAR\" (znacka,spz,druh,firemni)VALUES('"
                            + txtZnacka.getText()
                            + "','" + txtSpz.getText() + "',"
                            + typtext + ","
                            + "'" + (firemni.isSelected() ? "ano" : "ne") + "')";

                    new DbWrite(prikaz);
                    this.setVisible(false);


                } catch (MyException ex) {
                   if(ex.isShow())
                    {
                         JOptionPane.showMessageDialog(this, ex.getException());
                    }
                }
            }

        }
    }

    private void rePrintColor() {
        txtSpz.setBackground(Color.WHITE);
        txtTyp.setBackground(Color.WHITE);
        txtZnacka.setBackground(Color.WHITE);
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
