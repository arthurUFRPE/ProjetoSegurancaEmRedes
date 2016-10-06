package criptography;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class CriptographyManager {
	
	private PrivateKey privateKey;
	private PublicKey publicKey;
	private AES aes;
	private byte[] macKey;
	
	public static final int ASYNCHRONOUS_MODE = 0;
	public static final int SYNCHRONOUS_MODE = 1;
	
	
	public String encryptToSend(Object o, int mode){
		
		byte[] msg = convertTobyte(o);
		System.out.println("Entrada:"+mode+" |"+Base64.encode(msg));
		String encr = null;
		switch (mode) {
		case ASYNCHRONOUS_MODE:
			//encr = new RSA().criptografa(msg, publicKey);
			encr = new RSAnew().encrypt("public.key", msg);
			break;
		case SYNCHRONOUS_MODE:
			try {
				encr = Base64.encode(aes.encrypt(msg));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			return null;
		}
		
		return encr;
		
	}
	
	public void gerarMACandAES(){
		aes = new AES().gerarChave();
		macKey = new RSA().geraMACkey();
	}
	
	public Object decryptToRead(String msg, int mode){
		
		byte[] temp = null;
		switch (mode) {
		case ASYNCHRONOUS_MODE:
			//temp = new RSA().decriptografa(msg, privateKey);
			temp = new RSAnew().decrypt("private.key", msg);
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
		System.out.println("Saída: "+mode+" |"+ Base64.encode(temp));
		return convertFromByte(temp);
	}
	
	
	private static byte[] convertTobyte(Object o) {
		try {
			String str;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			byte[] objeto = baos.toByteArray();
			oos.close();
			return objeto;
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
	
	public static Object convertFromString(String str) {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(str));
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (Cliente) ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public byte[] criptografaMAC(byte[] key, Object o){
		byte[] msg = convertTobyte(o);
		
		SecretKey keyMac = (SecretKey) convertFromByte(key);
		
		try {
			return new RSA().retornaMAC(msg, keyMac);
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
		
		return null;
	}
	
	public boolean verificaMAC(byte[] key, Object textoPleno, byte[] textoMac){
		byte[] msg = convertTobyte(textoPleno);
		
		SecretKey keyMac = (SecretKey) convertFromByte(key);
		
		return new RSA().verificaMAC(msg, keyMac, textoMac);
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
	public AES getAes() {
		return aes;
	}

	public void setAes(AES aes) {
		this.aes = aes;
	}

	public byte[] getMacKey() {
		return macKey;
	}

	public void setMacKey(byte[] macKey) {
		this.macKey = macKey;
	}
	

}
