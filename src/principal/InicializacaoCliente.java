package principal;

import criptography.TCPConnection;

public class InicializacaoCliente {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TCPConnection connection = new TCPConnection(TCPConnection.CLIENT_MODE, 6789, "localhost");
	}

}


