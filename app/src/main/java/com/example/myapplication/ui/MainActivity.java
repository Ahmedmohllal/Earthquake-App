package com.example.myapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.Internet.JsonfeaturesApi;
import com.example.myapplication.PropertiesDAO;
import com.example.myapplication.PropertiesDatabase;
import com.example.myapplication.R;
import com.example.myapplication.pojo.properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements earthView,RecAdapter.onEarthClick {
    TextView textView;
    JsonfeaturesApi jsonfeaturesApi;
    ArrayList<properties> arrayList = new ArrayList<>();
    private static final String TAG = "MainActivity";
    RecAdapter recAdapter;
    earthPresenter presenter;
    @BindView(R.id.earth_rec)
    RecyclerView recyclerView;
    PropertiesDatabase propertiesDatabase;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recAdapter = new RecAdapter(arrayList,this);
        recyclerView.setAdapter(recAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        propertiesDatabase = PropertiesDatabase.getInstance(this);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://earthquake.usgs.gov/fdsnws/event/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        jsonfeaturesApi = retrofit.create(JsonfeaturesApi.class);
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
            getalldata();
        } else {
            getDataFromRoom();
        }

        presenter = new earthPresenter(this);

    }


    private void getalldata() {
        Observable<ResponseBody> observable = jsonfeaturesApi.getAllFeatures()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Observer<ResponseBody> observer = new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {

                ConvertJson(responseBody);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "getalldata: " + arrayList.size());
            }
        };
        observable.subscribe(observer);
    }

    private void ConvertJson(ResponseBody responseBody) {
        String earthResponse = "";
        try {
            earthResponse = responseBody.string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(earthResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("features");
            JSONObject object, object2;
            String place, url = "";
            double mag;
            for (int i = 0; i < jsonArray.length(); i++) {
                object = jsonArray.getJSONObject(i);
                object2 = object.getJSONObject("properties");
                place = object2.getString("place");
                url = object2.getString("url");
                mag = object2.getDouble("mag");
                properties properties = new properties(mag, place, url);
                arrayList.add(properties);
            }

            sendDataToRoom(arrayList);
            //send list of data to presenter
            //presenter.getList(arrayList);
            presenter.getAllData(arrayList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //method of interface that get data of model from interface and set to recycler view
    @Override
    public void onGetEarth(ArrayList<properties> arrayList) {
        recAdapter.setArrayList(arrayList);
        Log.d(TAG, "ahmed onGetEarth: " + arrayList.size());
    }


    @Override
    public void getItemClick(int position) {
        arrayList.get(position);
        String url = arrayList.get(position).getUrl();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    private void sendDataToRoom(ArrayList<properties> arrayList){
        propertiesDatabase.propertiesDAO().insertData(arrayList)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "aha onComplete: Done");

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void getDataFromRoom(){
        ArrayList<properties> propertiesArrayList = new ArrayList<>();
        propertiesDatabase.propertiesDAO().getAllData()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<properties>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<properties> properties) {
                        for (int i = 0 ; i < properties.size() ; i++){
                            propertiesArrayList.add(properties.get(i));
                        }
                        recAdapter.setArrayList(propertiesArrayList);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}