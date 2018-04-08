package wombatukun.tests.test6;

import wombatukun.tests.test6.converter.Converter;
import wombatukun.tests.test6.exception.ParserException;
import wombatukun.tests.test6.model.OrderOut;
import wombatukun.tests.test6.parser.OrderParser;
import wombatukun.tests.test6.parser.ParserFactory;

import java.util.List;
import java.util.stream.Stream;

public class App {

	private static final String INCORRECT_COMMAND = "Incorrect command: source files not specified";
	private static ParserFactory parserFactory;
	private static Converter orderConverter;

    public static void main(String[] args) {
    	if (args.length != 0) {
			try {
				parserFactory = ParserFactory.getInstance();
				orderConverter = Converter.getInstance();
				Stream.of(args).distinct().parallel().forEach(App::processFile);
			} catch (ParserException e) {
				System.out.println(e.getMessage());
			}
		} else {
			System.out.println(INCORRECT_COMMAND);
		}
    }

    private static void processFile(String filename) {
		try {
			OrderParser parser = parserFactory.getParserByFileName(filename);
			List<OrderOut> orders = parser.execute();
			orders.parallelStream().forEach(o -> System.out.println(orderConverter.convertOutToString(o)));
		} catch (Exception e) {
			System.out.println(Converter.buildErrorString(filename, null, e.getMessage()));
		}
	}
}
