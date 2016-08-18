package com.atcommandtool.com.atcommandtool;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.atcommandtool.com.atcommandtool.fragment.ATCommandFragment;
import com.atcommandtool.com.atcommandtool.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mLeftDrawer;
    private ListView mLeftDrawerList;
    private LinearLayout mLeftDrawerContent;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] mDrawerMenu;
    private int mPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fragment fragment = null;

        super.onCreate(savedInstanceState);
        mDrawerMenu = this.getResources().getStringArray(R.array.drawer_menu);
        initActionBar();
        initDrawer();
        initDrawerList();

        fragment = new MainFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    private void initDrawer(){
        setContentView(R.layout.activity_main);
        mLeftDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(null != mLeftDrawer)
            mLeftDrawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        else
            return;

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mLeftDrawer,
                R.string.open_left_drawer,
                R.string.close_left_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.open_left_drawer);
            }

            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                if(mPosition >= 0)
                    getSupportActionBar().setTitle(mDrawerMenu[mPosition]);
            }
        };
        mDrawerToggle.syncState();
        mLeftDrawer.addDrawerListener(mDrawerToggle);
    }

    private void initDrawerList(){

        mLeftDrawerList = (ListView) findViewById(R.id.left_drawer);
        mLeftDrawerContent = (LinearLayout) findViewById(R.id.llv_left_drawer);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.left_drawer_menu_item, mDrawerMenu);
        mLeftDrawerList.setAdapter(adapter);

        mLeftDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectMenuItem(position);
            }
        });
    }

    private void selectMenuItem(int position)
    {
        Fragment fragment = null;
        if(mPosition == position)
        {
            mLeftDrawer.closeDrawer(mLeftDrawerContent);
            return;
        }

        switch (position)
        {
            case 0:
                mPosition = 0;
                ATCommandFragment atCommandF= new ATCommandFragment();
                atCommandF.setContext(this);
                fragment = atCommandF;
                break;

            case 1:
                mPosition = 1;
                break;

            default:
                break;
        }

        if(null != fragment) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            mLeftDrawerList.setItemChecked(position, true);
        }
        mLeftDrawer.closeDrawer(mLeftDrawerContent);
    }

    private void initActionBar()
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if(mPosition != -1)
            {
                Fragment fragment = new MainFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                mPosition = -1;
            }
            else
            {
                return super.onKeyDown(keyCode, event);
            }
        }

        return true;
    }
}