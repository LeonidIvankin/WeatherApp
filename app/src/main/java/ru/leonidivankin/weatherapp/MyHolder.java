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

public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

	protected ImageView imageViewItemCity;
	protected TextView textViewItemCity;
	protected ItemClickListener itemClickListener;
	protected Context context;

	public MyHolder(View itemView, Context context) {
		super(itemView);

		this.context = context;

		textViewItemCity = itemView.findViewById(R.id.text_view_item_city);
		imageViewItemCity = itemView.findViewById(R.id.image_view_item_city);
		itemView.setOnClickListener(this);

		itemView.setOnCreateContextMenuListener(this);

	}


	@Override
	public void onClick(View v) {
		this.itemClickListener.onItemClick(v, getLayoutPosition());
	}

	public void setItemClickListener(ItemClickListener ic) {
		this.itemClickListener = ic;
	}

	//подключаем контекстное меню
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		MenuItem Edit = menu.add(Menu.NONE, 1, 1, "Edit");
		MenuItem Delete = menu.add(Menu.NONE, 2, 2, "Delete");
		Edit.setOnMenuItemClickListener(onEditMenu);
		Delete.setOnMenuItemClickListener(onEditMenu);
	}

	//обработка событий контекстного меню
	private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(MenuItem item) {
			int pos = getLayoutPosition();

			switch (item.getItemId()) {
				case 1:
					Toast.makeText(context, "edit " + pos, Toast.LENGTH_SHORT).show();

					break;

				case 2:
					Toast.makeText(context, "delete" + pos, Toast.LENGTH_SHORT).show();
					break;
			}
			return true;
		}
	};
}
