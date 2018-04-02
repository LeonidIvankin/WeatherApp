package ru.leonidivankin.weatherapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class NoteDataSource {

	private SQLiteDatabase db;
	private DBHelper dbHelper;

	private String[] notesAllColumn = {
			DBHelper.COLUMN_ID,
			DBHelper.COLUMN_NAME
	};

	public NoteDataSource(Context context) {
		dbHelper = new DBHelper(context);
	}

	//открыть базу
	public void open() throws SQLException {
		db = dbHelper.getWritableDatabase();
	}

	//закрыть базу
	public void close() {
		db.close();
		dbHelper.close();
	}

	//вставить
	public void addNote(String name) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBHelper.COLUMN_NAME, name);
		db.insert(DBHelper.TABLE_NAME, null, contentValues);
	}

	//изменить
	public void editNote(long id) {
		ContentValues editedNote = new ContentValues();
		editedNote.put(DBHelper.COLUMN_ID, id);

		db.update(DBHelper.TABLE_NAME, editedNote, DBHelper.COLUMN_ID + "=" + id, null);
	}

	//удалить
	public void deleteNote(long id) {
		db.delete(DBHelper.TABLE_NAME, DBHelper.COLUMN_NAME + " = " + id, null);
	}

	public void deleteAll() {
		db.delete(DBHelper.TABLE_NAME, null, null);
	}

	public List<City> getAllNotes() {
		List<City> cities = new ArrayList<>();
		//получение из базы всех строк
		//query - превращает в обычный SQL-запрос
		Cursor cursor = db.query(DBHelper.TABLE_NAME,
				notesAllColumn, null, null, null, null, null);
		//cursor - находится в определенной строке
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {//не вышел ли курсор за последнюю строку
			City city = cursorToNote(cursor);
			cities.add(city);

			cursor.moveToNext();
		}
		// обязательно закройте cursor
		cursor.close();
		//вернуть список заметок
		return cities;
	}

	private City cursorToNote(Cursor cursor) {
		//создаём новыю заметку
		City city = new City();
		city.setId((int) cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_ID)));
		city.setName(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME)));
		return city;
	}
}
