package wombatukun.tests.test6;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import wombatukun.tests.test6.parser.OrderParser;
import wombatukun.tests.test6.parser.ParserFactory;
import wombatukun.tests.test6.parser.impl.OrderParserCsv;
import wombatukun.tests.test6.parser.impl.OrderParserJson;

import static org.junit.Assert.assertTrue;

@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ApiConfig.class})
public class FactoryTest {
	@Autowired
	private ParserFactory parserFactory; // = ParserFactory.getInstance();

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test
	public void formatNotSpecifiedTest() {
		expectedEx.expect(RuntimeException.class);
		expectedEx.expectMessage(ParserFactory.FILE_FORMAT_NOT_SPECIFIED);
		parserFactory.getParserByFileName("ololo.");
	}

	@Test
	public void formatNotSupportedTest() {
		String  extension = "xxx";
		expectedEx.expect(RuntimeException.class);
		expectedEx.expectMessage(extension.toUpperCase() + ParserFactory.FILES_ARE_NOT_SUPPORTED);
		parserFactory.getParserByFileName("ololo." + extension);
	}

	@Test
	public void unableToLoadTest(){
		expectedEx.expect(RuntimeException.class);
		expectedEx.expectMessage(ParserFactory.UNABLE_TO_LOAD);
		parserFactory.getParserByFileName("ololo.xlsx");
	}

	@Test
	public void getParserSuccessTest() {
		OrderParser parser = parserFactory.getParserByFileName("ololo.csv");
		assertTrue(parser instanceof OrderParserCsv);
		parser = parserFactory.getParserByFileName("ololo.json");
		assertTrue(parser instanceof OrderParserJson);
	}

}
