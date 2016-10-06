package criptography;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;

import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.util.encoders.Base64;

import object.AESPackage;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class RSAnew {

	private final static String publicKeyFilename = "public.key";
	private final static String privateFilename = "private.key";

	public static void generate() {

		try {

			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

			// Create the public and private keys
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
			BASE64Encoder b64 = new BASE64Encoder();

			SecureRandom random = createFixedRandom();
			generator.initialize(2048, random);

			KeyPair pair = generator.generateKeyPair();
			Key pubKey = pair.getPublic();
			Key privKey = pair.getPrivate();


			BufferedWriter out = new BufferedWriter(new FileWriter(publicKeyFilename));
			out.write(b64.encode(pubKey.getEncoded()));
			out.close();

			out = new BufferedWriter(new FileWriter(privateFilename));
			out.write(b64.encode(privKey.getEncoded()));
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String encrypt (String publicKeyFilename, byte[] inputData){

        String encryptedData = null;
        try {

            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

            BASE64Decoder b64 = new BASE64Decoder();
            String key = readFileAsString(publicKeyFilename);
            AsymmetricKeyParameter publicKey = 
                (AsymmetricKeyParameter) PublicKeyFactory.createKey(b64.decodeBuffer(key));
            AsymmetricBlockCipher e = new RSAEngine();
            e = new org.bouncycastle.crypto.encodings.PKCS1Encoding(e);
            e.init(true, publicKey);

            byte[] hexEncodedCipher = e.processBlock(inputData, 0, inputData.length);

            encryptedData = getHexString(hexEncodedCipher);
   
        }
        catch (Exception e) {
            e.printStackTrace();
        }
       
        return encryptedData;
    }
	
	public byte[] decrypt (String privateKeyFilename, String encryptedData) {

        byte[] outputData = null;
        try {

            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

            String key = readFileAsString(privateKeyFilename);
            BASE64Decoder b64 = new BASE64Decoder();
            AsymmetricKeyParameter privateKey = 
                (AsymmetricKeyParameter) PrivateKeyFactory.createKey(b64.decodeBuffer(key));
            AsymmetricBlockCipher e = new RSAEngine();
            e = new org.bouncycastle.crypto.encodings.PKCS1Encoding(e);
            e.init(false, privateKey);

            byte[] messageBytes = hexStringToByteArray(encryptedData);
            byte[] hexEncodedCipher = e.processBlock(messageBytes, 0, messageBytes.length);

            
            outputData = hexEncodedCipher;

        }
        catch (Exception e) {
            e.printStackTrace();
        }
       
        return outputData;
    }
	
	public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
	
	public static String getHexString(byte[] b) throws Exception {
        String result = "";
        for (int i=0; i < b.length; i++) {
            result +=
                Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }

    private static String readFileAsString(String filePath)
    throws java.io.IOException{
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }
	
	public static SecureRandom createFixedRandom() {
		return new FixedRand();
	}

	private static class FixedRand extends SecureRandom {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		MessageDigest sha;
		byte[] state;

		FixedRand() {
			try {
				this.sha = MessageDigest.getInstance("SHA-1");
				this.state = sha.digest();
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException("can't find SHA-1!");
			}
		}

		public void nextBytes(byte[] bytes) {

			int off = 0;

			sha.update(state);

			while (off < bytes.length) {
				state = sha.digest();

				if (bytes.length - off > state.length) {
					System.arraycopy(state, 0, bytes, off, state.length);
				} else {
					System.arraycopy(state, 0, bytes, off, bytes.length - off);
				}

				off += state.length;

				sha.update(state);
			}
		}
	}
	
	public static void main(String[] args) {
		
		generate();
		// TODO Auto-generated method stub
		CriptographyManager manager = new CriptographyManager();
		manager.gerarMACandAES();
        AESPackage aesPackage = new AESPackage(manager.getAes().getKeySend(), manager.getMacKey());
        
//        System.out.println("AES Antes: "+Base64.encode(aesPackage.getKey()));
//    	System.out.println("MAC Antes: "+Base64.encode(aesPackage.getMackey()));
        
        String msg = manager.encryptToSend("Mensagemasdh ashdhaishdais h diauhsdiuahsiudhai shd a", CriptographyManager.SYNCHRONOUS_MODE);

        Object o = manager.decryptToRead(msg, CriptographyManager.SYNCHRONOUS_MODE);
        if(o instanceof String){
        	System.out.println((String)o);
        }
	}

}
