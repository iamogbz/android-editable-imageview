package com.ogbizi.android_editable_textview;

import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;

import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class AsyncEditTextBehavior implements EditTextView.Behavior {
    private EditTextView mView;

    private void asyncTask(final String value) {
        final int time = 4; // seconds
        mView.animateTextColor(time * 1000, Color.BLACK, Color.BLUE, Color.CYAN,
                               Color.GREEN, Color.MAGENTA, Color.BLACK);
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(time);
                    final String finalValue = value + " (async)";
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mView.setDisplayText(finalValue);
                        }
                    });
                } catch (InterruptedException e) {
                    Timber.e(e);
                }
            }
        }).start();
    }

    @Override
    public void onAttach(EditTextView view) {
        mView = view;
    }

    @Nullable
    @Override
    public String onFocusIn(int direction, String value, String text) {
        return text.equals(value) ? null : value;
    }

    @Nullable
    @Override
    public String onTextChanged(Editable s) {
        return null;
    }

    @Nullable
    @Override
    public String onFocusOut(int direction, String value, String text) {
        boolean isFalsy = value == null || value.isEmpty();
        if(!isFalsy) asyncTask(value);
        return text.equals(value) ? null : value;
    }
}
