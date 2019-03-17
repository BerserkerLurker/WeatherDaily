package com.onadasoft.weatherdaily.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CustomCurrent implements Parcelable {
    private String name;
    private String sysCountry;
    private long weatherId;
    private String weatherMain;
    private String weatherDescription;
    private double coordLon;
    private double coordLat;
    private double mainTemp;
    private double mainTempMin;
    private double mainTempMax;
    private long mainHumidity;
    private double windSpeed;
    private double windDeg;
    private long dt;


    public CustomCurrent(Current current) {
        this.name = current.getName();
        this.sysCountry = current.getSys().getCountry();
        this.weatherId = current.getWeather().get(0).getId();
        this.weatherMain = current.getWeather().get(0).getMain();
        this.weatherDescription = current.getWeather().get(0).getDescription();
        this.coordLon = current.getCoord().getLon();
        this.coordLat = current.getCoord().getLat();
        this.mainTemp = current.getMain().getTemp();
        this.mainTempMin = current.getMain().getTempMin();
        this.mainTempMax = current.getMain().getTempMax();
        this.mainHumidity = current.getMain().getHumidity();
        this.windSpeed = current.getWind().getSpeed();
        this.windDeg = current.getWind().getDeg();
        this.dt = current.getDt();
    }

    protected CustomCurrent(Parcel in) {
        name = in.readString();
        sysCountry = in.readString();
        weatherId = in.readLong();
        weatherMain = in.readString();
        weatherDescription = in.readString();
        coordLon = in.readDouble();
        coordLat = in.readDouble();
        mainTemp = in.readDouble();
        mainTempMin = in.readDouble();
        mainTempMax = in.readDouble();
        mainHumidity = in.readLong();
        windSpeed = in.readDouble();
        windDeg = in.readDouble();
        dt = in.readLong();
    }

    public static final Creator<CustomCurrent> CREATOR = new Creator<CustomCurrent>() {
        @Override
        public CustomCurrent createFromParcel(Parcel in) {
            return new CustomCurrent(in);
        }

        @Override
        public CustomCurrent[] newArray(int size) {
            return new CustomCurrent[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSysCountry() {
        return sysCountry;
    }

    public void setSysCountry(String sysCountry) {
        this.sysCountry = sysCountry;
    }

    public long getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(long weatherId) {
        this.weatherId = weatherId;
    }

    public String getWeatherMain() {
        return weatherMain;
    }

    public void setWeatherMain(String weatherMain) {
        this.weatherMain = weatherMain;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public double getCoordLon() {
        return coordLon;
    }

    public void setCoordLon(double coordLon) {
        this.coordLon = coordLon;
    }

    public double getCoordLat() {
        return coordLat;
    }

    public void setCoordLat(double coordLat) {
        this.coordLat = coordLat;
    }

    public double getMainTemp() {
        return mainTemp;
    }

    public void setMainTemp(double mainTemp) {
        this.mainTemp = mainTemp;
    }

    public double getMainTempMin() {
        return mainTempMin;
    }

    public void setMainTempMin(double mainTempMin) {
        this.mainTempMin = mainTempMin;
    }

    public double getMainTempMax() {
        return mainTempMax;
    }

    public void setMainTempMax(double mainTempMax) {
        this.mainTempMax = mainTempMax;
    }

    public long getMainHumidity() {
        return mainHumidity;
    }

    public void setMainHumidity(long mainHumidity) {
        this.mainHumidity = mainHumidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getWindDeg() {
        return windDeg;
    }

    public void setWindDeg(double windDeg) {
        this.windDeg = windDeg;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    @Override
    public String toString() {
        return "CustomCurrent{" +
                "name='" + name + '\'' +
                ", sysCountry='" + sysCountry + '\'' +
                ", weatherId=" + weatherId +
                ", weatherMain='" + weatherMain + '\'' +
                ", weatherDescription='" + weatherDescription + '\'' +
                ", coordLon=" + coordLon +
                ", coordLat=" + coordLat +
                ", mainTemp=" + mainTemp +
                ", mainTempMin=" + mainTempMin +
                ", mainTempMax=" + mainTempMax +
                ", mainHumidity=" + mainHumidity +
                ", windSpeed=" + windSpeed +
                ", windDeg=" + windDeg +
                ", dt=" + dt +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(sysCountry);
        parcel.writeLong(weatherId);
        parcel.writeString(weatherMain);
        parcel.writeString(weatherDescription);
        parcel.writeDouble(coordLon);
        parcel.writeDouble(coordLat);
        parcel.writeDouble(mainTemp);
        parcel.writeDouble(mainTempMin);
        parcel.writeDouble(mainTempMax);
        parcel.writeLong(mainHumidity);
        parcel.writeDouble(windSpeed);
        parcel.writeDouble(windDeg);
        parcel.writeLong(dt);
    }
}
