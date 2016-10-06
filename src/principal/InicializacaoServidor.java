package principal;

import criptography.RSAnew;
import criptography.TCPConnection;

public class InicializacaoServidor {

	public static void main(String[] args) {
		//new RSAnew().generate();
		TCPConnection connection = new TCPConnection(TCPConnection.SERVER_MODE, 6789, "localhost");
	}

}
