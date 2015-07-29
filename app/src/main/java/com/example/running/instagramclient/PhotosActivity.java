package com.example.running.instagramclient;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;


import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PhotosActivity extends Activity {
    public static final String CLIENT_ID = "789324fc82a049bfa16fd58c9e244231";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;
    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setLogo(R.mipmap.ic_launcher);
        getActionBar().setDisplayUseLogoEnabled(true);
        photos = new ArrayList<InstagramPhoto>();
        aPhotos = new InstagramPhotosAdapter(this, photos);
        ListView lvPhotos = (ListView)findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(aPhotos);
        fetchPopularPhotos();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPopularPhotos();

            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void fetchPopularPhotos(){
       String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                aPhotos.clear();
                JSONArray photosJSON = null;
                try {
                    photosJSON = response.getJSONArray("data");
                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.setUsername(photoJSON.getJSONObject("user").getString("username"));
                        if (photoJSON.optJSONObject("caption") != null) {
                            photo.setCaption(photoJSON.getJSONObject("caption").getString("text"));
                        }
                        photo.setImageUrl(photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url"));
                        photo.setImageHeight(photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height"));
                        photo.setLikesCount(photoJSON.getJSONObject("likes").getInt("count"));
                        photo.setProfilePicture(photoJSON.getJSONObject("user").getString("profile_picture"));
                        photo.setRelativeTime(relativeTime(photoJSON.getLong("created_time")));

                        JSONArray comments = photoJSON.optJSONObject("comments").getJSONArray("data");
                        JSONObject comment2 = comments.getJSONObject(comments.length() - 2);
                        String comment2Str = "<font color=\"#4fa0c6\">"+comment2.optJSONObject("from").getString("username") +"</font>    "+comment2.getString("text");

                        JSONObject comment1 = comments.getJSONObject(comments.length() - 1);
                        String comment1Str = "<font color=\"#4fa0c6\">"+comment1.optJSONObject("from").getString("username") +"</font>    "+comment1.getString("text");

                        List<String> commentList = new ArrayList<>();
                        commentList.add(comment2Str);
                        commentList.add(comment1Str);

                        photo.setComments(commentList);

                        photos.add(photo);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    ;
                }
                aPhotos.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);


            }

            private String relativeTime(Long createTime) {
               return DateUtils.getRelativeTimeSpanString(createTime * 1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
