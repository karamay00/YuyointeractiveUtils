package com.yuyointeractive.utils.net.security;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.swing.filechooser.FileSystemView;

/**
 * Created by fenghuaxz on 2016/11/27.
 */

public class RSATookit {

    /**
     * 加密模式
     */
    public static final int ENCRYPT_MODEL=1;

    /**
     * 解密模式
     */
    public static final int DECRYPT_MODEL=2;

    public static final byte[] Exec(Key key, int model, byte[] data){
        try {
            Cipher cipher=Cipher.getInstance("RSA");
            cipher.init(model,key);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建密匙文件 公匙/私匙
     * @return 路径
     */
    public static final String createKeyFiles(){
        try {
            KeyPairGenerator keyPairGen=KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(1024);
            KeyPair keyPair=keyPairGen.generateKeyPair();
            RSAPrivateKey privateKey= (RSAPrivateKey) keyPair.getPrivate();
            RSAPublicKey publicKey= (RSAPublicKey) keyPair.getPublic();
            //获取桌面路径
            String desktop= FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();
            //写出私匙
            ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(desktop+ File.separator+"privateKey.dat"));
            oos.writeObject(privateKey);
            oos.flush();
            oos.close();
            //写出公匙
            oos=new ObjectOutputStream(new FileOutputStream(desktop+File.separator+"publicKey.dat"));
            oos.writeObject(publicKey);
            oos.flush();
            oos.close();
            return desktop;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
