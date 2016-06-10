package com.hexicode.zend;

/**
 * Created by Brian on 6/8/2016.
 *
 */

import android.widget.ImageView;
import android.view.View;
import android.widget.TextView;

public class MyCardsViewHolder {
    public final ImageView contactImage;
    public final TextView companyAssociation;
    public final TextView nameLastName;
    public final TextView email;
    public final TextView number;

    public MyCardsViewHolder(View view){
        contactImage = (ImageView)view.findViewById(R.id.contact_image);
        companyAssociation = (TextView)view.findViewById(R.id.company_association);
        nameLastName = (TextView)view.findViewById(R.id.contact_name);
        email = (TextView)view.findViewById(R.id.contact_email);
        number = (TextView)view.findViewById(R.id.contact_number);
    }
}

