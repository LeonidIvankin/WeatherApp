package ru.leonidivankin.weatherapp;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {

	NoteDataSource notesDataSource;
	private RecyclerView recyclerView;
	private MyAdapter adapter;
	private List<City> elements;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				addElement();
			}
		});


		notesDataSource = new NoteDataSource(getApplicationContext());
		notesDataSource.open();

		//Создаем массив элементов для списка
		//elements = new ArrayList<>();
		elements = notesDataSource.getAllNotes();

		//Найдем наш RecyclerView
		recyclerView = findViewById(R.id.mRecycler);
		//Создадим LinearLayoutManager. Вспомогательный класс. Занимается размещением элементов внутри RecyclerView
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		//Обозначим ориентацию для LinearLayoutManager
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		//Назначим нашему RecyclerView созданный ранее layoutManager
		recyclerView.setLayoutManager(layoutManager);
		//ADAPTER
		adapter = new MyAdapter(this, elements);
		defaultCity(DBHelper.DEFAULT_CITIES);
		recyclerView.setAdapter(adapter);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		notesDataSource.close();
	}

	private void clearList() {
		notesDataSource.deleteAll();
		dataUpdated();
	}

	private void addElement() {

		LayoutInflater factory = LayoutInflater.from(this);
		final View alertView = factory.inflate(R.layout.custom_layout, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(alertView);
		builder.setTitle(R.string.alert_add_city);
		builder.setNegativeButton(R.string.alert_cancel, null);
		builder.setPositiveButton(R.string.menu_add, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				EditText editText = alertView.findViewById(R.id.name_edit_txt);
				notesDataSource.addNote(editText.getText().toString());
				dataUpdated();
			}
		});
		builder.show();

	}

	private void defaultCity(String[] defaultCities) {
		if (elements.size() < 1) {
			for (String city : defaultCities) {
				notesDataSource.addNote(city);
			}
			dataUpdated();
		}
	}


	private void editElement(long id) {
		notesDataSource.editNote(id);
		dataUpdated();
	}

	private void deleteElement(long id) {
		notesDataSource.deleteNote(id);
		dataUpdated();
	}

	private void dataUpdated() {
		elements.clear();
		elements.addAll(notesDataSource.getAllNotes());
		adapter.notifyDataSetChanged();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.menu_add:
				addElement();
				return true;
			case R.id.menu_clear:
				clearList();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}

	}


}
