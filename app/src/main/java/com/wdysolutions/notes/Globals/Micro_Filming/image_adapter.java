package com.wdysolutions.notes.Globals.Micro_Filming;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wdysolutions.notes.Globals.Micro_Filming.Fullscreen_image.Microfilming_fullscreen_img;
import com.wdysolutions.notes.R;

import java.util.ArrayList;

public class image_adapter extends RecyclerView.Adapter<image_adapter.MyHolder> {

    ArrayList<image_model> mdata;
    private Context context;
    private LayoutInflater inflater;

    public image_adapter(Context context, ArrayList<image_model> data){
        this.context = context;
        this.mdata = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.microfilming_image_row,viewGroup,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int position) {
        final String getRef_num = mdata.get(position).getRef_num();
        final String getType = mdata.get(position).getType();
        final String getUrl_img = mdata.get(position).getUrl_img();
        final String getValue = mdata.get(position).getValue();

        loadImages(myHolder, getUrl_img);
        myHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Microfilming_fullscreen_img.class);
                Bundle bundle = new Bundle();
                bundle.putInt("selected", position);
                bundle.putSerializable("data", mdata);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    private void loadImages(final MyHolder myHolder, final String getImg_path) {
        try {
            if (!getImg_path.equals("")) {
                myHolder.loading_img.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(getImg_path)
                        //.placeholder(R.drawable.no_image)
                        //.error(R.drawable.no_image)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                loadImages(myHolder, getImg_path);
                                return false; // important to return false so the error placeholder can be placed
                            }
                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                myHolder.loading_img.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(myHolder.img);
            }

        }catch (Exception e){}
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        ImageView img;
        ProgressBar loading_img;
        public MyHolder(View itemView) {
            super(itemView);
            loading_img = itemView.findViewById(R.id.loading_img);
            img = itemView.findViewById(R.id.img);
        }
    }
}
