package tiger;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Driver {
	
	private static WebDriver driver = null;
	public static WebDriver get() { return driver;}
	
	public static void init() {
		int sleep = 0;
		try {
			System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
			driver = new ChromeDriver();
			driver.manage().timeouts().implicitlyWait(sleep, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void exit(){
		Driver.get().quit();
	}
}
