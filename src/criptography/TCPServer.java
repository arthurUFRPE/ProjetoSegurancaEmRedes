package criptography;

import java.io.*;
import java.net.*;

public class TCPServer {
    public static void main(String args[]) throws Exception {
        String clientSentence;
        String capitalizedSentence;
        ServerSocket welcomeSocket = new ServerSocket(6789);
        Pessoa pessoaServer = new Pessoa(); //Obj da classe desejada
        
        System.out.println("SERVER: ");

        while(true) {
            Socket connectionSocket = welcomeSocket.accept();
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            clientSentence = inFromClient.readLine();
            //capitalizedSentence = clientSentence.toUpperCase() + '\n';
            outToClient.writeBytes(pessoaServer.convertFromString(clientSentence).toString() + "\n");
            System.out.println(clientSentence);
        }
    }
}
