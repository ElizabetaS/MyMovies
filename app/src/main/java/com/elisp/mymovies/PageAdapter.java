package com.elisp.mymovies;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by elisp on 07.2.2018.
 */

public class PageAdapter extends FragmentPagerAdapter {


    ArrayList<Fragment> fragmenti = new ArrayList<Fragment>();
    ArrayList<String> titles = new ArrayList<String>();
    public void addFragment (Fragment fragment, String title){
        fragmenti.add(fragment);
        titles.add(title);


    }


    public PageAdapter(FragmentManager fm) {super(fm);
    }

    @Override
    public Fragment getItem(int position) {return fragmenti.get(position);}

    @Override
    public int getCount() {return fragmenti.size();}


    @Override
    public CharSequence getPageTitle(int position) {return titles.get(position);}


}

