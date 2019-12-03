package com.kuruvatech.bescom.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.kuruvatech.bescom.MainActivity;
import com.kuruvatech.bescom.R;
//import com.kuruvatech.bescom.adapter.FeedAdapter;
//import com.kuruvatech.bescom.adapter.ScreenSlidePagerAdapter;
import com.kuruvatech.bescom.model.FeedItem2;
import com.kuruvatech.bescom.model.ShiftTimings;
//import com.kuruvatech.bescom.utils.CirclePageIndicator;
import com.kuruvatech.bescom.utils.Constants;
import com.kuruvatech.bescom.utils.MyViewPager;
import com.kuruvatech.bescom.utils.SessionManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;




public class MainFragment extends Fragment {

    private static final String TAG_FEEDERNO = "feederno";
    private static final String TAG_FEEDERNAME = "feedername";
    private static final String  TAG_TIMINGS = "timings";

    private static final String TAG_NIGHTSHIFT = "nightshift";
    private static final String TAG_DAYSHIFT = "dayshift";
    private static final String TAG_STARTTIME = "startTime";
    private static final String TAG_ENDTIME = "endTime";
    private static final String TAG_AVAILABLE = "available";


    Button btnshareApp;
    ArrayList<FeedItem2> feedList;
    ArrayList<String> scrollimages;
   // FeedAdapter adapter;
   // MainAdapter adapter2;
    View rootview;
    //ListView listView;
   // RecyclerView listView;
    TextView noFeedstv;
    boolean isSwipeRefresh;
    private SwipeRefreshLayout swipeRefreshLayout;
    SessionManager session;
    private MyViewPager pager;
    int sliderIndex=0,sliderMaxImages = 4;
    int delayMiliSec = 8000;
	private Handler handler;
  //  ScreenSlidePagerAdapter pagerAdapter;
   // CirclePageIndicator indicator;
    CardView video_cardview;
    boolean isResponsereceived = true;
    private AdView mAdView;
    private PublisherAdView mPublisherAdView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MobileAds.initialize(getActivity(), Constants.ADMOBAPPID);
        rootview = inflater.inflate(R.layout.fragment_main, container, false);

        //to use RecycleView, you need a layout manager. default is LinearLayoutManager
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
     //   listView.setLayoutManager(linearLayoutManager);
        mAdView = rootview.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
       // adRequest.setTesting(true);
      //  mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        mAdView.loadAd(adRequest);

        session = new SessionManager(getActivity().getApplicationContext());
        swipeRefreshLayout = (SwipeRefreshLayout) rootview.findViewById(R.id.swipe_refresh_layout);
        ((MainActivity) getActivity())
                .setActionBarTitle(getString(R.string.titletext));

        isSwipeRefresh = false;
        feedList = new ArrayList<FeedItem2>();
        scrollimages = new ArrayList<String>();


      //  feedList  =session.getLastNewsFeed();
//        if(feedList !=null)
//        {
//            initAdapter();
//        }




        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
//                if(isResponsereceived) {
//                    isSwipeRefresh = true;
//                    isResponsereceived = false;
                    getFeeds();
              //  }
            }

        });

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, R.color.colorAccent, R.color.colorPrimaryDark);
        swipeRefreshLayout.setProgressBackgroundColor(android.R.color.transparent);

        initfromsession();
        getFeeds();
        // Inflate the layout for this fragment
       // MobileAds.initialize(getActivity(), Constants.ADMOBAPPID);
         TableLayout table =  (TableLayout) rootview.findViewById(R.id.table_layout);

        table.setStretchAllColumns(true);
        table.setShrinkAllColumns(true);

        TableRow rowTitle = new TableRow(getActivity());
        TableRow rowTitle2 = new TableRow(getActivity());
        TextView title = new TextView(getActivity());
        title.setText("Chiluru Feeders Table");

        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(Typeface.SERIF, Typeface.BOLD);

        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.span = 2;

        rowTitle.addView(title, params);
        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);
       // rowTitle2.addView("", params);
        rowTitle2.setGravity(Gravity.CENTER_HORIZONTAL);
        View v = new View(getActivity());
        v.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 50));
        table.addView(v);
        table.addView(rowTitle);
        View v2 = new View(getActivity());
        v2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 50));
        table.addView(v2);
       // table.addView(v);
        //table.addView(v);

        for(int i = 0 ; i < feedList.size() ; i++)
        {
            TableRow tr = new TableRow(getActivity());
            TextView tv1 = new TextView(getActivity());
            tv1.setText("  "+ feedList.get(i).getFeedno() + " ( " +feedList.get(i).getFeedname() + " ) " );
            tv1.setTypeface(Typeface.DEFAULT_BOLD);
            tr.addView(tv1);
            TextView tv2 = new TextView(getActivity());
//            tv2.setText(feedList.get(i).getFeedname());
            tv2.setTypeface(Typeface.DEFAULT_BOLD);
            tr.addView(tv2);
            TextView tv3 = new TextView(getActivity());
            tv3.setText(feedList.get(i).getShiftTimings().timings());
            tv3.setTypeface(Typeface.DEFAULT_BOLD);
            tr.addView(tv3);
            table.addView(tr);
            View v3 = new View(getActivity());
            v3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 30));
            table.addView(v3);


        }
    return rootview;
    }



    @Override
    public void onResume() {
        super.onResume();
//        handler.postDelayed(runnable, delayMiliSec);
    }


    public void initfromsession()
    {
        String data  = session.getLastNewsFeed();

        if(data == null)
        {
            return;
        }


        try {
//            JSONObject feed_object2 = new JSONObject(data);
//            JSONArray feed_object22 = new JSONArray(data);
        //    if (feed_object2.has(TAG_FEEDS)) {
                //feed_object2.getString(TAG_FEEDS);
                JSONArray feedsarray = new JSONArray(data);

                ArrayList<FeedItem2> lfeedList = new ArrayList<FeedItem2>();
                for (int i = 0 ; i < feedsarray.length() ; i++) {
                    JSONObject feed_object = feedsarray.getJSONObject(i);
                    FeedItem2 feedItem2 = new FeedItem2();
                    if (feed_object.has(TAG_FEEDERNAME)) {
                        feedItem2.setFeedname(feed_object.getString(TAG_FEEDERNAME));
                    }
                    if (feed_object.has(TAG_FEEDERNO)) {
                        feedItem2.setFeedno(feed_object.getString(TAG_FEEDERNO));
                    }
                    if (feed_object.has(TAG_TIMINGS)) {
                        JSONObject feed_timings = new JSONObject(feed_object.getString(TAG_TIMINGS));
                        ShiftTimings st = new ShiftTimings();
                        if(feed_timings.has(TAG_DAYSHIFT))
                        {
                            JSONObject feed_time = new JSONObject(feed_timings.getString(TAG_DAYSHIFT));
                            if(feed_time.has(TAG_STARTTIME))
                            {
                                st.setDayshift_starttime(feed_time.getString(TAG_STARTTIME));
                            }
                            if(feed_time.has(TAG_ENDTIME))
                            {
                                st.setDayshift_endtime(feed_time.getString(TAG_ENDTIME));
                            }
                            if(feed_time.has(TAG_AVAILABLE))
                            {
                                if(feed_time.getString(TAG_AVAILABLE).equals("Yes"))
                                {
                                    st.setDayshift_on(true);
                                }
                                else {
                                    st.setDayshift_on(false);
                                }
                            }
                        }
                        if(feed_timings.has(TAG_NIGHTSHIFT))
                        {
                            JSONObject feed_time = new JSONObject(feed_timings.getString(TAG_NIGHTSHIFT));
                            if(feed_time.has(TAG_STARTTIME))
                            {
                                st.setNightshift_starttime(feed_time.getString(TAG_STARTTIME));
                            }
                            if(feed_time.has(TAG_ENDTIME))
                            {
                                st.setNightshift_endtime(feed_time.getString(TAG_ENDTIME));
                            }
                            if(feed_time.has(TAG_AVAILABLE))
                            {
                                if(feed_time.getString(TAG_AVAILABLE).equals("Yes"))
                                {
                                    st.setNightshift_on(true);
                                }
                                else {
                                    st.setNightshift_on(false);
                                }
                            }
                        }
                        feedItem2.setShiftTimings(st);
                    }

                    lfeedList.add(feedItem2);
                }
                feedList.clear();
                feedList.addAll(lfeedList);


          //  initAdapter();
        }
        catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void getFeeds()
    {
        String getFeedsUrl = "http://bescom.herokuapp.com/v1/vendor/menu/krv";//Constants.GET_FEEDS_URL;
      //  getFeedsUrl = getFeedsUrl + Constants.USERNAME;
        new JSONAsyncTask().execute(getFeedsUrl);
    }


public  class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {
        Dialog dialog;
        public JSONAsyncTask() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(isSwipeRefresh == false) {
                swipeRefreshLayout.setRefreshing(true);
//                dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.custom_progress_dialog);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                dialog.show();
//                dialog.setCancelable(true);
            }

        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                //------------------>>
                HttpGet request = new HttpGet(urls[0]);
//                request.addHeader(Constants.SECUREKEY_KEY, Constants.SECUREKEY_VALUE);
//                request.addHeader(Constants.VERSION_KEY, Constants.VERSION_VALUE);
//                request.addHeader(Constants.CLIENT_KEY, Constants.CLIENT_VALUE);

                HttpClient httpclient = new DefaultHttpClient();

                HttpResponse response = httpclient.execute(request);

                int status = response.getStatusLine().getStatusCode();


                ///feedList.clear();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();

                    String data = EntityUtils.toString(entity, HTTP.UTF_8);
                    session.setLastNewsFeed(data);
                    JSONArray feedsarray = new JSONArray(data);

                    ArrayList<FeedItem2> lfeedList = new ArrayList<FeedItem2>();
                    for (int i = 0 ; i < feedsarray.length() ; i++) {
                            JSONObject feed_object = feedsarray.getJSONObject(i);
                            FeedItem2 feedItem2 = new FeedItem2();
                            if (feed_object.has(TAG_FEEDERNAME)) {
                                feedItem2.setFeedname(feed_object.getString(TAG_FEEDERNAME));
                            }
                            if (feed_object.has(TAG_FEEDERNO)) {
                                feedItem2.setFeedno(feed_object.getString(TAG_FEEDERNO));
                            }
                            if (feed_object.has(TAG_TIMINGS)) {
                                JSONObject feed_timings = new JSONObject(feed_object.getString(TAG_TIMINGS));
                                ShiftTimings st = new ShiftTimings();
                                if(feed_timings.has(TAG_DAYSHIFT))
                                {
                                    JSONObject feed_time = new JSONObject(feed_timings.getString(TAG_DAYSHIFT));
                                    if(feed_time.has(TAG_STARTTIME))
                                    {
                                        st.setDayshift_starttime(feed_time.getString(TAG_STARTTIME));
                                    }
                                    if(feed_time.has(TAG_ENDTIME))
                                    {
                                        st.setDayshift_endtime(feed_time.getString(TAG_ENDTIME));
                                    }
                                    if(feed_time.has(TAG_AVAILABLE))
                                    {
                                        if(feed_time.getString(TAG_AVAILABLE).equals("Yes"))
                                        {
                                            st.setDayshift_on(true);
                                        }
                                        else {
                                            st.setDayshift_on(false);
                                        }
                                    }
                                }
                                if(feed_timings.has(TAG_NIGHTSHIFT))
                                {
                                    JSONObject feed_time = new JSONObject(feed_timings.getString(TAG_NIGHTSHIFT));
                                    if(feed_time.has(TAG_STARTTIME))
                                    {
                                        st.setNightshift_starttime(feed_time.getString(TAG_STARTTIME));
                                    }
                                    if(feed_time.has(TAG_ENDTIME))
                                    {
                                        st.setNightshift_endtime(feed_time.getString(TAG_ENDTIME));
                                    }
                                    if(feed_time.has(TAG_AVAILABLE))
                                    {
                                        if(feed_time.getString(TAG_AVAILABLE).equals("Yes"))
                                        {
                                            st.setNightshift_on(true);
                                        }
                                        else {
                                            st.setNightshift_on(false);
                                        }
                                    }
                                }
                                feedItem2.setShiftTimings(st);
                            }

                            lfeedList.add(feedItem2);
                        }
                        feedList.clear();
                        feedList.addAll(lfeedList);

                    return true;
                }
           } catch (IOException e) {

                e.printStackTrace();

            }
            catch (Exception e) {

                e.printStackTrace();
            }

            return false;

        }
        protected void onPostExecute(Boolean result) {
//            if(dialog != null && isSwipeRefresh ==false)
//                dialog.cancel();

            if(swipeRefreshLayout != null)
             swipeRefreshLayout.setRefreshing(false);
            isSwipeRefresh = false;
            isResponsereceived = true;
            if(getActivity() != null) {
                if (result == false) {

                   // Toast.makeText(getActivity().getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
                    alertMessage("Unable to fetch data from server");
                } else {
                //    initAdapter();

                }
            }

        }
    }
    public void alertMessage(String message) {
        DialogInterface.OnClickListener dialogClickListeneryesno = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {

                    case DialogInterface.BUTTON_NEUTRAL:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(message).setNeutralButton("Ok", dialogClickListeneryesno)
                .setIcon(R.drawable.ic_action_about).show();

    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
