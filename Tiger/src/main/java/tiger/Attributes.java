package tiger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;

public class Attributes {

	private static Map<String, String> schema = null;
	private static Set<String> labels = new TreeSet<String>();
	private static Map<String, Map<String, String>> techDetails = new TreeMap<String, Map<String, String>>();
	private int startItem = 1, endItem = 1;

	public void build(String csv, String site) {
		System.out.println("Gabbar: Building Attributes for [" + site + "] at CSV [" + csv + "]");

		// Read the Attribute Schema and extract the one corresponding to 'SITE'
		String modSite = site.toUpperCase().replace("_SELLER", "");
		System.out.println("Gabbar: Processing Attributes Schema for [" + site + "]");
		schema = Schema.readAttributes(modSite);		
		List<String> urls = getUrls(csv);
		writeHeader(csv);
		
		// Process for each URL
		setProcessingBoundary(urls.size());
		System.out.println("Gabbar: Found [" + urls.size() + "] URLs in [" + csv + "]. Processing products ["+ startItem + "] thru [" + endItem + "]");

		for(int currentItem = startItem ; currentItem <= endItem ; currentItem++){
			String url = urls.get(currentItem-1);
			System.out.println("Gabbar: Processing Attributes [" + currentItem + "/" + urls.size() + "] for [" + url + "]");
			Driver.get().get(url);
			performDynamicSleep(modSite, "ACTION_SLEEP");
			
			preBuildPage(modSite, "ACTION_SLEEP");
			writeAttributes(csv, url);
			buildTechDetails(url);
		}
		String techFilename = site + Constants.FILE_TECH_DETAILS;
		writeTechDetails(techFilename);
		mergeAttributesWithTechDetails(csv, techFilename);
		deleteFile(techFilename);
		
		System.out.println("Gabbar: Attributes build successfully in [" + csv + "]");
	}
	
	
	private void setProcessingBoundary(int count){
		startItem = 1;
		endItem = count;
		try{
			startItem = Integer.parseInt(schema.get("ACTION_ITEM_START"));
		}catch(Exception e){}
		
		try{
			endItem = Integer.parseInt(schema.get("ACTION_ITEM_END"));
		}catch(Exception e){}

		if((startItem > endItem) || (startItem > count)){
			startItem = 1;
		}
	
		if((endItem < startItem) || (endItem > count)){
			endItem = count;
		}
	}
	
	
	private void preBuildPage(String modSite, String sleep){
		String base = "ACTION_PRECLICK";
		try{
			for(int i = 1 ; i > 0 ; i++){
				String xpath = schema.get(base + i);
				Driver.get().findElement(By.xpath(xpath)).click();
				performDynamicSleep(modSite, "ACTION_PRECLICK_SLEEP");
			}
		}catch (Exception e){}
	}
	
	
	private void performDynamicSleep(String modSite, String sleep){
		try{
			schema = Schema.readAttributes(modSite);
			Utils.sleep(Integer.parseInt(schema.get(sleep)));
		}catch (Exception e){}
	}

	
	private String getImage(String in){
		int index = in.lastIndexOf("/@");
		String xpath = in.substring(0, index);
		String attr = in.substring(index+2, in.length());
		String img = Driver.get().findElement(By.xpath(xpath)).getAttribute(attr).trim();
		return postProcessImage(img);
		
	}
	
	private String postProcessImage(String image){
		String newImage = image;
		String action = schema.get("ACTION_IMAGE");
		try{
			if("REM_?END".equalsIgnoreCase(action)){ // E.g. PaytmMall
				newImage = image.substring(0, image.indexOf('?'));	
			} else if("REP_0x90".equalsIgnoreCase(action)){ // E.g. PayTm
				newImage = image.replaceAll("0x90", "0x720");				
			} else if("REP_._XX_".equalsIgnoreCase(action)){ // E.g. Amazon
				newImage = image.replaceAll("_(.*?)_", "_UL1500_");				
			} else if("REP_128".equalsIgnoreCase(action)){ // E.g. Flipkart
				Matcher m = Pattern.compile("\\((.*?)\\)").matcher(image);
				m.find();
				String tmpImg = m.group(1).replaceAll("\"", "");
				newImage = tmpImg.substring(0, tmpImg.indexOf('?')).replaceAll("128", "720");
			} else if("REP_LAST_7_600-676".equalsIgnoreCase(action)){
			} else if("REP_90x99_494x544".equalsIgnoreCase(action)){
			} else if("REP_14_57".equalsIgnoreCase(action)){
			} 
		}catch(Exception e){ newImage = image; }
		
		return newImage;
	}
	
	
	private String getText(String in){
		String text = Constants.NOT_FOUND;
		try{
			int index = in.lastIndexOf("/@");
			if(index != -1){
				String xpath = in.substring(0, index);
				String attr = in.substring(index+2, in.length());
				text = Driver.get().findElement(By.xpath(xpath)).getAttribute(attr).trim();
			}else{
				text = Driver.get().findElement(By.xpath(in)).getText();
			}
			text = text.replaceAll("\\r|\\n", "").replaceAll("\"", "").trim();
		}catch (Exception e){}
		return text;
	}
	
	
	private List<String> getUrls(String filename){
		try {
			return Files.readAllLines(Paths.get(filename), Charset.forName("UTF-8"));
		} catch (IOException e) {}
		return null;
	}
	
	
	private void writeHeader(String filename){
		String header = "Url,";
		for (String key : schema.keySet()) {
			if(!key.contains("ACTION")){
				header += key + ",";
			}
		}
		writeLine(filename, header, StandardOpenOption.TRUNCATE_EXISTING);
	}
	
	
	private void writeAttributes(String filename, String url){
		String line = "\"" + url + "\",";
		for (Map.Entry<String, String> entry: schema.entrySet()) {
			try{
				if(entry.getKey().contains("ACTION")){ continue; }
				if(entry.getKey().contains("Image")){
					line += "\"" + getImage(entry.getValue()) + "\",";
				}else{
					line += "\"" + getText(entry.getValue()) + "\",";
				}
			}catch(Exception e){ line += "\"" + Constants.NOT_FOUND + "\","; }
		}
		writeLine(filename, line, StandardOpenOption.APPEND);
	}
	
	
	private void buildTechDetails(String url){
		Map<String, String> techDetail = new TreeMap<String, String>();

		for(int i =1 ; i > 0 ; i++){
			String techSection = "ACTION_TECH_SEC" + i;
			String techKey = "ACTION_TECH_KEY" + i;
			String techValue = "ACTION_TECH_VAL" + i;
			if((schema.get(techSection) == null) || (schema.get(techSection).isEmpty()))
				break;

			int sections = getTechnicalSections(techSection);
			for (int section = 1 ; section <= sections ; section++){
				int rows = getTechnicalRows(techKey, section);
				for (int row = 1 ; row <= rows ; row++){
					String key = getTechnicalItem(techKey, section, row);
					String value = getTechnicalItem(techValue, section, row);
					if( (key != null) && (!key.isEmpty()) && (value!= null)){
						labels.add(key);
						techDetail.put(key, value);
					}
				}		
			}
		}
		techDetails.put(url, techDetail);	
	}
	
	
	private void writeTechDetails(String filename){
		System.out.println("Gabbar: Building Technical Details ...");
		String header = "Url," + labels.toString().replace("[", "").replace("]", "");
		writeLine(filename, header, StandardOpenOption.TRUNCATE_EXISTING);
		
		for(Map.Entry<String, Map<String, String>> entry: techDetails.entrySet()){
			String line = entry.getKey() + ",";
			Map <String, String> detail = entry.getValue();
			for(String label: labels){
				String value = detail.get(label);
				if(value == null){ value = Constants.NOT_FOUND; }
				line += "\"" + value + "\",";	
			}
			writeLine(filename, line, StandardOpenOption.APPEND);
		}
		System.out.println("Gabbar: Technical Details Complete");
	}
	

	private void mergeAttributesWithTechDetails(String fileAttr, String fileTech){
		try {
			System.out.println("Gabbar: Merging Attributes and Technical details ...");
			List<String> attributes = Files.readAllLines(Paths.get(fileAttr), Charset.forName("UTF-8"));
			Map<String, String> tech = toMap(Files.readAllLines(Paths.get(fileTech), Charset.forName("UTF-8")));
			
			List<String> lines = new ArrayList<String>();
			for (String attr : attributes){
				String url = attr.substring(0, attr.indexOf(",")).replaceAll("\"",  "");
				lines.add(attr + tech.get(url)); 
				tech.remove(url);		
			}
			Files.write(Paths.get(fileAttr), lines, Charset.forName("UTF-8"), StandardOpenOption.TRUNCATE_EXISTING);
			System.out.println("Gabbar: Attributes and Technical details merged successfully");

			if(tech.size()!= 0){
				throw new Exception("Gabbar: Something went wrong in the merge. Some items were left out. Plz check");
			}
		} catch (Exception e) {
			System.out.println("Gabbar: Error Merging Attributes and Technical details");
		}
	}
	

	
	private int getTechnicalSections(String techGroup){
		int sections = 1;
		try{
			sections = Driver.get().findElements(By.xpath(schema.get(techGroup))).size();
		}catch (Exception e){ }
		return sections;
	}
	
	
	private int getTechnicalRows(String techKey, int section){
		int value = 0;
		try{
			String path = Utils.raplace(schema.get(techKey), "%SEC%", Integer.toString(section));
			path = Utils.raplace(path, "[%ROW%]", "");
			path = Utils.raplace(path, "[%ROW%+1]", "");
			value = Driver.get().findElements(By.xpath(path)).size();		
		}catch(Exception e){}
		return value;
	}
	
	private String getTechnicalItem(String type, int section, int row){
		String value = null;
		try {
			String path = Utils.raplace(schema.get(type), "%SEC%", Integer.toString(section));
			path = Utils.raplace(path, "%ROW%", Integer.toString(row));
			value = Driver.get().findElement(By.xpath(path)).getText().trim().replaceAll("\\r|\\n", " ");
		} catch (Exception e) { }
		return value;
	}
	
	
	
	private void writeLine(String filename, String line, StandardOpenOption option){
		try {
			List<String> lines = new ArrayList<String>();
			lines.add(line);
			Files.write(Paths.get(filename), lines, Charset.forName("UTF-8"), option, StandardOpenOption.CREATE);
		}catch (IOException e) {
			System.out.println("Gabbar: Error writing [" + line + "] in [" + filename + "]");
		}
	}


	private Map<String, String> toMap(List<String> lines){
		Map<String, String> tech = new TreeMap<String, String>();
		for(String line : lines){
			int split = line.indexOf(',');
			String key = line.substring((0), split);
			String val = line.substring(split+1, line.length());		
			tech.put(key, val);
		}
		return tech;
	}

	private void deleteFile(String file){
		try {
			Files.deleteIfExists(Paths.get(file));
		} catch (IOException e) {}
	}

}
