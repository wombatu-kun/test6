package wombatukun.tests.test6;

import wombatukun.tests.test6.converter.Converter;
import wombatukun.tests.test6.parser.OrderParser;
import wombatukun.tests.test6.parser.ParserFactory;

import java.util.stream.Stream;

public class App {

	private static final String INCORRECT_COMMAND = "Incorrect command: source files not specified";
	private static ParserFactory parserFactory;

    public static void main(String[] args) {
    	if (args.length != 0) {
			try {
				parserFactory = ParserFactory.getInstance();
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
			OrderParser parser = parserFactory.getParserByFileName(filename);
			parser.execute(System.out);
		} catch (Exception e) {
			System.out.println(Converter.buildErrorString(filename, null, e.getMessage()));
		}
	}
}
