package tiger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Schema {

	
	public static Map<String, Map<String, String>> readProductsList() {
		return readSchema(Constants.SCHEMA_PRODUCTS, "ProductsList");
	}

	
	public static Map<String, Map<String, String>> readAttributes() {
		return readSchema(Constants.SCHEMA_ATTRIBUTES, "Attributes");
	}

	public static Map<String, String> readProductsList(String site) {
		return readSchema(Constants.SCHEMA_PRODUCTS, "ProductsList", site);
	}

	
	public static Map<String, String> readAttributes(String site) {
		return readSchema(Constants.SCHEMA_ATTRIBUTES, "Attributes", site);
	}
	
	
	
	private static Map<String, Map<String, String>> readSchema(String filename, String type) {
		try {
			Map<String, Map<String, String>> schema = new LinkedHashMap<String, Map<String, String>>();

			System.out.println("Gabbar: Begin Processing " + type + " schema");
			List<String> sites = Files.readAllLines(Paths.get(filename), Charset.forName("UTF-8"));
			String header[] = sites.get(0).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
			for (int i=1 ; i<sites.size() ; i++){
				Map<String, String> fields = new LinkedHashMap<String, String>();
				String values[] = sites.get(i).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

				System.out.println("Gabbar: Processing " + type + " Schema for [" + values[0] + "]");
				for(int j=1 ; j<values.length ; j++){
					fields.put(header[j], values[j].replace("\"", ""));
				}
				schema.put(values[0], fields);
			}
			System.out.println("Gabbar: Processing " + type + " schema complete");
			return schema;
		} catch (IOException e) {
			System.out.println("Gabbar: Error while processing " + type +" schema!");
		}
		return null;
	}

	
	private static Map<String, String> readSchema(String filename, String type, String site) {
		try {
			Map<String, String> schema = new LinkedHashMap<String, String>();
			List<String> sites = Files.readAllLines(Paths.get(filename), Charset.forName("UTF-8"));

			String header[] = sites.get(0).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
			for (int i=1 ; i<sites.size() ; i++){
				String values[] = sites.get(i).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
				if(site.equalsIgnoreCase(values[0])){
					for(int j=1 ; j<values.length ; j++){
						schema.put(header[j], values[j].replace("\"", ""));
					}
				}
			}
			return schema;
		} catch (IOException e) {
			System.out.println("Gabbar: Error while processing " + type +" schema!");
		}
		return null;
	}


}
