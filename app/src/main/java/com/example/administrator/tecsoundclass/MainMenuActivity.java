package com.example.administrator.tecsoundclass;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.Service;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.example.administrator.tecsoundclass.Fragments.CourseFragment;
import com.example.administrator.tecsoundclass.Fragments.FriendFragment;
import com.example.administrator.tecsoundclass.Fragments.MyselfFragment;

import java.util.ArrayList;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity {

    private RadioGroup mRgTab;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private String StudentID="";
    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        mRgTab=findViewById(R.id.rg_main);
        StudentID=getIntent().getExtras().getString("LoginId");
        //点击切换Fragment
        mRgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rb_course:
                        changeFragment(CourseFragment.class.getName());
                        break;
                    case R.id.rb_friends:
                        changeFragment(FriendFragment.class.getName());
                        break;
                    case R.id.rb_myself:
                        changeFragment(MyselfFragment.class.getName());
                        break;
                }
            }
        });
        if(savedInstanceState == null){
            changeFragment(CourseFragment.class.getName());
        }



    }
    public void changeFragment(String tag) {
        hideFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
            transaction.show(fragment);
        } else {
            if (tag.equals(CourseFragment.class.getName())) {
                fragment = CourseFragment.newInstance();
            } else if (tag.equals(FriendFragment.class.getName())) {
                fragment = FriendFragment.newInstance();
            } else if (tag.equals(MyselfFragment.class.getName())) {
                fragment = MyselfFragment.newInstance();
            }
            mFragmentList.add(fragment);
            transaction.add(R.id.fl_container, fragment, fragment.getClass().getName());
        }
        transaction.commitAllowingStateLoss();

    }
    private void hideFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        for (Fragment f : mFragmentList) {
            ft.hide(f);
        }
        ft.commit();
    }

}
