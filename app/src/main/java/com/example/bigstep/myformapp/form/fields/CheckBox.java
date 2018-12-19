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

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.example.bigstep.myformapp.R;
import com.example.bigstep.myformapp.form.helper.FormWrapper;
import com.example.bigstep.myformapp.form.helper.AbstractWidget;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * CheckBox is used to inflate the fields for the Check box element.
 */

public class CheckBox extends AbstractWidget implements View.OnClickListener {

    // Member variables.
    private Context mContext;
    private AppCompatCheckedTextView checkedTextView;
    private ArrayList<AbstractWidget> mFormWidgetList;
    private Map<String, AbstractWidget> mFormWidgetMap;
    private int elementOrder = 0;
    private String mFieldName;
    private FormWrapper mFormActivity;
    private AbstractWidget mFormWidget;
    private JSONObject joProperty;

    /**
     * Public constructor to inflate form field For the checkbox items.
     *
     * @param context               Context of calling class.
     * @param property              Property of the field.
     * @param label                 Label of the field.
     * @param hasValidator          True if the field has validation (Compulsory field).
     * @param defaultValue          Default value of the field.
     * @param _widget               List of FormWidget.
     */
    public CheckBox(Context context, JSONObject element, String property, String label, boolean hasValidator,
                    int defaultValue, ArrayList<AbstractWidget> _widget, Map<String, AbstractWidget> map) {
        super(context, property, hasValidator);

        // Initializing member variables.
        mContext = context;
        this.mFormWidgetList = _widget;
        this.mFormWidgetMap = map;
        this.mFieldName = property;
        this.joProperty = element;
        mFormActivity = new FormWrapper(mContext);

        checkedTextView = new AppCompatCheckedTextView(mContext);
        checkedTextView.setText(label);
        checkedTextView.setGravity(Gravity.CENTER);
        checkedTextView.setPadding(0, mContext.getResources().getDimensionPixelSize(R.dimen.padding_10dp),
                0, mContext.getResources().getDimensionPixelSize(R.dimen.padding_11dp));
        checkedTextView.setCheckMarkDrawable(getCheckMarkDrawable(mContext));

        checkedTextView.setId(R.id.checkbox);
        checkedTextView.setChecked(defaultValue != 0);
        checkedTextView.setPadding(mContext.getResources().getDimensionPixelSize(R.dimen.padding_5dp),
                mContext.getResources().getDimensionPixelSize(R.dimen.padding_10dp),
                mContext.getResources().getDimensionPixelSize(R.dimen.padding_5dp),
                mContext.getResources().getDimensionPixelSize(R.dimen.padding_10dp));
        _layout.addView(checkedTextView);


        // Adding bottom line divider.
        View view = new View(mContext);
        view.setBackgroundResource(R.color.light_gray);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                mContext.getResources().getDimensionPixelSize(R.dimen.padding_1dp));
        view.setLayoutParams(layoutParams);
        _layout.addView(view);

        // Applying click listener on the check box to mark checkbox as checked/unchecked.
        checkedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedTextView.setError(null);
                if (joProperty.optBoolean("hasSubForm", false)) {
                    if (checkedTextView.isChecked()){
                        inflateSubFormView("0");
                    } else {
                        inflateSubFormView("1");
                    }
                }
                checkedTextView.setChecked(!checkedTextView.isChecked());
                checkModuleSpecificConditions(view);
            }
        });

    }

    /**
     * Method to get List Drawable, which will set onto checked text view.
     *
     * @param context Context of calling class.
     * @return Returns the List Drawable.
     */
    public static StateListDrawable getCheckMarkDrawable(Context context) {

        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_done_24dp).mutate();
        drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.colorPrimary),
                PorterDuff.Mode.SRC_ATOP));
        StateListDrawable sld = new StateListDrawable();
        sld.addState(new int[]{android.R.attr.state_checked, android.R.attr.state_focused}, drawable);
        sld.addState(new int[]{-android.R.attr.state_checked, android.R.attr.state_focused}, new ColorDrawable(Color.WHITE));
        sld.addState(new int[]{-android.R.attr.state_checked}, new ColorDrawable(Color.WHITE));
        sld.addState(new int[]{android.R.attr.state_checked}, drawable);
        return sld;
    }

    /**
     * Method to check module specific conditions on the checkbox click.
     *
     * @param view Clicked check box view.
     */
    private void checkModuleSpecificConditions(View view) {


    }

    @Override
    public String getValue() {
        return String.valueOf(checkedTextView.isChecked() ? "1" : "0");
    }

    @Override
    public void setValue(String value) {
        checkedTextView.setChecked(value.equals("1"));
    }

    @Override
    public void setErrorMessage(String errorMessage) {

        // Showing error message.
        if (!checkedTextView.isChecked() && errorMessage != null) {
            checkedTextView.setError(errorMessage);
            checkedTextView.setFocusable(true);
            checkedTextView.requestFocus();
        }
    }

    @Override
    public void onClick(final View v) {

    }


    /**
     * Method to set the order of the selected parent element.
     */
    public void setElementOrder(){
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
    private void removeChild(String parentName){
        JSONObject formObject = FormWrapper.getFormSchema().optJSONObject("fields");
        String child = FormWrapper.getRegistryByProperty(parentName,"child");
        if(child != null && !child.trim().equals("") && formObject.optJSONArray(child) != null){
            JSONArray subFormArray = formObject.optJSONArray(child);
            for (int i = subFormArray.length()-1; i >= 0 ; --i) {
                if(subFormArray.optJSONObject(i) != null && subFormArray.optJSONObject(i).optBoolean("hasSubForm",false)){
                    removeChild(subFormArray.optJSONObject(i).optString("name"));
                }
                try{
                    mFormWidgetList.remove(elementOrder + i + 1);
                }catch (IndexOutOfBoundsException e){
                    Log.d("Exception Removing ",e.getMessage());
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
        JSONArray subFormArray = formObject.optJSONArray(mFieldName+"_"+key);
        if(subFormArray != null) {
            for (int i = 0; i < subFormArray.length(); ++i) {
                JSONObject fieldsObject = subFormArray.optJSONObject(i);
                if (fieldsObject != null) {
                    String name = fieldsObject.optString("name");
                    String label = fieldsObject.optString("label");

                    mFormWidget = mFormActivity.getWidget(mContext, name, fieldsObject, label, false, null, mFormWidgetList,
                            mFormWidgetMap);
                    if (fieldsObject.has(FormWrapper.SCHEMA_KEY_HINT))
                        mFormWidget.setHint(fieldsObject.optString(FormWrapper.SCHEMA_KEY_HINT));
                    try{
                        mFormWidgetList.add(elementOrder+i+1, mFormWidget);
                    }catch (IndexOutOfBoundsException e){
                        Log.d("Exception  Adding",e.getMessage());
                    }
                    mFormWidgetMap.put(name, mFormWidget);
                }
            }
        }
        FormWrapper._layout.removeAllViews();
        for (int i = 0; i < mFormWidgetList.size(); i++) {
            FormWrapper._layout.addView(mFormWidgetList.get(i).getView());
        }
        FormWrapper.setRegistryByProperty(mFieldName,FormWrapper.getFormSchema().optJSONObject(mFieldName),(!key.equals("")) ? mFieldName+"_"+key : null, "Checkbox", 0);
    }

}
