package sia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.sormula.Database;
import org.sormula.SormulaException;

import sia.models.Configuration;
import sia.models.Contact;
import sia.models.ContactAccount;
import sia.models.Conversation;
import sia.models.Message;
import sia.models.Protocol;
import sia.models.UserAccount;
import sia.utils.ORM;

public class SIA {
	public static SIA instance;

	private Connection connection;
	private ORM orm;
	
	/**
	 * Initialize database and GUI
	 */
	public void init() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:sia.db");
			Database database = new Database(connection, "main");
			orm = new ORM(database);
			orm.createTable(Configuration.class);
			orm.createTable(Contact.class);
			orm.createTable(ContactAccount.class);
			orm.createTable(Conversation.class);
			orm.createTable(Message.class);
			orm.createTable(Protocol.class);
			orm.createTable(UserAccount.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SormulaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Get SIA instance
	 * @return SIA
	 */
	public static SIA getInstance() {
		if (instance == null)
			instance = new SIA();
		return instance;
	}

	/**
	 * Get database connection
	 * @return database connection
	 */
	public static Connection getConnection() {
		return getInstance().connection;
	}

	/**
	 * Get ORM
	 * @return ORM
	 */
	public static ORM getORM() {
		return getInstance().orm;
	}
	
	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		SIA.getInstance().init();
	}
}
