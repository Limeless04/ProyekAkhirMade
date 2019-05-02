package com.example.awkow2x.myapplication1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE = "extra_movie";

    TextView nameObject;
    TextView descriptionObject;
    TextView yearObject;
    TextView scoreObject;
    ImageView imageObject;
    Button favButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Realm.init(this);
        final Realm realm = Realm.getDefaultInstance();

        nameObject = findViewById(R.id.name_detail);
        descriptionObject = findViewById(R.id.description_detail);
        yearObject = findViewById(R.id.year_detail);
        scoreObject = findViewById(R.id.score_detail);
        imageObject = findViewById(R.id.image_detail);
        favButton = findViewById(R.id.fav_button);

        final Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        nameObject.setText((movie.getName()));
        descriptionObject.setText(movie.getDescription());
        yearObject.setText(movie.getYear());
        scoreObject.setText(movie.getScore());
        //imageObject.setImageResource(movie.getPhoto());
        Picasso.get()
                .load("https://image.tmdb.org/t/p/w185" + movie.getImg())
                .placeholder(R.drawable.poster_arrow)
                .into(imageObject);


        RealmResults<RealmData> results = realm.where(RealmData.class).equalTo("id", movie.getId()).findAll();
        /*StringBuilder stringTes = new StringBuilder();
        for (int i = 0; i < results.size(); i++) {
            stringTes.append(results.get(i).getId());
        }
        Toast.makeText(this, stringTes, Toast.LENGTH_SHORT).show();*/
        if (results.size() == 0) {
            //Toast.makeText(this, "Tidak ada data" + movie.getType(), Toast.LENGTH_SHORT).show();
            favButton.setText(R.string.add_fav);
        }
        else {
            //Toast.makeText(this, "lll", Toast.LENGTH_SHORT).show();
            /*try {
                JSONObject jsonObject = new JSONObject(results.get(0).toString());
                JSONArray jsonArray = jsonObject.optJSONArray("proxy");

                for (int i = 0; i < jsonArray.length(); i++) {
                    Toast.makeText(this, jsonArray.getJSONObject(i).toString(), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e){
                e.printStackTrace();
            }*/
            favButton.setText(R.string.remove_fav);
        }

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RealmResults<RealmData> checkResults = realm.where(RealmData.class).equalTo("id", movie.getId()).findAll();
                if (checkResults.size() == 0) {
                    realm.beginTransaction();
                    RealmData obj = realm.createObject(RealmData.class);
                    obj.setId(movie.getId());
                    obj.setType(movie.getType());
                    realm.commitTransaction();
                    favButton.setText(R.string.remove_fav);
                }
                else {
                    realm.beginTransaction();
                    RealmResults<RealmData> delete = realm.where(RealmData.class).equalTo("id", movie.getId()).findAll();
                    delete.deleteAllFromRealm();
                    realm.commitTransaction();
                    favButton.setText(R.string.add_fav);
                    //Toast.makeText(this, "Data sudah ada" + results.get(0) + movie.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
