package components;

import java.util.ArrayList;
import java.util.Objects;

public class User extends Person{
	private ArrayList<Forum> listFollowingForum;

	// CONSTRUCTEUR
	public User(String fname, String lname, String email, String pwd, String gender, String role) {
		super(fname, lname, email, pwd, gender, role);
		this.listFollowingForum = new ArrayList<Forum>();
	}

	// MANIPULATE FORUM
	/**
	 * Récupérer la liste de forums abonnés
	 * @return
	 */
	public ArrayList<Forum> getlistFollowingForum() {
		return listFollowingForum;
	}
	
	/**
	 * Abonner un forum en l'ajoutant dans la liste listFollowingForum 
	 * @param forum
	 */
	public Boolean follow(Forum forum) {
		return listFollowingForum.add(forum);
	}

	/**
	 * Désabonner un forum en le supprimer dans la listz
	 * @param forum
	 * @return
	 */
	public Boolean unFollow(Forum forum) {
		return listFollowingForum.remove(forum);
	}
	
	// MESSAGE
}
