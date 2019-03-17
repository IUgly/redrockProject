package team.redrock.volunteer.util;//
//  https://github.com/WelkinXie/AESCipher-Java
//

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Tester {

	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
//		String str = "IAmThePlainText";
		String str = "kk123456";
		String sec = Encrypt.aesEncryptString(str);
//		System.out.println(sec.equals("Nt4wouCddJLLDdhSHboF+g=="));
		System.out.println(sec);

		System.out.println(Decrypt.aesDecryptString(sec));
	}
}
