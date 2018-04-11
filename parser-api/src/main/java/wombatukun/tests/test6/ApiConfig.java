package wombatukun.tests.test6;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import wombatukun.tests.test6.converter.Converter;
import wombatukun.tests.test6.parser.OrderParser;
import wombatukun.tests.test6.parser.impl.OrderParserCsv;
import wombatukun.tests.test6.parser.impl.OrderParserJson;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan("wombatukun.tests.test6")
@PropertySource(value={"classpath:config.properties"})
public class ApiConfig {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public OrderParser csvParser () {
		return new OrderParserCsv();
	}

	@Bean
	public OrderParser jsonParser () {
		return new OrderParserJson();
	}

	@Bean
	public Map<String, OrderParser> parsersMap() {
		Map<String, OrderParser> map = new HashMap<>();
		map.put(OrderParserCsv.EXTENSION, csvParser());
		map.put(OrderParserJson.EXTENSION, jsonParser());
		return map;
	}

	@Bean
	public ObjectMapper jsonMapper() {
		return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}

	@Bean
	public Converter orderConverter() {
		return Converter.getInstance();
	}

}
