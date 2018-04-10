package wombatukun.tests.test6.parser.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import wombatukun.tests.test6.converter.Converter;
import wombatukun.tests.test6.model.OrderIn;
import wombatukun.tests.test6.model.OrderOut;
import wombatukun.tests.test6.parser.OrderParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.stream.StreamSupport;

public class OrderParserCsv extends OrderParser {
	public static final String COLUMNS_COUNT_IS_INVALID = "columns count is invalid - ";

	public OrderParserCsv(String fileName) {
		super(fileName);
	}

	@Override
	public void parse(BufferedReader input, PrintStream output) throws IOException {
		Iterable<CSVRecord> records;
		records = CSVFormat.EXCEL.parse(input);
		StreamSupport.stream(records.spliterator(), true).map(this::parseRecord)
				.forEach(o -> output.println(orderConverter.convertOutToString(o)));
	}

	private OrderOut parseRecord (CSVRecord record) {
		OrderOut target;
		if (record.size() == 4) { //valid columns count = 4
			OrderIn source = new OrderIn(record.get(0), record.get(1), record.get(2), record.get(3));
			target = Converter.convertInToOut(source, filename, record.getRecordNumber(), null);
		} else {
			target = Converter.convertInToOut(null, filename,
					record.getRecordNumber(), COLUMNS_COUNT_IS_INVALID + record.size());
		}
		return target;
	}

}
