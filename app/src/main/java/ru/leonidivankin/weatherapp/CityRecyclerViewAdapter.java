package ru.leonidivankin.weatherapp;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class CityRecyclerViewAdapter extends RecyclerView.Adapter<CityRecyclerViewHolder> {

	Context context;
	List<City> cities;
	ClickListenerShowActivity clickListenerShowActivity;


	private View view;

	public CityRecyclerViewAdapter(Context context, List<City> cities) {
		this.context = context;
		this.cities = cities;
		clickListenerShowActivity = (ClickListenerShowActivity) context;

	}

	@Override
	public CityRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model, null);
		CityRecyclerViewHolder holder = new CityRecyclerViewHolder(view, context, cities);
		return holder;

	}

	@Override
	public void onBindViewHolder(CityRecyclerViewHolder holder, int position) {

		holder.imageViewItemCity.setImageResource(R.drawable.marker);
		holder.textViewItemCity.setText(cities.get(position).getName());

		holder.setClickListenerOnItemClick(new ClickListenerOnItemClick() {
			@Override
			public void onItemClick(View v, int pos) {
				Snackbar.make(v, cities.get(pos).getName(), Snackbar.LENGTH_SHORT).show();
				clickListenerShowActivity.showActivity(cities.get(pos).getName());
			}
		});
	}


	@Override
	public int getItemCount() {
		return cities.size();
	}


}
