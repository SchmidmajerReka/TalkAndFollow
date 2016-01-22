package hu.rka.talkfollow.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

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
    public static String titles[] = {"Book details", "Critics", "Readers", "Forum"};

    public TabPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
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
        return mNumOfTabs;
    }

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}

