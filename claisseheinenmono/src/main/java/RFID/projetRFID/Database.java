package RFID.projetRFID;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
     * <p>Permet de vérifier la disponibilité d'un livre a partir de son UID</p>
     *
     * @param uid : l'UID du livre
     * @return retour : 0 si pas dispo, 1 si dispo
     * @throws SQLException
     */

    public int isDispo(String uid) throws SQLException {
        int retour = -1;
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
        int retour = -1;
        ResultSet rs;
        String sql = "";
        switch (table) {
            case "user":
                rs = this.stmt.executeQuery("SELECT * FROM users WHERE uidUser = '" + info + "'");
                rs.last();
                retour = rs.getRow();
                rs.beforeFirst();
                break;
            case "stock":
                rs = this.stmt.executeQuery("SELECT * FROM stock WHERE uidProduit = '" + info + "'");
                rs.last();
                retour = rs.getRow();
                rs.beforeFirst();
                break;
            case "emprunt":
                String data = "";
                if (info.length() == 14) data = "uidUser";
                else if (info.length() == 8) data = "uidProduit";
                else break;
                //System.out.println (data);
                sql = "SELECT * FROM emprunt WHERE " + data + " = '" + info + "'";
                System.out.println(sql);
                rs = this.stmt.executeQuery(sql);
                rs.last();
                retour = rs.getRow();
                rs.beforeFirst();
                System.out.println(retour);
                break;
            case "empruntOeuvre":
                sql = "SELECT * FROM emprunt WHERE uidProduit IN (SELECT uidProduit FROM stock WHERE idCatalogue = '"+info+"')";
                System.out.println(sql);
                rs = this.stmt.executeQuery(sql);
                rs.last();
                retour = rs.getRow();
                rs.beforeFirst();
                System.out.println(retour);
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
            int nbDispo = this.getNbDispo(uidProduit);
            int dispo = 0;
            if (action.equals("Emprunt")) {
                if (this.isDispo(uidProduit) == 0) {
                    //System.out.println(uidProduit + " non dispo");
                    return "[{\"retour\": \"Non Disponible\"}]";
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String date = sdf.format(new Date());
                //System.out.println("INSERT INTO emprunt (uidProduit,uidUser,dateEmprunt) VALUES ('" + uidProduit + "','" + uidUser + "','" + date + "')");
                this.stmt.executeUpdate("INSERT INTO emprunt (uidProduit,uidUser,dateEmprunt) VALUES ('" + uidProduit + "','" + uidUser + "','" + date + "')");
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
     * @param action      : create ou delete
     * @param idCatalogue : ID du catalogue
     * @return
     * @throws SQLException
     */

    public String manageCatalogue(String action, Integer idCatalogue, String nomCatalogue, String auteur, String type, String categorie) throws SQLException {
        //System.out.println("action : " + action + " titre : " + nomCatalogue);
        if (action.equals("Création")) {
            this.stmt.executeUpdate("INSERT INTO catalogue (nomCatalogue,auteur,nbDispo,nbTotal,type,categorie) VALUES ('" + nomCatalogue + "','" + auteur + "','0','0','" + type + "','" + categorie + "')");
        } else {
            if (this.isInDb("empruntOeuvre", Integer.toString(idCatalogue)) >= 1)
                return "[{\"retour\": \"Oeuvre dans Emprunt\"}]";
            this.stmt.executeUpdate("DELETE FROM stock WHERE idCatalogue = '" + idCatalogue + "'");
            this.stmt.executeUpdate("DELETE FROM catalogue WHERE idCatalogue = '" + idCatalogue + "'");
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
            return "[{\"retour\": \"Ajout Utilisateur OK\"}]";
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
            this.stmt.executeUpdate("UPDATE catalogue SET nbDispo = " + nbDispo + ", nbTotal = " + nbTotal + " WHERE idCatalogue LIKE " + param1);
            return "[{\"retour\": \"Ajout Produit OK\"}]";
        } else {
            return "[{\"retour\": \"Mauvais type\"}]";
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

            //System.out.println("Carte produit détectée");
            // traitement carte produit
            Produit produit = this.getProduitStock(uid);
            if ((produit.getUidProduits()) == null) {
                return "[{\"uidNew\": \"" + uid + "\"}]";
            }
            //System.out.println(produit.toString());
            data = produit.toString();
            return data;

        } else if (uid.length() == 14) {

            //System.out.println("Carte utilisateur détectée");
            Database base = new Database();
            base.prepareToQuery();
            User user = base.getProduitUser(uid);
            if ((user.getUidUser()) == null) {
                return "[{\"uidNew\": \"" + uid + "\"}]";
            }
            //System.out.println(user.toString());
            data = user.toString();
            return data;

        } else {
            //System.out.println("Merci de passer une carte valide");
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
            obj.put("nbTotal", allCat.getInt("nbTotal"));
            obj.put("nbDispo", allCat.getInt("nbDispo"));
            obj.put("auteur", allCat.getString("auteur"));
            obj.put("type", allCat.getString("type"));
            obj.put("categorie", allCat.getString("categorie"));

            listCat.put(obj);
        }
        //System.out.println("Database");
        //System.out.println(listCat);
        resList.put("Livres", listCat);
        return resList;
    }

    /**
     * <p>Récupérer tout les emprunts en base</p>
     *
     * @return
     * @throws SQLException
     * @throws JSONException
     */

    public JSONObject getAllBorrow() throws SQLException, JSONException {
        ResultSet allBorrow = this.stmt.executeQuery("SELECT * FROM emprunt");
        ArrayList<Emprunt> borrow = new ArrayList<Emprunt>();
        ResultSet allUser;
        ResultSet allProduit;
        ResultSet allCatalogue;
        JSONArray listBorrow = new JSONArray();
        JSONObject resList = new JSONObject();

        while (allBorrow.next()) {
            Emprunt emp = new Emprunt();
            emp.idEmprunt = allBorrow.getInt("idEmprunt");
            emp.uidUser = allBorrow.getString("uidUser");
            emp.uidProduit = allBorrow.getString("uidProduit");
            emp.dateEmprunt = allBorrow.getString("dateEmprunt");
            borrow.add(emp);
        }
        for (Emprunt emp : borrow) {
            String etudiant = "";
            String oeuvre = "";
            int idCatalogue = 0;
            allUser = this.stmt.executeQuery("SELECT * FROM users WHERE uidUser LIKE '" + emp.uidUser + "'");
            while (allUser.next()) {
                etudiant = allUser.getString("nomUser") + " " + allUser.getString("prenomUser");
            }
            allProduit = this.stmt.executeQuery("SELECT * FROM stock WHERE uidProduit LIKE '" + emp.uidProduit + "'");
            while (allProduit.next()) {
                idCatalogue = allProduit.getInt("idCatalogue");
            }
            allCatalogue = this.stmt.executeQuery("SELECT * FROM catalogue WHERE idCatalogue LIKE '" + idCatalogue + "'");
            while (allCatalogue.next()) {
                oeuvre = allCatalogue.getString("nomCatalogue");
            }
            JSONObject obj = new JSONObject();
            obj.put("idEmprunt", emp.idEmprunt);
            obj.put("etudiant", etudiant);
            obj.put("nomCatalogue", oeuvre);
            obj.put("date", emp.dateEmprunt);
            listBorrow.put(obj);
        }
        //System.out.println("Database");
        //System.out.println(listBorrow);
        resList.put("Emprunt", listBorrow);
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
        ResultSet nbEmprunt;
        ArrayList<User> users = new ArrayList<User>();
        JSONArray listUsers = new JSONArray();
        JSONObject resList = new JSONObject();

        while (allUsers.next()) {
            User usr = new User();
            usr.idUser = allUsers.getInt("idUser");
            usr.nomUser = allUsers.getString("nomUser");
            usr.prenomUser = allUsers.getString("prenomUser");
            usr.uidUser = allUsers.getString("uidUser");
            users.add(usr);
        }
        //System.out.println(users);

        for (User usr : users) {
            int nbEmp = 0;
            nbEmprunt = this.stmt.executeQuery("SELECT * FROM emprunt WHERE uidUser LIKE '" + usr.uidUser + "'");
            while (nbEmprunt.next()) {
                nbEmp++;
            }
            JSONObject obj = new JSONObject();
            obj.put("iduser", usr.idUser);
            obj.put("nomUser", usr.nomUser);
            obj.put("prenomUser", usr.prenomUser);
            obj.put("uidUser", usr.uidUser);
            obj.put("nbEmprunt", nbEmp);
            listUsers.put(obj);
        }

        //System.out.println("Database");
        //System.out.println(listUsers);
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
            obj.put("auteur", allCat.getString("auteur"));
            obj.put("type", allCat.getString("type"));
            obj.put("categorie", allCat.getString("categorie"));

            listCat.put(obj);
        }
        //System.out.println("Database");
        //System.out.println(listCat);
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
     * <p>Trouver un user avec son UID</p>
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
            if (this.isInDb("emprunt", uid) >= 1) return "[{\"retour\": \"User dans Emprunt\"}]";
            if (this.isInDb("user", uid) == 0) return "[{\"retour\": \"User pas dans la database\"}]";
            this.stmt.executeUpdate("DELETE FROM users WHERE uidUser = '" + uid + "'");
            return "[{\"retour\": \"Suppression utilisateur OK\"}]";
        } else if (uid.length() == 8) {
            if (this.isInDb("emprunt", uid) >= 1) return "[{\"retour\": \"Livre dans Emprunt\"}]";
            if (this.isInDb("produit", uid) == 0) return "[{\"retour\": \"Livre pas dans la database\"}]";
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
            return "[{\"retour\": \"Suppression livre OK\"}]";
        }
        return "[{\"retour\": \"Suppression \"}]";
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
