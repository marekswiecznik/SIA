package sia.test.py.fileparsers.KaduParser;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.python.core.PyException;
import sia.fileparsers.Parser;
import sia.ui.SIA;
import sia.utils.Dictionaries;
import sia.utils.ParserFactory;

/**
 * 
 * @author Agnieszka Glabala
 *
 */
public class KaduParserTest {
	private Parser parser;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		SIA.getInstance().dbInit("test/sia.db");
		Dictionaries.getInstance().init();
		parser = new ParserFactory("KaduParser").create();
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		SIA.getInstance().close(null);
	}
	
	@Test
	public void testLoadFilesEmptyArray() throws Exception {
		try {
			parser.loadFiles(new String[] {});
			fail("Shouldn't get so far.");
		} catch (PyException e) {
			assertEquals("Unexpected error type", "exceptions.IndexError", PyException.exceptionClassName(e.type));
			assertTrue("Unexpected IOError argument", e.value.toString().indexOf("index out of range: 0") == 12);
		}
	}
	
	@Test
	public void testLoadFilesOk() throws Exception {
		try {
			parser.loadFiles(new String[] {"test/sia/test/py/fileparsers/KaduParser"});	
		} catch (PyException e) {
			fail("Unexpected error");
		}
	}
	
	@Test
	public void test3useraccount() {
		
	}
	
}
