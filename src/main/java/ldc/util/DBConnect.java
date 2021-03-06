package ldc.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DBConnect {

	private static Connection conn = null;
	private static PreparedStatement Pstmt = null;
	private static Statement Stmt = null;
	private static ResultSet rs = null;

	/**
	 * commandType was exist for checking command type that were query or
	 * manipulate. 1 = Query, 2 = Manipulate (Insert, Update, Delete), 3 =
	 * Nothing (Default).
	 */
	private static int commandType = 3;

	public Connection getConnectMYSql() throws Exception, IOException {

		try {

			Class.forName("com.mysql.jdbc.Driver");
			// Class.forName ("org.gjt.mm.mysql.Driver");

			/**
			 * LOCALHOST
			 */
			/*String dbName = "smart_dental";
			String hostname = "localhost";
			String port = "3306";
			String dbUserName = "root";
			String dbPassword = "1234";*/


			/**
			 * SMART ICT (INTERNAL)
			 */
			String dbName = "smart_dental";
			String hostname = "192.168.1.233";
			String port = "3306";
			String dbUserName = "root";
			String dbPassword = "1234"; 

			/**
			 * SMART ICT (EXTERNAL)
			*/
			/*String dbName = "smart_dental"; 
			String hostname = "183.88.238.69"; 
			String port = "3233"; // 
			String dbUserName = "root"; 
			String dbPassword = "1234";*/
			 

			// String hostname =
			// "pcpnru.cre4njgwawzc.ap-southeast-1.rds.amazonaws.com"; // amazon
			// String dbPassword = "a8s5T5d4"; // amazon
			//

			/**
			 * SMARTICT.AR-BRO.NET
			 */
			/*
			 * String dbName = "smart_dental1"; String port = "3306"; String
			 * dbUserName = "root"; String hostname = "smartict.ar-bro.net"; //
			 * smart server String dbPassword = "a010103241c"; // smart server
			 */

			String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName
					+ "?useUnicode=yes&characterEncoding=UTF-8&user=" + dbUserName + "&password=" + dbPassword
					+ "&zeroDateTimeBehavior=convertToNull"
					+ "&allowMultiQueries=true";

			// jdbc:mysql://localhost/infra?zeroDateTimeBehavior=convertToNull

			conn = DriverManager.getConnection(jdbcUrl);

			return conn;

		} catch (ClassNotFoundException e) {
			throw new Exception("class not found " + e);
		} catch (SQLException se) {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ignore) {
				}
			throw new Exception(se);
		}

		// finally {
		// if (conn != null) try { conn.close(); } catch (SQLException ignore)
		// {}

		// }
	}

	/**
	 * TRANSACTION ZONE.
	 */
	public void begin() {
		try {
//			DBConnect.conn.setAutoCommit(false);
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			this.disconnectMySQL();
			e.printStackTrace();
		}
	}

	public void commit() {
		try {
			if (conn.isClosed()) {
				connectMySQL();
				begin();
			}
			conn.commit();
		} catch (SQLException e) {
			this.disconnectMySQL();
			System.out.println("Can't commit right now \n Please see exception stack trace below : ");
			e.printStackTrace();
		}
	}

	public void rollback() {
		try {
			if (conn.isClosed()) {
				connectMySQL();
				begin();
			}
			conn.rollback();
		} catch (SQLException e) {
			this.disconnectMySQL();
			System.out.println("Can't rollback right now \n Please see exception stack trace below : ");
			e.printStackTrace();
		}
	}
	
	public Statement getNewStatement(){
		try {
			return conn.createStatement();
		} catch (SQLException e) {
			System.out.println("Can't create new statement.");
			e.printStackTrace();
		} finally {
			rollback();
			disconnectMySQL();
		}
		return null;
	}

	public PreparedStatement getNewPrepareStatement(String SQL){
		try {
			return conn.prepareStatement(SQL);
		} catch (SQLException e) {
			System.out.println("Can't create new prepare statement.");
			e.printStackTrace();
		} finally {
			rollback();
			disconnectMySQL();
		}
		return null;
	}
	
	
	
	// end

	/**
	 * connecting to mysql.
	 * 
	 * @author wesarut
	 * @return boolean | if connection succes then return true.
	 */
	public boolean connectMySQL() {
		try {
			getConnectMYSql();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Close the connection.
	 * 
	 * @author wesarut
	 * @return boolean | if success then return true.
	 */
	public boolean disconnectMySQL() {
		try {

			if (commandType == 2) {
				if (!Pstmt.isClosed())
					Pstmt.close();
			} else if (commandType == 1) {
				if (!Stmt.isClosed())
					Stmt.close();
				if (!rs.isClosed())
					rs.close();
			}

			if (!conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * EXECUTION ZONE.
	 */

	/**
	 * Checking out for exist item.
	 * 
	 * @author anubissmile
	 * @param table
	 * @param where
	 * @return int | Count of row of query.
	 */
	public int isExist(String table, String where) {
		String SQL = "SELECT * FROM " + table + " WHERE " + where;
		// System.out.println(SQL);

		connectMySQL();
		try {
			Stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = Stmt.executeQuery(SQL);
			commandType = 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int rec = size();
		disconnectMySQL();
		return rec;
	}
	

	/**
	 * Execute the SQL manipulate command for insert, update, delete.
	 * 
	 * @author wesarut
	 * @param SQL | String of SQL commands.
	 * @return integer | either (1) the row count for SQL Data Manipulation
	 *         Language (DML) statements or (2) 0 for SQL statements that return
	 *         nothing.
	 */
	public int exeUpdate(String SQL) {
		try {
			Pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
			commandType = 2;
//  			return Pstmt.executeUpdate();
			int numRow = Pstmt.executeUpdate();
			rs = Pstmt.getGeneratedKeys();
			return numRow;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * Execute the Query command
	 * @author wesarut
	 * @param SQL | The String of SQL command.
	 * @return Object ResultSet | return the result record set.
	 */
	public ResultSet exeQuery(String SQL) {
		try {
			Stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = Stmt.executeQuery(SQL);
			commandType = 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rs;
	}

	/**
	 * Find count of records in result set.
	 * 
	 * @author anubissmile
	 * @return int | Size of records in result set.
	 */
	public int size() {
		int row = 0;
		try {
			if (rs.last()) {
				row = rs.getRow();
				rs.beforeFirst();
			}
			return row;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return row;
	}

	/**
	 * GETTER SETTER ZONE
	 * 
	 * @return
	 */
	public ResultSet getRs() {
		return rs;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}
}
