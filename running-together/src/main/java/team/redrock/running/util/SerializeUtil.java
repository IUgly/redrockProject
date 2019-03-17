package team.redrock.running.util;

import team.redrock.running.vo.InviteInfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

public class SerializeUtil {

    public static void main(String[] args) {
        InviteInfo inviteInfo = new InviteInfo("123321","crown");
        String str = serialize(inviteInfo);

        InviteInfo invitation = (InviteInfo) deserialize(str);

        System.out.println(invitation.getNickname());
    }

    public static String serialize(Object obj){
        try{
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(byteStream);
            oos.writeObject(obj);

            byte[] bintoken = byteStream.toByteArray();

            Base64.Encoder b64encoder = Base64.getUrlEncoder();
            return b64encoder.encodeToString(bintoken);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Object deserialize(String strSerialize){
        try {
            Base64.Decoder b64decoder = Base64.getUrlDecoder();
            byte[] byteStream = b64decoder.decode(strSerialize);

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(byteStream));
            return ois.readObject();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
