package team.redrock.volunteer.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author ugly
 */
public class Decrypt {

    private static final String IV_STRING = "zhangshangcquptv";
    private static final String charset = "UTF-8";
    public static final String KEY = "redrockvolunteer";

    public static String aesDecryptString(String content) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encryptedBytes = decoder.decode(content);
        byte[] keyBytes = KEY.getBytes(charset);
        byte[] decryptedBytes = aesDecryptBytes(encryptedBytes, keyBytes);
        return new String(decryptedBytes, charset);
    }

    public static void main(String[] args) throws Exception{
        String sec = "Fh413oZXPKxA4G1+8Pif2tlX+Alqh+5M3sexC7bLzs8yntbQVkptd1Pzuk/kgNPJlk2gWO0Lgnw28njvFmMeOT1BmcMl7t8kexnRWH7+LX4HXqGxHvBnSuyw9sG+J7QekAgszX+WiQesil0vSRH+Wmoy3hSCJyIEX8kzKRSaYV4=";

        System.out.println(Decrypt.aesDecryptString(sec));
    }

    public static byte[] aesDecryptBytes(byte[] contentBytes, byte[] keyBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        return cipherOperation(contentBytes, keyBytes, Cipher.DECRYPT_MODE);
    }

    private static byte[] cipherOperation(byte[] contentBytes, byte[] keyBytes, int mode) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

        byte[] initParam = IV_STRING.getBytes(charset);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(mode, secretKey, ivParameterSpec);

        return cipher.doFinal(contentBytes);
    }
}