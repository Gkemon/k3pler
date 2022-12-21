package com.k3.k3pler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.k3.k3pler.R;

import java.util.ArrayList;

/** Layout inflater class for viewPager layouts **/
public class LayoutPagerAdapter extends PagerAdapter {
    public enum PagerEnum {
        MainPage(R.string.main_page, R.layout.layout_pager_main),
        BlackListPage(R.string.blacklist_page, R.layout.layout_pager_blacklist),
        SettingsPage(R.string.settings_page, R.layout.layout_pager_settings),
        AboutPage(R.string.about_page, R.layout.layout_pager_about);
        private int mTitleResId;
        private int mLayoutResId;
        PagerEnum(int titleResId, int layoutResId) {
            mTitleResId = titleResId;
            mLayoutResId = layoutResId;
        }
        public int getTitleResId() {
            return mTitleResId;
        }
        public int getLayoutResId() {
            return mLayoutResId;
        }
    }
    private Context context;
    private ArrayList<ViewGroup> layouts = new ArrayList<>();
    public interface IViewPager{
        void onViewsAdded(ArrayList<ViewGroup> layouts);
    }
    private final IViewPager iViewPager;

    public LayoutPagerAdapter(Context context, IViewPager iViewPager) {
        this.context = context;
        this.iViewPager = iViewPager;
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, int position) {
        PagerEnum pagerEnum = PagerEnum.values()[position];
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(pagerEnum.getLayoutResId(),
                collection, false);
        layouts.add(layout);
        collection.addView(layout);
        if(position == getCount() - 1) {
            iViewPager.onViewsAdded(layouts);
        }
        return layout;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup collection, int position,
                            @NonNull Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return PagerEnum.values().length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        PagerEnum pagerEnum = PagerEnum.values()[position];
        return context.getString(pagerEnum.getTitleResId());
    }

}