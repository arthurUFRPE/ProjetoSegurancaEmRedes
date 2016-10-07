package criptography;

import java.io.*;
import java.net.*;

import com.google.gson.Gson;

import object.AESPackage;
import principal.Mensagem;

public class TCPServer {
	
    private CriptographyManager manager;
    ServerSocket welcomeSocket;
    Socket connectionSocket;
    


	public static void main(String[] args) {
		//new RSAnew().generate();
    	TCPServer connection = new TCPServer().initServer(6789);
	}
   
	
	public TCPServer initServer(int port) {
        manager = new CriptographyManager();
        try {
            welcomeSocket =  new ServerSocket(port);
            System.out.println("SERVER MODE START!");
            connectionSocket = welcomeSocket.accept();
            
            System.out.println("Conexão iniciada!");
            new ConnectionServer(connectionSocket, manager).run();
            
            return this;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
        
    }
    
}

class ConnectionServer implements Runnable{
    
    Socket connectionSocket;
    BufferedReader inFromClient;
    PrintWriter outToClient;
    CriptographyManager manager;
    long count = -1;
    

    public ConnectionServer(Socket connectionSocket, CriptographyManager manager) {
        super();
        this.connectionSocket = connectionSocket;
        this.manager = manager;
    }
    
    @Override
    public void run() {
        try {
            inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            outToClient = new PrintWriter(connectionSocket.getOutputStream(), true);
            
            
            while(manager.getAes() == null){
            	String msg = inFromClient.readLine();
                Object obj = manager.decryptToRead(msg, CriptographyManager.ASYNCHRONOUS_MODE);
                
                if(obj instanceof AESPackage){
                	//System.out.println(new Gson().toJson(((AESPackage)obj)));
                	AES aes = new AES();
                	aes.setKeySend(((AESPackage) obj).getKey());
                	aes.setKey(aes.getKeySend());
                    manager.setAes(aes);
                    manager.setMacKey(((AESPackage) obj).getMackey());
                    
                }
            }
                        
            while(true){
            	System.out.println("Entrou");
            	String msg = inFromClient.readLine();
            	System.out.println(new Gson().toJson(msg));
            	System.out.println(msg);
            	if(msg != null){
            		Mensagem mensagem = (Mensagem) manager.decryptToRead(msg, manager.SYNCHRONOUS_MODE);
            		if(manager.verificaMAC(manager.getMacKey(), mensagem.getMensagem(), mensagem.getMacMens())){
            			if(count == -1){
            				count = mensagem.getCount();
            				System.out.println("Cliente: "+mensagem.getMensagem());
            			}else if(mensagem.getCount() == count+1){
            				System.out.println("Cliente: "+mensagem.getMensagem());
            			}else{
            				System.out.println("Mensagem fora de ordem!");
            			}
            		}
            	}else{
            		System.out.println("Não veio nada!");
            	}
                
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        
    }
    
}

