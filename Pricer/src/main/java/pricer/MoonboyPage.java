package pricer;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

public class MoonboyPage extends LoadableComponent<MoonboyPage> {
	
	@FindBy(xpath = "//div[@class='price_single']/div[@class='col-md-8']/div[@class='prc']/span[@class='regular-price']/span[@id='mrp_spn']") private WebElement textMrp;
	@FindBy(xpath = "//div[@class='price_single']/div[@class='col-md-8']/div[@class='prc']/span[@class='price2']/span[@id='finl_spn']") private WebElement textPrice;
	@FindBy(xpath = "//div[@class='price_single']/div[@class='col-md-8']/div[@class='prc']/div[@class='shipng_spn']") private WebElement textShipping;
	@FindBy(xpath = "//div[@class='price_single']/div[@class='col-md-4']/div[@class='sold-by']/a[@id='goslr']") private WebElement textSeller;

	public MoonboyPage() {
		PageFactory.initElements(Driver.get(), this);
		this.get();
	}
	
	@Override
	protected void isLoaded() throws Error {}

	@Override
	protected void load() {}
	
	public List<String> getProduct(List<String> lines){
		List<String> output = new ArrayList<String>();
		for (String line : lines){
			String url = line.split(",")[5];
			Driver.get().get(url);
			String mrp = textMrp.getText().trim();
			String price = textPrice.getText().trim();
			String shipping = refineShipping(textShipping.getText().trim());
			shipping.replace("(", "");
			String seller = textSeller.getText().trim();
			
			output.add(url + "," + mrp + "," + price + "," + shipping + "," + seller);
		}
		return output;
	}
	
	public String refineShipping(String input){
		if(input.contains("free")){
			input = "Free";
		}else{
			input = input.split("Rs. ")[1];
		}
		return input;
	}
}
