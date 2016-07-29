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
    private String cityName;
    private String cnty;
    private int id;
    private String cityCode;
    private float lat;
    private float lon;
    private String prov;

    public String getCityCode() {
        return cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCnty() {
        return cnty;
    }

    public int getId() {
        return id;
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

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCnty(String cnty) {
        this.cnty = cnty;
    }

    public void setId(int id) {
        this.id = id;
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
}

