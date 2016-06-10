package com.hexicode.zend;

/**
 * Created by Brian on 6/9/2016.
 *
 */

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hexicode.zend.data.ZendCardsContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class CardsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private ListView cardList;
    private CardsAdapter mCardsAdapter;
    private View view;
    private View emptyView;

    public static int DIALOG_FRAGMENT_ID = 2;
    private static int currentClickedPosition = 0;
    static final String CONTACT_DETAIL_KEY = "CDK";
    static final int CONTACT_LOADER = 0;

    // These are the Contacts rows that we will retrieve.
    static final String[] CONTACTS_SUMMARY_PROJECTION = new String[] {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.CONTACT_STATUS,
            ContactsContract.Contacts.CONTACT_PRESENCE,
            ContactsContract.Contacts.PHOTO_URI,
            ContactsContract.Contacts.LOOKUP_KEY,
    };
    // Request code for READ_CONTACTS. It can be any number > 0.
    static final int PERMISSIONS_REQUEST_CODE_CONTACTS = 110;
    public CardsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ){
            //Insert the Permission logic here
            // Assume thisActivity is the current activity
            int permissionCheck_ReadContacts = ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.WRITE_CONTACTS);
            int permissionCheck_WriteContacts = ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_CONTACTS);

            if (permissionCheck_ReadContacts != PackageManager.PERMISSION_GRANTED
                    || permissionCheck_WriteContacts != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) &&
                        shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    requestPermissions(new String[]{
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.WRITE_CONTACTS},
                            PERMISSIONS_REQUEST_CODE_CONTACTS);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            getLoaderManager().initLoader(CONTACT_LOADER, null, this);
            Toast.makeText(getActivity(),"Loading contacts", Toast.LENGTH_LONG).show();
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    // permission was granted
                    Toast.makeText(getActivity(),"Permission Granted", Toast.LENGTH_LONG).show();
                    getLoaderManager().initLoader(CONTACT_LOADER, null, this);

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

                // other 'case' lines to check for other
                // permissions this app might request
        }
    }

    private void cardsFragmentUpdateListView(){
        getLoaderManager().restartLoader(CONTACT_LOADER,null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mCardsAdapter = new CardsAdapter(getActivity(), null, 0);
        view = inflater.inflate(R.layout.fragment_cards, container, false);
        cardList = (ListView)view.findViewById(R.id.cards_listview);
        cardList.setAdapter(mCardsAdapter);

        cardList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                currentClickedPosition = position;
                showNoticeDialog();
                return true;
            }
        });

        cardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                int index = cursor.getColumnIndex(ZendCardsContract.Cards._ID);
                int _id = cursor.getInt(index);
                ((Callback) getActivity()).onCardsFragmentItemSelected(CONTACT_DETAIL_KEY, _id);
            }
        });

        return view;
    }

    private static void deleteContact(int position){
        //contactInfo.remove(position);
        //contactAdapter.setArray(contactInfo);
        //cardList.setAdapter(contactAdapter);
    }

    public static void deleteContact(){
        deleteContact(currentClickedPosition);
    }

    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new CreateContactDialogFragment();
        dialog.setTargetFragment(this, DIALOG_FRAGMENT_ID);
        Bundle args = new Bundle();
        args.putInt("position", currentClickedPosition);
        dialog.setArguments(args);
        dialog.show(getActivity().getSupportFragmentManager(), "CardsFragmentDialog");
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //TODO: change content uri to contact cards database location and run required query
        Uri baseUri = ContactsContract.Contacts.CONTENT_URI;
        String select = "((" + ContactsContract.Contacts.DISPLAY_NAME + " NOTNULL) AND ("
                + ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1) AND ("
                + ContactsContract.Contacts.DISPLAY_NAME + " != '' ) AND ("
                + ContactsContract.Contacts.PHOTO_URI + " NOTNULL))";
        return new android.support.v4.content.CursorLoader(
                getActivity(),
                baseUri,
                CONTACTS_SUMMARY_PROJECTION,
                select,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC"
        );
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        mCardsAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mCardsAdapter.swapCursor(null);
    }

    public interface Callback{
        void onCardsFragmentItemSelected(String key, int id);
    }
}