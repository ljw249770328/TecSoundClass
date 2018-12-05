package com.example.administrator.tecsoundclass;

import android.app.FragmentTransaction;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.example.administrator.tecsoundclass.Fragments.CourseFragment;
import com.example.administrator.tecsoundclass.Fragments.FriendFragment;
import com.example.administrator.tecsoundclass.Fragments.InteractFragment;
import com.example.administrator.tecsoundclass.Fragments.MoreFragment;
import com.example.administrator.tecsoundclass.Fragments.MyselfFragment;
import com.example.administrator.tecsoundclass.Fragments.ReviewFragment;
import com.example.administrator.tecsoundclass.Fragments.SignFragment;

import java.util.ArrayList;
import java.util.List;

public class CourseMenuActivity extends AppCompatActivity {
    private RadioGroup mRgTab;
    private List<Fragment> mFragmentList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_menu);
        mRgTab=findViewById(R.id.rg_course_menu);
        mRgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_sign:
                        changeFragment(SignFragment.class.getName());
                        break;
                    case R.id.rb_review:
                        changeFragment(ReviewFragment.class.getName());
                        break;
                    case R.id.rb_interact:
                        changeFragment(InteractFragment.class.getName());
                        break;
                    case R.id.rb_more:
                        changeFragment(MoreFragment.class.getName());
                        break;
                }
            }
        });
        if(savedInstanceState == null){
            changeFragment(SignFragment.class.getName());
        }
    }
    public void changeFragment(String tag) {
        hideFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
            transaction.show(fragment);
        } else {
            if (tag.equals(SignFragment.class.getName())) {
                fragment = SignFragment.newInstance();
            } else if (tag.equals(InteractFragment.class.getName())) {
                fragment = InteractFragment.newInstance();
            } else if (tag.equals(ReviewFragment.class.getName())) {
                fragment = ReviewFragment.newInstance();
            }else if (tag.equals(MoreFragment.class.getName())) {
                fragment = MoreFragment.newInstance();
            }
            mFragmentList.add(fragment);
            transaction.add(R.id.fl_container, fragment, fragment.getClass().getName());
        }
        transaction.commitAllowingStateLoss();

    }
    private void hideFragment(){
        FragmentTransaction ft=getFragmentManager().beginTransaction();
        for(Fragment f: mFragmentList){
            ft.hide(f);
        }
        ft.commit();
    }
}
