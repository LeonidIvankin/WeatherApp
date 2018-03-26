package ru.leonidivankin.weatherapp;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Вспомогательный класс для работы с API openweathermap
 * и скачивания нужных данных
 */

public class WeatherDataLoader {
	private static final String APP_ID = "3eaae351d9076e90dd3406857debb701";
	//первое s - какой-то город, второе - ключ приложения
	private static final String OPEN_WEATHER_MAP_API = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";
	private static final String KEY = "x-api-key";
	private static final String RESPONSE = "cod";
	private static final String NEW_LINE = "\n";
	private static final int ALL_GOOD = 200;

	//Единственный метод класса, который делает запрос на сервер и получает от него данные
	//возвращает объект JSON или null

	static JSONObject getJSONData(String city) {
		try {
			//Используем API openweathermap
			URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city, APP_ID));
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();//создаем http соединение

			//в BufferedReader принимаем поток с сайта
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder rawData = new StringBuilder(1024);
			String tempVarible;

			//читаем построчно сайт. Если не равна null добавляем в StringBuilder
			while ((tempVarible = reader.readLine()) != null) {
				rawData.append(tempVarible).append(NEW_LINE);
			}
			reader.close();

			//JSON файл в виде строки получаем rawData

			JSONObject jsonObject = new JSONObject(rawData.toString());

			if (jsonObject.getInt(RESPONSE) != ALL_GOOD) {
				return null;
			}
			return jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
			return null;//FIXME Обработка ошибки
		}
	}
}
