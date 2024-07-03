package com.hycan.idn.tsp.message.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES工具类
 *
 * @author liangliang
 * @date 2022/12/26
 */
public class AESUtil {
    /**
     * 关键算法
     */
    private static final String KEY_ALGORITHM = "AES";
    /**
     * 密码算法
     */
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * 加密操作
     *
     * @param secret 秘钥
     * @param data   需要进行加密的原文
     * @return String 数据密文，加密后的数据
     */
    public static String encrypt(String secret, String data) throws Exception {
        // 转换密钥
        final Key key = new SecretKeySpec(Base64.getDecoder().decode(secret), KEY_ALGORITHM);
        final Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        // 加密
        cipher.init(Cipher.ENCRYPT_MODE, key);
        final byte[] result = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(result);
    }

    /**
     * 解密操作
     *
     * @param secret 秘钥
     * @param data   需要解密的数据
     * @return String 返回解密后的原文
     */
    public static String decrypt(String secret, String data) throws Exception {
        // 转换密钥
        final Key key = new SecretKeySpec(Base64.getDecoder().decode(secret), KEY_ALGORITHM);
        final Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        // 解密
        cipher.init(Cipher.DECRYPT_MODE, key);
        final byte[] result = cipher.doFinal(Base64.getDecoder().decode(data));
        return new String(result);
    }

    /**
     * 生成AES秘钥
     *
     * @param salt 盐值(sha256(vin_sn_date))
     * @return String 对生成的秘钥进行了Base64编码的字符串
     */
    public static String keyGenerate(String salt) throws Exception {
        // 生成密钥
        final KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(new SecureRandom(salt.getBytes(StandardCharsets.UTF_8)));
        final SecretKey secretKey = keyGenerator.generateKey();
        final byte[] keyBytes = secretKey.getEncoded();
        return Base64.getEncoder().encodeToString(keyBytes);
    }
}
