package sia.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sormula.SormulaException;

import sia.SIA;
import sia.datasources.DataSource;
import sia.datasources.ExampleDataSource;
import sia.datasources.FMADataSource;
import sia.models.Protocol;
//import sia.datasources.*;

/**
 * Dictionaries
 * 
 * @author jumper
 */
public class Dictionaries {
	private static Dictionaries instance;
	
	private Map<String, DataSource> dataSources;
	private Map<String, Protocol> protocols;
	
	/**
	 * Default constructor
	 */
	private Dictionaries() { }
	
	/**
	 * Load dictionaries
	 * @throws SormulaException 
	 */
	public void init() throws SormulaException {
		ORM orm = SIA.getORM();
		
		List<Protocol> protocols = orm.getTable(Protocol.class).selectAll();
		this.protocols = new HashMap<String, Protocol>();
		for (Protocol p : protocols)
			this.protocols.put(p.getName(), p);
		
		dataSources = new HashMap<String, DataSource>();
		//dataSources.put("kadu", new KaduDataSource());
		dataSources.put("Float's Mobile Agent", new FMADataSource());
		dataSources.put("Example", new ExampleDataSource());
	}
	
	/**
	 * Returns data sources dictionary
	 * @return data sources dictionary
	 */
	public Map<String, DataSource> getDataSources() {
		return dataSources;
	}
	
	/**
	 * Returns data source for given key
	 * @param key
	 * @return data source
	 */
	public DataSource getDataSource(String key) {
		return dataSources.get(key);
	}
	
	/**
	 * Returns protocols dictionary
	 * @return protocols dictionary
	 */
	public Map<String, Protocol> getProtocols() {
		return protocols;
	}
	
	/**
	 * Returns protocol for given key
	 * @param key
	 * @return protocol
	 */
	public Protocol getProtocol(String key) {
		return protocols.get(key);
	}
	
	/**
	 * Returns instance
	 * @return dictionaries
	 */
	public static Dictionaries getInstance() {
		if (instance == null)
			instance = new Dictionaries();
		return instance;
	}
}
