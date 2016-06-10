package com.hexicode.zend;

/**
 * Created by Brian on 6/8/2016.
 *
 */

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import com.hexicode.zend.data.ZendCardsContract;
import com.squareup.picasso.Picasso;

public class ContactAdapter extends CursorAdapter {

    public ContactAdapter(Context context, Cursor c, int flags){
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.
                from(context).
                inflate(R.layout.contact_list_item, parent, false);
        MyCardsViewHolder myCardsViewHolder = new MyCardsViewHolder(view);
        view.setTag(myCardsViewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        MyCardsViewHolder myCardsViewHolderTag = (MyCardsViewHolder)view.getTag();
        int companyIndex = cursor.getColumnIndex(ZendCardsContract.MyCards.COLUMN_COMPANY_ASSOCIATION);
        int nameIndex = cursor.getColumnIndex(ZendCardsContract.MyCards.COLUMN_FIRST_NAME);
        int lastNameIndex = cursor.getColumnIndex(ZendCardsContract.MyCards.COLUMN_LAST_NAME);
        int numberIndex = cursor.getColumnIndex(ZendCardsContract.MyCards.COLUMN_PHONE_NUMBER);
        int emailIndex = cursor.getColumnIndex(ZendCardsContract.MyCards.COLUMN_EMAIL_ADDRESS);
        int photoIndex = cursor.getColumnIndex(ZendCardsContract.MyCards.COLUMN_PHOTO_ADDRESS);

        if(!cursor.isNull(photoIndex)){
            Uri imageUri = Uri.parse(cursor.getString(photoIndex));
            Picasso
                    .with(context)
                    .load(imageUri)
                    .resize(100, 100)
                    .into(myCardsViewHolderTag.contactImage);
        } else {
            Picasso
                    .with(context)
                    .load(R.mipmap.no_image)
                    .resize(100,100)
                    .into(myCardsViewHolderTag.contactImage);
        }
        if(!cursor.isNull(companyIndex)){
            myCardsViewHolderTag.companyAssociation.setText(cursor.getString(companyIndex));
        }
        if(!cursor.isNull(nameIndex) && !cursor.isNull(lastNameIndex)){
            String firstName = cursor.getString(nameIndex);
            String lastName = cursor.getString(lastNameIndex);
            myCardsViewHolderTag.nameLastName.setText(firstName + " " + lastName);
        }
        if(!cursor.isNull(numberIndex)){
            myCardsViewHolderTag.number.setText(cursor.getString(numberIndex));
        }
        if(!cursor.isNull(emailIndex)){
            myCardsViewHolderTag.email.setText(cursor.getString(emailIndex));
        }
        myCardsViewHolderTag.email.setText(cursor.getString(emailIndex));
    }
}