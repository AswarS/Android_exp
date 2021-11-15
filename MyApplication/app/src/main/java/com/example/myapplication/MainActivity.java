package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.myapplication.entity.DataBunk;
import com.example.myapplication.fragment.BookFragment;
import com.example.myapplication.fragment.WebFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> titles = new ArrayList<>();
        titles.add("图书");
        titles.add("新闻");
        titles.add("卖家");

        fragments.add(new BookFragment());
        fragments.add(new WebFragment());
        fragments.add(new WebFragment());

        TabLayout mTabLayout = findViewById(R.id.tab_layout);
        ViewPager2 mViewPage = findViewById(R.id.view_pager);
        MyViewPageAdapter mAdapter = new MyViewPageAdapter(this, fragments);
        mViewPage.setAdapter(mAdapter);

        new TabLayoutMediator(mTabLayout, mViewPage, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles.get(position));
            }
        }).attach();

        mViewPage.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public static class MyViewPageAdapter extends FragmentStateAdapter {
        List<Fragment> mDatas = new ArrayList<>();

        public MyViewPageAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> mDatas) {
            super(fragmentActivity);
            this.mDatas = mDatas;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return mDatas.get(position);
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }
    }
}