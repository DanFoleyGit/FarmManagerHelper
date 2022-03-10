package com.example.farmmanagerhelper;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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

import com.example.farmmanagerhelper.models.Order;


import java.util.ArrayList;

    /* List view adapter class for the orders board. It shows the product name, quantity and status
    off the order. It receives an Order object. If quantity is
    set to null, it means that it is a header for the customer name. Customer name row has its background colour
    set and only displays the name column, hiding the others. When the OrderBoardServices is searching for orders for the
    date provided, it will create a header and add it to the list. Once it adds the orders for that day,
    it moves onto the next customer, making a new header row and reading all the orders for that date again.
    This is cuaght here and displayed in a way that makes it easy for the user to tell when a when each
    Customer begins.
     */

public class OrdersBoardListAdapter extends ArrayAdapter<Order> {

    private Context mContext;
    private int mResource;


    public OrdersBoardListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Order> objects) {
        super(context,resource,objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String orderRowOrderID = getItem(position).getOrderID();
        String orderRowDate = getItem(position).getOrderDate();
        String orderRowCustomer = getItem(position).getCustomer();
        String orderRowProduct = getItem(position).getProduct();
        String orderRowQuantity = getItem(position).getQuantity();
        Boolean orderRowOrderComplete = getItem(position).isOrderComplete();

        LayoutInflater inflater = LayoutInflater.from(mContext);

        convertView = inflater.inflate(mResource, parent, false);

        TextView textViewOrderRowOrderID = (TextView) convertView.findViewById(R.id.TextViewOrderID);
        TextView textViewOrderRowDate = (TextView) convertView.findViewById(R.id.TextViewOrderBoardDate);
        TextView textViewOrderRowCustomer = (TextView) convertView.findViewById(R.id.TextViewOrderBoardCustomer);
        TextView textViewOrderRowProduct = (TextView) convertView.findViewById(R.id.TextViewOrderBoardRowProductName);
        TextView textViewOrderRowQuantity = (TextView) convertView.findViewById(R.id.TextViewOrderBoardRowQuantity);
        TextView textViewOrderRowOrderComplete = (TextView) convertView.findViewById(R.id.TextViewOrderBoardRowStatus);

        // get layout parameters encase Its a header and needs to be stretched the full way across.
        //
        LinearLayout.LayoutParams rowNameParams = (LinearLayout.LayoutParams)
                textViewOrderRowCustomer.getLayoutParams();

        // Set data to textViews
        //
        textViewOrderRowOrderID.setText(orderRowOrderID);
        textViewOrderRowDate.setText(orderRowDate);
        textViewOrderRowCustomer.setText(orderRowCustomer);
        textViewOrderRowProduct.setText(orderRowProduct);
        textViewOrderRowQuantity.setText(orderRowQuantity);
        if(orderRowOrderComplete)
        {
            textViewOrderRowOrderComplete.setText("Completed");
        }
        else
        {
            textViewOrderRowOrderComplete.setText("Not Ready");
        }

        // if its the header
        //
        if(orderRowProduct == null)
        {
            Log.d("displaying header ",textViewOrderRowCustomer.getText().toString());
            // set text to increase size and make it take up 100% width
            //
            textViewOrderRowCustomer.setText(orderRowCustomer);

            textViewOrderRowCustomer.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            rowNameParams.weight = 0.99f;
            textViewOrderRowCustomer.setLayoutParams(rowNameParams);

            textViewOrderRowCustomer.setVisibility(View.VISIBLE);

            // set background colour
            //
            textViewOrderRowCustomer.setBackgroundColor(Color.parseColor("#bdbdbd"));

            // hide other elements
            //
            textViewOrderRowOrderID.setVisibility(View.GONE);
            textViewOrderRowDate.setVisibility(View.GONE);
            textViewOrderRowProduct.setVisibility(View.GONE);
            textViewOrderRowQuantity.setVisibility(View.GONE);
            textViewOrderRowOrderComplete.setVisibility(View.GONE);

        }
        else {

            // hide Date and OrderID and hide the elements
            //
            textViewOrderRowDate.setVisibility(View.GONE);
            textViewOrderRowOrderID.setVisibility(View.GONE);
        }

        return convertView;
    }
}
