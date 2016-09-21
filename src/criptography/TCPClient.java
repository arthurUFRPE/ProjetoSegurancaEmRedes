package criptography;

import java.io.*;
import java.net.*;

public class TCPClient {
	public static void main(String argv[]) throws Exception {
        String sentence;
        String modifiedSentence;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        Socket clientSocket = new Socket("localhost", 6789);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        
        System.out.print("CLIENT: ");

        //sentence = inFromUser.readLine();
        
        //Obj da classe desejada
        Pessoa minhaPessoa = new Pessoa("paulinely", "masc", 27);
        
        sentence = minhaPessoa.convertToString();
        outToServer.writeBytes(sentence + "\n");
        
        //Para ler do servidor (resposta)
        modifiedSentence = inFromServer.readLine();
        
        //Para imprimir resposta no cliente
        System.out.println(modifiedSentence);
        clientSocket.close();
    }
}