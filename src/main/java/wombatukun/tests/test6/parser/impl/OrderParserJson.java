package wombatukun.tests.test6.parser.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import wombatukun.tests.test6.converter.Converter;
import wombatukun.tests.test6.exception.ParserException;
import wombatukun.tests.test6.model.OrderIn;
import wombatukun.tests.test6.model.OrderOut;
import wombatukun.tests.test6.parser.OrderParser;

import java.io.BufferedReader;
import java.util.Collections;
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
	public List<OrderOut> parse(BufferedReader input) {
		String data = input.lines().collect(Collectors.joining());
		if (StringUtils.isBlank(data)) {
			throw new ParserException(FILE_IS_EMPTY);
		}

		Object json;
		try {
			json = new JSONTokener(data).nextValue();
		} catch (JSONException je) {
			throw new ParserException(JSON_IS_INVALID);
		}
		if (json instanceof JSONObject) { //json with single object-order
			OrderIn source;
			try {
				source = jsonMapper.readValue(data, OrderIn.class);
			} catch (Exception e) {
				throw new ParserException(cutDeserializationError(e.getMessage()));
			}
			return Collections.singletonList(Converter.convertInToOut(source, filename, null, null));
		} else if (json instanceof JSONArray) { //json with array of orders
			List<OrderIn> sourceArray;
			try {
				sourceArray = jsonMapper.readValue(data, new TypeReference<List<OrderIn>>(){});
			} catch (Exception e) {
				throw new ParserException(cutDeserializationError(e.getMessage()));
			}
			return sourceArray.parallelStream()
					.map(s -> Converter.convertInToOut(s, filename, null, null))
					.collect(Collectors.toList());
		} else { //strange content
			throw new ParserException(JSON_IS_INVALID);
		}
	}

	private String cutDeserializationError(String errorMsg) {
		return errorMsg.substring(0, errorMsg.indexOf('\n')).replace('"','`');
	}

}
