package sia.utils;

import java.util.HashMap;
import java.util.Map;

import org.sormula.Database;
import org.sormula.SormulaException;
import org.sormula.Table;

/**
 * Object relational mapping.
 * 
 * @author jumper
 */
public class ORM {
	private Database database;
	private Map<String, Table<Object>> dao;
	
	/**
	 * Default and only constructor
	 * @param database Sormula database
	 */
	public ORM(Database database) {
		this.database = database;
		this.dao = new HashMap<String, Table<Object>>();
	}
	
	/**
	 * Create ORM table
	 * @param type
	 * @throws SormulaException
	 */
	public <T> void createTable(Class<T> type) throws SormulaException {
		Table<T> table = database.getTable(type);
		this.dao.put(type.getName(), (Table<Object>) table);
	}
	
	/**
	 * Returns ORM table
	 * @param type
	 * @return ORM table
	 */
	public <T> Table<T> getTable(Class<T> type) {
		return (Table<T>) this.dao.get(type.getName());
	}
}
