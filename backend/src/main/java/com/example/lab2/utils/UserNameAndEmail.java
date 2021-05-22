package com.example.lab2.utils;


public class UserNameAndEmail implements Comparable<UserNameAndEmail> {
    public String username;
    public String email;

    public UserNameAndEmail(String username, String email) {
        this.username = username;
        this.email = email;
    }

    @Override
    public int compareTo(UserNameAndEmail o) {
        if (this.username.compareTo(o.username) != 0) {
            return this.username.compareTo(o.username);
        }
        return this.email.compareTo(o.email);
    }

    @Override
    public int hashCode() {
        return this.username.hashCode() + this.email.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UserNameAndEmail)) {
            return false;
        }
        UserNameAndEmail une = (UserNameAndEmail) obj;
        return this.username.equals(une.username) && this.email.equals(une.email);
    }
}