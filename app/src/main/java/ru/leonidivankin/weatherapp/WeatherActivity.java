package ru.leonidivankin.weatherapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherActivity extends AppCompatActivity {

	public static final String EXTRA_POS = "pos";
	public static final String SELECTION_CITY = "selectionCity";
	public static final int DELAY_MILLIS = 1000;

	private TextView name;
	private TextView temp;
	private TextView windSpeed;
	private TextView pressure;
	private TextView humidity;
	private ImageView weatherIcon;
	private String city;
	private String tempStr;
	private String windSpeedStr;
	private String pressureStr;
	private String humidityStr;
	private WeatherUpdateService weatherUpdateService;
	private boolean bound = false;

	private ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName componentName, IBinder binder) {
			WeatherUpdateService.WeatherUpdateBinder weatherUpdateBinder =
					(WeatherUpdateService.WeatherUpdateBinder) binder;
			weatherUpdateService = weatherUpdateBinder.getOdometer();
			bound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			bound = false;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);

		init();
		displayWeatherGson();
		Bundle bundle = getIntent().getExtras();

		if (bundle != null) {
			city = bundle.getString(EXTRA_POS);
			name = findViewById(R.id.name);
			name.setText(city);
			// Заполнить изображение услуги сервиса
			ImageView photo = findViewById(R.id.photo);
			photo.setImageResource(R.drawable.marker);
			photo.setContentDescription(city);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		Intent intent = new Intent(this, WeatherUpdateService.class);
		intent.putExtra(SELECTION_CITY, city);
		bindService(intent, connection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (bound) {
			unbindService(connection);
			bound = false;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}




	private void displayWeatherGson() {
		final Handler handler = new Handler();
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (bound && weatherUpdateService != null) {
					WeatherGson weatherGson = weatherUpdateService.getWeatherGson();
					if(weatherGson != null){
						renderWeather(weatherGson);
					}
				}
				handler.postDelayed(this, DELAY_MILLIS);
			}
		});
	}


	private void init() {
		temp = findViewById(R.id.temp_field);
		windSpeed = findViewById(R.id.wind_speed_field);
		pressure = findViewById(R.id.pressure_field);
		humidity = findViewById(R.id.humidity_field);
		weatherIcon = findViewById(R.id.weather_icon);
	}


	private void renderWeather(WeatherGson weatherGson) {
		tempStr = "температурка " + weatherGson.getTemp() + " °С";
		windSpeedStr = "ветерок " + weatherGson.getWindSpeed() + " м/с";
		pressureStr = "давленице " + weatherGson.getPressure() + " мм.рт.ст.";
		humidityStr = "влажность " + weatherGson.getHumidity() + " %";

		temp.setText(tempStr);
		windSpeed.setText(windSpeedStr);
		pressure.setText(pressureStr);
		humidity.setText(humidityStr);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_weather_activity, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.menu_share:
				shareWeather();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}

	}

	private void shareWeather() {
		String msg = city + "\n" + tempStr + "\n" + windSpeedStr + "\n" + pressureStr + "\n" + humidityStr;
		//неявный интент
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, msg);
		startActivity(intent);
	}

}
