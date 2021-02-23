package com.aarshinkov.mobile.hotelly.responses.hotels;

import com.aarshinkov.mobile.hotelly.responses.users.UserGetResponse;

import java.io.Serializable;
import java.sql.Timestamp;

public class HotelGetResponse implements Serializable {

    private String hotelId;
    private String name;
    private String description;
    private String countryCode;
    private String city;
    private String street;
    private String number;
    private Integer stars;
    private String mainImage;
    private UserGetResponse owner;
    private Timestamp createdOn;
    private Timestamp editedOn;

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public UserGetResponse getOwner() {
        return owner;
    }

    public void setOwner(UserGetResponse owner) {
        this.owner = owner;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public Timestamp getEditedOn() {
        return editedOn;
    }

    public void setEditedOn(Timestamp editedOn) {
        this.editedOn = editedOn;
    }
}
