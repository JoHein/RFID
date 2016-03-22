package RFID.projetRFID;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Database {

    Statement stmt;
    Connection con;

    public Database() {
    }

    public void prepareToQuery() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //String url = "jdbc:mysql://localhost:3307/rfid";
        String url = "jdbc:mysql://127.0.0.1:3306/rfid";

        this.con = DriverManager.getConnection(url, "root", "");
        this.stmt = con.createStatement();
    }

    public String getCardData(String uid) throws SQLException {
        String data = "";
        if (uid.length() == 8) {
            System.out.println("Carte produit détectée");
            // traitement carte produit
            Produit produit = this.getProduitStock(uid);
            System.out.println(produit.toString());
            data = produit.toString();
            return data;
        } else if (uid.length() == 14) {
            System.out.println("Carte user détectée");
            Database base = new Database();
            base.prepareToQuery();
            User user = base.getProduitUser(uid);
            System.out.println(user.toString());
            data = user.toString();
            return data;
        } else {
            System.out.println("Merci de passer une carte valide");
        }
        return data;
    }

    public JSONObject getAllCatalogues() throws SQLException, JSONException {
        ResultSet allCat = this.stmt.executeQuery("SELECT * FROM catalogue");
        JSONArray listCat = new JSONArray();
        JSONObject resList = new JSONObject();

        while (allCat.next()) {
            JSONObject obj = new JSONObject();
            obj.put("idCatalqogue", allCat.getInt("idCatalogue"));
            obj.put("nomCatalogue", allCat.getString("nomCatalogue"));
            obj.put("nbDispo", allCat.getInt("nbDispo"));
            listCat.put(obj);
        }
        System.out.println("Database");
        System.out.println(listCat);
        resList.put("Livres", listCat);
        return resList;
    }

    public Produit getProduitStock(String uid) throws SQLException {
        ResultSet stock = this.stmt.executeQuery("SELECT * FROM stock WHERE uidProduit = '" + uid + "'");
        Produit prod = new Produit();
        while (stock.next()) {
            prod.idStock = stock.getInt("idStock");
            prod.uidProduits = stock.getString("uidProduit");
            prod.dispo = stock.getInt("dispo");
            prod.idCatalogue = stock.getInt("idCatalogue");

        }

        ResultSet catalogue = this.stmt.executeQuery("SELECT * FROM catalogue WHERE idCatalogue = '" + prod.idCatalogue + "'");
        while (catalogue.next()) {
            prod.nomCatalogue = catalogue.getString("nomCatalogue");
        }
        return prod;
    }

    public User getProduitUser(String uid) throws SQLException {
        ResultSet users = this.stmt.executeQuery("SELECT * FROM users WHERE uidUser = '" + uid + "'");
        User user = new User();

        while (users.next()) {
            user.idUser = users.getInt("idUser");
            user.nomUser = users.getString("nomUser");
            user.prenomUser = users.getString("prenomUser");
            user.uidUser = users.getString("uidUser");
        }
        return user;
    }

    public String deleteEntity(String uid) throws SQLException {
        int idCatalogue = 0;
        int nbTotal = 0;
        int nbDispo = 0;
        if (uid.length() == 14) {
            this.stmt.executeUpdate("DELETE FROM users WHERE uidUser = '" + uid + "'");
            return "Delete OK";
        } else if (uid.length() == 8) {
            ResultSet produits = this.stmt.executeQuery("SELECT * FROM stock WHERE uidProduit = '" + uid + "'");
            while (produits.next()) {
                idCatalogue = produits.getInt("idCatalogue");
            }
            ResultSet catalogues = this.stmt.executeQuery("SELECT * FROM catalogue WHERE idCatalogue = '" + idCatalogue + "'");
            while (catalogues.next()) {
                nbTotal = catalogues.getInt("nbTotal");
                nbDispo = catalogues.getInt("nbDispo");
            }
            nbTotal--;
            if (nbDispo > 0) {
                nbDispo--;
            }
            String nbDispoS = ", nbdispo = " + nbDispo;
            this.stmt.executeUpdate("UPDATE catalogue SET nbTotal = " + nbTotal + " " + nbDispoS + " WHERE idCatalogue = '" + idCatalogue + "'");
            this.stmt.executeUpdate("DELETE FROM stock WHERE uidProduit = '" + uid + "'");
            return "Delete OK";
        }
        return "Delete NOK";
    }

    public void closeConnection() throws SQLException {
        this.con.close();
    }
}
