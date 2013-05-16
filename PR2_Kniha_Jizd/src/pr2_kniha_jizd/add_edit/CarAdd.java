package pr2_kniha_jizd.add_edit;

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
import pr2_kniha_jizd.database.DbAccess;

public class CarAdd extends JDialog implements ActionListener, DocumentListener {// dialogove okno přispusobené k zadávání a upravje zaznamu o autě

    private JPanel panel = new JPanel();
    private JTextField txtSpz = new JTextField();
    private JTextField txtZnacka = new JTextField();
    private JTextField txtTyp = new JTextField();
    private JCheckBox firemni = new JCheckBox();
    private int select;
    private boolean edit = false;
    private DbAccess k;
    public CarAdd() {// prázdný konstruktor volám pokud chci vytvořit prázdné okno pro přidání záznamu
        print();
    }

    public CarAdd(int prikaz, int select) {// kontruktor pro upravu záznamu příkaz je databázový příkaz a select je databazove ID
        this.select = select;
        edit = true;
        k = new DbAccess();// vytahnu data z databáze
        k.DbRead(prikaz,select);
        String[][] data = k.getData();
        txtZnacka.setText(data[0][1]);// a naplním jimy požadovaná políčka
        txtSpz.setText(data[0][2]);
        txtTyp.setText(data[0][3]);

        if ("ano".equals(data[0][4])) {
            firemni.setSelected(true);
        }
        print();
    }

    private void print() {//  vykreslím gui
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
    public void actionPerformed(ActionEvent ae) {// posluchač zažizuje ukládání do dtabáze nového záznamu a kontrolu zadanách dat
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
                  //       new DbAccess(false, prikaz);
                    k.DbWriteCarEdit(txtZnacka.getText(),txtSpz.getText(),typtext,(firemni.isSelected() ? "ano" : "ne"),select);
                    this.setVisible(false);
                } catch (MyException ex) {
                    if (ex.isShow()) {
                        JOptionPane.showMessageDialog(this, ex.getException());
                    }
                }
            } else {
                try {
                    new MyExceptionDetector(aray, MyExceptionDetector.CAR_ADD);
                    k = new DbAccess();
                    k.DbWriteCarAdd(txtZnacka.getText(),txtSpz.getText(),typtext,(firemni.isSelected() ? "ano" : "ne"));
                    this.setVisible(false);


                } catch (MyException ex) {
                    if (ex.isShow()) {
                        JOptionPane.showMessageDialog(this, ex.getException());
                    }
                }
            }

        }
    }

    private void rePrintColor() { // vyčištění barev varovných pozadí z5 na býlou
        txtSpz.setBackground(Color.WHITE);
        txtTyp.setBackground(Color.WHITE);
        txtZnacka.setBackground(Color.WHITE);
    }

    @Override
    public void insertUpdate(DocumentEvent de) {// posluchač editace
        rePrintColor();
    }

    @Override
    public void removeUpdate(DocumentEvent de) {// posluchač editace
        rePrintColor();
    }

    @Override
    public void changedUpdate(DocumentEvent de) {// posluchač editace
        rePrintColor();
    }
}
