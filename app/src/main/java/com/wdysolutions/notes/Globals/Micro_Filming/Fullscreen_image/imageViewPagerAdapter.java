package com.wdysolutions.notes.Globals.Micro_Filming.Fullscreen_image;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.wdysolutions.notes.Globals.Micro_Filming.image_model;
import com.wdysolutions.notes.R;

import java.util.ArrayList;

public class imageViewPagerAdapter extends PagerAdapter {

    Context context;
    LayoutInflater mLayoutInflater;
    ArrayList<image_model> data;

    public imageViewPagerAdapter(Context context, ArrayList<image_model> image_models){
        this.data = image_models;
        this.context = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.microfilming_pager_item, container, false);

        PhotoView imageView = itemView.findViewById(R.id.imageView);
        loadImage(imageView, data.get(position).getUrl_img());

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }

    private void loadImage(final ImageView imageView, final String getImg_path) {
        try {
            if (!getImg_path.equals("")) {
                Glide.with(context)
                        .load(getImg_path)
                        .placeholder(R.drawable.no_image)
                        .error(R.drawable.no_image)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                // log exception«
                                loadImage(imageView, getImg_path);
                                return false; // important to return false so the error placeholder can be placed
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                return false;
                            }
                        })
                        .into(imageView);
            }
        }catch (Exception e){}
    }
}
