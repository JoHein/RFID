package webservice.RFID;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.smartcardio.CardException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import RFID.projetRFID.lecteur;

@Path("/")
public class webservice {

	@Path("/readCard")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String connectReader() throws JSONException, CardException, SQLException {
		System.out.println("connectReader");
		 
		String data = lecteur.openConnection();
		System.out.println("produit:" +data);    
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//		} catch (ClassNotFoundException e) {
//
//			e.printStackTrace();
//		}
//
//		String url = "jdbc:mysql://localhost:3307/rfid";
//
//		Connection con = DriverManager.getConnection(url, "root", "");
//
//		Statement cmd = con.createStatement();
//
//		ResultSet var = cmd.executeQuery("SELECT * from STOCK where uidProduit = '" + uid + "'");
//
//		Statement cmd2 = con.createStatement();
//		int UIDProduit = 0;
//		String data = "";
//		while (var.next()) {
//			System.out.println("id stock");
//			System.out.println(var.getString("idStock"));
//			System.out.println("id catalogue");
//			System.out.println(var.getString("idCatalogue"));
//			UIDProduit = var.getInt("idCatalogue");
//			data += "{\"UIDProduit\":"+UIDProduit;
//		}
//		ResultSet detail = cmd.executeQuery("SELECT * FROM CATALOGUE where idCatalogue = '" + UIDProduit + "'");
//		while (detail.next()) {
//
//			System.out.println("Nom du produit");
//			System.out.println(detail.getString("nomCatalogue"));
//			String nomCatalogue = detail.getString("nomCatalogue");
//			
//			data += ",\"nomCatalogue\":\""+nomCatalogue+"\"}";
//		}
//		
//		con.close();
//		System.out.println(uid);
		return data;
	}
}
