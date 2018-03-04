package com.sparkcodingacademy.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by proSb on 3/2/2018.
 */

public class PuzzleView extends View {
    private final PuzzleActivity puzzleActivity;

    private float width;
    private float height;
    private int selX;
    private int selY;
    private final Rect selRect = new Rect();

    public PuzzleView(Context context) {
        super(context);

        this.puzzleActivity = (PuzzleActivity)context;
        setFocusable(true); // ?
        setFocusableInTouchMode(true);  // ?
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w / 9f;
        height = h / 9f;

        // for draw cursor (D-pad cursor)
        getRect(selX, selY, selRect);

        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void getRect(int x, int y, Rect rect) {
        rect.set((int)(x * width), (int)(y * height), (int)((x * width) + width), (int)((y * height) + height));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        MyLog.log("phai", "on draw at PuzzleView");

        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.puzzle_background));
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        drawTableLines(canvas);
        drawNumber(canvas);
        drawCursor(canvas);
    }

    private void drawTableLines(Canvas canvas) {
        Paint dark = new Paint();
        dark.setColor(getResources().getColor(R.color.puzzle_dark));

        Paint highlight = new Paint();
        highlight.setColor(getResources().getColor(R.color.puzzle_highlight));

        Paint light = new Paint();
        light.setColor(getResources().getColor(R.color.puzzle_light));

        // วาดเส้นตารางย่อย
        for(int i = 0;i < 9; i++) {
            canvas.drawLine(0, i * height, getWidth(), i * height, light);
            canvas.drawLine(0, i * height + 1, getWidth(), i * height + 1, highlight);
            canvas.drawLine(i * width, 0, i * width, getHeight(), light);
            canvas.drawLine(i * width + 1, 0, i * width + 1, getHeight(), highlight);
        }

        // วาดเส้นตารางหลัก
        for(int i = 0; i < 9; i++) {
             if(i % 3 != 0) {
                 continue;
             }
             canvas.drawLine(0, i * height, getWidth(), i * height, dark);
             canvas.drawLine(0, i * height + 1, getWidth(), i * height + 1, highlight);
             canvas.drawLine(i * width, 0, i * width, getHeight(), dark);
             canvas.drawLine(i * width + 1, 0, i * width + 1, getHeight(), highlight);
        }
    }

    private void drawNumber(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getResources().getColor(R.color.puzzle_foreground));
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(height * 0.75f);
        paint.setTextScaleX(width / height);
        paint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float x = width / 2;
        float y = height / 2 - (fontMetrics.ascent + fontMetrics.descent) / 2;

        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                canvas.drawText(puzzleActivity.getTileString(i, j), i * width + x, j * height + y, paint);
            }
        }
    }

    private void drawCursor(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.puzzle_selected));
        canvas.drawRect(selRect, paint);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            // for D-pad
            case KeyEvent.KEYCODE_DPAD_UP:
                select(selX, selY - 1);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                select(selX, selY + 1);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                select(selX - 1, selY);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                select(selX + 1, selY );
                break;

            // for keyboard
            case KeyEvent.KEYCODE_0:
            case KeyEvent.KEYCODE_SPACE:
                setSelectedTile(0);
                break;
            case KeyEvent.KEYCODE_1:
                setSelectedTile(1);
                break;
            case KeyEvent.KEYCODE_2:
                setSelectedTile(2);
                break;
            case KeyEvent.KEYCODE_3:
                setSelectedTile(3);
                break;
            case KeyEvent.KEYCODE_4:
                setSelectedTile(4);
                break;
            case KeyEvent.KEYCODE_5:
                setSelectedTile(5);
                break;
            case KeyEvent.KEYCODE_6:
                setSelectedTile(6);
                break;
            case KeyEvent.KEYCODE_7:
                setSelectedTile(7);
                break;
            case KeyEvent.KEYCODE_8:
                setSelectedTile(8);
                break;
            case KeyEvent.KEYCODE_9:
                setSelectedTile(9);
                break;
            default:
                return super.onKeyDown(keyCode, event);
        }

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() != MotionEvent.ACTION_DOWN) {
            return super.onTouchEvent(event);
        }

        // move the cursor
        select((int)(event.getX() / width), (int)(event.getY() / height));
        puzzleActivity.showKeypadOrError(selX, selY);

        return true;
    }

    private void select(int x, int y) {
        invalidate(selRect);

        selX = Math.min(Math.max(x, 0), 8); // 0 to 8
        selY = Math.min(Math.max(y, 0), 8); // 0 to 8
        getRect(selX, selY, selRect);

        invalidate(selRect);
    }

    protected void setSelectedTile(int number) {
        if(puzzleActivity.setTileIfValid(selX, selY, number)) {
            invalidate(); // draw the screen
        }
    }
}
