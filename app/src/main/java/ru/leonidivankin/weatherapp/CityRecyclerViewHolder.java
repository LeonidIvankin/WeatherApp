package ru.leonidivankin.weatherapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CityRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

	public static final int MENU_CONTEXT_ID0 = 0;
	public static final int MENU_CONTEXT_ID1 = 1;
	protected ImageView imageViewItemCity;
	protected TextView textViewItemCity;
	protected ClickListenerOnItemClick clickListenerOnItemClick;
	protected Context context;
	List<City> cities;
	private ClickListenerOnMenuItem clickListenerOnMenuItem;
	//обработка событий контекстного меню
	private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(MenuItem item) {
			int pos = getLayoutPosition();

			switch (item.getItemId()) {
				case MENU_CONTEXT_ID0:
					Toast.makeText(context, "edit " + pos, Toast.LENGTH_SHORT).show();
					clickListenerOnMenuItem.editElement(cities.get(pos).getId());
					break;

				case MENU_CONTEXT_ID1:
					Toast.makeText(context, "delete " + cities.get(pos).getName(), Toast.LENGTH_SHORT).show();
					clickListenerOnMenuItem.deleteElement(cities.get(pos).getId());
					break;
			}
			return true;
		}
	};


	public CityRecyclerViewHolder(View itemView, Context context, List<City> cities) {
		super(itemView);

		this.context = context;
		this.cities = cities;


		textViewItemCity = itemView.findViewById(R.id.text_view_item_city);
		imageViewItemCity = itemView.findViewById(R.id.image_view_item_city);
		itemView.setOnClickListener(this);
		clickListenerOnMenuItem = (ClickListenerOnMenuItem) context;

		itemView.setOnCreateContextMenuListener(this);

	}

	@Override
	public void onClick(View v) {
		this.clickListenerOnItemClick.onItemClick(v, getLayoutPosition());
	}

	public void setClickListenerOnItemClick(ClickListenerOnItemClick ic) {
		this.clickListenerOnItemClick = ic;
	}

	//подключаем контекстное меню
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		MenuItem edit = menu.add(Menu.NONE, MENU_CONTEXT_ID0, 1, "Edit");
		MenuItem delete = menu.add(Menu.NONE, MENU_CONTEXT_ID1, 2, "Delete");
		edit.setOnMenuItemClickListener(onEditMenu);
		delete.setOnMenuItemClickListener(onEditMenu);
	}
}
