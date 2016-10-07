package criptography;

import java.io.*;
import java.net.*;
import java.util.Random;

import com.google.gson.Gson;

import object.AESPackage;
import principal.Mensagem;

public class TCPClient {

	private CriptographyManager manager;
	ServerSocket welcomeSocket;
	Socket connectionSocket;
	
	public static void main(String[] args){
		TCPClient client = new TCPClient().initClient("localhost",6789);
	}

	public  TCPClient initClient(String host, int port) {
		manager = new CriptographyManager();
		try {
			connectionSocket = new Socket(host, port);
			System.out.println("CLIENT MODE START!");
			new ConnectionClient(connectionSocket, manager).run();

			
			return this;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public void closeConnection() {

		try {
			System.out.println("End!");
			connectionSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	public void send(Object o) {
//		try {
//			DataOutputStream outTo = new DataOutputStream(connectionSocket.getOutputStream());
//
//			outTo.writeUTF(manager.encryptToSend(o, CriptographyManager.SYNCHRONOUS_MODE));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

}

class ConnectionClient implements Runnable {

	Socket connectionSocket;
	BufferedReader in;
	CriptographyManager manager;

	public ConnectionClient(Socket connectionSocket, CriptographyManager manager) {
		super();
		this.connectionSocket = connectionSocket;
		this.manager = manager;
	}

	@Override
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			PrintWriter outToServer = new PrintWriter(connectionSocket.getOutputStream(), true);
			manager.gerarMACandAES();
			AESPackage aesPackage = new AESPackage(manager.getAes().getKeySend(), manager.getMacKey());

			String msg = manager.encryptToSend(aesPackage, CriptographyManager.ASYNCHRONOUS_MODE);
			outToServer.println(msg);
			String resposta;
			do{
				resposta =in.readLine(); 
			}
			while(resposta == null);
			System.out.println(resposta);
			
			Mensagem mensagem = new Mensagem();
			mensagem.setCount(new Random().nextInt(1000));
			mensagem.setMensagem("VAI DE PRIMEIRA");
			// mensagem.setMacMens(manager.criptografaMAC(manager.getMacKey(),
			// "VAI DE PRIMEIRA"));
			System.out.println(new Gson().toJson(mensagem));
			String objCrip = manager.encryptToSend(mensagem, manager.SYNCHRONOUS_MODE);
			System.out.println("ENVIANDO: "+ objCrip);
			outToServer.println(objCrip);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}