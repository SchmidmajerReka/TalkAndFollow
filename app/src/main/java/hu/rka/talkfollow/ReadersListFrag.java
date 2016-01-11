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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reader, container, false);
        listView = (ListView) v.findViewById(R.id.reader_list);

        context = getActivity();

        readerAdapter = new ReaderAdapter(getActivity(), 0);
        ArrayList<Readers> items = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            Readers item = new Readers();
            item.setName("FirstName LastName " + i);
            item.setRate(3);
            item.setDate("2016.01.09");
            items.add(item);
        }

        readerAdapter.setReaders(items);
        listView.setAdapter(readerAdapter);

        return v;
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
