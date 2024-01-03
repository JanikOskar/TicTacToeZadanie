package com.example.za;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class PvCActivity extends AppCompatActivity {

    private GridLayout gameGridLayout;
    private char currentPlayer = 'X';
    private boolean gameOver = false;
    private char[][] board = new char[3][3];
    private boolean[][] isFieldOccupied = new boolean[3][3];
    private char selectedPlayer = 'X';

    private boolean isPlayerXHuman = true; // Czy gracz X jest człowiekiem
    private boolean isPlayerOHuman = false; // Czy gracz O jest człowiekiem
    private boolean isPlayerOTurn = false; // Czyja kolej na ruch - początkowo ustawione na gracza X


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity_pvc);

        gameGridLayout = findViewById(R.id.gameGridLayout);
        initializeGameBoard();
        updatePlayerTurnLabel();
    }

    public void onButtonClick(View view) {
        if (view.getId() == R.id.resetButton) {
            gameOver = false;
            resetGameBoard();
            initializeGameBoard();

        }
    }

    private void initializeGameBoard() {
        for (int i = 0; i < gameGridLayout.getChildCount(); i++) {
            View child = gameGridLayout.getChildAt(i);

            if (child instanceof Button) {
                Button button = (Button) child;
                button.setOnClickListener(view -> {
                    if (!gameOver) {
                        onGameButtonClick(button);
                    }
                });
            }
        }
    }

    private void togglePlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    private void updatePlayerTurnLabel() {
        String turnLabel = "Tura gracza: " + currentPlayer;
        TextView playerTurnLabel = findViewById(R.id.playerTurnLabel);
        playerTurnLabel.setText(turnLabel);
    }

    private boolean checkForWinner() {
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer) ||
                    (board[0][i] == currentPlayer && board[1][i] == currentPlayer && board[2][i] == currentPlayer)) {
                return true;
            }
        }

        if ((board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) ||
                (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer)) {
            return true;
        }

        return false;
    }
    private void resetGameBoard() {
        currentPlayer = 'X';
        gameOver = false;
        clearButtonText();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
                isFieldOccupied[i][j] = false;
            }
        }

        updatePlayerTurnLabel();
    }

    private void clearButtonText() {
        for (int i = 0; i < gameGridLayout.getChildCount(); i++) {
            View child = gameGridLayout.getChildAt(i);

            if (child instanceof Button) {
                Button button = (Button) child;
                button.setText("");
                button.setEnabled(true);
            }
        }
    }

    private void updateButtons() {
        for (int i = 0; i < gameGridLayout.getChildCount(); i++) {
            View child = gameGridLayout.getChildAt(i);
            if (child instanceof Button) {
                Button button = (Button) child;
                int row = i / 3;
                int col = i % 3;

                if (isFieldOccupied[row][col]) {
                    button.setText(String.valueOf(board[row][col]));
                } else {
                    button.setText("");
                }
            }
        }
    }
    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!isFieldOccupied[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    private void displayDraw() {
        TextView playerTurnLabel = findViewById(R.id.playerTurnLabel);
        playerTurnLabel.setText("Remis!");
    }

    private void onGameButtonClick(Button button) {
        int buttonId = button.getId();

        if (buttonId >= R.id.button1 && buttonId <= R.id.button9) {
            int row = (buttonId - R.id.button1) / 3;
            int col = (buttonId - R.id.button1) % 3;

            if (!isFieldOccupied[row][col]) {
                isFieldOccupied[row][col] = true;
                board[row][col] = currentPlayer;
                if (checkForWinner()) {
                    gameOver = true;
                    displayWinner();
                    updateButtons();
                } else if (isBoardFull()) {
                    gameOver = true;
                    displayDraw();
                    updateButtons();
                } else {
                    togglePlayer();
                    updatePlayerTurnLabel();
                    isPlayerXHuman = false;
                    if (!isPlayerXHuman){
                        // Tutaj dodaj kod, aby komputer wykonał ruch
                        Log.d("dsa","sda");

                        makeComputerMove();
                    }
                    updateButtons();
                }
            }
        }
    }
    private void makeComputerMove() {
        // Szukaj dostępnych pustych pól
        int availableMoves = 0;
        int[] availableRows = new int[9];
        int[] availableCols = new int[9];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!isFieldOccupied[i][j]) {
                    availableRows[availableMoves] = i;
                    availableCols[availableMoves] = j;
                    availableMoves++;
                }
            }
        }

        if (availableMoves > 0) {
            // Wybierz losowe dostępne pole
            Random random = new Random();
            int randomIndex = random.nextInt(availableMoves);
            int row = availableRows[randomIndex];
            int col = availableCols[randomIndex];

            // Oznacz pole jako zajęte przez komputer
            isFieldOccupied[row][col] = true;
            board[row][col] = currentPlayer;

            // Sprawdź warunki wygranej lub remisu
            if (checkForWinner()) {
                gameOver = true;
                displayWinner();
                updateButtons();
            } else if (isBoardFull()) {
                gameOver = true;
                displayDraw();
                updateButtons();
            } else {
                // Przełącz gracza i zaktualizuj etykietę
                togglePlayer();
                updatePlayerTurnLabel();
            }
        }
    }
    private void displayWinner() {
        String winnerMessage;
        if (currentPlayer == 'X') {
            winnerMessage = "Wygrał gracz X!";
        } else {
            winnerMessage = "Wygrał gracz O!";
        }

        TextView playerTurnLabel = findViewById(R.id.playerTurnLabel);
        playerTurnLabel.setText(winnerMessage);

        disableButtons();
    }

    private void disableButtons() {
        for (int i = 0; i < gameGridLayout.getChildCount(); i++) {
            View child = gameGridLayout.getChildAt(i);
            if (child instanceof Button) {
                Button button = (Button) child;
                button.setEnabled(false);
            }
        }
    }
}
