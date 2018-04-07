package wombatukun.tests.integration;

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

public class ParserJsonTest {

	private ClassLoader classLoader = getClass().getClassLoader();
	private ParserFactory factory = ParserFactory.getInstance();
	private OrderConverter converter = OrderConverter.getInstance();

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test
	public void fileNotFoundTest() {
		expectedEx.expect(ParserException.class);
		expectedEx.expectMessage("File not found");
		OrderParser parser = factory.getParserByFileName("ooorders.json");
		parser.execute();
	}

	@Test
	public void invalidJsonTest() {
		expectedEx.expect(ParserException.class);
		expectedEx.expectMessage("json is invalid");
		File file = new File(classLoader.getResource("invalid.json").getFile());
		OrderParser parser = factory.getParserByFileName(file.getAbsolutePath());
		parser.execute();
	}

	@Test
	public void emptyFileTest() {
		expectedEx.expect(ParserException.class);
		expectedEx.expectMessage("File is empty");
		File file = new File(classLoader.getResource("empty.json").getFile());
		OrderParser parser = factory.getParserByFileName(file.getAbsolutePath());
		parser.execute();
	}

	@Test
	public void singleObjectTest() {
		File file = new File(classLoader.getResource("object.json").getFile());
		OrderParser parser = factory.getParserByFileName(file.getAbsolutePath());
		List<OrderOut> orders = parser.execute();
		assertEquals(1, orders.size());
		OrderOut order = orders.get(0);
		assertNull(order.getLine());
		assertEquals(Long.valueOf(1), order.getId());
		assertEquals(new BigDecimal(100), order.getAmount());
		assertEquals("USD", order.getCurrency());
		assertEquals("оплата заказа", order.getComment());
		assertEquals(file.getAbsolutePath(), order.getFilename());
		assertEquals(OrderConverter.RESULT_OK, order.getResult());
	}

	@Test
	public void executeSuccessTest() {
		File file = new File(classLoader.getResource("array.json").getFile());
		OrderParser parser = factory.getParserByFileName(file.getAbsolutePath());
		List<OrderOut> orders = parser.execute();
		orders.stream().map(converter::convertOutToString).forEach(System.out::println);
		assertEquals(6, orders.size());

		OrderOut order = orders.stream().filter(o -> o.getAmount().equals(new BigDecimal(150))).findFirst().get();
		assertNull(order.getId());
		assertEquals("KZH", order.getCurrency());
		assertEquals("оплата заказа11", order.getComment());
		assertEquals(file.getAbsolutePath(), order.getFilename());
		assertEquals("id not specified", order.getResult());

		order = orders.stream().filter(o -> o.getAmount() == null).findFirst().get();
		assertEquals(Long.valueOf(12), order.getId());
		assertEquals("BLR", order.getCurrency());
		assertEquals("оплата заказа12", order.getComment());
		assertEquals(file.getAbsolutePath(), order.getFilename());
		assertEquals("amount not specified", order.getResult());

		order = orders.stream().filter(o -> o.getResult().equals(OrderConverter.RESULT_OK)).findFirst().get();
		assertEquals(Long.valueOf(13), order.getId());
		assertEquals(new BigDecimal(350), order.getAmount());
		assertEquals("UGG", order.getCurrency());
		assertEquals("оплата заказа13", order.getComment());
		assertEquals(file.getAbsolutePath(), order.getFilename());

		order = orders.stream().filter(o -> o.getCurrency().equals("GBP")).findFirst().get();
		assertEquals(Long.valueOf(14), order.getId());
		assertNull(order.getAmount());
		assertNull(order.getComment());
		assertEquals(file.getAbsolutePath(), order.getFilename());
		assertEquals("amount is invalid - q450, comment not specified", order.getResult());

		order = orders.stream().filter(o -> "800".equals(o.getComment())).findFirst().get();
		assertNull(order.getId());
		assertNull(order.getCurrency());
		assertEquals(new BigDecimal(450), order.getAmount());
		assertEquals(file.getAbsolutePath(), order.getFilename());
		assertEquals("id is invalid - 15w, currency not specified", order.getResult());

		order = orders.stream().filter(o -> (o.getAmount() == null && o.getId() == null)).findFirst().get();
		assertNull(order.getCurrency());
		assertNull(order.getComment());
		assertEquals(file.getAbsolutePath(), order.getFilename());
		assertEquals("id not specified, amount not specified, currency not specified, comment not specified", order.getResult());
	}

}
