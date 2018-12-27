/*
 *   Copyright (c) 2016 BigStep Technologies Private Limited.
 *
 *   You may not use this file except in compliance with the
 *   SocialEngineAddOns License Agreement.
 *   You may obtain a copy of the License at:
 *   https://www.socialengineaddons.com/android-app-license
 *   The full copyright and license information is also mentioned
 *   in the LICENSE file that was distributed with this
 *   source code.
 *
 */

package com.example.bigstep.myformapp.form.fields;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.bigstep.myformapp.R;
import com.example.bigstep.myformapp.form.helper.AbstractWidget;
import com.example.bigstep.myformapp.form.helper.FormWrapper;
import com.example.bigstep.myformapp.ui.WidgetLayoutParams;
import com.example.bigstep.myformapp.utils.InputTypeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

/**
 * @TextField is used to inflate the fields for the Edit text with the rich input types
 * like number, phone, location, textarea etc.
 */

public class TextField extends AbstractWidget implements TextWatcher, AdapterView.OnItemClickListener,
        View.OnClickListener {

    public static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    // Member Variables.
    private Context mContext;
    private EditText etFieldValue;
    private AppCompatAutoCompleteTextView tvLocationField;
    private String mFieldName;
    private JSONObject jsonObjectProperty;

    /**
     * Public constructor to inflate form field For the edit text.
     *
     * @param context            Context of calling class.
     * @param property           Property of the field.
     * @param jsonObjectProperty Json object of the selected property.
     * @param description        Description of the field.
     * @param hasValidator       True if the field has validation (Compulsory field).
     * @param type               Type of the field.
     * @param inputType          Input Type of the field.
     * @param widgets            List of FormWidget.
     * @param isNeedToHideView   True if need to hide the inflated view.
     * @param value              Value of the field.
     */
    public TextField(Context context, String property, JSONObject jsonObjectProperty,
                     String description, boolean hasValidator, String type, String inputType,
                     ArrayList<AbstractWidget> widgets, Map<String, AbstractWidget> map,
                     boolean isNeedToHideView, String value) {

        super(context, property, hasValidator);

        // Initialize member variables.
        mContext = context;
        mFieldName = property;
        this.jsonObjectProperty = jsonObjectProperty;


        // Inflate the field view layout.
        View inflateView;
        // Inflate the field view layout.
        if (FormWrapper.getLayoutType() == 3) {
            inflateView = ((Activity) mContext).getLayoutInflater().inflate(R.layout.element_type_text_field_3, null);
        } else if (FormWrapper.getLayoutType() == 2){
            inflateView = ((Activity) mContext).getLayoutInflater().inflate(R.layout.element_type_text_field_2, null);
        } else {
            inflateView = ((Activity) mContext).getLayoutInflater().inflate(R.layout.element_type_text_field_1, null);
        }
        getViews(inflateView, description);
        setInputType(inputType);
        inflateView.setTag(mFieldName);
        setValue(value);
        _layout.addView(inflateView);
        if (isNeedToHideView) {
            _layout.setTag(mFieldName);
            _layout.setVisibility(View.GONE);
        }

    }

    /**
     * Method to get views from the form layout and set data in views..
     *
     * @param configFieldView View which is inflated.
     * @param description     Description of the field.
     */
    private void getViews(View configFieldView, String description) {

        // Getting label, description and field value views.
        TextView tvLabel = configFieldView.findViewById(R.id.view_label);
        tvLabel.setTypeface(Typeface.DEFAULT_BOLD);
        TextView tvDescription = configFieldView.findViewById(R.id.view_description);
        etFieldValue = configFieldView.findViewById(R.id.field_value);
        tvLocationField = configFieldView.findViewById(R.id.location_field_value);
        tvLocationField.setVisibility(View.GONE);
        etFieldValue.setVisibility(View.VISIBLE);
        etFieldValue.setTag("edittext_" + mFieldName);

        if (FormWrapper.getLayoutType() != 2 && jsonObjectProperty.optString("label") != null && !jsonObjectProperty.optString("label").isEmpty()) {
            tvLabel.setVisibility(View.VISIBLE);
            etFieldValue.setPadding(0, mContext.getResources().getDimensionPixelSize(R.dimen.padding_5dp), 0, 0);
            tvLabel.setText(jsonObjectProperty.optString("label"));
        } else {
            tvLabel.setVisibility(View.GONE);
        }

        // Showing description field if it is coming in response.
        if (description != null && !description.isEmpty()) {
            tvDescription.setVisibility(View.VISIBLE);
            tvDescription.setText(description);
        } else {
            tvDescription.setVisibility(View.GONE);
        }

        // Setting up the max length on the edit text if it is coming in response.
        if (jsonObjectProperty.has("maxLength") && jsonObjectProperty.optInt("maxLength") != 0) {
            etFieldValue.setFilters(new InputFilter[]{
                    new InputFilter.LengthFilter(jsonObjectProperty.optInt("maxLength")) {
                        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                            CharSequence res = super.filter(source, start, end, dest, dstart, dend);
                            if (res != null) {
                                etFieldValue.setError("Limit exceeded! Max number of "
                                        + jsonObjectProperty.optInt("maxLength") + " characters allowed.");
                            }
                            return res;
                        }
                    }
            });
        }
    }

    /***
     * Method to check the text type and set the input type and max lines accordingly.
     *
     * @param inputType Input Type of the field.
     */
    private void setInputType(String inputType) {

        int type = InputTypeUtil.getInputType(inputType.toLowerCase());
        etFieldValue.setInputType(type);
        switch (type) {
            case InputTypeUtil.TYPE_CLASS_TEXT_AREA:
                etFieldValue.setHeight(mContext.getResources().getDimensionPixelSize(R.dimen.dimen_80dp));
                etFieldValue.setMaxHeight(mContext.getResources().getDimensionPixelSize(R.dimen.dimen_80dp));
                etFieldValue.setSingleLine(false);
                etFieldValue.setGravity(Gravity.START | Gravity.TOP);
                break;
            case InputType.TYPE_NUMBER_VARIATION_PASSWORD:
            case InputType.TYPE_TEXT_VARIATION_PASSWORD:
            case InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD:
                etFieldValue.setTransformationMethod(PasswordTransformationMethod.getInstance());
                break;
            case InputTypeUtil.TYPE_CLASS_TEXT_LOCATION:
                etFieldValue.setVisibility(View.GONE);
                tvLocationField.setVisibility(View.VISIBLE);
                tvLocationField.setAdapter(new GooglePlacesAutocompleteAdapter(mContext, R.layout.element_type_heading));
                break;
            default:
                etFieldValue.setGravity(Gravity.CENTER_VERTICAL);
                etFieldValue.setMaxLines(1);
                break;
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence searchText, int start, int before, int count) {

        etFieldValue.setError(null);
        tvLocationField.setError(null);

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public String getValue() {

        if (mFieldName.equals("location")) {
            return tvLocationField.getText().toString();
        } else {
            return etFieldValue.getText().toString();
        }
    }

    @Override
    public void setValue(String value) {

        if (mFieldName.equals("location")) {
            tvLocationField.setText(value);
        } else {
            WidgetLayoutParams.setEditText(etFieldValue, value);
        }
    }

    @Override
    public void setHint(String hint) {
        // Showing hint on the respective views..
        if (hint != null) {
            if (mFieldName.equals("location")) {
                tvLocationField.setHint(hint);
            } else {
                etFieldValue.setHint(hint);
            }
        }
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        // Showing error message on error view.
        if (errorMessage != null) {
            if (mFieldName.equals("location")) {
                tvLocationField.requestFocus();
                tvLocationField.setError(errorMessage);
            } else {
                etFieldValue.requestFocus();
                etFieldValue.setError(errorMessage);
            }
        }
    }

    @Override
    public void onClick(View view) {

    }

    /**
     * Method to get result list of the locations.
     *
     * @param input Entered location by the user.
     * @return Returns the list of locations on the basis of input.
     */
    private ArrayList<String> locationAutoComplete(String input) {

        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            String sb = PLACES_API_BASE
                    + TYPE_AUTOCOMPLETE + OUT_JSON + "?key="
                    + mContext.getResources().getString(R.string.places_api_key)
                    + "&input=" + URLEncoder.encode(input, "utf8");
            // sb.append("&components=country:gr");
            //           sb.append("&types=(cities)");

            URL url = new URL(sb);

            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            return resultList;
        } catch (IOException e) {
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());

            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
            // Extract the Place descriptions from the results
            resultList = new ArrayList<>(predsJsonArray.length());
            resultList = new ArrayList<>(predsJsonArray.length());

            for (int i = 0; i < predsJsonArray.length(); i++) {

                JSONObject list = predsJsonArray.optJSONObject(i);
                String value = list.optString("description");
                resultList.add(value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    /**
     * Class to suggest the locations on the basis of user entered string.
     */
    private class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {

        private ArrayList<String> resultList;

        /**
         * Public constructor to find the places.
         *
         * @param context            Context of calling class.
         * @param textViewResourceId Resource id of the text view.
         */
        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @NonNull
        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = locationAutoComplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }

                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint,
                                              FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

}
