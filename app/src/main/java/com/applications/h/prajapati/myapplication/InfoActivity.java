package com.applications.h.prajapati.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.Document;

import org.codehaus.jackson.annotate.JsonProperty;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class InfoActivity extends AppCompatActivity {

    private int pos;
    private Button notify;
    private Button share;
    private CheckBox checkBox;
    private CloudantClient client;
    private Database db;
    private ExampleDocument doc;
    private TextView view;
    private Boolean hideBox;
    private TextView message;
    private static String attends = "Hi";
    private TextView eventName;
    private TextView sDate;
    private TextView sTime;
    private TextView eDate;
    private TextView eTime;
    private TextView description;
    private String[] months;
    private int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pos = getIntent().getExtras().getInt("position");
        Log.e("pos",String.valueOf(pos));
        final String name = getIntent().getExtras().getString("name");
        final String startDate = getIntent().getExtras().getString("startdate");
        final String startTime = getIntent().getExtras().getString("starttime");
        String endDate = getIntent().getExtras().getString("enddate");
        String endTime = getIntent().getExtras().getString("endtime");
        final String desc = getIntent().getExtras().getString("description");
        months = getResources().getStringArray(R.array.months);
        message = (TextView)findViewById(R.id.textView2);
        view = (TextView)findViewById(R.id.textView3);
        checkBox = (CheckBox)findViewById(R.id.checkBox);
        notify = (Button)findViewById(R.id.button);

        if(!MainActivity.isChecked(pos))
        {
            Log.e("Vishwa","Check");
            Log.e("Vishwa",String.valueOf(MainActivity.getSize()));
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setEnabled(true);
        }
        else
        {
            Log.e("Vishwa","UnCheck");
            checkBox.setVisibility(View.INVISIBLE);
            checkBox.setEnabled(false);
        }
        Log.e("Vishwa Check Again",String.valueOf(MainActivity.getSize()));

        if(!MainActivity.isNotified(pos))
        {
            notify.setVisibility(View.VISIBLE);
            notify.setEnabled(true);
        }
        else
        {
            notify.setEnabled(false);
            notify.setVisibility(View.INVISIBLE);
        }

        eventName = (TextView) findViewById(R.id.title);
        sDate = (TextView) findViewById(R.id.start_date);
        sTime = (TextView) findViewById(R.id.start_time);
        eDate = (TextView) findViewById(R.id.end_date);
        eTime = (TextView) findViewById(R.id.end_time);
        description = (TextView) findViewById(R.id.description);

        try {
            URL url = new URL("https://vishwapatel.cloudant.com");
            client = ClientBuilder.url(url).username("vishwapatel").password("petconnect").build();


        }catch (MalformedURLException e) {
            e.printStackTrace();
        }



        eventName.setText("EVENT NAME: "+name);
        sDate.setText("START DATE: " + startDate);
        sTime.setText("   START TIME: " + startTime);
        eDate.setText("END DATE: " + endDate);
        eTime.setText("   END TIME: " + endTime);
        description.setText("DESCRIPTION: " + desc);

        share = (Button) findViewById(R.id.button2);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"");
                shareIntent.putExtra(Intent.EXTRA_TEXT,name);
                startActivity(Intent.createChooser(shareIntent,"Share Via"));
            }
        });
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.addNotfiy(pos);
                Log.e("Vishwa", "Hello");
                String[] array = getComponents(startDate);
                String[] timeComp = getTime(startTime);

                Calendar calendar = Calendar.getInstance();
                for (int i = 0; i < months.length; i++) {
                    if (months[i].equals(array[0])) {
                        index = i;
                    }
                }
                if (calendar.get(Calendar.MONTH) < index) {
                    calendar.set(Calendar.MONTH, index);
                    Log.e("Month", String.valueOf(index));


                } else if (calendar.get(Calendar.MONTH) == index)
                {
                    if((calendar.get(Calendar.DAY_OF_MONTH) < Integer.parseInt(array[1]) - 1))
                    {
                        calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(array[1]) - 1);
                        Log.e("Day",String.valueOf(array[1]));

                        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeComp[0]));
                        Log.e("Hour",timeComp[0]);

                        calendar.set(Calendar.MINUTE, Integer.parseInt(timeComp[1]));
                        Log.e("Minute",timeComp[1]);
                        calendar.set(Calendar.SECOND, 0);
                        if(timeComp[2].equals("AM"))
                        {
                            calendar.set(Calendar.AM_PM,Calendar.AM);
                            Log.e("Am_Pm","AM");
                        }
                        else if(timeComp[2].equals("PM"))
                        {
                            calendar.set(Calendar.AM_PM,Calendar.PM);
                            Log.e("Am_Pm","PM");
                        }

                    }
                    else if((calendar.get(Calendar.DAY_OF_MONTH) <= Integer.parseInt(array[1]) - 1))
                    {
                        if((calendar.get(Calendar.AM_PM) == 0 && timeComp[2].equals("AM")) || ((calendar.get(Calendar.AM_PM) == 1) && timeComp[2].equals("PM")) || (calendar.get(Calendar.AM_PM) == 0 && timeComp[2].equals("PM")))
                        {
                            if(calendar.get(Calendar.HOUR) < Integer.parseInt(timeComp[0]))
                            {
                                calendar.set(Calendar.HOUR,Integer.parseInt(timeComp[0]));
                            }
                            if(calendar.get(Calendar.HOUR) == Integer.parseInt(timeComp[0]) && calendar.get(Calendar.MINUTE) < Integer.parseInt(timeComp[1]))
                            {
                                calendar.set(Calendar.MINUTE,Integer.parseInt(timeComp[1]));
                            }
                            else
                            {
                                Toast toast = Toast.makeText(getApplicationContext(),"Notifications are only 1 Day prior to Event 1 ",Toast.LENGTH_LONG);
                                toast.show();
                            }

                        }
                        else
                        {
                            Toast toast = Toast.makeText(getApplicationContext(),"Notifications are only 1 Day prior to Event 2 ",Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(),"Notifications are only 1 Day prior to Event 3 ",Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(),"Notifications are only 1 Day prior to Event 4 ",Toast.LENGTH_LONG);
                    toast.show();
                }


                Long time = calendar.getTimeInMillis();
                if(time > System.currentTimeMillis())
                {
                    Log.e("Time",time.toString());
                    Intent notification = new Intent(InfoActivity.this,Reciever.class);
                    AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    manager.set(AlarmManager.RTC_WAKEUP,time,PendingIntent.getBroadcast(InfoActivity.this,1,notification,PendingIntent.FLAG_UPDATE_CURRENT));
                    Toast toast = Toast.makeText(getApplicationContext(),"Notification Scheduled for " + array[0] + " " + String.valueOf(Integer.parseInt(array[1]) - 1) + " " + array[2] + " at " + startTime,Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(),"No Possibility",Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String url = "https://vishwapatel.cloudant.com/_users/";
                //ConnectivityManager connMgr = (ConnectivityManager)
                //      getSystemService(Context.CONNECTIVITY_SERVICE);
                //NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                ////if (networkInfo != null && networkInfo.isConnected()) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                checkBox.setVisibility(View.INVISIBLE);
                                MainActivity.addCheck(pos);
                                new DownloadWebpageTask().execute();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                checkBox.toggle();

                                break;
                        }
                    }
                };
                AlertDialog.Builder dialog = new AlertDialog.Builder(InfoActivity.this);
                dialog.setMessage("Are you sure you are attending this event?")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("Attending " + name + " event")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener)
                        .show();

                Log.e("Vishwa","Alert working");
            }
        });
        new RunTask().execute();


    }
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            db = client.database("petconnect",false);
            if(db == null)
            {
                Log.e("Vishwa","Error");
            }
            else
            {
                Log.e("Vishwa","Hi");
            }
            doc = db.find(ExampleDocument.class,"UserIdentity");
            doc.addAttendes(pos,SitesXmlPullParser.getStackSize());
            db.update(doc);



            Log.e("Vishwa", "Fine");

            return "Done";
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.e("Vishwa","Done");

        }

    }
    private class RunTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... urls) {
            db = client.database("petconnect",false);
            doc = db.find(ExampleDocument.class,"UserIdentity");

            attends = doc.getAttendes(pos);

            return "";
        }
        @Override
        protected void onPreExecute()
        {
            eventName.setVisibility(View.INVISIBLE);
            sDate.setVisibility(View.INVISIBLE);
            sTime.setVisibility(View.INVISIBLE);
            eDate.setVisibility(View.INVISIBLE);
            eTime.setVisibility(View.INVISIBLE);
            description.setVisibility(View.INVISIBLE);
            notify.setVisibility(View.INVISIBLE);
            share.setVisibility(View.INVISIBLE);
            message.setVisibility(View.INVISIBLE);
            if(checkBox.getVisibility() == View.VISIBLE)
            {
                checkBox.setVisibility(View.INVISIBLE);
                hideBox = true;
            }
            else
            {
                hideBox = false;
            }
            view.setVisibility(View.INVISIBLE);
        }
        @Override
        protected void onPostExecute(String result) {
            notify.setVisibility(View.VISIBLE);
            share.setVisibility(View.VISIBLE);
            if(hideBox == true)
            {
                checkBox.setVisibility(View.VISIBLE);
            }
            message.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
            eventName.setVisibility(View.INVISIBLE);
            sDate.setVisibility(View.VISIBLE);
            sTime.setVisibility(View.VISIBLE);
            eDate.setVisibility(View.VISIBLE);
            eTime.setVisibility(View.VISIBLE);
            description.setVisibility(View.VISIBLE);
            view.setText(attends);
            Log.e("Vishwa","Done");

        }
    }
    // Class to abstract the JSON document.
    public class ExampleDocument extends Document {
        @JsonProperty("age")
        private int age;
        @JsonProperty("userList")
        private ArrayList<String> userList;
        @JsonProperty("emails")
        private ArrayList<String> emails;
        @JsonProperty("passWords")
        private ArrayList<String> passWords;
        private ArrayList<String> attendes;
        public ExampleDocument()
        {
        }
        public void addAttendes(int index,int size)
        {
            if(attendes == null)
            {
                attendes = new ArrayList<>();
                for(int i=0; i<size;i++)
                {
                    attendes.add(i,String.valueOf(0));
                }
            }
            int attendents = Integer.parseInt(attendes.get(index));
            attendents++;
            attendes.add(index,String.valueOf(attendents));
            attendes.remove(index + 1);
        }
        public String getAttendes(int index)
        {
            return attendes.get(index);
        }
        public void addUsername(String userName) {
            if(userList == null)
            {
                userList = new ArrayList<>();
            }
            userList.add(userName);
        }
        public void addEmails(String addemails)
        {
            if(emails == null)
            {
                emails = new ArrayList<>();
            }
            emails.add(addemails);
        }
        public void addPassword(String password)
        {
            if(passWords == null)
            {
                passWords = new ArrayList<>();
            }
            passWords.add(password);
        }
        public int getAge() {
            return age;
        }
        public void setAge(int age){
            this.age = age;
        }
        public ArrayList<String> getEmails()
        {
            return emails;
        }
        public ArrayList<String> getUserList()
        {
            return userList;
        }
        public ArrayList<String> getPassWords()
        {
            return passWords;
        }
    }
    public static String getAttends()
    {
        return attends;
    }
    public String[] getComponents(String date)
    {
        StringBuilder builder = new StringBuilder(date);
        int index = builder.indexOf(",");
        builder.deleteCharAt(index);
        date = builder.toString();
        String[] components = date.split(" ");
        return components;
    }
    public String[] getTime(String time)
    {
        StringBuilder builder = new StringBuilder(time);
        int index = builder.indexOf(":");
        builder.deleteCharAt(index);
        builder.insert(index," ");
        time = builder.toString();
        String[] components = time.split(" ");
        return components;
    }
}
