package pricer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class PriceCompare {
	
	public static void main(String[] args){
		Driver.init();
		List<String> moonboys = new ReadMoonboy().read("ProductSource.csv");
		moonboys.remove(0); // remove header
		
		List<String> output = new MoonboyPage().getProduct(moonboys);		
		try {
			Files.write(Paths.get("sample.csv"), output, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Driver.get().quit();
	}
}
