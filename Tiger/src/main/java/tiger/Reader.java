package tiger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Reader {

	private Map<String, String> schema = null;
	private List<String> products = null;

	public Map<String, String> getSchema() { return schema; }
	public List<String> getProducts() { return products; }
	
	public void read(){
		schema = readSchema();
		products = readProducts();
	}

	private Map<String, String> readSchema() {
		int reserve=2;
		Map<String, String> schema = new TreeMap<String, String>();
		try {
			List<String> sites = Files.readAllLines(Paths.get(Constants.FILEBASE + "_Schema.csv"), Charset.forName("UTF-8"));
			for (int site = 1; site < sites.size(); site++) {
				String attributes="";
				String line = sites.get(site);
				String tokens[] = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
				
				for (int col = 0 ; col < (tokens.length-reserve) ; col++) {
					String name = sites.get(0).split(Constants.STD_SEP)[col + reserve];
					String value = tokens[col + reserve].replace("\"", "");
					attributes += name + Constants.VALUE_SEP + value + Constants.TOKEN_SEP;
				}
				schema.put(tokens[1].toLowerCase(), attributes.substring(0, attributes.length()-2));
			}
			return schema;
		} catch (IOException e) {}
		return null;
	}
	

	private List<String> readProducts() {
		try {
			return Files.readAllLines(Paths.get(Constants.FILEBASE + "_Products.csv"), Charset.forName("UTF-8"));
		} catch (IOException e) {}
		return null;
	}
}
