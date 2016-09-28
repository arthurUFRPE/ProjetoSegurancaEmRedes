package criptography;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class CriptographyManager {
	
	private PrivateKey privateKey;
	private PublicKey publicKey;
	private AES aes;
	private String macKey;
	
	public static final int ASYNCHRONOUS_MODE = 0;
	public static final int SYNCHRONOUS_MODE = 1;
	
	
	public String encryptToSend(Object o, int mode){
		
		String msg = convertToString(o);
		String encr = null;
		switch (mode) {
		case ASYNCHRONOUS_MODE:
			encr = new RSA().criptografa(msg, publicKey);
			break;
		case SYNCHRONOUS_MODE:
			try {
				encr = Base64.encode(aes.encrypt(Base64.decode(msg)));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			return null;
		}
		
		return encr;
		
	}
	
	public Object decryptToRead(String msg, int mode){
		
		byte[] temp = null;
		switch (mode) {
		case ASYNCHRONOUS_MODE:
			temp = new RSA().decriptografa(msg, privateKey);
			break;
		case SYNCHRONOUS_MODE:
			try {
				temp = aes.decrypt(Base64.decode(msg));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			return null;
		}
		
		return convertFromByte(temp);
	}
	
	
	private static String convertToString(Object o) {
		try {
			String str;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			byte[] objeto = baos.toByteArray();
			str = Base64.encode(objeto);
			oos.close();
			return str;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Object convertFromByte(byte[] str) {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(str);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	@SuppressWarnings("resource")
	public PrivateKey readPrivateKey(){
		ObjectInputStream inputStream;
		try {
			inputStream = new ObjectInputStream(new FileInputStream("private.key"));
	        PrivateKey chavePrivada = (PrivateKey) inputStream.readObject();
			return chavePrivada;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;	
			
	}
	
	@SuppressWarnings("resource")
	public PublicKey readPublicKey(){
		
		ObjectInputStream inputStream;
		try {
			inputStream = new ObjectInputStream(new FileInputStream("public.key"));
	        PublicKey chavePublica = (PublicKey) inputStream.readObject();
			return chavePublica;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}
		
	public PrivateKey getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}
	public PublicKey getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}
	public AES getAes(boolean generateAES) {
		if(aes == null && generateAES)
			aes = new AES().gerarChave();
		return aes;
	}

	public void setAes(AES aes) {
		this.aes = aes;
	}

	public String getMacKey() {
		return macKey;
	}

	public void setMacKey(String macKey) {
		this.macKey = macKey;
	}
	

}
