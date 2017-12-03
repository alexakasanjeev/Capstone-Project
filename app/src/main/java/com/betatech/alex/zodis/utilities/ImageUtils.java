package com.betatech.alex.zodis.utilities;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.betatech.alex.zodis.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Code to load either cache or fetch user pic from url
 */

public class ImageUtils {

    public static void loadImage(final Context mContext, final ImageView imageView, final String imageUrl) {
        Picasso.with(mContext)
                .load(imageUrl)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .fit()
                .centerCrop()
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(mContext)
                                .load(imageUrl)
                                .error(R.drawable.ic_boy)
                                .fit()
                                .centerCrop()
                                .into(imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });
    }
}
