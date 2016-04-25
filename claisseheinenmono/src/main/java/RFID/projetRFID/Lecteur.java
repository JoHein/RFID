package RFID.projetRFID;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.smartcardio.*;

/**
 * Application launch
 */
public class Lecteur {
    private static Card card = null;
    private static CardChannel channel = null;
    private static CardTerminal terminal;
    static Database db = new Database();

    /**
     * <p>Ouverture d'une connection afin de lire une carte</p>
     *
     * @return
     * @throws CardException
     * @throws SQLException
     */

    public String openConnection() throws CardException, SQLException {
        TerminalFactory factory = TerminalFactory.getDefault();
        CardTerminals cardterminals = factory.terminals();
        try {
            List<CardTerminal> terminals = cardterminals.list();
            // System.out.println("\nTerminals: " + terminals);
            terminal = cardterminals.getTerminal(terminals.get(0).getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Presenter carte !");
        String data = waitForCard();
        return data;
    }

    /**
     * <p>Affichage coté serveur lors de la lecture d'une carte (pour les test)</p>
     *
     * @throws CardException
     */

    public static void affichage() throws CardException {
        System.out.println("Card detected!");
        card = terminal.connect("*");
        System.out.println("Card: " + card);
        channel = card.getBasicChannel();
        System.out.println("Channel: " + channel);
        System.out.println("Connection open!");
        ATR atr = card.getATR();
        byte[] baAtr = atr.getBytes();
        System.out.print("ATR = 0x");
        for (int i = 0; i < baAtr.length; i++) {
            System.out.printf("%02X ", baAtr[i]);
        }
        System.out.println();
    }

    /**
     * <p>Récupération et formatage de l'UID de la carte lue </p>
     *
     * @return
     * @throws CardException
     */

    public static String getCardData() throws CardException {
        String uid = "";
        byte[] cmdApduGetCardUid = new byte[]{(byte) 0xFF, (byte) 0xCA, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        ResponseAPDU respApdu = channel.transmit(new CommandAPDU(cmdApduGetCardUid));
        if (respApdu.getSW1() == 0x90 && respApdu.getSW2() == 0x00) {
            byte[] baCardUid = respApdu.getData();

            for (int i = 0; i < baCardUid.length; i++) {
                uid += String.format("%02X", baCardUid[i]);
            }
            System.out.println("numero UID : " + uid);
        }
        return uid;
    }

    /**
     * <p>Déconnexion du terminal de lecture</p>
     */

    public static void disconnect() {
        try {
            card.disconnect(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>Boucle pour initier la lecture d'une carte</p>
     *
     * @return
     * @throws CardException
     * @throws SQLException
     */

    public static String waitForCard() throws CardException, SQLException {
        while (terminal.waitForCardPresent(5000)) {
            if (terminal.isCardPresent()) {
                try {
                    affichage();
                    String uid = getCardData();
                    return uid;
                } catch (CardException e) {
                    System.err.println(e);
                }

                try {
                    card.disconnect(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                while (!terminal.waitForCardAbsent(10000)) {
                    System.out.println("retirer carte");
                }
            }
        }
        return "";
    }
}
