package object;

import java.io.Serializable;

public class AESPackage implements Serializable{

	private byte[] key;

	public AESPackage(byte[] key) {
		super();
		this.key = key;
	}

	public byte[] getKey() {
		return key;
	}

	public void setKey(byte[] key) {
		this.key = key;
	}
	
	
}
