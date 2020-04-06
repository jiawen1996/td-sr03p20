package components;

import java.util.Objects;

public abstract class Person {
	protected String firstName;
	protected String lastName;
	protected String email;
	protected String pwd;
	protected String gender;
	protected String role;
	
	// CONSTRUCTEUR
	public Person(String fname, String lname, String email, String pwd, String gender, String role) {
		this.firstName = fname;
		this.lastName = lname;
		this.email = email;
		this.pwd = pwd;
		this.gender = gender;
		this.role = role;
	}
	
	// RÉCUPÉRER DES DONNÉES
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

		public String getRole() {
			return this.role;
		}

		public String toString() {
			return "First name : " + this.firstName + " Last Name : " + this.lastName + " Email : " + this.email + " Pwd : "
					+ this.pwd + " Gender : " + this.gender;
		}
		
		
		// VÉRIFICATION SI LE DOUBLONS
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
}
