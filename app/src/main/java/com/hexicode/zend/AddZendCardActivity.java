package com.hexicode.zend;

/**
 * Created by Brian on 6/8/2016.
 *
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddZendCardActivity extends AppCompatActivity
        implements AddNewImageDialogFragment.DialogClickListener, View.OnClickListener{

    static final int SELECT_PICTURE = 110;
    static final int REQUEST_TAKE_PHOTO = 12;
    private static final int REQUEST_CODE = 100;
    static Uri pictureSelectionURI;
    Intent returnIntent = new Intent();
    ImageView mImageView;
    public static int imageItemID = 0;
    Bitmap imageBitmap;
    EditText company;
    EditText name;
    EditText lastName;
    EditText number;
    EditText email;
    Button createButton;
    String imageFilePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact_card);
        mImageView = (ImageView)findViewById(R.id.select_image);
        company = (EditText)findViewById(R.id.insert_co_association);
        name = (EditText)findViewById(R.id.insert_name);
        lastName = (EditText)findViewById(R.id.insert_lastname);
        number = (EditText)findViewById(R.id.insert_number);
        email = (EditText)findViewById(R.id.insert_email);
        mImageView.setOnClickListener(this);
        createButton = (Button)findViewById(R.id.create_button);
        createButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_contact_card, menu);
        return true;
    }

    public void imageSelect(){
        showNoticeDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(pictureSelectionURI != null){
            //this.setPic();
            Picasso.with(this).load(pictureSelectionURI).resize(200, 200).into(mImageView);
        } else{
            Picasso.with(this).load(R.mipmap.no_image).resize(200, 200).into(mImageView);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File imageFile = null;
            try{
                imageFile = getImageFile();
            }catch (IOException ex ){

            }
            if(imageFile != null){
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(imageFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File getImageFile() throws IOException{
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "Contact_Cards" + "_" + timestamp;
        File storageDirectory = Environment.
                getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File pic = File.createTempFile(fileName, ".JPEG", storageDirectory);
        imageFilePath = "file:" + pic.getAbsolutePath();
        pictureSelectionURI = Uri.parse(imageFilePath);

        return pic;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
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

    private void uploadImage(){
        Intent galleryIntent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, SELECT_PICTURE);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if (requestCode == REQUEST_TAKE_PHOTO) {
                if(pictureSelectionURI != null){
                    //this.setPic();
                    Picasso.with(this).load(pictureSelectionURI).resize(200, 200).into(mImageView);
                }
            } else if(requestCode == SELECT_PICTURE){
                pictureSelectionURI = data.getData();
                //this.setPic();
                Picasso.with(this).load(pictureSelectionURI).resize(200, 200).into(mImageView);
            }
        }
    }

    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new AddNewImageDialogFragment();
        dialog.show(this.getSupportFragmentManager(), "ImageDialogFragment");
    }

    @Override
    public void takeImagePositiveClick(DialogFragment dialogFragment) {
        //Clicked on take picture option
        dispatchTakePictureIntent();
    }

    @Override
    public void uploadImageNegativeClick(DialogFragment dialogFragment) {
        //Clicked on upload image option
        uploadImage();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.create_button){
            if(company.getText() == null || name.getText() == null || lastName.getText() == null
                    || number.getText() == null || email.getText() == null || pictureSelectionURI == null){
                Toast.makeText(this, "Empty fields", Toast.LENGTH_SHORT).show();
            } else {
                if(company.getText() != null){
                    //set company
                    returnIntent.putExtra("company", company.getText().toString());
                    setResult(RESULT_OK, returnIntent);
                }
                if(name.getText() != null){
                    returnIntent.putExtra("name", name.getText().toString());
                    setResult(RESULT_OK, returnIntent);
                }
                if(lastName.getText() != null){
                    returnIntent.putExtra("lastName", lastName.getText().toString());
                    setResult(RESULT_OK, returnIntent);
                }
                if(number.getText() != null){
                    returnIntent.putExtra("number", number.getText().toString());
                    setResult(RESULT_OK, returnIntent);
                }
                if(email.getText() != null){
                    returnIntent.putExtra("email", email.getText().toString());
                    setResult(RESULT_OK, returnIntent);
                }
                if(pictureSelectionURI != null){
                    returnIntent.putExtra("filePath", pictureSelectionURI.toString());
                    setResult(RESULT_OK, returnIntent);
                }
                this.finish();
            }
        } else if(v.getId() == R.id.select_image){
            imageSelect();
        }
    }
}
