package sia.ui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.sormula.Database;
import org.sormula.SormulaException;

import sia.models.*;
import sia.ui.Start;
import sia.utils.Dictionaries;
import sia.utils.ORM;

public class SIA {
	public static SIA instance;

	private Connection connection;
	private ORM orm;
	private Start window;
	
	/**
	 * Initialize database and GUI
	 */
	public void init() {
		String error = null;
		try {
			dbInit("sia.db");
			Dictionaries.getInstance().init();
			guiInit();
		} catch (SQLException e) {
			error = e.getLocalizedMessage();
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			error = e.getLocalizedMessage();
			e.printStackTrace();
		} catch (SormulaException e) {
			error = e.getLocalizedMessage();
			e.printStackTrace();
		} catch (Exception e) {
			error = e.getLocalizedMessage();
			e.printStackTrace();
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					error += "\n" + e.getLocalizedMessage();
					e.printStackTrace();
				}
		}
		if (error != null) {
			MessageDialog.openError(new Shell(Display.getCurrent()), "Error", error);
		}
	}
	
	/**
	 * Database init
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 * @throws SormulaException 
	 */
	public void dbInit(String dbPath) throws ClassNotFoundException, SQLException, SormulaException {
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:"+dbPath);
		Database database = new Database(connection, "main");
		orm = new ORM(database);
		orm.createTable(Configuration.class);
		orm.createTable(Contact.class);
		orm.createTable(ContactAccount.class);
		orm.createTable(Conversation.class);
		orm.createTable(Message.class);
		orm.createTable(Protocol.class);
		orm.createTable(UserAccount.class);
	}

	/**
	 * Close database connection
	 * @throws SQLException
	 */
	public void dbClose() throws SQLException {
		connection.close();
	}
	
	/**
	 * GUI init
	 */
	public void guiInit() {
		window = new Start();
		window.run();
	}
	
	/**
	 * Returns SIA instance
	 * @return SIA
	 */
	public static SIA getInstance() {
		if (instance == null)
			instance = new SIA();
		return instance;
	}

	/**
	 * Returns database connection
	 * @return database connection
	 */
	public static Connection getConnection() {
		return getInstance().connection;
	}

	/**
	 * Returns ORM
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
