package com.example.carl.customtracktextview;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

@SuppressLint("Registered")
public class ViewPageActivity extends AppCompatActivity {
    private String[] items = {"直播","推荐","视频","图片","段子","精华"};
    private LinearLayout mIndicatorContainer;
    private List<ColorTrackTextView> mIndicators;
    private ViewPager mViewPage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pageview);
        mIndicators = new ArrayList<>();
        mIndicatorContainer = findViewById(R.id.indicator_view);
        mViewPage = findViewById(R.id.view_pager);
        initIndicator();
        initViewPager();
    }
    private void initViewPager(){
        mViewPage.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return ItemFragment.newInstance(items[position]);
            }

            @Override
            public int getCount() {
                return items.length;
            }
        });
        mViewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //position 当前位置 positionOffset 滚动的百分比
                try {
                    //1.左边
                    ColorTrackTextView left = mIndicators.get(position);
                    left.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
                    left.setCurrentProgress(1-positionOffset);
                    //2.右边
                    ColorTrackTextView right = mIndicators.get(position+1);
                    right.setDirection(ColorTrackTextView.Direction.LEFT_TO_RIGHT);
                    right.setCurrentProgress(positionOffset);
                }catch (Exception e){

                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void initIndicator(){
        for (int i=0;i<items.length;i++){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight=1;
            ColorTrackTextView colorTrackTextView = new ColorTrackTextView(this);
            //设置颜色
            colorTrackTextView.setTextSize(20);
            colorTrackTextView.setText(items[i]);
            colorTrackTextView.setLayoutParams(params);
            colorTrackTextView.setChangeColor(Color.RED);
            colorTrackTextView.setOriginColor(Color.BLACK);


            mIndicatorContainer.addView(colorTrackTextView);
            mIndicators.add(colorTrackTextView);

        }

    }



























}
