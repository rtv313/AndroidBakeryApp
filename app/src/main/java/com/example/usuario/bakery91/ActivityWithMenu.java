package com.example.usuario.bakery91;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

class MenuOption
{
    public String name;
    public Class<?>  activityName;
    public int icon;

    public MenuOption(String name , Class<?>  activityName , int icon)
    {
        this.name = name;
        this.activityName = activityName;
        this.icon = icon;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setActivityName(Class<?>  activityName)
    {
        this.activityName = activityName;
    }

    public Class<?>  getActivityName()
    {
        return activityName;
    }

    public void setIcon(int icon)
    {
        this.icon = icon;
    }

    public int getIcon()
    {
        return icon;
    }
}

public class ActivityWithMenu extends AppCompatActivity {
    private ListView mDrawerList;
    protected DrawerLayout mDrawerLayout;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected int layout = R.layout.activity_menu_tutorial;
    protected String menuTitle;
    protected boolean menuOpen;
    MenuOption[] menuOptions = new MenuOption[5];
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(layout);

        menuOpen = false;

        mDrawerList = findViewById(R.id.navList);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setTitle(menuTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void addDrawerItems() {

        MenuOption  mainMenu = new MenuOption("Menu Principal",MainActivity.class,R.drawable.ic_home);
        MenuOption  orders = new MenuOption("Pedidos",OrdersClients.class,R.drawable.ic_orders);
        MenuOption  products = new MenuOption("Productos",ActivityWithMenu.class,R.drawable.ic_products);
        MenuOption  salesData = new MenuOption("Reporte de ventas",ActivityWithMenu.class,R.drawable.ic_data);
        MenuOption  ordersResume = new MenuOption("Resumen pedidos",OrdersResume.class,R.drawable.ic_orders_resume);

        menuOptions[0] = mainMenu;
        menuOptions[1] = orders;
        menuOptions[2] = products;
        menuOptions[3] = ordersResume;
        menuOptions[4] = salesData;

        MyAdapterMenu mAdapter = new MyAdapterMenu(this, menuOptions);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Class<?> targetActivity = menuOptions[position].getActivityName();
                Intent intent = new Intent(ActivityWithMenu.this, targetActivity);
                startActivity(intent);
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.action_settings, R.string.action_settings) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                menuOpen = true;
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                menuOpen = false;
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tutorial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

// My AdapterMenu
class MyAdapterMenu extends BaseAdapter {

    MenuOption[] menuOptions;
    LayoutInflater lInflater;

    MyAdapterMenu(Context context, MenuOption[] orders){
        menuOptions = orders;
        lInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount(){
        return menuOptions.length;
    }

    @Override
    public Object getItem(int index){
        return menuOptions[index];
    }

    @Override
    public long getItemId(int id){
        return id;
    }

    @Override
    public View getView(int item, View view, ViewGroup parent){

        view = lInflater.inflate(R.layout.menu_row,null);
        TextView menuName = view.findViewById(R.id.textViewMenu);
        ImageView imageViewMenu = view.findViewById(R.id.imageViewMenu);
        MenuOption menuOption = menuOptions[item];
        menuName.setText(menuOption.getName());
        imageViewMenu.setImageResource(menuOption.icon);
        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);

        if(item % 2 == 0)
            linearLayout.setBackgroundColor(Color.parseColor("#e7e8e5"));

        return view;
    }
} // MenuAdapter End

