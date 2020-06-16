package com.example.plantique;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_settings,container,false);
        final String usnm = ((MyApplication) view.getContext().getApplicationContext()).getUsername();
        Button button=view.findViewById(R.id.button9);
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                delete_account(view,usnm);
            }
        });
        return view;
    }

    public void delete_account(View v, String username)
    {

    }

}
