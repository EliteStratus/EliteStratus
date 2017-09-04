package tiger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Writer {
	
	public void write(List<String> reserved, List<String> processed){
		SimpleDateFormat sdf = new SimpleDateFormat("MMMddyyyy_HHmmss");
		String filename = Constants.FILEBASE + "_Output_" + sdf.format(new Date()) + ".csv";
		
		List<String> lines = merge(reserved, processed);
		try {
			Files.write(Paths.get(filename), lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private List<String> merge(List<String> reserved, List<String> processed){
		int count = reserved.size();
		if(count != processed.size())
			return null;
		
		List<String> lines = new ArrayList<String>();
		for (int i=0 ; i<reserved.size() ; i++){
			lines.add(reserved.get(i) + "," + processed.get(i));
		}
		return lines;
	}
}
