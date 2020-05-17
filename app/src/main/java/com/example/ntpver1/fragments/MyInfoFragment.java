package com.example.ntpver1.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ntpver1.R;

public class MyInfoFragment extends Fragment {
    private static final String TAG = "MyInfoFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() is called");

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_info, container, false);

        setUI(rootView);

        return rootView;
    }

    private void setUI(ViewGroup rootView) {

    }
}
