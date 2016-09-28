package principal;

import criptography.RSA;
import criptography.TCPConnection;

/*IMPLEMENTAÇÃO DO CANAL SEGURO
 * -ENCRIPTAÇÃO  --¬
 * -AUTENTICAÇÃO ---> CRIPTOGRAFIA SIMÉTRICA
 * -MANTER ORDEM DAS MENSAGENS
 * -TROCA DA CHAVE
 * -CRIPTOGRAFIA ASSIMETRICA*/

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//TCPConnection connection = new TCPConnection(TCPConnection.SERVER_MODE, 6789, "localhost");
		TCPConnection connectionCli = new TCPConnection(TCPConnection.CLIENT_MODE, 6789, "localhost");
		

	}
		

}

