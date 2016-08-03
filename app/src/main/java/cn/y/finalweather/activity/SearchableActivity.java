package cn.y.finalweather.activity;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

import cn.y.finalweather.R;
import cn.y.finalweather.adapter.CityRecyclerAdapter;
import cn.y.finalweather.db.FinalWeatherDB;
import cn.y.finalweather.model.City;
import cn.y.finalweather.util.DividerItemDecoration;

public class SearchableActivity extends AppCompatActivity {

    public static final String TAG = "SearchableActivity";
    private RecyclerView recyclerView;
    private InputMethodManager inputMethodManager;
    private SearchView searchView;
    private FinalWeatherDB db;
    private List<City> cityList;
    private LinearLayoutManager layoutManager;
    private Point pointStart = new Point();
    private Point pointEnd = new Point();


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        pointEnd.set(10, 10);
        pointStart.set(0, 0);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                hideSoftInput();
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        pointStart.set((int) (e.getX()), (int) (e.getY()));
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //point.set((int)(e.getX()),(int)(e.getY()));//暂时不用监听
                        break;
                    case MotionEvent.ACTION_UP:
                        pointEnd.set((int) (e.getX()), (int) (e.getY()));
                        break;
                }
//                Log.d(TAG,"X: "+(int) (e.getX())+"\nY: "+ (int) (e.getY()));
//                Log.d(TAG, "Xs: " + pointStart.x + "\nXe: " + pointEnd.x);
                if (Math.abs(pointStart.x - pointEnd.x) < 2 && Math.abs(pointStart.y - pointEnd.y) < 2) {
                    View v = rv.findChildViewUnder(e.getX(), e.getY());
                    int position = rv.getChildLayoutPosition(v);
//                    Log.d(TAG, rv.getChildLayoutPosition(v) + "");
                    String cityId = cityList.get(position).getId();
                    Intent intent = new Intent();
                    intent.putExtra("cityId", cityId);
                    setResult(RESULT_OK, intent);
                    Log.d(TAG, cityId);
                    finish();
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        db = FinalWeatherDB.getFinalWeatherDB(this);
//        cityList = db.loadCities(null);
//        cityList = cityList.subList(0,100);
//        Toast.makeText(this,cityList.size()+"",Toast.LENGTH_LONG).show();
//        adapter  = new CityRecyclerAdapter(this,cityList);
        recyclerView.setHasFixedSize(true);//不加这句notifyDataSetChanged（）不工作
        //设置Adapter
//        recyclerView.setAdapter(adapter);
//        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
//                hideSoftInput();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setIconified(false);
        menu.findItem(R.id.action_search).collapseActionView();
        //是搜索框默认展开
        menu.findItem(R.id.action_search).expandActionView();
//        searchView.setBackgroundColor(0xff000000);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 1) {
                    cityList = db.loadCities(newText);
                    Log.d(TAG, cityList.size() + "");
//                adapter.notifyDataSetChanged();//!!!!!!!!!!!!!!!!!!!!!!!!!!!!无效
//                adapter.notifyItemInserted(adapter.getItemCount());//必须用此方法才能进行recycleview的刷新。（末尾刷新）
                    CityRecyclerAdapter adapter = new CityRecyclerAdapter(SearchableActivity.this, cityList);
                    recyclerView.setAdapter(adapter);
                }
                return false;
            }
        });
        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.action_search), new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                finish();
                return false;
            }
        });
        return true;
    }


    private void hideSoftInput() {
        if (inputMethodManager != null) {
            View v = SearchableActivity.this.getCurrentFocus();
            if (v == null) {
                return;
            }

            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            searchView.clearFocus();
        }
    }


}
