package criptography;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import object.AESPackage;

public class TCPConnection {

	private CriptographyManager manager;
	ServerSocket welcomeSocket;
	Socket connectionSocket;
	public static final int SERVER_MODE = 0;
	public static final int CLIENT_MODE = 1;
	
	public TCPConnection(int mode, int port, String host){
		switch (mode) {
		case SERVER_MODE:
				initServer(port);
		case CLIENT_MODE:
				initClient(host, port);
		}
	}
	
	private TCPConnection initServer(int port) {
		manager = new CriptographyManager();
		manager.setPrivateKey(manager.readPrivateKey());
		try {
			welcomeSocket =  new ServerSocket(port);
			connectionSocket = welcomeSocket.accept();
			
			new ConnectionServer(connectionSocket, manager).run();
			System.out.println("SERVER MODE START!");
			return this;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	private TCPConnection initClient(String host, int port) {
		manager = new CriptographyManager();
		manager.setPublicKey(manager.readPublicKey());
		try {
			connectionSocket = new Socket(host, port);
	        DataOutputStream outToServer = new DataOutputStream(connectionSocket.getOutputStream());
	        
	        AESPackage aesPackage = new AESPackage(manager.getAes(true).getKeySend());	        
	        
	        outToServer.writeUTF(manager.encryptToSend(aesPackage, CriptographyManager.ASYNCHRONOUS_MODE));
	        
	        new ConnectionClient(connectionSocket,manager).run();

			System.out.println("CLIENT MODE START!");
	        return this;        
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	public void closeConnection(){
		
        try {
        	System.out.println("End!");
        	connectionSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void send(Object o) {
		try {
			DataOutputStream outTo = new DataOutputStream(connectionSocket.getOutputStream());
			
			outTo.writeUTF(manager.encryptToSend(o, CriptographyManager.SYNCHRONOUS_MODE));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

class ConnectionClient implements Runnable{
	
	Socket connectionSocket;
	BufferedReader inFromServer;
	CriptographyManager manager;
	
	public ConnectionClient(Socket connectionSocket, CriptographyManager manager) {
		super();
		this.connectionSocket = connectionSocket;
		this.manager = manager;
	}

	@Override
	public void run() {
        try {
			inFromServer = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			
//			while(true){
//				//Ler Mensagens AQUI!!
//			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}

class ConnectionServer implements Runnable{
	
	Socket connectionSocket;
	BufferedReader inFromClient;
	DataOutputStream outToClient;
	CriptographyManager manager;
	

	public ConnectionServer(Socket connectionSocket, CriptographyManager manager) {
		super();
		this.connectionSocket = connectionSocket;
		this.manager = manager;
	}
	
	@Override
	public void run() {
        try {
        	inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			
			while(manager.getAes(false) == null){
	        	String msg = inFromClient.readLine();
	        	Object obj = manager.decryptToRead(msg, CriptographyManager.ASYNCHRONOUS_MODE);
	        	if(obj instanceof AESPackage){
	        		manager.getAes(true).setKey(((AESPackage) obj).getKey());
	        		//manager.setMacKey(((CriptographyManager) obj).getMacKey());
	        	}
	        }
			System.out.println("FOI!!!!");
//			while(true){
//				//Ler Mensagens AQUI!!
//			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
		
	}
	
}
