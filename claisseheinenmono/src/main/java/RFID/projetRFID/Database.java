package RFID.projetRFID;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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

    /**
     * <p>Connecte à la BDD et crée un statement</p>
     *
     * @throws SQLException
     */

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

    /**
     * <p<Permet de vérifier la disponibilité d'un livre a partir de son UID
     *
     * @param uid : l'UID du livre
     * @return retour : 0 si pas dispo, 1 si dispo
     * @throws SQLException
     */

    public int isDispo(String uid) throws SQLException {
        int retour=-1;
        ResultSet rs;
        rs = this.stmt.executeQuery("SELECT dispo FROM stock WHERE uidProduit = '" + uid + "'");
        while (rs.next()) {
            retour = rs.getInt("dispo");
        }
        return retour;
    }

    /**
     * <p>Permet de vérifier l'existence de choses en base</p>
     * <p>Présence d'une carte user en base on passe user et UID de la carte</p>
     * <p>Présence d'une carte livre en base on passe stock et UID de la carte</p>
     *
     * @param table : la table sur laquelle vérifier
     * @param info  : la data à vérifier
     * @return retour : 1 si en base, 0 si pas en base, -1 si pas bonne table
     * @throws SQLException
     */

    public int isInDb(String table, String info) throws SQLException {
        int retour = 10;
        ResultSet rs;
        ResultSetMetaData rsm;
        switch (table) {
            case "user":
                rs = this.stmt.executeQuery("SELECT * FROM users WHERE uidUser = '" + info + "'");
                rsm = rs.getMetaData();
                retour = rsm.getColumnCount();
                break;
            case "stock":
                rs = this.stmt.executeQuery("SELECT * FROM stock WHERE uidProduit = '" + info + "'");
                rsm = rs.getMetaData();
                retour = rsm.getColumnCount();
                break;
            default:
                retour = -1;
                break;
        }
        return retour;
    }

    /**
     * <p>Permet de gérer les emprunts suivant une action</p>
     *
     * @param action     : borrow ou return
     * @param uidUser
     * @param uidProduit
     * @return
     * @throws SQLException
     */

    public String manageBorrow(String action, String uidUser, String uidProduit) throws SQLException {
        if (uidUser.length() == 14 && uidProduit.length() == 8) {
            if (this.isDispo(uidProduit) == 0) {
                return "[{\"retour\": \"Non Disponible\"}]";
            }
            int nbDispo = this.getNbDispo(uidProduit);
            int dispo = 0;
            if (action.equals("borrow")) {
                this.stmt.executeUpdate("INSERT INTO emprunt (uidProduit,uidUser) VALUES ('" + uidProduit + "','" + uidUser + "')");
                if (nbDispo > 0) nbDispo--;
            } else {
                this.stmt.executeUpdate("DELETE FROM emprunt WHERE uidProduit = '" + uidProduit + "' AND uidUser = '" + uidUser + "'");
                nbDispo++;
                dispo = 1;
            }
            this.stmt.executeUpdate("UPDATE catalogue SET nbDispo = " + nbDispo + " WHERE idCatalogue = (SELECT idCatalogue FROM stock WHERE uidProduit = '" + uidProduit + "')");
            this.stmt.executeUpdate("UPDATE stock SET dispo = " + dispo + " WHERE uidProduit = '" + uidProduit + "'");
            return "[{\"retour\": \"" + action + " OK\"}]";

        } else {
            return "[{\"retour\": \"Emprunt non ajouté car mauvaises cartes:\"" + uidUser.length() + "\" et produit \"" + uidProduit.length() + "\"}]";

        }
    }

    /**
     * <p>Permet de gérer les catalogues suivant une action</p>
     *
     * @param action : create ou delete
     * @param info   : peut être soit un ID pour delete soit un nom pour create
     * @return
     * @throws SQLException
     */

    public String manageCatalogue(String action, String info) throws SQLException {
        System.out.println("action : "+action+" info : "+info);
        if (action.equals("create")) {
            System.out.println("create");
            this.stmt.executeUpdate("INSERT INTO catalogue (nomCatalogue,nbDispo,nbTotal) VALUES ('" + info + "','0','0')");
        } else {
            System.out.println("delete");
            this.stmt.executeUpdate("DELETE FROM catalogue WHERE idCatalogue = '" + info + "'");
        }
        return "[{\"retour\":\"" + action + " Catalogue OK\"}]";
    }

    /**
     * <p>Ajouter une entité à la base</p>
     *
     * @param type   : user ou product
     * @param uid
     * @param param1
     * @param param2
     * @return
     * @throws SQLException
     */

    public String addEntity(String type, String uid, String param1, String param2) throws SQLException {
        if (type.equals("user")) {
            this.stmt.executeUpdate("INSERT INTO users (nomUser,prenomUser,uidUser) VALUES ('" + param1 + "','" + param2 + "','" + uid + "')");
            return "[{\"retour\": \"Ajout User OK\"}]";
        } else if (type.equals("product")) {
            this.stmt.executeUpdate("INSERT INTO stock (idCatalogue,dispo,uidProduit) VALUES ('" + param1 + "','" + param2 + "','" + uid + "')");
            ResultSet rs = this.stmt.executeQuery("SELECT nbTotal,nbDispo FROM catalogue WHERE idCatalogue = '" + param1 + "'");
            int nbTotal = 0;
            int nbDispo = 0;
            while (rs.next()) {
                nbTotal = rs.getInt("nbTotal");
                nbDispo = rs.getInt("nbDispo");
            }
            if (param2.equals("1")) nbDispo++;
            nbTotal++;
            this.stmt.executeUpdate("UPDATE catalogue SET nbDispo = " + nbDispo + ", nbTotal = " + nbTotal);
            return "[{\"retour\": \"Ajout Produit OK\"}]";
        } else {
            return "[{\"retour\": \"Bad type\"}]";
        }
    }

    /**
     * <p>Récupérer le nombre de livres dispo pour un titre</p>
     *
     * @param uid
     * @return
     * @throws SQLException
     */

    public int getNbDispo(String uid) throws SQLException {
        int idCatalogue = 0;
        int nbDispo = 0;
        ResultSet rs = this.stmt.executeQuery("SELECT idCatalogue FROM stock WHERE uidProduit = '" + uid + "'");
        while (rs.next()) {
            idCatalogue = rs.getInt("idCatalogue");
        }
        rs = this.stmt.executeQuery("SELECT nbDispo FROM catalogue WHERE idCatalogue = '" + idCatalogue + "'");
        while (rs.next()) {
            nbDispo = rs.getInt("nbDispo");
        }
        return nbDispo;
    }

    /**
     * <p>Récupérer les données d'une carte passé</p>
     *
     * @param uid
     * @return
     * @throws SQLException
     */

    public String getCardData(String uid) throws SQLException {
        String data = "";
        if (uid.length() == 8) {

            System.out.println("Carte produit détectée");
            // traitement carte produit
            Produit produit = this.getProduitStock(uid);
            if ((produit.getUidProduits()) == null) {
                return "[{\"uidNew\": \"" + uid + "\"}]";
            }
            System.out.println(produit.toString());
            data = produit.toString();
            return data;

        } else if (uid.length() == 14) {

            System.out.println("Carte user détectée");
            Database base = new Database();
            base.prepareToQuery();
            User user = base.getProduitUser(uid);
            if ((user.getUidUser()) == null) {
                return "[{\"uidNew\": \"" + uid + "\"}]";
            }
            System.out.println(user.toString());
            data = user.toString();
            return data;

        } else {
            System.out.println("Merci de passer une carte valide");
        }
        return data;
    }

    /**
     * <p>Récupérer tout les catalogues en base</p>
     *
     * @return
     * @throws SQLException
     * @throws JSONException
     */

    public JSONObject getAllCatalogues() throws SQLException, JSONException {
        ResultSet allCat = this.stmt.executeQuery("SELECT * FROM catalogue");
        JSONArray listCat = new JSONArray();
        JSONObject resList = new JSONObject();

        while (allCat.next()) {
            JSONObject obj = new JSONObject();
            obj.put("idCatalogue", allCat.getInt("idCatalogue"));
            obj.put("nomCatalogue", allCat.getString("nomCatalogue"));
            obj.put("nbDispo", allCat.getInt("nbDispo"));
            listCat.put(obj);
        }
        System.out.println("Database");
        System.out.println(listCat);
        resList.put("Livres", listCat);
        return resList;
    }

    /**
     * <p>Récupérer tout les users en base</p>
     *
     * @return
     * @throws SQLException
     * @throws JSONException
     */

    public JSONObject getAllUsers() throws SQLException, JSONException {
        ResultSet allUsers = this.stmt.executeQuery("SELECT * FROM users");
        JSONArray listUsers = new JSONArray();
        JSONObject resList = new JSONObject();

        while (allUsers.next()) {
            JSONObject obj = new JSONObject();
            obj.put("idUser", allUsers.getInt("idUser"));
            obj.put("nomUser", allUsers.getString("nomUser"));
            obj.put("prenomUser", allUsers.getString("prenomUser"));
            obj.put("uidUser", allUsers.getString("uidUser"));
            listUsers.put(obj);
        }
        System.out.println("Database");
        System.out.println(listUsers);
        resList.put("Users", listUsers);
        return resList;
    }

    /**
     * <p>Trouver un livre avec son titre</p>
     *
     * @param search
     * @return
     * @throws SQLException
     * @throws JSONException
     */

    public JSONObject getBookByTitle(String search) throws SQLException, JSONException {
        ResultSet allCat = this.stmt.executeQuery("SELECT * FROM catalogue where nomCatalogue like '%" + search + "%'");
        JSONArray listCat = new JSONArray();
        JSONObject resList = new JSONObject();

        while (allCat.next()) {
            JSONObject obj = new JSONObject();
            obj.put("idCatalogue", allCat.getInt("idCatalogue"));
            obj.put("nomCatalogue", allCat.getString("nomCatalogue"));
            obj.put("nbDispo", allCat.getInt("nbDispo"));
            listCat.put(obj);
        }
        System.out.println("Database");
        System.out.println(listCat);
        resList.put("Livres", listCat);
        return resList;
    }

    /**
     * <p>Trouver un produit avec son UID</p>
     *
     * @param uid
     * @return
     * @throws SQLException
     */

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

    /**
     * Heu...
     *
     * @param uid
     * @return
     * @throws SQLException
     */

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

    /**
     * <p>Supprimer une entité de la base</p>
     *
     * @param uid
     * @return
     * @throws SQLException
     */

    public String deleteEntity(String uid) throws SQLException {
        int idCatalogue = 0;
        int nbTotal = 0;
        int nbDispo = 0;
        if (uid.length() == 14) {
            this.stmt.executeUpdate("DELETE FROM users WHERE uidUser = '" + uid + "'");
            return "[{\"retour\": \"Delete utilisateur OK\"}]";
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
            return "[{\"retour\": \"Delete livre OK\"}]";
        }
        return "[{\"retour\": \"Delete \"}]";
    }

    /**
     * <p>Fermer une connexion</p>
     *
     * @throws SQLException
     */

    public void closeConnection() throws SQLException {
        this.con.close();
    }
}
