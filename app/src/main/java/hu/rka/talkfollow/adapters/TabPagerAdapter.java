package hu.rka.talkfollow.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import hu.rka.talkfollow.R;
import hu.rka.talkfollow.fragments.BookDetailsFrag;
import hu.rka.talkfollow.fragments.CriticListFrag;
import hu.rka.talkfollow.fragments.ForumFrag;
import hu.rka.talkfollow.fragments.ReadersListFrag;
import hu.rka.talkfollow.results.DetailsResult;

/**
 * Created by Réka on 2016.01.09..
 */
public class TabPagerAdapter extends FragmentStatePagerAdapter {

    static Resources res = null;
    public static String titles[] = { "Book details", "Critics", "Readers", "Forum"};
    int size = 3;

    private BookDetailsFrag bookDetailsFrag;
    private CriticListFrag criticListFrag;
    private ReadersListFrag readersListFrag;
    private ForumFrag forumFrag;

    public TabPagerAdapter(FragmentManager fm, int NumOfTabs, Context c) {
        super(fm);
        this.size = NumOfTabs;
        res = c.getResources();
        bookDetailsFrag = new BookDetailsFrag();
        criticListFrag = new CriticListFrag();
        readersListFrag = new ReadersListFrag();
        forumFrag = new ForumFrag();
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
                return criticListFrag;
            case 2:
                return readersListFrag;
            case 3:
                return forumFrag;
            default:
                return new BookDetailsFrag();
        }
    }

    public void refreshChilds(DetailsResult result) {
        bookDetailsFrag.refreshUI(result.getBook_details());
        criticListFrag.refreshUI(result.getBook_details(), result.getCritics());
        readersListFrag.refreshUI(result.getReaders());
        forumFrag.refreshUI(result.getForum_messages());
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return res.getString(R.string.book_details_label);
            case 1:
                return res.getString(R.string.critics_label);
            case 2:
                return res.getString(R.string.readers_label);
            case 3:
                return res.getString(R.string.forum_label);
            default:
                return res.getString(R.string.book_details_label);
        }
    }

}

