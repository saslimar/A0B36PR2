package pr2_kniha_jizd.database;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class DbAccess implements DbAccessInterface {// dřída zajistující přístup k DB

    private String[] colum;// uložiště pro data vytažené z databáze
    private String[][] data;//
    private String details = "";

    public DbAccess() {//konstruktor
    }

    // <editor-fold defaultstate="collapsed" desc="DB READ">
    public String getDetails() {
        String s = details;
        details = "";
        return s;
    }

    public void DbReadException(String s1, String s2, String s3) {
        if (s1 != null && s2 != null && s3 != null) {
            DbRead("select * from APP.DRIVER WHERE JMENO='" + s1 + "' and PRIJMENI='" + s2 + "' and DATUM_NAROZENI ='" + s3 + "'");
        } else {
            DbRead("SELECT SPZ,CarID FROM app.CAR");
        }
    }

    public void DbRead(int i, int id) {
        DbRead("SELECT * FROM APP." + getStringOfConstrant(i) + " WHERE " + getStringOfConstrant(i) + "ID = " + id);
    }

    public void getToCar() {
        String s = "SELECT APP.CAR.ZNACKA,APP.CAR.SPZ ,APP.CAR.CARID AS VYUZITO FROM APP.CAR";//"SELECT ZNACKA AS \"ZNACKA/JMENO\",SPZ AS\"SPZ/DATUM\",CARID AS\"VYUZITO\" FROM APP.CAR;";
        DbRead(s);
        String[] colum = this.getColum();
        String[][] datCar = this.getData();
        //     System.out.println(colum.length);
        for (int x = 0; x < datCar.length; x++) {
            DbRead("SELECT COUNT(*) AS Rides FROM APP.RIDE WHERE CARID =" + datCar[x][2]);
            datCar[x][2] = (getData()[0][0] + " x");
        }
        this.colum = colum;
        this.data = datCar;
    }

    public void getToDriver() {// konstruktor načte nazvy 2 tabulek vytahne z nich data spracuje je a zobrazí
        String s = "SELECT (APP.DRIVER.PRIJMENI||' '||APP.DRIVER.jmeno) ,APP.DRIVER.DATUM_NAROZENI ,APP.DRIVER.DRIVERID AS VYUZITO from APP.DRIVER";//"SELECT ZNACKA AS \"ZNACKA/JMENO\",SPZ AS\"SPZ/DATUM\",CARID AS\"VYUZITO\" FROM APP.CAR;";
        DbRead(s);
        String[] colum = this.getColum();
        String[][] datDriver = getData();
        //  System.out.println(colum.length);
        for (int x = 0; x < datDriver.length; x++) {
            DbRead("SELECT COUNT(*) AS Rides FROM APP.RIDE WHERE DRIVERID =" + datDriver[x][2]);
            datDriver[x][2] = (getData()[0][0] + " x");
        }
        this.colum = colum;
        this.data = datDriver;
    }

    public void DbReadRideEdit(int i, boolean cislo) {
        if (i == CAR) {
            if (cislo) {
                DbRead("select CARID from APP.CAR");
            } else {
                DbRead("select SPZ from APP.CAR");
            }
        }
        if (i == DRIVER) {
            if (cislo) {
                DbRead("select DRIVERID from APP.DRIVER");
            } else {

                DbRead("select PRIJMENI||'.'||substr(JMENO, 1, 1) from APP.DRIVER");
            }
        }
    }

    public void DbReadForDelete(int i, int id) {
        DbRead("SELECT * FROM app.RIDE WHERE " + getStringOfConstrant(i) + "ID =" + id);

    }

    public void DbReadDetails(int i, int id) {// vytáhne data a seřazené jakodetaily uloží do stringu
        String prikaz = "select * from APP." + getStringOfConstrant(i) + " where " + getStringOfConstrant(i) + "ID = " + id;
        DbRead(prikaz);
        String[] columP = colum;// uložiště pro data vytažené z databáze
        String[][] dataP = data;//


        if (i == CAR) {
            details += printCar(id);
            DbRead("select RIDEID,DRIVERID from APP.ride where CARID =" + id);
            String[][] rideId = this.data;
            if (rideId != null) {
                for (int x = 0; x < rideId.length; x++) {
                    details += printRide(Integer.parseInt(rideId[x][0]));
                    details += printDriver(Integer.parseInt(rideId[x][1]));
                }
            }
        }
        if (i == DRIVER) {
            details += printDriver(id);
            DbRead("select RIDEID,CARID from APP.ride where DRIVERID =" + id);
            String[][] rideId = this.data;
            if (rideId != null) {
                for (int x = 0; x < rideId.length; x++) {
                    details += printRide(Integer.parseInt(rideId[x][0]));
                    details += printCar(Integer.parseInt(rideId[x][1]));
                }
            }
        }
        if (i == RIDE) {
            details += printRide(id);
            details += printDriver(Integer.parseInt(dataP[0][dataP[0].length - 2]));
            details += printCar(Integer.parseInt(dataP[0][dataP[0].length - 1]));

        }
    }

    public void DbRead(int i) {// print full tabel
        if (i == CAR) {
            DbRead("select * from APP.CAR");
        }
        if (i == DRIVER) {
            DbRead("select * from APP.DRIVER");
        }
        if (i == RIDE) {
            DbRead("SELECT RIDEID,DATUM_CESTY,ODKUD,KAM,DUVOD_CESTY,VZDALENOST,SPOTREBA ,"
                    + "PRIJMENI||'.'||substr(JMENO, 1, 1) AS DRIVER, SPZ AS AUTO FROM APP.RIDE JOIN"
                    + " APP.DRIVER ON APP.DRIVER.DRIVERID = APP.RIDE.DRIVERID join APP.CAR on "
                    + "APP.CAR.CARID = app.RIDE.CARID");
        }
    }

    public void DbRead(int i, String search) {// nactení hodnost s vyhledanym stringem v tabulcee i
        String dotaz = "";
        if (i == CAR) {
            dotaz = "select * from APP.CAR WHERE "
                    + "upper(ZNACKA) like upper('%" + search + "%') or "
                    + "upper(SPZ) like upper('%" + search + "%') or "
                    + "upper(DRUH) like upper('%" + search + "%')";// ošklivé db příkazy uspusobene k vihledávání 
        }
        if (i == DRIVER) {
            dotaz = "select * from APP.DRIVER WHERE "
                    + "upper(JMENO) like upper('%" + search + "%') or "
                    + "upper(PRIJMENI) like upper('%" + search + "%') or "
                    + "upper(DATUM_NAROZENI) like upper('%" + search + "%')";// ošklivé db příkazy uspusobene k vihledávání 


        }
        if (i == RIDE) {
            dotaz = "select RIDEID,DATUM_CESTY,ODKUD,KAM,DUVOD_CESTY,VZDALENOST,SPOTREBA ,"
                    + "PRIJMENI||'.'||substr(JMENO, 1, 1) AS DRIVER, SPZ AS AUTO FROM APP.RIDE JOIN"
                    + " APP.DRIVER ON APP.DRIVER.DRIVERID = APP.RIDE.DRIVERID join APP.CAR on "
                    + "APP.CAR.CARID = app.RIDE.CARID WHERE "
                    + "upper(ODKUD) like upper('%" + search + "%') or "
                    + "upper(KAM) like upper('%" + search + "%') or "
                    + "upper(DUVOD_CESTY) like upper('%" + search + "%') or "
                    + "upper(PRIJMENI||'.'||substr(JMENO, 1, 1)) like upper('%" + search + "%') or "
                    + "upper(SPZ) like upper('%" + search + "%')";// ošklivé db příkazy uspusobene k vihledávání 

            boolean addInt = false;
            try {
                Integer test = Integer.parseInt(search);
                addInt = true;
            } catch (NumberFormatException e) {
            }
            if (addInt) {
                dotaz += "or VZDALENOST = " + search + " or "
                        + "SPOTREBA = " + search;
            }
        }
        DbRead(dotaz);
    }

    public void DbRead(String prikaz) {// metoda pro čtení z databáze načte data podle příkazu a uloží do uložiště
        //     System.out.println(prikaz);

        String url = "jdbc:derby:KJData";
        boolean ukoncit = false;
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(DbAccess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DbAccess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        ArrayList<String[]> dataArray = new ArrayList<String[]>();
        try {
            conn = DriverManager.getConnection(url, "user", "123456");
            st = conn.createStatement();
            rs = st.executeQuery(prikaz);

            ResultSetMetaData m = rs.getMetaData();
            colum = new String[m.getColumnCount()];
            colum[0] = "#";
            for (int i = 2; i <= m.getColumnCount(); i++) {
                colum[i - 1] = m.getColumnName(i);
            }
            while (rs.next()) {
                String[] data1 = new String[m.getColumnCount()];
                for (int i = 1; i <= m.getColumnCount(); i++) {
                    data1[i - 1] = rs.getString(i);
                }
                dataArray.add(data1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Chyba v databázy. \nProgram bude ukončem.");
            //         System.out.println(prikaz);
            ukoncit = true;
        } finally {

            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DbAccess.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DbAccess.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DbAccess.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (ukoncit) {
                System.exit(0);
            }
        }
        data = arrayToPole(dataArray);

    }

    public String[][] getData() {//getr dat
        return data;
    }

    public String[] getColum() {// getr colum(jmena sloupečků)
        return colum;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="DB WRITE">
    public void DbWriteDriverEdit(String jmeno, String prijmeni, String datum, int select) {
        String prikaz = "UPDATE \"APP\".\"DRIVER\" "
                + "set JMENO='" + jmeno + "',"
                + "PRIJMENI='" + prijmeni + "',"
                + "DATUM_NAROZENI={d '" + datum + "'} "
                + "WHERE DRIVERID = " + select;
        DbWrite(prikaz);
    }

 public void DbWriteDriverAdd(String jmeno, String prijmeni, String datum) {
        String prikaz = "INSERT INTO \"APP\".\"DRIVER\"(JMENO,PRIJMENI,DATUM_NAROZENI)"
                + "VALUES("
                + "'" + jmeno + "',"
                + "'" + prijmeni + "',"
                + "{d '" + datum + "'})";
        DbWrite(prikaz);
    }
    public void DbWriteRideEdit(String datum, String odkud, String kam, String duvod,
            Integer vzdalenost, Integer spotreba, Integer driverId, Integer carId, int select) {
        String prikaz = "UPDATE \"APP\".\"RIDE\"set DATUM_CESTY='" + datum + "'"
                + ",ODKUD='" + odkud + "'"
                + ",kam='" + kam + "'"
                + ",DUVOD_CESTY='" + duvod + "'"
                + ",VZDALENOST=" + vzdalenost + ""
                + ",SPOTREBA=" + spotreba + ""
                + ",DRIVERID=" + driverId + ""
                + ",CARID=" + carId + ""
                + " where RIDEID =" + select;
        DbWrite(prikaz);
    }

    public void DbWriteRideAdd(String datum, String odkud, String kam, String duvod,
            Integer vzdalenost, Integer spotreba, Integer driverId, Integer carId) {
        String prikaz = "INSERT INTO \"APP\".\"RIDE\""
                + "(DATUM_CESTY,ODKUD,KAM,DUVOD_CESTY,VZDALENOST,SPOTREBA,DRIVERID,CARID)"
                + "VALUES('" + datum + "',"
                + "'" + odkud + "',"
                + "'" + kam + "',"
                + "'" + duvod + "'"
                + "," + vzdalenost + ","
                + "" + spotreba + ","
                + "" + driverId + ","
                + "" + carId + ""
                + ")";
        DbWrite(prikaz);
    }

    public void DbWriteCarAdd(String znacka, String spz, String druh, String firemni) {
        String prikaz = "INSERT INTO \"APP\".\"CAR\" (znacka,spz,druh,firemni)VALUES('"
                + znacka
                + "','" + spz + "',"
                + druh + ","
                + "'" + firemni + "')";
        DbWrite(prikaz);

    }

    public void DbWriteCarEdit(String znacka, String spz, String druh, String firemni, int select) {
        String prikaz = "UPDATE \"APP\".\"CAR\" "
                + "set ZNACKA='" + znacka + "',"
                + "SPZ='" + spz + "',"
                + "DRUH=" + druh + " "
                + ",firemni = '" + firemni + "'"
                + "WHERE CARID =" + select;
        DbWrite(prikaz);
    }

    public void DbWriteDelete(int i, int id) {
        String prikaz = "DELETE FROM app." + getStringOfConstrant(i) + "  WHERE " + getStringOfConstrant(i) + "ID=" + id;
        DbWrite(prikaz);
    }

    public void DbWriteDeleteRide(int i, int id) {
        String prikaz = "DELETE FROM app.RIDE WHERE " + getStringOfConstrant(i) + "ID =" + id;
        DbWrite(prikaz);
    }

    public void DbWrite(String prikaz) {// metoda pro zapis do databáze pouze otevře spojení , odešle příkaz a uzavře spojení
        String url = "jdbc:derby:KJData";

        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbAccess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(DbAccess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DbAccess.class.getName()).log(Level.SEVERE, null, ex);
        }


        Connection conn = null;
        Statement st = null;
        try {
            conn = DriverManager.getConnection(url, "user", "123456");

            st = conn.createStatement();
            st.executeUpdate(prikaz);
        } catch (SQLException ex) {
            Logger.getLogger(DbAccess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DbAccess.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DbAccess.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Support methods">

    private String[][] arrayToPole(ArrayList<String[]> list) {// metoda kopírujídí ArrayList<String[]>  do dbvourozmnerného pole
        if (!list.isEmpty()) {
            String[][] data = new String[list.size()][list.get(0).length];
            int y = 0;
            for (String[] k : list) {
                data[y] = k;
                y++;
            }
            return data;
        } else {
            return null;
        }
    }

    private String getStringOfConstrant(int i) {
        if (i == CAR) {
            return "CAR";
        }
        if (i == DRIVER) {
            return "DRIVER";
        }
        if (i == RIDE) {
            return "RIDE";
        }
        return null;
    }

    private String printDriver(Integer id) {// vipíše data o řidičovy s ID
        String s = "";
        DbRead("select * from APP.DRIVER where DRIVERID =" + id);
        String[] colum = this.getColum();
        String[][] data = this.getData();
        s += ("ŘIDIČ\n_________________\n");
        for (int x = 1; x < 4; x++) {
            String data_ = data[0][x];
            if (data[0][x] == null) {
                data_ = "nevedeno";
            }
            s += (colum[x] + " : " + data_ + "\n");
        }
        s += ("\n");
        return s;

    }

    private String printCar(Integer id) {// vipíše data o autě s ID
        String s = "";
        DbRead("select * from APP.CAR where CARID =" + id);
        String[] colum = this.getColum();
        String[][] data = this.getData();
        s += ("AUTO\n_________________\n");
        for (int x = 1; x < 5; x++) {
            String data_ = data[0][x];
            if (data[0][x] == null) {
                data_ = "nevedeno";
            }
            s += (colum[x] + " : " + data_ + "\n");
        }
        s += ("\n");
        return s;

    }

    private String printRide(Integer id) {// vipíše data o jízdě s ID
        String s = "";
        DbRead("select * from APP.ride where RIDEID =" + id);
        String[] colum = this.getColum();
        String[][] data = this.getData();
        s += "Jízda\n_________________\n";
//        text.setText(text.getText() + "Jízda\n_________________\n");
        for (int x = 1; x < 6; x++) {
            String data_ = data[0][x];
            if (data[0][x] == null) {
                data_ = "nevedeno";
            }
            s += (colum[x] + " : " + data_ + "\n");

            //   text.setText(text.getText() + colum[x] + " : " + data_ + "\n");
        }
        s += "\n";

        //  text.setText(text.getText() + "\n");
        return s;
    }
    // </editor-fold>   
}