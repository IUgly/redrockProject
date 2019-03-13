package team.redrock.volunteer.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.*;

/**
 * @author ugly
 */
public class Encrypt {

    private static final byte[] seckey = {
            0x33, 0x21, 0x27, 0x21,
            0x26, 0x73, 0x12, 0x71,
            0x62, 0x42, 0x73, 0x74,
            0x72, 0x4c, 0x5f, 0x66
    };

    private static final byte[] iv = {
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00,
    };

    public Encrypt() {}

    public static void main(String[] args) {
        //使用
        String encryptStr = encrypt("2017211901");
        System.out.println(encryptStr);

    }

    public static String encrypt(String password)
    {
        try {
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bytestream);
            oos.writeObject(password);

            byte[] bintoken = bytestream.toByteArray();
            SecretKeySpec sKey = new SecretKeySpec(seckey, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, sKey, new IvParameterSpec(iv));
            byte[] sectoken = cipher.doFinal(bintoken);

            Base64.Encoder b64encoder = Base64.getUrlEncoder();
            return b64encoder.encodeToString(sectoken);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}