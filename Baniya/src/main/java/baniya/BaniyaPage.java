package baniya;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

public class BaniyaPage {

	PaginationPage pagination = new PaginationPage();
	String xCategory = "//div[@class='box-category']/ul[@class='mega-category']/li";
	
	public int getTotalCategories(){
		return Driver.get().findElements(By.xpath(xCategory)).size();
	}
	
	public String loadCategoryPage(int category){
		WebElement we = Driver.get().findElement(By.xpath(xCategory + "[" + category + "]/a"));
		String catName = we.getAttribute("innerText").trim();
		we.sendKeys(Keys.RETURN);	
		return catName;
	}
	
	public void getProductList(){
		String image = "//div[@class='product-list']/div[%I%]/div[@class='left']/div[@class='image']/a/img";
		String summary = "//div[@class='product-list']/div[%I%]/div[@class='left']/div[@class='name']/a";
		String description = "//div[@class='product-list']/div[%I%]/div[@class='left']/div[@class='description']";
		String price = "//div[@class='product-list']/div[%I%]/div[@class='right']/div[@class='price']";

		List<String> lines = new ArrayList<String>();
		int categories = getTotalCategories();
		for (int cat=1 ; cat <= categories ; cat++){
			String catName = loadCategoryPage(cat);
			
			do{
				pagination.init();
				int items = pagination.getLastItem() - pagination.getFirstItem() + 1;
	
				for (int item=1 ; item<=items ; item++){
					String valImg = Driver.get().findElement(By.xpath(getXpath(image, item))).getAttribute("src");
					String valSum = Driver.get().findElement(By.xpath(getXpath(summary, item))).getText();
					String valDesc = Driver.get().findElement(By.xpath(getXpath(description, item))).getText();
					String valPrice = Driver.get().findElement(By.xpath(getXpath(price, item))).getText().split("\\n")[0];
		
					lines.add(catName + "," + valImg + ",\"" + valSum + "\",\"" + valDesc + "\",\"" + valPrice + "\"");
				}
			}while(pagination.getNextPage() != null);
		}
		writeFile(lines);
	}
	
	private void writeFile(List<String> lines){
		try {
			Files.write(Paths.get("sample.csv"), lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getXpath(String path, int index){
		return path.replace("%I%", Integer.toString(index));
	}
}
