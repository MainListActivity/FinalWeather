package cn.y.finalweather.model;

/**
 * =============================================
 * 版权：版权所有(C) 2016
 * 作者： y
 * 版本：1.0
 * 创建日期： 2016/8/1
 * 描述：{"code":"100","txt":"晴","txt_en":"Sunny/Clear","icon":"http://files.heweather.com/cond_icon/100.png"}
 * 修订历史：
 * =============================================
 */
public class Condition {
    private String code;
    private String txt;
    private String txt_en;
    private String icon;
    private String id;

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getIcon() {
        return icon;
    }

    public String getTxt() {
        return txt;
    }

    public String getTxt_en() {
        return txt_en;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTxt_en(String txt_en) {
        this.txt_en = txt_en;
    }
}
