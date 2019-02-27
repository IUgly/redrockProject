package team.redrock.running.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.*;

/**
 * @author ugly
 */
public class Token implements Serializable, List<Object> {
    String userName;
    long expiredTime;

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

    public Token(String userName, Date date)
    {
        this.userName = userName;
        this.expiredTime = date.getTime()/1000+1800;  //半小时
    }

    public String CreateToken()
    {
        try {
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bytestream);
            oos.writeObject(this);

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


    public static Token CreateFrom(String token){
        try {
            Base64.Decoder b64decoder = Base64.getUrlDecoder();
            byte[] sectoken = b64decoder.decode(token);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec sKey = new SecretKeySpec(seckey, "AES");
            cipher.init(Cipher.DECRYPT_MODE, sKey, new IvParameterSpec(iv));
            byte[] rawtoken = cipher.doFinal(sectoken);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(rawtoken));
            return (Token)ois.readObject();
        }
        catch(Exception e) {
            return null;
        }
    }

    public boolean IsExpired() {
        Date date =new Date();
        return date.getTime()/1000 >= expiredTime;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<Object> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(Object o) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public Object get(int index) {
        return null;
    }

    @Override
    public Object set(int index, Object element) {
        return null;
    }

    @Override
    public void add(int index, Object element) {

    }

    @Override
    public Object remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<Object> listIterator() {
        return null;
    }

    @Override
    public ListIterator<Object> listIterator(int index) {
        return null;
    }

    @Override
    public List<Object> subList(int fromIndex, int toIndex) {
        return null;
    }
}