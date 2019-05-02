package com.example.awkow2x.myapplication1;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TVFragment extends Fragment {


    private RecyclerView rvCategory;
    private ArrayList<Movie> list = new ArrayList<>();
    private String[] dataName;
    private String[] dataDescription;
    private String[] dataYear;
    private String[] dataScore;
    private int[] dataPhoto;
    private int errorS = 0;
    private int succesS = 0;
    private int a = 0;
    private int b = 0;
    private MovieAdapter adapter;
    private ArrayList<Movie> movies;

    public TVFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setTitle(R.string.loading);
        dialog.setMessage(getString(R.string.loading_msg));
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.show();

        AndroidNetworking.initialize(getActivity());

        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            AndroidNetworking.get("https://api.themoviedb.org/3/discover/tv?api_key=" + Config.API_KEY + "&language=en-US")
                    .setTag("test")
                    .setPriority(Priority.HIGH)
                    .getResponseOnlyFromNetwork()
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray movieArr = response.getJSONArray("results");
                                for (int a = 0; a < 10; a++) {
                                    Movie movie = new Movie();
                                    movie.setName(movieArr.getJSONObject(a).getString("original_name"));
                                    movie.setScore(movieArr.getJSONObject(a).getString("vote_average"));
                                    movie.setYear(movieArr.getJSONObject(a).getString("first_air_date"));
                                    movie.setDescription(movieArr.getJSONObject(a).getString("overview"));
                                    movie.setImg(movieArr.getJSONObject(a).getString("poster_path"));
                                    movie.setId(movieArr.getJSONObject(a).getString("id"));
                                    movie.setType("tv");
                                    list.add(movie);
                                    showRecyclerList();
                                    if (a == 9) {
                                        dialog.dismiss();
                                    }
                                }
                            } catch (JSONException e) {
                                dialog.dismiss();
                                if (b == 0) {
                                    new AlertDialog.Builder(getContext())
                                            .setTitle("Error")
                                            .setMessage(R.string.error_msg)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            })
                                            .show();
                                    b = 1;
                                }
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                            //errorS = 1;
                            dialog.dismiss();
                            if (b == 0) {
                                new AlertDialog.Builder(getContext())
                                        .setTitle("Error")
                                        .setMessage(R.string.error_msg)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .show();
                                b = 1;
                            }
                        }
                    });
        }
        else {
            dialog.dismiss();
            new AlertDialog.Builder(getContext())
                    .setTitle("Error")
                    .setMessage(R.string.error_msg)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }

        rvCategory = view.findViewById(R.id.tv_list);
        rvCategory.setHasFixedSize(true);
    }

    private void showRecyclerList(){
        rvCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        MovieAdapter listMovieAdapter = new MovieAdapter(getContext());
        listMovieAdapter.setListMovie(list);
        rvCategory.setAdapter(listMovieAdapter);

        ItemClickSupport.addTo(rvCategory).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedMovie(list.get(position));
            }
        });
    }

    private void showSelectedMovie(Movie movie){
        movie.setPhoto(movie.getPhoto());
        movie.setName(movie.getName());
        movie.setYear(movie.getYear());
        movie.setScore(movie.getScore());
        movie.setId(movie.getId());
        movie.setType(movie.getType());
        movie.setDescription(movie.getDescription());

        Intent moveWithObjectIntent = new Intent(getActivity(), DetailActivity.class);
        moveWithObjectIntent.putExtra(DetailActivity.EXTRA_MOVIE, movie);
        startActivity(moveWithObjectIntent);
    }
}
