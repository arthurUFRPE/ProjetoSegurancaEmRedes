package criptography;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

/* Exemplo retirado do link: http://www.devmedia.com.br/criptografia-assimetrica-criptografando-e-descriptografando-dados-em-java/31213*/


public class RSA {

	public static final String ALGORITHM = "RSA";

	public static void gerarChave() {
		try {
			final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
			keyGen.initialize(1024);

			final KeyPair key = keyGen.generateKeyPair();

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static byte[] criptografa(String texto, PublicKey chave) {
		byte[] cipherText = null;

		try {
			final Cipher cipher = Cipher.getInstance(ALGORITHM);
			// Criptografa o texto puro usando a chave Púlica
			cipher.init(Cipher.ENCRYPT_MODE, chave);
			cipherText = cipher.doFinal(texto.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cipherText;
	}

	public static String decriptografa(byte[] texto, PrivateKey chave) {
		byte[] dectyptedText = null;

		try {
			final Cipher cipher = Cipher.getInstance(ALGORITHM);
			// Decriptografa o texto puro usando a chave Privada
			cipher.init(Cipher.DECRYPT_MODE, chave);
			dectyptedText = cipher.doFinal(texto);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return new String(dectyptedText);
	}

}
