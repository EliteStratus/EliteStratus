package tiger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

public class ProductsList {

	private static Map<String, String> schema = new TreeMap<String, String>();
	private static int sleep = 0;
	
	public String build(String url, String site){

		List<String> urls = null;
		System.out.println("Gabbar: Building ProductsList for [" + site + "] at URL [" + url + "]");
 		schema = Schema.readProductsList(site);
		
		Driver.get().get(url);
 		performDynamicSleep(site);
		String pageType = schema.get("PAGINATION");
		if(pageType.equalsIgnoreCase("SCROLL")){
			loadScroll(site);
			urls = loadProducts(site);
		} else if(pageType.equalsIgnoreCase("PAGE")){
			urls = loadPagination(site);
		} else if(pageType.equalsIgnoreCase("PAGESCROLL")){
			loadPageScroll(site);
			urls = loadProducts(site);
		}else if(pageType.equalsIgnoreCase("SHOWMORE")){
			loadShowmore(site);
			urls = loadProducts(site);
		}else if(pageType.equalsIgnoreCase("SINGLELOAD")){
			loadScroll(site);
			urls = loadSingle(site);
		}else if(pageType.equalsIgnoreCase("PREPAGE")){
			urls = loadPrePagination(site);
		}
		
		String filename = getFilename(site);
 		writeProducts(filename, urls);
		System.out.println("Gabbar: Building ProductsList for [" + site + "] complete.");
		
		return filename;
	}
	
	
	public void verify(String csv){
		System.out.print("Gabbar: Verifying URLs in CSV [" + csv + "] ...");
		String response = "OK";
		try{
			List<String> lines = new ArrayList<String>();
			List<String> urls = Files.readAllLines(Paths.get(csv), Charset.forName("UTF-8"));
			for(String url : urls){
				System.out.print(".");
				if(!isUrlExists(url)){
					response = "ERROR";
				}
				lines.add(url + "," + response);
			}
	 		writeProducts(csv, lines);
		}catch(Exception e){}
		System.out.println("Gabbar: Verifying URLs in CSV [" + csv + "] complete");
	}


	
	
	private void loadScroll(String site){
		int scroll = 1;
		long curHeight = 1; long newHeight = 0;
		int maxScroll = Integer.parseInt(schema.get("MAXPAGES"));
		int offset = Integer.parseInt(schema.get("PAGE_OFFSET"));
		System.out.println("Gabbar: Loading Page in SCROLL mode. Will load [" + maxScroll + "] times. Loading ... ");
		
		while(curHeight != newHeight){
	        if(scroll++ >= maxScroll) break;
			System.out.println("Gabbar: Scrolling Page[" + scroll + "]");
			curHeight = (Long)((JavascriptExecutor) Driver.get()).executeScript("return document.body.scrollHeight");
			((JavascriptExecutor) Driver.get()).executeScript("window.scrollTo(0, document.body.scrollHeight" + (- offset) + ")");			
	 		performDynamicSleep(site);
	        newHeight = (Long)((JavascriptExecutor) Driver.get()).executeScript("return document.body.scrollHeight");
		}
		System.out.println("Gabbar: Page load complete");
	}
	
	private List<String> loadPagination(String site){
		int maxPages = Integer.parseInt(schema.get("MAXPAGES"));
		boolean click = schema.get("NEXT_CLICK").equalsIgnoreCase("CLICK");
		List<String> lines = new ArrayList<String>();
		System.out.println("Gabbar: Loading Page in PAGINATION mode. Will load [" + maxPages + "] times. Loading ...");
		try{
			int page = 1;
			WebElement next = null;
			do{
		        if(page > maxPages) break;				
				System.out.println("Gabbar: Processing Page[" + page + "]");
				lines.addAll(loadProducts(site));
				if((next = getNextButton(site, ++page)) != null){
					if(click){ next.click();
					}else{ next.sendKeys(Keys.RETURN); }
			 		performDynamicSleep(site);
				}
			}while(next != null);				
		}catch(Exception e){}
		return lines;
	}



	private List<String> loadPrePagination(String site){
		Driver.get().findElement(By.xpath(schema.get("PRECLICK"))).sendKeys(Keys.RETURN);
 		performDynamicSleep(site);
		return loadPagination(site);
	}

	
	private void loadPageScroll(String site){
		int maxPages = Integer.parseInt(schema.get("MAXPAGES"));
		System.out.println("Gabbar: Loading Page in PAGESCROLL mode. Will load [" + maxPages + "] times. Loading ...");
		try{
			int page = 1;
			WebElement next = null;
			do{
		        if(page++ >= maxPages) break;				
				System.out.println("Gabbar: PageScrolling Page[" + page + "]");
				loadScroll(site);
				if((next = Driver.get().findElement(By.xpath(schema.get("NEXT")))) != null){
					clickElement(next);
			 		performDynamicSleep(site);
				}
			}while(next != null);				
		}catch(Exception e){}
		System.out.println("Gabbar: Page load complete");
	}
	

	
	private void loadShowmore(String site){
		int maxPages = Integer.parseInt(schema.get("MAXPAGES"));
		System.out.println("Gabbar: Loading Page in SHOWMORE mode. Will load [" + maxPages + "] times. Loading ...");
		try{
			int showmore = 1; WebElement next = null;
			do{
		        if(showmore++ >= maxPages) break;				
		        if((next = Driver.get().findElement(By.xpath(schema.get("NEXT")))) != null){
					next.sendKeys(Keys.RETURN);
			 		performDynamicSleep(site);
					System.out.println("Gabbar: Showmoring Page[" + showmore + "]");
				}
			}while(next != null);				
		}catch(Exception e){}
		System.out.println("Gabbar: Page load complete");
	}
	
	

	private List<String> loadProducts(String site){
		String display = schema.get("DISPLAY");
		if(display.equalsIgnoreCase("MATRIX")){
			return loadProductsMatrix(site);
		}else{
			return loadProductsList(site);
		}
	}
	
	
	private List<String> loadProductsMatrix(String site){
		List<String> lines = new ArrayList<String>();
		System.out.println("Gabbar: Products are displayed in MATRIX mode.");

		int count = 1;
		int rows = Driver.get().findElements(By.xpath(schema.get("ROWS"))).size();
		for(int row = 1 ; row <= rows ; row++){
			String xpathRows = schema.get("COLS").replace("%ROW%", Integer.toString(row));
			int cols = Driver.get().findElements(By.xpath(xpathRows)).size();
			for(int col = 1 ; col <= cols ; col++){
				String xpathUrl = schema.get("URL").replace("%ROW%", Integer.toString(row))
					.replace("%COL%", Integer.toString(col)).replace("/@href", "");
				try{
					String url = Driver.get().findElement(By.xpath(xpathUrl)).getAttribute("href").trim();
					lines.add(url);
					System.out.println("Gabbar: URL[" + Integer.toString(count++) +"] = [" + url + "]");
				}catch(Exception e){ }
			}
		}
		System.out.println("Gabbar: ProductList Complete. Processed [" + lines.size() + "] URLs");	
		return lines;
	}
	
	
	private List<String> loadProductsList(String site){
		List<String> lines = new ArrayList<String>();
		System.out.println("Gabbar: Products are displayed in LIST mode.");
		String xpathUrl = null;
		int items = Driver.get().findElements(By.xpath(schema.get("ITEMS"))).size();
		for(int item = 1 ; item <= items ; item++){
			try{
				xpathUrl = schema.get("URL").replace("%ITEM%", Integer.toString(item)).replace("/@href", "");
				String url = Driver.get().findElement(By.xpath(xpathUrl)).getAttribute("href").trim();
				lines.add(url);
				System.out.println("Gabbar: URL[" + Integer.toString(item) +"] = [" + url + "]");		
			}catch(Exception e){
				System.out.println("Gabbar: URL[" + Integer.toString(item) +"] not found at xpath = [" + xpathUrl + "]");						
			}
		}
		System.out.println("Gabbar: Processing Products. Processed [" + lines.size() + "] URLs");	
		return lines;
	}

	
	private List<String> loadSingle(String site){
		List<String> lines = new ArrayList<String>();
		System.out.println("Gabbar: Products are displayed in SINGLE mode.");
		String xpathUrl = null;
		String url = Driver.get().findElement(By.xpath(schema.get("URL"))).getAttribute("content").trim();
		lines.add(url);
		System.out.println("Gabbar: Processing Products. Processed [" + lines.size() + "] URLs");	
		return lines;
	}



	private void writeProducts(String filename, List<String> lines){
		try {
			if(!Files.exists(Paths.get(Constants.DATA_DIR))){
				Files.createDirectory(Paths.get(Constants.DATA_DIR));
			}
			Files.write(Paths.get(filename), lines, Charset.forName("UTF-8"));
			System.out.println("Gabbar: Totally processed [" + lines.size() + "] URLs. Writing ProductsList to [" + filename + "]");	
		} catch (IOException e) {
			System.out.println("Gabbar: Error Writing ProductsList to [" + filename + "]");	
			e.printStackTrace();
		}
	}
	
	/************************************************************/
	/*						HELPER METHODS						*/
	/************************************************************/	
	private void performDynamicSleep(String site){
		try{
	 		schema = Schema.readProductsList(site);
			Utils.sleep(Integer.parseInt(schema.get("SLEEP")));
		}catch (Exception e){}
	}

	private String getFilename(String site){
		SimpleDateFormat sdf = new SimpleDateFormat("MMMddyyyy_HHmmss");
		return Constants.DATA_DIR + "/" + site + "_" + Constants.PRODUCTS + "_" + sdf.format(new Date()) + ".csv";
	}
	
	private static WebElement getNextButton(String site, int page){
		String next = schema.get("NEXT");
		if(site.contains("FLIPKART")){
			next = next.replace("%PAGE%", Integer.toString(page));
		}
		return Driver.get().findElement(By.xpath(next));
	}
	
	private static void clickElement(WebElement elementName) {
		JavascriptExecutor js = (JavascriptExecutor) Driver.get();
		String toExecute = "var x = document.getElementById('" + elementName.getAttribute("id") + "'); x.click();";
		js.executeScript(toExecute);
	}

	public boolean isUrlExists(String url){
		boolean success = false;
		try{
			HttpURLConnection huc = (HttpURLConnection) new URL(url).openConnection();
			huc.setRequestMethod("HEAD");
			int response = huc.getResponseCode();
			if(response >= 200 && response < 300){
				success = true;			
			}
		}catch (Exception e){}
		return success;
	}

	
	public static boolean pageNextFluentWait(WebElement element, long timeout){
        Utils.sleep(3000);
		boolean changed = false;
		final WebElement we = element;
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(Driver.get()).
				withTimeout(timeout, TimeUnit.SECONDS).
				pollingEvery(2, TimeUnit.SECONDS);
			
			changed = wait.until( new ExpectedCondition<Boolean>(){
				public Boolean apply(WebDriver driver){
					if(we.getText().isEmpty()){ return false; } else { return true; }
				}
			});
		} catch (Exception e) {
			changed = false;
			e.printStackTrace();
		}
		return changed;
	}

}
