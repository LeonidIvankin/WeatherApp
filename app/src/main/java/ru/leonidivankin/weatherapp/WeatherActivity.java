package ru.leonidivankin.weatherapp;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

public class WeatherActivity extends AppCompatActivity {

	public static final String EXTRA_WEATHERNOM = "weatherNom";

	//handler - это класс, позволяющий отправлять и обрабатывать сообщения и объекты runnable. Он используется в двух
	//случаях: когда нужно применить объект runnable в будущем и когда необходимо передать другому потоку
	//выполнение какого-то метода. Второй случай наш
	private final Handler handler = new Handler();

	private TextView temp;
	private TextView windSpeed;
	private TextView pressure;
	private TextView humidity;
	private ImageView weatherIcon;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		// Получить вид услуги сервиса
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			int nailNom = bundle.getInt(EXTRA_WEATHERNOM);
			Weather weather = Weather.weather[nailNom];

			// Заполнить иpображение услуги сервиса
			ImageView photo = findViewById(R.id.photo);
			photo.setImageResource(weather.getImageResourceId());
			photo.setContentDescription(weather.getName());

			// Заполнение наименования услуги сервиса
			TextView name = findViewById(R.id.name);
			name.setText(weather.getName());


			temp = findViewById(R.id.temp_field);
			windSpeed = findViewById(R.id.wind_speed_field);
			pressure = findViewById(R.id.pressure_field);
			humidity = findViewById(R.id.humidity_field);
			weatherIcon = findViewById(R.id.weather_icon);


			updateWeatherData(weather.getCityISO());
		}
	}

	//Обновление/загрузка погодных данных
	private void updateWeatherData(final String city) {
		//создание отдельного потока
		new Thread() {
			public void run() {
				//получаем объект json
				final JSONObject json = WeatherDataLoader.getJSONData(city);
				// Вызов методов напрямую может вызвать runtime error
				// Мы не можем напрямую обновить UI, поэтому используем handler, чтобы обновить интерфейс в главном потоке.
				if (json == null) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getApplicationContext(), R.string.place_not_found, Toast.LENGTH_SHORT).show();
						}
					});
				} else {
					//если объект не null, нужно его распарсить
					final WeatherGson weatherGson = new Gson().fromJson(json.toString(), WeatherGson.class);
					handler.post(new Runnable() {
						@Override
						public void run() {
							renderWeather(weatherGson);
						}
					});
				}
			}
		}.start();
	}

	private void renderWeather(WeatherGson weatherGson) {

		temp.setText("температурка " + weatherGson.getTemp() + " °С");
		windSpeed.setText("ветерок " + weatherGson.getWindSpeed() + " м/с");
		pressure.setText("давленице " + weatherGson.getPressure() + " мм.рт.ст.");
		humidity.setText("влажность " + weatherGson.getHumidity() + " %");
		setWeatherIcon(weatherGson.getIcon());

	}

	// Подстановка нужной иконки
	// Парсим коды http://openweathermap.org/weather-conditions
	private void setWeatherIcon(String iconCode) {
		Drawable drawable;
		switch (iconCode) {
			case "01d":
				drawable = getResources().getDrawable(R.drawable.icon_01d);
				break;
			case "01n":
				drawable = getResources().getDrawable(R.drawable.icon_01n);
				break;
			case "02d":
				drawable = getResources().getDrawable(R.drawable.icon_02d);
				break;
			case "02n":
				drawable = getResources().getDrawable(R.drawable.icon_02n);
				break;
			case "03d":
				drawable = getResources().getDrawable(R.drawable.icon_03d);
				break;
			case "03n":
				drawable = getResources().getDrawable(R.drawable.icon_03n);
				break;
			case "04d":
				drawable = getResources().getDrawable(R.drawable.icon_04d);
				break;
			case "04n":
				drawable = getResources().getDrawable(R.drawable.icon_04n);
				break;
			case "09d":
				drawable = getResources().getDrawable(R.drawable.icon_09d);
				break;
			case "09n":
				drawable = getResources().getDrawable(R.drawable.icon_09n);
				break;
			case "10d":
				drawable = getResources().getDrawable(R.drawable.icon_10d);
				break;
			case "10n":
				drawable = getResources().getDrawable(R.drawable.icon_10n);
				break;
			case "11d":
				drawable = getResources().getDrawable(R.drawable.icon_11d);
				break;
			case "11n":
				drawable = getResources().getDrawable(R.drawable.icon_11n);
				break;
			case "13d":
				drawable = getResources().getDrawable(R.drawable.icon_13d);
				break;
			case "13n":
				drawable = getResources().getDrawable(R.drawable.icon_13n);
				break;
			case "50d":
				drawable = getResources().getDrawable(R.drawable.icon_50d);
				break;
			case "50n":
				drawable = getResources().getDrawable(R.drawable.icon_50n);
				break;
			default:
				drawable = getResources().getDrawable(R.drawable.icon_01d);
				break;
		}
		weatherIcon.setImageDrawable(drawable);
	}
}
