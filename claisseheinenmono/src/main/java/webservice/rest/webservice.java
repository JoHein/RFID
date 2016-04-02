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
import javax.ws.rs.PathParam;
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
     * Permet de lire et de retourner l'uid d'une carte
	 */

    @Path("/readCard")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String connectReader() throws JSONException, SQLException, CardException {
        Lecteur lect = new Lecteur();
        System.out.println("connectReader");
        String uid = lect.openConnection();
        System.out.println("uid de la carte :" + uid);
        Database db = new Database();
        db.prepareToQuery();
        String data = db.getCardData(uid);    
        System.out.println(data);
        System.out.println(uid);

        return data;
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
     * @Param : entity = user ou product
     * @Param : data = JSON formaté dans CET ORDRE
     * Format du JSON : en premier (String)uid puis :
     * USER : (String)nom,(String)prenom
     * Produit : (String)idCatalogue,(String)dispo
	 */

    @Path("/addEnt/{entity}/{uid}/{param1}/{param2}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String addEntDB(@PathParam("entity") String entity, @PathParam("uid") String uid, @PathParam("param1") String param1, @PathParam("param2") String param2) throws JSONException, SQLException {
        Database db = new Database();
        db.prepareToQuery();
        System.out.println(uid.length());
        if (uid.length() == 8 || uid.length()==14) {
            return db.addEntity(entity, uid,param1, param2);
        } else {
            return "[{\"retour\": \"Taille de carte incorrecte\"}]";
         
        }
    }

	/*
     * Supression d'une Entity de la base de donnée
     * @Param : UID de la carte de l'entité
	 */

    @Path("/deleteEnt/{uid}")
    @DELETE
    public String deleteEntDB(@PathParam("uid") String uid) throws SQLException {
        Database db = new Database();
        db.prepareToQuery();
        System.out.println(db.deleteEntity(uid));
        return db.deleteEntity(uid);
    }


	/*
     * Modification de la table emprunt (ajout ou suppression) selon l'action réalisée
     * @Param : action = borrow ou return
     * @Param : uid = JSON avec uidUser et uidProduit
	 */

    @Path("/manageBorrow/{action}/{uidUser}/{uidProduit}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String manageBorrow(@PathParam("action") String action, @PathParam("uidUser") String uidUser, @PathParam("uidProduit") String uidProduit) throws
            JSONException, SQLException {
    	
        if (action.equals("borrow") || action.equals("return")) {
            Database db = new Database();
            db.prepareToQuery();
            return db.manageBorrow(action, uidUser, uidProduit);
        } else {
            return "[{\"retour\": \"Action non reconnue\"}]";
        }
    }
}