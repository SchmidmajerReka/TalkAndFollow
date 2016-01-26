package hu.rka.talkfollow.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import hu.rka.talkfollow.BookDetailsFrag;
import hu.rka.talkfollow.CriticListFrag;
import hu.rka.talkfollow.ForumFrag;
import hu.rka.talkfollow.ReadersListFrag;
import hu.rka.talkfollow.results.DetailsResult;

/**
 * Created by Réka on 2016.01.09..
 */
public class TabPagerAdapter extends FragmentStatePagerAdapter {

    public static String titles[] = {"Book details", "Critics", "Readers", "Forum"};
    int size = 3;

    private BookDetailsFrag bookDetailsFrag;

    public TabPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.size = NumOfTabs;
        bookDetailsFrag = new BookDetailsFrag();
    }

    public void setSize(int i) {
        size = i;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int i) {

            switch (i) {
                case 0:
                    return bookDetailsFrag;
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

    public void refreshChilds(DetailsResult result) {
        bookDetailsFrag.refreshUI(result.getBook_details());
    }

    @Override
    public int getCount() {
        return size;
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

