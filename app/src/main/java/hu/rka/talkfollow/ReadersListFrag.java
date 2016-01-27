package hu.rka.talkfollow;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import hu.rka.talkfollow.adapters.ReaderAdapter;
import hu.rka.talkfollow.models.Readers;

/**
 * Created by Réka on 2016.01.09..
 */
public class ReadersListFrag extends android.support.v4.app.Fragment {

    ListView listView;
    ReaderAdapter readerAdapter;
    Context context;
    ArrayList<Readers> readers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reader, container, false);
        context = getActivity();
        TabMenuActivity activity = (TabMenuActivity) getActivity();
        listView = (ListView) v.findViewById(R.id.reader_list);
        readers = activity.getReaders();
        readerAdapter = new ReaderAdapter(getActivity(), 0);
        setUI(readers);

        return v;
    }

    public void refreshUI(ArrayList<Readers> readers) {
        setUI(readers);
    }

    private void setUI(final ArrayList<Readers> readers) {
        if(readers != null) {
            if(listView!=null) {
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
            inflater.inflate(R.menu.menu_empty, menu);
    }
}
