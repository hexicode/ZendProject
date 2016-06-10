package com.hexicode.zend;

/**
 * Created by Brian on 6/9/2016.
 *
 */

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import com.squareup.picasso.Picasso;

public class CardsAdapter extends CursorAdapter {

    public CardsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.
                from(context).
                inflate(R.layout.contact_list_item, parent, false);
        MyCardsViewHolder viewHolder = new MyCardsViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        MyCardsViewHolder viewTag = (MyCardsViewHolder)view.getTag();

        int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        int lookUpIndex = cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY);
        int presenceIndex = cursor.getColumnIndex(ContactsContract.Contacts.CONTACT_PRESENCE);
        int statusIndex = cursor.getColumnIndex(ContactsContract.Contacts.CONTACT_STATUS);
        int pictureIndex = cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI);

        /*
        * Setting the image for the contact. If the image is not available,
        * a default image will be given to the contact.
        */

        if(!cursor.isNull(pictureIndex)){
            Uri imageUri = Uri.parse(cursor.getString(pictureIndex));
            Picasso.with(context).load(imageUri).resize(150,150).into(viewTag.contactImage);
        } else {
            Picasso.with(context).
                    load(R.mipmap.no_image).
                    resize(150,150).
                    into(viewTag.contactImage);
        }

        if(!cursor.isNull(lookUpIndex)){
            viewTag.companyAssociation.setText(cursor.getString(lookUpIndex));
        } else {
            viewTag.companyAssociation.setText("no look up key");
        }

        if(!cursor.isNull(pictureIndex)){
            viewTag.email.setText(cursor.getString(presenceIndex));
        }else{
            viewTag.email.setText("no presence");
        }

        if(!cursor.isNull(statusIndex)){
            viewTag.number.setText(statusIndex);
        }
        else {
            viewTag.number.setText("no Status");
        }

        if(!cursor.isNull(nameIndex)){
            viewTag.nameLastName.setText(cursor.getString(nameIndex));
        }else{
            viewTag.nameLastName.setText("No name");
        }
    }
}
