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
    private float lat;
    private float lon;
    private String prov;
    private int customId;

    public String getCity() {
        return city;
    }

    public String getId() {
        return id;
    }

    public String getCnty() {
        return cnty;
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }

    public String getProv() {
        return prov;
    }

    public int getCustomId() {
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

    public void setLat(float lat) {
        this.lat = lat;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public void setCustomId(int customId) {
        this.customId = customId;
    }
}

