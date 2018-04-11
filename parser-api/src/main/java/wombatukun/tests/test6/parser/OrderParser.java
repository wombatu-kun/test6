package wombatukun.tests.test6.parser;

import org.springframework.beans.factory.annotation.Autowired;
import wombatukun.tests.test6.converter.Converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class OrderParser {
	public static final String FILE_NOT_FOUND = "File not found";
	public static final String FILE_IS_EMPTY = "File is empty";
	public static final String FORMAT_IS_NOT_SUPPORTED = "Format is not supported";

	@Autowired
	protected Converter orderConverter;

	/**
	 * Executes parsing-process
	 */
	public void execute(String filename, PrintStream output) {
		if (!extensionCorrect(filename)) {
			throw new RuntimeException(FORMAT_IS_NOT_SUPPORTED);
		}
		try (BufferedReader input = Files.newBufferedReader(Paths.get(filename))) {
			try {
				parse(filename, input, output);
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage().replace("\"", "\\\"").replace('\n', ' '));
			}
		} catch (IOException ioe) {
			throw new RuntimeException(FILE_NOT_FOUND);
		}
	}

	/**
	 * Parses input wih instant printing to output
	 * @param filename name of input file
	 * @param input filename opened with BufferedReader
	 * @param output opened PrintStream
	 * @throws IOException on parsing errors
	 */
	protected abstract void parse(String filename, BufferedReader input, PrintStream output) throws IOException;

	/**
	 * Checks if file is supported by the parser
	 * @param filename of the file to be parsed
	 * @return true if extension is correct
	 */
	protected abstract boolean extensionCorrect(String filename);

}
