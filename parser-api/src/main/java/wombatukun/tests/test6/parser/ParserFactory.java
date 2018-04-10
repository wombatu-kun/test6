package wombatukun.tests.test6.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class ParserFactory {
	public static final String UNABLE_TO_LOAD = "Unable to load ";
	public static final String PARSER_BY_NAME = "-parser by name ";
	public static final String FILES_ARE_NOT_SUPPORTED = "-files are not supported";
	public static final String FILE_FORMAT_NOT_SPECIFIED = "File format not specified";

	private static class SingletonHolder {
		private static final ParserFactory INSTANCE = new ParserFactory();
	}
	@Autowired
	private Properties parsers;

	private ParserFactory () {}

	public static ParserFactory getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public OrderParser getParserByFileName(String filename) {
		int dotIndex = filename.lastIndexOf('.');
		if ((dotIndex == -1) || (dotIndex == filename.length()-1)) {
			throw new RuntimeException(FILE_FORMAT_NOT_SPECIFIED);
		}

		String extension = filename.substring(dotIndex+1).toUpperCase();
		String className = parsers.getProperty(extension);
		if (className == null) {
			throw new RuntimeException(extension + FILES_ARE_NOT_SUPPORTED);
		}

		OrderParser parser;
		try {
			parser = (OrderParser) Class.forName(className).getDeclaredConstructor(String.class).newInstance(filename);
		} catch (Exception e) {
			throw new RuntimeException(UNABLE_TO_LOAD + extension + PARSER_BY_NAME + className);
		}
		return parser;
	}
}
