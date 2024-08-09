package com.mcac.common.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class EncryptionUtil {
    Context context;
    public EncryptionUtil(Context context) {
        this.context = context;
    }

    public String encByPublicKey(String plain)
            throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Log.d("ttttttttt","plain="+plain);

        String publicKeyF = "fam-public-key.pem";
        InputStreamReader sr = new InputStreamReader(context.getAssets().open(publicKeyF));
        BufferedReader br = new BufferedReader(sr);
        StringBuilder sb = new  StringBuilder();
        String line = "";
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        sr.close();
        PublicKey publicKey = null;
        publicKey = KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(new BASE64Decoder().decodeBuffer(sb.toString())));

        Cipher cipher = null;
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        String encryptedText = null;
        encryptedText = new BASE64Encoder().encode(cipher.doFinal(plain.getBytes("UTF-8")));
        Log.d("ttttttttt","encryptedText="+encryptedText);
        return encryptedText;
    }
}
