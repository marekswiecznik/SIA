package sia.utils;

import java.util.HashMap;
import java.util.Map;

import org.sormula.Database;
import org.sormula.SormulaException;
import org.sormula.Table;

import sia.models.IModel;

/**
 * Object relational mapping.
 * 
 * @author jumper
 */
public class ORM {
	private Database database;
	private Map<String, Table<IModel>> dao;
	
	/**
	 * Default and only constructor
	 * @param database Sormula database
	 */
	public ORM(Database database) {
		this.database = database;
		this.dao = new HashMap<String, Table<IModel>>();
	}
	
	/**
	 * Create ORM table
	 * @param type
	 * @throws SormulaException
	 */
	public <T extends IModel> void createTable(Class<T> type) throws SormulaException {
		Table<T> table = database.getTable(type);
		dao.put(type.getName(), (Table<IModel>) table);
	}
	
	/**
	 * Get ORM table
	 * @param type
	 * @return ORM table
	 */
	public <T extends IModel> Table<T> getTable(Class<T> type) {
		return (Table<T>) dao.get(type.getName());
	}
}
