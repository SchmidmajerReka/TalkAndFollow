package hu.rka.talkfollow.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import hu.rka.talkfollow.BookDetailsFrag;
import hu.rka.talkfollow.CriticListFrag;
import hu.rka.talkfollow.ForumFrag;
import hu.rka.talkfollow.ReadersListFrag;

/**
 * Created by Réka on 2016.01.09..
 */
public class TabPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    int starter;
    public TabPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.starter = starter;

    }



    @Override
    public Fragment getItem(int i) {

            switch (i) {
                case 0:
                    return new BookDetailsFrag();
                case 1:
                    return new CriticListFrag();
                case 2:
                    return new ReadersListFrag();
                case 3:
                    return new ForumFrag();
                default:
                    return new BookDetailsFrag();
            }

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mNumOfTabs; //No of Tabs
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Book details";
            case 1:
                return "Critics";
            case 2:
                return "Readers";
            case 3:
                return "Forum";
            default:
                return "Page " + position;
        }
    }

}

