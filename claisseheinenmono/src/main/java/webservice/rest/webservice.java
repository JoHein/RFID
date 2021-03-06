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
import javax.ws.rs.QueryParam;
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

    /**
     * Permet de lire et de retourner l'uid d'une carte
     * 
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

    /**
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

    /**
     * Lister les users de la bibliotheque
     */

    @Path("/allUser")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject allUsers() throws JSONException, SQLException {
        Database data = new Database();
        data.prepareToQuery();
        return data.getAllUsers();
    }

    /**
     * Lister les emprunts de la bibliotheque
     */

    @Path("/allBorrow")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject allBorrow() throws JSONException, SQLException {
        Database data = new Database();
        data.prepareToQuery();
        return data.getAllBorrow();
    }

    /**
     * Gérer les catalogues de la bibliotheque
     * @param action : création ou suppression
     * @param idCatalogue : id du catalogue
     * @param nomCatalogue : nom du catalogue
     * @param auteur : auteur de l'ouvrage juste le nom
     * @param type : le type du livre Roman, Biographie, Science-fiction
     * @param categorie : pour préciser le type exemple anticipation
     */

    @Path("/manageCat/{action}/{idCatalogue}/{nomCatalogue}/{auteur}/{type}/{categorie}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String addCatalogue(@PathParam("action") String action, @PathParam("idCatalogue")Integer idCatalogue ,@PathParam("nomCatalogue") String nomCatalogue,@PathParam("auteur") String auteur,
    		@PathParam("type") String type,@PathParam("categorie") String categorie) throws JSONException, SQLException {
    	
        if (action.equals("Création") || action.equals("Suppression")) {
            Database db = new Database();
            db.prepareToQuery();
            return db.manageCatalogue(action, idCatalogue, nomCatalogue, auteur, type, categorie);
        } else {
            return "[{\"retour\": \"Action non reconnue\"}]";
        }
    }

    /**
     * Trouver un livre à partir de son titre
     * @param search : String d'une partie du nom d'un livre
     */

    @Path("/titleFind")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject findByTitle(@QueryParam("search") String search) throws JSONException, SQLException {
        Database data = new Database();
        data.prepareToQuery();
        System.out.println("Trouver le livre : " + search);
        return data.getBookByTitle(search);
    }

    /**
     * Ajout d'une Entity dans la bdd
     *
     * @param entity : entity = user ou product
     * @param uid : uid du livre ou de l'utilisateur
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
        if (uid.length() == 8 || uid.length() == 14) {
            return db.addEntity(entity, uid, param1, param2);
        } else {
            return "[{\"retour\": \"Taille de carte incorrecte\"}]";

        }
    }

    /**
     * Supression d'une Entity de la base de donnée
     * @param uid : UID de la carte de l'entité
     */

    @Path("/deleteEnt/{uid}")
    @DELETE
    public String deleteEntDB(@PathParam("uid") String uid) throws SQLException {
        Database db = new Database();
        db.prepareToQuery();
        System.out.println(db.deleteEntity(uid));
        return db.deleteEntity(uid);
    }

    /**
     * Vérification de la présence d'une carte dans la base de donnée
     *
     * @param uid : UID de la carte à tester
     */

    @Path("/checkCard/{uid}")
    @POST
    public String checkCardDB(@PathParam("uid") String uid) throws SQLException {
        Database db = new Database();
        String table;
        db.prepareToQuery();
        if (uid.length() == 14 || uid.length() == 8) {
            if (uid.length() == 14) table = "user";
            else table = "stock";
            if (db.isInDb(table, uid) == 0) {
                return "[{\"retour\": \"Carte non présente\"}]";
            } else if (db.isInDb(table, uid) == 1) {
                return "[{\"retour\": \"Carte déjà présente\"}]";
            } else {
                return "[{\"retour\": \"Carte non reconnue\"}]";
            }
        }
        return "[{\"retour\": \"Carte non valide\"}]";
    }

    /**
     * Modification de la table emprunt (ajout ou suppression) selon l'action réalisée
     *
     * @param  action : action = borrow ou return
     * @param uidUser : uid = JSON avec uidUser
     * @param uidProduit : uid = JSON uidProduit
     */

    @Path("/manageBorrow/{action}/{uidUser}/{uidProduit}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String manageBorrow(@PathParam("action") String action, @PathParam("uidUser") String uidUser, @PathParam("uidProduit") String uidProduit) throws
            JSONException, SQLException {
        if (action.equals("Emprunt") || action.equals("Retour")) {
            Database db = new Database();
            db.prepareToQuery();
            return db.manageBorrow(action, uidUser, uidProduit);
        } else {
            return "[{\"retour\": \"Action non reconnue\"}]";
        }
    }
}