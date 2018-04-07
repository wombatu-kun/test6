package wombatukun.tests.unit;

import org.junit.Test;
import wombatukun.tests.test6.converter.OrderConverter;
import wombatukun.tests.test6.model.OrderIn;
import wombatukun.tests.test6.model.OrderOut;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ConverterTest {

	private OrderConverter converter = OrderConverter.getInstance();

	private String filename = "file.ext";
	private Long line = 42L;
	private Long id = 100L;
	private BigDecimal amount = new BigDecimal(23);
	private String currency = "USD";
	private String comment = "payment";
	private String error = "some error";

	@Test
	public void convertInToOutTest() {
		OrderIn source = new OrderIn(id.toString(), amount.toString(), currency, comment);
		OrderOut target = OrderConverter.convertInToOut(source, filename, line, null);
		assertEquals(id, target.getId());
		assertEquals(amount, target.getAmount());
		assertEquals(currency, target.getCurrency());
		assertEquals(comment, target.getComment());
		assertEquals(filename, target.getFilename());
		assertEquals(line, target.getLine());
		assertEquals(OrderConverter.RESULT_OK, target.getResult());

		target = OrderConverter.convertInToOut(null, filename, null, error);
		assertNull(target.getId());
		assertNull(target.getAmount());
		assertNull(target.getCurrency());
		assertNull(target.getComment());
		assertNull(target.getLine());
		assertEquals(filename, target.getFilename());
		assertEquals(error, target.getResult());

		source.setOrderId("123.4");
		source.setAmount("qwe");
		source.setCurrency(null);
		target = OrderConverter.convertInToOut(source, filename, line, null);
		assertNull(target.getId());
		assertNull(target.getAmount());
		assertNull(target.getCurrency());
		assertEquals(comment, target.getComment());
		assertEquals(filename, target.getFilename());
		assertEquals(line, target.getLine());
		assertEquals("id is invalid - 123.4, amount is invalid - qwe, currency not specified", target.getResult());

		source.setOrderId(null);
		source.setAmount(null);
		source.setCurrency(null);
		source.setComment(null);
		target = OrderConverter.convertInToOut(source, filename, null, null);
		assertNull(target.getComment());
		assertNull(target.getLine());
		assertEquals(filename, target.getFilename());
		assertEquals("id not specified, amount not specified, currency not specified, comment not specified", target.getResult());
	}

	@Test
	public void buildErrorStringTest() {
		String expected = "{\"filename\":\"file.ext\", \"line\":42, \"result\":\"some error\"}";
		String actual = OrderConverter.buildErrorString(filename, line, error);
		assertEquals(expected, actual);
		expected = "{\"filename\":\"file.ext\", \"result\":\"some error\"}";
		actual = OrderConverter.buildErrorString(filename, null, error);
		assertEquals(expected, actual);
	}

	@Test
	public void convertOutToStringTest(){
		OrderOut order = new OrderOut();
		order.setId(id);
		order.setAmount(amount);
		order.setCurrency(currency);
		order.setComment(comment);
		order.setFilename(filename);
		order.setLine(line);
		order.setResult(OrderConverter.RESULT_OK);
		String expected = "{\"id\":" + order.getId() + ", \"amount\":" + order.getAmount()
				+ ", \"comment\":\"" + order.getComment() + "\", \"filename\":\"" + order.getFilename()
				+ "\", \"line\":" + order.getLine() + ", \"result\":\"" + order.getResult() + "\"}";
		String actual = converter.convertOutToString(order);
		assertEquals(expected, actual);
		order.setLine(null);
		expected = "{\"id\":" + order.getId() + ", \"amount\":" + order.getAmount()
				+ ", \"comment\":\"" + order.getComment() + "\", \"filename\":\"" + order.getFilename()
				+ "\", \"result\":\"" + order.getResult() + "\"}";
		actual = converter.convertOutToString(order);
		assertEquals(expected, actual);
	}
}
