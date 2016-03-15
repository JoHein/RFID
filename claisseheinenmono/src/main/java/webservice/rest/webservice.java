package webservice.rest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

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

import RFID.projetRFID.Catalogue;
import RFID.projetRFID.Database;
import RFID.projetRFID.Lecteur;
import RFID.projetRFID.Produit;

@Path("/")
public class webservice {

	@Path("/readCard")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String connectReader() throws JSONException, CardException, SQLException {
		Lecteur lect = new Lecteur();
		System.out.println("connectReader");
		Object prod = lect.openConnection();
		System.out.println("produit :" + prod.toString());
		String json = prod.toString();
		return json;
	}
	
	@Path("/allCat")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject allCatalogue() throws JSONException, SQLException {
		Database data = new Database();
		System.out.println("Prepare");

		data.prepareToQuery();

		return data.getAllCatalogues();
		
	}
}
