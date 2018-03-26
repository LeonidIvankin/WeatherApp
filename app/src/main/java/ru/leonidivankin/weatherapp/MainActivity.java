package ru.leonidivankin.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		RecyclerView nailsCategoriesRecyclerView = findViewById(R.id.recycler_view); //Найдем наш RecyclerView
		LinearLayoutManager layoutManager = new LinearLayoutManager(this); //Создадим LinearLayoutManager
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);//Обозначим ориентацию
		nailsCategoriesRecyclerView.setLayoutManager(layoutManager);//Назначим нашему RecyclerView созданный ранее layoutManager
		nailsCategoriesRecyclerView.setAdapter(new MyAdapter(Weather.weather));//Назначим нашему RecyclerView адаптер

		//NavigationView
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();
		NavigationView navigationView = findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);


	}

	//Запускаем активити для конкретной услуги
	private void showNailActivity(int categoryId) {//id элемента в массиве
		Intent intent = new Intent(this, WeatherActivity.class);
		intent.putExtra(WeatherActivity.EXTRA_WEATHERNOM, categoryId);
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();
		Intent intent = null;
		if (id == R.id.about_us) {
			intent = new Intent(this, AboutUsActivity.class);
			startActivity(intent);
		} else if (id == R.id.nav_gallery) {

		}

		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	//Класс, который содержит в себе все элементы списка
	private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		private TextView categoryNameTextView;

		MyViewHolder(LayoutInflater inflater, ViewGroup parent) {
			//inflater - получает xml и надувает
			//false - будет ли этот элемент при attach
			super(inflater.inflate(R.layout.category_list_item, parent, false));
			//setOnClickListener - при нажатии
			itemView.setOnClickListener(this);
			categoryNameTextView = itemView.findViewById(R.id.category_name_text_view);
		}

		void bind(String category) {//вызывается каждый раз, когда нужно отображать элемент списка
			//String category = nail.getName();
			if (getLayoutPosition() != RecyclerView.NO_POSITION) {//когда достаточно сложный элемент
				//адаптер может не справляться
				categoryNameTextView.setText(category);
			}
		}

		@Override
		public void onClick(View view) {
			int position = getLayoutPosition();
			showNailActivity(position == RecyclerView.NO_POSITION ? 0 : position);
		}
	}

	//Адаптер для RecyclerView
	private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

		private final Weather[] nails;

		MyAdapter(Weather[] nails) {
			this.nails = nails;
		}

		@Override
		public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {//вызывается по количеству элементов на экране
			LayoutInflater inflater = LayoutInflater.from(getApplicationContext());//класс надувать View получая на вход xml
			return new MyViewHolder(inflater, parent);
		}

		@Override
		public void onBindViewHolder(MyViewHolder holder, int position) {//берёт готовый Viewholder связывает с определённой позицией в списке
			holder.bind(nails[position].getName());
		}

		@Override
		public int getItemCount() {//метод вызывается прежде, чем адаптер вызывает список; длина списка
			return nails.length;
		}
	}


}
