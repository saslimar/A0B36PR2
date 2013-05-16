package pr2_kniha_jizd.add_edit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
import pr2_kniha_jizd.database.DbAccess;

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
    /// datum 
    private JComboBox combYear;
    private JComboBox combMonth;
    private JComboBox combDey;
    private GregorianCalendar calendarMy;
    private Calendar kal = Calendar.getInstance();
    /// datum end
    DbAccess k = new DbAccess();

    public RideAdd(int prikaz, int select) {
        this.select = select;
        edit = true;
        k.DbRead(prikaz, select);
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
        //// combo boxy 
        calendarMy = new GregorianCalendar();
        Integer[] year = new Integer[(100)];
        for (int x = -1; x < 99; x++) {
            year[x + 1] = calendarMy.get(Calendar.YEAR) - x;
        }
        int maxDey = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        Integer[] deys = new Integer[maxDey];
        for (int x = 0; x < maxDey; x++) {
            deys[x] = (x + 1);
        }

        JPanel datum = new JPanel(new FlowLayout());
        combYear = new JComboBox(year);
        combYear.setSelectedItem((calendarMy.get(Calendar.YEAR)));
        combYear.addActionListener(this);
        combYear.setPreferredSize(new Dimension(82, 28));
        datum.add(combYear);

        combMonth = new JComboBox(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12});
        combMonth.setSelectedItem((calendarMy.get(Calendar.MONTH)) + 1);
        combMonth.addActionListener(this);
        combMonth.setPreferredSize(new Dimension(66, 28));
        datum.add(combMonth);

        combDey = new JComboBox(deys);
        combDey.setSelectedItem(calendarMy.get(Calendar.DATE));
        combDey.addActionListener(this);
        combDey.setPreferredSize(new Dimension(66, 28));
        datum.add(combDey);

        if (edit) {
            combYear.setSelectedItem(Integer.parseInt(txtDatum.getText().substring(0, 4)));
            combMonth.setSelectedItem(Integer.parseInt(txtDatum.getText().substring(5, 7)));
            combDey.setSelectedItem(Integer.parseInt(txtDatum.getText().substring(8, 10)));
        }
        panel.add(datum);

        /// comboboxy end
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
        String[] pole = getData(DbAccess.DRIVER);
        poleIdDriver = getDataInt(DbAccess.DRIVER);

        txtDriver.setModel(new javax.swing.DefaultComboBoxModel(pole));
        panel.add(txtDriver);

        panel.add(new Label("Auto :"));
        pole = getData(DbAccess.CAR);
        poleIdCar = getDataInt(DbAccess.CAR);

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

    private String[] getData(int i) {
        k.DbReadRideEdit(i, false);
        String[][] data_ = k.getData();
        String[] data = new String[data_.length];
        for (int x = 0; x < data_.length; x++) {
            data[x] = data_[x][0];
        }
        return data;

    }

    private Integer[] getDataInt(int i) {
        k.DbReadRideEdit(i, true);
        String[][] data_ = k.getData();
        Integer[] data = new Integer[data_.length];
        for (int x = 0; x < data_.length; x++) {
            data[x] = Integer.parseInt(data_[x][0]);
        }
        return data;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        boolean ok = true;
        if (ae.getSource() instanceof JComboBox) {
            JComboBox c = (JComboBox) ae.getSource();
            if (c == combMonth) {
                int x = (Integer) combYear.getSelectedItem();
                int y = (Integer) combMonth.getSelectedItem() - 1;
                int z = (Integer) combDey.getSelectedItem();
                kal.set(x, y, z);
                combDey.removeAllItems();
                int maxDey = kal.getActualMaximum(kal.DAY_OF_MONTH);
                for (int a = 0; a < maxDey; a++) {
                    combDey.addItem((a + 1));
                }
            }
        } else {
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

                txtDatum.setText(combYear.getSelectedItem() + "-" + (((Integer) combMonth.getSelectedItem())) + "-" + combDey.getSelectedItem());

                aray.add(txtDatum);
                aray.add(txtOdkud);
                aray.add(txtKam);
                aray.add(txtDuvod);

                try {
                    new MyExceptionDetector(aray, MyExceptionDetector.RIDE_ADD_EDIT);
                    Integer intVzdalenost = null;
                    if (!"".equals(txtVzdalenost.getText())) {
                        try {
                            intVzdalenost = Integer.parseInt(txtVzdalenost.getText());
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Vzdalenost muý být číslo");
                            txtVzdalenost.setBackground(Color.RED);
                            ok = false;
                        }
                    }
                    Integer intSpotreba = null;
                    if (!"".equals(txtSpotreba.getText())) {
                        intSpotreba = Integer.parseInt(txtSpotreba.getText());
                    }
                    if (ok) {
                        if (edit) {

                            k.DbWriteRideEdit(txtDatum.getText(), txtOdkud.getText(), txtKam.getText(), txtDuvod.getText(), intVzdalenost, intSpotreba, poleIdDriver[txtDriver.getSelectedIndex()], poleIdCar[txtCar.getSelectedIndex()], select);
                        } else {

                            k.DbWriteRideAdd(txtDatum.getText(), txtOdkud.getText(), txtKam.getText(), txtDuvod.getText(), intVzdalenost, intSpotreba, poleIdDriver[txtDriver.getSelectedIndex()], poleIdCar[txtCar.getSelectedIndex()]);
                        }
                        this.setVisible(false);
                    }
                } catch (MyException ex) {
                    if (ex.isShow()) {
                        JOptionPane.showMessageDialog(this, ex.getException());
                    }
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
