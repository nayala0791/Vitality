package com.app.project.blooddonorfinder.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.project.blooddonorfinder.Modules.DonorProfile.DonorProfileDetails;
import com.app.project.blooddonorfinder.Modules.DonorProfile.IncomingRequestForDonor;
import com.app.project.blooddonorfinder.Modules.DonorProfile.ServedRequestByDonor;

/**
 * Created by Pranavi on 9/26/2015.
 */
public class DonorDetailsAdapter extends FragmentStatePagerAdapter {
    CharSequence titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int mNumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    public DonorDetailsAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb)
    {
        super(fm);
        this.titles = mTitles;
        this.mNumbOfTabs = mNumbOfTabsumb;
    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        switch (position)
        {
            case 0:
                fragment= DonorProfileDetails.newInstance("","");
                break;
            case 1:
                fragment= IncomingRequestForDonor.newInstance("","");
                break;
            case 2:
                fragment= ServedRequestByDonor.newInstance("","");
                break;
        }
        return fragment;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }


    @Override
    public int getCount() {
        return mNumbOfTabs;
    }
}
