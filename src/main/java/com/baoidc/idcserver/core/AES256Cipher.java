package com.baoidc.idcserver.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 加密解密
 * @author Administrator
 *
 */
public class AES256Cipher {

    /**
     * Encryption mode enumeration
     */
    private enum EncryptMode {
        ENCRYPT, DECRYPT;
    }

    // cipher to be used for encryption and decryption
    Cipher _cx;

    // encryption key and initialization vector
    byte[] _key, _iv;


    public AES256Cipher() throws NoSuchAlgorithmException, NoSuchPaddingException {
        // initialize the cipher with transformation AES/CBC/PKCS5Padding
        _cx = Cipher.getInstance("AES/CBC/PKCS5Padding");
        _key = new byte[32]; //256 bit key space
        _iv = new byte[16]; //128 bit IV
    }

    /**
     * Note: This function is no longer used.
     * This function generates md5 hash of the input string
     * @param inputString
     * @return md5 hash of the input string
     */
    public static final String md5(final String inputString) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(inputString.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     *
     * @param _inputText
     *            Text to be encrypted or decrypted
     * @param _encryptionKey
     *            Encryption key to used for encryption / decryption
     * @param _mode
     *            specify the mode encryption / decryption
     * @param _initVector
     * 	      Initialization vector
     * @return encrypted or decrypted string based on the mode
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    private String encryptDecrypt(String _inputText, String _encryptionKey,
                                  EncryptMode _mode, String _initVector) throws IOException,
            InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        BASE64Encoder base64Encoder = new BASE64Encoder();
        BASE64Decoder base64Decoder = new BASE64Decoder();
        String _out = "";// output string
        //_encryptionKey = md5(_encryptionKey);
        //System.out.println("key="+_encryptionKey);

        int len = _encryptionKey.getBytes("UTF-8").length; // length of the key	provided

        if (_encryptionKey.getBytes("UTF-8").length > _key.length)
            len = _key.length;

        int ivlen = _initVector.getBytes("UTF-8").length;

        if(_initVector.getBytes("UTF-8").length > _iv.length)
            ivlen = _iv.length;

        System.arraycopy(_encryptionKey.getBytes("UTF-8"), 0, _key, 0, len);
        System.arraycopy(_initVector.getBytes("UTF-8"), 0, _iv, 0, ivlen);
        //KeyGenerator _keyGen = KeyGenerator.getInstance("AES");
        //_keyGen.init(128);

        SecretKeySpec keySpec = new SecretKeySpec(_key, "AES"); // Create a new SecretKeySpec
        // for the
        // specified key
        // data and
        // algorithm
        // name.

        IvParameterSpec ivSpec = new IvParameterSpec(_iv); // Create a new
        // IvParameterSpec
        // instance with the
        // bytes from the
        // specified buffer
        // iv used as
        // initialization
        // vector.

        // encryption
        if (_mode.equals(EncryptMode.ENCRYPT)) {
            // Potentially insecure random numbers on Android 4.3 and older.
            // Read
            // https://android-developers.blogspot.com/2013/08/some-securerandom-thoughts.html
            // for more info.
            _cx.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);// Initialize this cipher instance
            byte[] results = _cx.doFinal(_inputText.getBytes("UTF-8")); // Finish
            // multi-part
            // transformation
            // (encryption)
           // BASE64Decoder
                    //Base64.encodeToString(results, Base64.DEFAULT)
            _out = base64Encoder.encode(results); // ciphertext
            // output
        }

        // decryption
        if (_mode.equals(EncryptMode.DECRYPT)) {
            _cx.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);// Initialize this ipher instance

            byte[] decodedValue = base64Decoder.decodeBuffer(_inputText);
            byte[] decryptedVal = _cx.doFinal(decodedValue); // Finish
            // multi-part
            // transformation
            // (decryption)
            _out = new String(decryptedVal);
        }
        return _out; // return encrypted/decrypted string
    }

    /***
     * This function computes the SHA256 hash of input string
     * @param text input text whose SHA256 hash has to be computed
     * @param length length of the text to be returned
     * @return returns SHA256 hash of input text
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String SHA256 (String text, int length) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        String resultStr;
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(text.getBytes("UTF-8"));
        byte[] digest = md.digest();

        StringBuffer result = new StringBuffer();
        for (byte b : digest) {
            result.append(String.format("%02x", b)); //convert to hex
        }
        //return result.toString();

        if(length > result.toString().length())
        {
            resultStr = result.toString();
        }
        else
        {
            resultStr = result.toString().substring(0, length);
        }

        return resultStr;

    }

    /***
     * This function encrypts the plain text to cipher text using the key
     * provided. You'll have to use the same key for decryption
     *
     * @param _plainText
     *            Plain text to be encrypted
     * @param _key
     *            Encryption Key. You'll have to use the same key for decryption
     * @param _iv
     * 	    initialization Vector
     * @return returns encrypted (cipher) text
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */

    public String encrypt(String _plainText, String _key, String _iv)
            throws InvalidKeyException, IOException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException,
            BadPaddingException {
        return encryptDecrypt(_plainText, _key, EncryptMode.ENCRYPT, _iv);
    }

    /***
     * This funtion decrypts the encrypted text to plain text using the key
     * provided. You'll have to use the same key which you used during
     * encryprtion
     *
     * @param _encryptedText
     *            Encrypted/Cipher text to be decrypted
     * @param _key
     *            Encryption key which you used during encryption
     * @param _iv
     * 	    initialization Vector
     * @return encrypted value
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public String decrypt(String _encryptedText, String _key, String _iv)
            throws InvalidKeyException, IOException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException,
            BadPaddingException {
        return encryptDecrypt(_encryptedText, _key, EncryptMode.DECRYPT, _iv);
    }

    /**
     * this function generates random string for given length
     * @param length
     * 				Desired length
     * * @return
     */
    public static String generateRandomIV(int length)
    {
        SecureRandom ranGen = new SecureRandom();
        byte[] aesKey = new byte[16];
        ranGen.nextBytes(aesKey);
        StringBuffer result = new StringBuffer();
        for (byte b : aesKey) {
            result.append(String.format("%02x", b)); //convert to hex
        }
        if(length> result.toString().length())
        {
            return result.toString();
        }
        else
        {
            return result.toString().substring(0, length);
        }
    }

    public static String generateRandomKEY(int length)
    {
        SecureRandom ranGen = new SecureRandom();
        byte[] aesKey = new byte[1];
        ranGen.nextBytes(aesKey);
        StringBuffer result = new StringBuffer();
        for (byte b : aesKey) {
            result.append(String.format("%02x", b)); //convert to hex
        }
        if(length> result.toString().length())
        {
            return result.toString();
        }
        else
        {
            return result.toString().substring(0, length);
        }
    }

    public static void main(String[] args) throws Exception{
        AES256Cipher a = new AES256Cipher();
       //System.out.println("Date:"+a.encrypt("378","0123456789abcdefghijklmnopqrstuv",""));
      System.out.println("Date:"+a.encrypt("123456","lanysec",""));
      System.out.println(a.decrypt("pxt70EXZgcRSzGFW51/etA==", "lanysec", ""));
       //System.out.println("Date:" + a.decrypt("6pkkcHN7nyQlCAUGzahyyg==", "0123456789abcdefghijklmnopqrstuv", ""));
    }
}

