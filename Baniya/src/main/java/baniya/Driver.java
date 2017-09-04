package baniya;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Driver {
	
	private static WebDriver driver = null;
	public static WebDriver get() { return driver;}
	
	public static void init() {
		try {
			System.setProperty("webdriver.chrome.driver", "C:\\SeleniumDrv\\chromedriver.exe");
			driver = new ChromeDriver();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sleep(int time){	
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {}
	}
}
