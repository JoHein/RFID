package RFID.projetRFID;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

        this.con = DriverManager.getConnection(url, "root", "");

        this.stmt = con.createStatement();
    }
    
    public List<Catalogue> getAllCatalogues() throws SQLException{
        ResultSet allCat = this.stmt.executeQuery("SELECT * FROM catalogue");
        
        List<Catalogue> listCat = new ArrayList<Catalogue>();
        
        while (allCat.next()){
            Catalogue catalogue = new Catalogue();
        	catalogue.idCatalogue=allCat.getInt("idCatalogue");
        	catalogue.nomCatalogue=allCat.getString("nomCatalogue");
        	catalogue.nbDispo=allCat.getInt("nbDispo");
        	listCat.add(catalogue);
        }
        return listCat;
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
            user.idUser = users.getInt("idStock");
            user.nomUser = users.getString("nomUser");
            user.uidUser = users.getString("uidUser");

        }
        return user;
    }

    public void closeConnection() throws SQLException {
        this.con.close();
    }
}
