package cn.y.finalweather.model;

import java.util.List;

/**
 * =============================================
 * 版权：版权所有(C) 2016
 * 作者： y
 * 版本：1.0
 * 创建日期： 2016/7/30
 * 描述：
 * 修订历史：
 * =============================================
 */
public class CityInfo {
    private List<City> city_info;
    private String status;

    public List<City> getCity_info() {
        return city_info;
    }

    public String getStatus() {
        return status;
    }

    public void setCity_info(List<City> city_info) {
        this.city_info = city_info;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
