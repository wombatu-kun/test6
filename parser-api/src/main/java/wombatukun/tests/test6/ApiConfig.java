package wombatukun.tests.test6;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import wombatukun.tests.test6.converter.Converter;
import wombatukun.tests.test6.parser.ParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@ComponentScan("wombatukun.tests.test6")
public class ApiConfig {
	public static final String PROPERTIES_FILENAME = "parsers.properties";

	@Bean
	public Properties parsers() throws IOException{
		Properties parsers;
		try(InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES_FILENAME)) {
			parsers = new Properties();
			parsers.load(in);
		}
		return parsers;
	}

	@Bean
	public ParserFactory parserFactory() {
		return ParserFactory.getInstance();
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
