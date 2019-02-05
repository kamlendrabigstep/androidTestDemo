package com.example.bigstep.myformapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bigstep.myformapp.form.helper.FormWrapper;
import com.example.bigstep.myformapp.listeners.OnRequestMediaListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity implements OnRequestMediaListener {
    public String url = "http://development.bigsteptech.in/neighboursapp/api/rest/albums/upload?oauth_consumer_key=w13dhb9xebwz92b92j6yjkug6mnyhq82&oauth_secret=x9w78rwp2eu3yhn6k4ylk8fgoxvhe4ap&oauth_consumer_secret=3tm3y20nnsrrg1pkji4khx62ohpnmgu7&oauth_token=qxz85kzl6kaqz41x7sxxeiv74fuyfxky&_ANDROID_VERSION=3.0&language=en&restapilocation=";
    OkHttpClient client = new OkHttpClient();
    TextView txtString;
    LinearLayout linearLayout;
    private String formResponse = "{\"form\":[{\"type\":\"Text\",\"name\":\"title\",\"autoCompleter\":\"true\",\"label\":\"Title\",\"error\":\"Specific errors here !\",\"hint\":\"Page Title\",\"required\":true},{\"type\":\"Text\",\"name\":\"profileur\",\"label\":\"URL\",\"hint\":\"Custom URL\",\"required\":true},{\"type\":\"Text\",\"name\":\"location\",\"autoCompleter\":\"true\",\"inputType\":\"location\",\"hint\":\"Enter a location\",\"label\":\"Location\"},{\"type\":\"Select\",\"name\":\"category_id\",\"label\":\"Category\",\"multiOptions\":{\"0\":\"\",\"1\":\"Automobile\",\"6\":\"Jobs\",\"12\":\"Movies - TV\",\"17\":\"Fashion\",\"23\":\"Real Estate\",\"29\":\"Sports\",\"34\":\"Travel\",\"39\":\"Electronics\",\"46\":\"Places\",\"52\":\"Famous Person\",\"53\":\"From Life\",\"54\":\"Celebrities\"},\"hasValidator\":\"true\"},{\"type\":\"Text\",\"name\":\"tags\",\"label\":\"Tags (Keywords)\",\"hint\":\"Separate tags with commas.\"},{\"type\":\"Textarea\",\"inputType\":\"Textarea\",\"name\":\"body\",\"label\":\"Description\",\"required\":true,\"hint\":\"Write overview about page\" },{\"type\":\"File\",\"name\":\"photo\",\"label\":\"Profile Photo\"},{\"type\":\"Select\",\"name\":\"auth_view\",\"label\":\"View Privacy\",\"multiOptions\":{\"everyone\":\"Everyone\",\"registered\":\"All Registered Members\",\"owner_network\":\"Friends and Networks\",\"owner_member_member\":\"Friends of Friends\",\"owner_member\":\"Friends Only\"},\"value\":\"everyone\"},{\"type\":\"Select\",\"name\":\"sspcreate\",\"label\":\"Who may create sub pages in this page?\",\"value\":\"member\"},{\"type\":\"Select\",\"name\":\"splcreate\",\"label\":\"Poll Create Privacy\",\"value\":\"member\"},{\"type\":\"Select\",\"name\":\"smcreate\",\"label\":\"Music Create Privacy\",\"value\":\"member\"},{\"type\":\"Select\",\"name\":\"auth_comment\",\"label\":\"Comment Privacy\",\"multiOptions\":{\"registered\":\"All Registered Members\",\"owner_network\":\"Friends and Networks\",\"owner_member_member\":\"Friends of Friends\",\"owner_member\":\"Friends Only\"},\"value\":\"registered\"},{\"type\":\"Select\",\"name\":\"sdicreate\",\"label\":\"Discussion Topic Privacy\",\"value\":\"member\"},{\"type\":\"Select\",\"name\":\"spcreate\",\"label\":\"Photo Privacy\",\"value\":\"member\"},{\"type\":\"Select\",\"name\":\"sdcreate\",\"label\":\"Document Privacy\",\"value\":\"member\"},{\"type\":\"Select\",\"name\":\"svcreate\",\"label\":\"Video Privacy\",\"value\":\"member\"},{\"label\":\"Status\",\"name\":\"draft\",\"type\":\"select\",\"multiOptions\":{\"1\":\"Published\",\"0\":\"Saved As Draft\"},\"value\":1},{\"type\":\"Checkbox\",\"name\":\"search\",\"label\":\"Show this Page on browse page and in various blocks.\",\"value\":1},{\"label\":\"Create\",\"type\":\"Submit\",\"name\":\"submit\"}]}";
//    private String formResponse = "{\"form\":[{\"name\":\"title\",\"type\":\"Text\",\"hasValidator\":true,\"label\":\"Reward Title\"},{\"name\":\"pledge_amount\",\"type\":\"Text\",\"hasValidator\":true,\"label\":\"Minimum Back Amount (Euro)\"},{\"name\":\"description\",\"type\":\"Textarea\",\"hasValidator\":true,\"label\":\"Description\"},{\"name\":\"photo\",\"type\":\"File\",\"label\":\"Main Photo\"},{\"name\":\"delivery_date\",\"type\":\"Date\",\"hasValidator\":true,\"label\":\"Estimated Delivery\"},{\"name\":\"shipping_method\",\"type\":\"Select\",\"hasValidator\":true,\"hasSubForm\":true,\"label\":\"Shipping Detail\",\"multiOptions\":{\"\":\"Select an option\",\"1\":\"No shipping involved\",\"2\":\"Ships only to certain countries\",\"3\":\"Ships anywhere in the world\"}},{\"name\":\"limit\",\"hasSubForm\":true,\"type\":\"Checkbox\",\"label\":\"Limit quantity\",\"value\":0},{\"name\":\"submit\",\"type\":\"submit\",\"label\":\"Create Reward\"}],\"fields\":{\"country_AF\":[{\"name\":\"shhipping_charge_AF\",\"type\":\"Text\",\"label\":\"Shipping Chagre in Afghanistan\"}],\"country_AL\":[{\"name\":\"shhipping_charge_AL\",\"type\":\"Text\",\"label\":\"Shipping Chagre in Albania\"}],\"country_DZ\":[{\"name\":\"shhipping_charge_DZ\",\"type\":\"Text\",\"label\":\"Shipping Chagre in Algeria\"}],\"country_AS\":[{\"name\":\"shhipping_charge_AS\",\"type\":\"Text\",\"label\":\"Shipping Chagre in American Samoa\"}],\"shipping_method_2\":[{\"name\":\"country\",\"type\":\"Select\",\"hasSubForm\":true,\"append\":0,\"label\":\"Select Locaion\",\"multiOptions\":{\"\":\"\",\"AF\":\"Afghanistan\",\"AL\":\"Albania\",\"DZ\":\"Algeria\",\"AS\":\"American Samoa\"},\"value\":\"\"}],\"shipping_method_3\":[{\"name\":\"rest_world\",\"type\":\"Text\",\"label\":\"Rest of World\"},{\"name\":\"country\",\"type\":\"Select\",\"append\":0,\"hasSubForm\":true,\"label\":\"Select Locaion\",\"multiOptions\":{\"\":\"\",\"AF\":\"Afghanistan\",\"AL\":\"Albania\",\"DZ\":\"Algeria\",\"AS\":\"American Samoa\"},\"value\":\"\"}],\"limit_1\":[{\"name\":\"quantity\",\"type\":\"Text\",\"label\":\"Quantity\"}]}}";
    private FormWrapper activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtString = findViewById(R.id.text_id);
        linearLayout = findViewById(R.id.app_form_view);
        activity = new FormWrapper(MainActivity.this);
        txtString.setText("processing ...");
        renderForm(1);
        txtString.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renderForm(1);
            }
        });
        getSupportActionBar().setTitle("Forms Demo Layout 1");
    }

    private void renderForm(int layoutType) {
        try {
            linearLayout.removeAllViews();
            JSONObject response = new JSONObject(formResponse);
            activity.setFormSchema(response);
            activity.setLayoutType(layoutType);
            linearLayout.addView(activity.getFormWrapper(MainActivity.this, response));
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
        txtString.setVisibility(View.GONE);
    }

    @Override
    public void onRequestMedia(String mediaType, String fieldName) {
        Log.d("mediaType Data ", mediaType + " fieldName :: " + fieldName);
        linearLayout.findViewWithTag("FILE_" + "");
        if (linearLayout.findViewWithTag("FILE_" + fieldName) instanceof EditText) {
            EditText editText = linearLayout.findViewWithTag("FILE_" + fieldName);
            editText.setText("1 file selected");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.layout_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int menuId = item.getItemId();
        switch (menuId) {
            case R.id.iMenu1:
                getSupportActionBar().setTitle("Forms Demo Layout 1");
                renderForm(1);
                break;
            case R.id.iMenu2:
                getSupportActionBar().setTitle("Forms Demo Layout 2");
                renderForm(2);
                break;
            case R.id.iMenu3:
                getSupportActionBar().setTitle("Forms Demo Layout 3");
                renderForm(3);
                break;
            case R.id.iSubmit:
                HashMap<String, String> params = activity.save();
                System.out.println(params);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
