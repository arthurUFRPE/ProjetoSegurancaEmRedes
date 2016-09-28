package criptography;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/* Exemplo retirado do link: http://www.devmedia.com.br/criptografia-assimetrica-criptografando-e-descriptografando-dados-em-java/31213*/


public class RSA {

	public static final String ALGORITHM = "RSA";

	public static void geraChave() {
	      try {
	        final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
	        keyGen.initialize(1024);
	        final KeyPair key = keyGen.generateKeyPair();
	   
	        File chavePrivadaFile = new File("private.key");
	        File chavePublicaFile = new File("public.key");
	   
	        // Cria os arquivos para armazenar a chave Privada e a chave Publica
	        if (chavePrivadaFile.getParentFile() != null) {
	          chavePrivadaFile.getParentFile().mkdirs();
	        }
	        
	        chavePrivadaFile.createNewFile();
	   
	        if (chavePublicaFile.getParentFile() != null) {
	          chavePublicaFile.getParentFile().mkdirs();
	        }
	        
	        chavePublicaFile.createNewFile();
	   
	        // Salva a Chave Pública no arquivo
	        ObjectOutputStream chavePublicaOS = new ObjectOutputStream(
	            new FileOutputStream(chavePublicaFile));
	        chavePublicaOS.writeObject(key.getPublic());
	        chavePublicaOS.close();
	   
	        // Salva a Chave Privada no arquivo
	        ObjectOutputStream chavePrivadaOS = new ObjectOutputStream(
	            new FileOutputStream(chavePrivadaFile));
	        chavePrivadaOS.writeObject(key.getPrivate());
	        chavePrivadaOS.close();
	      } catch (Exception e) {
	        e.printStackTrace();
	      }
	   
	    }

	public String criptografa(String texto, PublicKey chave) {
		byte[] cipherText = null;

		try {
			final Cipher cipher = Cipher.getInstance(ALGORITHM);
			// Criptografa o texto puro usando a chave Púlica
			cipher.init(Cipher.ENCRYPT_MODE, chave);
			cipherText = cipher.doFinal(Base64.decode(texto));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Base64.encode(cipherText);
	}

	public byte[] decriptografa(String obj, PrivateKey chave) {
		byte[] dectyptedText = null;

		try {
			final Cipher cipher = Cipher.getInstance(ALGORITHM);
			// Decriptografa o texto puro usando a chave Privada
			cipher.init(Cipher.DECRYPT_MODE, chave);
			dectyptedText = cipher.doFinal(Base64.decode(obj));

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return dectyptedText;
	}

}
