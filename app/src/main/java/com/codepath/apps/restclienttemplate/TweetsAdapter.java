package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.databinding.ItemTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.List;

import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
    private final int REQUEST_CODE = 20;
    Context context;
    List<Tweet> tweets;

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        // pass in the context and list of tweets
        this.context = context;
        this.tweets = tweets;
    }

    @NonNull
    @Override
    // for each row, inflate the layout
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ItemTweetBinding binding = ItemTweetBinding.inflate(LayoutInflater.from(context),
                                     parent, false);
        return new ViewHolder(binding);
    }

    // bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        // get the data at the position
        Tweet tweet = tweets.get(position);
        // bind the tweet with view holder
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // Helper method to clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }


    // Define a ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemTweetBinding itemTweetBinding;

        public ViewHolder(@NonNull ItemTweetBinding itemTweetBinding) {
            super(itemTweetBinding.getRoot());
            this.itemTweetBinding = itemTweetBinding;
        }

        public void bind(Tweet tweet) {
            itemTweetBinding.ibReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != -1) {
                        // Get position of tweet being replied to in RV
                        Tweet tweet = tweets.get(position);
                        Intent intent = new Intent(context, ReplyActivity.class);
                        // Send user screen name to ReplyActivity class for the @
                        intent.putExtra("userHandle", tweet.user.screenName);
                        ((TimelineActivity) context).startActivityForResult(intent, REQUEST_CODE);
                    }
                }
            });

            // nitialize and display fields in itemTweet in the RV
            itemTweetBinding.tvBody.setText(tweet.body);
            String nameString = "<b>" + tweet.user.name + "</b> " + tweet.user.screenName;
            itemTweetBinding.tvDisplayName.setText(Html.fromHtml(nameString));
            itemTweetBinding.tvTimeStamp.setText(tweet.getTimeDifference(tweet.createdAt));
            Glide.with(context).load(tweet.user.profileImageUrl).into(itemTweetBinding.ivProfileImage);
            if (tweet.mediaUrl != null) {
                itemTweetBinding.ivEmbeddedImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(tweet.mediaUrl).into(itemTweetBinding.ivEmbeddedImage);

            } else {
                itemTweetBinding.ivEmbeddedImage.setVisibility(View.GONE);
            }

        }

    }


}



