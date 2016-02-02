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
 *
 */
public class lecteur {
	private static Card card = null;
	private static CardChannel channel = null;
	private static CardTerminal terminal;


	public static void main(String[] args) throws CardException, SQLException, InterruptedException {
		while(true){
			TerminalFactory factory = TerminalFactory.getDefault();
			CardTerminals cardterminals = factory.terminals();
			try{
				List<CardTerminal> terminals = cardterminals.list();
				System.out.println("\nTerminals: " + terminals);
				terminal = cardterminals.getTerminal(terminals.get(0).getName());
			}catch(Exception e){
				e.printStackTrace();
			}
			System.out.println("\nPresenter carte !\n");
			if(terminal.waitForCardPresent(10000) && terminal.isCardPresent()){
				try{
					System.out.println("Card detected!");
					card = terminal.connect("*");
					System.out.println("Card: " + card);
					channel = card.getBasicChannel();
					System.out.println("Channel: " + channel);

					System.out.println("Connection open!");
					ATR atr = card.getATR();
					byte[] baAtr = atr.getBytes();
					System.out.print("ATR = 0x");
					for(int i = 0; i < baAtr.length; i++ ){
						System.out.printf("%02X ",baAtr[i]);
					}
					System.out.println();
					byte[] cmdApduGetCardUid = new byte[]{(byte)0xFF, (byte)0xCA, (byte)0x00, (byte)0x00, (byte)0x00};

					ResponseAPDU respApdu = channel.transmit(new CommandAPDU(cmdApduGetCardUid));
					if(respApdu.getSW1() == 0x90 && respApdu.getSW2() == 0x00){
						byte[] baCardUid = respApdu.getData();
						
						String uid="";
						for(int i = 0; i < baCardUid.length; i++ ){
							 uid+=baCardUid[i];
						}
						
						System.out.println("numero UID : "+uid);
						
					}
				}catch (CardException e){
					System.err.println(e);
				}
		       
				try {
					card.disconnect(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				while(!terminal.waitForCardAbsent(10000)){
					System.out.println("retirer carte");
				}
			}
    	}
    }
	
	public static boolean openConnection() {
		
		card = null;
		try {
			terminal.waitForCardPresent(20000);
			if(terminal.isCardPresent()) {
				
				return true;
			} else {
				System.out.println("No card detected!");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void disconnect() {
		try {
			card.disconnect(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getProduit(String uid) throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}

		String url = "jdbc:mysql://localhost:3307/rfid";

		Connection con = DriverManager.getConnection(url, "root", "");

		Statement cmd = con.createStatement();

		ResultSet var = cmd.executeQuery("SELECT * from STOCK where uidProduit = '" + uid + "'");

		Statement cmd2 = con.createStatement();
		int test = 0;

		while (var.next()) {
			System.out.println("id stock");
			System.out.println(var.getString("idStock"));
			System.out.println("id catalogue");
			System.out.println(var.getString("idCatalogue"));
			test = var.getInt("idCatalogue");
		}
		ResultSet detail = cmd.executeQuery("SELECT * FROM CATALOGUE where idCatalogue = '" + test + "'");
		while (detail.next()) {

			System.out.println("Nom du produit");
			System.out.println(detail.getString("nomCatalogue"));
		}

		// String requete = "SELECT NOM, PRIX FROM CAFE";
		// ResultSet rs = cmd.executeQuery(requete);
		// while(rs.next()){
		// String s = rs.getString("NOM");
		// float n = rs.getFloat("PRIX");
		// System.out.println(s + " " + n);
		// }
		con.close();

	}
}
