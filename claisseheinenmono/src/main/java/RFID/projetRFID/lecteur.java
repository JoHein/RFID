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

    public Object openConnection() throws CardException, SQLException {


        while (true) {
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
            waitForCard();
        }
    }

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

    public static void disconnect() {
        try {
            card.disconnect(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object waitForCard() throws CardException {
        if (terminal.waitForCardPresent(10000) && terminal.isCardPresent()) {
            try {
                affichage();
                String uid = getCardData();
                if (uid.length() == 8) {
                    System.out.println("Carte produit détectée");
                    // traitement carte produit
                    Database base = new Database();
                    Produit produit = base.getProduitStock(uid);
                    return produit;
                } else if (uid.length() == 14) {
                    System.out.println("Carte utilisateur détectée");
                    Database base = new Database();
                    User user = base.getProduitUser(uid);
                    return user;
                } else {
                    System.out.println("Merci de passer une carte valide");
                }

                //System.out.println(prod.toString());
                //return prod;
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

    public static Produit productCardProcess(String uid) throws SQLException {
        db.prepareToQuery();
        Produit prod = db.getProduitStock(uid);
        return prod;
    }

    public static User userCardProcess(String uid) throws SQLException {
        db.prepareToQuery();
        User utilisateur = db.getProduitUser(uid);
        return utilisateur;
    }
}
