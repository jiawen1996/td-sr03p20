package components;

public class Forum {
	private String titre;
	private String description;
	
	public Forum(String t, String d) {
		this.titre = t;
		this.description = d;
	}
	
	//Getter
	public String getTitre() {
		return titre;
	}
	
	public String getDescription() {
		return description;
	}
	
	//Setter
	public void setTitre(String titre) {
		this.titre = titre;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}
