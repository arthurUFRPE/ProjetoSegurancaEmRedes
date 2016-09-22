package criptography;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class CriptographyManager {
	
	private PrivateKey privateKey;
	private PublicKey publicKey;
	private AES aes;
	
	public static final int ASYNCHRONOUS_MODE = 0;
	public static final int SYNCHRONOUS_MODE = 1;
	
	
	public byte[] encryptToSend(Object o, int mode){
		
		byte[] byteObject = convertToString(o);
		byte[] encr = null;
		switch (mode) {
		case ASYNCHRONOUS_MODE:
			encr = new RSA().criptografa(byteObject, publicKey);
			break;
		case SYNCHRONOUS_MODE:
			try {
				encr = aes.encrypt(byteObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			return null;
		}
		
		return encr;
		
	}
	
	public Object decryptToRead(String message){
		
		Object object = null;
		object = convertFromString(message);		
		return object;
	}
	
	
	private static byte[] convertToString(Object o) {
		try {
			String str;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			byte[] objeto = baos.toByteArray();
			str = Base64.encode(objeto);
			oos.close();
			return str.getBytes("UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Object convertFromString(String str) {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(str));
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public void generateKeyPair(){
		KeyPair key = new RSA().gerarChave();
		
		setPrivateKey(key.getPrivate());
		setPublicKey(key.getPublic());
	}
	
	
	public PrivateKey getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}
	public String getPublicKey() {
		return Base64.encode(convertToString(publicKey));
	}
	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}
	public AES getAes() {
		return aes;
	}
	public void setAes(AES aes) {
		this.aes = aes;
	}

}
