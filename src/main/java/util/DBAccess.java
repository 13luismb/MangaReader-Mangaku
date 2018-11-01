package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class DBAccess {
	private Timestamp ts;
	private Connection con;
	private PreparedStatement pstm;
	private ResultSet rs;
	private ResultSetMetaData rsmd;
	private int res;
        
	public DBAccess(String driver, String url, String usr, String pwd) {
		try {
                    Class.forName(driver);
                    con = DriverManager.getConnection(url, usr, pwd);
		} catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
		}
	}
	
	//Select simples para comprobaciones
	public ResultSet execute(String query, Object... values) throws SQLException {
		try {
			this.pstm = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			for (int i = 0; i < values.length; i++) {
				this.pstm.setObject(i + 1, values[i]);
			}
			this.rs = this.pstm.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this.rs;
	}
	
	//Sentencias de modificaciones a la DB
	public int update(String query, Object... values) throws SQLException {
		try {
			this.pstm = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			for (int i = 0; i < values.length; i++) {
				this.pstm.setObject(i + 1, values[i]);
			}
				this.res = this.pstm.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return this.res; 
	}
        
        public void multiUpdate(String query, List<Integer> list,int id) throws SQLException {
                try {
                    this.pstm = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    for (int i = 0; i < list.size(); i++) {
                        this.pstm.setObject(1, list.get(i));
                        this.pstm.setObject(2, id);
                        this.pstm.addBatch();
                    }
                    this.pstm.executeBatch();
		} catch (SQLException e) {
                    e.printStackTrace();
		}
        }
        
        public Timestamp currentTimestamp(){
            ts = new Timestamp(System.currentTimeMillis());
            return ts;
        }
	
	//Cerrar conexiones
	public void close() {
		try {
			this.con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
