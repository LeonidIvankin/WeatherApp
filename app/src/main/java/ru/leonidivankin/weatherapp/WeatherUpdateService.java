package ru.leonidivankin.weatherapp;

import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

public class WeatherUpdateService extends Service {

	public static final int TIME_OUT_SERVICE = 10_000;

	private final IBinder binder = new WeatherUpdateBinder();
	private final Handler handler = new Handler();

	private String city;
	private WeatherGson weatherGson;
	//handler - это класс, позволяющий отправлять и обрабатывать сообщения и объекты runnable. Он используется в двух
	//случаях: когда нужно применить объект runnable в будущем и когда необходимо передать другому потоку
	//выполнение какого-то метода. Второй случай наш

	public class WeatherUpdateBinder extends Binder {
		WeatherUpdateService getOdometer() {
			return WeatherUpdateService.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();

		new Thread(new Runnable() {
			@Override
			public void run() {

				stopSelf();
			}
		}).start();
	}

	private void checkInternet() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
		if (networkinfo != null && networkinfo.isConnected()) {
			Toast.makeText(this, "Интернет подключен", Toast.LENGTH_SHORT).show();

			updateWeatherData();
		} else {
			Toast.makeText(this, "Подключите интернет", Toast.LENGTH_SHORT).show();
		}
	}

	//Обновление/загрузка погодных данных
	private void updateWeatherData() {
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
							updateGson(weatherGson);

						}
					});
				}
			}
		}.start();
	}

	private void updateGson(WeatherGson weatherGson){
		this.weatherGson = weatherGson;
	}

	@Override
	public IBinder onBind(Intent intent) {
		Bundle bundle = intent.getExtras();
		city = bundle.getString(WeatherActivity.SELECTION_CITY);
		checkInternet();
		return binder;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public WeatherGson getWeatherGson() {
		return weatherGson;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		timeOutService();
		return super.onUnbind(intent);
	}

	private void timeOutService() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				//задерживае на время
				long endTime = System.currentTimeMillis() + TIME_OUT_SERVICE;
				while (System.currentTimeMillis() < endTime) {
					synchronized (this) {
						try {
							stopSelf();
						} catch (Exception ignored) {
						}
					}
				}
			}
		}).start();
	}
}
