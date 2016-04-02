package RFID.projetRFID;

public class User {

    int idUser;
    String nomUser;
    String prenomUser;
    String uidUser;

    public User() {
    }

    public User(int idUser, String nomUser, String prenomUser, String uidUser) {
        this.idUser = idUser;
        this.nomUser = nomUser;
        this.prenomUser = prenomUser;
        this.uidUser = uidUser;
    }

    public String toString() {
        String json = "[{\"idUser\" : " + this.idUser +",\"nomUser\":\"" + this.nomUser +"\",\"prenomUser\":\"" + this.prenomUser +"\",\"uidUser\":\"" + this.uidUser + "\"}]";
        return json;
    }

    public String getUidUser() {
        return uidUser;
    }

    public void setUidUser(String uidUser) {
        this.uidUser = uidUser;
    }

    public String getNomUser() {
        return nomUser;
    }

    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }

    public String getPrenomUser() {
        return prenomUser;
    }

    public void setPrenomUser(String prenomUser) {
        this.prenomUser = prenomUser;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
