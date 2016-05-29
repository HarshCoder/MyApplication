package com.applications.h.prajapati.myapplication;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {

    private SitesAdapter mAdapter;
    private ListView sitesList;
    private ProgressBar bar;
    private TextView view;
    private static ArrayList<String> checked;
    private static ArrayList<String> notify;
    public static final String PREFS_NAME = "MyPrefsFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("StackSites", "OnCreate()");
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        Log.e("Vishwa", "Hi");

        bar = (ProgressBar)findViewById(R.id.progressBar);
        //Get reference to our ListView
        sitesList = (ListView)findViewById(R.id.sitesList);
        view = (TextView) findViewById(R.id.textView);
        //Set the click listener to launch the browser when a row is clicked.
        sitesList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                //String url = mAdapter.getItem(pos).getLink();
                Intent i = new Intent(MainActivity.this, InfoActivity.class);
                i.putExtra("position", pos);
                i.putExtra("name", mAdapter.getItem(pos).getName());
                i.putExtra("startdate", mAdapter.getItem(pos).getStartDate());
                i.putExtra("starttime", mAdapter.getItem(pos).getStartTime());
                i.putExtra("enddate", mAdapter.getItem(pos).getEndDate());
                i.putExtra("endtime", mAdapter.getItem(pos).getEndTime());
                i.putExtra("description", mAdapter.getItem(pos).getDescription());
                startActivity(i);
            }

        });

		/*
		 * If network is available download the xml from the Internet.
		 * If not then try to use the local file from last time.
		 */
        if(isNetworkAvailable() && !(MainActivity.getValue(getApplicationContext(),"download") == 1)){
            MainActivity.setValue(getApplicationContext(),"download",1);
            Log.i("StackSites", "starting download Task");
            SitesDownloadTask download = new SitesDownloadTask();
            download.execute();


        }else{
            MainActivity.loadArray(getApplicationContext());
            MainActivity.loadNotify(getApplicationContext());
            bar.setVisibility(View.INVISIBLE);
            bar.setEnabled(false);
            view.setVisibility(View.INVISIBLE);
            view.setEnabled(false);
            mAdapter = new SitesAdapter(getApplicationContext(), -1, SitesXmlPullParser.getStackSitesFromFile(MainActivity.this));
            sitesList.setAdapter(mAdapter);
        }
    }

    //Helper method to determine if Internet connection is available.
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /*
     * AsyncTask that will download the xml file for us and store it locally.
     * After the download is done we'll parse the local file.
     */
    private class SitesDownloadTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... arg0) {
            //Download the file
            try {
                Downloader.DownloadFromUrl("http://wx.toronto.ca/festevents.nsf/tpaview?readviewentries", openFileOutput("StackSites.xml", Context.MODE_PRIVATE));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPreExecute()
        {
            bar.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(Void result){
            bar.setVisibility(View.INVISIBLE);
            view.setVisibility(View.INVISIBLE);

            //setup our Adapter and set it to the ListView.
            mAdapter = new SitesAdapter(MainActivity.this, -1, SitesXmlPullParser.getStackSitesFromFile(MainActivity.this));
            sitesList.setAdapter(mAdapter);
            Log.i("StackSites", "adapter size = "+ mAdapter.getCount());
            if(checked == null)
            {
                Log.e("Vishwa","null");
                checked = new ArrayList<>(SitesXmlPullParser.getStackSize());
                for(int i=0; i<SitesXmlPullParser.getStackSize();i++)
                {
                    checked.add(i,String.valueOf(0));
                }
            }

        }
    }
    public static boolean isChecked(int pos)
    {
        if(checked == null || checked.size() == 0)
        {
            Log.e("Vishwa","null");
            checked = new ArrayList<>(SitesXmlPullParser.getStackSize());
            Log.e("Vishwa Stack Size",String.valueOf(SitesXmlPullParser.getStackSize()));
            for(int i=0; i<SitesXmlPullParser.getStackSize();i++)
            {
                checked.add(i,String.valueOf(0));
            }
        }
        if(checked.get(pos).equals("0"))
        {
            Log.e("Vishwa",String.valueOf(checked.get(pos)));
            return false;
        }
        return true;
    }
    public static boolean isNotified(int pos)
    {
        if(notify == null || notify.size() == 0)
        {
            Log.e("Vishwa","null");
            notify = new ArrayList<>(SitesXmlPullParser.getStackSize());
            for(int i=0; i<SitesXmlPullParser.getStackSize();i++)
            {
                notify.add(i,String.valueOf(0));
            }
        }
        if(notify.get(pos).equals("0"))
        {
            return false;
        }
        return true;
    }
    public static void addCheck(int pos)
    {
        int checkNo = Integer.parseInt(checked.get(pos));
        checkNo++;
        checked.add(pos,String.valueOf(checkNo));
        checked.remove(pos+1);
    }
    public static void addNotfiy(int pos)
    {
        int notifyNo = Integer.parseInt(notify.get(pos));
        notifyNo++;
        notify.add(pos + SitesXmlPullParser.getStackSize(),String.valueOf(notifyNo));
        notify.remove(pos+1);
    }
    private static int getValue(Context context, String key)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME,context.MODE_PRIVATE);
        int value = settings.getInt(key,0);
        return value;
    }
    private static void setValue(Context context,String key,int value)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        // Commit the edits
        editor.apply();
    }
    public static boolean saveArray(Context context)
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor mEdit1 = sp.edit();
    /* checked is an array */
        mEdit1.putInt("array", checked.size());

        for(int i=0;i<checked.size();i++)
        {
            mEdit1.remove("miniArray" + i);
            mEdit1.putString("miniArray" + i, checked.get(i));
        }

        return mEdit1.commit();
    }
    public static void loadArray(Context context)
    {
        SharedPreferences mSharedPreference1 = PreferenceManager.getDefaultSharedPreferences(context);
        int size = mSharedPreference1.getInt("array", 0);
        checked = new ArrayList<>(size);
        for(int i=0;i<size;i++)
        {
            checked.add(mSharedPreference1.getString("miniArray" + i, null));
        }

    }
    public static boolean saveNotifyArray(Context context)
    {
        SharedPreferences sp1 = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor mEdit1 = sp1.edit();
    /* checked is an array */
        mEdit1.putInt("notifyArray", notify.size());

        for(int i=0;i<notify.size();i++)
        {
            mEdit1.remove("notify" + i);
            mEdit1.putString("notify" + i, notify.get(i));
        }

        return mEdit1.commit();
    }    public static void loadNotify(Context context)
    {
        SharedPreferences mSharedPreference2 = PreferenceManager.getDefaultSharedPreferences(context);
        int size = mSharedPreference2.getInt("notifyArray", 0);
        notify = new ArrayList<>(size);
        for(int i=0;i<size;i++)
        {
            notify.add(mSharedPreference2.getString("notify" + i, null));
        }

    }
    protected void onDestroy()
    {
        super.onDestroy();
        MainActivity.saveArray(getApplicationContext());
        MainActivity.saveNotifyArray(getApplicationContext());
    }
    public static int getSize()
    {
        return checked.size();
    }
}