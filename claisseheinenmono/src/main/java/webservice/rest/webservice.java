package webservice.rest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.smartcardio.CardException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import RFID.projetRFID.Catalogue;
import RFID.projetRFID.Database;
import RFID.projetRFID.Lecteur;
import RFID.projetRFID.Produit;

@Path("/")
public class webservice {
	
	/*
	 * Permet de lire et de retourner les données d'une carte
	 */

	@Path("/readCard")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String connectReader() throws JSONException, CardException, SQLException {
		Lecteur lect = new Lecteur();
		System.out.println("connectReader");

		Object card = lect.openConnection();
		System.out.println("produit :" + card.toString());
		String json = card.toString();
		
		System.out.println("webservice");
		System.out.println(json);
		

		return json;
	}
	
	/*
	 * Lister les livres de la bibliotheque
	 * 
	 */
	
	@Path("/allCat")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject allCatalogue() throws JSONException, SQLException {
		Database data = new Database();

		data.prepareToQuery();

		return data.getAllCatalogues();
		
	}
	
	/*
	 * Ajout d'une Entity dans la bdd
	 */
	
	@Path("/addCard")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void addEntDB() throws JSONException{
//		if(card<8){
//			/*
//			 * then call methode card produit
//			 */
//		}elseif(card>13){
//			/*
//			 * then call methode card User
//			 */
//		}else{
//			/*
//			 * Return error message mauvaise card (ou check dans angular)
//			 */
//		}
	}
	
	/*
	 * Supression d'une Entity de la base de donnée
	 */
	
	@Path("/deleteEnt")
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteEntDB() throws JSONException{
//		if(card<8){
//			/*
//			 * then call methode card produit
//			 */
//		}elseif(card>13){
//			/*
//			 * then call methode card User
//			 */
//		}else{
//			/*
//			 * Return error message mauvaise card (ou check dans angular)
//			 */
//		}
	}
	
	/*
	 * Emprunt d'un livre par un user
	 */
	
	@Path("/empruntLivre")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void empruntLivre()throws JSONException{
		
	}
	
	/*
	 * Retour d'un livre par un user
	 */
	@Path("/retourLivre")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void retourLivre()throws JSONException{
		
	}
	
}
