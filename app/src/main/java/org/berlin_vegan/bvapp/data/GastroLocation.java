package org.berlin_vegan.bvapp.data;

import org.berlin_vegan.bvapp.activities.MainListActivity;
import org.berlin_vegan.bvapp.helpers.DateUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Holds all information about a gastro location. This is a direct mapping to a JSON entry. Additionally
 * the class has the member {@code distToCurLoc}, which save the distance to the current user location and
 * a {@link java.util.Comparator} for this member to sort the {@link android.support.v7.widget.RecyclerView} in
 * {@link MainListActivity}.
 */
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
        if (id == null) {
            return "";
        }
        return id.trim();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        if (name == null) {
            return "";
        }
        return name.trim();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        if (street == null) {
            return "";
        }
        return street.trim();
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
        if (city == null) {
            return "";
        }
        return city.trim();
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        if (district == null) {
            return "";
        }
        return district.trim();
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
        if (publicTransport == null) {
            return "";
        }
        return publicTransport.trim();
    }

    public void setPublicTransport(String publicTransport) {
        this.publicTransport = publicTransport;
    }

    public String getTelephone() {
        if (telephone == null) {
            return "";
        }
        return telephone.trim();
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getWebsite() {
        if (website == null) {
            return "";
        }
        return website.trim();
    }

    public String getWebsiteWithProtocolPrefix() {
        final String http = "http://";
        final String https = "https://";
        if (!website.startsWith(http) || !website.startsWith(https)) {
            return http + website.trim();
        }
        return website.trim();
    }

    /**
     * Method to present a nicer formatted website string to a user in the view.
     *
     * @return formatted website string
     */
    public String getWebsiteFormatted() {
        if (website == null) {
            return "";
        }

        final String http = "http://";
        final String https = "https://";
        final char slash = '/';
        String out = website;

        out = out.replaceAll(http, "");
        out = out.replaceAll(https, "");

        final int lastCharacter = out.length() - 1;
        if (out.charAt(lastCharacter) == slash) {
            out = out.substring(0, lastCharacter);
        }

        return out.trim();
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getOtMon() {
        if (otMon == null) {
            return "";
        }
        return otMon.trim();
    }

    public void setOtMon(String otMon) {
        this.otMon = otMon;
    }

    public String getOtTue() {
        if (otTue == null) {
            return "";
        }
        return otTue.trim();
    }

    public void setOtTue(String otTue) {
        this.otTue = otTue;
    }

    public String getOtWed() {
        if (otWed == null) {
            return "";
        }
        return otWed.trim();
    }

    public void setOtWed(String otWed) {
        this.otWed = otWed;
    }

    public String getOtThu() {
        if (otThu == null) {
            return "";
        }
        return otThu.trim();
    }

    public void setOtThu(String otThu) {
        this.otThu = otThu;
    }

    public String getOtFri() {
        if (otFri == null) {
            return "";
        }
        return otFri.trim();
    }

    public void setOtFri(String otFri) {
        this.otFri = otFri;
    }

    public String getOtSat() {
        if (otSat == null) {
            return "";
        }
        return otSat.trim();
    }

    public void setOtSat(String otSat) {
        this.otSat = otSat;
    }

    public String getOtSun() {
        if (otSun == null) {
            return "";
        }
        return otSun.trim();
    }

    public void setOtSun(String otSun) {
        this.otSun = otSun;
    }

    public List<OpeningHoursInterval> getCondensedOpeningHours() {
        final ArrayList<OpeningHoursInterval> result = new ArrayList<>();
        String[] openingHours = getOpeningHoursAsArray();

        int equalIndex = -1;
        for (int day = 0; day <= 6; day++) {
            if (day < 6 && openingHours[day].equalsIgnoreCase(openingHours[day + 1])) {
                // successor has equal opening hours, so remember current day and continue
                if (equalIndex == -1) {
                    equalIndex = day;
                }
            } else {
                if (equalIndex == -1) {
                    // current day (opening hours) is unique, so create new entry
                    if (openingHours[day].isEmpty()) {
                        // closed
                        result.add(new OpeningHoursInterval(day, OpeningHoursInterval.CLOSED));
                    } else {
                        result.add(new OpeningHoursInterval(day, openingHours[day]));
                    }
                } else {
                    // there are consecutive days
                    String openTimesText = openingHours[day].isEmpty() ? OpeningHoursInterval.CLOSED : openingHours[day];
                    result.add(new OpeningHoursInterval(equalIndex, day, openTimesText));
                    equalIndex = -1;
                }
            }
        }
        return result;
    }

    public boolean isOpen(Date date) {
        final int sundayIndex = 6;
        final String[] openingHours = getOpeningHoursAsArray();

        final Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        final int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        boolean afterMidnight = false;
        if (currentHour >= 0 && currentHour <= 6) {
            afterMidnight = true;
        }
        int dayOfWeek = DateUtil.getDayOfWeek(date);
        if (afterMidnight) {
            dayOfWeek = dayOfWeek - 1; // its short after midnight, so we use the opening hour from the day before
            if (dayOfWeek == -1) {
                dayOfWeek = sundayIndex; // sunday
            }
        }
        if (DateUtil.isPublicHoliday(date)) { // it is a holiday so take the opening hours from sunday
            dayOfWeek = sundayIndex;
        }

        final OpeningHours hours = new OpeningHours(openingHours[dayOfWeek]);
        int currentMinute = DateUtil.inMinutes(currentHour, calendar.get(Calendar.MINUTE));
        if (afterMidnight) {
            currentMinute = currentMinute + 24 * 60; // add a complete day
        }
        return hours.isInRange(currentMinute);

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
        if (comment == null) {
            return "";
        }
        return comment.trim();
    }

    public String getCommentWithoutSoftHyphens() {
        // unfortunately soft hyphen (&shy;) is only partially working with fromHtml(): the word gets
        // split at the correct place, but the hyphen (dash) is not shown. this might be very annoying
        // to the user, because it just does not look right. as a workaround we do not split words at
        // all.
        // a web view solves the hyphen problem, but does not integrate into our current layout very well
        return getComment().replace("&shy;", "").trim();
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


    private String[] getOpeningHoursAsArray() {
        String[] openingHours = new String[7];
        openingHours[0] = getOtMon();
        openingHours[1] = getOtTue();
        openingHours[2] = getOtWed();
        openingHours[3] = getOtThu();
        openingHours[4] = getOtFri();
        openingHours[5] = getOtSat();
        openingHours[6] = getOtSun();
        return openingHours;
    }

}

