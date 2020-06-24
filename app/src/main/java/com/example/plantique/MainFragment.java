package com.example.plantique;
import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.plantique.HomeActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.plantique.adapter.PostAdapter;
import com.example.plantique.dataprovider.PostDataProvider;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static android.preference.PreferenceManager.*;

public class MainFragment extends Fragment {
    String s = "";
    MyApplication a;
    private onFragmentBtnSelected listener;
    private ListView listView;
    private String[] posts;
    private PostDataProvider dataProvider;
    private PostAdapter adapter;
    private static final int REQUEST_CODE_LOCATION_PERMISSION=1;
    private ProgressBar progressBar;
    private View view;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getView(inflater,container);
        final FragmentActivity f  = this.getActivity();
        display_post(this.view);
        Button clickme = this.view.findViewById(R.id.button8);
        Button postme=this.view.findViewById(R.id.button4);
        final EditText text=this.view.findViewById(R.id.editText5);
        postme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onButtonSelected(text.getText().toString());
            }
        });

        clickme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(
                            MainFragment.this.getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                            ,REQUEST_CODE_LOCATION_PERMISSION);
                }
                else
                {
                    getCurrentLocation(view);
                }
            }
        });

        return this.view;
    }

    public void getView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container)
    {
        this.view = inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {

            super.onRequestPermissionsResult(requestCode,permissions,grantResults);
            if(requestCode==REQUEST_CODE_LOCATION_PERMISSION && grantResults.length>0)
            {
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                getCurrentLocation(this.view);
                else
                {
                    Toast.makeText(this.getContext(),"Permission denied",Toast.LENGTH_SHORT).show();
                }
            }
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context  instanceof onFragmentBtnSelected)
        {
            listener=(onFragmentBtnSelected) context;
        }

        else
        {
            throw new ClassCastException(context.toString() + "must implement" );
        }

    }




    public interface onFragmentBtnSelected{
        public void onButtonSelected(String s);
    }

    public void display_list(List<PostClass> map,View view)
    {
        listView = view.findViewById(R.id.postlist);
        adapter = new PostAdapter(view.getContext(), R.layout.post_layout);
        listView.setAdapter(adapter);
                for(PostClass pc:map) {
                    dataProvider = new PostDataProvider(pc.getPost_text(),pc.getName(),pc.getPost_date());
                    adapter.add(dataProvider);
                }


    }
    public void display_post(final View view)
    {
        String usnm = ((MyApplication) view.getContext().getApplicationContext()).getUsername();
        //post=view.findViewById(R.id.posts);
        s = "http://192.168.1.6:5000/userhome?usname="+usnm;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(s, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //to change to JSON
                List<PostClass> lists=new ArrayList<>();
                JSONObject testV=null;

                try {
                    testV =new JSONObject(new String(responseBody));
                    for (Iterator<String> it = testV.keys(); it.hasNext(); ) {
                        String str = it.next();
                        JSONArray jsonar=testV.getJSONArray(str);
                        for(int i=0;i<jsonar.length();i++)
                        {

                            PostClass pobj=new PostClass(str,jsonar.getJSONArray(i).get(0).toString(), (String) jsonar.getJSONArray(i).get(1));
                            lists.add(pobj);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
               display_list(lists,view);
                //post.setText(postMap.keySet().toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                //post.setText("Unable to load posts!");
            }


        });


    }

    private void getCurrentLocation(View v)
    {
        final EditText t1=v.findViewById(R.id.editText5);
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.getFusedLocationProviderClient(MainFragment.this.getActivity()).requestLocationUpdates(locationRequest,new LocationCallback()
        {
                @Override
            public void onLocationResult(LocationResult locationResult)
                {
                    super.onLocationResult(locationResult);
                    LocationServices.getFusedLocationProviderClient(MainFragment.this.getActivity()).removeLocationUpdates(this);
                    if(locationResult!=null && locationResult.getLocations().size()>0)
                    {
                        int latestLocationIndex=locationResult.getLocations().size()-1;
                        double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                        double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                        Geocoder geocoder = new Geocoder(MainFragment.this.getContext(), Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(12.961690,79.145790,  1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //t1.setText(String.format("Latitude:%s\nLongitude:%s",latitude,longitude));
                        t1.setText("I am at "+ addresses.get(0).getSubLocality()+", "+addresses.get(0).getLocality());
                    }
                }
        }, Looper.getMainLooper());

    }

    }

