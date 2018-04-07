package wombatukun.tests.integration;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import wombatukun.tests.test6.converter.OrderConverter;
import wombatukun.tests.test6.exception.ParserException;
import wombatukun.tests.test6.model.OrderOut;
import wombatukun.tests.test6.parser.OrderParser;
import wombatukun.tests.test6.parser.ParserFactory;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ParserCsvTest {

	private ClassLoader classLoader = getClass().getClassLoader();
	private ParserFactory factory = ParserFactory.getInstance();
	private OrderConverter converter = OrderConverter.getInstance();

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test
	public void fileNotFoundTest() {
		expectedEx.expect(ParserException.class);
		expectedEx.expectMessage("File not found");
		OrderParser parser = factory.getParserByFileName("ooorders.csv");
		parser.execute();
	}

	@Test
	public void emptyFileTest() {
		expectedEx.expect(ParserException.class);
		expectedEx.expectMessage("File is empty");
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
		assertEquals("id not specified, amount is invalid - a100, currency not specified", order.getResult());

		order = orders.stream().filter(o -> o.getLine() == 2L).findFirst().get();
		assertEquals(Long.valueOf(2), order.getId());
		assertEquals(new BigDecimal(200), order.getAmount());
		assertEquals("RUB", order.getCurrency());
		assertEquals("оплата заказа2", order.getComment());
		assertEquals(file.getAbsolutePath(), order.getFilename());
		assertEquals(OrderConverter.RESULT_OK, order.getResult());

		order = orders.stream().filter(o -> o.getLine() == 3L).findFirst().get();
		assertEquals(Long.valueOf(3), order.getId());
		assertEquals(new BigDecimal(300), order.getAmount());
		assertEquals("EUR", order.getCurrency());
		assertEquals("оплата заказа3", order.getComment());
		assertEquals(file.getAbsolutePath(), order.getFilename());
		assertEquals(OrderConverter.RESULT_OK, order.getResult());

		order = orders.stream().filter(o -> o.getLine() == 4L).findFirst().get();
		assertNull(order.getId());
		assertEquals(new BigDecimal(400), order.getAmount());
		assertEquals("JPY", order.getCurrency());
		assertEquals("оплата заказа4", order.getComment());
		assertEquals(file.getAbsolutePath(), order.getFilename());
		assertEquals("id is invalid - 4f", order.getResult());

		order = orders.stream().filter(o -> o.getLine() == 5L).findFirst().get();
		assertEquals(Long.valueOf(5), order.getId());
		assertNull(order.getAmount());
		assertEquals("BRP", order.getCurrency());
		assertEquals("оплата заказа5", order.getComment());
		assertEquals(file.getAbsolutePath(), order.getFilename());
		assertEquals("amount is invalid - dfg", order.getResult());

		order = orders.stream().filter(o -> o.getLine() == 6L).findFirst().get();
		assertEquals(Long.valueOf(6), order.getId());
		assertEquals(new BigDecimal(600), order.getAmount());
		assertEquals("USD", order.getCurrency());
		assertNull(order.getComment());
		assertEquals(file.getAbsolutePath(), order.getFilename());
		assertEquals("comment not specified", order.getResult());

		order = orders.stream().filter(o -> o.getLine() == 7L).findFirst().get();
		assertEquals(Long.valueOf(7), order.getId());
		assertEquals(new BigDecimal(700), order.getAmount());
		assertNull(order.getCurrency());
		assertEquals("оплата заказа7", order.getComment());
		assertEquals(file.getAbsolutePath(), order.getFilename());
		assertEquals("currency not specified", order.getResult());

		order = orders.stream().filter(o -> o.getLine() == 8L).findFirst().get();
		assertNull(order.getId());
		assertNull(order.getAmount());
		assertNull(order.getCurrency());
		assertNull(order.getComment());
		assertEquals(file.getAbsolutePath(), order.getFilename());
		assertEquals("columns count is invalid - 3", order.getResult());

		order = orders.stream().filter(o -> o.getLine() == 9L).findFirst().get();
		assertNull(order.getId());
		assertNull(order.getAmount());
		assertNull(order.getCurrency());
		assertNull(order.getComment());
		assertEquals(file.getAbsolutePath(), order.getFilename());
		assertEquals("columns count is invalid - 5", order.getResult());
	}

}
