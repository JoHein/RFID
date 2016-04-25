package RFID.projetRFID;

public class Emprunt {

    int idEmprunt;
    String uidProduit;
    String uidUser;
    String dateEmprunt;

    public Emprunt() {
    }

    public Emprunt(int idEmprunt, String uidProduit, String uidUser, String dateEmprunt) {
        this.idEmprunt = idEmprunt;
        this.uidProduit = uidProduit;
        this.uidUser = uidUser;
        this.dateEmprunt = dateEmprunt;
    }

    /**
     *
     * @return classe Emprunt formatée en JSON
     */

    public String toString() {
        String json = "[{\"idEmprunt\" : " + this.idEmprunt + ",\"uidProduit\":\"" + this.uidProduit + "\",\"uidUser\":\"" + this.uidUser + "\",\"dateEmprunt\":\"" + this.dateEmprunt + "\"}]";
        return json;
    }
}

