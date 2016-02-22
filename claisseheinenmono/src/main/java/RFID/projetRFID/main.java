package RFID.projetRFID;

import java.sql.SQLException;

import javax.smartcardio.CardException;

public class main {

    public static void main(String[] args) throws CardException, SQLException {
        lecteur lect = new lecteur();

        while (true) {
            System.out.println(lect.openConnection());
        }
    }

}
