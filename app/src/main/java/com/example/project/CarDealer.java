package com.example.project;

public class CarDealer {
    private int id;
    private String dealerName;
    private String dealerInformation;

    private String email;

    private String phoneNumber;

    public CarDealer(int id, String dealerName, String dealerInformation) {
        this.id = id;
        this.dealerName = dealerName;
        this.dealerInformation = dealerInformation;
    }

    public CarDealer(){}


    public int getId() {
        return id;
    }

    public String getDealerName() {
        return dealerName;
    }

    public String getDealerInformation() {
        return dealerInformation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "CarDealer{" +
                "id=" + id +
                ", dealerName='" + dealerName + '\'' +
                ", dealerInformation='" + dealerInformation + '\'' +
                '}';
    }
}
