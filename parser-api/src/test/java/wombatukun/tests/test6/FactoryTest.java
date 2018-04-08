package wombatukun.tests.test6;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import wombatukun.tests.test6.exception.ParserException;
import wombatukun.tests.test6.parser.OrderParser;
import wombatukun.tests.test6.parser.ParserFactory;
import wombatukun.tests.test6.parser.impl.OrderParserCsv;
import wombatukun.tests.test6.parser.impl.OrderParserJson;

import static org.junit.Assert.assertTrue;

public class FactoryTest {

	private ParserFactory factory = ParserFactory.getInstance();

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test
	public void formatNotSpecifiedTest() {
		expectedEx.expect(ParserException.class);
		expectedEx.expectMessage(ParserFactory.FILE_FORMAT_NOT_SPECIFIED);
		factory.getParserByFileName("ololo.");
	}

	@Test
	public void formatNotSupportedTest() {
		String  extension = "xxx";
		expectedEx.expect(ParserException.class);
		expectedEx.expectMessage(extension.toUpperCase() + ParserFactory.FILES_ARE_NOT_SUPPORTED);
		factory.getParserByFileName("ololo." + extension);
	}

	@Test
	public void unableToLoadTest(){
		expectedEx.expect(ParserException.class);
		expectedEx.expectMessage(ParserFactory.UNABLE_TO_LOAD);
		factory.getParserByFileName("ololo.xlsx");
	}

	@Test
	public void getParserSuccessTest() {
		OrderParser parser = factory.getParserByFileName("ololo.csv");
		assertTrue(parser instanceof OrderParserCsv);
		parser = factory.getParserByFileName("ololo.json");
		assertTrue(parser instanceof OrderParserJson);
	}

}
