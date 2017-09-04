package tiger;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;



public class Crypto {
	
	public static void authenticate(String[] args) {	
		if (args.length != 1) {
			System.console().writer().println("Error: Invalid arguments. Enter password");
			System.exit(Constants.INVALID);
		}
		
		int auth = validate(args[0]);
		
		if (auth == Constants.EXPIRED) {
			System.console().writer().println("Error: Expired password");
			System.exit(Constants.EXPIRED);
		}
		if (auth == Constants.INVALID) {
			System.console().writer().println("Error: Invalid password");
			System.exit(Constants.INVALID);
		}
	}
	
	
	//Format: "XXXXDDMMddyyyy"
	public static int validate(String crypt){
		try{
			String clear = Crypto.decrypt(crypt);
			int total = clear.length();
			if(total < 10)
				return Constants.INVALID;
			
			int days = Integer.parseInt(clear.substring(total-10, total-8));
			String issued = clear.substring(total-8, total);
			SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
			long start = sdf.parse(issued).getTime();
			long today = new Date().getTime();
			
			if(today < start + days*24*60*60*1000)
				return Constants.SUCCESS;
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return Constants.EXPIRED;
	}
	
	
	public static String encrypt(String plain){
		String val = null;
		try{
			byte[] encrypt = getCipher(Cipher.ENCRYPT_MODE).doFinal(plain.getBytes());
			val = Base64.encodeBase64String(encrypt);
		}catch (Exception e){
			e.printStackTrace();
		}
		return val;
	}
	
	
	public static String decrypt (String crypt){
		String val = null;
		try{
			byte[] clear = getCipher(Cipher.DECRYPT_MODE).doFinal(Base64.decodeBase64(crypt));
			val = new String(clear);
		}catch(Exception e){
			e.printStackTrace();
		}
		return val;
	}
	
	private static Cipher getCipher(int mode) throws Exception{
		String algo = "AES";
		
		SecretKeySpec keySpec = new SecretKeySpec(getKey(), algo);
		Cipher cipher = Cipher.getInstance(algo);
		cipher.init(mode, keySpec);
		return cipher;
	}
	
	private static byte[] getKey() throws Exception{
		byte[] key = {'!', 's', 'e', 'l', 'a', 'u', 't', 'o', '!'};	
		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		key = sha.digest(key);
		return Arrays.copyOf(key, 16);
	}
	
}
