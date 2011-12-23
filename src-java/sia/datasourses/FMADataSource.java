package sia.datasourses;

import sia.utils.ParserFactory;

public class FMADataSource extends DataSource {
	
	/**
	 * FMA Data source
	 * 
	 * Float's Mobile Agent (for Sony Ericsson)
	 */
	public FMADataSource() {
		extensions = new String[] {"*.xml" };
		descriptions = new String[][] {new String[] {"XML file", "SMS archive in XML"}};
		parserFactory = new ParserFactory("FmaParser");
	}
}
