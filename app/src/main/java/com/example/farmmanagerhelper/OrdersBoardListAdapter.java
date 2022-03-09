package com.example.farmmanagerhelper;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.farmmanagerhelper.models.OrderBoardOrderItem;


import java.util.ArrayList;

    /* List view adapter class for the orders board. It shows the product name, quantity and status
    off the order. It receives an object OrderBoardOrderItem containing those 3 elements. If quantity is
    set to null, it means that it is a header for the customer name. This row has its background colour
    set and only displays the name col. When the OrderBoardServices is searching for orders for the
    date provided, it will create a header and add it to the list. Once it adds the orders for that day,
    it moves onto the next customer, making a new header row and reading all the orders for that date again.
    This is cuaght here and displayed in a way that makes it easy for the user to tell when a when each
    Customer begins.
     */

public class OrdersBoardListAdapter extends ArrayAdapter<OrderBoardOrderItem> {

    private Context mContext;
    private int mResource;


    public OrdersBoardListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<OrderBoardOrderItem> objects) {
        super(context, resource,objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String orderRowName = getItem(position).getName();
        String orderRowQuantity = getItem(position).getQuantity();
        Boolean orderRowOrderComplete = getItem(position).isOrderComplete();


        OrderBoardOrderItem order = new OrderBoardOrderItem(orderRowName, orderRowQuantity,orderRowOrderComplete);

        LayoutInflater inflater = LayoutInflater.from(mContext);

        convertView = inflater.inflate(mResource, parent, false);

        TextView textViewOrderRowName = (TextView) convertView.findViewById(R.id.TextViewOrderBoardRowProductName);
        TextView textViewOrderRowQuantity = (TextView) convertView.findViewById(R.id.TextViewOrderBoardRowQuantity);
        TextView textViewOrderRowOrderComplete = (TextView) convertView.findViewById(R.id.TextViewOrderBoardRowStatus);

        // get layout parameters encase Its a header and needs to be stretched the full way across.
        //
        LinearLayout.LayoutParams rowNameParams = (LinearLayout.LayoutParams)
                textViewOrderRowName.getLayoutParams();


        // if its the header
        if(orderRowQuantity == null)
        {
            // set text to be align left, increase size and make it take up 100% width
            //
            textViewOrderRowName.setText(orderRowName);
            textViewOrderRowName.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            rowNameParams.weight = 0.99f;
            textViewOrderRowName.setLayoutParams(rowNameParams);

            // set background colour
            //
            textViewOrderRowName.setBackgroundColor(Color.parseColor("#bdbdbd"));

            // hide other elements
            //
            textViewOrderRowQuantity.setVisibility(View.GONE);
            textViewOrderRowOrderComplete.setVisibility(View.GONE);

        }
        else {
            textViewOrderRowName.setText(orderRowName);
            textViewOrderRowQuantity.setText(orderRowQuantity);

            if(orderRowOrderComplete)
            {
                textViewOrderRowOrderComplete.setText("Completed");
            }
            else
            {
                textViewOrderRowOrderComplete.setText("Not Ready");

            }
        }

        return convertView;
    }
}
