package wombatukun.tests.test6.parser;

import wombatukun.tests.test6.exception.ParserException;
import wombatukun.tests.test6.model.OrderOut;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public abstract class OrderParser {
	public static final String FILE_NOT_FOUND = "File not found";
	public static final String FILE_IS_EMPTY = "File is empty";

	protected final String filename;

	public OrderParser(String filename) {
		this.filename = filename;
	}

	/**
	 * Executes parsing-process
	 * @return list of orders
	 */
	public List<OrderOut> execute() {
		try (BufferedReader input = Files.newBufferedReader(Paths.get(filename))) {
			return parse(input);
		} catch (IOException e) {
			throw new ParserException(FILE_NOT_FOUND);
		}
	}

	/**
	 * Parses input into List of Orders
	 * @param input opened BufferedReader
	 * @return list ot orders
	 */
	protected abstract List<OrderOut> parse(BufferedReader input);

}
