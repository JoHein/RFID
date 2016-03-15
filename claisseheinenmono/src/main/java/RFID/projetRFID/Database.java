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

        String url = "jdbc:mysql://localhost:3307/rfid";
       // String url = "jdbc:mysql://localhost:3306/rfid";

        this.con = DriverManager.getConnection(url, "root", "");
        this.stmt = con.createStatement();
    }
    
    
    public JSONObject getAllCatalogues() throws SQLException, JSONException{
        ResultSet allCat = this.stmt.executeQuery("SELECT * FROM catalogue");
        JSONArray listCat = new JSONArray();
        JSONObject resList = new JSONObject();

        while (allCat.next()){
        	JSONObject obj = new JSONObject();
        	obj.put("idCatalqogue", allCat.getInt("idCatalogue"));
        	obj.put("nomCatalogue", allCat.getString("nomCatalogue"));
        	obj.put("nbDispo", allCat.getInt("nbDispo"));
        	listCat.put(obj);
        }
        System.out.println("Database");
        System.out.println(listCat);
        resList.put("Livres",listCat);
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
            user.uidUser = users.getString("uidUser");
        }
        return user;
    }

    public void closeConnection() throws SQLException {
        this.con.close();
    }
}
