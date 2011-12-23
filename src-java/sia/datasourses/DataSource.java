package sia.datasourses;

import sia.fileparsers.IParser;
import sia.utils.ParserFactory;

public abstract class DataSource {
	protected String[] extensions = new String[] { "*.jpg;*.png" };
	protected String[][] descriptions = new String[][] { new String[] {
			"PNG file",
			"To implement the canFlipToNextPage method for the first page of our wizard, we first prevent the user from moving to the next page when the page has any errors. When there are no errors, the destination and departure fields are filled, the return date is set and a mode of transport is selected, the user can move to the next page." } };
	protected ParserFactory parserFactory;
	
	/**
	 * Returns accepted by parser file extensions
	 * 
	 * @return array of extensions
	 */
	public String[] getFileExtensions() {
		return extensions;
	}

	/**
	 * Returns descriptions 
	 * 
	 * @return array of arrays of decriptions
	 */
	public String[][] getDescriptions() {
		return descriptions;
	}
	
	/**
	 * Returns Parser
	 *  
	 * @return parser
	 */
	public IParser getParser() {
		return parserFactory.create();
		
	}
}
