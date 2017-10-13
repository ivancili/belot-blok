package com.cili.ivan.belotblock;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.cili.ivan.belotblock.adapter.GamesAdapter;
import com.cili.ivan.belotblock.database.GameDatabaseHelper;
import com.cili.ivan.belotblock.enums.IntentData;
import com.cili.ivan.belotblock.model.Game;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private Toolbar tb;
    private GameDatabaseHelper db;
    private ListView gamesList;
    private ArrayList<Game> games;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupScreen();
        loadData();
    }

    @Override
    protected void onStart() {
        loadData();
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_home, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home_menu_settings:
                clickedSettings();
                break;
            case R.id.home_menu_info:
                clickedInfo();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupScreen() {
        tb = (Toolbar) findViewById(R.id.home_action_bar);
        setSupportActionBar(tb);

        gamesList = (ListView) findViewById(R.id.home_games_list);
        db = new GameDatabaseHelper(this);
    }

    private void loadData() {
        games = new ArrayList<>();
        Cursor data = db.getGames();

        while (data.moveToNext()) {
            String gameCreatedOn = data.getString(0);
            String teamOneName = data.getString(1);
            String teamTwoName = data.getString(2);

            Game game = new Game(gameCreatedOn, teamOneName, teamTwoName);
            games.add(game);
        }

        GamesAdapter adapter = new GamesAdapter(this, games);

        gamesList.setAdapter(adapter);
        gamesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Game game = games.get(position);
                Intent intent = new Intent(HomeActivity.this, GameActivity.class);
                intent.putExtra(IntentData.GAME_CREATED_ON.toString(), game.getGameCreatedOn());
                startActivity(intent);
            }

        });

        if (!(games.size() == 0)) {
            hideGamesScreen(false);
            hideEmptyScreen(true);
        } else {
            hideGamesScreen(true);
            hideEmptyScreen(false);
        }
    }

    private void hideGamesScreen(boolean yes) {
        if (yes) {
            gamesList.setVisibility(View.GONE);
        } else {
            gamesList.setVisibility(View.VISIBLE);
        }
    }

    private void hideEmptyScreen(boolean yes) {
        LinearLayout ll = (LinearLayout) findViewById(R.id.home_empty_screen);
        if (yes) {
            ll.setVisibility(View.GONE);
        } else {
            ll.setVisibility(View.VISIBLE);
        }
    }

    public void clickedSettings() {
        Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    public void clickedInfo() {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(HomeActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_info, null);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button okButton = (Button) mView.findViewById(R.id.dialog_info_ok);

        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });

        dialog.show();
    }

    public void clickedPlayNewGame(View view) {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(HomeActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_new_game, null);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText teamOne = (EditText) mView.findViewById(R.id.dialog_team_one);
        final EditText teamTwo = (EditText) mView.findViewById(R.id.dialog_team_two);
        final Button okButton = (Button) mView.findViewById(R.id.dialog_new_game_ok);

        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(okButton.getWindowToken(), 0);

                final String teamOneName = teamOne.getText().toString();
                final String teamTwoName = teamTwo.getText().toString();
                final String gameCreatedOn = String.valueOf(System.currentTimeMillis());
                Game game = new Game(gameCreatedOn, teamOneName, teamTwoName);

                if (teamOneName.isEmpty() || teamTwoName.isEmpty()) {
                    toast("Sva polja moraju biti ispunjena");
                    return;
                }

                dialog.dismiss();

                if (db.addNewGame(gameCreatedOn, teamOneName, teamTwoName)) {
                    loadData();

                    Intent intent = new Intent(HomeActivity.this, GameActivity.class);
                    intent.putExtra(IntentData.GAME_CREATED_ON.toString(), game.getGameCreatedOn());
                    startActivity(intent);

                } else {
                    toast("Došlo je do greške");
                }

            }

        });

        dialog.show();
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
