package pr2_kniha_jizd;
import javax.swing.JDialog;  
public class Statistika extends JDialog{
    private TabelPanel tabulka;
    public Statistika() {
        print();
    }
    private void print(){
        
       tabulka = new TabelPanel("CAR","DRIVER"); // vytvoření tabulky vstupní jako vstup sou názvy 2 tabulek
        this.add(tabulka);    
        
        this.setResizable(false);
        this.setModal(true);
        this.pack();
        this.setSize(600, 600);
        this.setLocation(WIDTH, HEIGHT);
        setLocationRelativeTo(getRootPane());
    
    }    
}
