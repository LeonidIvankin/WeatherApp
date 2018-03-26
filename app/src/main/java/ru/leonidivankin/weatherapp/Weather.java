package ru.leonidivankin.weatherapp;

class Weather {

	static final Weather[] weather = {
			new Weather("Санкт-Петербург",
					"Saint Petersburg",
					R.drawable.spb),
			new Weather("Москва",
					"Moscow",
					R.drawable.moskow),
			new Weather("Минск",
					"Minsk",
					R.drawable.minsk)};
	private String name;
	private String description;
	private int imageResourceId;

	// Для каждой услуги сервиса хранится название, описание и ресурс изображения
	private Weather(String name, String description, int imageResourceId) {
		this.name = name;
		this.description = description;
		this.imageResourceId = imageResourceId;
	}

	String getName() {
		return name;
	}

	String getCityISO() {
		return description;
	}

	int getImageResourceId() {
		return imageResourceId;
	}
}