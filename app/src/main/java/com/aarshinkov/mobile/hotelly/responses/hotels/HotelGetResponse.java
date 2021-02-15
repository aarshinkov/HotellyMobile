package com.aarshinkov.mobile.hotelly.responses.hotels;

import com.aarshinkov.mobile.hotelly.responses.users.UserGetResponse;

import java.io.Serializable;
import java.sql.Timestamp;

public class HotelGetResponse implements Serializable {

    private String hotelId;
    private String name;
    private AddressGetResponse address;
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

    public AddressGetResponse getAddress() {
        return address;
    }

    public void setAddress(AddressGetResponse address) {
        this.address = address;
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
