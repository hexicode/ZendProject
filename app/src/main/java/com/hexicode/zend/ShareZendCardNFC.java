package com.hexicode.zend;

/**
 * Created by Brian on 6/9/2016.
 *
 */

import android.net.Uri;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.hexicode.zend.data.ZendCardsContract;
import com.hexicode.zend.data.ZendCardsDbHelper;
import com.squareup.picasso.Picasso;
import java.io.File;

public class ShareZendCardNFC extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    ZendCardsDbHelper contactCardsDbHelper;
    SQLiteDatabase db;
    Cursor mCursor;
    String imageFilePath, name, lastName, email, company_association, number;
    TextView Company;
    TextView Name_LastName;
    TextView Email;
    TextView Phone;
    ImageView mImageView;
    String imageFileName;
    Bitmap imageBitmap;
    File imageFile;
    private static String imageFileDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_contact_card_nfc);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        int id = extras.getInt(MyZendCardsFragment.CONTACT_DETAIL_KEY);
        long _id = (long)id;

        Company = (TextView)findViewById(R.id.company);
        Name_LastName = (TextView)findViewById(R.id.name);
        Email = (TextView)findViewById(R.id.email);
        Phone = (TextView)findViewById(R.id.number);
        mImageView = (ImageView)findViewById(R.id.contact_share_image);


        PackageManager packageManager = this.getPackageManager();
        // Check whether NFC is available on device
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_NFC)) {
            Toast.makeText(this, "This device does not have NFC hardware.",
                    Toast.LENGTH_SHORT).show();
            mCursor = null;
        }
        else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            Toast.makeText(this, "Android Beam is not supported.",
                    Toast.LENGTH_SHORT).show();
            mCursor = null;
        }
        else {
            nfcAdapter = NfcAdapter.getDefaultAdapter(this);
            mCursor = getCursorResult(_id);

            if(mCursor.moveToNext()){
                int imageIndex = mCursor.getColumnIndex(ZendCardsContract.MyCards.COLUMN_PHOTO_ADDRESS);
                int nameIndex = mCursor.getColumnIndex(ZendCardsContract.MyCards.COLUMN_FIRST_NAME);
                int lastNameIndex = mCursor.getColumnIndex(ZendCardsContract.MyCards.COLUMN_LAST_NAME);
                int emailIndex = mCursor.getColumnIndex(ZendCardsContract.MyCards.COLUMN_EMAIL_ADDRESS);
                int companyIndex = mCursor.getColumnIndex(ZendCardsContract.MyCards.COLUMN_COMPANY_ASSOCIATION);
                int numberIndex = mCursor.getColumnIndex(ZendCardsContract.MyCards.COLUMN_PHONE_NUMBER);

                imageFilePath = mCursor.getString(imageIndex);

                name = mCursor.getString(nameIndex);
                lastName = mCursor.getString(lastNameIndex);
                email = mCursor.getString(emailIndex);
                company_association = mCursor.getString(companyIndex);
                number = mCursor.getString(numberIndex);
            }
        }

        Uri uri = Uri.parse(imageFilePath);
        imageFileName = getFileNameByUri(this, uri);
        // setPic(imageFileDirectory);

        Picasso.with(this).load(imageFilePath).resize(256,256).into(mImageView);
        Company.setText(company_association);
        Name_LastName.setText(name + " " + lastName);
        Phone.setText(number);
        Email.setText(email);
        mCursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_share_contact_card_nfc, menu);
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

    public void sendContactCard(View view){
        if(!nfcAdapter.isEnabled()){
            Toast.makeText(this, "Please enable Android Beam.",
                    Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
        } else if(!nfcAdapter.isNdefPushEnabled()){
            Toast.makeText(this, "Please enable Android Beam.",
                    Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_NFCSHARING_SETTINGS));
        } else {

            //TODO: Use pending intent to start Main Activity and prompt to save or delete them.
            File imageFileToTransfer = new File(imageFileDirectory, imageFileName);
            imageFileToTransfer.setReadable(true, false);

            nfcAdapter.setBeamPushUris(
                    new Uri[]{Uri.fromFile(imageFileToTransfer)},
                    this);
        }
        Toast.makeText(this, "Bump both Devices to share", Toast.LENGTH_LONG).show();
    }

    private Cursor getCursorResult(long id){
        contactCardsDbHelper = new ZendCardsDbHelper(this);
        db = contactCardsDbHelper.getReadableDatabase();
        String selection = ZendCardsContract.MyCards._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };

        return db.query(ZendCardsContract.MyCards.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }

    public static String getFileNameByUri(Context context, Uri uri)
    {
        String fileName="";
        Uri filePathUri = uri;
        String scheme = uri.getScheme();
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (scheme.compareTo("content") == 0) {
            if (cursor.moveToFirst())
            {
                int column_index = cursor.getColumnIndexOrThrow("_data");
                filePathUri = Uri.parse(cursor.getString(column_index));
                File file = new File(cursor.getString(column_index));
                imageFileDirectory = file.getParent();
                fileName = filePathUri.getLastPathSegment().toString();
            }
            cursor.close();
        }
        else if (scheme.compareTo("file") == 0)
        {
            fileName = filePathUri.getLastPathSegment().toString();
        }
        else
        {
            fileName = fileName + "_" + filePathUri.getLastPathSegment();
        }
        return fileName;
    }
}

