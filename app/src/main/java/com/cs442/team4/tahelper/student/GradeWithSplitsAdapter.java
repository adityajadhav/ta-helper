package com.cs442.team4.tahelper.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.cs442.team4.tahelper.R;

import java.util.ArrayList;
import java.util.HashMap;
import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by Mohammed on 11/9/2016.
 */

public class GradeWithSplitsAdapter extends ArrayAdapter<Split> {

    HashMap<String, String> textValues = new HashMap<String, String>();

    int resource;

    public GradeWithSplitsAdapter(Context context, int resource, ArrayList<Split> splits) {
        super(context, resource, splits);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        boolean convertViewWasNull = false;

        final Split split = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grade_split_item_layout, parent, false);
            convertViewWasNull = true;
        }

        final TextView gradeSplitTextView = (TextView) convertView.findViewById(R.id.gradeSplitTextView);
        final EditText gradeSplitEditText = (EditText) convertView.findViewById(R.id.gradeSplitEditText);
        final TextView maxSplitPointsTextView = (TextView) convertView.findViewById(R.id.maxSplitPointsTextView);

        if(convertViewWasNull ){
            //be aware that you shouldn't do this for each call on getView, just once by listItem when convertView is null
            gradeSplitEditText.addTextChangedListener(new GenericTextWatcher(gradeSplitEditText));
        }

        gradeSplitEditText.setTag("theFirstEditTextAtPos:"+position);


        gradeSplitTextView.setTag(position);
        gradeSplitEditText.setTag(position);
        maxSplitPointsTextView.setTag(position);

        gradeSplitTextView.setText(split.getSplitName());

        if(split.getSplitGainedPoints()!=0)
            gradeSplitEditText.setText(split.getSplitGainedPoints().toString());

        maxSplitPointsTextView.setText("/ " + split.getSplitMaximumPoints());

        return convertView;
    }

    private class GenericTextWatcher implements TextWatcher{

        private View view;
        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {

            String text = editable.toString();
            //save the value for the given tag :
            GradeWithSplitsAdapter.this.textValues.put(view.getTag().toString(), editable.toString());
        }
    }

    public String getValueFromFirstEditText(int position){
        //here you need to recreate the id for the first editText
        String result = textValues.get("theFirstEditTextAtPos:"+position);
        if(result ==null)
            result = "default value";

        return result;
    }
}
