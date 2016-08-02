package cn.y.finalweather.model;

import java.util.ArrayList;
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
public class HeWeather {
    private Basic basic;
    private List<DailyForecast> daily_forecast;
    private List<HourlyForecast> hourly_forecast;
    private Now now;
    private String status;
    private Suggestion suggestion;


    public String getStatus() {
        return status;
    }

    public Basic getBasic() {
        if (basic == null)
            return new Basic();
        else
            return basic;
    }

    public List<DailyForecast> getDaily_forecast() {
        return daily_forecast == null ? new ArrayList<DailyForecast>(10) : daily_forecast;
    }

    public DailyForecast getDailyForecast(){
        return new DailyForecast();
    }
    public HourlyForecast getHourlyForecast(){
        return new HourlyForecast();
    }

    public List<HourlyForecast> getHourly_forecast() {
        return hourly_forecast == null ? new ArrayList<HourlyForecast>(10) : hourly_forecast;
    }

    public Now getNow() {
        if (now == null)
            return new Now();
        else
            return now;
    }

    public Suggestion getSuggestion() {
        if (suggestion == null)
            return new Suggestion();
        else
            return suggestion;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public void setDaily_forecast(List<DailyForecast> daily_forecast) {
        this.daily_forecast = daily_forecast;
    }

    public void setHourly_forecast(List<HourlyForecast> hourly_forecast) {
        this.hourly_forecast = hourly_forecast;
    }

    public void setNow(Now now) {
        this.now = now;
    }

    public void setSuggestion(Suggestion suggestion) {
        this.suggestion = suggestion;
    }

    public class Basic {
        private String city;
        private String cnty;
        private String id;
        private String lat;
        private String lon;
        private Update update;

        public String getLat() {
            return lat;
        }

        public String getLon() {
            return lon;
        }

        public String getCity() {
            return city;
        }

        public String getCnty() {
            return cnty;
        }

        public String getId() {
            return id;
        }

        public Update getUpdate() {
            if (update == null)
                return new Update();
            else
                return update;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setCnty(String cnty) {
            this.cnty = cnty;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public void setUpdate(Update update) {
            this.update = update;
        }


        public class Update {
            private String loc;
            private String utc;

            public String getLoc() {
                return loc;
            }

            public String getUtc() {
                return utc;
            }

            public void setLoc(String loc) {
                this.loc = loc;
            }

            public void setUtc(String utc) {
                this.utc = utc;
            }
        }
    }

    public class DailyForecast {
        private Astro astro;
        private Cond cond;
        private String date;
        private String hum;
        private String pcpn;
        private String pop;
        private String pres;
        private Tmp tmp;
        private String vis;
        private Wind wind;

        public Astro getAstro() {

            return astro == null ? new Astro() : astro;
        }

        public Cond getCond() {
            return cond == null ? new Cond() : cond;
        }

        public String getPcpn() {
            return pcpn;
        }

        public String getHum() {
            return hum;
        }

        public String getPop() {
            return pop;
        }

        public String getPres() {
            return pres;
        }

        public String getVis() {
            return vis;
        }

        public String getDate() {
            return date;
        }

        public Tmp getTmp() {
            return tmp == null ? new Tmp() : tmp;
        }

        public Wind getWind() {
            return wind == null ? new Wind() : wind;
        }

        public void setAstro(Astro astro) {
            this.astro = astro;
        }

        public void setCond(Cond cond) {
            this.cond = cond;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setHum(String hum) {
            this.hum = hum;
        }

        public void setPcpn(String pcpn) {
            this.pcpn = pcpn;
        }

        public void setPop(String pop) {
            this.pop = pop;
        }

        public void setPres(String pres) {
            this.pres = pres;
        }

        public void setTmp(Tmp tmp) {
            this.tmp = tmp;
        }

        public void setVis(String vis) {
            this.vis = vis;
        }

        public void setWind(Wind wind) {
            this.wind = wind;
        }


        public class Astro {
            private String sr;
            private String ss;

            public String getSr() {
                return sr;
            }

            public String getSs() {
                return ss;
            }

            public void setSr(String sr) {
                this.sr = sr;
            }

            public void setSs(String ss) {
                this.ss = ss;
            }
        }

        public class Cond {
            private String code_d;
            private String code_n;
            private String txt_d;
            private String txt_n;

            public String getCode_d() {
                return code_d;
            }

            public String getCode_n() {
                return code_n;
            }

            public String getTxt_d() {
                return txt_d;
            }

            public String getTxt_n() {
                return txt_n;
            }

            public void setCode_d(String code_d) {
                this.code_d = code_d;
            }

            public void setCode_n(String code_n) {
                this.code_n = code_n;
            }

            public void setTxt_d(String txt_d) {
                this.txt_d = txt_d;
            }

            public void setTxt_n(String txt_n) {
                this.txt_n = txt_n;
            }
        }

        public class Tmp {
            private String max;
            private String min;

            public String getMax() {
                return max;
            }

            public String getMin() {
                return min;
            }

            public void setMax(String max) {
                this.max = max;
            }

            public void setMin(String min) {
                this.min = min;
            }
        }

        public class Wind {
            private String deg;
            private String dir;
            private String sc;
            private String spd;

            public String getDeg() {
                return deg;
            }

            public String getSpd() {
                return spd;
            }

            public String getDir() {
                return dir;
            }

            public String getSc() {
                return sc;
            }

            public void setDeg(String deg) {
                this.deg = deg;
            }

            public void setDir(String dir) {
                this.dir = dir;
            }

            public void setSpd(String spd) {
                this.spd = spd;
            }

            public void setSc(String sc) {
                this.sc = sc;
            }
        }

    }

    public class HourlyForecast {
        private String date;
        private String hum;
        private String pop;
        private String pres;
        private String tmp;
        private DailyForecast.Wind wind;

        public String getHum() {
            return hum;
        }

        public String getPop() {
            return pop;
        }

        public String getPres() {
            return pres;
        }

        public String getTmp() {
            return tmp;
        }

        public String getDate() {
            return date;
        }

        public DailyForecast.Wind getWind() {
            return wind == null ? new DailyForecast().getWind() : wind;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setHum(String hum) {
            this.hum = hum;
        }

        public void setPop(String pop) {
            this.pop = pop;
        }

        public void setPres(String pres) {
            this.pres = pres;
        }

        public void setTmp(String tmp) {
            this.tmp = tmp;
        }

        public void setWind(DailyForecast.Wind wind) {
            this.wind = wind;
        }

    }

    public class Now {
        private Cond cond;
        private String fl;
        private String hum;
        private String pcpn;
        private String pres;
        private String tmp;
        private DailyForecast.Wind wind;
        private String vis;

        public DailyForecast.Wind getWind() {
            if (wind == null)
                return new DailyForecast().getWind();
            else
                return wind;
        }

        public String getTmp() {
            return tmp;
        }

        public String getPres() {
            return pres;
        }

        public Cond getCond() {
            if (cond == null)
                return new Cond();
            else
                return cond;
        }

        public String getFl() {
            return fl;
        }

        public String getHum() {
            return hum;
        }

        public String getPcpn() {
            return pcpn;
        }

        public String getVis() {
            return vis;
        }

        public void setWind(DailyForecast.Wind wind) {
            this.wind = wind;
        }

        public void setTmp(String tmp) {
            this.tmp = tmp;
        }

        public void setPres(String pres) {
            this.pres = pres;
        }

        public void setCond(Cond cond) {
            this.cond = cond;
        }

        public void setFl(String fl) {
            this.fl = fl;
        }

        public void setHum(String hum) {
            this.hum = hum;
        }

        public void setPcpn(String pcpn) {
            this.pcpn = pcpn;
        }

        public void setVis(String vis) {
            this.vis = vis;
        }

        public class Cond {
            private String code;
            private String txt;

            public String getCode() {
                return code;
            }

            public String getTxt() {
                return txt;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }
    }

    public class Suggestion {
        private Comf comf;
        private Cw cw;
        private Drsg drsg;
        private Flu flu;
        private Sport sport;
        private Trav trav;
        private Uv uv;

        public Comf getComf() {

            return comf == null ? new Comf() : comf;
        }

        public Cw getCw() {
            return cw == null ? new Cw() : cw;
        }

        public Drsg getDrsg() {
            return drsg == null ? new Drsg() : drsg;
        }

        public Flu getFlu() {
            return flu == null ? new Flu() : flu;
        }

        public Sport getSport() {
            return sport == null ? new Sport() : sport;
        }

        public Trav getTrav() {
            return trav == null ? new Trav() : trav;
        }

        public Uv getUv() {
            return uv == null ? new Uv() : uv;
        }

        public void setComf(Comf comf) {
            this.comf = comf;
        }

        public void setCw(Cw cw) {
            this.cw = cw;
        }

        public void setDrsg(Drsg drsg) {
            this.drsg = drsg;
        }

        public void setFlu(Flu flu) {
            this.flu = flu;
        }

        public void setSport(Sport sport) {
            this.sport = sport;
        }

        public void setTrav(Trav trav) {
            this.trav = trav;
        }

        public void setUv(Uv uv) {
            this.uv = uv;
        }

        public class Comf {
            private String brf;
            private String txt;

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }

            public String getBrf() {
                return brf;
            }

            public String getTxt() {
                return txt;
            }
        }

        public class Cw {
            private String brf;
            private String txt;

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }

            public String getBrf() {
                return brf;
            }

            public String getTxt() {
                return txt;
            }
        }

        public class Drsg {
            private String brf;
            private String txt;

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }

            public String getBrf() {
                return brf;
            }

            public String getTxt() {
                return txt;
            }
        }

        public class Flu {
            private String brf;
            private String txt;

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }

            public String getBrf() {
                return brf;
            }

            public String getTxt() {
                return txt;
            }
        }

        public class Sport {
            private String brf;
            private String txt;

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }

            public String getBrf() {
                return brf;
            }

            public String getTxt() {
                return txt;
            }
        }

        public class Trav {
            private String brf;
            private String txt;

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }

            public String getBrf() {
                return brf;
            }

            public String getTxt() {
                return txt;
            }
        }

        public class Uv {
            private String brf;
            private String txt;

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }

            public String getBrf() {
                return brf;
            }

            public String getTxt() {
                return txt;
            }
        }
    }

}
