package tiger;

public class Gabbar {

	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println("Usage: Gabbar -list <product_base_url> <site_type> | "
				+ "-verify <product_csv> <site_type> | "
				+ "-attributes <product_csv> <site_type> | "
				+ "-all] <[product_base_url]> <site_type>");
			System.exit(Constants.INVALID);
		}
	
		Driver.init();
		if(args[0].equalsIgnoreCase("-list")){
			new ProductsList().build(args[1], args[2]);			
		}else if(args[0].equalsIgnoreCase("-verify")){
			new ProductsList().verify(args[1]);					
		}else if(args[0].equalsIgnoreCase("-attributes")){
			new Attributes().build(args[1], args[2]);
		}if(args[0].equalsIgnoreCase("-all")){
			String filename = new ProductsList().build(args[1], args[2]);
			new Attributes().build(filename, args[2]);		
		}else{			
		}
		Driver.exit();


				
		
//		GABBAR -LIST		
//		String url = "https://www.flipkart.com/search?q=royal%20sunglasses&sid=26x&as=on&as-show=on&otracker=start&as-pos=1_1_ic_royal%20sun";
//		String site = "FLIPKART_SELLER";			
//		String url = "https://www.flipkart.com/search?q=iphone&otracker=start&as-show=on&as=off";
//		String site = "FLIPKART";			
//		String url = "https://www.snapdeal.com/search?keyword=inspiron&santizedKeyword=laptops&catId=0&categoryId=0&suggested=false&vertical=p&noOfResults=20&searchState=&clickSrc=go_header&lastKeyword=&prodCatId=&changeBackToAll=false&foundInAll=false&categoryIdSearched=&cityPageUrl=&categoryUrl=&url=&utmContent=&dealDetail=&sort=rlvncy";
//		String site = "SNAPDEAL";
//		String url = "https://www.snapdeal.com/seller/S5a0fb";
//		String site = "SNAPDEAL";
//		String url = "https://www.amazon.in/s?marketplaceID=A21TJRUUN4KGV&me=AT95IG9ONZD7S&merchant=AT95IG9ONZD7S&redirect=true";
//		String site = "AMAZON";			
//		String url = "https://www.amazon.in/sp?_encoding=UTF8&asin=&isAmazonFulfilled=1&isCBA=&marketplaceID=A21TJRUUN4KGV&orderID=&seller=AT95IG9ONZD7S&tab=&vasStoreID=";
//		String site = "AMAZON_SELLER";
//		String url = "https://www.amazon.in/s?marketplaceID=A21TJRUUN4KGV&me=A30FCV22A5XI71&merchant=A30FCV22A5XI71&redirect=true";
//		String site = "AMAZON";
//		String url = "http://www.amazon.in/s/ref=bl_dp_s_web_1350380031?ie=UTF8&node=1350380031&field-brandtextbin=VB+Enterprise";
//		String site = "AMAZON";
//		String url = "http://www.amazon.in/s/ref=nb_sb_noss?url=search-alias%3Daps&field-keywords=iphone+6&rh=i%3Aaps%2Ck%3Aiphone+6";
//		String site = "AMAZON";
//		String url = "https://paytm.com/shop/g/jack-and-jones?src=1&q=jack%20&%20jones";
//		String site = "PAYTMMALL";
//		String url = "https://paytm.com/shop/g/electronics-store/lt?src=1&q=lenovo%20laptop";
//		String site = "PAYTMMALL";
//		String url = "https://paytm.com/@sri-santosh-enterprises/shop/search/?merchant=88843&=&availability=1";
//		String site = "PAYTM";
//		String url = "http://www.shopclues.com/search?q=envy&sc_z=4444&z=0";
//		String site = "SHOPCLUES";
//		String url = "http://www.shopclues.com/search?q=Rosella&sc_z=4444&z=0";
//		String site = "SHOPCLUES";	
//		String url = "https://www.amazon.in/s?marketplaceID=A21TJRUUN4KGV&me=A30FCV22A5XI71&merchant=A30FCV22A5XI71&redirect=true";
//		String site = "AMAZON";
//		String url = "https://www.flipkart.com/bags-wallets-belts/pr?sid=reh&q=edel&otracker=categorytree";
//		String site = "FLIPKART_SELLER";
//		String url = "http://www.amazon.in/s/ref=sr_nr_p_6_0?fst=as%3Aoff&rh=n%3A1571271031%2Cp_4%3ABY+DESIGN%2Cp_6%3AA32WJO188D1PRH&bbn=1571271031&ie=UTF8&qid=1487837735&rnid=1318474031";
//		String url = "http://www.amazon.in/s/ref=sr_nr_p_6_0?fst=as%3Aoff&rh=n%3A1571271031%2Cp_4%3ABY+DESIGN%2Cp_6%3AA32WJO188D1PRH&bbn=1571271031&ie=UTF8&qid=1487837735&rnid=1318474031";
//		String site = "AMAZON";
//		String url = "https://www.snapdeal.com/seller/Sfa985";
//		String site = "SNAPDEAL";
//		String url = "https://www.zoyaclothing.com/sarees";
//		String site = "ZOYA";	
//		String url = "http://anmol-jewell-and.shopclues.com/";
//		String site = "SHOPCLUES";
//		String url = "http://www.amazon.in/s/ref=bl_dp_s_web_5866078031?ie=UTF8&node=5866078031&field-brandtextbin=GEMKOLABWELL";
//		String site = "AMAZON";
//		String url = "https://paytm.com/shop/search?merchant=45711&mn=sangam%20enterprises";
//		String site = "PAYTMMALL";
//		String url = "https://www.flintstop.com/Stationery-depid-492410-page-1.html";
//		String site = "FLINTSTOP";
//		String url = "https://paytmmall.com/shop/search?merchant=45074&mn=Lee%20Marc&availability=1";
//		String site = "PAYTMMALL";
//		String url = "https://www.flipkart.com/search?q=Curatio&otracker=start&as-show=on&as=off";
//		String site = "FLIPKART_SELLER";
//		String url = "http://shop.skybags.co.in/products/everyday/backpacks?p=1";
//		String site = "SKYBAGS";
//		String url = "http://www.craftsvilla.com/Quickshop48";
//		String site = "CRAFTSVILLA";
//		String url = "https://kraftly.com/search?q=sahjanandcreation";
//		String site = "KRAFTLY";
//		String url = "https://www.amazon.in/s/ref=sr_il_ti_merchant-items?me=A11Q8CJYF0KWLE&rh=i%%3Amerchant-items&ie=UTF8&qid=1503967834&lo=merchant-items";
//		String site = "AMAZON";

		
		
// 		GABBAR -LIST
//		Driver.init();
//		new ProductsList().build(url, site);
//		Driver.exit();

		

// 		GABBAR -VERIFY
//		String csv = "ProductsList_Jun242017_173823.csv";	
//		Driver.init();
//		new ProductsList().verify(csv);
//		Driver.exit();


// 		GABBAR -ATTRIBUTES
//		String csv = "Products.csv";
//		String site = "CRAFTSVILLA";	
//		Driver.init();
//		new Attributes().build(csv, site);
//		Driver.exit();


		
// 		GABBAR -ALL
//		Driver.init();
//		String filename = new ProductsList().build(url, site);
//		new Attributes().build(filename, site);		
//		Driver.exit();
	
	}	
}
