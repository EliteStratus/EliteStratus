package tiger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Passwd {
	
	public static void main(String[] args) {
		String clear = new String(System.console().readPassword("[%s]", "Enter Password"));
		if(clear.length() < 3){
			System.console().writer().println("Error: Format <Random Text><Days>");
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
		clear += sdf.format(new Date());
		String crypt = Crypto.encrypt(clear);
		System.console().writer().println("Success: Your password is [" + crypt + "]");
	}
}
