package baniya;

public class Baniya {
	
	public static void main(String[] args){
		Driver.init();
		Driver.get().get("http://www.desibaniya.com");

		BaniyaPage page = new BaniyaPage();		
		page.getProductList();
		Driver.get().quit();
	}
}
