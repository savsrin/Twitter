package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;
import android.util.Log;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@Parcel
public class Tweet {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    public String body;
    public String createdAt;
    public User user;
    public String mediaUrl;

    // empty constructor needed by the Parceler Library
    public Tweet() {}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("full_text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        if(!jsonObject.isNull("extended_entities")){
            JSONObject extendedEntities = jsonObject.getJSONObject("extended_entities");
            JSONArray jsonArray = extendedEntities.getJSONArray("media");
            JSONObject media = jsonArray.getJSONObject(0);
            tweet.mediaUrl = String.format("%s:large",media.getString("media_url_https"));
        }
        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
//    public String getRelativeTimeAgo(String rawJsonDate) {
//        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
//        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
//        sf.setLenient(true);
//
//        String relativeDate = "";
//        try {
//            long dateMillis = sf.parse(rawJsonDate).getTime();
//            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
//                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return relativeDate;
//    }

//    public String getRelativeTimeAgo(String rawJsonDate) {
//        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
//        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
//        sf.setLenient(true);
//
//        try {
//            long time = sf.parse(rawJsonDate).getTime();
//            long now = System.currentTimeMillis();
//
//            final long diff = now - time;
//            if (diff < MINUTE_MILLIS) {
//                return "just now";
//            } else if (diff < 2 * MINUTE_MILLIS) {
//                return "a minute ago";
//            } else if (diff < 50 * MINUTE_MILLIS) {
//                return diff / MINUTE_MILLIS + " m";
//            } else if (diff < 90 * MINUTE_MILLIS) {
//                return "an hour ago";
//            } else if (diff < 24 * HOUR_MILLIS) {
//                return diff / HOUR_MILLIS + " h";
//            } else if (diff < 48 * HOUR_MILLIS) {
//                return "yesterday";
//            } else {
//                return diff / DAY_MILLIS + " d";
//            }
//        } catch (ParseException e) {
//            Log.i("Tweet", "getRelativeTimeAgo failed");
//            e.printStackTrace();
//        }
//
//        return "";
//    }
public static String getTimeDifference(String rawJsonDate) {
    String time = "";
    String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
    SimpleDateFormat format = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
    format.setLenient(true);
    try {
        long diff = (System.currentTimeMillis() - format.parse(rawJsonDate).getTime()) / 1000;
        if (diff < 5)
            time = "Just now";
        else if (diff < 60)
            time = String.format(Locale.ENGLISH, "%ds",diff);
        else if (diff < 60 * 60)
            time = String.format(Locale.ENGLISH, "%dm", diff / 60);
        else if (diff < 60 * 60 * 24)
            time = String.format(Locale.ENGLISH, "%dh", diff / (60 * 60));
        else if (diff < 60 * 60 * 24 * 30)
            time = String.format(Locale.ENGLISH, "%dd", diff / (60 * 60 * 24));
        else {
            Calendar now = Calendar.getInstance();
            Calendar then = Calendar.getInstance();
            then.setTime(format.parse(rawJsonDate));
            if (now.get(Calendar.YEAR) == then.get(Calendar.YEAR)) {
                time = String.valueOf(then.get(Calendar.DAY_OF_MONTH)) + " "
                        + then.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US);
            } else {
                time = String.valueOf(then.get(Calendar.DAY_OF_MONTH)) + " "
                        + then.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US)
                        + " " + String.valueOf(then.get(Calendar.YEAR) - 2000);
            }
        }
    }  catch (ParseException e) {
        e.printStackTrace();
    }
    return time;
}
}



