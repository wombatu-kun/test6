package wombatukun.tests.test6;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import wombatukun.tests.test6.converter.Converter;
import wombatukun.tests.test6.parser.OrderParser;
import wombatukun.tests.test6.parser.ParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ApiConfig.class})
public class ParserJsonTest {

	private ClassLoader classLoader = getClass().getClassLoader();
	@Autowired
	private ParserFactory parserFactory; // = ParserFactory.getInstance();

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test
	public void invalidJsonTest() {
		expectedEx.expect(RuntimeException.class);
		File file = new File(classLoader.getResource("invalid.json").getFile());
		OrderParser parser = parserFactory.getParserByFileName(file.getAbsolutePath());
		parser.execute(System.out);
	}

	@Test
	public void emptyFileTest() {
		expectedEx.expect(RuntimeException.class);
		expectedEx.expectMessage(OrderParser.FILE_IS_EMPTY);
		File file = new File(classLoader.getResource("empty.json").getFile());
		OrderParser parser = parserFactory.getParserByFileName(file.getAbsolutePath());
		parser.execute(System.out);
	}

	@Test
	public void singleObjectTest() throws UnsupportedEncodingException {
		File file = new File(classLoader.getResource("object.json").getFile());
		OrderParser parser = parserFactory.getParserByFileName(file.getAbsolutePath());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		parser.execute(ps);
		String result = baos.toString("UTF-8");
		assertEquals("{\"" + Converter.ORDER_ID + "\":1, \""
				+ Converter.ORDER_AMOUNT + "\":100, \""
				+ Converter.ORDER_COMMENT + "\":\"оплата заказа\", \""
				+ Converter.ORDER_FILENAME + "\":\"" + file.getAbsolutePath() + "\", \""
				+ Converter.ORDER_RESULT + "\":\"" + Converter.RESULT_OK + "\"}\n", result);
		ps.close();
	}

	@Test
	public void executeSuccessTest() throws UnsupportedEncodingException {
		File file = new File(classLoader.getResource("array.json").getFile());
		OrderParser parser = parserFactory.getParserByFileName(file.getAbsolutePath());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		parser.execute(ps);
		String result = baos.toString("UTF-8");
		ps.close();
		System.out.print(result);

		String expected = "{\"" + Converter.ORDER_AMOUNT + "\":150, \""
				+ Converter.ORDER_COMMENT + "\":\"оплата заказа11\", \""
				+ Converter.ORDER_FILENAME + "\":\"" + file.getAbsolutePath() + "\", \""
				+ Converter.ORDER_RESULT + "\":\"" + Converter.ORDER_ID + Converter.NOT_SPECIFIED + "\"}\n";
		assertTrue(result.contains(expected));

		expected = "{\"" + Converter.ORDER_ID + "\":12, \""
				+ Converter.ORDER_COMMENT + "\":\"оплата заказа12\", \""
				+ Converter.ORDER_FILENAME + "\":\"" + file.getAbsolutePath() + "\", \""
				+ Converter.ORDER_RESULT + "\":\"" + Converter.ORDER_AMOUNT + Converter.NOT_SPECIFIED + "\"}\n";
		assertTrue(result.contains(expected));

		expected = "{\"" + Converter.ORDER_ID + "\":13, \""
				+ Converter.ORDER_AMOUNT + "\":350, \""
				+ Converter.ORDER_COMMENT + "\":\"оплата заказа13\", \""
				+ Converter.ORDER_FILENAME + "\":\"" + file.getAbsolutePath() + "\", \""
				+ Converter.ORDER_RESULT + "\":\"" + Converter.RESULT_OK + "\"}\n";
		assertTrue(result.contains(expected));

		expected = "{\"" + Converter.ORDER_ID + "\":14, \""
				+ Converter.ORDER_FILENAME + "\":\"" + file.getAbsolutePath() + "\", \""
				+ Converter.ORDER_RESULT + "\":\"" + Converter.ORDER_AMOUNT + Converter.IS_INVALID + "q450"
				+ ", " + Converter.ORDER_COMMENT + Converter.NOT_SPECIFIED + "\"}\n";
		assertTrue(result.contains(expected));

		expected = "{\"" + Converter.ORDER_AMOUNT + "\":450, \""
				+ Converter.ORDER_COMMENT + "\":\"800\", \""
				+ Converter.ORDER_FILENAME + "\":\"" + file.getAbsolutePath() + "\", \""
				+ Converter.ORDER_RESULT + "\":\"" + Converter.ORDER_ID + Converter.IS_INVALID + "15w"
				+ ", " + Converter.ORDER_CURRENCY + Converter.NOT_SPECIFIED + "\"}\n";
		assertTrue(result.contains(expected));

		expected = "{\"" + Converter.ORDER_FILENAME + "\":\"" + file.getAbsolutePath() + "\", \""
				+ Converter.ORDER_RESULT + "\":\"" + Converter.ORDER_ID + Converter.NOT_SPECIFIED
				+ ", " + Converter.ORDER_AMOUNT + Converter.NOT_SPECIFIED
				+ ", " + Converter.ORDER_CURRENCY + Converter.NOT_SPECIFIED
				+ ", " + Converter.ORDER_COMMENT + Converter.NOT_SPECIFIED + "\"}\n";
		assertTrue(result.contains(expected));
	}

}
