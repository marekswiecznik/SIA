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
	private Database databaseTemp;
	private Map<String, Table<Object>> dao;
	
	/**
	 * Default and only constructor
	 * @param database Sormula database
	 */
	public ORM(Database database, Database databaseTemp) {
		this.database = database;
		this.databaseTemp = databaseTemp;
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
	 * Create ORM temporary table
	 * @param type
	 * @throws SormulaException
	 */
	public <T> void createTempTable(Class<T> type) throws SormulaException {
		Table<T> table = databaseTemp.getTable(type);
		this.dao.put(type.getName()+"_temp", (Table<Object>) table);
	}
	
	/**
	 * Returns ORM table
	 * @param type
	 * @return ORM table
	 */
	public <T> Table<T> getTable(Class<T> type) {
		return (Table<T>) this.dao.get(type.getName());
	}
	
	/**
	 * Returns ORM table
	 * @param type
	 * @return ORM table
	 */
	public <T> Table<T> getTempTable(Class<T> type) {
		return (Table<T>) this.dao.get(type.getName()+"_temp");
	}
}
