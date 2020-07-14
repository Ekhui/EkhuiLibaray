package com.example.ekhuibaselibrary.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
/*
 * Created by Ekhui on 2019/10/8.
 */

public class RSAUtil {

    /**
     * 使用私钥解密
     *
     * @param content
     * @param private_key
     * @param input_charset
     * @return
     * @throws Exception
     */
    public static String decrypt(String content, String private_key, String input_charset) throws Exception {
        PrivateKey prikey = getPrivateKey(private_key);

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, prikey);

        InputStream ins = new ByteArrayInputStream(Base64Util.decode(content));
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        byte[] buf = new byte[128];
        int bufl;

        while ((bufl = ins.read(buf)) != -1) {
            byte[] block = null;

            if (buf.length == bufl) {
                block = buf;
            } else {
                block = new byte[bufl];
                for (int i = 0; i < bufl; i++) {
                    block[i] = buf[i];
                }
            }

            writer.write(cipher.doFinal(block));
        }

        return new String(writer.toByteArray(), input_charset);
    }

    /**
     * 获得私钥
     *
     * @param key 私钥
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {

        byte[] keyBytes;

        keyBytes = Base64Util.decode(key);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        return privateKey;
    }

    /**
     * 得到公钥
     *
     * @param bysKey
     * @return
     */
    private static PublicKey getPublicKeyFromX509(String bysKey) throws Exception {
        byte[] decodedKey = Base64Util.decode(bysKey);
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodedKey);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(x509);
    }

    /**
     * 使用公钥加密
     *
     * @param content 密文
     * @param pub_key 公钥
     * @return
     */
    public static String encryptByPublic(String content, String pub_key) {
        try {
            PublicKey pubkey = getPublicKeyFromX509(pub_key);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubkey);

            byte plaintext[] = content.getBytes("UTF-8");
            byte[] output = cipher.doFinal(plaintext);

            String s = Base64Util.encode(output);

            return s;

        } catch (Exception e) {
            return null;
        }
    }
}