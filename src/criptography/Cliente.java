package criptography;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;


public class Cliente implements Serializable{
	private String nome;
	private String cpf;




	public static String convertToString(Cliente c) {
		try {
			String str;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(c);
			byte[] objeto = baos.toByteArray();
			str = Base64.encode(objeto);
			oos.close();
			return str;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Cliente convertFromString(String str) {
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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}
	
	
	public static void main(String[] args) {
		Cliente c1, c2;
		
		c1 = new Cliente();
		c1.setNome("Zé");
		c1.setCpf("123");
		
		String s = Cliente.convertToString(c1);
		System.out.println(s);
		
		c2 = Cliente.convertFromString(s);
		
		System.out.println(c2.getNome());
		System.out.println(c2.getCpf());
		
		
	}
	

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
}
