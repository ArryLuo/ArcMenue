package com.example.arcmenue;

import java.util.ArrayList;
import java.util.List;

import com.example.arcmenue.PopMenus.OnMenuItemListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends Activity {
private PopMenus menus;
private ListView listView;
private List<String>list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list=new ArrayList<String>();
        for (int i = 'A'; i < 'Z'; i++) {
			list.add((char)i+"");
		}
        menus=(PopMenus) findViewById(R.id.id_menus);
        listView=(ListView) findViewById(R.id.listview);
        ArrayAdapter<String>adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if(menus.isOpen()){
					menus.toggleMenu(600);
				}
				//Toast.makeText(MainActivity.this, "ª¨∂Ø¡À", 0).show();
			}
		});
        menus.setOnMenuItemListener(new OnMenuItemListener() {
			
			@Override
			public void onClick(View v, int pos) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, v.getTag()+"", 0).show();
			}
		});
    }

}
