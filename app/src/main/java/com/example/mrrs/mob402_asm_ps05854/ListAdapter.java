package com.example.mrrs.mob402_asm_ps05854;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Windows 10 Gamer on 15/06/2017.
 */

public class ListAdapter extends ArrayAdapter<Customer> {

    public ListAdapter(Context context, int resource, List<Customer> items) {
        super(context, resource, items);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view =  inflater.inflate(R.layout.layout_custom, null);
        }
        Customer p = getItem(position);
        if (p != null) {
            // Anh xa + Gan gia tri
            TextView txtName = (TextView) view.findViewById(R.id.txtFullname);
            txtName.setText(p.Fullname);

            TextView txtEmail = (TextView) view.findViewById(R.id.txtEmail);
            txtEmail.setText(p.Email);

            TextView txtSdt = (TextView) view.findViewById(R.id.txtSdt);
            txtSdt.setText(String.valueOf(p.Sdt));

            TextView txtGender = (TextView) view.findViewById(R.id.txtGender);
            txtGender.setText(String.valueOf(p.Gender));

            TextView txtCode = (TextView) view.findViewById(R.id.txtCode);
            txtCode.setText(String.valueOf(p.Code));
            txtCode.setPaintFlags(txtCode.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);

            //ImageView imageHinh = (ImageView)view.findViewById(R.id.imageViewHinh);

        }
        return view;

    }

}
