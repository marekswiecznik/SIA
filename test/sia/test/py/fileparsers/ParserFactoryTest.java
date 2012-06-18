package sia.test.py.fileparsers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.python.core.PyException;

import sia.fileparsers.Parser;
import sia.ui.SIA;
import sia.utils.Dictionaries;
import sia.utils.ParserFactory;

public class ParserFactoryTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		SIA.getInstance().dbInit(ParserFactoryTest.class.getClassLoader().getResource("sia/test/py/fileparsers/sia.db").getPath());
		SIA.getInstance().tmpInit();
		SIA.getInstance().ormInit();
		Dictionaries.getInstance().init();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		SIA.getInstance().close(null);
	}

	@Test
	public void testParserFactoryClassNotFound() {
		try {
			ParserFactory.getInstance().create("NotExistingClass");
		} catch (PyException e) {
			assertEquals("Unexpected error type", "exceptions.ImportError",
					PyException.exceptionClassName(e.type));
			assertEquals("Unexpected IOError argument",
					"No module named NotExistingClass", e.value.toString());
		}
	}

	@Test
	public void testParserFactoryClassExists() {
		ParserFactory.getInstance().create("FmaParser");
	}

	@Test
	public void testCreate() {
		ParserFactory factory = ParserFactory.getInstance();
		Parser parser = factory.create("FmaParser");
		assertTrue("Incorrect python proxy type",
			parser.toString().indexOf("org.python.proxies.sia.py.fileparsers.FmaParser$FmaParser$") == 0);
	}

}
