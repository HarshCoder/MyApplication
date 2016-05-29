package com.applications.h.prajapati.myapplication;

/**
 * Created by Harsh on 2016-05-15.
 */
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*
 * Custom Adapter class that is responsible for holding the list of sites after they
 * get parsed out of XML and building row views to display them on the screen.
 */
public class SitesAdapter extends ArrayAdapter<StackSite> {


    public SitesAdapter(Context ctx, int textViewResourceId, List<StackSite> sites) {
        super(ctx, textViewResourceId, sites);

        //Setup the ImageLoader, we'll use this to display our images


    }


    /*
     * (non-Javadoc)
     * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
     *
     * This method is responsible for creating row views out of a StackSite object that can be put
     * into our ListView
     */
    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        RelativeLayout row = (RelativeLayout)convertView;
        Log.i("StackSites", "getView pos = " + pos);
        if(null == row){
            //No recycled View, we have to inflate one.
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = (RelativeLayout)inflater.inflate(R.layout.row_site, null);
        }

        //Get our View References
        TextView nameTxt = (TextView)row.findViewById(R.id.nameTxt);
        TextView startDateTxt = (TextView)row.findViewById(R.id.aboutTxt);
        TextView startTimeTxt = (TextView)row.findViewById(R.id.timeTxt);


        //Set the relavent text in our TextViews
      //  if(!(getItem(pos-1).getName().equals(getItem(pos).getName()))) {
        nameTxt.setText("EVENT NAME: " + getItem(pos).getName());
        startDateTxt.setText("DATE: " + getItem(pos).getStartDate());
        startTimeTxt.setText("      TIME: " + getItem(pos).getStartTime());

        //}

        return row;


    }

}