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

public class DriverAdd extends JDialog implements ActionListener, DocumentListener {// naprosto stejná podstata CarEdit

    private JPanel panel = new JPanel();
    private JTextField txtJmeno = new JTextField();
    private JTextField txtPrijmeni = new JTextField();
    private JTextField txtdatum = new JTextField();
    private int select;
    private boolean edit = false;
    /// datum 
    private JComboBox combYear;
    private JComboBox combMonth;
    private JComboBox combDey;
    private GregorianCalendar calendarMy;
    private Calendar kal = Calendar.getInstance();
    /// datum end
    DbAccess k;

    public DriverAdd() {
        print();
    }

    public DriverAdd(int prikaz, int select) {
        this.select = select;
        edit = true;
        k = new DbAccess();
        k.DbRead(prikaz, select);
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

        //// combo boxy 
        calendarMy = new GregorianCalendar();
        int ridicak = 18;
        Integer[] year = new Integer[(100 - ridicak)];
        for (int x = ridicak; x < 100; x++) {
            year[x - ridicak] = calendarMy.get(Calendar.YEAR) - x;
        }
        int maxDey = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        Integer[] deys = new Integer[maxDey];
        for (int x = 0; x < maxDey; x++) {
            deys[x] = (x + 1);
        }

        JPanel datum = new JPanel(new FlowLayout());
        combYear = new JComboBox(year);
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
            combYear.setSelectedItem(Integer.parseInt(txtdatum.getText().substring(0, 4)));
            combMonth.setSelectedItem(Integer.parseInt(txtdatum.getText().substring(5, 7)));
            combDey.setSelectedItem(Integer.parseInt(txtdatum.getText().substring(8, 10)));
        }


        panel.add(datum);


        /// comboboxy end


        JButton butt1 = new JButton("OK");
        butt1.addActionListener(this);
        panel.add(butt1);

        JButton butt2 = new JButton("Cancel");
        butt2.addActionListener(this);
        panel.add(butt2);

        panel.setPreferredSize(new Dimension(458, 116));

        this.add(panel);

        this.setResizable(false);
        this.setModal(true);
        this.pack();
        setLocationRelativeTo(getRootPane());

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
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
                txtJmeno.setName("Jmeno");
                txtPrijmeni.setName("příjmení");
                txtdatum.setName("datum");
                txtdatum.setText(combYear.getSelectedItem() + "-" + (((Integer) combMonth.getSelectedItem())) + "-" + combDey.getSelectedItem());
                aray.add(txtJmeno);
                aray.add(txtPrijmeni);
                aray.add(txtdatum);

                if (edit) {
                    try {
                        new MyExceptionDetector(aray, select, MyExceptionDetector.DRIVER_EDIT);

                        k.DbWriteDriverEdit(txtJmeno.getText(), txtPrijmeni.getText(), txtdatum.getText(), select);
                        //new DbAccess(false, prikaz);
                        this.setVisible(false);
                    } catch (MyException ex) {
                        if (ex.isShow()) {
                            JOptionPane.showMessageDialog(this, ex.getException());
                        }
                    }
                } else {
                    try {
                        new MyExceptionDetector(aray, MyExceptionDetector.DRIVER_ADD);

                        k.DbWriteDriverAdd(txtJmeno.getText(), txtPrijmeni.getText(), txtdatum.getText());
                        //  new DbAccess(false, prikaz);
                        this.setVisible(false);

                    } catch (MyException ex) {
                        if (ex.isShow()) {
                            JOptionPane.showMessageDialog(this, ex.getException());
                        }
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
