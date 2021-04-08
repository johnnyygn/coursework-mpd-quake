package org.me.gcu.equakestartercode.fragments;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import android.widget.ScrollView;


import org.jetbrains.annotations.NotNull;
import org.me.gcu.equakestartercode.UIComponents.QuakeButton;
import org.me.gcu.equakestartercode.interfaces.IBottomNavMove;
import org.me.gcu.equakestartercode.models.Item;
import org.me.gcu.equakestartercode.viewmodels.ItemViewModel;
import org.me.gcu.equakestartercode.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private LinearLayout liner;
    private IBottomNavMove bottomNavMove;
    private ArrayList<Item> items ;
    private ItemViewModel itemViewModel;
    private SwipeRefreshLayout swipeRefresh;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        liner = (LinearLayout) view.findViewById(R.id.liner);
        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);

        //refresh list on pull up on scrollview
        swipeRefresh.setOnRefreshListener(this);
        bottomNavMove = (IBottomNavMove) requireActivity();

        itemViewModel.getItems().observe(this, listItems -> {
            //Sorts Items reverse Magnitude
            Collections.sort(listItems , Collections.reverseOrder());
            items = listItems;
            //If first load, display list
            //needs to be in observe because otherwise null object reference :(
            //only on first because otherwise items are in savedInstanceState
            if(savedInstanceState == null) {
                displayItems();
            }
        });
        //If not first load set saved items and display list.
        if(savedInstanceState != null) {
            items = savedInstanceState.getParcelableArrayList("items");
            displayItems();
        }
    }

    //Move Bottom Nav to correct position if not explicitly click on Bottom Nav
    //i.e when pressing back
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden == false) {
            bottomNavMove.bottomNavMoved("Home");
        }
    }

    //Save items on orientation change
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("items" , items);
    }

    //Take items and display list
    // 1x QuakeButton if vertical
    // 2x QuakeButtons if horizontal
    private void displayItems() {
        //Ensures fresh list
        liner.removeAllViews();
        LinearLayout linearLayout = null;
        //Different layout params for vertical and horizontal
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        boolean orientaion = false;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            orientaion = true;
            param = new LinearLayout.LayoutParams(
                    3,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0.7f);
            param.setMargins(8, 0, 8, 0);
        }


        for (int i = 0; i < items.size(); i++) {
            Item selectedItem = items.get(i);
            QuakeButton btn = new QuakeButton(getContext(), selectedItem, param);

            //If vertical just add QuakeButton to liner
            //Else add to horizontal linearLayout
            if (orientaion == true) {
                //If list is odd and current item last
                //1 button takes up 2x slot
                if (i % 2 == 0 && i == items.size() - 1) {
                    linearLayout = new LinearLayout(getContext());

                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayout.addView(btn);
                    liner.addView(linearLayout);
                } else if (i % 2 == 0) {
                    linearLayout = new LinearLayout(getContext());
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayout.addView(btn);
                } else {
                    linearLayout.addView(btn);
                    liner.addView(linearLayout);
                }

            } else {
                liner.addView(btn);
            }
        }
    }

    //Redraws list
    //Don't actually refresh cause observer is always watching - not scary sounding  ◥(ºwº)◤
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Cancel the Visual indication of a refresh
                swipeRefresh.setRefreshing(false);
                //Get new items and display
                displayItems();
            }
        }, 1500);
    }
}