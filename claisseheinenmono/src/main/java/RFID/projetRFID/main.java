package RFID.projetRFID;

import java.sql.SQLException;

import javax.smartcardio.CardException;

public class main {

	public static void Main(String[] args) throws CardException, SQLException {
		Lecteur lect = new Lecteur();

		while (true) {
			System.out.println(lect.openConnection());
		}
	}

}
