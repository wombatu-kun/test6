package wombatukun.tests.test6;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import wombatukun.tests.test6.converter.Converter;
import wombatukun.tests.test6.parser.OrderParser;

import java.util.Map;
import java.util.stream.Stream;

public class App {
	private static final String INCORRECT_COMMAND = "Incorrect command: source files not specified";
	public static final String FILE_FORMAT_NOT_SPECIFIED = "File format not specified";

	private static Map<String, OrderParser> parsersMap;

    public static void main(String[] args) {
    	if (args.length != 0) {
			try {
				ApplicationContext ctx = new AnnotationConfigApplicationContext("wombatukun.tests.test6");
				parsersMap = (Map<String, OrderParser>) ctx.getBean("parsersMap");
				Stream.of(args).distinct().parallel().forEach(App::processFile);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else {
			System.out.println(INCORRECT_COMMAND);
		}
    }

    private static void processFile(String filename) {
		try {
			OrderParser parser = parsersMap.get(getExtension(filename));
			if (parser == null) {
				System.out.println(Converter.buildErrorString(filename, null, OrderParser.FORMAT_IS_NOT_SUPPORTED));
			} else {
				parser.execute(filename, System.out);
			}
		} catch (Exception e) {
			System.out.println(Converter.buildErrorString(filename, null, e.getMessage()));
		}
	}

	private static String getExtension(String filename) {
		int dotIndex = filename.lastIndexOf('.');
		if ((dotIndex == -1) || (dotIndex == filename.length()-1)) {
			throw new RuntimeException(FILE_FORMAT_NOT_SPECIFIED);
		}
		return filename.substring(dotIndex+1).toUpperCase();
	}
 }
