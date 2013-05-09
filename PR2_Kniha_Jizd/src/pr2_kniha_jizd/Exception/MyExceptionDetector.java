package pr2_kniha_jizd.Exception;

import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JTextField;
import pr2_kniha_jizd.database.DbRead;

public class MyExceptionDetector {// třída kontrolující chiby

    public static final int CAR_ADD = 1;
    public static final int CAR_EDIT = 2;
    public static final int DRIVER_ADD = 3;
    public static final int DRIVER_EDIT = 4;
    public static final int RIDE_ADD_EDIT = 5; // konstanty určující o jakou kontrolu jde
    private int select;

    public MyExceptionDetector() throws MyException {// konstruktory
        throw (new MyException());
    }

    public MyExceptionDetector(ArrayList<JTextField> text, int select, int exceptionconstants) throws MyException {
        this.select = select;
        ExceptionDetect(text, exceptionconstants);
    }

    public MyExceptionDetector(ArrayList<JTextField> text, int exceptionconstants) throws MyException {
        ExceptionDetect(text, exceptionconstants);
    }

    private void ExceptionDetect(ArrayList<JTextField> text, int exceptionconstants) throws MyException {

        if (exceptionconstants == RIDE_ADD_EDIT) {// kontrola jednotlivých chyb podle zadanách konstant
            MyException ex = new MyException();// vytvožím si instanci své chybi
            for (JTextField tf : text) {
                if ("".equals(tf.getText())) {// nejsou li informace spracne
                    ex.addException("neuvedeny informace ( " + tf.getName() + " )"); // přidám své chybje zaznam o tom co je špatně
                    tf.setBackground(Color.RED);
                }
            }
            try {
                new SimpleDateFormat("yyyy-MM-dd").parse(text.get(0).getText());
            } catch (ParseException ee) {
                ex.addException("datum bylo zadáno ve špatném formátu zadejte prosím (yyyy-MM-dd)");
                text.get(0).setBackground(Color.RED);
            }
            if (ex.isException()) {// pokud mam v chybje zaznam o chybje 
                throw ex;// vyhodim jí
            }
        }
        if (exceptionconstants == DRIVER_ADD || exceptionconstants == DRIVER_EDIT) {
            MyException ex = new MyException();
            for (JTextField tf : text) {
                if ("".equals(tf.getText())) {
                    ex.addException("neuvedeny informace ( " + tf.getName() + " )");
                    tf.setBackground(Color.RED);
                }
            }
            try {
                new SimpleDateFormat("yyyy-MM-dd").parse(text.get(2).getText());
            } catch (ParseException ee) {
                ex.addException("datum bylo zadáno ve špatném formátu zadejte prosím (yyyy-MM-dd)");
                text.get(2).setBackground(Color.RED);
            }
            if (ex.isException()) {
                throw ex;
            }
            String[][] data = new DbRead("select * from APP.DRIVER WHERE JMENO='" + text.get(0).getText() + "' and PRIJMENI='" + text.get(1).getText() + "' and DATUM_NAROZENI ='" + text.get(2).getText() + "'").getData();
            if (data != null) {
                if (exceptionconstants == DRIVER_EDIT) {
                    if (Integer.parseInt(data[0][0]) != select) {
                        ex.setShow(true);
                        ex.addException("Tato osoba již je v seznamu!");
                    }
                } else {
                    ex.setShow(true);
                    ex.addException("Tato osoba již je v seznamu!");
                }
            }
            if (ex.isException()) {
                throw ex;
            }
        }
        boolean carEdit = false;
        if (exceptionconstants == CAR_EDIT) {
            carEdit = true;
        }
        if (exceptionconstants == CAR_ADD || exceptionconstants == CAR_EDIT) {
            MyException ex = new MyException();
            for (JTextField tf : text) {
                if ("".equals(tf.getText())) {
                    ex.addException("neuvedeny informace ( " + tf.getName() + " )");
                    tf.setBackground(Color.RED);
                }
            }
            if (ex.isException()) {
                throw ex;
            }
            String[][] data = new DbRead("SELECT SPZ,CarID FROM app.CAR").getData();
            if (data != null) {
                for (int x = 0; x < data.length; x++) {
                    if (data[x][0].equals(text.get(0).getText())) {
                        if (carEdit && (Integer.parseInt(data[x][1]) == select)) {
                        } else {
                            ex.setShow(true);
                            text.get(0).setBackground(Color.RED);
                            ex.addException("tato spz uš je v seznamu!");
                        }
                    }
                }
                if (ex.isException()) {
                    throw ex;
                }
            }
        }



    }
}
