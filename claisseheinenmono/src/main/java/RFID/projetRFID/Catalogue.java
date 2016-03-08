package RFID.projetRFID;

public class Catalogue {
	int idCatalogue;
	String nomCatalogue;
	int nbDispo;
	
	public Catalogue(){}
	
	public Catalogue(int idCatalogue, String nomCatalogue, int nbDispo){
		this.idCatalogue=idCatalogue;
		this.nomCatalogue = nomCatalogue;
		this.nbDispo=nbDispo;
	}
	
	
	
    public String toString() {
        String json = "[{\"idCatalogue\" : " + this.idCatalogue +
                ",\"nomCatalogue\":\"" + this.nomCatalogue +
                "\",\"dispo\":" + this.nbDispo + "}]";
    
		return json;
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

	public int getNbDispo() {
		return nbDispo;
	}

	public void setNbDispo(int nbDispo) {
		this.nbDispo = nbDispo;
	}
    
}

