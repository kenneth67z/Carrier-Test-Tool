package com.carriertesttool;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.carriertesttool.fragment.AutoExePhoneFuncFragment;
import com.carriertesttool.fragment.ShellCommandFragment;
import com.atcommandtool.com.atcommandtool.R;
import com.carriertesttool.fragment.ATCommandFragment;
import com.carriertesttool.fragment.MainFragment;
import com.carriertesttool.util.ToolConstants;
import com.carriertesttool.util.ToolFragmentFactory;
import com.carriertesttool.util.ToolFragmentManager;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mLeftDrawer;
    private ListView mLeftDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] mDrawerMenu;
    private int mPosition = -1;
    private ToolFragmentManager mToolFragmentMngr;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLeftDrawer = null;
        mLeftDrawerList = null;
        mDrawerToggle = null;
        mDrawerMenu = null;

        mToolFragmentMngr.release();
        mToolFragmentMngr = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fragment fragment;

        super.onCreate(savedInstanceState);

        mToolFragmentMngr = ToolFragmentManager.getInstance();
        mToolFragmentMngr.setContext(this);

        // initialize left drawer
        mDrawerMenu = this.getResources().getStringArray(R.array.drawer_menu);
        initActionBar();
        initDrawer();
        initDrawerList();

        // init main view by using main fragment instead
        fragment = mToolFragmentMngr.getFragment(ToolFragmentFactory.TOOL_TYPE.TOOL_MAIN_PAGE);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    private void initDrawer() {
        setContentView(R.layout.activity_main);
        mLeftDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (null != mLeftDrawer)
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
                if (mPosition >= 0)
                    getSupportActionBar().setTitle(mDrawerMenu[mPosition]);
                else
                    getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            }
        };
        mDrawerToggle.syncState();
        mLeftDrawer.addDrawerListener(mDrawerToggle);
    }

    private void initDrawerList() {

        mLeftDrawerList = (ListView) findViewById(R.id.left_drawer);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.left_drawer_menu_item, mDrawerMenu);
        mLeftDrawerList.setAdapter(adapter);

        mLeftDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectMenuItem(position);
            }
        });
    }

    private void selectMenuItem(int position) {
        Fragment fragment = null;
        ToolFragmentFactory.TOOL_TYPE selectItem = ToolFragmentFactory.TOOL_TYPE.values()[position];

        if (mPosition == position) {
            mLeftDrawer.closeDrawer(GravityCompat.START);
            return;
        }

        switch (selectItem) {
            case TOOL_AT_COMMAND:
                mPosition = ToolFragmentFactory.TOOL_TYPE.TOOL_AT_COMMAND.ordinal();
                ATCommandFragment atCommandF = (ATCommandFragment)mToolFragmentMngr.getFragment(selectItem);
                fragment = atCommandF;
                break;

            case TOOL_SHELL_COMMAND:
                mPosition = ToolFragmentFactory.TOOL_TYPE.TOOL_SHELL_COMMAND.ordinal();
                ShellCommandFragment shellCommandF = (ShellCommandFragment)mToolFragmentMngr.getFragment(selectItem);
                fragment = shellCommandF;
                break;
            case TOOL_AUTO_EXE_PHONE_FUNCTION:
                mPosition = ToolFragmentFactory.TOOL_TYPE.TOOL_AUTO_EXE_PHONE_FUNCTION.ordinal();
                AutoExePhoneFuncFragment autoExecutePhoneF = (AutoExePhoneFuncFragment)mToolFragmentMngr.getFragment(selectItem);
                fragment = autoExecutePhoneF;
                break;

            default:
                break;
        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        mLeftDrawerList.setItemChecked(position, true);
        mLeftDrawer.closeDrawer(GravityCompat.START);
    }

    private void initActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // if current view not equal to main fragment, then back to main fragment.
        if (mPosition > -1) {
            Fragment fragment = mToolFragmentMngr.getFragment(ToolFragmentFactory.TOOL_TYPE.TOOL_MAIN_PAGE);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            getSupportActionBar().setTitle(R.string.app_name);
            mLeftDrawer.closeDrawer(GravityCompat.START);
            mPosition = -1;
        } else {
            // if the drawer is opening, close it.
            if (mLeftDrawer.isDrawerOpen(GravityCompat.START))
                mLeftDrawer.closeDrawer(GravityCompat.START);
            else
                super.onBackPressed();      // do the default back key behavior.
        }
    }
}