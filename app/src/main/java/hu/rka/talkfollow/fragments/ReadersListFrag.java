package hu.rka.talkfollow.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import hu.rka.talkfollow.Activities.TabMenuActivity;
import hu.rka.talkfollow.R;
import hu.rka.talkfollow.adapters.ReaderAdapter;
import hu.rka.talkfollow.models.Book;
import hu.rka.talkfollow.models.Readers;

/**
 * Created by Réka on 2016.01.09..
 */
public class ReadersListFrag extends android.support.v4.app.Fragment {


    @Bind(R.id.empty_view)
    TextView emptyView;

    @Bind(R.id.reader_list)
    ListView listView;

    ReaderAdapter readerAdapter;
    Context context;
    ArrayList<Readers> readers;
    ArrayList<Readers> filteredReaders;
    Readers me;
    Book bookDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reader, container, false);
        ButterKnife.bind(this, v);
        context = getActivity();
        TabMenuActivity activity = (TabMenuActivity) getActivity();
        readers = activity.getReaders();

        filteredReaders = activity.getReaders();
        if (filteredReaders != null) {
            for (int i = 0; i < filteredReaders.size(); i++) {
                if (filteredReaders.get(i).getMe() != null && filteredReaders.get(i).getMe()) {
                    me = filteredReaders.get(i);
                    filteredReaders.remove(i);
                }
            }
        }

        bookDetails = activity.getBookDetails();
        readerAdapter = new ReaderAdapter(getActivity(), 0);
        if (bookDetails.isVisibility()) {
            if (me != null) {
                filteredReaders.add(me);
            }
            setUI(filteredReaders);
        } else {
            setUI(filteredReaders);
        }
        return v;
    }

    public void refreshUI(ArrayList<Readers> readers) {
        setUI(readers);
    }

    private void setUI(final ArrayList<Readers> readers) {
        if (readers != null) {
            if (listView != null) {
                listView.setEmptyView(emptyView);
                readerAdapter.setReaders(readers);
                listView.setAdapter(readerAdapter);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_empty, menu);
    }
}
