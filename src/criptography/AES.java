package criptography;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class AES{

	private final BlockCipher AESCipher = new AESEngine();

	private PaddedBufferedBlockCipher pbbc;
	private KeyParameter key;
	private byte[] keySend;

	public void setPadding(BlockCipherPadding bcp) {
		this.pbbc = new PaddedBufferedBlockCipher(AESCipher, bcp);
	}
	

	public byte[] getKeySend() {
		return keySend;
	}

	public void setKeySend(byte[] keySend) {
		this.keySend = keySend;
	}



	public void setKey(byte[] key) {
		this.key = new KeyParameter(key);
	}

	public byte[] encrypt(byte[] input) throws DataLengthException, InvalidCipherTextException {
		return processing(input, true);
	}

	public byte[] decrypt(byte[] input) throws DataLengthException, InvalidCipherTextException {
		return processing(input, false);
	}

	private byte[] processing(byte[] input, boolean encrypt) throws DataLengthException, InvalidCipherTextException {

		pbbc.init(encrypt, key);

		byte[] output = new byte[pbbc.getOutputSize(input.length)];
		int bytesWrittenOut = pbbc.processBytes(input, 0, input.length, output, 0);

		pbbc.doFinal(output, bytesWrittenOut);

		return output;

	}

	public AES gerarChave() {

		KeyGenerator kg;
		try {
			kg = KeyGenerator.getInstance("AES");

			kg.init(256);
			SecretKey sk = kg.generateKey();

			AES abc = new AES();
			abc.setPadding(new PKCS7Padding());
			keySend = sk.getEncoded();
			abc.setKey(sk.getEncoded());

			return abc;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static void main(String[] args) {
		try {
			KeyGenerator kg = KeyGenerator.getInstance("AES");
			kg.init(256);
			SecretKey sk = kg.generateKey();

			AES abc = new AES();
			abc.setPadding(new PKCS7Padding());
			abc.setKey(sk.getEncoded());

			String secret = "This is a secret message!";
			System.out.println(secret);
			byte[] ba = secret.getBytes("UTF-8");

			byte[] encr = abc.encrypt(ba);
			System.out.println("Encrypted " + encr.length + ": " + Base64.encode(encr));
			byte[] retr = abc.decrypt(encr);

			if (retr.length == ba.length) {
				ba = retr;
			} else {
				System.arraycopy(retr, 0, ba, 0, ba.length);
			}

			String decrypted = new String(ba, "UTF-8");
			System.out.println(decrypted);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
