package com.example.bigstep.myformapp.form.fields;
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
 */


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.bigstep.myformapp.R;
import com.example.bigstep.myformapp.form.helper.FormWrapper;
import com.example.bigstep.myformapp.form.helper.AbstractWidget;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SwitchElement extends AbstractWidget {

    private Context mContext;
    private String mFieldName;
    private Switch sElement;
    private int elementOrder = 0;
    private ArrayList<AbstractWidget> mFormWidgetList;
    private Map<String, AbstractWidget> mFormWidgetMap;
    private AbstractWidget mFormWidget;
    private FormWrapper mFormActivity;

    public SwitchElement(Context context, String property, boolean hasValidator, JSONObject joProperty, ArrayList<AbstractWidget> widgets
            , Map<String, AbstractWidget> map) {
        super(context, property, hasValidator);

        // Initialize member variables.
        mContext = context;
        mFieldName = property;
        mFormWidgetList = widgets;
        this.mFormWidgetMap = map;
        mFormActivity = new FormWrapper(mContext);

        // Inflate the field view layout.
        View inflateView = ((Activity) mContext).getLayoutInflater().inflate(R.layout.element_type_switch_1, null);
        getViews(inflateView, joProperty);
        inflateView.setTag(mFieldName);
        setValue(joProperty.optString("value", "0"));
        _layout.addView(inflateView);

    }

    private void getViews(View configFieldView, final JSONObject joProperty) {
        TextView tvLabel = configFieldView.findViewById(R.id.view_label);
        tvLabel.setTypeface(Typeface.DEFAULT_BOLD);
        if (joProperty.optString("label") != null && !joProperty.optString("label").isEmpty()) {
            tvLabel.setVisibility(View.VISIBLE);
            tvLabel.setText(joProperty.optString("label"));
        } else {
            tvLabel.setVisibility(View.GONE);
            tvLabel.setPadding(0, 0, 0, 0);
        }
        sElement = configFieldView.findViewById(R.id.sElement);
        sElement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (joProperty.optBoolean("hasSubForm")) {
                    inflateSubForm(getValue());
                }
            }
        });

    }

    @Override
    public String getValue() {
        return sElement.isChecked() ? "1" : "0";
    }

    @Override
    public void setValue(String value) {
        if (value != null && !value.isEmpty() && value.equals("1")) {
            sElement.setChecked(true);
        } else {
            sElement.setChecked(false);
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
    private void inflateSubForm(String key) {
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
