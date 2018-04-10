package wombatukun.tests.test6.parser.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import wombatukun.tests.test6.converter.Converter;
import wombatukun.tests.test6.model.OrderIn;
import wombatukun.tests.test6.model.OrderOut;
import wombatukun.tests.test6.parser.OrderParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.stream.Collectors;

public class OrderParserJson extends OrderParser {
	public static final String JSON_IS_INVALID = "json is invalid";

	private ObjectMapper jsonMapper;

	public OrderParserJson(String fileName) {
		super(fileName);
		jsonMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	@Override
	public void parse(BufferedReader input, PrintStream output) throws IOException {
		String data = input.lines().collect(Collectors.joining());
		if (StringUtils.isBlank(data)) {
			throw new RuntimeException(FILE_IS_EMPTY);
		}

		Object json;
		json = new JSONTokener(data).nextValue();

		if (json instanceof JSONObject) { //json with single object-order
			OrderIn source = jsonMapper.readValue(data, OrderIn.class);
			OrderOut order = Converter.convertInToOut(source, filename, null, null);
			output.println(orderConverter.convertOutToString(order));
		} else if (json instanceof JSONArray) { //json with array of orders
			List<OrderIn> sourceArray = jsonMapper.readValue(data, new TypeReference<List<OrderIn>>(){});
			sourceArray.parallelStream()
					.map(s -> Converter.convertInToOut(s, filename, null, null))
					.forEach(o -> output.println(orderConverter.convertOutToString(o)));
		} else { //strange content
			throw new RuntimeException(JSON_IS_INVALID);
		}
	}

}
