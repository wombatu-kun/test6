package wombatukun.tests.test6.parser.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import wombatukun.tests.test6.converter.Converter;
import wombatukun.tests.test6.model.OrderIn;
import wombatukun.tests.test6.model.OrderOut;
import wombatukun.tests.test6.parser.OrderParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class OrderParserJson extends OrderParser {
	public static final String EXTENSION = "JSON";
	public static final String JSON_IS_INVALID = "json is invalid";

	@Autowired
	private ObjectMapper jsonMapper;

	@Override
	protected boolean extensionCorrect(String filename) {
		return filename.toUpperCase().endsWith("." + EXTENSION);
	}

	@Override
	public void parse(String filename, BufferedReader input, PrintStream output) throws IOException, ExecutionException, InterruptedException {
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
			forkJoinPool.submit( () ->
				sourceArray.parallelStream()
						.map(s -> Converter.convertInToOut(s, filename, null, null))
						.forEach(o -> output.println(orderConverter.convertOutToString(o)))
			).get();
		} else { //strange content
			throw new RuntimeException(JSON_IS_INVALID);
		}
	}

}
