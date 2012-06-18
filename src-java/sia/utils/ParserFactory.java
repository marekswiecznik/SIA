package sia.utils;

import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import sia.fileparsers.Parser;

/**
 * Parser factory
 * 
 * @author jumper
 */
public class ParserFactory {

    private PyObject parserClass;
    private PythonInterpreter interpreter;
    private static ParserFactory instance;

    /**
     * Create a new PythonInterpreter object, then use it to
     * execute some python code. In this case, we want to
     * import the python module that we will coerce.
     *
     * Once the module is imported than we obtain a reference to
     * it and assign the reference to a Java variable
     */
    private ParserFactory() {
        this.interpreter = new PythonInterpreter();
        //TODO: to speed up create(), import any parser first; change GtalkParser to ?
        interpreter.exec("from sia.py.fileparsers.GtalkParser import GtalkParser");
    }

    /**
     * The create method is responsible for performing the actual
     * coercion of the referenced python module into Java bytecode
     * 
     * @return parser
     */
    public Parser create(String className) {
        interpreter.exec("from sia.py.fileparsers."+className+" import "+className);
        parserClass = interpreter.get(className);
        PyObject buildingObject = parserClass.__call__();
        return (Parser)buildingObject.__tojava__(Parser.class);
    }
    
    public static ParserFactory getInstance() {
    	if (instance == null)
    		instance = new ParserFactory();
    	return instance;
    }
}
