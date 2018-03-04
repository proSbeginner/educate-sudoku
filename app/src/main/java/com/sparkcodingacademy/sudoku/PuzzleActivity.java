package com.sparkcodingacademy.sudoku;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by proSb on 3/2/2018.
 */

public class PuzzleActivity extends Activity {
    protected static final String KEY_DIFFICULTY = "KEY_DIFFICULTY";
    protected static final int DIFFICULTY_EASY = 0;
    protected static final int DIFFICULTY_MEDIUM = 1;
    protected static final int DIFFICULTY_HARD = 2;

    private int puzzle[] = new int[9 * 9];

    private final String EASY_PUZZLE =
            "000070000080000273009036800" + "001247000807050309000389400" + "003510700124000090000020000";
    private final String MEDIUM_PUZZLE =
            "300000056400000000000897000" + "002080400006103800005070300" + "000462000000000008510000007";
    private final String HARD_PUZZLE =
            "462900000900100000705800000" + "631000000000000000000000259" + "000005901000008003000001542";

    private PuzzleView puzzleView;

    private final int usedTiles[][][] = new int[9][9][];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // key for read and default value
        int diff = getIntent().getIntExtra(KEY_DIFFICULTY, DIFFICULTY_EASY);

        puzzle = getPuzzle(diff);
        calculateUsedTiles();

        puzzleView = new PuzzleView(this);
        setContentView(puzzleView);
        puzzleView.requestFocus();

        MyLog.log("phai", "on create at PuzzleActivity");
    }

    protected String getTileString(int x , int y) {
        int tile = getTile(x, y);
        if(tile == 0) {
            return "";
        }
        return String.valueOf(tile);
    }

    private int getTile(int x, int y) {
        return puzzle[y * 9 + x];
    }

    private int[] getPuzzle(int diff) {
        String puzzleStr;
        switch (diff) {
            case DIFFICULTY_MEDIUM:
                puzzleStr = MEDIUM_PUZZLE;
                break;
            case DIFFICULTY_HARD:
                puzzleStr = HARD_PUZZLE;
                break;
            default:
                puzzleStr = EASY_PUZZLE;
        }
        return fromPuaaleString(puzzleStr);
    }

    private int[] fromPuaaleString(String puzzleStr) {
        int[] puz = new int[puzzleStr.length()];
        for(int i = 0; i < puz.length; i++) {
            puz[i] = puzzleStr.charAt(i) - '0';
        }
        return puz;
    }

    protected boolean setTileIfValid(int x, int y, int value) {
//        int tiles[] = getUsedTiles(x, y);
//        if(value != 0) {
//            for(int tile : tiles) {
//                // ถ้าตัวเลขที่ต้องการเติมได้ถูกใช้ไปแล้ว ให้ออกจากเมธอดทันที
//                if(tile == value) {
//                    return false;
//                }
//            }
//        }
//
//        setTile(x, y, value);
        calculateUsedTiles();

        return true;
    }

    private void calculateUsedTiles() {
        for(int x = 0; x < 9; x++) {
            for(int y = 0; y < 9; y++) {
                usedTiles[x][y] = calculateUsedTiles(x, y);
            }
        }
    }

    private int[] calculateUsedTiles(int x, int y) {
        int[] result = new int[9];

        // รวบรวมตัวเลขที่ใช้ไปแล้วในแถวเดียวกัน
        for(int i = 0;i < 9; i++) {
            if(i == y) {
                continue;
            }
            int tile = getTile(x, i);
            if(tile != 0) { // ไม่เอาเลข 0 เพราะคือช่องว่าง
                result[tile - 1] = tile;
            }
        }

        //

        return result;
    }

    protected void showKeypadOrError(int x, int y) {
        int tiles[] = getUsedTiles(x, y);
        MyLog.log("phai", "tiles length is " + tiles.length);

        if(!invalidTiles(tiles.length)) {
            Dialog dialog = new Keypad(this, tiles, puzzleView);
            dialog.show();
        } else {
            Toast toast =Toast.makeText(this, R.string.no_moves_label, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private boolean invalidTiles(int tilesLength) {
        return tilesLength >= 9;
    }

    private int[] getUsedTiles(int x, int y) {
        return usedTiles[x][y];
    }
}
