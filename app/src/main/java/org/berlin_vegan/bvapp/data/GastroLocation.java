package org.berlin_vegan.bvapp.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GastroLocation implements Comparable<GastroLocation>, Serializable {
    private String id;
    private String name;
    private String street;
    private Integer cityCode;
    private String city;
    private String district;
    private Double latCoord;
    private Double longCoord;
    private Float distToCurLoc = -1.0f;
    private String publicTransport;
    private String telephone;
    private String website;
    private String otMon;
    private String otTue;
    private String otWed;
    private String otThu;
    private String otFri;
    private String otSat;
    private String otSun;
    private Integer vegan;
    private Integer handicappedAccessible;
    private Integer handicappedAccessibleWc;
    private Integer dog;
    private Integer childChair;
    private Integer catering;
    private Integer delivery;
    private Integer organic;
    private Integer seatsOutdoor;
    private Integer seatsIndoor;
    private String comment;
    private Integer wlan;
    private Integer glutenFree;
    private List<String> tags = new ArrayList<>();
    private List<GastroLocationPicture> pictures;


    // getter & setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getCityCode() {
        return cityCode;
    }

    public void setCityCode(Integer cityCode) {
        this.cityCode = cityCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Double getLatCoord() {
        return latCoord;
    }

    public void setLatCoord(Double latCoord) {
        this.latCoord = latCoord;
    }

    public Double getLongCoord() {
        return longCoord;
    }

    public void setLongCoord(Double longCoord) {
        this.longCoord = longCoord;
    }

    public Float getDistToCurLoc() {
        return distToCurLoc;
    }

    public void setDistToCurLoc(Float distToCurLoc) {
        this.distToCurLoc = distToCurLoc;
    }

    public String getPublicTransport() {
        return publicTransport;
    }

    public void setPublicTransport(String publicTransport) {
        this.publicTransport = publicTransport;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getOtMon() {
        return otMon;
    }

    public void setOtMon(String otMon) {
        this.otMon = otMon;
    }

    public String getOtTue() {
        return otTue;
    }

    public void setOtTue(String otTue) {
        this.otTue = otTue;
    }

    public String getOtWed() {
        return otWed;
    }

    public void setOtWed(String otWed) {
        this.otWed = otWed;
    }

    public String getOtThu() {
        return otThu;
    }

    public void setOtThu(String otThu) {
        this.otThu = otThu;
    }

    public String getOtFri() {
        return otFri;
    }

    public void setOtFri(String otFri) {
        this.otFri = otFri;
    }

    public String getOtSat() {
        return otSat;
    }

    public void setOtSat(String otSat) {
        this.otSat = otSat;
    }

    public String getOtSun() {
        return otSun;
    }

    public void setOtSun(String otSun) {
        this.otSun = otSun;
    }

    public Integer getVegan() {
        return vegan;
    }

    public void setVegan(Integer vegan) {
        this.vegan = vegan;
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

    private String getComment() {
        return comment;
    }

    public String getCommentWithoutSoftHyphens() {
        // unfortunately soft hyphen (&shy;) is only partially working with fromHtml(): the word gets
        // split at the correct place, but the hyphen (dash) is not shown. this might be very annoying
        // to the user, because it just does not look right. as a workaround we do not split words at
        // all.
        // a web view solves the hyphen problem, but does not integrate into our current layout very well
        return getComment().replace("&shy;", "");
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<GastroLocationPicture> getPictures() {
        return pictures;
    }

    public void setPictures(List<GastroLocationPicture> pictures) {
        this.pictures = pictures;
    }
    // --------------------------------------------------------------------
    // implement comparable interface

    // idea for equals(...) and hashCode() are taken from:
    // http://java67.blogspot.de/2013/04/example-of-overriding-equals-hashcode-compareTo-java-method.html
    //
    // note: not all member variables are taken into account for calculation. adapt, if needed!
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || (getClass() != object.getClass())) {
            return false;
        }
        GastroLocation other = (GastroLocation) object;
        return (id != null && id.equals(other.id)) &&
                (name != null && name.equals(other.name)) &&
                (street != null && street.equals(other.street)) &&
                (cityCode != null && cityCode.equals(other.cityCode)) &&
                (latCoord != null && latCoord.equals(other.latCoord)) &&
                (longCoord != null && longCoord.equals(other.longCoord));
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (cityCode != null ? cityCode.hashCode() : 0);
        result = 31 * result + (latCoord != null ? latCoord.hashCode() : 0);
        result = 31 * result + (longCoord != null ? longCoord.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(GastroLocation other) {
        if (getDistToCurLoc() == null && other.getDistToCurLoc() == null) {
            return 0;
        }
        if (getDistToCurLoc() == null) {
            return 1;
        }
        if (other.getDistToCurLoc() == null) {
            return -1;
        }
        return getDistToCurLoc().compareTo(other.getDistToCurLoc());
    }

}

