package principal;

import criptography.TCPConnection;

public class MainCliente {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		TCPConnection connectionCli = new TCPConnection(TCPConnection.CLIENT_MODE, 6789, "localhost");
	}

}