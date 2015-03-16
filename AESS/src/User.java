import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User {
	protected String id;
	protected String password;
	
	protected Connection conn = null;
	protected Statement query = null;
	protected ResultSet result = null;
	protected String qry;
	
	public User(String id, Connection conn){
		this.id = id;
		this.conn = conn;
		try {
			this.query = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
