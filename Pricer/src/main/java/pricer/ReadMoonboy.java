package pricer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ReadMoonboy {
	
	public List<String> read(String file){
		try {
			return Files.readAllLines(Paths.get(file), Charset.forName("UTF-8"));
		} catch (IOException e) {}
		return null;
	}
}
