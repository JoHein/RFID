package webservice.rest;

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
import RFID.projetRFID.produit;

@Path("/")
public class webservice {

	@Path("/readCard")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String connectReader() throws JSONException, CardException, SQLException {
		lecteur lect = new lecteur();
		System.out.println("connectReader");

		produit prod = lect.openConnection();
		System.out.println("produit :" + prod.toString());
		String json = prod.toString();
		return json;
	}
}
