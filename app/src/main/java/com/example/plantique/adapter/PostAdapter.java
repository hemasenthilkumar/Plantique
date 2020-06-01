package com.example.plantique.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import com.example.plantique.MainFragment;
import com.example.plantique.R;
import com.example.plantique.dataprovider.PostDataProvider;
public class PostAdapter extends ArrayAdapter

{
    List list = new ArrayList();

    public PostAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    static class DataHandler
    {
        TextView postt;
        TextView name;
        TextView date;
    }


    @Override
    public void add(@Nullable Object object) {
        list.add(object);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        DataHandler handler;
        if(convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater) this.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.post_layout, parent, false);
            handler = new DataHandler();
            handler.postt = row.findViewById(R.id.posttext);
            handler.name = row.findViewById(R.id.textView);
            handler.date = row.findViewById(R.id.textView12);

            row.setTag(handler);
        }
        else
        {
            handler=(DataHandler) row.getTag();
        }
        PostDataProvider dataProvider;
        dataProvider = (PostDataProvider) this.getItem(position);
        handler.postt.setText(dataProvider.getPost());
        handler.name.setText(dataProvider.getId());
        handler.date .setText(dataProvider.getDate());
        return row;
    }


}
