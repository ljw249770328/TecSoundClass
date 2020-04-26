package com.example.administrator.tecsoundclass.View.Activity;

import android.app.FragmentTransaction;
import android.app.Fragment;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.example.administrator.tecsoundclass.View.Fragments.InteractFragment;
import com.example.administrator.tecsoundclass.View.Fragments.MoreFragment;
import com.example.administrator.tecsoundclass.View.Fragments.ReviewFragment;
import com.example.administrator.tecsoundclass.View.Fragments.SignFragment;
import com.example.administrator.tecsoundclass.model.JavaBean.Course;
import com.example.administrator.tecsoundclass.model.JavaBean.User;
import com.example.administrator.tecsoundclass.R;

import java.util.ArrayList;
import java.util.List;

public class CourseMenuActivity extends BaseActivity {
    private RadioGroup mRgTab;
    private List<Fragment> mFragmentList=new ArrayList<>();
    private User mUser;
    private Course mCourse;


    public User getmUser() {
        return mUser;
    }

    public Course getmCourse() {
        return mCourse;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_menu);
        init();
        if(savedInstanceState == null){
            changeFragment(SignFragment.class.getName());
        }
    }
    private void  init(){
        mRgTab=findViewById(R.id.rg_course_menu);
        Bundle bundle=getIntent().getExtras();
        mUser= (User) bundle.getSerializable("user");
        mCourse=(Course)bundle.getSerializable("course");
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