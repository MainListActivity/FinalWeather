package cn.y.finalweather.model;

import java.util.List;

/**
 * =============================================
 * 版权：版权所有(C) 2016
 * 作者： y
 * 版本：1.0
 * 创建日期： 2016/8/1
 * 描述：
 * 修订历史：
 * =============================================
 */
public class ConditionInfo {
    private List<Condition> cond_info;
    private String status;

    public List<Condition> getCond_info() {
        return cond_info;
    }

    public String getStatus() {
        return status;
    }

    public void setCond_info(List<Condition> cond_info) {
        this.cond_info = cond_info;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
