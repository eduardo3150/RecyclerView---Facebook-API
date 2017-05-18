package com.chavez.eduardo.recyclerview;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        RecyclerViewFgr.OnFragmentInteractionListener,
        AddElement.OnFragmentInteractionListener,
        EditEventFragment.OnFragmentInteractionListener,
        ViewEventFragment.OnFragmentInteractionListener,
        GraphItemEvent.OnFragmentInteractionListener,
        ShareFragment.OnFragmentInteractionListener {

    public FloatingActionButton fab;
    RecyclerViewFgr recyclerViewFgr;
    AddElement addElement;
    EditEventFragment editEventFragment;
    ViewEventFragment viewEventFragment;
    GraphItemEvent graphItemEvent;
    CoordinatorLayout coordinatorLayout;
    ShareFragment shareFragment;
    private  MenuItem menuItem;

    private int ID_ACTUAL = 0;
    Bundle bundle;

    TextView nav_user;
    CircleImageView profilePic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerViewFgr = new RecyclerViewFgr();
        addElement = new AddElement();
        editEventFragment = new EditEventFragment();
        viewEventFragment = new ViewEventFragment();
        graphItemEvent = new GraphItemEvent();
        shareFragment = new ShareFragment();


        Bundle inBundle = getIntent().getExtras();
        String name = inBundle.get("name").toString();
        String surname = inBundle.get("surname").toString();
        String imageUrl = inBundle.get("imageUrl").toString();


        bundle = new Bundle();


        getSupportFragmentManager().beginTransaction().replace(R.id.viewFragment, recyclerViewFgr).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.viewFragment, addElement).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                fab.hide();

            }
        });

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        Snackbar.make(coordinatorLayout, "Bienvenido "+name, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        nav_user = (TextView) hView.findViewById(R.id.user_name);
        profilePic = (CircleImageView) hView.findViewById(R.id.imageHeader);
        new FetchPicture(profilePic).execute(imageUrl);
        nav_user.setText(name +" "+surname);

        navigationView.setNavigationItemSelectedListener(this);






    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.viewFragment, recyclerViewFgr).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(MainActivity.this,LoginScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_agregar) {
            getSupportFragmentManager().beginTransaction().replace(R.id.viewFragment, addElement).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
            fab.hide();

        } else if (id == R.id.nav_editar) {
            if (ID_ACTUAL==0){
                Snackbar.make(coordinatorLayout, "Seleccione un elemento de la lista", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            else {
            if (editEventFragment.isVisible()) {
            } else {
                bundle.putInt("ID", getID_ACTUAL());
                editEventFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.viewFragment, editEventFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                fab.hide();
            }
            }
        } else if (id == R.id.nav_mostrar) {
            if (ID_ACTUAL==0){
                Snackbar.make(coordinatorLayout, "Seleccione un elemento de la lista", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }else {
                if (viewEventFragment.isVisible()) {

                } else {
                    bundle.putInt("ID", getID_ACTUAL());
                    viewEventFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.viewFragment, viewEventFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                    fab.hide();
                }
            }

        } else if (id == R.id.nav_manage) {
            if (ID_ACTUAL==0){
                Snackbar.make(coordinatorLayout, "Seleccione un elemento de la lista", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
            else {
                if (graphItemEvent.isVisible()) {

                } else {
                    bundle.putInt("ID", getID_ACTUAL());
                    graphItemEvent.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.viewFragment, graphItemEvent).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                    fab.hide();
                }

            }
        } else if (id == R.id.nav_post) {

            if (ID_ACTUAL==0){
                Snackbar.make(coordinatorLayout, "Seleccione un elemento de la lista", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            else {
                if (shareFragment.isVisible()) {
                } else {
                    bundle.putInt("ID", getID_ACTUAL());
                    shareFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.viewFragment, shareFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                    fab.hide();
                }
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void hideFloat() {
        fab.hide();
    }

    public int getID_ACTUAL() {
        return ID_ACTUAL;
    }

    public void setID_ACTUAL(int ID_ACTUAL) {
        this.ID_ACTUAL = ID_ACTUAL;
    }




}
