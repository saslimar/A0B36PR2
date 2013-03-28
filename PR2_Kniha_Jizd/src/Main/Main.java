package Main;

import java.awt.Toolkit;
import pr2_kniha_jizd.add_edit.DriverAdd;
import pr2_kniha_jizd.add_edit.CarAdd;
import pr2_kniha_jizd.add_edit.RideAdd;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import pr2_kniha_jizd.MainGui;
import pr2_kniha_jizd.database.*;

public class Main{

    static MainGui mainGui = new MainGui();
    
    static JMenuBar menu = new JMenuBar();                  
    static JMenu Soubor = new JMenu("Soubor");
    static JMenu add = new JMenu("add");
    static JMenuItem car = new JMenuItem("auto");
    static JMenuItem driver = new JMenuItem("řidič");
    static JMenuItem ride = new JMenuItem("jízda");

    static JMenuItem export = new JMenuItem("Export");
    static JMenuItem importD = new JMenuItem("Import");    
    static JMenuItem exit = new JMenuItem("Exit (E)", 'E');

    static JMenu napoveda = new JMenu("Nápověda");
    static JMenuItem oProgramu = new JMenuItem("O programu (O)", 'O');

    public static void main(String[] args) {
        
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (java.lang.ClassNotFoundException e) {
            System.err.print("ClassNotFoundException: ");
            System.err.println(e.getMessage());
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:derby:KJData;create=true", "ures", "123456");
            conn.close();
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }

        // <editor-fold defaultstate="collapsed" desc="MENU">
        
        add.add(car);
        add.add(ride);
        add.add(driver);
        
        Soubor.add(add);
        menu.add(Soubor);
        menu.add(napoveda);

        Soubor.add(export);
        Soubor.add(importD);
        Soubor.add(exit);
        napoveda.add(oProgramu);
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                System.exit(0);
            }
        });
        export.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new ExportDb();
            }
        });
        importD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new ImportDb();
                mainGui.print_tabel_import();
            }
        });
        oProgramu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JOptionPane.showMessageDialog(new JDialog(), "<html><b>Kniha Jízd</b></html>\nVerze produktu: 1.0\n\nvytvořil: Martin Saslík");

            }
        });
        car.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new CarAdd().setVisible(true);
                mainGui.print_tabel("select * from APP.CAR");
            }
        });
        driver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new DriverAdd().setVisible(true);
                mainGui.print_tabel("select * from APP.DRIVER");
            }
        });
        ride.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new RideAdd().setVisible(true);
                mainGui.print_tabel("SELECT RIDEID,DATUM_CESTY,ODKUD,KAM,DUVOD_CESTY,VZDALENOST,SPOTREBA ,"
            + "PRIJMENI||'.'||substr(JMENO, 1, 1) AS DRIVER, SPZ AS AUTO FROM APP.RIDE JOIN"
            + " APP.DRIVER ON APP.DRIVER.DRIVERID = APP.RIDE.DRIVERID join APP.CAR on "
            + "APP.CAR.CARID = app.RIDE.CARID");
            }
        });
       // </editor-fold>
        
        
        
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainGui);
        frame.pack();
        frame.setLocation(
                (Toolkit.getDefaultToolkit().getScreenSize().height-frame.getSize().height)/2, 
                ((Toolkit.getDefaultToolkit().getScreenSize().width-frame.getSize().width)/2)-200
                );
        frame.setSize(600, 500);
        frame.setJMenuBar(menu);
        frame.setVisible(true);

    }
}