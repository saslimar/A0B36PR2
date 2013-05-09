package pr2_kniha_jizd.add_edit;

import pr2_kniha_jizd.database.DbRead;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import pr2_kniha_jizd.Exception.MyException;
import pr2_kniha_jizd.Exception.MyExceptionDetector;
import pr2_kniha_jizd.database.DbWrite;

public class RideAdd extends JDialog implements ActionListener, DocumentListener {// viis car add

    private JPanel panel = new JPanel();
    private JTextField txtDatum = new JTextField();
    private JTextField txtOdkud = new JTextField();
    private JTextField txtKam = new JTextField();
    private JTextField txtDuvod = new JTextField();
    private JTextField txtVzdalenost = new JTextField();
    private JTextField txtSpotreba = new JTextField();
    private JComboBox txtDriver = new JComboBox();
    private JComboBox txtCar = new JComboBox();
    private int driver;
    private int car;
    private Integer[] poleIdDriver;
    private Integer[] poleIdCar;
    private int select;
    private boolean edit = false;

    public RideAdd(String prikaz, int select) {
        this.select = select;
        edit = true;
        DbRead k = new DbRead(prikaz);
        String[][] data = k.getData();
        txtDatum.setText(data[0][1]);
        txtOdkud.setText(data[0][2]);
        txtKam.setText(data[0][3]);
        txtDuvod.setText(data[0][4]);

        txtVzdalenost.setText(data[0][5]);
        txtSpotreba.setText(data[0][6]);

        driver = (Integer.parseInt(data[0][7]));//-1);
        car = (Integer.parseInt(data[0][8]));//-1);
        print();
    }

    public RideAdd() {
        print();
    }

    private void print() {
        txtDatum.getDocument().addDocumentListener(this);
        txtDuvod.getDocument().addDocumentListener(this);
        txtKam.getDocument().addDocumentListener(this);
        txtOdkud.getDocument().addDocumentListener(this);
        txtSpotreba.getDocument().addDocumentListener(this);
        txtVzdalenost.getDocument().addDocumentListener(this);

        panel.setLayout(new GridLayout(9, 2));

        panel.add(new Label("Datum jízdy"));
        panel.add(txtDatum);
        panel.add(new Label("Odkud :"));
        panel.add(txtOdkud);
        panel.add(new Label("Kam :"));
        panel.add(txtKam);
        panel.add(new Label("Duvod cesty :"));
        panel.add(txtDuvod);
        panel.add(new Label("Vzdalenost :"));
        panel.add(txtVzdalenost);
        panel.add(new Label("Spotřeba :"));
        panel.add(txtSpotreba);

        panel.add(new Label("Řidič :"));
        String[] pole = getData("select PRIJMENI||'.'||substr(JMENO, 1, 1) from APP.DRIVER");
        poleIdDriver = getDataInt("select DRIVERID from APP.DRIVER");

        txtDriver.setModel(new javax.swing.DefaultComboBoxModel(pole));
        panel.add(txtDriver);


        panel.add(new Label("Auto :"));
        pole = getData("select SPZ from APP.CAR");
        poleIdCar = getDataInt("select CARID from APP.CAR");

        txtCar.setModel(new javax.swing.DefaultComboBoxModel(pole));
        panel.add(txtCar);

        if (edit) {
            for (int i = 0; i < poleIdDriver.length; i++) {
                if (poleIdDriver[i] == driver) {
                    txtDriver.setSelectedIndex(i);
                }
            }
            for (int i = 0; i < poleIdCar.length; i++) {
                if (poleIdCar[i] == car) {
                    txtCar.setSelectedIndex(i);
                }
            }
        }
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

    private String[] getData(String prikaz) {
        String[][] data_ = new DbRead(prikaz).getData();
        String[] data = new String[data_.length];
        for (int x = 0; x < data_.length; x++) {
            data[x] = data_[x][0];
        }
        return data;

    }

    private Integer[] getDataInt(String prikaz)
    {
        String[][] data_ = new DbRead(prikaz).getData();
        Integer[] data = new Integer[data_.length];
        for (int x = 0; x < data_.length; x++) {
            data[x] = Integer.parseInt(data_[x][0]);
        }
        return data;

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("Cancel")) {
            this.setVisible(false);
        }
        if (ae.getActionCommand().equals("OK")) {
            ArrayList<JTextField> aray = new ArrayList<JTextField>();
            txtVzdalenost.setName("vzdálenost");
            txtSpotreba.setName("spotřeba");
            txtOdkud.setName("odkud");
            txtKam.setName("kam");
            txtDuvod.setName("duvod");
            txtDatum.setName("datum");

            aray.add(txtDatum);
            aray.add(txtOdkud);
            aray.add(txtKam);
            aray.add(txtDuvod);

            try {
                new MyExceptionDetector(aray, MyExceptionDetector.RIDE_ADD_EDIT);
                Integer intVzdalenost = null;
                if (!"".equals(txtVzdalenost.getText())) {
                    intVzdalenost = Integer.parseInt(txtVzdalenost.getText());
                }
                Integer intSpotreba = null;
                if (!"".equals(txtSpotreba.getText())) {
                    intSpotreba = Integer.parseInt(txtSpotreba.getText());
                }

                String prikaz;
                if (edit) {
                    prikaz = "UPDATE \"APP\".\"RIDE\"set DATUM_CESTY='" + txtDatum.getText() + "'"
                            + ",ODKUD='" + txtOdkud.getText() + "'"
                            + ",kam='" + txtKam.getText() + "'"
                            + ",DUVOD_CESTY='" + txtDuvod.getText() + "'"
                            + ",VZDALENOST=" + intVzdalenost + ""
                            + ",SPOTREBA=" + intSpotreba + ""
                            + ",DRIVERID=" + poleIdDriver[txtDriver.getSelectedIndex()] + ""
                            + ",CARID=" + poleIdCar[txtCar.getSelectedIndex()] + ""
                            + " where RIDEID =" + select;
                } else {
                    prikaz = "INSERT INTO \"APP\".\"RIDE\""
                            + "(DATUM_CESTY,ODKUD,KAM,DUVOD_CESTY,VZDALENOST,SPOTREBA,DRIVERID,CARID)"
                            + "VALUES('" + txtDatum.getText() + "',"
                            + "'" + txtOdkud.getText() + "',"
                            + "'" + txtKam.getText() + "',"
                            + "'" + txtDuvod.getText() + "'"
                            + "," + intVzdalenost + ","
                            + "" + intSpotreba + ","
                            + "" + poleIdDriver[txtDriver.getSelectedIndex()] + ","
                            + "" + poleIdCar[txtCar.getSelectedIndex()] + ""
                            + ")";
                }

                new DbWrite(prikaz);
                this.setVisible(false);
            } catch (MyException ex) {
                if (ex.isShow()) {
                    JOptionPane.showMessageDialog(this, ex.getException());
                }
            }
        }
    }

    private void rePrintColor() {
        txtDatum.setBackground(Color.WHITE);
        txtDuvod.setBackground(Color.WHITE);
        txtKam.setBackground(Color.WHITE);
        txtOdkud.setBackground(Color.WHITE);
        txtSpotreba.setBackground(Color.WHITE);
        txtVzdalenost.setBackground(Color.WHITE);
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
