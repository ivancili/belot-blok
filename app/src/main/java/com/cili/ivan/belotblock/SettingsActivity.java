package com.cili.ivan.belotblock;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cili.ivan.belotblock.database.GameDatabaseHelper;
import com.cili.ivan.belotblock.enums.PreferenceData;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar tb;
    private GameDatabaseHelper db;
    private CheckBox cb;
    private TextView gameValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setupScreen();
    }

    private void setupScreen() {
        db = new GameDatabaseHelper(this);
        tb = (Toolbar) findViewById(R.id.settings_action_bar);
        cb = (CheckBox) findViewById(R.id.settings_cb_screen_on);
        gameValue = (TextView) findViewById(R.id.settings_game_value);

        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Boolean.valueOf(loadPreferences(PreferenceData.STAY_AWAKE))) {
            cb.setChecked(true);
        } else {
            cb.setChecked(false);
        }

        if (loadPreferences(PreferenceData.GAME_VALUE).isEmpty()) {
            savePreferences(PreferenceData.GAME_VALUE, getString(R.string.default_game_value));
        }

        gameValue.setText(loadPreferences(PreferenceData.GAME_VALUE));
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void clickedGameValue(View view) {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingsActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dailog_game_value, null);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText newValue = (EditText) mView.findViewById(R.id.dialog_game_value_new_val);
        final Button okButton = (Button) mView.findViewById(R.id.dialog_game_value_ok);
        final Button cancelButton = (Button) mView.findViewById(R.id.dialog_game_value_cancel);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newGameValue = newValue.getText().toString();

                if (newGameValue.matches("\\d+")) {
                    savePreferences(PreferenceData.GAME_VALUE, newGameValue);
                    gameValue.setText(loadPreferences(PreferenceData.GAME_VALUE));

                    dialog.dismiss();
                    toast("Nova vrijednost spremljena");
                    return;
                }

                toast("Unesite valjanju vrijednost");
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void clickedKeepScreenOn(View view) {
        CheckBox checkBox = (CheckBox) view;
        if (checkBox.isChecked()) {
            savePreferences(PreferenceData.STAY_AWAKE, String.valueOf(true));
        } else {
            savePreferences(PreferenceData.STAY_AWAKE, String.valueOf(false));
        }
    }

    public void clickedResetAllData(View view) {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingsActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_delete_data, null);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final Button okButton = (Button) mView.findViewById(R.id.dialog_delete_data_ok);
        final Button cancelButton = (Button) mView.findViewById(R.id.dialog_delete_data_cancel);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                db.clearDatabase();
                toast("Svi podatci su izbrisani");
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void savePreferences(PreferenceData data, String value) {
        SharedPreferences prefs = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        prefs.edit().putString(data.toString(), value).apply();
    }

    private String loadPreferences(PreferenceData data) {
        SharedPreferences prefs = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        return prefs.getString(data.toString(), "");
    }

}
