package com.slymey.gravballs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.SeekBar;

public class SettingsMenu{

    private PopupWindow popupWindow;

    public SettingsMenu(Context context) {
        // Create the popup window layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.settings_menu, null);

        // Create the popup window
        popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        BallViewPane.force_ell = layout.findViewById(R.id.force_bar);
        BallViewPane.wrap_ell = layout.findViewById(R.id.wrap_switch);
        BallViewPane.drag_ell = layout.findViewById(R.id.drag_bar);
        //BallViewPane.grav_ell = layout.findViewById(R.id.grav_switch);


    }
    public void onDraw(Canvas canvas){

        //System.out.println("in innit"+x+" "+y);
    }
    public void show(View anchor) {
        // Show the popup window at the top left of the anchor view
        popupWindow.showAtLocation(anchor, Gravity.TOP | Gravity.LEFT, 0, 0);



    }

    public void dismiss() {
        popupWindow.dismiss();
    }
}