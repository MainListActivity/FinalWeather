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
public class HeWeather {
    private Basic basic;
    private List<DailyForecast> daily_forecast;
    private List<HourlyForecast>  hourly_forecast;
    private Now now;
    private String status;
    private Suggestion suggestion;


    public String getStatus() {
        return status;
    }

    public Basic getBasic() {
        return basic;
    }

    public List<DailyForecast> getDaily_forecast() {
        return daily_forecast;
    }

    public List<HourlyForecast> getHourly_forecast() {
        return hourly_forecast;
    }

    public Now getNow() {
        return now;
    }

    public Suggestion getSuggestion() {
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

    public class Basic{
        private String city;
        private String cnty;
        private String id;
        private float lat;
        private float lon;
        private Update update;

        public float getLat() {
            return lat;
        }

        public float getLon() {
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

        public void setLat(float lat) {
            this.lat = lat;
        }

        public void setLon(float lon) {
            this.lon = lon;
        }

        public void setUpdate(Update update) {
            this.update = update;
        }


        public class Update{
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
    public class DailyForecast{
        private Astro astro;
        private Cond cond;
        private String date;
        private int hum;
        private double pcpn;
        private int pop;
        private int pres;
        private Tmp tmp;
        private int vis;
        private Wind wind;

        public Astro getAstro() {
            return astro;
        }

        public Cond getCond() {
            return cond;
        }

        public double getPcpn() {
            return pcpn;
        }

        public int getHum() {
            return hum;
        }

        public int getPop() {
            return pop;
        }

        public int getPres() {
            return pres;
        }

        public int getVis() {
            return vis;
        }

        public String getDate() {
            return date;
        }

        public Tmp getTmp() {
            return tmp;
        }

        public Wind getWind() {
            return wind;
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

        public void setHum(int hum) {
            this.hum = hum;
        }

        public void setPcpn(double pcpn) {
            this.pcpn = pcpn;
        }

        public void setPop(int pop) {
            this.pop = pop;
        }

        public void setPres(int pres) {
            this.pres = pres;
        }

        public void setTmp(Tmp tmp) {
            this.tmp = tmp;
        }

        public void setVis(int vis) {
            this.vis = vis;
        }

        public void setWind(Wind wind) {
            this.wind = wind;
        }


        public class Astro{
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
        public class Cond{
            private int code_d;
            private int code_n;
            private String txt_d;
            private String txt_n;

            public int getCode_d() {
                return code_d;
            }

            public int getCode_n() {
                return code_n;
            }

            public String getTxt_d() {
                return txt_d;
            }

            public String getTxt_n() {
                return txt_n;
            }

            public void setCode_d(int code_d) {
                this.code_d = code_d;
            }

            public void setCode_n(int code_n) {
                this.code_n = code_n;
            }

            public void setTxt_d(String txt_d) {
                this.txt_d = txt_d;
            }

            public void setTxt_n(String txt_n) {
                this.txt_n = txt_n;
            }
        }
        public class Tmp{
            private int max;
            private int min;

            public int getMax() {
                return max;
            }

            public int getMin() {
                return min;
            }

            public void setMax(int max) {
                this.max = max;
            }

            public void setMin(int min) {
                this.min = min;
            }
        }
        private class Wind{
            private int deg;
            private String dir;
            private String sr;
            private int spd;

            public int getDeg() {
                return deg;
            }

            public int getSpd() {
                return spd;
            }

            public String getDir() {
                return dir;
            }

            public String getSr() {
                return sr;
            }

            public void setDeg(int deg) {
                this.deg = deg;
            }

            public void setDir(String dir) {
                this.dir = dir;
            }

            public void setSpd(int spd) {
                this.spd = spd;
            }

            public void setSr(String sr) {
                this.sr = sr;
            }
        }

    }
    public class HourlyForecast{
        private String date;
        private int hum;
        private int pop;
        private int pres;
        private int tmp;
        private DailyForecast.Wind wind;

        public int getHum() {
            return hum;
        }

        public int getPop() {
            return pop;
        }

        public int getPres() {
            return pres;
        }

        public int getTmp() {
            return tmp;
        }

        public String getDate() {
            return date;
        }

        public DailyForecast.Wind getWind() {
            return wind;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setHum(int hum) {
            this.hum = hum;
        }

        public void setPop(int pop) {
            this.pop = pop;
        }

        public void setPres(int pres) {
            this.pres = pres;
        }

        public void setTmp(int tmp) {
            this.tmp = tmp;
        }

        public void setWind(DailyForecast.Wind wind) {
            this.wind = wind;
        }

    }
    public class Now{
        private Cond cond;
        private int fl;
        private int hum;
        private int pcpn;
        private int pres;
        private int tmp;
        private DailyForecast.Wind wind;
        private int vis;

        public DailyForecast.Wind getWind() {
            return wind;
        }

        public int getTmp() {
            return tmp;
        }

        public int getPres() {
            return pres;
        }

        public Cond getCond() {
            return cond;
        }

        public int getFl() {
            return fl;
        }

        public int getHum() {
            return hum;
        }

        public int getPcpn() {
            return pcpn;
        }

        public int getVis() {
            return vis;
        }

        public void setWind(DailyForecast.Wind wind) {
            this.wind = wind;
        }

        public void setTmp(int tmp) {
            this.tmp = tmp;
        }

        public void setPres(int pres) {
            this.pres = pres;
        }

        public void setCond(Cond cond) {
            this.cond = cond;
        }

        public void setFl(int fl) {
            this.fl = fl;
        }

        public void setHum(int hum) {
            this.hum = hum;
        }

        public void setPcpn(int pcpn) {
            this.pcpn = pcpn;
        }

        public void setVis(int vis) {
            this.vis = vis;
        }

        public class Cond{
            private int code;
            private String txt;

            public int getCode() {
                return code;
            }

            public String getTxt() {
                return txt;
            }

            public void setCode(int code) {
                this.code = code;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }
    }
    public class Suggestion{
        private Comf comf;
        private Cw cw;
        private Drsg drsg;
        private Flu flu;
        private Sport sport;
        private Trav trav;
        private Uv uv;

        public Comf getComf() {
            return comf;
        }

        public Cw getCw() {
            return cw;
        }

        public Drsg getDrsg() {
            return drsg;
        }

        public Flu getFlu() {
            return flu;
        }

        public Sport getSport() {
            return sport;
        }

        public Trav getTrav() {
            return trav;
        }

        public Uv getUv() {
            return uv;
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

        public class Comf{
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
        public class Cw{
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
        public class Drsg{
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
        public class Flu{
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
        public class Sport{
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
        public class Trav{
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
        public class Uv{
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
