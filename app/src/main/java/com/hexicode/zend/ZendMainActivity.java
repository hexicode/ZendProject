package com.hexicode.zend;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hexicode.zend.data.ZendCardsContract;
import com.hexicode.zend.data.ZendCardsDbHelper;

public class ZendMainActivity extends AppCompatActivity implements
        CreateContactDialogFragment.DialogClickListener,
        MyZendCardsFragment.Callback, CardsFragment.Callback{
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    ZendCardsDbHelper zendCardsDbHelper;
    SQLiteDatabase db;

    private static final int MAIN_ACTIVITY_FRAGMENT_POSITION = 0;
    private static final int CARDS_FRAGMENT_POSITION = 1;
    private static final int PLACEHOLDER_FRAGMENT_POSITION = 2;
    private static final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zend_main);
        zendCardsDbHelper = new ZendCardsDbHelper(this);
        db = zendCardsDbHelper.getWritableDatabase();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                createContact();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_zend_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            ContentValues contentValues = new ContentValues();
            String name = data.getStringExtra("name");
            String lastName = data.getStringExtra("lastName");
            String email = data.getStringExtra("email");
            String number = data.getStringExtra("number");
            String uriFilePath = data.getStringExtra("filePath");
            String company = data.getStringExtra("company");
            contentValues.put(ZendCardsContract.MyCards.COLUMN_COMPANY_ASSOCIATION, company);
            contentValues.put(ZendCardsContract.MyCards.COLUMN_FIRST_NAME, name);
            contentValues.put(ZendCardsContract.MyCards.COLUMN_LAST_NAME, lastName);
            contentValues.put(ZendCardsContract.MyCards.COLUMN_EMAIL_ADDRESS, email);
            contentValues.put(ZendCardsContract.MyCards.COLUMN_PHONE_NUMBER, number);
            contentValues.put(ZendCardsContract.MyCards.COLUMN_PHOTO_ADDRESS, uriFilePath);
            long rowId = db.insert(ZendCardsContract.MyCards.TABLE_NAME,
                    null,
                    contentValues);
            //TODO: Create restriction on getItem parameters.
            ((MyZendCardsFragment)mSectionsPagerAdapter.
                    getItem(MAIN_ACTIVITY_FRAGMENT_POSITION)).
                    updateListView();
        }
    }

    @Override
    public void onPositiveClick(DialogFragment dialogFragment, Long id) {
        int dialogID = dialogFragment.getTargetRequestCode();
        if(dialogID == MyZendCardsFragment.DIALOG_FRAGMENT_ID){
            //MainActivityFragment.deleteContact();
            String selection = ZendCardsContract.MyCards._ID + " LIKE ?";
            String[] selectionArgs = { String.valueOf(id) };
            db.delete(ZendCardsContract.MyCards.TABLE_NAME , selection, selectionArgs);
            //Update the R.layout.fragment_main to remove the cardview
            ((MyZendCardsFragment)mSectionsPagerAdapter.
                    getItem(MAIN_ACTIVITY_FRAGMENT_POSITION)).
                    updateListView();
        } else if(dialogID == CardsFragment.DIALOG_FRAGMENT_ID){
            //TODO: delete contact cards given to user using db.delete() methods
            // CardsFragment.deleteContact();
        } else{
            Toast.makeText(this, "Failed to delete", Toast.LENGTH_SHORT).show();
        }
        dialogFragment.dismiss();
    }

    @Override
    public void onNegativeClick(DialogFragment dialogFragment) {
        dialogFragment.dismiss();
    }

    @Override
    public void onMainFragmentItemSelected(String key, int id) {
        Intent shareContactIntent = new Intent(this, ShareZendCardNFC.class);
        shareContactIntent.putExtra(key, id);
        startActivity(shareContactIntent);
    }

    @Override
    public void onCardsFragmentItemSelected(String key, int id) {
        //TODO: create new class to show contact detail on created contact cards
        Intent contactDetailIntent = new Intent(this, ContactDetail.class);
        contactDetailIntent.putExtra(key, id);
        startActivity(contactDetailIntent);
    }

    private void createContact(){
        Intent addContactIntent = new Intent(this, AddZendCardActivity.class);
        startActivityForResult(addContactIntent, REQUEST_CODE);
    }
}