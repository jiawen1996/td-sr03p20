package components;
import java.util.Objects;

public class User {
	private String firstName, lastName, email, pwd, gender;
	private String role;
	//CONSTRUCTEUR 
	public User(String fname, String lname, String email, String pwd, String gender, String role) {
		this.firstName = fname;
		this.lastName = lname;
		this.email = email;
		this.pwd = pwd;
		this.gender = gender;
		this.role = role;
	}
	
	//RÉCUPÉRER DES DONNÉES
	public String getFirstName() {
		return this.firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String getPwd() {
		return this.pwd;
	}
	
	public String getGender() {
		return this.gender;
	}
	public String getUser() {
		return "First name : " + this.firstName + " Last Name : " + this.lastName + " Email : " + this.email + " Pwd : " + this.pwd + " Gender : " + this.gender;
	}
	
	//VÉRIFICATION SI LE DOUBLONS
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null) {
			return false;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		final User other = (User) obj;
		if (!Objects.equals(this.lastName, other.lastName)) {
			return false;
		}
		if (!Objects.equals(this.firstName, other.firstName)) {
			return false;
		}		
		return true;
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
