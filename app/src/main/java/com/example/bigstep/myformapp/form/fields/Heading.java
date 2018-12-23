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
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bigstep.myformapp.R;
import com.example.bigstep.myformapp.form.helper.FormWrapper;
import com.example.bigstep.myformapp.form.helper.AbstractWidget;
import com.example.bigstep.myformapp.ui.WidgetLayoutParams;

import org.json.JSONObject;

/**
 * This class is used for rendering Heading / Dummy element which is specially for lengthy form
 *  to categories their fields.
 */
public class Heading extends AbstractWidget {

    public static final LinearLayout.LayoutParams viewParams = WidgetLayoutParams.
            getCustomWidthHeightLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
    protected TextView _label;
    protected View view;

    public Heading(final Context context, final String property, boolean hasValidator,
                   boolean isNeedToAddPadding, String label, final JSONObject jsonObject) {
        super(context, property, hasValidator);

        LinearLayout.LayoutParams layoutParams = WidgetLayoutParams.getFullWidthLayoutParams();
        layoutParams.setMargins(context.getResources().getDimensionPixelSize(R.dimen.dimen_5dp),
                0, context.getResources().getDimensionPixelSize(R.dimen.dimen_5dp),
                context.getResources().getDimensionPixelSize(R.dimen.dimen_12dp));
        layoutParams.gravity = Gravity.CENTER;


        // Adding bottom line divider.
        View dividerView = new View(context);
        dividerView.setBackgroundResource(R.color.light_gray);
        LinearLayout.LayoutParams dividerLayoutParams = WidgetLayoutParams.getCustomWidthHeightLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                context.getResources().getDimensionPixelSize(R.dimen.padding_1dp));
        dividerView.setLayoutParams(dividerLayoutParams);
        addView(context, label, isNeedToAddPadding);

    }

    /**
     * Method to add label view.
     *
     * @param context      Context of the class.
     * @param label        Label to be shown.
     * @param isAddPadding True if need to add padding.
     */
    public void addView(Context context, String label, boolean isAddPadding) {
        _label = new TextView(context);
        _label.setText(label);
        _label.setTypeface(null, Typeface.BOLD);
        _label.setLayoutParams(FormWrapper.defaultLayoutParams);
        if (isAddPadding) {
            _label.setPadding(context.getResources().getDimensionPixelSize(R.dimen.padding_5dp),
                    context.getResources().getDimensionPixelSize(R.dimen.padding_5dp),
                    context.getResources().getDimensionPixelSize(R.dimen.padding_5dp),
                    context.getResources().getDimensionPixelSize(R.dimen.padding_5dp));
        }

        view = new View(context);
        view.setLayoutParams(viewParams);

        _layout.addView(_label);
        _layout.addView(view);
    }
}