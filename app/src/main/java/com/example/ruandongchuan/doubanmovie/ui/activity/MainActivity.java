package com.example.ruandongchuan.doubanmovie.ui.activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ruandongchuan.doubanmovie.R;
import com.example.ruandongchuan.doubanmovie.constants.DoubanApi;
import com.example.ruandongchuan.doubanmovie.ui.adapter.FragmentPagerAdapterImpl;
import com.example.ruandongchuan.doubanmovie.ui.fragment.OnShowFragment;
import com.example.ruandongchuan.doubanmovie.ui.fragment.TopFragment;
import com.example.ruandongchuan.doubanmovie.ui.fragment.UsBoxFragment;
import com.example.ruandongchuan.doubanmovie.util.ImageLoader.ImageLoader;
import com.example.ruandongchuan.doubanmovie.util.LogTool;
import com.example.ruandongchuan.doubanmovie.util.MySuggestionProvider;
import com.example.ruandongchuan.doubanmovie.util.interfaces.OnCallBack;

import java.util.List;

public class MainActivity extends AbstractActivity implements NavigationView.OnNavigationItemSelectedListener{
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private Toast mToast;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private String located;
    private OnShowFragment onShowFragment;
    private FragmentPagerAdapterImpl mAdapter;
    private String la,lo;
    private DrawerLayout mDrawer;
    private ImageView navigationImage;
    private TextView tv_naviga_name,tv_userid,tv_addtime,tv_location;
    private SharedPreferences mSharedPreferences;
    private NavigationView mNavigationView;
    private Toolbar toolbar;
    private boolean isDark = false,isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        getLocation();
        initData();
        updateNavigation();
    }

    //更新侧滑导航栏的数据
    private void updateNavigation(){
        SharedPreferences sharedPreferences = getSharedPreferences("setting",MODE_PRIVATE);
        isDark = sharedPreferences.getBoolean("isDark",false);
        if (isDark){
            switchTheme(true);
        }
        mSharedPreferences = getSharedPreferences("Token", MODE_PRIVATE);
        //判断是否登录
        if (mSharedPreferences.contains("access_token")){
            isLogin = true;
            String token = mSharedPreferences.getString("access_token",null);
            if (token!=null){
                DoubanApi.authComplete(token);
            }
            //将用户数据更新到侧滑导航栏
            String name = mSharedPreferences.getString("douban_user_name", "");
            String image = mSharedPreferences.getString("image",null);
            String created = mSharedPreferences.getString("created","");
            String uid = mSharedPreferences.getString("uid","");
            String loc_name = mSharedPreferences.getString("loc_name","");
            if (image!=null){
                ImageLoader.getmInstance().loadImage(image,navigationImage);
            }
            tv_naviga_name.setText(name);
            tv_userid.setText(uid);
            tv_location.setText(loc_name);
            tv_addtime.setText(created);
        }else {
            navigationImage.setImageResource(R.mipmap.douban_movie);
            tv_naviga_name.setText(getString(R.string.have_not_login));
            isLogin = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initView() {
        //初始化抽屉与侧滑导航栏
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        View naviga_header = mNavigationView.inflateHeaderView(R.layout.nav_header_main_drawer);
        navigationImage = (ImageView) naviga_header.findViewById(R.id.imageView);
        tv_naviga_name = (TextView) naviga_header.findViewById(R.id.naviga_tv_name);
        tv_addtime = (TextView) naviga_header.findViewById(R.id.tv_addtime);
        tv_location = (TextView) naviga_header.findViewById(R.id.tv_location);
        tv_userid = (TextView) naviga_header.findViewById(R.id.tv_userid);
        naviga_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.closeDrawer(GravityCompat.START);
                if (!DoubanApi.isAuthed) {
                    Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                    startActivityForResult(intent, 100);
                } else {

                }
            }
        });
        mViewPager = (ViewPager) findViewById(R.id.vp_main);
        mTabLayout = (TabLayout) findViewById(R.id.tab_main);
        mViewPager.setOffscreenPageLimit(3);
        located = getString(R.string.locating);
    }

    private void initData(){
        //提交经纬度
        onShowFragment = OnShowFragment.getInstance(la,lo);
        LogTool.i("activity_create", onShowFragment.toString());
        onShowFragment.setOnGetCity(new OnCallBack<String>() {
            @Override
            public void callBack(String s) {
                located = s;
                invalidateOptionsMenu();
            }
        });
        mAdapter = new FragmentPagerAdapterImpl(getSupportFragmentManager());
        //初始化主界面fragment
        mAdapter.addFragment(getString(R.string.onshow), onShowFragment);
        mAdapter.addFragment(getString(R.string.title_activity_Top205), new TopFragment());
        mAdapter.addFragment(getString(R.string.title_fragment_us_box), new UsBoxFragment());
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mToast = Toast.makeText(this, getString(R.string.exit_message), Toast.LENGTH_SHORT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100){
            updateNavigation();
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)){
            //关闭抽屉
            mDrawer.closeDrawer(GravityCompat.START);
        }else {
            //双击返回键退出应用
            if (mToast.getView().getParent() == null) {
                mToast.show();
            } else {
                finish();
                super.onBackPressed();
            }
        }
    }
    //
    private void getLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = "";
        List<String> providers = mLocationManager.getProviders(true);
        //GPS,网络定位是否可用
        if (!mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.main_dialog_title))
                    .setMessage(getString(R.string.main_dialog_message))
                    .setPositiveButton(getString(R.string.setting), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
            dialog.show();
        }
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            locationProvider = LocationManager.NETWORK_PROVIDER;
            //Toast.makeText(getApplicationContext(),"网络定位",Toast.LENGTH_SHORT).show();
        } else {
            located = getString(R.string.default_location);
            //onShowFragment.update(located);
            la = "";
            lo = "";
            return;
        }
        mLocationListener = new LocationListener() {
            //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
            @Override
            public void onLocationChanged(Location location) {
                LogTool.i("onLocationChanged",location.getLatitude()+"-"+location.getLongitude());
            }

            // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            // Provider被enable时触发此函数，比如GPS被打开
            @Override
            public void onProviderEnabled(String provider) {
                LogTool.i("onProviderEnabled",provider);
            }

            // Provider被disable时触发此函数，比如GPS被关闭
            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        try{
            Location location = mLocationManager.getLastKnownLocation(locationProvider);
            if (location != null){
                la = location.getLatitude()+"";
                lo = location.getLongitude()+"";
                removeLocation();
                LogTool.i("location",location.getLatitude()+"-"+location.getLongitude());
            }else {
                mLocationManager.requestLocationUpdates(locationProvider, 0, 0, mLocationListener);
                located = getString(R.string.default_location);
            }

        }catch (SecurityException e){
            Toast.makeText(getApplicationContext(),"no permission",Toast.LENGTH_SHORT).show();
        }
        invalidateOptionsMenu();
    }

    //刷新经纬度，获取地理位置信息
    private void refresh(){
        onShowFragment = (OnShowFragment) mAdapter.getActiveFragment(mViewPager, 0);
        onShowFragment.update(la, lo);
        onShowFragment.setOnGetCity(new OnCallBack<String>() {
            @Override
            public void callBack(String s) {
                located = s;
                invalidateOptionsMenu();
            }
        });
        LogTool.i("activity", onShowFragment.toString() + "--" + onShowFragment.toString());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeLocation();
        ImageLoader.getmInstance().clearMemeoryCache();
    }
    //移除定位监听
    private void removeLocation(){
        if (mLocationManager != null && mLocationListener != null){
            try {
                mLocationManager.removeUpdates(mLocationListener);
            }catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds mItems to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        //搜索按钮
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setQueryRefinementEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //保存搜索记录
                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(getApplicationContext(),
                        MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
                suggestions.saveRecentQuery(query, null);
                //跳转到搜索界面
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                //获取点击的字符串
                Cursor cursor = searchView.getSuggestionsAdapter().getCursor();
                String text = cursor.getString(3);
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("query", text);
                startActivity(intent);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem location = menu.findItem(R.id.location);
        location.setTitle(located);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //清除搜索记录
        if (id == R.id.action_clear) {
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(getApplicationContext(),
                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
            suggestions.clearHistory();
            return true;
        }
        //重新定位
        if (id == R.id.location){
            //Toast.makeText(getApplicationContext(),"location",Toast.LENGTH_SHORT).show();
            getLocation();
            refresh();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        //设置
        if (id == R.id.nav_setting){
            mDrawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(MainActivity.this,SettingsActivity.class));
            return  false;
        }
        //退出登录
        if (id == R.id.nav_logout){
            mDrawer.closeDrawer(GravityCompat.START);
            if (!isLogin){
                isLogin = false;
                Toast.makeText(this,getString(R.string.please_login),Toast.LENGTH_SHORT).show();
                return false;
            }
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage(getString(R.string.clearing_data));
            dialog.show();
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.clear();
            editor.apply();
            DoubanApi.authClear();
            updateNavigation();
            dialog.dismiss();
            Toast.makeText(this,getString(R.string.cleared_data),Toast.LENGTH_SHORT).show();
            return false;
        }
        //切换模式
        if (id == R.id.switch_theme){
            isDark = !isDark;
            switchTheme(isDark);
            return false;
        }
        return false;
    }
    //切换夜间模式
    private void switchTheme(boolean isDark){
        mSharedPreferences = getSharedPreferences("setting",MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean("isDark",isDark);
        editor.apply();
        int bg_dark = getResources().getColor(R.color.bg_dark);
        int bg_nomal = getResources().getColor(R.color.douban_movie);
        int bg_white = Color.WHITE;
        mNavigationView.setBackgroundColor(isDark ? bg_dark : bg_white);
        mDrawer.setBackgroundColor(isDark ? bg_dark : bg_white);
        toolbar.setBackgroundColor(isDark ? bg_dark : bg_nomal);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(isDark ? bg_dark : getResources().getColor(R.color.douban_movie_dark));
        }
        mTabLayout.setBackgroundColor(isDark ? bg_dark : bg_nomal);
        mNavigationView.setItemTextColor(ColorStateList.valueOf(isDark ? bg_white : Color.BLACK));
    }
}
