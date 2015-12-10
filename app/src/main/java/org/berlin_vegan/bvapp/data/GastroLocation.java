package org.berlin_vegan.bvapp.data;

import org.berlin_vegan.bvapp.activities.LocationDetailActivity;

import java.io.Serializable;
import java.util.List;

/**
 * Holds all information about a gastro location. This is a direct mapping to a JSON entry. Additionally
 * the class has the member {@code distToCurLoc}, which save the distance to the current user location and
 * a {@link java.util.Comparator} for this member to sort the {@link android.support.v7.widget.RecyclerView} in
 * {@link LocationDetailActivity}.
 */
public class GastroLocation extends Location implements Serializable {
    final public static String TYPE_RESTAURANT = "Restaurant";
    public static final String TYPE_FAST_FOOD = "Imbiss";
    public static final String TYPE_ICE_CAFE = "Eiscafe";
    public static final String TYPE_CAFE = "Cafe";

    private String publicTransport;
    private Integer handicappedAccessible;
    private Integer handicappedAccessibleWc;
    private Integer dog;
    private Integer childChair;
    private Integer catering;
    private Integer delivery;
    private Integer organic;
    private Integer seatsOutdoor;
    private Integer seatsIndoor;
    private Integer wlan;
    private Integer glutenFree;
    private List<GastroLocationPicture> pictures;


    public String getDistrict() {
        if (district == null) {
            return "";
        }
        return district.trim();
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPublicTransport() {
        if (publicTransport == null) {
            return "";
        }
        return publicTransport.trim();
    }

    public void setPublicTransport(String publicTransport) {
        this.publicTransport = publicTransport;
    }

    public Integer getHandicappedAccessible() {
        return handicappedAccessible;
    }

    public void setHandicappedAccessible(Integer handicappedAccessible) {
        this.handicappedAccessible = handicappedAccessible;
    }

    public Integer getHandicappedAccessibleWc() {
        return handicappedAccessibleWc;
    }

    public void setHandicappedAccessibleWc(Integer handicappedAccessibleWc) {
        this.handicappedAccessibleWc = handicappedAccessibleWc;
    }

    public Integer getDog() {
        return dog;
    }

    public void setDog(Integer dog) {
        this.dog = dog;
    }

    public Integer getChildChair() {
        return childChair;
    }

    public void setChildChair(Integer childChair) {
        this.childChair = childChair;
    }

    public Integer getCatering() {
        return catering;
    }

    public void setCatering(Integer catering) {
        this.catering = catering;
    }

    public Integer getDelivery() {
        return delivery;
    }

    public void setDelivery(Integer delivery) {
        this.delivery = delivery;
    }

    public Integer getOrganic() {
        return organic;
    }

    public void setOrganic(Integer organic) {
        this.organic = organic;
    }

    public Integer getSeatsOutdoor() {
        return seatsOutdoor;
    }

    public void setSeatsOutdoor(Integer seatsOutdoor) {
        this.seatsOutdoor = seatsOutdoor;
    }

    public Integer getSeatsIndoor() {
        return seatsIndoor;
    }

    public void setSeatsIndoor(Integer seatsIndoor) {
        this.seatsIndoor = seatsIndoor;
    }

    public Integer getWlan() {
        return wlan;
    }

    public void setWlan(Integer wlan) {
        this.wlan = wlan;
    }

    public Integer getGlutenFree() {
        return glutenFree;
    }

    public void setGlutenFree(Integer glutenFree) {
        this.glutenFree = glutenFree;
    }

    public List<GastroLocationPicture> getPictures() {
        return pictures;
    }

    public void setPictures(List<GastroLocationPicture> pictures) {
        this.pictures = pictures;
    }

    public boolean isRestaurant() {
        return tags.contains(TYPE_RESTAURANT);
    }

    public boolean isFastFood() {
        return tags.contains(TYPE_FAST_FOOD);
    }

    public boolean isCafe() {
        return tags.contains(TYPE_CAFE);
    }

    public boolean isIceCafe() {
        return tags.contains(TYPE_ICE_CAFE);
    }

}

