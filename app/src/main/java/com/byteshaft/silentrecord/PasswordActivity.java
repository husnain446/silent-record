package com.byteshaft.silentrecord;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.byteshaft.silentrecord.fragments.SettingFragment;
import com.byteshaft.silentrecord.utils.Helpers;


public class PasswordActivity extends Activity implements View.OnClickListener {

    private EditText pinInput;
    private Button submitButton;
    private SettingFragment settingFragment;
    public static boolean wasShown;
    SharedPreferences mPreference;
    Helpers mHelpers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mHelpers = new Helpers(AppGlobals.getContext());
        setContentView(R.layout.activity_password);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        settingFragment = new SettingFragment();
        mPreference = AppGlobals.getPreferenceManager();
        pinInput = (EditText) findViewById(R.id.edit_pin);
        submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        wasShown = true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_button:
                String pin = Helpers.getValueFromKey("pin_code");
                String input = pinInput.getText().toString();
                if (TextUtils.equals(input, pin)) {
                    AppGlobals.setIsUnlocked(true);
                    openMainActivity();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Incorrect PIN", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void openMainActivity() {
        Intent intent = new Intent(PasswordActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
