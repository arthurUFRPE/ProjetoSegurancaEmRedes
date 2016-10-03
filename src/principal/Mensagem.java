package principal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class Mensagem implements Serializable{
	
	private String mensagem;
	private long count;
	private byte[] macMens;
	

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public byte[] getMacMens() {
		return macMens;
	}

	public void setMacMens(byte[] macMens) {
		this.macMens = macMens;
	}
	
	

}
