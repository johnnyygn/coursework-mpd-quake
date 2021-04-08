package org.me.gcu.equakestartercode.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.me.gcu.equakestartercode.viewmodels.ItemViewModel;
import org.me.gcu.equakestartercode.R;
import org.me.gcu.equakestartercode.fragments.HomeFragment;
import org.me.gcu.equakestartercode.fragments.MapFragment;
import org.me.gcu.equakestartercode.fragments.SearchFragment;
import org.me.gcu.equakestartercode.interfaces.IBottomNavMove;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener , IBottomNavMove , Serializable {
    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment = null;
    private MapFragment mapFragment  = null;
    private SearchFragment searchFragment  = null;
    private ItemViewModel itemViewModel ;
    //Represents tag of current fragment user is looking at
    private String currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);

       
        itemViewModel = new ItemViewModel();
        itemViewModel.getItems();

       
        homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("Home");
        searchFragment = (SearchFragment) getSupportFragmentManager().findFragmentByTag("Search");
        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentByTag("Map");

      
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(savedInstanceState == null){
                homeFragment = new HomeFragment();
                fragmentTransaction.add(R.id.frameLayout , homeFragment , "Home");
                fragmentTransaction.hide(homeFragment);

                searchFragment = new SearchFragment();
                fragmentTransaction.add(R.id.frameLayout , searchFragment , "Search");
                fragmentTransaction.hide(searchFragment);

                mapFragment = new MapFragment();
                fragmentTransaction.add(R.id.frameLayout , mapFragment , "Map");
                fragmentTransaction.hide(mapFragment);

                currentFragment = homeFragment.getTag();
                fragmentTransaction.show(homeFragment);
        }
        else{
            currentFragment = savedInstanceState.getString("current");

            if (currentFragment == homeFragment.getTag()) {
                        currentFragment = homeFragment.getTag();
                        fragmentTransaction.show(homeFragment);
                        fragmentTransaction.hide(mapFragment);
                        fragmentTransaction.hide(searchFragment);

                } else if (currentFragment == searchFragment.getTag()) {
                        currentFragment = searchFragment.getTag();
                        fragmentTransaction.hide(homeFragment);
                        fragmentTransaction.hide(mapFragment);
                        fragmentTransaction.show(searchFragment);
                } else if (currentFragment == mapFragment.getTag()) {
                        currentFragment = mapFragment.getTag();
                        fragmentTransaction.show(mapFragment);
                        fragmentTransaction.hide(homeFragment);
                        fragmentTransaction.hide(searchFragment);

                }
        }
        fragmentTransaction.commit();

    
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                        public void run() {
                            try {
                                itemViewModel.loadNewItems();
                            } catch (Exception e) {
                            }
                        }
                    }); }
        };
        timer.schedule(doAsynchronousTask, 0, 400000);
    }

 
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("current" , currentFragment);
    }

  
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(bottomNavigationView.getMenu().getItem(0).isChecked()) {
            if(currentFragment != homeFragment.getTag()) {
                transaction.hide(getSupportFragmentManager().findFragmentByTag(currentFragment));
                currentFragment = homeFragment.getTag();
                transaction.show(homeFragment);
                transaction.addToBackStack("Home");
            }
        }
        else if(bottomNavigationView.getMenu().getItem(1).isChecked()) {
            if(currentFragment != searchFragment.getTag()) {
                transaction.hide(getSupportFragmentManager().findFragmentByTag(currentFragment));
                currentFragment = searchFragment.getTag();
                transaction.show(searchFragment);
                transaction.addToBackStack("Search");
            }
        }
        else if(bottomNavigationView.getMenu().getItem(2).isChecked()) {
            if(currentFragment != mapFragment.getTag()) {
                transaction.hide(getSupportFragmentManager().findFragmentByTag(currentFragment));
                currentFragment = mapFragment.getTag();
                transaction.show(mapFragment);
                transaction.addToBackStack("Map");
            }
        }
        transaction.commit();
        return false;
    }

   
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0 ){
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

  
    @Override
    public void bottomNavMoved(String id) {
        if(id == "Home")
        {
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
        }
        else if(id == "Search")
        {
            bottomNavigationView.getMenu().getItem(1).setChecked(true);
        }
        else if (id == "Map")
        {
            bottomNavigationView.getMenu().getItem(2).setChecked(true);
        }

    }
}
