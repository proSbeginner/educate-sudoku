package com.sparkcodingacademy.sudoku;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

/**
 * Created by proSb on 3/2/2018.
 */

public class Keypad extends Dialog {
    private final int[] usedTiles;
    private final PuzzleView puzzleView;

    // อ้างอิงไปยังปุ่มต่างๆในหน้าจอ keypad
    private final View keys[] = new View[10];

    public Keypad(Context context, int[] usedTiles, PuzzleView puzzleView) {
        super(context);
        this.usedTiles = usedTiles;
        this.puzzleView = puzzleView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.keypad_title);
        setContentView(R.layout.keypad);

        findViews();

        for(int tile : usedTiles) {
            keys[tile].setVisibility(View.INVISIBLE);
        }

        setListeners();
    }

    private void findViews() {
        keys[0] = findViewById(R.id.keypad_clear);
        keys[1] = findViewById(R.id.keypad_1);
        keys[2] = findViewById(R.id.keypad_2);
        keys[3] = findViewById(R.id.keypad_3);
        keys[4] = findViewById(R.id.keypad_4);
        keys[5] = findViewById(R.id.keypad_5);
        keys[6] = findViewById(R.id.keypad_6);
        keys[7] = findViewById(R.id.keypad_7);
        keys[8] = findViewById(R.id.keypad_8);
        keys[9] = findViewById(R.id.keypad_9);
    }

    private void setListeners() {
        for(int i = 0; i < keys.length; i++) {
            final int tile = i;
            keys[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    returnResult(tile);
                }
            });
        }
    }

    private void returnResult(int tile) {
        puzzleView.setSelectedTile(tile);
        dismiss(); // close this dialog
    }
}
