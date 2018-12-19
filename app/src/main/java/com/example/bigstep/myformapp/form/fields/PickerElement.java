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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.bigstep.myformapp.R;
import com.example.bigstep.myformapp.form.helper.AbstractWidget;
import com.example.bigstep.myformapp.listeners.OnRequestMediaListener;

import org.json.JSONObject;

import java.util.Calendar;

/**
 * FormPicker is used to inflate the fields for the attachment picker (Music, Photo, Video etc.),
 * Date/Time picker and Rating bar.
 */

public class PickerElement extends AbstractWidget implements View.OnClickListener {

    // Member Variables.
    private Context mContext;
    private View mConfigFieldView;
    private EditText etFieldValue;
    private TextView tvLabel, tvError;
    private String mFileType, mLabel, mFieldName, mDateType;
    private boolean mIsDatePicker, mIsAttachmentPicker;
    private Drawable mDrawableIcon;
    private String formatHourString, hourString, minuteString, yearString, monthString, dateString, strDateTime,
            dateTag;
    private OnRequestMediaListener mOnRequestMediaListener;
    private JSONObject joProperty;

    /**
     * Public constructor to inflate form field for date/time picker.
     *
     * @param context      Context of calling class.
     * @param name         Property of the field.
     * @param hasValidator True if the field has validation (Compulsory field).
     * @param type         Type of the date picker (date picker or date+time picker).
     */
    public PickerElement(Context context, String name, boolean hasValidator, String type) {

        super(context, name, hasValidator);

        // Initializing member variables.
        mContext = context;
        mFieldName = name;
        mDateType = type;
        mIsDatePicker = true;

        // Inflate the field view layout.
        inflateView();
    }


    /**
     * Public constructor to inflate form field for attachment(music, photo, video etc.) picker.
     *
     * @param context    Context of calling class.
     * @param name       Property of the field.
     * @param joProperty JSONObject Of the field.
     */
    public PickerElement(Context context, final String name, JSONObject joProperty) {
        super(context, name, false);

        // Initializing member variables.
        this.mContext = context;
        this.mFieldName = name;
        this.joProperty = joProperty;
        this.mIsAttachmentPicker = true;
        this.mLabel = joProperty.optString("label");
        this.mFileType = joProperty.optString("fileType", "photo");
        try {
            mOnRequestMediaListener = (OnRequestMediaListener) mContext;
        } catch (ClassCastException cce) {
            cce.printStackTrace();
        }
        // Inflate the field view layout.
        inflateView();
    }

    /**
     * Method to inflate view according to field type.
     */
    private void inflateView() {

        // Inflate the field view layout.
        mConfigFieldView = ((Activity) mContext).getLayoutInflater().inflate(R.layout.element_type_select_1, null);
        if (mIsDatePicker) {
            mDrawableIcon = ContextCompat.getDrawable(mContext, R.drawable.ic_date_range_black_24dp);
        } else if (mIsAttachmentPicker && mFileType != null) {
            setFileTypeDrawable(mFileType);
        }
        getViews();
        mConfigFieldView.setTag(mFieldName);
        _layout.addView(mConfigFieldView);
    }

    /**
     * Method to set drawable according to file type
     * @param fileType String
     */
    private void setFileTypeDrawable(String fileType) {
        switch (fileType) {
            case "photo" :
                mDrawableIcon = ContextCompat.getDrawable(mContext, R.drawable.ic_photo_camera_white_24dp);
                break;
            case "video" :
                mDrawableIcon = ContextCompat.getDrawable(mContext, R.drawable.ic_video_file_24dp);
                break;
                default:
                    mDrawableIcon = ContextCompat.getDrawable(mContext, R.drawable.ic_attach_file_24dp);
        }
    }

    /**
     * Method to get views from the form layout and set data in views..
     */
    private void getViews() {

        // Getting label, description and field value views.
        tvLabel = mConfigFieldView.findViewById(R.id.view_label);
        tvLabel.setTypeface(Typeface.DEFAULT_BOLD);
        tvLabel.setText(mLabel != null ? mLabel : getDisplayText());
        TextView tvDescription = mConfigFieldView.findViewById(R.id.view_description);
        etFieldValue = mConfigFieldView.findViewById(R.id.field_value);
        tvError = mConfigFieldView.findViewById(R.id.error_view);
        // Showing the attachment picker/date picker options.
        etFieldValue.setVisibility(View.VISIBLE);

        // Showing the right drawable icon on the field value view.
        if (mDrawableIcon != null) {
            mDrawableIcon.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(mContext, R.color.light_gray),
                    PorterDuff.Mode.SRC_ATOP));
            etFieldValue.setCompoundDrawablesWithIntrinsicBounds(null, null, mDrawableIcon, null);
            etFieldValue.setCompoundDrawablePadding(mContext.getResources().getDimensionPixelSize(R.dimen.padding_6dp));
        }

        // Showing description for the picker.
        if (joProperty != null && joProperty.optString("description") != null) {
            tvDescription.setVisibility(View.VISIBLE);
            tvDescription.setText(joProperty.optString("description"));
            tvDescription.setPadding(0, 0, 0, mContext.getResources().getDimensionPixelSize(R.dimen.padding_5dp));
        } else {
            tvDescription.setVisibility(View.GONE);
        }

        // Setting up click listener on form view.
        etFieldValue.setOnClickListener(this);
        mConfigFieldView.findViewById(R.id.form_main_view).setOnClickListener(this);


    }


    @Override
    public void setHint(String value) {

        // Showing the hint as a label.
        if (mIsDatePicker && value != null && !value.isEmpty()) {
            tvLabel.setText(value);
            etFieldValue.setHint(value);
        }
    }

    @Override
    public String getValue() {

        // Returning field value according to the inflated view type.
        if (mIsDatePicker) {
            return etFieldValue.getText().toString();
        } else {
            return "";
        }
    }

    @Override
    public void setValue(String value) {

        // Showing the field value according to inflated view type when it is coming in response.
        if (mIsDatePicker && etFieldValue != null && value != null) {
            etFieldValue.setText(value);
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
    public void onClick(View v) {

        // Hiding error view when the attachment option is clicked.
        tvError.setError(null);
        tvError.setVisibility(View.GONE);

        // Perform action on onClick according to inflated view type.
        if (mIsDatePicker) {
            showDateTimeDialogue(mContext, etFieldValue, mDateType, 0L);
        } else if (mOnRequestMediaListener != null) {
            String mediaType = "photo";
            mOnRequestMediaListener.onRequestMedia(mediaType);
        }
    }


    // Create Interface and listener for set date on add destination form

    /**
     * Method to show date time dialog
     *
     * @param context    Context of calling class.
     * @param tvDateTime TextView in which date/time is to be shown.
     * @param type       Type of dialog (date+time or only date picker)
     * @param minDate    Minimum date need to be set when the picker is opened.
     */
    public void showDateTimeDialogue(final Context context, TextView tvDateTime, final String type,
                                     long minDate) {

        mContext = context;

        Calendar newCalendar = Calendar.getInstance();
        DatePicker datePicker = new DatePicker(mContext);

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            datePicker.setCalendarViewShown(false);
        }

        // Showing the recently selected value in date picker if the user recently selected.
        if (yearString != null && !yearString.isEmpty() && monthString != null && !monthString.isEmpty()) {
            datePicker.init(Integer.parseInt(yearString), Integer.parseInt(monthString) - 1, Integer.parseInt(dateString), null);
        } else {
            datePicker.init(newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH), null);
        }
        showDateTimePicker(context, type, tvDateTime, datePicker, null);
        if (minDate != 0L) {
            datePicker.setMinDate(minDate);
        }
    }

    /**
     * Method to show date or time picker according to response.
     *
     * @param context    context of calling class.
     * @param type       format type (whether it is date+time or only date).
     * @param tvDateTime TextView in which date/time is to be shown.
     * @param datePicker DatePicker.
     * @param timePicker TimePicker.
     */
    public void showDateTimePicker(Context context, final String type, final TextView tvDateTime,
                                   final DatePicker datePicker, final TimePicker timePicker) {

        mContext = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        if (datePicker != null) {
            builder.setTitle(mContext.getResources().getString(R.string.date));
            builder.setView(datePicker);
        } else if (timePicker != null) {
            builder.setTitle(mContext.getResources().getString(R.string.time));
            builder.setView(timePicker);
        }

        builder.setPositiveButton(mContext.getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // Adding the date picker value in the text field.
                        if (datePicker != null) {
                            yearString = String.valueOf(datePicker.getYear());
                            int month = datePicker.getMonth() + 1;
                            int date = datePicker.getDayOfMonth();
                            if (month < 10) {
                                monthString = "0" + month;
                            } else {
                                monthString = "" + month;
                            }

                            if (date < 10) {
                                dateString = "0" + date;
                            } else {
                                dateString = "" + date;
                            }

                            strDateTime = yearString + "-" + monthString + "-" + dateString;
                            dateTag = strDateTime;
                        }

                        // Showing the time picker when the format is not of date type.
                        // and showing it only once.
                        if ((type == null || !type.equals("date")) && timePicker == null) {
                            TimePicker tmPicker = new TimePicker(mContext);

                            // Showing the recently selected value in time picker if the user recently selected.
                            if (hourString != null && !hourString.isEmpty() && minuteString != null && !minuteString.isEmpty()) {
                                tmPicker.setCurrentHour(Integer.valueOf(hourString));
                                tmPicker.setCurrentMinute(Integer.valueOf(minuteString));
                            }
                            showDateTimePicker(mContext, null, tvDateTime, null, tmPicker);
                        }

                        // Adding the time picker value in the text field if it selected.
                        if (timePicker != null) {

                            int hour = timePicker.getCurrentHour();
                            int minute = timePicker.getCurrentMinute();

                            if (hour < 10) {
                                hourString = "0" + hour;
                                formatHourString = "0" + hour;
                            } else if (hour > 12) {
                                formatHourString = "" + hour;
                                hour -= 12;
                                hourString = "" + hour;
                            } else {
                                hourString = "" + hour;
                                formatHourString = "" + hour;
                            }

                            if (minute < 10) {
                                minuteString = "0" + minute;
                            } else {
                                minuteString = "" + minute;
                            }

                            dateTag += " " + formatHourString + ":" + minuteString;
                            strDateTime += " " + hourString + ":" + minuteString;

                        }

                        tvDateTime.setText(strDateTime);
                    }
                });

        builder.setNegativeButton(mContext.getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        strDateTime = null;
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }

}
