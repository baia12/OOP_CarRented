package model;
public class User {
    private String fullName;
    private String icNumber;
    private String university;
    private String matricNumber;
    private String username;
    private String password;
    private boolean verified;
    private boolean blacklisted;

    public User(String fullName, String icNumber, String university, String matricNumber,
                String username, String password) {
        this.fullName = fullName;
        this.icNumber = icNumber;
        this.university = university;
        this.matricNumber = matricNumber;
        this.username = username;
        this.password = password;
        this.verified = false;
        this.blacklisted = false;
    }

    public String getFullName() { return fullName; }
    public String getUsername() { return username; }
    public String getICNumber() { return icNumber; }
    public String getUni() { return university; }
    public String getMatric() { return matricNumber; }
    public boolean isVerified() { return verified; }
    public void setVerified(boolean val) { this.verified = val; }
    public boolean isBlacklisted() { return blacklisted; }
    public void setBlacklisted(boolean val) { this.blacklisted = val; }
    public boolean checkPassword(String pass) { return password.equals(pass); }
    public String toString() { 
        return fullName + (verified ? " [Verified]" : " [Not Verified]") + (blacklisted ? " [Blacklisted]" : "");
    }
    
}