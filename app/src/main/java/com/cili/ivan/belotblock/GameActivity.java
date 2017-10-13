package com.cili.ivan.belotblock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cili.ivan.belotblock.adapter.SessionsAdapter;
import com.cili.ivan.belotblock.database.GameDatabaseHelper;
import com.cili.ivan.belotblock.enums.IntentData;
import com.cili.ivan.belotblock.enums.PreferenceData;
import com.cili.ivan.belotblock.model.Game;
import com.cili.ivan.belotblock.model.Session;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    private Game game;
    private Toolbar tb;
    private GameDatabaseHelper db;
    private ListView sessionsList;
    private ArrayList<Session> sessions;

    private String gameCreatedOn;
    private boolean backButtonPressedOnce = false;

    private TextView gameUs;
    private TextView gameThem;
    private TextView callingUs;
    private TextView callingThem;
    private TextView focudesText;

    private Button[] btn = new Button[2];
    private Button btn_unfocus;
    private int[] btn_id = {R.id.game_game_btn, R.id.game_calling_btn};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        loadIntentData();
        setupScreen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_game, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.game_menu_reset:
                clickedResetGameToZero();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!backButtonPressedOnce) {
            backButtonPressedOnce = true;
            toast(getString(R.string.click_once_again));
            return;
        }

        super.onBackPressed();
    }

    private void loadIntentData() {
        Intent intent = getIntent();
        gameCreatedOn = intent.getStringExtra(IntentData.GAME_CREATED_ON.toString());
    }

    private void setupScreen() {
        if (Boolean.valueOf(loadPreferences(PreferenceData.STAY_AWAKE))) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        db = new GameDatabaseHelper(this);
        tb = (Toolbar) findViewById(R.id.game_action_bar);
        gameUs = (TextView) findViewById(R.id.game_game_us);
        gameThem = (TextView) findViewById(R.id.game_game_them);
        callingUs = (TextView) findViewById(R.id.game_calling_us);
        callingThem = (TextView) findViewById(R.id.game_calling_them);
        sessionsList = (ListView) findViewById(R.id.game_session_list);
        focudesText = gameUs;
        resetButtonClicked(null);

        loadGameData();
        loadSessions();

        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(game.getTeamOneName() + " vs. " + game.getTeamTwoName());

        for (int i = 0; i < btn.length; i++) {
            btn[i] = (Button) findViewById(btn_id[i]);
            btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.game_game_btn:
                            setFocus(btn_unfocus, btn[0]);
                            focudesText = gameUs;
                            break;

                        case R.id.game_calling_btn:
                            setFocus(btn_unfocus, btn[1]);
                            focudesText = callingUs;
                            break;
                    }
                }
            });
        }

        btn_unfocus = btn[0];
    }

    private void loadGameData() {
        Cursor data = db.getGameFromGames(gameCreatedOn);
        while (data.moveToNext()) {
            String gameCreatedOn = data.getString(0);
            String teamOneName = data.getString(1);
            String teamTwoName = data.getString(2);
            game = new Game(gameCreatedOn, teamOneName, teamTwoName);
        }
    }

    private void loadSessions() {
        sessions = new ArrayList<>();

        Cursor data = db.getSessionsForGame(gameCreatedOn);
        while (data.moveToNext()) {
            Session session = new Session(data.getString(0), data.getString(1), data.getString(2), data.getString(3));
            sessions.add(session);
        }

        SessionsAdapter sessionsAdapter = new SessionsAdapter(this, sessions);
        sessionsList.setAdapter(sessionsAdapter);
    }

    private void setFocus(Button btn_unfocus, Button btn_focus) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            btn_unfocus.setBackgroundColor(this.getResources().getColor(R.color.colorAccent, getTheme()));
            btn_focus.setBackgroundColor(this.getResources().getColor(R.color.accent, getTheme()));
        } else {
            btn_unfocus.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            btn_focus.setBackgroundColor(getResources().getColor(R.color.accent));
        }

        this.btn_unfocus = btn_focus;
    }

    private void clickedResetGameToZero() {
        db.clearSessionsForGame(gameCreatedOn);
        loadSessions();
        toast("Igra resetirana");
    }

    public void resetButtonClicked(View view) {
        gameUs.setText("0");
        gameThem.setText("0");
        callingUs.setText("0");
        callingThem.setText("0");
    }

    public void removeButtonClicked(View view) {
        String currentNumUS = gameUs.getText().toString();
        String currentNumThem = gameThem.getText().toString();

        if (currentNumUS.equals("0") && currentNumThem.equals("0")) {
            return;
        }

        if (currentNumUS.length() >= 1 && currentNumThem.length() >= 1) {
            gameUs.setText(currentNumUS.substring(0, currentNumUS.length() - 1));
            gameThem.setText(currentNumThem.substring(0, currentNumThem.length() - 1));
        } else {
            resetButtonClicked(view);
        }

    }

    public void numberClicked(View view) {
        String currentNumber = focudesText.getText().toString();

        if (currentNumber == "0") {
            currentNumber = "";
        }

        String number = "";
        switch (view.getId()) {
            case R.id.n0:
                number = "0";
                break;
            case R.id.n1:
                number = "1";
                break;
            case R.id.n2:
                number = "2";
                break;
            case R.id.n3:
                number = "3";
                break;
            case R.id.n4:
                number = "4";
                break;
            case R.id.n5:
                number = "5";
                break;
            case R.id.n6:
                number = "6";
                break;
            case R.id.n7:
                number = "7";
                break;
            case R.id.n8:
                number = "8";
                break;
            case R.id.n9:
                number = "9";
                break;
        }

        focudesText.setText(currentNumber + number);

        if (focudesText == gameUs) {
            gameThem.setText(String.valueOf(162 - Integer.valueOf(focudesText.getText().toString())));
        }

    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private String loadPreferences(PreferenceData data) {
        SharedPreferences prefs = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        return prefs.getString(data.toString(), "");
    }

    public void clickedSaveButton(View view) {
        Integer gameUs = Integer.valueOf(this.gameUs.getText().toString());
        Integer gameThem = Integer.valueOf(this.gameThem.getText().toString());
        Integer callingUs = Integer.valueOf(this.callingUs.getText().toString());
        Integer callingThem = Integer.valueOf(this.callingThem.getText().toString());

        String sessionStartedOn = String.valueOf(System.currentTimeMillis());
        String teamOneScore = String.valueOf(gameUs + callingUs);
        String teamTwoScore = String.valueOf(gameThem + callingThem);
        db.addNewSessionToGame(gameCreatedOn, sessionStartedOn, teamOneScore, teamTwoScore);

        loadSessions();
    }
}
