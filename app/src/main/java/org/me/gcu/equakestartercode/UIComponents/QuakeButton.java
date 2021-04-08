package org.me.gcu.equakestartercode.UIComponents;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;

import org.me.gcu.equakestartercode.activites.QuakeActivity;
import org.me.gcu.equakestartercode.models.Item;

//Reusable Button for displaying Quakes - allows Press for more information
public class QuakeButton extends MaterialButton implements View.OnClickListener {
    Item item;

    public QuakeButton(@NonNull Context context , Item item , LinearLayout.LayoutParams params) {
        super(context);
        this.item = item;
        this.setText("location: " + item.getLocation() + "\n" + "mangitude: " + item.getMagnitude());
        this.setTextColor(Color.BLACK);
        this.setOnClickListener(this::onClick);
        this.setLayoutParams(params);

        if (Float.parseFloat(item.getMagnitude()) < 1) {
            this.setBackgroundColor(Color.BLUE);
        } else if (Float.parseFloat(item.getMagnitude()) >= 1 && Float.parseFloat(item.getMagnitude()) < 2) {
            this.setBackgroundColor(Color.YELLOW);
        } else if (Float.parseFloat(item.getMagnitude()) >= 2) {
            this.setBackgroundColor(Color.RED);
        }
    }

    public QuakeButton(@NonNull Context context , Item item ) {
        super(context);
        this.item = item;
        this.setText("location: " + item.getLocation() + "\n" + "mangitude: " + item.getMagnitude());
        this.setTextColor(Color.BLACK);
        this.setOnClickListener(this::onClick);
        if (Float.parseFloat(item.getMagnitude()) < 1) {
            this.setBackgroundColor(Color.BLUE);
        } else if (Float.parseFloat(item.getMagnitude()) >= 1 && Float.parseFloat(item.getMagnitude()) < 2) {
            this.setBackgroundColor(Color.YELLOW);
        } else if (Float.parseFloat(item.getMagnitude()) >= 2) {
            this.setBackgroundColor(Color.RED);
        }
    }

    //OnClick opens QuakeActivity on top of main activity and fragment with that quakes information
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), QuakeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Item", item);
        intent.putExtras(bundle);
        getContext().startActivity(intent);
    }
}
