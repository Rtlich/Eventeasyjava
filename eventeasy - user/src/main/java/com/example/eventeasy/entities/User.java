package com.example.eventeasy.entities;


public class User {

    private int id;
    private String email;
    private String password;
    private String fname;
    private String lname;
    private int phonenumber;
    private UserRole role;
    private boolean enabled;

    public User(int id, String email, String password, String fname, String lname, int phonenumber, UserRole role, boolean enabled) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
        this.phonenumber = phonenumber;
        this.role = role;
        this.enabled = enabled;
    }

    public User(String email, String password, String fname, String lname, int phonenumber, UserRole role, boolean enabled) {
        this.email = email;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
        this.phonenumber = phonenumber;
        this.role = role;
        this.enabled = enabled;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public int getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(int phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getRoleString() {
        return role == UserRole.Admin ? "[\"ROLE_ADMIN\"]" : "[\"ROLE_USER\"]";
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


}