package criptography;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/* Exemplo retirado do link: http://www.devmedia.com.br/criptografia-assimetrica-criptografando-e-descriptografando-dados-em-java/31213*/


public class RSA {

	public static final String ALGORITHM = "RSA";
	
	public byte[] geraMACkey(){
		KeyGenerator kg;
		try {
			kg = KeyGenerator.getInstance("HmacSHA256");
	        //final GeraMAC mac;
		    kg.init(56); // 56 tamanho fixo da chave.
		    SecretKey chaveAutenticacao = kg.generateKey();
		    return chaveAutenticacao.getEncoded();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void geraChave() {
	      try {
	        final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
	        		    
	        //final das alteracoes para autenticacao
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
	
	
	//metodo que rertorna o mac(byte array)
	
	public byte[] retornaMAC(String mensagem, SecretKey chave) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException{

	    SecretKey key = chave;

	    Mac mac = Mac.getInstance(chave.getAlgorithm());
	    mac.init(key);
	    
	    String message = mensagem;
	    
	    byte[] b = message.getBytes("UTF-8");

	    byte[] macbytes = mac.doFinal(b);
	  
//	  System.out.println("MAC:: " );
//	  System.out.println(macbytes);
//	  System.out.println(b);
	  
	 return macbytes;
	}
	
	//metodo para verificar os dois macs
public boolean verificaMAC(String mensagem, SecretKey chave, byte[] macAntigo){
	boolean resultado = false;
	byte[] oldMAC = macAntigo;
		
		try {
			
			byte[]macGerado = retornaMAC(mensagem,chave);
			
			if(Arrays.equals(oldMAC, macGerado)){
				
				resultado = true;
			}
			
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultado;
	}

}
