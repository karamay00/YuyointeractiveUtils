package com.yuyointeractive.utils.net.security;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by fenghuaxz on 2016/11/27.
 */

public class AESTookit {

    /**
     * 加密模式
     */
    public static final int ENCRYPT_MODEL=1;

    /**
     * 解密模式
     */
    public static final int DECRYPT_MODEL=2;


    /**
     * 执行 加密/解密 操作
     * @param pwd 密匙
     * @param model 模式
     * @param data
     * @return 加密/解密 后字节流
     */
    public static final byte[] Exec(String pwd,int model,byte[] data){
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom random=SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(pwd.getBytes());
            kgen.init(128, random);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key=new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher=Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(model,key);
            return cipher.doFinal(data);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
