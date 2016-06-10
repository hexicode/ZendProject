package com.hexicode.zend;

/**
 * Created by Brian on 6/8/2016.
 */

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hexicode.zend.data.ZendCardsContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class MyZendCardsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String LOG_TAG = MyZendCardsFragment.class.getSimpleName();

    public static int DIALOG_FRAGMENT_ID = 1;
    public static final String CONTACT_DETAIL_KEY = "CDK";
    private static final int MY_CONTACT_LOADER = 11;

    ListView contactList;
    ContactAdapter contactAdapter;

    static final String[] CONTACTS_SUMMARY_PROJECTION = new String[] {
            ZendCardsContract.MyCards._ID,
            ZendCardsContract.MyCards.COLUMN_COMPANY_ASSOCIATION,
            ZendCardsContract.MyCards.COLUMN_FIRST_NAME,
            ZendCardsContract.MyCards.COLUMN_LAST_NAME,
            ZendCardsContract.MyCards.COLUMN_PHONE_NUMBER,
            ZendCardsContract.MyCards.COLUMN_EMAIL_ADDRESS,
            ZendCardsContract.MyCards.COLUMN_PHOTO_ADDRESS
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(MY_CONTACT_LOADER, savedInstanceState, this);
    }

    public MyZendCardsFragment() {
    }

    public void updateListView(){
        getLoaderManager().restartLoader(MY_CONTACT_LOADER,null, this);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = ZendCardsContract.MyCards.CONTENT_URI;
        //select may change on user preferences to view desired card stack
        String select = null;
        return new android.support.v4.content.CursorLoader(
                getActivity(),
                baseUri,
                CONTACTS_SUMMARY_PROJECTION,
                select,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        contactAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        contactAdapter.swapCursor(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contactAdapter = new ContactAdapter(getActivity(), null, 0);
        View view = inflater.inflate(R.layout.fragment_zend_main, container, false);
        View emptyView = view.findViewById(R.id.empty_view);
        contactList = (ListView)view.findViewById(R.id.contact_listview);
        contactList.setEmptyView(emptyView);
        contactList.setAdapter(contactAdapter);

        contactList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                int index = cursor.getColumnIndex(ZendCardsContract.MyCards._ID);
                long _id = cursor.getLong(index);
                showNoticeDialog(_id);
                return true;
            }
        });

        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                int index = cursor.getColumnIndex(ZendCardsContract.MyCards._ID);
                long _id = cursor.getLong(index);
                ((Callback) getActivity()).onMainFragmentItemSelected(CONTACT_DETAIL_KEY, (int) _id);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void showNoticeDialog(Long id) {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new CreateContactDialogFragment();
        dialog.setTargetFragment(this, DIALOG_FRAGMENT_ID);
        Bundle args = new Bundle();
        args.putLong("ID", id);
        dialog.setArguments(args);
        dialog.show(getActivity().getSupportFragmentManager(), "MyCardsFragmentDialog");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public interface Callback {
        void onMainFragmentItemSelected(String key, int id);
    }
}