package com.cs442.team4.tahelper.listItem;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.cs442.team4.tahelper.R;

import java.lang.reflect.Method;
import java.util.List;


/**
 * Created by neo on 11-11-2016.
 */

public class CustomListAdapter<T> extends ArrayAdapter {

    private final Context context;
    private final List<T> values;
    Method method = null;
    String getDisplayText = "";

    final String tag = CustomListAdapter.class.getSimpleName();

    public CustomListAdapter(Context context, int layout, List<T> values) {
        super(context, layout, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.custom_list, parent, false);

        TextView textView = (TextView) v.findViewById(R.id.label);
        ImageView imageView = (ImageView) v.findViewById(R.id.icon);

        try {
            method = values.get(position).getClass().getMethod("getDisplayText");
            getDisplayText = (String) method.invoke(values.get(position));
        } catch (Exception e) {
            Log.e(tag, e.getLocalizedMessage());
        }
        textView.setText(getDisplayText);
        ColorGenerator generator = ColorGenerator.MATERIAL;
        imageView.setImageDrawable(TextDrawable.builder()
                .buildRound("" + Character.toUpperCase(getDisplayText.charAt(0)), generator.getRandomColor()));

        return v;
    }


}
