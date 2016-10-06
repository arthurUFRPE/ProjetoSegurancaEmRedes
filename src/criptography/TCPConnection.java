package criptography;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import javax.crypto.SecretKey;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import object.AESPackage;
import principal.Mensagem;

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

    private TCPConnection initClient(String host, int port) {
        manager = new CriptographyManager();
        try {
            connectionSocket = new Socket(host, port);
                        
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

//    public void send(Object o) {
//        try {
//            DataOutputStream outTo = new DataOutputStream(connectionSocket.getOutputStream());
//            
//            outTo.writeUTF(manager.encryptToSend(o, CriptographyManager.SYNCHRONOUS_MODE));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

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
            
            PrintWriter outToServer = new PrintWriter(connectionSocket.getOutputStream(), true);
            manager.gerarMACandAES();
            AESPackage aesPackage = new AESPackage(manager.getAes().getKeySend(), manager.getMacKey());
//          System.out.println("AES antes: "+Base64.encode(aesPackage.getKey()));
//        	System.out.println("MAC antes: "+Base64.encode(aesPackage.getMackey()));
            String msg = manager.encryptToSend(aesPackage, CriptographyManager.ASYNCHRONOUS_MODE);
            
            Object o = manager.decryptToRead(msg, CriptographyManager.ASYNCHRONOUS_MODE);
            if(o instanceof AESPackage){
            	
//            	System.out.println("AES: "+Base64.encode(((AESPackage)o).getKey()));
//            	System.out.println("MAC: "+Base64.encode(((AESPackage)o).getMackey()));
            }
            
            outToServer.println(msg);

            Mensagem mensagem = new Mensagem();
            mensagem.setCount(new Random().nextInt(1000));
            mensagem.setMensagem("VAI DE PRIMEIRA");
            //mensagem.setMacMens(manager.criptografaMAC(manager.getMacKey(), "VAI DE PRIMEIRA"));
            
            String objCrip = manager.encryptToSend(mensagem, manager.SYNCHRONOUS_MODE);
            System.out.println("Syncrono saida:"+ objCrip);
            outToServer.println(objCrip);
            
//            while(true){
//                //Ler Mensagens AQUI!!
//            }
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
            outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            
            while(manager.getAes() == null){
                String msg = inFromClient.readLine();
                
                Object obj = manager.decryptToRead(msg, CriptographyManager.ASYNCHRONOUS_MODE);
                
                if(obj instanceof AESPackage){
                	AES aes = new AES();
                	aes.setKeySend(((AESPackage) obj).getKey());
                	aes.setKey(aes.getKeySend());
                    manager.setAes(aes);
                    manager.setMacKey(((AESPackage) obj).getMackey());
                }
            }
            
            while(true){
            	String msg = inFromClient.readLine();
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
            	}
                
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        
    }
    
}

