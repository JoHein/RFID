package RFID.projetRFID;

public class User {

    int idUser;
    String nomUser;
    String uidUser;

    public User() {
    }

    public User(int idUser, String nomUser, String uidUser) {
        this.idUser = idUser;
        this.nomUser = nomUser;
        this.uidUser = uidUser;
    }

    public String toString() {
        String json = "[{\"idUser\" : " + this.idUser +
                ",\"nomUser\":\"" + this.nomUser +
                ",\"uidUser\":\"" + this.uidUser + "\"}]";;
    
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

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
