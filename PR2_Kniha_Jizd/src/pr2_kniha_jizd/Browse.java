package pr2_kniha_jizd;

import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Browse extends JInternalFrame {

    private JFileChooser chooser;

    public String saveDialog() {
        chooser = new JFileChooser();
        chooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt", "txt");



        chooser.addChoosableFileFilter(filter);
        chooser.setCurrentDirectory(new java.io.File("*.*"));
        chooser.setDialogTitle("Vyberte adresář pro uložení");
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            return String.valueOf(chooser.getSelectedFile());
        } else {
            return "";
        }

    }

    public String loadDialog() {
        chooser = new JFileChooser();
        chooser.setAcceptAllFileFilterUsed(true);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt", "txt");
        chooser.addChoosableFileFilter(filter);


        chooser.setCurrentDirectory(new java.io.File("*.*"));
        chooser.setDialogTitle("Vyberte soubor s databází");
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        chooser.setFileSelectionMode(JFileChooser.OPEN_DIALOG);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            return String.valueOf(chooser.getSelectedFile());
        } else {
            return "";
        }
    }
}