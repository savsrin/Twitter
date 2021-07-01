package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.databinding.ItemTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
    // pass in the context and list of tweets
    Context context;
    List<Tweet> tweets;

    public TweetsAdapter(Context context, List<Tweet> tweets) {
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

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }


    //define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemTweetBinding itemTweetBinding;


        public ViewHolder(@NonNull ItemTweetBinding itemTweetBinding) {
            super(itemTweetBinding.getRoot());
            this.itemTweetBinding = itemTweetBinding;
        }

        public void bind(Tweet tweet) {
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
