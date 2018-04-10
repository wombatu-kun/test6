package wombatukun.tests.test6.parser;

import wombatukun.tests.test6.converter.Converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class OrderParser {
	public static final String FILE_IS_EMPTY = "File is empty";

	protected final String filename;
	protected Converter orderConverter;

	public OrderParser(String filename) {
		this.filename = filename;
		orderConverter = Converter.getInstance();
	}

	/**
	 * Executes parsing-process
	 */
	public void execute(PrintStream output) {
		try (BufferedReader input = Files.newBufferedReader(Paths.get(filename))) {
			parse(input, output);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage().replace("\"", "\\\"").replace('\n', ' '));
		}
	}

	/**
	 * Parses input into List of Orders
	 * @param input opened BufferedReader
	 * @param output opened PrintStream
	 * @throws IOException on parsing errors
	 */
	protected abstract void parse(BufferedReader input, PrintStream output) throws IOException;

}
