package Main;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
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
import pr2_kniha_jizd.Statistika;
import pr2_kniha_jizd.database.*;

public class Main {
//*

    static MainGui mainGui = new MainGui(); //inicializace hlavního okna programu
    static JMenuBar menu = new JMenuBar();                  //inicializace horního menu 
    static JMenu Soubor = new JMenu("Soubor");              // a jeho prvků
    static JMenu add = new JMenu("add");
    static JMenuItem car = new JMenuItem("auto");
    static JMenuItem driver = new JMenuItem("řidič");
    static JMenuItem ride = new JMenuItem("jízda");
    static JMenuItem export = new JMenuItem("Export");
    static JMenuItem importD = new JMenuItem("Import");
    static JMenuItem exit = new JMenuItem("Exit (E)", 'E');
    static JMenu Zobrazit = new JMenu("Zobrazit");
    static JMenuItem statistikaCar = new JMenuItem("Statistika Aut (A)", 'A');
    static JMenuItem statistikaDrivers = new JMenuItem("Statistika Lidí (S)", 'S');
    
    static JMenu napoveda = new JMenu("Nápověda");
    static JMenuItem oProgramu = new JMenuItem("O programu (O)", 'O');
//*/

    public static void main(String[] args) {
        //*      
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver"); //ošetření programu v případě že databéze programu nebyla nalezena
        } catch (java.lang.ClassNotFoundException e) {
            System.err.print("ClassNotFoundException: ");
            System.err.println(e.getMessage());
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:derby:KJData;create=true", "ures", "123456"); /// ošetření programu v případě že k databázy se nepodařilo připojit
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
        menu.add(Zobrazit);
        menu.add(napoveda);

        Zobrazit.add(statistikaCar);
        Zobrazit.add(statistikaDrivers);
        Soubor.add(export);
        Soubor.add(importD);
        Soubor.add(exit);
        napoveda.add(oProgramu);
        exit.addActionListener(new java.awt.event.ActionListener() { // volba v menu exit přidání posluchače na ukončení programu

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                System.exit(0);
            }
        });
        export.addActionListener(new java.awt.event.ActionListener() {// volba export databaze

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new ExportDb(); // exportování databáze
            }
        });
        importD.addActionListener(new java.awt.event.ActionListener() { // volba import DB

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new ImportDb(); // importování databáze
                mainGui.print_tabel_import(); // aktualizace zobrazených dat pro zobrazení s novými importovanýmy daty
            }
        });
        oProgramu.addActionListener(new java.awt.event.ActionListener() {// o programu 

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JOptionPane.showMessageDialog(new JDialog(), "<html><b>Kniha Jízd</b></html>\nVerze produktu: 1.0\n\nvytvořil: Martin Saslík");// vyskakovací okýnko s popiskem 

            }
        });
        car.addActionListener(new java.awt.event.ActionListener() {// přepnutí zobrazených dat na auta

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new CarAdd().setVisible(true);
                mainGui.print_tabel(DbAccess.CAR);
            }
        });
        driver.addActionListener(new java.awt.event.ActionListener() {// přepnutí zobrazených dat na řidiče

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new DriverAdd().setVisible(true);
                mainGui.print_tabel(DbAccess.DRIVER);
            }
        });
        ride.addActionListener(new java.awt.event.ActionListener() {// přepnutí zobrazených dat na jízdy

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new RideAdd().setVisible(true);
                mainGui.print_tabel(DbAccess.RIDE); // databázový příkaz pro jízdy (je složitejší jelikoš propojuje všechny 3 tabulky a ze zbylích 2 vipisuje jen požadované hodnoty)
            }
        });

        statistikaCar.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                new Statistika(false).setVisible(true);
            }
        });
        
        statistikaDrivers.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                new Statistika(true).setVisible(true);
            }
        });

        // </editor-fold>



        JFrame frame = new JFrame();// inicializace frame 
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);// nastavení pro standartní ukončení
        frame.getContentPane().add(mainGui);
        frame.pack();
        frame.setLocation(
                (Toolkit.getDefaultToolkit().getScreenSize().height - frame.getSize().height) / 2,
                ((Toolkit.getDefaultToolkit().getScreenSize().width - frame.getSize().width) / 2) - 200);// nastavení umýstění
        frame.setSize(600, 500);// nastavení velikosti
        frame.setJMenuBar(menu);
        frame.setVisible(true); // zobrazení
        // */
    }
}