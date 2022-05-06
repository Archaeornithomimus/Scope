package com.example.scope.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.scope.R;

public class PlanetsAdapter extends ArrayAdapter<String> {

    private Integer[] tab_images_pour_la_liste = {
            R.drawable.mercure,
            R.drawable.venus,
            R.drawable.iss,
            R.drawable.moon,
            R.drawable.mars,
            R.drawable.jupiter,
            R.drawable.saturn,
            R.drawable.uranus,
            R.drawable.neptune
    };

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.fragment_home, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.planete);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imagePlanete);

        textView.setText(getItem(position));

        if(convertView == null )
            imageView.setImageResource(tab_images_pour_la_liste[position]);
        else
            rowView = (View)convertView;

        return rowView;
    }

    public PlanetsAdapter(Context context, String[] values) {
        super(context, R.layout.fragment_home, values);
    }
}
