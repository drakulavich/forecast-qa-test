package model;

import com.google.gson.annotations.SerializedName;

public class Forecast {

    private Long id;
    @SerializedName("weather_state_name")
    private String weatherState;
    @SerializedName("wind_speed")
    private Double windSpeed;
    @SerializedName("the_temp")
    private Double temperature;
    private Integer humidity;

    public Long getId() {
        return id;
    }

    public Forecast setId(Long id) {
        this.id = id;
        return this;
    }

    public String getWeatherState() {
        return weatherState;
    }

    public Forecast setWeatherState(String weatherState) {
        this.weatherState = weatherState;
        return this;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public Forecast setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
        return this;
    }

    public Double getTemperature() {
        return temperature;
    }

    public Forecast setTemperature(Double temperature) {
        this.temperature = temperature;
        return this;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public Forecast setHumidity(Integer humidity) {
        this.humidity = humidity;
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s\nTemp: %.1f °C\nWind: %.1f mph\nHumidity: %d%%",
                weatherState, temperature, windSpeed, humidity);
    }
}
