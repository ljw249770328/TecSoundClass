package com.example.administrator.tecsoundclass.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.tecsoundclass.Adapter.MySignListAdapter;
import com.example.administrator.tecsoundclass.R;

public class SignFragment extends Fragment {
    private ImageView mIvBack;
    private ListView mLv;
    private TextView mTvSign;
    public SignFragment(){

    }
    public static Fragment newInstance() {
        Fragment fragment = new SignFragment();
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_sign,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Onclick onclick=new Onclick();
        mIvBack=view.findViewById(R.id.im_back);
        mTvSign=view.findViewById(R.id.tv_start_sign);
        mLv=view.findViewById(R.id.lv_1);
        mIvBack.setOnClickListener(onclick);
        mTvSign.setOnClickListener(onclick);
        mLv.setAdapter(new MySignListAdapter(getActivity()));
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }
    private class Onclick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.im_back:
                    getActivity().finish();
                    break;
                case R.id.tv_start_sign:
                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    View view =LayoutInflater.from(getActivity()).inflate(R.layout.layout_sign_dialog,null);
                    TextView mSign=view.findViewById(R.id.tv_press_sign);
                    builder.setView(view).show();
                    mSign.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //To do
                        }
                    });
            }
        }
    }
}
