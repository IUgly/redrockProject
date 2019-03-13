package team.redrock.volunteer.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Base64;

/**
 * @author ugly
 */
public class Decrypt {

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

    public Decrypt() {
    }

    public static void main(String[] args) {
        String sec = "BzHrwqeLMuBZSnLGZv2UiXyaNaGBvJXs3K92NxvyKfQ=";

        String password = Decrypt.decrypt(sec);
        System.out.println(password);
    }

    public static String decrypt(String secret){
        try {
            Base64.Decoder b64decoder = Base64.getUrlDecoder();
            byte[] sectoken = b64decoder.decode(secret);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec sKey = new SecretKeySpec(seckey, "AES");
            cipher.init(Cipher.DECRYPT_MODE, sKey, new IvParameterSpec(iv));
            byte[] rawtoken = cipher.doFinal(sectoken);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(rawtoken));
            return (String) ois.readObject();
        }
        catch(Exception e) {
            return null;
        }
    }
}