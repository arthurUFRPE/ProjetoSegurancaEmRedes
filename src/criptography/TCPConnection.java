package criptography;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;

public class TCPConnection {

	private Socket socket;
	private CriptographyManager manager;
	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	Socket clientSocket;
	ServerSocket welcomeSocket;
	
	public void initServer(int port) {
		manager = new CriptographyManager();
		BufferedReader inFromClient;
		DataOutputStream outToClient;
		
		try {
			
			System.out.println("Estabelecendo Conexão...");
			welcomeSocket =  new ServerSocket(port);
			Socket connectionSocket = welcomeSocket.accept();
	        inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
	        outToClient = new DataOutputStream(connectionSocket.getOutputStream());
	        
	        System.out.println("Trocando chaves...");
	        
	        while(manager.getPublicKey() == null){
	        	
				String publicKey = inFromClient.readLine();
				Object object = manager.decryptToRead(publicKey);
				if(object instanceof PublicKey){
	        		manager.setPublicKey((PublicKey) object);
	        		
	        		manager.setAes(new AES());
	        		manager.getAes().gerarChave();
	        		
	        		outToClient.write(manager.encryptToSend(manager.getAes(), manager.ASYNCHRONOUS_MODE));
	        	}else{
	        		outToClient.writeBytes("Send PublicKey");
	        	}
			}
	        
	        System.out.println("Iniciando Conversa...");
	        
	        new ReceiverServer().run();
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		

	}

	public void initClient(String host, int port) {
		
		try {
			System.out.println("Estabelecendo Conexão...");
			clientSocket = new Socket(host, port);
	        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
	        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	        
	        System.out.println("Trocando chaves...");
	        
	        manager.generateKeyPair();
	        outToServer.writeBytes(manager.getPublicKey());
	        
	        while(manager.getAes() == null){
	        	String modifiedSentence = inFromServer.readLine();
	        	Object object = manager.decryptToRead(modifiedSentence);
	        	if(object instanceof AES){
	        		manager.setAes((AES) object);
	        	}else{
	        		if(object instanceof String){
	        			String msg = (String) object;
	        			if(msg.equals("Send PublicKey")){
	        				outToServer.writeBytes(manager.getPublicKey());
	        			}
	        		}
	        	}
	        }
	        
	        System.out.println("Iniciando Conversa...");
	        new ReceiverClient().run();
	        
	        
	        
	        
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void closeConnection(){
		
        try {
        	System.out.println("Fim da conversa!");
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendToServer(Object o) {

	}

	public void sendToClient(Object o) {

	}

}

class ReceiverClient implements Runnable{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
}

class ReceiverServer implements Runnable{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
}
