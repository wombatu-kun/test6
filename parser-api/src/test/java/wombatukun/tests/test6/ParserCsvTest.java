package wombatukun.tests.test6;

import org.junit.Test;
import wombatukun.tests.test6.converter.Converter;
import wombatukun.tests.test6.parser.OrderParser;
import wombatukun.tests.test6.parser.ParserFactory;
import wombatukun.tests.test6.parser.impl.OrderParserCsv;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertTrue;

public class ParserCsvTest {

	private ClassLoader classLoader = getClass().getClassLoader();
	private ParserFactory factory = ParserFactory.getInstance();

	@Test
	public void executeSuccessTest() throws UnsupportedEncodingException {
		File file = new File(classLoader.getResource("orders.csv").getFile());
		OrderParser parser = factory.getParserByFileName(file.getAbsolutePath());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		parser.execute(ps);
		String result = baos.toString("UTF-8");
		ps.close();
		System.out.print(result);

		String expected = "{\"" + Converter.ORDER_COMMENT + "\":\"оплата заказа1\", \""
				+ Converter.ORDER_FILENAME + "\":\"" + file.getAbsolutePath() + "\", \""
				+ Converter.ORDER_LINE + "\":1, \""
				+ Converter.ORDER_RESULT + "\":\"" + Converter.ORDER_ID + Converter.NOT_SPECIFIED
				+ ", " + Converter.ORDER_AMOUNT + Converter.IS_INVALID + "a100"
				+ ", " + Converter.ORDER_CURRENCY + Converter.NOT_SPECIFIED + "\"}\n";
		assertTrue(result.contains(expected));

		expected = "{\"" + Converter.ORDER_ID + "\":2, \""
				+ Converter.ORDER_AMOUNT + "\":200, \""
				+ Converter.ORDER_COMMENT + "\":\"оплата заказа2\", \""
				+ Converter.ORDER_FILENAME + "\":\"" + file.getAbsolutePath() + "\", \""
				+ Converter.ORDER_LINE + "\":2, \""
				+ Converter.ORDER_RESULT + "\":\"" + Converter.RESULT_OK + "\"}\n";
		assertTrue(result.contains(expected));

		expected = "{\"" + Converter.ORDER_ID + "\":3, \""
				+ Converter.ORDER_AMOUNT + "\":300, \""
				+ Converter.ORDER_COMMENT + "\":\"оплата заказа3\", \""
				+ Converter.ORDER_FILENAME + "\":\"" + file.getAbsolutePath() + "\", \""
				+ Converter.ORDER_LINE + "\":3, \""
				+ Converter.ORDER_RESULT + "\":\"" + Converter.RESULT_OK + "\"}\n";
		assertTrue(result.contains(expected));

		expected = "{\"" + Converter.ORDER_AMOUNT + "\":400, \""
				+ Converter.ORDER_COMMENT + "\":\"оплата заказа4\", \""
				+ Converter.ORDER_FILENAME + "\":\"" + file.getAbsolutePath() + "\", \""
				+ Converter.ORDER_LINE + "\":4, \""
				+ Converter.ORDER_RESULT + "\":\"" + Converter.ORDER_ID + Converter.IS_INVALID + "4f" + "\"}\n";
		assertTrue(result.contains(expected));

		expected = "{\"" + Converter.ORDER_ID + "\":5, \""
				+ Converter.ORDER_COMMENT + "\":\"оплата заказа5\", \""
				+ Converter.ORDER_FILENAME + "\":\"" + file.getAbsolutePath() + "\", \""
				+ Converter.ORDER_LINE + "\":5, \""
				+ Converter.ORDER_RESULT + "\":\"" + Converter.ORDER_AMOUNT + Converter.IS_INVALID + "dfg" + "\"}\n";
		assertTrue(result.contains(expected));

		expected = "{\"" + Converter.ORDER_ID + "\":6, \""
				+ Converter.ORDER_AMOUNT + "\":600, \""
				+ Converter.ORDER_FILENAME + "\":\"" + file.getAbsolutePath() + "\", \""
				+ Converter.ORDER_LINE + "\":6, \""
				+ Converter.ORDER_RESULT + "\":\"" + Converter.ORDER_COMMENT + Converter.NOT_SPECIFIED + "\"}\n";
		assertTrue(result.contains(expected));

		expected = "{\"" + Converter.ORDER_ID + "\":7, \""
				+ Converter.ORDER_AMOUNT + "\":700, \""
				+ Converter.ORDER_COMMENT + "\":\"оплата заказа7\", \""
				+ Converter.ORDER_FILENAME + "\":\"" + file.getAbsolutePath() + "\", \""
				+ Converter.ORDER_LINE + "\":7, \""
				+ Converter.ORDER_RESULT + "\":\"" + Converter.ORDER_CURRENCY + Converter.NOT_SPECIFIED + "\"}\n";
		assertTrue(result.contains(expected));

		expected = "{\"" + Converter.ORDER_FILENAME + "\":\"" + file.getAbsolutePath() + "\", \""
				+ Converter.ORDER_LINE + "\":8, \""
				+ Converter.ORDER_RESULT + "\":\"" + OrderParserCsv.COLUMNS_COUNT_IS_INVALID + "3" + "\"}\n";
		assertTrue(result.contains(expected));

		expected = "{\"" + Converter.ORDER_FILENAME + "\":\"" + file.getAbsolutePath() + "\", \""
				+ Converter.ORDER_LINE + "\":9, \""
				+ Converter.ORDER_RESULT + "\":\"" + OrderParserCsv.COLUMNS_COUNT_IS_INVALID + "5" + "\"}\n";
		assertTrue(result.contains(expected));
	}

}
