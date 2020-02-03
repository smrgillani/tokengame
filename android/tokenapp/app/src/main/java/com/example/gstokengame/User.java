package com.example.gstokengame;


import com.google.gson.Gson;

public class User {
    private String id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String dateOfBirth;
    private String membershipDate;
    private String oauthtoken;
    private String modified;
    private String cityString;
    private String currencyString;
    private String genderString;
    private String facebookId;

    public User(String id, String password, String firstName, String lastName, String email, String phone, String dateOfBirth, String membershipDate, String oauthtoken, String modified, String cityString, String currencyString, String genderString, String facebookId) {
        this.id = id;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.membershipDate = membershipDate;
        this.oauthtoken = oauthtoken;
        this.modified = modified;
        this.cityString = cityString;
        this.currencyString = currencyString;
        this.genderString = genderString;
        this.facebookId = facebookId;
    }

    public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    static public User create(String serializedData) {
        Gson gson = new Gson();
        return gson.fromJson(serializedData, User.class);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMembershipDate() {
        return membershipDate;
    }

    public void setMembershipDate(String membershipDate) {
        this.membershipDate = membershipDate;
    }

    public String getOauthtoken() {
        return oauthtoken;
    }

    public void setOauthtoken(String oauthtoken) {
        this.oauthtoken = oauthtoken;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getCityString() {
        return cityString;
    }

    public void setCityString(String cityString) {
        this.cityString = cityString;
    }

    public String getCurrencyString() {
        return currencyString;
    }

    public void setCurrencyString(String currencyString) {
        this.currencyString = currencyString;
    }

    public String getGenderString() {
        return genderString;
    }

    public void setGenderString(String genderString) {
        this.genderString = genderString;
    }

    public String getFacebookId() { return facebookId; }

    public void setFacebookId(String facebookId) { this.facebookId = facebookId; }
}
