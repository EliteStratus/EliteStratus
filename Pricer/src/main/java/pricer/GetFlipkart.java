package pricer;

import java.util.ArrayList;
import java.util.List;

public class GetFlipkart {
	
	public List<String> process(List<String> moonboys){
		List<String> flipkart = new ArrayList<String>();
		Driver.get().get("https://www.flipkart.com");
		FlipkartPage page = new FlipkartPage();
		
		for (String moonboy : moonboys){
			String name = moonboy.split(",")[2];
			flipkart.add(page.getProductLine(name));
		}	
		return flipkart;
	}
}
