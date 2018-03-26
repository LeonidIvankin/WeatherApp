package ru.leonidivankin.weatherapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherGson {

	@SerializedName("cod")
	private Integer code;

	@SerializedName("name")
	private String name;

	private Integer id;


	@SerializedName("sys")
	private SystemData systemData;

	@SerializedName("dt")
	private Integer dt;

	@SerializedName("clouds")
	private Clouds clouds;

	@SerializedName("wind")
	private Wind wind;

	@SerializedName("visibility")
	private transient Integer visibility;

	@SerializedName("main")
	private Main main;

	@SerializedName("base")
	private String base;

	@SerializedName("weather")
	private List<WeatherItem> weather;

	@SerializedName("coord")
	private Coord coord;

	public Integer getId() {
		return weather.get(0).id;
	}

	public String getCity() {
		return name;
	}

	public String getIcon() {
		if (weather.size() > 0) {
			return weather.get(0).icon;
		} else {
			return "";
		}
	}

	public String getTemp() {
		return String.valueOf(main.temp - 273.15);
	}

	public String getPressure() {
		return String.valueOf(main.pressure);
	}

	public String getWindSpeed() {
		return wind.speed.toString();
	}


	public String getHumidity() {
		return main.humidity.toString();
	}

	class SystemData {//

		@SerializedName("type")
		Integer type;
		@SerializedName("id")
		Integer id;
		@SerializedName("message")
		Double message;
		@SerializedName("country")
		String country;
		@SerializedName("sunrise")
		Integer sunrise;
		@SerializedName("sunset")
		Integer sunset;
	}

	class Clouds {//
		@SerializedName("all")
		Integer all;
	}

	private class Wind {//

		@SerializedName("speed")
		Double speed;

	}

	private class Main {//
		@SerializedName("temp")
		Double temp;
		@SerializedName("pressure")
		Integer pressure;
		@SerializedName("humidity")
		Integer humidity;
		@SerializedName("temp_min")
		Double temp_min;
		@SerializedName("temp_max")
		Double temp_max;
	}

	private class WeatherItem {
		@SerializedName("id")
		Integer id;
		@SerializedName("main")
		String main;
		@SerializedName("description")
		String description;
		@SerializedName("icon")
		String icon;
	}

	private class Coord {
		@SerializedName("lon")
		Double lon;
		@SerializedName("lat")
		Double lat;
	}

}

