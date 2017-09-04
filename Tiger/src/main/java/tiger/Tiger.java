package tiger;

import java.util.List;

public class Tiger {

	public static void main(String[] args) {

		Crypto.authenticate(args);

		Reader reader = new Reader();
		reader.read();
		Driver.init();

		Processor processor = new Processor();
		List<String> reserved = processor.reserve(reader);
		List<String> processed = processor.process(reader);

		Writer writer = new Writer();
		writer.write(reserved, processed);

		Driver.get().quit();
	}
}
