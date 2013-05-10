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

public class DbAccess {// dřída zajistující přístup k DB

    public DbAccess() {//konstruktor
    }

    public DbAccess(boolean read, String prikaz) {// kontruktor pro okamžitý přístup
        if (read) {
            DbRead(prikaz);
        } else {
            DbWrite(prikaz);
        }
    }
    private String[] colum;// uložiště pro data vytažené z databáze
    private String[][] data;//

    public void DbRead(String prikaz) {// metoda pro čtení z databáze načte data podle příkazu a uloží do uložiště
        String url = "jdbc:derby:KJData";

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
        }
        data = arrayToPole(dataArray);

    }

    public String[][] getData() {//getr dat
        return data;
    }

    public String[] getColum() {// getr colum(jmena sloupečků)
        return colum;
    }

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
}
