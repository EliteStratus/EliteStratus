package pricer;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

public class FlipkartPage extends LoadableComponent<FlipkartPage> {
	
	@FindBy(xpath = "//div[@class='O8ZS_U']/input[@class='LM6RPg']") private WebElement textSearch;

	public FlipkartPage() {
		PageFactory.initElements(Driver.get(), this);
		this.get();
	}
	
	@Override
	protected void isLoaded() throws Error {}

	@Override
	protected void load() {}

	public String getProductLine(String value){
		String desc = search(value).getDescription(1);
		String match = getMatch(value, desc.split(",")[0]);
		return "\"" + match + "\"," + desc;
	}

	public FlipkartPage search(String value){
		textSearch.clear();
		textSearch.sendKeys(value);
		textSearch.sendKeys(Keys.RETURN);	
		return new FlipkartPage();
	}
	
	public String getDescription(int index){
		String xpath = "//div[@class='col col-3-12 col-md-3-12 MP_3W3'][" + index + "]";
		String name="NA", price="NA", link="NA", original="NA";
		try {
			WebElement we = Driver.get().findElement(By.xpath(xpath));
			name = we.findElement(By.className("_2cLu-l")).getText();
			price = we.findElement(By.className("_1vC4OE")).getText();
			link = we.findElement(By.className("_2cLu-l")).getAttribute("href");
			original = we.findElement(By.className("_3auQ3N")).getText();
		} catch (Exception e) {}

		return "\"" + name + "\",\"" + price + "\",\"" + original + "\",\"" + link + "\"";
	}
	
	public String getMatch(String source, String found){
		int match = 0;
		int tokens = source.split(" ").length;		
		for (int i=0 ; i<tokens ; i++){
			if(found.toLowerCase().contains(source.split(" ")[i].toLowerCase()))
				match++;
		}	
		return String.format("%.2f", (float)match/tokens*100);
	}
}
