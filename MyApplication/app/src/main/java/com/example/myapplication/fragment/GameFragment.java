package com.example.myapplication.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.myapplication.GameView;
import com.example.myapplication.R;

public class GameFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_game,container,false);
        GameView gameView = new GameView(getContext());
        gameView.setMinimumWidth(view.getWidth());
        gameView.setMinimumWidth(view.getWidth());
        gameView.setMinimumHeight(view.getHeight());

        ((FrameLayout)view).addView(gameView);
        return view;
    }
}