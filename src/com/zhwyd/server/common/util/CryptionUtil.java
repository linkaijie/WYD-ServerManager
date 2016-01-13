package com.zhwyd.server.common.util;
import java.io.UnsupportedEncodingException;
/**
 * 自定义加解密算法
 * 
 * @author Administrator
 */
public class CryptionUtil {
    public static final String CHARSET = "UTF-8";

    /**
     * 加密
     * 
     * @param txt   是进行加密的字符串
     * @param key   密匙
     * @return      加密后字符串
     * @throws UnsupportedEncodingException
     */
    public static byte[] Encrypt(String txt, String key) {
        try {
            return Encrypt(txt, key, CHARSET);
        } catch (UnsupportedEncodingException e) {
            // ignore
            return null;
        }
    }

    public static byte[] Encrypt(String txt, String key, String charset) throws UnsupportedEncodingException {
        byte[] bs = txt.getBytes(charset); // 原字符串转换成字节数组
        byte[] keys = key.getBytes(charset); // 密钥转换成字节数组
        byte iRandom = (byte) (Math.random() * bs.length);// 获取随机key的位置
        byte KRandom = (byte) (Math.random() * 255);// 获取随机key
        byte[] rb = new byte[bs.length + 2];
        // 异或
        for (int i = 0; i < bs.length; i++) {
            if (i < iRandom) {
                bs[i] = (byte) (bs[i] ^ keys[i % keys.length]);
                rb[i] = (byte) (bs[i] ^ KRandom);
            } else {
                if (i == iRandom) rb[i] = KRandom;
                bs[i] = (byte) (bs[i] ^ keys[i % keys.length]);
                rb[i + 1] = (byte) (bs[i] ^ KRandom);
            }
        }
        rb[rb.length - 1] = iRandom;
        return rb;
    }

    public static byte[] Encrypt(byte[] bs, String key, String charset) throws UnsupportedEncodingException {
        byte[] keys = key.getBytes(charset); // 密钥转换成字节数组
        byte iRandom = (byte) (Math.random() * bs.length);// 获取随机key的位置
        byte KRandom = (byte) (Math.random() * 255);// 获取随机key
        byte[] rb = new byte[bs.length + 2];
        // 异或
        for (int i = 0; i < bs.length; i++) {
            if (i < iRandom) {
                bs[i] = (byte) (bs[i] ^ keys[i % keys.length]);
                rb[i] = (byte) (bs[i] ^ KRandom);
            } else {
                if (i == iRandom) rb[i] = KRandom;
                bs[i] = (byte) (bs[i] ^ keys[i % keys.length]);
                rb[i + 1] = (byte) (bs[i] ^ KRandom);
            }
        }
        rb[rb.length - 1] = iRandom;
        return rb;
    }

    /**
     * 获取加密字符串
     * @param txt   是进行加密的字符串
     * @param key   密匙
     * @return      加密字符串
     * @throws UnsupportedEncodingException
     */
    public static String getEncryptString(String txt, String key) {
        return bytesToHexString(Encrypt(txt, key));
    }

    public static String Decrypt(byte[] bs, String key) {
        try {
            return Decrypt(bs, key, CHARSET);
        } catch (UnsupportedEncodingException e) {
            // ignore
            return null;
        }
    }

    /**
     * 解密
     * @param txt   要进行解密的字符串
     * @return      解密完成后字符中
     * @throws UnsupportedEncodingException
     */
    public static String Decrypt(byte[] bs, String key, String charset) throws UnsupportedEncodingException {
        byte[] keys = key.getBytes(charset); // 密钥转换成字节数组
        byte iRandom = bs[bs.length - 1];
        byte KRandom = bs[iRandom];
        byte[] rb = new byte[bs.length - 2];
        // 异或
        for (int i = 0; i < rb.length; i++) {
            if (i < iRandom) {
                bs[i] = (byte) (bs[i] ^ KRandom);
                rb[i] = (byte) (bs[i] ^ keys[i % keys.length]);
            } else {
                bs[i + 1] = (byte) (bs[i + 1] ^ KRandom);
                rb[i] = (byte) (bs[i + 1] ^ keys[i % keys.length]);
            }
        }
        // byte数组还原成字符串
        return new String(rb, charset);
    }

    /**
     * 解密
     * @param txt   要进行解密的字符串
     * @param key   解密完成后字符中
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getDecryptString(String txt, String key) {
        return Decrypt(hexStringToBytes(txt), key);
    }

    public static void main(String[] args) {
        // byte[] bs = Encrypt("637-1347523975-459173", Value.PAY_KEY);
        // System.out.println(byteToString(bs));
        // String key = "10,18,17,30,18,0B,06,00,54,0C,1E,05,49,18,0F,0E,01,45,01,08,01,15,09,09,0B,17,05,4D,0C,19,52,41,46,57,41,46,44,5B,43,19,00,03,1C,5A,59,40,5C,5B,43,1C,1C,14,1A,38,1B,19,01,0E,0B,54,59,45,5E,10";
        // System.out.println(key);
        // byte[] data = CryptionUtil.getByteFromHexString(key);
        // String urlString = CryptionUtil.Decrypt(data, Value.PAY_KEY);
        // System.out.println("urlString: " + urlString);
        // System.out.println(getDecryptString("78791c641977120e757b7818701f710b767b731a7e1b07xqlhygmmcyhpfl", "@D%G&J(L"));
    }

    /**
     * 将字符转成16进制
     * @param c 字符
     * @return  16进制
     */
    public static byte getByteFromHexString(char c) {
        char[] mark = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        for (byte i = 0; i < mark.length; i++) {
            if (mark[i] == c) {
                return i;
            }
        }
        return 0;
    }

    /**
     * 将字符串转换成比特数组
     * @param data  字符串
     * @return      比特数组
     */
    public static byte[] getByteFromHexString(String data) {
        String[] datas = data.split(",");
        byte[] logData = new byte[datas.length];
        for (int i = 0; i < logData.length; i++) {
            byte a = getByteFromHexString(datas[i].charAt(0));
            byte b = getByteFromHexString(datas[i].charAt(1));
            logData[i] = (byte) (((a & 0xf) << 4) | (b & 0xf));
        }
        return logData;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * Convert hex string to byte[]
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static String byteToString(byte[] src) {
        char[] mark = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', ','};
        StringBuffer reS = new StringBuffer();
        int t1, t2;
        for (int i = 0; i < src.length; i++) {
            t1 = (src[i] >> 4) & 0xf;
            t2 = src[i] & 0xf;
            reS.append(mark[t1]);
            reS.append(mark[t2]);
            if (i <= src.length - 2) reS.append(mark[0x10]);
        }
        return reS.toString();
    }

    /**
     * Convert char to byte
     * 
     * @param c
     *            char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
    
    /**
     * 加密
     * 
     * @param txt
     * @param key
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] EncryptDispatch(String txt, String key) throws UnsupportedEncodingException {
        byte[] bs = txt.getBytes("utf-8"); // 原字符串转换成字节数组
        byte[] keys = key.getBytes("utf-8"); // 密钥转换成字节数组
        int iRandom = (int) (Math.random() * bs.length) & 0xff;// 获取随机key的位置
        byte KRandom = (byte) (Math.random() * 255);// 获取随机key
        byte[] rb = new byte[bs.length + 2];
        // 异或
        for (int i = 0; i < bs.length; i++) {
            if (i < iRandom) {
                bs[i] = (byte) (bs[i] ^ keys[i % keys.length]);
                rb[i] = (byte) (bs[i] ^ KRandom);
            } else {
                if (i == iRandom) rb[i] = KRandom;
                bs[i] = (byte) (bs[i] ^ keys[i % keys.length]);
                rb[i + 1] = (byte) (bs[i] ^ KRandom);
            }
        }
        rb[rb.length - 1] = (byte) iRandom;
        return rb;
    }
}
