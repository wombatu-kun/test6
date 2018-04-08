package wombatukun.tests.test6.converter;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import wombatukun.tests.test6.model.OrderIn;
import wombatukun.tests.test6.model.OrderOut;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Class for order conversion
 */
public class Converter {

	public static final String RESULT_OK = "OK";
	public static final String NOT_SPECIFIED = " not specified";
	public static final String IS_INVALID = " is invalid - ";

	public static final String ORDER_ID = "id";
	public static final String ORDER_AMOUNT = "amount";
	public static final String ORDER_CURRENCY = "currency";
	public static final String ORDER_COMMENT = "comment";
	public static final String ORDER_FILENAME = "filename";
	public static final String ORDER_LINE = "line";
	public static final String ORDER_RESULT = "result";

	private static class SingletonHolder {
		private static final Converter INSTANCE = new Converter();
	}

	private ObjectMapper jsonMapper;

	private Converter() {
		jsonMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}

	public static Converter getInstance() {
		return SingletonHolder.INSTANCE;
	}

	/**
	 * Builds OrderOut by OrderIn with validation: id (numeric, not null), amount (numeric, not null),
	 * currency  and comment (not nulls)
	 * @param source
	 * @param filename
	 * @param line
	 * @param errorMsg
	 * @return
	 */
	public static OrderOut convertInToOut(OrderIn source, String filename, Long line, String errorMsg) {
		OrderOut target = new OrderOut();
		target.setFilename(filename);
		target.setLine(line);
		if (source != null) {
			List<String> errors = new ArrayList<>();
			target.setId(convertId(source.getOrderId(), errors));
			target.setAmount(convertAmount(source.getAmount(), errors));
			target.setCurrency(validateValue(ORDER_CURRENCY, source.getCurrency(), errors, null));
			target.setComment(validateValue(ORDER_COMMENT, source.getComment(), errors, null));
			if (!errors.isEmpty()) {
				target.setResult(errors.stream().collect(Collectors.joining(", ")));
			} else {
				target.setResult(RESULT_OK);
			}
		} else {
			target.setResult(errorMsg);
		}
		return target;
	}

	/**
	 * Serializes OrderOut (with spaces between elements, without "currency" field, null-values not included).
	 * Example: {“id”:1, ”amount”:100, ”comment”:”оплата заказа”, ”filename”:”orders.csv”, ”line”:1, ”result”:”OK”}
	 * @param order
	 * @return
	 */
	public String convertOutToString(OrderOut order) {
		String str;
		try {
			str = jsonMapper.writeValueAsString(order).replace(",\"", ", \"");
		} catch (JsonProcessingException e) {
			str = buildErrorString(order.getFilename(), order.getLine(), e.getMessage());
		}
		return str;
	}

	/**
	 * Builds string-representation of error
	 * @param filename
	 * @param line
	 * @param errorMsg
	 * @return
	 */
	public static String buildErrorString(String filename, Long line, String errorMsg) {
		final StringBuilder sb = new StringBuilder("{");
		sb.append("\"" + ORDER_FILENAME + "\":\"").append(filename).append("\"");
		if (line != null) {
			sb.append(", \"" + ORDER_LINE + "\":").append(line);
		}
		sb.append(", \"" + ORDER_RESULT + "\":\"").append(errorMsg);
		sb.append("\"}");
		return sb.toString();
	}

	private static Long convertId(String idStr, List<String> errors) {
		Long id = null;
		String value;
		value = validateValue(ORDER_ID, idStr, errors, val -> {
			if (!StringUtils.isNumeric(val)) {
				errors.add(ORDER_ID + IS_INVALID + val);
				return false;
			} else {
				return true;
			}
		});
		if (value != null) {
			id = Long.valueOf(value);
		}
		return id;
	}

	private static BigDecimal convertAmount(String amountStr, List<String> errors) {
		BigDecimal amount = null;
		String value;
		value = validateValue(ORDER_AMOUNT, amountStr, errors, val -> {
			try {
				new BigDecimal(val);
				return true;
			} catch (NumberFormatException e) {
				errors.add(ORDER_AMOUNT + IS_INVALID + val);
				return false;
			}
		});
		if (value != null) {
			amount = new BigDecimal(value);
		}
		return amount;
	}

	private static String validateValue(String field, String value, List<String> errors, Predicate<String> numberCheck) {
		String result = null;
		if (StringUtils.isNotBlank(value)) {
			result = value.trim();
			if (numberCheck != null && !numberCheck.test(result)) {
				result = null;
			}
		} else {
			errors.add(field + NOT_SPECIFIED);
		}
		return result;
	}

}
