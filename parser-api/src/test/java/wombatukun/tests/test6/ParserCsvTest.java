package wombatukun.tests.test6;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import wombatukun.tests.test6.converter.Converter;
import wombatukun.tests.test6.exception.ParserException;
import wombatukun.tests.test6.model.OrderOut;
import wombatukun.tests.test6.parser.OrderParser;
import wombatukun.tests.test6.parser.ParserFactory;
import wombatukun.tests.test6.parser.impl.OrderParserCsv;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ParserCsvTest {

	private ClassLoader classLoader = getClass().getClassLoader();
	private ParserFactory factory = ParserFactory.getInstance();
	private Converter converter = Converter.getInstance();

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test
	public void fileNotFoundTest() {
		expectedEx.expect(ParserException.class);
		expectedEx.expectMessage(OrderParser.FILE_NOT_FOUND);
		OrderParser parser = factory.getParserByFileName("ooorders.csv");
		parser.execute();
	}

	@Test
	public void emptyFileTest() {
		expectedEx.expect(ParserException.class);
		expectedEx.expectMessage(OrderParser.FILE_IS_EMPTY);
		File file = new File(classLoader.getResource("empty.csv").getFile());
		OrderParser parser = factory.getParserByFileName(file.getAbsolutePath());
		parser.execute();
	}

	@Test
	public void executeSuccessTest() {
		File file = new File(classLoader.getResource("orders.csv").getFile());
		OrderParser parser = factory.getParserByFileName(file.getAbsolutePath());
		List<OrderOut> orders = parser.execute();
		orders.stream().map(converter::convertOutToString).forEach(System.out::println);
		assertEquals(9, orders.size());


		OrderOut order = orders.stream().filter(o -> o.getLine() == 1L).findFirst().get();
		assertNull(order.getId());
		assertNull(order.getAmount());
		assertNull(order.getCurrency());
		assertEquals("оплата заказа1", order.getComment());
		assertEquals(file.getAbsolutePath(), order.getFilename());
		assertEquals(Converter.ORDER_ID + Converter.NOT_SPECIFIED
				+ ", " + Converter.ORDER_AMOUNT + Converter.IS_INVALID + "a100"
				+ ", " + Converter.ORDER_CURRENCY + Converter.NOT_SPECIFIED, order.getResult());

		order = orders.stream().filter(o -> o.getLine() == 2L).findFirst().get();
		assertEquals(Long.valueOf(2), order.getId());
		assertEquals(new BigDecimal(200), order.getAmount());
		assertEquals("RUB", order.getCurrency());
		assertEquals("оплата заказа2", order.getComment());
		assertEquals(file.getAbsolutePath(), order.getFilename());
		assertEquals(Converter.RESULT_OK, order.getResult());

		order = orders.stream().filter(o -> o.getLine() == 3L).findFirst().get();
		assertEquals(Long.valueOf(3), order.getId());
		assertEquals(new BigDecimal(300), order.getAmount());
		assertEquals("EUR", order.getCurrency());
		assertEquals("оплата заказа3", order.getComment());
		assertEquals(file.getAbsolutePath(), order.getFilename());
		assertEquals(Converter.RESULT_OK, order.getResult());

		order = orders.stream().filter(o -> o.getLine() == 4L).findFirst().get();
		assertNull(order.getId());
		assertEquals(new BigDecimal(400), order.getAmount());
		assertEquals("JPY", order.getCurrency());
		assertEquals("оплата заказа4", order.getComment());
		assertEquals(file.getAbsolutePath(), order.getFilename());
		assertEquals(Converter.ORDER_ID + Converter.IS_INVALID + "4f", order.getResult());

		order = orders.stream().filter(o -> o.getLine() == 5L).findFirst().get();
		assertEquals(Long.valueOf(5), order.getId());
		assertNull(order.getAmount());
		assertEquals("BRP", order.getCurrency());
		assertEquals("оплата заказа5", order.getComment());
		assertEquals(file.getAbsolutePath(), order.getFilename());
		assertEquals(Converter.ORDER_AMOUNT + Converter.IS_INVALID + "dfg", order.getResult());

		order = orders.stream().filter(o -> o.getLine() == 6L).findFirst().get();
		assertEquals(Long.valueOf(6), order.getId());
		assertEquals(new BigDecimal(600), order.getAmount());
		assertEquals("USD", order.getCurrency());
		assertNull(order.getComment());
		assertEquals(file.getAbsolutePath(), order.getFilename());
		assertEquals(Converter.ORDER_COMMENT + Converter.NOT_SPECIFIED, order.getResult());

		order = orders.stream().filter(o -> o.getLine() == 7L).findFirst().get();
		assertEquals(Long.valueOf(7), order.getId());
		assertEquals(new BigDecimal(700), order.getAmount());
		assertNull(order.getCurrency());
		assertEquals("оплата заказа7", order.getComment());
		assertEquals(file.getAbsolutePath(), order.getFilename());
		assertEquals(Converter.ORDER_CURRENCY + Converter.NOT_SPECIFIED, order.getResult());

		order = orders.stream().filter(o -> o.getLine() == 8L).findFirst().get();
		assertNull(order.getId());
		assertNull(order.getAmount());
		assertNull(order.getCurrency());
		assertNull(order.getComment());
		assertEquals(file.getAbsolutePath(), order.getFilename());
		assertEquals(OrderParserCsv.COLUMNS_COUNT_IS_INVALID + "3", order.getResult());

		order = orders.stream().filter(o -> o.getLine() == 9L).findFirst().get();
		assertNull(order.getId());
		assertNull(order.getAmount());
		assertNull(order.getCurrency());
		assertNull(order.getComment());
		assertEquals(file.getAbsolutePath(), order.getFilename());
		assertEquals(OrderParserCsv.COLUMNS_COUNT_IS_INVALID + "5", order.getResult());
	}

}
