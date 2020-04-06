package components;

public class Admin extends Person {

	public Admin(String fname, String lname, String email, String pwd, String gender, String role) {
		super(fname, lname, email, pwd, gender, role);
	}
	
	public void createUser() {
		
	}
	
	public void deleteUser() {
		
	}
	
	public void createForum(String titre, String description) {
		new Forum(titre,description);
	}
	
	public void deleteForum(Forum forum) { 
		//delete Messages du Forum
		
		
	}
	
}
