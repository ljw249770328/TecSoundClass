package com.example.administrator.tecsoundclass.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.tecsoundclass.R;

public class ReviewFragment extends Fragment {
    private ImageView mIvBack;
    private ListView mLv;
    public ReviewFragment(){

    }
    public static Fragment newInstance() {
        Fragment fragment = new ReviewFragment();
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_review,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mIvBack=view.findViewById(R.id.im_back);
        mIvBack.setOnClickListener(new Onclick());
        mLv=view.findViewById(R.id.lv_1);
    }
    private class Onclick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.im_back:
                    getActivity().finish();
                    break;
            }
        }
    }
}
