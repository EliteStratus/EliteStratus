package tiger;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;

public class Processor {
	
	private List<String> sites = new ArrayList<String>();			
	private final static int reserve = 8;
	
	public List<String> reserve(Reader reader) {
		
		List<String> lines = new ArrayList<String>();
		for (String product : reader.getProducts()) {
			lines.add(buildReserve(product));
		}
		return lines;
	}


	public List<String> process(Reader reader) {
		int skip = 0;
		List<String> lines = new ArrayList<String>();
		for (String product : reader.getProducts()) {
			if(skip++ == 0){ // First line
				buildSites(product);
				lines.add(buildHeader(reader));
				continue;
			}		
			lines.add(buildAttributes(reader, product));
		}
		return lines;
	}

	
	private void buildSites(String header){
		String[] tokens = header.split(",");
		for (int i = 0; i < tokens.length-reserve ; i++) {
			sites.add(tokens[reserve+i]);
		}
	}
	
	
	private String buildHeader(Reader reader){
		String header = "";
		for(int site=0 ; site<sites.size() ; site++){
			String[] attribs = getSiteSchema(reader, site).split(Constants.TOKEN_SEP);
			for(int attrib=0 ; attrib<attribs.length ; attrib++){
				header += sites.get(site) + "_" + attribs[attrib].split(Constants.VALUE_SEP)[0] + Constants.STD_SEP;
			}
		}
		return header.substring(0, header.length()-1);
	}
	
	
	public String buildReserve(String product) {
		String line = "";
		String[] tokens = product.split(Constants.STD_SEP);
		for (int i = 0; i < reserve ; i++) {
			line += tokens[i] + Constants.STD_SEP;
		}
		return line.substring(0, line.length()-1);
	}

	
	private String buildAttributes(Reader reader, String product){
		String attributes = "";
		String[] tokens = product.split(Constants.STD_SEP);
		for (int i = 0; i < tokens.length - reserve ; i++) {
			String schema = getSiteSchema(reader, i);
			String url = tokens[reserve+i];			
			if((url == null) || (url.isEmpty())){
				attributes += buildEmptyArrtibutes(schema);
			}else{
				Driver.get().get(url);
				attributes += buildFullAttributes(schema);	
			}
		}
		return attributes.substring(0, attributes.length()-1);
	}
	
	
	private String buildEmptyArrtibutes(String in){
		String out = "";
		int tokenCount = in.split(Constants.TOKEN_SEP).length;
		for(int i=0 ; i<tokenCount ; i++){
			out+= ",";				
		}
		return out;
	}
	
	private String buildFullAttributes(String in){
		String out = "";
		String[] tokens = in.split(Constants.TOKEN_SEP);
		for(int field=0 ; field<tokens.length ; field++){
			String[] attr = tokens[field].split(Constants.VALUE_SEP);
			try{
				out += "\"" + Driver.get().findElement(By.xpath(attr[1])).getText().trim() + "\",";
			}catch(Exception e){
				out+= "\"NA\",";				
			}
		}
		return out;
	}

	private String getSiteSchema(Reader reader, int index){
		String siteName = sites.get(index).toLowerCase();	
		return reader.getSchema().get(siteName);
	}

}
