package com.example.plantique;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class EditFragment extends Fragment {
    private static final int RESULT_OK =1 ;
    ImageView ProfileImage;
    Button button;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getView(inflater,container);
        final View view=inflater.inflate(R.layout.fragment_edit,container,false);
        display_profile(view);
        ProfileImage = view.findViewById(R.id.imageView);
        getURI();
        ProfileImage.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE);
            }
        });
        return view;
    }

    private void getURI()
    {
        Bitmap bitmap;
        String usnm = ((MyApplication)this.view.getContext().getApplicationContext()).getUsername();
        String s = "http://192.168.1.6:5000/getURI?usname="+usnm;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(s, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {
                String response = null;
                try {
                    response = new String(responseBody, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                File imgFile = new  File(response);

                if(imgFile.exists()){

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ProfileImage.setImageBitmap(myBitmap);

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                //post.setText("Unable to load posts!");
            }


        });

    }


    public void getView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container)
    {
        this.view = inflater.inflate(R.layout.fragment_edit, container, false);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == -1 && data != null) {
            imageUri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(EditFragment.this.getContext().getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ProfileImage.setImageBitmap(bitmap);
           setURI(imageUri.toString());

        }
    }
    public void setURI(String imageUri)
    {
        String usnm = ((MyApplication)this.view.getContext().getApplicationContext()).getUsername();
        String s = "http://192.168.1.6:5000/setURI?usname="+usnm+"&uri="+imageUri;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(s, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                //post.setText("Unable to load posts!");
            }


        });
    }



    private void display_profile(final View view)
    {
        String usnm = ((MyApplication)this.view.getContext().getApplicationContext()).getUsername();
       String s = "http://192.168.1.6:5000/edit?usname="+usnm;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(s, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //to change to JSON
                JSONObject testV=null;
                String response = null;
                try {
                    response = new String(responseBody, "UTF-8");
                    testV =new JSONObject(new String(responseBody));
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }

                try {
                    add_Table(view,testV);
                } catch (JSONException ex) {
        ex.printStackTrace();
        }

        }

@Override
public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
        //post.setText("Unable to load posts!");
        }


        });
        }

    private void add_Table(View view, JSONObject testV) throws JSONException
    {
               TextView follows=view.findViewById(R.id.textView19);
               TextView followedby=view.findViewById(R.id.textView20);
               TextView role=view.findViewById(R.id.textView21);
               TextView doj=view.findViewById(R.id.textView22);

              follows.setText(testV.get("Follows").toString());
               followedby.setText(testV.get("Followed by").toString());
               role.setText(testV.get("Role").toString());
               doj.setText(testV.get("Date").toString());

    }



}
