package com.example.awkow2x.myapplication1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.CategoryViewHolder> {
    private Context context;
    private ArrayList<Movie> listMovie;

    public MovieAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<Movie> getListMovie() {
        return listMovie;
    }

    public void setListMovie(ArrayList<Movie> listMovie) {
        this.listMovie = listMovie;
    }

    public int getCount() {
        return listMovie.size();
    }

    public Object getItem(int position) {
        return listMovie.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new CategoryViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.movName.setText(getListMovie().get(position).getName());
        holder.movDesc.setText(getListMovie().get(position).getDescription());
        //holder.movPhoto.setImageResource(getListMovie().get(position).getPhoto());
        Picasso.get()
                .load("https://image.tmdb.org/t/p/w185" + getListMovie().get(position).getImg())
                .into(holder.movPhoto);
    }

    @Override
    public int getItemCount() {
        return getListMovie().size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView movName;
        TextView movDesc;
        ImageView movPhoto;
        CategoryViewHolder(View itemView) {
            super(itemView);
            movName = itemView.findViewById(R.id.txt_name);
            movDesc = itemView.findViewById(R.id.txt_description);
            movPhoto = itemView.findViewById(R.id.img_photo);
        }
    }
}

