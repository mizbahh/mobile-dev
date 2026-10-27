package com.example.guessinggame;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_WINS = "wins";
    private static final String KEY_LOSSES = "losses";
    private static final String KEY_WINNING_DOOR = "winningDoor";
    private static final String KEY_RESULT_TEXT = "resultText";

    private int wins = 0;
    private int losses = 0;
    private int winningDoor;
    private String resultText = "Pick a door!";

    private final Random random = new Random();

    private TextView resultTextView;
    private TextView scoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = findViewById(R.id.resultTextView);
        scoreTextView = findViewById(R.id.scoreTextView);

        if (savedInstanceState != null) {
            wins = savedInstanceState.getInt(KEY_WINS);
            losses = savedInstanceState.getInt(KEY_LOSSES);
            winningDoor = savedInstanceState.getInt(KEY_WINNING_DOOR);
            resultText = savedInstanceState.getString(KEY_RESULT_TEXT, "Pick a door!");
        } else {
            chooseWinningDoor();
        }

        updateDisplay();

        findViewById(R.id.door1Button).setOnClickListener(v -> playDoor(1));
        findViewById(R.id.door2Button).setOnClickListener(v -> playDoor(2));
        findViewById(R.id.door3Button).setOnClickListener(v -> playDoor(3));
    }

    private void chooseWinningDoor() {
        winningDoor = random.nextInt(3) + 1;
    }

    private void playDoor(int chosenDoor) {
        if (chosenDoor == winningDoor) {
            wins++;
            resultText = "You win!!";
        } else {
            losses++;
            resultText = "You lose!";
        }
        chooseWinningDoor();
        updateDisplay();
    }

    private void updateDisplay() {
        resultTextView.setText(resultText);
        scoreTextView.setText("Wins: " + wins + "; Losses: " + losses);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_WINS, wins);
        outState.putInt(KEY_LOSSES, losses);
        outState.putInt(KEY_WINNING_DOOR, winningDoor);
        outState.putString(KEY_RESULT_TEXT, resultText);
    }
}