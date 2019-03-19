package com.example.ahmed.topartist;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ahmed.topartist.data.CustomListviewAdapter;
import com.example.ahmed.topartist.model.Artist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private CustomListviewAdapter adapter;
    private ArrayList<Artist> artists = new ArrayList<>();
    private ListView listView;
   private SwipeRefreshLayout swipeRefreshLayout;
    private String url ="http://ws.audioscrobbler.com/2.0/?method=chart.gettopartists&api_key=aa0388043375257aab27d92414a7383f&format=json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        getEvents();
                                    }
                                }
        );

        }

    private  void listData(){
        adapter = new CustomListviewAdapter(MainActivity.this, R.layout.list_row, artists);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void getEvents() {
        //clear data first
        artists.clear();
        // showing refresh animation before making http call
        swipeRefreshLayout.setRefreshing(true);

        JsonObjectRequest eventsRequest = new JsonObjectRequest(Request.Method.GET,
                url, (JSONObject)null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {
                    JSONObject eventsObject = response.getJSONObject("artists");
                    Log.v("data",eventsObject.toString());


                    JSONArray eventsArray = eventsObject.getJSONArray("artist");

                    for (int i = 0; i < eventsArray.length(); i++) {
                        JSONObject jsonObject = eventsArray.getJSONObject(i);

                        //Get artist object

                       String artistName= jsonObject.getString("name");
                        String playcount= jsonObject.getString("playcount");
                        String listener= jsonObject.getString("listeners");

                        //Get url image
                        JSONArray imageArray = jsonObject.getJSONArray("image");
                        //Get image
                        JSONObject largeImage = imageArray.getJSONObject(3);
                        //Get actual image url
                        String image = largeImage.getString("#text");
                        //---------------------------------------------------------
                        Artist artist=new Artist();
                        artist.setArtistName(artistName);
                        artist.setPlayCount(playcount);
                        artist.setListeners(listener);
                        artist.setArtistIamge(image);
                        artists.add(artist);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listData();
                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    Log.d("Error Response code" ,"Error Response code: "+ error.networkResponse.statusCode);
                }
                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);


            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 1 * 60 * 1000; // in 2 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    /* If the response is taking from server using String Request then just Replace the Line

                      return Response.success(new JSONObject(jsonString), cacheEntry);
                      with this
                      return Response.success(new String(jsonString), cacheEntry); */
                    return Response.success(new JSONObject(jsonString), cacheEntry);
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }};
        AppController.getInstance().addToRequestQueue(eventsRequest);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
     getEvents();
    }
}
