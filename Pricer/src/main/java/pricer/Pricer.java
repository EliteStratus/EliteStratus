package pricer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Pricer {
	
	public static void main(String[] args){
		Driver.init();
		List<String> moonboys = new ReadMoonboy().read("products1.csv");
		moonboys.remove(0); // remove header
		
		List<String> flipkarts = new GetFlipkart().process(moonboys);
		
		try {
			Files.write(Paths.get("sample.csv"), flipkarts, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Driver.get().quit();
	}
}
