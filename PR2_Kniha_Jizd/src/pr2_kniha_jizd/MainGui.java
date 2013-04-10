package pr2_kniha_jizd;

import pr2_kniha_jizd.add_edit.DriverAdd;
import pr2_kniha_jizd.add_edit.CarAdd;
import pr2_kniha_jizd.add_edit.RideAdd;
import pr2_kniha_jizd.database.DbWrite;
import pr2_kniha_jizd.database.DbRead;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainGui extends JPanel implements ActionListener {
    // <editor-fold defaultstate="collapsed" desc="global promnenný">
    
    private TabelPanel tabulka;
    private TextField text = new TextField(12);
    private String tabel_sel = "RIDE";
    private String code = "SELECT RIDEID,DATUM_CESTY,ODKUD,KAM,DUVOD_CESTY,VZDALENOST,SPOTREBA ,"
            + "PRIJMENI||'.'||substr(JMENO, 1, 1) AS DRIVER, SPZ AS AUTO FROM APP.RIDE JOIN"
            + " APP.DRIVER ON APP.DRIVER.DRIVERID = APP.RIDE.DRIVERID join APP.CAR on "
            + "APP.CAR.CARID = app.RIDE.CARID";
    private JPanel cards;
    private String jizdy = "jízdy";
    private String auta = "auta";
    private String lide = "lidé";
    private JButton btn_jizdy;
    private JButton btn_auta;
    private JButton btn_lide;
    
    
    // </editor-fold>

    public MainGui() {
        // <editor-fold defaultstate="collapsed" desc="GUI PRINT">
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        
        JPanel selectPanel = new JPanel(); //use FlowLayout

        btn_jizdy = new JButton(jizdy);
        btn_auta = new JButton(auta);
        btn_lide = new JButton(lide);


        btn_jizdy.addActionListener(this);
        btn_auta.addActionListener(this);
        btn_lide.addActionListener(this);

        selectPanel.add(btn_jizdy);
        selectPanel.add(btn_auta);
        selectPanel.add(btn_lide);
        
        this.add(selectPanel);
        tabulka = new TabelPanel(code);
        this.add(tabulka);

        JPanel btnSearchPanel = new JPanel(new FlowLayout());;

        text.setSize(60, 50);
        btnSearchPanel.add(text);

        JButton search = new JButton("search");
        search.addActionListener(this);
        btnSearchPanel.add(search); 

        JButton edit = new JButton("edit");
        edit.addActionListener(this);
        btnSearchPanel.add(edit);

        JButton add = new JButton("add");
        add.addActionListener(this);
        btnSearchPanel.add(add);

        JButton delete = new JButton("delete");
        delete.addActionListener(this);
        btnSearchPanel.add(delete);

        JButton details = new JButton("detaily");
        details.addActionListener(this);
        btnSearchPanel.add(details);

        this.add(btnSearchPanel);
        // </editor-fold>
      
    }
    // <editor-fold defaultstate="collapsed" desc="PRINT Tabel">
    public void print_tabel_import()
    {
        if ("RIDE".equals(tabel_sel)) {
            print_tabel(code);
        } else {
            print_tabel("select * from APP." + tabel_sel);
        }
        this.validate();
    }
    public void print_tabel(String list) {
        this.remove(tabulka);
        tabulka = new TabelPanel(list);
        this.add(tabulka, 1);
    }
    // </editor-fold>

    @Override
    public void actionPerformed(ActionEvent ae) {
        // <editor-fold defaultstate="collapsed" desc="přepínání tabulek">        
        if (ae.getActionCommand().equals(auta)) {
            tabel_sel = "CAR";
            print_tabel("select * from APP." + tabel_sel);
        }
        if (ae.getActionCommand().equals(lide)) {
            tabel_sel = "DRIVER";
            print_tabel("select * from APP." + tabel_sel);
        }
        if (ae.getActionCommand().equals(jizdy)) {
            tabel_sel = "RIDE";
            print_tabel(code);
        }
        // </editor-fold>     
        // <editor-fold defaultstate="collapsed" desc="ADD">        
        if (ae.getActionCommand().equals("add")) {
            if ("DRIVER".equals(tabel_sel)) {
                new DriverAdd().setVisible(true);
            }
            if ("CAR".equals(tabel_sel)) {
                new CarAdd().setVisible(true);
            }
            if ("RIDE".equals(tabel_sel)) {
                new RideAdd().setVisible(true);
                print_tabel(code);
            } else {
                print_tabel("select * from APP." + tabel_sel);
            }
        }
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="EDIT">        
        if (ae.getActionCommand().equals("edit")) {
            int col = tabulka.getTable().getSelectedRow();
            if (col != -1) {
                int sel = Integer.parseInt(tabulka.getTable().getValueAt(col, 0).toString());
                if ("DRIVER".equals(tabel_sel)) {
                    new DriverAdd("SELECT * FROM APP.DRIVER WHERE DRIVERID = " + sel, sel).setVisible(true);
                }
                if ("CAR".equals(tabel_sel)) {
                    new CarAdd("SELECT * FROM APP.CAR WHERE CARID = " + sel, sel).setVisible(true);
                }
                if ("RIDE".equals(tabel_sel)) {
                    new RideAdd("SELECT * FROM APP.RIDE WHERE RIDEID = " + sel, sel).setVisible(true);
                    print_tabel(code);
                } else {
                    print_tabel("select * from APP." + tabel_sel);
                }
            }
        }
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="DELETE">
        if (ae.getActionCommand().equals("delete")) {
            int col = tabulka.getTable().getSelectedRow();
            if (col != -1) {
                int sel = Integer.parseInt(tabulka.getTable().getValueAt(col, 0).toString());
                if ("RIDE".equals(tabel_sel)) {
                    delete(sel);
                    print_tabel(code);
                    if (tabulka.getTable().getRowCount() > 0) {
                        if (col == 0) {
                            col++;
                        }
                        tabulka.getTable().setRowSelectionInterval(col - 1, col - 1);
                    }
                } else {
                    boolean isUse = ((new DbRead("SELECT * FROM app.RIDE WHERE " + tabel_sel + "ID =" + sel).getData() != null));
                    if (isUse) {
                        switch (JOptionPane.showConfirmDialog(this, "Po smazaní budou odstaněny i všechny související jízdy \n Pokračovat?", "varování", JOptionPane.WARNING_MESSAGE)) {
                            case JOptionPane.OK_OPTION:
                                delete(sel);
                                new DbWrite("DELETE FROM app.RIDE WHERE " + tabel_sel + "ID =" + sel);
                                print_tabel("select * from APP." + tabel_sel);
                                if (tabulka.getTable().getRowCount() > 0) {
                                    if (col == 0) {
                                        col++;
                                    }
                                    tabulka.getTable().setRowSelectionInterval(col - 1, col - 1);
                                }
                                ;
                                break;
                            case JOptionPane.CANCEL_OPTION:
                                tabulka.getTable().setRowSelectionInterval(col, col);
                        }
                    } else {
                        delete(sel);
                        print_tabel("select * from APP." + tabel_sel);
                        if (tabulka.getTable().getRowCount() > 0) {
                            if (col == 0) {
                                col++;
                            }
                            tabulka.getTable().setRowSelectionInterval(col - 1, col - 1);
                        }
                    }

                }
            }
        }
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Detaily">
        if (ae.getActionCommand().equals("detaily")) {
            int col = tabulka.getTable().getSelectedRow();
            if (col != -1) {
                int sel = Integer.parseInt(tabulka.getTable().getValueAt(col, 0).toString());
                new Details(tabel_sel,sel).setVisible(true);
            }
        }
       // </editor-fold>

        if (ae.getActionCommand().equals("search")) {
            // <editor-fold defaultstate="collapsed" desc="SEARCH">

            if ("RIDE".equals(tabel_sel)) {
                String dotazRide = "select RIDEID,DATUM_CESTY,ODKUD,KAM,DUVOD_CESTY,VZDALENOST,SPOTREBA ,"
                + "PRIJMENI||'.'||substr(JMENO, 1, 1) AS DRIVER, SPZ AS AUTO FROM APP.RIDE JOIN"
                + " APP.DRIVER ON APP.DRIVER.DRIVERID = APP.RIDE.DRIVERID join APP.CAR on "
                + "APP.CAR.CARID = app.RIDE.CARID WHERE "
                            + "upper(ODKUD) like upper('%" + text.getText() + "%') or "
                        + "upper(KAM) like upper('%" + text.getText() + "%') or "
                        + "upper(DUVOD_CESTY) like upper('%" + text.getText() + "%') or "
                        + "upper(PRIJMENI||'.'||substr(JMENO, 1, 1)) like upper('%" + text.getText() + "%') or "
                        + "upper(SPZ) like upper('%" + text.getText() + "%')";
                        
                boolean addInt = false;
                try {
                    Integer test = Integer.parseInt(text.getText());
                    addInt = true;
                } catch (NumberFormatException e) {
                }
                if (addInt) {
                    dotazRide += "or VZDALENOST = " + text.getText() + " or "
                            + "SPOTREBA = " + text.getText();
                }
                print_tabel(dotazRide);
            }
            if ("CAR".equals(tabel_sel)) {
                String dotazCar = "select * from APP.CAR WHERE "
                        + "upper(ZNACKA) like upper('%" + text.getText() + "%') or "
                        + "upper(SPZ) like upper('%" + text.getText() + "%') or "
                        + "upper(DRUH) like upper('%" + text.getText() + "%')";
                print_tabel(dotazCar);
            }
            if ("DRIVER".equals(tabel_sel)) {
                String dotazDriver = "select * from APP.DRIVER WHERE "
                        + "upper(JMENO) like upper('%" + text.getText() + "%') or "
                        + "upper(PRIJMENI) like upper('%" + text.getText() + "%') or "
                        + "upper(DATUM_NAROZENI) like upper('%" + text.getText() + "%')";
                print_tabel(dotazDriver);
            }
            //</editor-fold>
        }
        eventRepack(ae);
    }
    
    private void delete(int sel) {
        String prikaz = "DELETE FROM app." + tabel_sel + "  WHERE " + tabel_sel + "ID=";
        new DbWrite(prikaz + sel);
    }

    private void eventRepack(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            Container c = ((JButton) (e.getSource())).getParent();
            while ((c.getParent() != null) && (!(c instanceof Window))) {
                c = c.getParent();
            }
            if (c instanceof Window) {
                Window w = (Window) c;
                w.validate();
            }
        }
    }
}