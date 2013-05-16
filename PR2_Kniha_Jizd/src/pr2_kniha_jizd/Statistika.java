package pr2_kniha_jizd;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class Statistika extends JDialog {

    private TabelPanel tabulkaCar;
    private TabelPanel tabulkaDriver;

    public Statistika(boolean Drivers) {
        if (Drivers) {
            printDrivers();
        } else {
            printCars();
        }
        this.setResizable(false);
        this.setModal(true);
        this.pack();
        this.setSize(600, 600);
        this.setLocation(WIDTH, HEIGHT);
        setLocationRelativeTo(getRootPane());

    }

    private void print() {

        tabulkaCar = new TabelPanel(); // vytvoření tabulky vstupní jako vstup sou názvy 2 tabulek
        tabulkaCar.setToCar();
        this.add(tabulkaCar);

    }

    private void printDrivers() {
        tabulkaDriver = new TabelPanel(); // vytvoření tabulky vstupní jako vstup sou názvy 2 tabulek
        tabulkaDriver.setToDriver();
        this.add(tabulkaDriver);
    }

    private void printCars() {
        JPanel tabulky = new JPanel(new FlowLayout());
        tabulkaCar = new TabelPanel(); // vytvoření tabulky vstupní jako vstup sou názvy 2 tabulek
        tabulkaCar.setToCar();
        this.add(tabulkaCar);
    }
}
