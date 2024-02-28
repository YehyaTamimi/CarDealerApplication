package com.example.project;

public class Cars {
    private int id;
    private String type;

    private String information;
    private int price;
    private int num_doors;
    private String state;
    private int milage;
    private int year;

    private String ReserveDate;

    private int offerPrice;

    public Cars(int id, String type, String information, int price, int num_doors, String state, int milage, int year) {
        this.id = id;
        this.type = type;
        this.information = information;
        this.price = price;
        this.num_doors = num_doors;
        this.state = state;
        this.milage = milage;
        this.year = year;
    }

    public Cars() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNum_doors() {
        return num_doors;
    }

    public void setNum_doors(int num_doors) {
        this.num_doors = num_doors;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getMilage() {
        return milage;
    }

    public void setMilage(int milage) {
        this.milage = milage;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getReserveDate() {
        return ReserveDate;
    }

    public void setReserveDate(String reserveDate) {
        ReserveDate = reserveDate;
    }

    public int getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(int offerPrice) {
        this.offerPrice = offerPrice;
    }
}
