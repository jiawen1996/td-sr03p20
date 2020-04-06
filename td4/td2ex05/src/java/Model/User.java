/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Objects;

/**
 *
 * @author lounis
 */


public class User {
	private String lastName; 
        private String firstName;
        private String login;
        private String gender;
        private int id;
        private static int autoIncrement=0;
        private String role;

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public static int getAutoIncrement() {
        return autoIncrement;
    }
    public User() {
    }

    public User(String lastName, String firstName, String login, int key, String gender, String pwd) {
        autoIncrement++;
        this.lastName = lastName;
        this.firstName = firstName;
        this.login = login;
        this.gender = gender;
        this.pwd = pwd;
        this.id=User.autoIncrement;
    }

    public int getId() {
        return id;
    }


    public void setLogin(String login) {
        this.login = login;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getLogin() {
        return login;
    }

    public String getPwd() {
        return pwd;
    }
        private String pwd;

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void finalize(){
        autoIncrement--;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.lastName);
        hash = 97 * hash + Objects.hashCode(this.firstName);
        return hash;
    }

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
	
    
	
	public User(String lastName, String firstName){
		this.lastName = lastName;
                this.firstName = firstName;
               
              
	}

    @Override
    public String toString() {
        return "User{" + "lastName=" + lastName + ", firstName=" + firstName + ""
               + ", login=" + login  + ", gender=" + gender + ","
                + " pwd=" + pwd + '}';
    }
	
	


	
}
