package webservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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



	@SuppressWarnings("restriction")
	@Path("/connectReader")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public boolean connectReader(String nameLivreJSON) throws JSONException {
		System.out.println("connectReader");
		
		return lecteur.openConnection();
		
	}
}
