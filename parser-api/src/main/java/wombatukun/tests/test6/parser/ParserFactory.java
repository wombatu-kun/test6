package wombatukun.tests.test6.parser;

import wombatukun.tests.test6.exception.ParserException;

import java.io.InputStream;
import java.util.Properties;

public class ParserFactory {

	public static final String PROPERTIES_FILENAME = "parsers.properties";
	public static final String UNABLE_TO_LOAD = "Unable to load ";
	public static final String PARSER_BY_NAME = "-parser by name ";
	public static final String FILES_ARE_NOT_SUPPORTED = "-files are not supported";
	public static final String FILE_FORMAT_NOT_SPECIFIED = "File format not specified";

	private static class SingletonHolder {
		private static final ParserFactory INSTANCE = new ParserFactory();
	}

	private Properties parsers;

	private ParserFactory() {
		try(InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES_FILENAME)) {
			parsers = new Properties();
			parsers.load(in);
		} catch (Exception e) {
			throw new ParserException(UNABLE_TO_LOAD + PROPERTIES_FILENAME);
		}
	}

	public static ParserFactory getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public OrderParser getParserByFileName(String filename) {
		int dotIndex = filename.lastIndexOf('.');
		if ((dotIndex == -1) || (dotIndex == filename.length()-1)) {
			throw new ParserException(FILE_FORMAT_NOT_SPECIFIED);
		}

		String extension = filename.substring(dotIndex+1).toUpperCase();
		String className = parsers.getProperty(extension);
		if (className == null) {
			throw new ParserException(extension + FILES_ARE_NOT_SUPPORTED);
		}

		OrderParser parser;
		try {
			parser = (OrderParser) Class.forName(className).getDeclaredConstructor(String.class).newInstance(filename);
		} catch (Exception e) {
			throw new ParserException(UNABLE_TO_LOAD + extension + PARSER_BY_NAME + className);
		}
		return parser;
	}
}
