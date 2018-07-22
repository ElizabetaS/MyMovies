package com.elisp.mymovies;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by elisp on 03.3.2018.
 */
public class ImgPagerAdapter extends FragmentPagerAdapter {


    ArrayList<Fragment> fragmenti = new ArrayList<Fragment>();
    public void addFragment (Fragment fragment){
        fragmenti.add(fragment);

    }


    public ImgPagerAdapter(FragmentManager fm) {super(fm);}

    @Override
    public Fragment getItem(int position) {return fragmenti.get(position);}

    @Override
    public int getCount() {return fragmenti.size();}





}

