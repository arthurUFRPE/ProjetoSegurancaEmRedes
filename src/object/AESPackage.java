package object;

import java.io.Serializable;

public class AESPackage implements Serializable{

	private byte[] key;
	private byte[] mackey;
	

	public AESPackage(byte[] key, byte[]chaveMAC) {
		super();
		this.key = key;
		this.mackey = chaveMAC;
	}	

	public byte[] getMackey() {
		return mackey;
	}

	public void setMackey(byte[] mackey) {
		this.mackey = mackey;
	}

	public byte[] getKey() {
		return key;
	}

	public void setKey(byte[] key) {
		this.key = key;
	}
	
	
}
