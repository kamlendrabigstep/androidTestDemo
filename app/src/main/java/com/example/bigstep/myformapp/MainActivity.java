package com.example.bigstep.myformapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bigstep.myformapp.form.helper.FormWrapper;
import com.example.bigstep.myformapp.listeners.OnRequestMediaListener;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity implements OnRequestMediaListener {
    public String url = "http://development.bigsteptech.in/neighboursapp/api/rest/albums/upload?oauth_consumer_key=w13dhb9xebwz92b92j6yjkug6mnyhq82&oauth_secret=x9w78rwp2eu3yhn6k4ylk8fgoxvhe4ap&oauth_consumer_secret=3tm3y20nnsrrg1pkji4khx62ohpnmgu7&oauth_token=qxz85kzl6kaqz41x7sxxeiv74fuyfxky&_ANDROID_VERSION=3.0&language=en&restapilocation=";
    OkHttpClient client = new OkHttpClient();
    TextView txtString;
    LinearLayout linearLayout;
    private String formResponse = "{\"form\":[{\"name\":\"title\",\"type\":\"Text\",\"hasValidator\":true,\"label\":\"Reward Title\"},{\"name\":\"pledge_amount\",\"type\":\"Text\",\"hasValidator\":true,\"label\":\"Minimum Back Amount (Euro)\"},{\"name\":\"description\",\"type\":\"Textarea\",\"hasValidator\":true,\"label\":\"Description\"},{\"name\":\"photo\",\"type\":\"File\",\"label\":\"Main Photo\"},{\"name\":\"delivery_date\",\"type\":\"Date\",\"hasValidator\":true,\"label\":\"Estimated Delivery\"},{\"name\":\"shipping_method\",\"type\":\"Select\",\"hasValidator\":true,\"hasSubForm\":true,\"label\":\"Shipping Detail\",\"multiOptions\":{\"\":\"Select an option\",\"1\":\"No shipping involved\",\"2\":\"Ships only to certain countries\",\"3\":\"Ships anywhere in the world\"}},{\"name\":\"limit\",\"hasSubForm\":true,\"type\":\"switch\",\"label\":\"Limit quantity\",\"value\":0},{\"name\":\"submit\",\"type\":\"submit\",\"label\":\"Create Reward\"}],\"fields\":{\"country_AF\":[{\"name\":\"shhipping_charge_AF\",\"type\":\"Text\",\"label\":\"Shipping Chagre in Afghanistan\"}],\"country_AL\":[{\"name\":\"shhipping_charge_AL\",\"type\":\"Text\",\"label\":\"Shipping Chagre in Albania\"}],\"country_DZ\":[{\"name\":\"shhipping_charge_DZ\",\"type\":\"Text\",\"label\":\"Shipping Chagre in Algeria\"}],\"country_AS\":[{\"name\":\"shhipping_charge_AS\",\"type\":\"Text\",\"label\":\"Shipping Chagre in American Samoa\"}],\"shipping_method_2\":[{\"name\":\"country\",\"type\":\"Select\",\"hasSubForm\":true,\"append\":0,\"label\":\"Select Locaion\",\"multiOptions\":{\"\":\"\",\"AF\":\"Afghanistan\",\"AL\":\"Albania\",\"DZ\":\"Algeria\",\"AS\":\"American Samoa\"},\"value\":\"\"}],\"shipping_method_3\":[{\"name\":\"rest_world\",\"type\":\"Text\",\"label\":\"Rest of World\"},{\"name\":\"country\",\"type\":\"Select\",\"append\":0,\"hasSubForm\":true,\"label\":\"Select Locaion\",\"multiOptions\":{\"\":\"\",\"AF\":\"Afghanistan\",\"AL\":\"Albania\",\"DZ\":\"Algeria\",\"AS\":\"American Samoa\"},\"value\":\"\"}],\"limit_1\":[{\"name\":\"quantity\",\"type\":\"Text\",\"label\":\"Quantity\"}]}}";
    private FormWrapper activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtString = findViewById(R.id.text_id);
        linearLayout = findViewById(R.id.app_form_view);
        activity = new FormWrapper(MainActivity.this);
        txtString.setText("processing ...");
        renderForm();
        txtString.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renderForm();
            }
        });
    }

    private void renderForm() {
        try {
            linearLayout.removeAllViews();
            JSONObject response = new JSONObject(formResponse);
            activity.setFormSchema(response);
            linearLayout.addView(activity.generateForm(MainActivity.this, response));
            if (linearLayout.findViewWithTag("edittext_title") instanceof EditText) {
                EditText editText = linearLayout.findViewWithTag("edittext_title");
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        Log.d(" Data ", " :: " + s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        txtString.setText("Form Added ! (y)");
    }

    @Override
    public void onRequestMedia(String mediaType) {
        Log.d("mediaType Data ", mediaType);
    }
}
