
package org.me.gcu.equakestartercode.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.me.gcu.equakestartercode.UIComponents.QuakeButton;
import org.me.gcu.equakestartercode.interfaces.IBottomNavMove;
import org.me.gcu.equakestartercode.models.Item;
import org.me.gcu.equakestartercode.viewmodels.ItemViewModel;
import org.me.gcu.equakestartercode.activites.QuakeActivity;
import org.me.gcu.equakestartercode.R;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class SearchFragment extends Fragment implements View.OnClickListener  {

    private Button search;
    private EditText startDate;
    private EditText endDate;
    private DatePickerDialog.OnDateSetListener from_dateListener, to_dateListener;
    private List<Item> items = new ArrayList<>();
    private FrameLayout eastFrame;
    private FrameLayout westFrame;
    private FrameLayout northFrame;
    private FrameLayout southFrame;
    private FrameLayout bigFrame;
    private FrameLayout deepFrame;
    private FrameLayout shallowFrame;
    private ScrollView sc;
    private IBottomNavMove bottomNavMove;
    private ItemViewModel itemViewModel;
    private ArrayList<Item> searchItems = new ArrayList<>();
    private TextView noQuakes;
    //Westsest
    private Item leftist = null;
    //Eastest
    private Item rightist = null;
    //Northest
    private Item upist = null;
    //Southest
    private Item downist = null;
    //Biggest Magnitude
    private Item biggest = null;
    //Deepest in km
    private Item deepest = null;
    //Deepest in km
    private Item shallowist = null;
    //Has user searched
    private boolean hasPressed = false;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

            //View Model Data Shares instance with Activity (Main Activity)
            itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
            //Observes updates items global variables whenever itemviewmodel updates
            itemViewModel.getItems().observe(this, listItems -> {
                items = listItems;
            });
            eastFrame = (FrameLayout) view.findViewById(R.id.eastFrame);
            westFrame = (FrameLayout) view.findViewById(R.id.westFrame);
            northFrame = (FrameLayout) view.findViewById(R.id.northFrame);
            southFrame = (FrameLayout) view.findViewById(R.id.southFrame);
            bigFrame = (FrameLayout) view.findViewById(R.id.bigFrame);
            deepFrame = (FrameLayout) view.findViewById(R.id.deepFrame);
            shallowFrame = (FrameLayout) view.findViewById(R.id.shallowFrame);
            sc = (ScrollView) getView().findViewById(R.id.scrollView3);
            noQuakes = (TextView) getView().findViewById(R.id.noQuakes);

            //Sets EditTexts startDate and endDate to uneditable and on press show DatePickerDialog.
            startDate = (EditText) view.findViewById(R.id.startDate);
            startDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
            startDate.setKeyListener(null);
            startDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), from_dateListener, 2021, 01, 01);
                    Date latestDate = parseEnterDate(endDate.getText().toString());
                    datePickerDialog.getDatePicker().setMaxDate(latestDate.getTime());
                    datePickerDialog.show();
                }
            });
            from_dateListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String t = dayOfMonth + "-" + (month + 1) + "-" + year;
                    startDate.setText(t);
                }
            };

            endDate = (EditText) view.findViewById(R.id.endDate);
            endDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
            endDate.setKeyListener(null);
            endDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), to_dateListener, 2021, 01, 01);
                    datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                    Date earlyDate = parseEnterDate(startDate.getText().toString());
                    datePickerDialog.getDatePicker().setMinDate(earlyDate.getTime());

                    datePickerDialog.show();
                }
            });
            to_dateListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String t = dayOfMonth + "-" + (month + 1) + "-" + year;
                    endDate.setText(t);
                }
            };

            //Sets default text for startDate and endDate to current Date.

            search = (Button) view.findViewById(R.id.search);
            search.setOnClickListener(this);
            bottomNavMove = (IBottomNavMove) getActivity();

        //On orientation change set hasPressed to previous saved value.
        if(savedInstanceState != null) {
            searchItems = savedInstanceState.getParcelableArrayList("searchItems");
            hasPressed = savedInstanceState.getBoolean("hasPressed");
            //only re-search for data if search has already occurred
            if(hasPressed == true) {
                getExtremes(searchItems);
                displayItems();
            }
        }
    }

    //On orientation change save hasPressed value and current list of items
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("searchItems",  searchItems);
        outState.putBoolean("hasPressed" , hasPressed);
    }

    //Move Bottom Nav to correct position if not explicitly click on
    //Bottom Nav - i.e when pressing back
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden == false) {
            bottomNavMove.bottomNavMoved("Search");
        }
    }

    //On click of search button run searchDate(), the getExtremes() then displayItems()
    @Override
    public void onClick(View v) {
        if (v == search) {
            hasPressed = true;
            searchDate(startDate.getText().toString(), endDate.getText().toString());
            getExtremes(searchItems);
            displayItems();
        }
    }

    //Takes startDate and endDate and sets all items with those dates to searchItems
    private void searchDate(String startDate, String endDate) {
        Date startDateDate = parseEnterDate(startDate);
        Date endDateDate = parseEnterDate(endDate);
        searchItems = new ArrayList<>();
        //Returns list of Items which falls between startDate and endDate.
        for (int i = 0; i < items.size(); i++) {
            Date date = parseDataDate(items.get(i).getPubDate());
            if (date.after(startDateDate) && date.before(endDateDate)) {
                searchItems.add(items.get(i));
            }
        }
    }

    //Using searchItems calculates and sets the leftist , rightist etc...
    private void getExtremes(ArrayList<Item> searchItems) {
        for (int j = 0; j < searchItems.size(); j++) {
            Item currentItem = searchItems.get(j);
            if (j == 0) {
                leftist = currentItem;
                rightist = currentItem;
                upist = currentItem;
                downist = currentItem;
                deepest = currentItem;
                shallowist = currentItem;
                biggest = currentItem;
            } else {
                if (Float.parseFloat(leftist.getGeoLong()) > Float.parseFloat(currentItem.getGeoLong())) {
                    leftist = currentItem;
                }

                if (Float.parseFloat(rightist.getGeoLong()) < Float.parseFloat(currentItem.getGeoLong())) {
                    rightist = currentItem;
                }

                if (Float.parseFloat(upist.getGeoLat()) < Float.parseFloat(currentItem.getGeoLat())) {
                    upist = currentItem;
                }

                if (Float.parseFloat(downist.getGeoLat()) > Float.parseFloat(currentItem.getGeoLat())) {
                    downist = currentItem;
                }
                if (Float.parseFloat(biggest.getMagnitude()) < Float.parseFloat(currentItem.getMagnitude())) {
                    biggest = currentItem;
                }
                //".getDepth().substring(0, 2)" -> removes last 2 characters from depth which is "km"
                if (Float.parseFloat(deepest.getDepth().substring(0, 2)) < Float.parseFloat(currentItem.getDepth().substring(0, 2))) {
                    deepest = currentItem;
                }

                if (Float.parseFloat(shallowist.getDepth().substring(0, 2)) > Float.parseFloat(currentItem.getDepth().substring(0, 2))) {
                    shallowist = currentItem;
                }
            }
        }
    }

    //Removes views from each Frame
    //Displays each corresponding Item as a QuakeButton
    private void displayItems() {
            sc.setVisibility(View.INVISIBLE);
            noQuakes.setVisibility(View.INVISIBLE);
            //Sets text of each of the specific Items.
            eastFrame.removeAllViews();
            northFrame.removeAllViews();
            westFrame.removeAllViews();
            southFrame.removeAllViews();
            bigFrame.removeAllViews();
            deepFrame.removeAllViews();
            shallowFrame.removeAllViews();
            if (searchItems.size() > 0) {
                sc.setVisibility(View.VISIBLE);
                eastFrame.addView(new QuakeButton(getContext(), rightist));
                westFrame.addView(new QuakeButton(getContext(), leftist));
                northFrame.addView(new QuakeButton(getContext(), upist));
                southFrame.addView(new QuakeButton(getContext(), downist));
                bigFrame.addView(new QuakeButton(getContext(), biggest));
                deepFrame.addView(new QuakeButton(getContext(), deepest));
                shallowFrame.addView(new QuakeButton(getContext(), shallowist));
            } else {
                noQuakes.setVisibility(View.VISIBLE);
            }
    }
    
    //Parses Date entered with DatePickerDialog
    private Date parseEnterDate(String date) {
        SimpleDateFormat dateFormatOut = new SimpleDateFormat("dd-MM-yyyy");
        Date outDate = null;
        try {
            outDate = dateFormatOut.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outDate;
    }

    //Parses Date from Items (XML)
    private Date parseDataDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        Date outDate = null;
        try {
            outDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outDate;

    }
}
