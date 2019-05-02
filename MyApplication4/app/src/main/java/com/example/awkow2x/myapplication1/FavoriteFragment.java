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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.net.ConnectivityManagerCompat;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {

    private RecyclerView rvCategory;
    private ArrayList<Movie> list = new ArrayList<>();
    private int a = 0;
    private int b = 0;
    private String keyName;
    private String keyDate;
    private String type;
    private String url;

    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Realm.init(getActivity());
        final Realm realm = Realm.getDefaultInstance();

        AndroidNetworking.initialize(getActivity());

        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        final RealmResults<RealmData> results = realm.where(RealmData.class).findAll();
        /*StringBuilder stringTes = new StringBuilder();
        for (int i = 0; i < results.size(); i++) {
            stringTes.append(results.get(i).getId());
        }*/

        if (results.size() == 0) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Error")
                    .setMessage(R.string.no_fav)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
        else {
            final ProgressDialog dialog = new ProgressDialog(getContext());
            dialog.setTitle(R.string.loading);
            dialog.setMessage(getString(R.string.loading_msg));
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();

            for (int i = 0; i < results.size(); i++) {
                a = i;
                url = "";
                keyName = "";
                keyDate = "";
                type = results.get(i).getType();

                String id = results.get(i).getId();
                if (type.equals("movie")) {
                    url = "https://api.themoviedb.org/3/movie/";
                } else if (type.equals("tv")) {
                    url = "https://api.themoviedb.org/3/tv/";
                }

                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    AndroidNetworking.get(url + id + "?api_key=" + Config.API_KEY + "&language=en-US")
                            .setTag("test")
                            .setPriority(Priority.HIGH)
                            .getResponseOnlyFromNetwork()
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                String tes = type;

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (tes.equals("movie")) {
                                            keyName = "original_title";
                                            keyDate = "release_date";
                                        } else if (tes.equals("tv")) {
                                            keyName = "original_name";
                                            keyDate = "first_air_date";
                                        }

                                        Movie movie = new Movie();
                                        movie.setName(response.getString(keyName));
                                        movie.setScore(response.getString("vote_average"));
                                        movie.setYear(response.getString(keyDate));
                                        movie.setDescription(response.getString("overview"));
                                        movie.setImg(response.getString("poster_path"));
                                        movie.setId(response.getString("id"));
                                        movie.setType(type);
                                        //movie.setPhoto(R.drawable.poster_arrow);
                                        list.add(movie);
                                        showRecyclerList();
                                        if (a == results.size() - 1) {
                                            dialog.dismiss();
                                        }
                                        //list.addAll(movie);
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
                } else {
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
                    break;
                }
            }
        }


        rvCategory = view.findViewById(R.id.fav_list);
        rvCategory.setHasFixedSize(true);
    }

    private void showRecyclerList(){
        rvCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        MovieAdapter listHeroAdapter = new MovieAdapter(getContext());
        listHeroAdapter.setListMovie(list);
        rvCategory.setAdapter(listHeroAdapter);

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
