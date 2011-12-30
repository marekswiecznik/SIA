package sia.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sormula.SormulaException;

import sia.datasourses.DataSource;
import sia.datasourses.FMADataSource;
import sia.models.Configuration;
import sia.models.Protocol;
import sia.ui.SIA;

/**
 * Dictionaries
 * 
 * @author jumper
 */
public class Dictionaries {
	private static Dictionaries instance;
	
	private Map<String, DataSource> dataSources;
	private Map<String, Protocol> protocols;
	private Map<String, Configuration> configuration;
	
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
		
		List<Configuration> configuration = orm.getTable(Configuration.class).selectAll();
		this.configuration = new HashMap<String, Configuration>();
		for (Configuration c : configuration)
			this.configuration.put(c.getKey(), c);
		
		List<Protocol> protocols = orm.getTable(Protocol.class).selectAll();
		this.protocols = new HashMap<String, Protocol>();
		for (Protocol p : protocols)
			this.protocols.put(p.getName(), p);
		
		dataSources = new HashMap<String, DataSource>();
		//dataSources.put("kadu", new KaduDataSource());
		dataSources.put("Float's Mobile Agent", new FMADataSource());
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

	public Map<String, Configuration> getConfiguration() {
		return configuration;
	}
}
