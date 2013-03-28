package pr2_kniha_jizd.database;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbWrite {

    public DbWrite(String prikaz) {
        String url = "jdbc:derby:KJData";

        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbWrite.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(DbWrite.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DbWrite.class.getName()).log(Level.SEVERE, null, ex);
        }


        Connection conn = null;
        Statement st = null;
        try {
            conn = DriverManager.getConnection(url, "user", "123456");

            st = conn.createStatement();
            st.executeUpdate(prikaz);
        } catch (SQLException ex) {
            Logger.getLogger(DbWrite.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DbWrite.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DbWrite.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
