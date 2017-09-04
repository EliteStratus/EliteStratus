package baniya;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

public class PaginationPage {
	int pages, total, first, last;	
	String results = "//div[@class='pagination']/div[@class='results']";
	String links = "//div[@class='pagination']/div[@class='links']";

	public int getFirstItem(){ return first; }
	public int getLastItem(){ return last; }
	public int getTotalItems(){ return total; }
	public int getTotalPages(){ return pages; }
	
	public void init(){
		String pagiText = Driver.get().findElement(By.xpath(results)).getText();
		Pattern pattern = Pattern.compile("\\d+");
		Matcher match = pattern.matcher(pagiText);
	
		first = getMatch(match);
		last = getMatch(match);
		total = getMatch(match);
		pages = getMatch(match);
	}
	
	public BaniyaPage getNextPage(){
		BaniyaPage page = null;
		try{
			WebElement we = Driver.get().findElement(By.xpath(links)).findElement(By.linkText(">"));
			we.sendKeys(Keys.RETURN);
			page = new BaniyaPage();
		}catch (Exception e){}
		return page;
	}
	
	private int getMatch(Matcher match){
		match.find();
		return Integer.parseInt(match.group());
	}
}
