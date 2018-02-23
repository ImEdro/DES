package DES;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

class DesEncrypter {

    Cipher ecipher;
    Cipher dcipher;

    DesEncrypter(SecretKey key) throws Exception {
        ecipher = Cipher.getInstance("DES");
        dcipher = Cipher.getInstance("DES");
        ecipher.init(Cipher.ENCRYPT_MODE, key);
        dcipher.init(Cipher.DECRYPT_MODE, key);
    }

    public String encrypt(String str) throws Exception {
        // Encode the string into bytes using utf-8
        byte[] utf8 = str.getBytes("UTF8");

        // Encrypt
        byte[] enc = ecipher.doFinal(utf8);

        // Encode bytes to base64 to get a string
        return new sun.misc.BASE64Encoder().encode(enc);
    }

    public String decrypt(String str) throws Exception {
        // Decode base64 to get bytes
        byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

        byte[] utf8 = dcipher.doFinal(dec);

        // Decode using utf-8
        return new String(utf8, "UTF8");
    }
}

public class Cifrado {

    public static void main(String[] argv) throws Exception {
        
    }
    
    private static SecretKey key;
    private static String encodedKey;

    public static String cifrado(String texto)throws Exception {
        clave();
        System.out.println(encodedKey);
        DesEncrypter encrypter = new DesEncrypter(key);
        String encrypted = encrypter.encrypt(texto);
        System.out.println(encrypted);
        String decrypted = encrypter.decrypt(encrypted);
        System.out.println(decrypted);
        return encrypted;
    }

    public static String clave() {
        try {
            key = KeyGenerator.getInstance("DES").generateKey();
            encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
            return encodedKey;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Cifrado.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }
}
