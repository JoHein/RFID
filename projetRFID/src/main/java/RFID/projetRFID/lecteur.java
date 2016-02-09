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

	public static void main(String[] args) throws CardException, SQLException{
		while(true){
		System.out.println(openConnection());
		}
	}

//	public static void main(String[] args) throws CardException, SQLException, InterruptedException {
//		while(true){
//			TerminalFactory factory = TerminalFactory.getDefault();
//			CardTerminals cardterminals = factory.terminals();
//			try{
//				List<CardTerminal> terminals = cardterminals.list();
//				System.out.println("\nTerminals: " + terminals);
//				terminal = cardterminals.getTerminal(terminals.get(0).getName());
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//			System.out.println("\nPresenter carte !\n");
//			if(terminal.waitForCardPresent(10000) && terminal.isCardPresent()){
//				try{
//					System.out.println("Card detected!");
//					card = terminal.connect("*");
//					System.out.println("Card: " + card);
//					channel = card.getBasicChannel();
//					System.out.println("Channel: " + channel);
//
//					System.out.println("Connection open!");
//					ATR atr = card.getATR();
//					byte[] baAtr = atr.getBytes();
//					System.out.print("ATR = 0x");
//					for(int i = 0; i < baAtr.length; i++ ){
//						System.out.printf("%02X ",baAtr[i]);
//					}
//					System.out.println();
//					byte[] cmdApduGetCardUid = new byte[]{(byte)0xFF, (byte)0xCA, (byte)0x00, (byte)0x00, (byte)0x00};
//
//					ResponseAPDU respApdu = channel.transmit(new CommandAPDU(cmdApduGetCardUid));
//					if(respApdu.getSW1() == 0x90 && respApdu.getSW2() == 0x00){
//						byte[] baCardUid = respApdu.getData();
//						
//						String uid="";
//						for(int i = 0; i < baCardUid.length; i++ ){
//							   uid += String.format("%02X", baCardUid [i]);
//						}
//						
//						System.out.println("numero UID : "+uid);
//						getProduit(uid);
//						
//					}
//				}catch (CardException e){
//					System.err.println(e);
//				}
//		       
//				try {
//					card.disconnect(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				while(!terminal.waitForCardAbsent(10000)){
//					System.out.println("retirer carte");
//				}
//			}
//    	}
//    }
	
	public static String openConnection() throws CardException, SQLException {
		
		while(true){
			TerminalFactory factory = TerminalFactory.getDefault();
			CardTerminals cardterminals = factory.terminals();
			try{
				List<CardTerminal> terminals = cardterminals.list();
				//System.out.println("\nTerminals: " + terminals);
				terminal = cardterminals.getTerminal(terminals.get(0).getName());
			}catch(Exception e){
				e.printStackTrace();
			}
			System.out.println("Presenter carte !");
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
							   uid += String.format("%02X", baCardUid [i]);
						}
						
						System.out.println("numero UID : "+uid);
						//return uid;
						String data = getProduit(uid);
						System.out.println(data);
						return data;
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
	
	public static void disconnect() {
		try {
			card.disconnect(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getProduit(String uid) throws SQLException {
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
		int UIDProduit = 0;
		String data = "";
		while (var.next()) {
			System.out.println("id stock");
			System.out.println(var.getString("idStock"));
			System.out.println("id catalogue");
			System.out.println(var.getString("idCatalogue"));
			UIDProduit = var.getInt("idCatalogue");
			data += "{\"UIDProduit\":"+UIDProduit;
		}
		ResultSet detail = cmd.executeQuery("SELECT * FROM CATALOGUE where idCatalogue = '" + UIDProduit + "'");
		while (detail.next()) {

			System.out.println("Nom du produit");
			System.out.println(detail.getString("nomCatalogue"));
			String nomCatalogue = detail.getString("nomCatalogue");
			
			data += ",\"nomCatalogue\":\""+nomCatalogue+"\"}";
		}
		
		con.close();
		return data;
	}
}
