package util;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class Encrypter {
	private static final Random RANDOM = new SecureRandom();
        private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static MessageDigest md;;
	private static String generatedPassword = null;
	
          
     public static String getSalt(int length) {
        StringBuilder returnValue = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
            return new String(returnValue);
    }      
        
	public static String getSecurePassword(String passwordToHash) {
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(passwordToHash.getBytes());
			byte[] bytes = md.digest();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] + 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}
	
}