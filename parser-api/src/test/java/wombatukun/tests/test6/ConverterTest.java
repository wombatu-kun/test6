package wombatukun.tests.test6;

import org.junit.Test;
import wombatukun.tests.test6.converter.Converter;
import wombatukun.tests.test6.model.OrderIn;
import wombatukun.tests.test6.model.OrderOut;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ConverterTest {

	private Converter converter = Converter.getInstance();

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
		OrderOut target = Converter.convertInToOut(source, filename, line, null);
		assertEquals(id, target.getId());
		assertEquals(amount, target.getAmount());
		assertEquals(currency, target.getCurrency());
		assertEquals(comment, target.getComment());
		assertEquals(filename, target.getFilename());
		assertEquals(line, target.getLine());
		assertEquals(Converter.RESULT_OK, target.getResult());

		target = Converter.convertInToOut(null, filename, null, error);
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
		target = Converter.convertInToOut(source, filename, line, null);
		assertNull(target.getId());
		assertNull(target.getAmount());
		assertNull(target.getCurrency());
		assertEquals(comment, target.getComment());
		assertEquals(filename, target.getFilename());
		assertEquals(line, target.getLine());
		assertEquals(Converter.ORDER_ID + Converter.IS_INVALID + "123.4, "
				+ Converter.ORDER_AMOUNT + Converter.IS_INVALID +"qwe, "
				+ Converter.ORDER_CURRENCY + Converter.NOT_SPECIFIED, target.getResult());

		source.setOrderId(null);
		source.setAmount(null);
		source.setCurrency(null);
		source.setComment(null);
		target = Converter.convertInToOut(source, filename, null, null);
		assertNull(target.getComment());
		assertNull(target.getLine());
		assertEquals(filename, target.getFilename());
		assertEquals(Converter.ORDER_ID + Converter.NOT_SPECIFIED + ", "
				+ Converter.ORDER_AMOUNT + Converter.NOT_SPECIFIED + ", "
				+ Converter.ORDER_CURRENCY + Converter.NOT_SPECIFIED + ", "
				+ Converter.ORDER_COMMENT + Converter.NOT_SPECIFIED, target.getResult());
	}

	@Test
	public void buildErrorStringTest() {
		String expected = "{\"" + Converter.ORDER_FILENAME + "\":\"" + filename + "\", \""
		+ Converter.ORDER_LINE + "\":42, \"" + Converter.ORDER_RESULT + "\":\"" + error + "\"}";
		String actual = Converter.buildErrorString(filename, line, error);
		assertEquals(expected, actual);
		expected = "{\"" + Converter.ORDER_FILENAME + "\":\"" + filename + "\", \"" + Converter.ORDER_RESULT + "\":\"" + error + "\"}";
		actual = Converter.buildErrorString(filename, null, error);
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
		order.setResult(Converter.RESULT_OK);
		String expected = "{\"" + Converter.ORDER_ID + "\":" + order.getId()
				+ ", \"" + Converter.ORDER_AMOUNT + "\":" + order.getAmount()
				+ ", \"" + Converter.ORDER_COMMENT + "\":\"" + order.getComment()
				+ "\", \"" + Converter.ORDER_FILENAME + "\":\"" + order.getFilename()
				+ "\", \"" + Converter.ORDER_LINE + "\":" + order.getLine()
				+ ", \"" + Converter.ORDER_RESULT + "\":\"" + order.getResult() + "\"}";
		String actual = converter.convertOutToString(order);
		assertEquals(expected, actual);
		order.setLine(null);
		expected = "{\"" + Converter.ORDER_ID + "\":" + order.getId()
				+ ", \"" + Converter.ORDER_AMOUNT + "\":" + order.getAmount()
				+ ", \"" + Converter.ORDER_COMMENT + "\":\"" + order.getComment()
				+ "\", \"" + Converter.ORDER_FILENAME + "\":\"" + order.getFilename()
				+ "\", \"" + Converter.ORDER_RESULT + "\":\"" + order.getResult() + "\"}";
		actual = converter.convertOutToString(order);
		assertEquals(expected, actual);
	}
}
