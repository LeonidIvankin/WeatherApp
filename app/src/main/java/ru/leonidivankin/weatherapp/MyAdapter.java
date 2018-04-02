package ru.leonidivankin.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyHolder> {

	Context context;
	List<City> cities;

	private View v;

	public MyAdapter(Context context, List<City> cities) {
		this.context = context;
		this.cities = cities;

	}

	@Override
	public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model, null);
		MyHolder holder = new MyHolder(v, context);
		return holder;
	}

	@Override
	public void onBindViewHolder(MyHolder holder, int position) {

		holder.imageViewItemCity.setImageResource(R.drawable.marker);
		holder.textViewItemCity.setText(cities.get(position).getName());

		holder.setItemClickListener(new ItemClickListener() {
			@Override
			public void onItemClick(View v, int pos) {
				Snackbar.make(v, cities.get(pos).getName(), Snackbar.LENGTH_SHORT).show();
				showActivity(cities.get(pos).getName());

			}
		});
	}

	private void showActivity(String city) {
		Intent intent = new Intent(context, WeatherActivity.class);
		intent.putExtra(WeatherActivity.EXTRA_POS, city);
		context.startActivity(intent);
	}

	@Override
	public int getItemCount() {
		return cities.size();
	}
}
