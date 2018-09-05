package com.example.android.miwok;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.miwokWithFragment.R;

/**
 * 這個Classs目的是要設置FragmentPagerAdaptor，並透過getItem對接各個子Fragment，透過getPageTitle抓各子fragment的標題
 */


/**
 * Create a new {@link MiwokFragmentPagerAdaptor} object to provides the appropriate {@link Fragment} for a view pager.
 * {@link MiwokFragmentPagerAdaptor} is a {@link FragmentPagerAdapter} that can provide the layout for each list item based on a data source which is a list of {@link Word} objects.
 *  "fm" is the fragment manager that will keep each fragment's state in the adapter across swipes.
 */
public class MiwokFragmentPagerAdaptor extends FragmentPagerAdapter {

    /** Context of the app */
    private Context mContext;   // As part of the modification to the constructor as follows, create a context variable.


    public MiwokFragmentPagerAdaptor(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }
    //The MiwokFragmentPagerAdaptor constructor originally was:  public MiwokFragmentPagerAdaptor(FragmentManager fm) {        super(fm);}    before modification as stated at the far bottom.


    /**
     * Return the {@link Fragment} that should be displayed for the given page number.
     */
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new NumbersFragment();
        } else if (position == 1) {
            return new FamilyFragment();
        } else if (position == 2) {
            return new ColorsFragment();
        } else {
            return new PhrasesFragment();
        }
    }


    /**
     * Return the total number of pages.
     */
    @Override
    public int getCount() {
        return 4;
    }


    /**
     * Now we have to tell the app what text to display in each tab. Go into the "MiwokFragmentPagerAdaptor.java" file and override the getPageTitle() method of the class.
     * This method was originally defined in the superclass (the FragmentPagerAdapter), but we want to override the method to customize the tab text (which is known as the page title in the code).
     */
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.category_numbers);
        } else if (position == 1) {
            return mContext.getString(R.string.category_family);
        } else if (position == 2) {
            return mContext.getString(R.string.category_colors);
        } else {
            return mContext.getString(R.string.category_phrases);
        }
    }
    /**
     * Modify the default MiwokFragmentPagerAdaptor getPageTitle(int position) implementation to return the proper category name per page. We could return a hardcoded string such as “Numbers,” “Family,” and so on.
     * However, we don’t want to restrict our app to only support the English language. Instead, we should use the string resource for those category names. Unfortunately, that also means we need a Context object in order to turn the string resource ID into an actual String.
     * So we modified the MiwokFragmentPagerAdaptor constructor at the far top to also require a Context input so that we can get the proper text string.
     */


}