package cn.y.finalweather.model;

/**
 * 版权：版权所有(c) 2016
 * 作者：y
 * 版本：1.0
 * 创建日期：2016/7/29
 * 描述：
 * 修订历史：
 */
public class City {
    private String city;
    private String cnty;
    private String id;
    private String lat;
    private String lon;
    private String prov;
    private String customId;

    public String getCity() {
        return city;
    }

    public String getId() {
        return id;
    }

    public String getCnty() {
        return cnty;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getProv() {
        return prov;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCnty(String cnty) {
        this.cnty = cnty;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }
}

