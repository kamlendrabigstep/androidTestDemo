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
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bigstep.myformapp.R;
import com.example.bigstep.myformapp.form.helper.FormWrapper;
import com.example.bigstep.myformapp.form.helper.AbstractWidget;
import com.example.bigstep.myformapp.model.Option;
import com.example.bigstep.myformapp.adaptors.OptionAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * FormMultiOptions is used to inflate the view for the radio buttons and spinner item which contains the multi-options.
 * Bottom sheet view is used to show the multi options.
 */

public class Select extends AbstractWidget implements View.OnClickListener {


    //Member variables.
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private EditText etFieldValue;
    private TextView tvError;
    private List<Option> mOptionsItemList = new ArrayList<>();
    private OptionAdapter mSheetAdapter;
    private BottomSheetDialog mBottomSheetDialog;
    private String mFieldName, mFieldValue = "";
    private JSONObject jsonObjectOptions, joProperty;
    private ArrayList<AbstractWidget> mFormWidgetList;
    private Map<String, AbstractWidget> mFormWidgetMap;
    private AbstractWidget mFormWidget;
    private FormWrapper mFormActivity;
    private boolean mIsOptionWithIcon = false;
    private int elementOrder = 0;


    /**
     * Public constructor to inflate form field For the radio button items.
     *
     * @param context               Context of calling class.
     * @param property              Property of the field.
     * @param options               Object with multi options.
     * @param label                 Label of the field.
     * @param hasValidator          True if the field has validation (Compulsory field).
     * @param description           Description of the field.
     * @param jsonObjectProperty    Json object of the selected property.
     */
    public Select(final Context context, String property, JSONObject options, String label,
                  boolean hasValidator, String description, JSONObject jsonObjectProperty) {
        super(context, property, hasValidator);

        // Initializing member variables.
        mContext = context;
        mFieldName = property;
        jsonObjectOptions = options;
        this.joProperty = jsonObjectProperty;

        initializeConstructorValues(label, description);

    }

    /**
     * Public constructor to inflate form field For the Spinner items.
     *
     * @param context               Context of calling class.
     * @param property              Property of the field.
     * @param options               Object with multi options.
     * @param label                 Label of the field.
     * @param hasValidator          True if the field has validation (Compulsory field).
     * @param description           Description of the field.
     * @param jsonObjectProperty    Json object of the selected property.
     * @param widgets               List of FormWidget.
     * @param map                   Map of field name and formWidget.
     * @param isOptionWithIcon      True if need to show options with the icon.
     */
    public Select(final Context context, String property, JSONObject options, String label,
                  boolean hasValidator, String description, JSONObject jsonObjectProperty, ArrayList<AbstractWidget> widgets,
                  Map<String, AbstractWidget> map, boolean isOptionWithIcon) {

        super(context, property, hasValidator);

        // Initializing member variables.
        this.mContext = context;
        this.mFieldName = property;
        this.jsonObjectOptions = options;
        this.joProperty = jsonObjectProperty;
        this.mFormWidgetList = widgets;
        this.mFormWidgetMap = map;
        this.mIsOptionWithIcon = isOptionWithIcon;
        mFormActivity = new FormWrapper(mContext);

        initializeConstructorValues(label, description);
    }


    /**
     * Method to initialize the constructor values and  inflate the field view accordingly.
     *
     * @param label       Label of the field.
     * @param description Description of the field.
     */
    private void initializeConstructorValues(String label, String description) {
        // Initializing member variables.
        mLayoutInflater = ((Activity) mContext).getLayoutInflater();

        // Fetching all keys from json object and adding data into list.
        Iterator<?> keys = jsonObjectOptions.keys();
        if (keys != null) {
            while (keys.hasNext()) {
                String key = (String) keys.next();
                mOptionsItemList.add(new Option(getLabelFromKey(key), key));
            }
        }


        // Setting up the adapter.
        setAdapter();
        // Inflate the field view layout.
        View configFieldView;
        if (FormWrapper.getLayoutType() == 2) {
            configFieldView = mLayoutInflater.inflate(R.layout.element_type_select_2, null);
        } else {
            configFieldView = mLayoutInflater.inflate(R.layout.element_type_select_1, null);
        }
        getViews(configFieldView, label, description);
        _layout.addView(configFieldView);
        configFieldView.setTag(mFieldName);

        // Setting up the value in the field value view when it is coming in response.
        if (joProperty != null && joProperty.length() > 0
                && jsonObjectOptions != null && jsonObjectOptions.length() > 0 && etFieldValue != null
                && joProperty.optString("value") != null && !joProperty.optString("value").isEmpty()) {
            String value = joProperty.optString("value");
            etFieldValue.setText(jsonObjectOptions.optString(value));
            etFieldValue.setTag(value);
            mFieldValue = value;
        }

    }

    /**
     * Method to set adapter for the list items.
     */
    private void setAdapter() {
        if (!mIsOptionWithIcon) {
            mSheetAdapter = new OptionAdapter(mContext, mOptionsItemList);
        }
        mSheetAdapter.setOnItemClickListener(new OptionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Option item, int position) {
                mBottomSheetDialog.dismiss();
                etFieldValue.setText(item.getName());
                etFieldValue.setTag(item.getKey());
                tvError.setError(null);
                tvError.setVisibility(View.GONE);
                // Not performing any action when the same option is selected.
                if (!mFieldValue.equals(item.getKey())) {
                    mFieldValue = item.getKey();

                    // Performing view creation on multi option selection for the specific modules.
                    if (joProperty != null
                            && (joProperty.optString("type").equals(FormWrapper.SCHEMA_KEY_SELECT)
                            || joProperty.optString("type").equals(FormWrapper.SCHEMA_KEY_SELECT_UPPER))) {

                        if (joProperty.optBoolean("hasSubForm", false)) {
                            inflateSubFormView(item.getKey());
                        }
                    }
                }
            }
        });
    }

    /**
     * Method to get views from the form layout and set data in views..
     *
     * @param configFieldView View which is inflated.
     * @param label           Label of the field.
     * @param description     Description of the field.
     */
    private void getViews(View configFieldView, String label, String description) {

        // Getting label, description and field value views.
        TextView tvLabel = configFieldView.findViewById(R.id.view_label);
        if (FormWrapper.getLayoutType() != 2) {
            tvLabel.setTypeface(Typeface.DEFAULT_BOLD);
        }
        TextView tvDescription = configFieldView.findViewById(R.id.view_description);
        etFieldValue = configFieldView.findViewById(R.id.field_value);
        etFieldValue.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        tvError = configFieldView.findViewById(R.id.error_view);

        // Setting up click listener on form view.
        etFieldValue.setOnClickListener(this);
        configFieldView.findViewById(R.id.form_main_view).setOnClickListener(this);

        // Setting up data in views.
        tvLabel.setText(label);

        // Showing description field if it is coming in response.
        if (description != null && !description.isEmpty()) {
            tvDescription.setVisibility(View.VISIBLE);
            tvDescription.setText(description);
        } else {
            tvDescription.setVisibility(View.GONE);
        }


    }

    /**
     * Method to get label associated with the key.
     *
     * @param key Key of the object.
     * @return Returns the label.
     */
    private String getLabelFromKey(String key) {
        return jsonObjectOptions.optString(key);
    }

    @Override
    public String getValue() {

        // Returning field value.
        return (etFieldValue != null && etFieldValue.getTag() != null ? etFieldValue.getTag().toString() : "");
    }

    @Override
    public void setValue(String value) {

        // Showing the field value when it is coming in response.
        if (value != null && etFieldValue != null && jsonObjectOptions != null
                && jsonObjectOptions.length() > 0) {
            etFieldValue.setText(getLabelFromKey(value));
            etFieldValue.setTag(value);
            mFieldValue = value;
        }
    }

    @Override
    public void setErrorMessage(String errorMessage) {

        // Showing error message on error view.
        if (tvError != null && errorMessage != null) {
            tvError.setVisibility(View.VISIBLE);
            tvError.requestFocus();
            tvError.setError(errorMessage);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.field_value:
            case R.id.form_main_view:
                View inflateView = mLayoutInflater.inflate(R.layout.list_options, null);
                RecyclerView recyclerView = inflateView.findViewById(R.id.recycler_view);

                recyclerView.getLayoutParams().height = RecyclerView.LayoutParams.WRAP_CONTENT;
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                recyclerView.setAdapter(mSheetAdapter);
                mSheetAdapter.setDefaultKey(etFieldValue.getTag() != null
                        ? etFieldValue.getTag().toString() : null);
                mBottomSheetDialog = new BottomSheetDialog(mContext);
                mBottomSheetDialog.setContentView(inflateView);
                mBottomSheetDialog.show();
                break;


        }
    }

    /**
     * Method to set the order of the selected parent element.
     */
    public void setElementOrder() {
        for (int i = 0; i < mFormWidgetList.size(); i++) {
            if (mFormWidgetList.get(i).getPropertyName().equals(mFieldName)) {
                elementOrder = i;
                break;
            }
        }
    }

    /**
     * Method to remove the child element of the selected parent element.
     *
     * @param parentName, name of the selected element.
     */
    private void removeChild(String parentName) {
        String append = FormWrapper.getAttribByProperty(parentName, "append", null);
        int appendValue = (append != null && !append.isEmpty()) ? Integer.parseInt(append) : 1;
        JSONObject formObject = FormWrapper.getFormSchema().optJSONObject("fields");
        String child = FormWrapper.getRegistryByProperty(parentName, "child");


        JSONObject multiChild = FormWrapper.getRegistryByProperty(parentName, "multiChild", "multicheckbox");
        if (multiChild != null) {
            Iterator<String> keys = multiChild.keys();

            List<Integer> keyList = new ArrayList<>();
            while (keys.hasNext()) {
                String key = keys.next();
                int keyOrder = multiChild.optInt(key);
                keyList.add(keyOrder);
            }
            Collections.sort(keyList);
            for (int i = keyList.size() - 1; i >= 0; i--) {
                int keyOrder = keyList.get(i);
                if (keyOrder > 0 && mFormWidgetList.size() > keyOrder) {
                    mFormWidgetList.remove(keyOrder);
                }
            }
            FormWrapper.setMultiChild(parentName, "multiChild", multiChild);
        }


        if (child != null && !child.trim().equals("") && formObject.optJSONArray(child) != null) {
            JSONArray subFormArray = formObject.optJSONArray(child);
            for (int i = subFormArray.length() - 1; i >= 0; --i) {
                if (subFormArray.optJSONObject(i) != null && subFormArray.optJSONObject(i).optBoolean("hasSubForm", false)) {
                    removeChild(subFormArray.optJSONObject(i).optString("name"));
                }
                try {
                    appendValue = (appendValue == 0) ? -1 : appendValue;
                    mFormWidgetList.remove(elementOrder + i + appendValue);
                } catch (IndexOutOfBoundsException e) {
                    Log.d("Exception Removing ", e.getMessage());
                }
            }
        }


    }

    /**
     * Method to inflate the subForm view from the option selection.
     *
     * @param key Key of the selected item.
     */
    private void inflateSubFormView(String key) {
        JSONObject formObject = FormWrapper.getFormSchema().optJSONObject("fields");
        if (formObject == null) {
            return;
        }
        setElementOrder();
        removeChild(mFieldName);
        setElementOrder();
        JSONArray subFormArray = formObject.optJSONArray(mFieldName + "_" + key);
        if (subFormArray != null) {
            String append = FormWrapper.getAttribByProperty(mFieldName, "append", null);
            int appendValue = (append != null && !append.isEmpty()) ? Integer.parseInt(append) : 1;
            for (int i = 0; i < subFormArray.length(); ++i) {
                JSONObject fieldsObject = subFormArray.optJSONObject(i);
                if (fieldsObject != null) {
                    String name = fieldsObject.optString("name");
                    String label = fieldsObject.optString("label");
                    mFormWidget = mFormActivity.getWidget(mContext, name, fieldsObject, label, false, null, mFormWidgetList,
                            mFormWidgetMap);
                    if (fieldsObject.has(FormWrapper.SCHEMA_KEY_HINT))
                        mFormWidget.setHint(fieldsObject.optString(FormWrapper.SCHEMA_KEY_HINT));
                    try {
                        mFormWidgetList.add(elementOrder + i + appendValue, mFormWidget);

                    } catch (IndexOutOfBoundsException e) {
                        Log.d("Exception  Adding", e.getMessage());
                    }
                    mFormWidgetMap.put(name, mFormWidget);
                }
            }
        }
        FormWrapper._layout.removeAllViews();
        for (int i = 0; i < mFormWidgetList.size(); i++) {
            FormWrapper._layout.addView(mFormWidgetList.get(i).getView());
        }
        FormWrapper.setRegistryByProperty(mFieldName, FormWrapper.getFormSchema().optJSONObject(mFieldName), (!key.equals("")) ? mFieldName + "_" + key : null, "multiOptions", 0);
    }

}
