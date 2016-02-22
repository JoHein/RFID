package RFID.projetRFID;

public class Produit {

    int idStock;
    String uidProduits;
    int idCatalogue;
    String nomCatalogue;
    int dispo;

    public Produit() {
    }

    public Produit(int idStock, String uidProduits, int idCatalogue, String nomCatalogue, int dispo) {
        this.idStock = idStock;
        this.uidProduits = uidProduits;
        this.idCatalogue = idCatalogue;
        this.nomCatalogue = nomCatalogue;
        this.dispo = dispo;
    }

    public String toString() {
        String json = "{\"idStock\" : " + this.idStock +
                ",\"uidProduit\":\"" + this.uidProduits +
                "\",\"idCatalogue\":" + this.idCatalogue +
                ",\"nomCatalogue\":\"" + this.nomCatalogue +
                "\",\"dispo\":" + this.dispo + "}";
        return json;
    }

    public int getIdStock() {
        return idStock;
    }

    public void setIdStock(int idStock) {
        this.idStock = idStock;
    }

    public String getUidProduits() {
        return uidProduits;
    }

    public void setUidProduits(String uidProduits) {
        this.uidProduits = uidProduits;
    }

    public int getIdCatalogue() {
        return idCatalogue;
    }

    public void setIdCatalogue(int idCatalogue) {
        this.idCatalogue = idCatalogue;
    }

    public String getNomCatalogue() {
        return nomCatalogue;
    }

    public void setNomCatalogue(String nomCatalogue) {
        this.nomCatalogue = nomCatalogue;
    }

    public int getDispo() {
        return idStock;
    }

    public void setDispo(int dispo) {
        this.dispo = dispo;
    }

}
