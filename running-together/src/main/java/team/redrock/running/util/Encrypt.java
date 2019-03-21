package team.redrock.running.util;

import com.alibaba.fastjson.JSONObject;

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
public class Encrypt {

    private static final String IV_STRING = "running-together";
    private static final String charset = "UTF-8";
    public static final String KEY = "redrockmobilerun";

    public static void main(String[] args) throws Exception{
        //加密解密
        String run_data = "{\"student_id\":\"2015211878\",\"begin_time\":1478079445,\"end_time\":1478079945,\"lat_lng\":[{\"longitude\":39.999391,\"latitude\":116.135972},{\"longitude\":39.999391,\"latitude\":116.135972},{\"longitude\":39.999391,\"latitude\":116.135972}],\"distance\":66.66,\"steps\":2000,\"date\":\"2016-11-12\"}";
        String sec = Encrypt.aesEncryptString(run_data);
        System.out.println(sec);

        String data = Decrypt.aesDecryptString(sec);
        JSONObject json = JSONObject.parseObject(data);

        System.out.println(json.toJSONString());
    }

    public static String aesEncryptString(String content) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        byte[] contentBytes = content.getBytes(charset);
        byte[] keyBytes = KEY.getBytes(charset);
        byte[] encryptedBytes = aesEncryptBytes(contentBytes, keyBytes);
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(encryptedBytes);
    }

    public static byte[] aesEncryptBytes(byte[] contentBytes, byte[] keyBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        return cipherOperation(contentBytes, keyBytes, Cipher.ENCRYPT_MODE);
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