package com.ogbizi.android_editable_textview;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import timber.log.Timber;

/**
 * Custom edit text view to handle some common usage patterns
 */
public class EditTextView extends AppCompatAutoCompleteTextView {
    private Behavior mBehavior;
    private boolean mFocused = false;
    private String mValue = "";

    public EditTextView(Context context) {
        this(context, null);
    }

    public EditTextView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public EditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Initialize view, debug logging and listeners
     */
    public void init() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        initListeners();
    }

    /**
     * Set up listeners
     */
    protected void initListeners() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                Timber.i("before text change: %s, %d, %d, %d", s, start, count,
                         after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                Timber.i("on text change: %s, %d, %d, %d", s, start, count,
                         before);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Timber.i("after text change: %s", s);
                if (mFocused) setValue(s.toString());
                if (mBehavior != null) setDisplayText(mBehavior.onTextChanged(s));
            }
        });
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect rect) {
        mFocused = focused;
        if(mBehavior != null) {
            String displayText;
            String currValue = getValue();
            String currText = getText().toString();
            Timber.d("focus: %s %s %s", focused, currValue, currText);
            if (focused) {
                displayText = mBehavior.onFocusIn(direction, currValue, currText);
            } else {
                displayText = mBehavior.onFocusOut(direction, currValue, currText);
            }
            setDisplayText(displayText);
        }
        super.onFocusChanged(focused, direction, rect);
    }

    /**
     * Get internal edit text value
     *
     * @return the internal value
     */
    public String getValue() {
        return mValue;
    }

    /**
     * Set the internal edit text value.
     *
     * @param text the new internal value
     */
    private void setValue(String text) {
        mValue = text;
    }

    /**
     * Set the view display text if given string is not null, which
     * will trigger {@link Behavior#onTextChanged} after text change
     *
     * @param text the text string to set
     */
    public void setDisplayText(@Nullable final String text) {
        final int animateHalfDuration = 48;
        final Integer hiddenTextColor = Color.argb(0, 0, 0, 0);
        final Integer currentTextColor = getCurrentTextColor();
        if (text != null && !text.equals(getText().toString())) {
            Animator animator = animateTextColor(
                    animateHalfDuration, currentTextColor, hiddenTextColor);
            animator.addListener(new BaseAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    setText(text);
                    setSelection(text.length());
                    animateTextColor(animateHalfDuration, hiddenTextColor, currentTextColor);
                }
            });
        }
    }

    /**
     * Animate the textColor property of this instance
     *
     * @param duration the length of the transition in milliseconds
     * @param colors the list of colors to switch through
     * @return the animator used
     */
    public Animator animateTextColor(long duration, Integer ...colors) {
        ObjectAnimator animator = ObjectAnimator
                .ofObject(this, "textColor",
                          new ArgbEvaluator(), (Object[]) colors);
        animator.setDuration(duration);
        animator.start();
        return animator;
    }

    /**
     * Set the edit text view behavior
     *
     * @param behavior the callback used to implement behavior
     */
    public void setBehavior(Behavior behavior) {
        mBehavior = behavior;
        mBehavior.onAttach(this);
    }

    /**
     * The edit text view behavior
     */
    public interface Behavior {
        /**
         * Called when behaviour is attached to {@link EditTextView}
         *
         * @param view the swappable image view attached
         */
        void onAttach(EditTextView view);

        /**
         * Called when the edit text view gains focus.
         *
         * @param direction the direction from {@link #onFocusChanged}
         * @param value the current value from {@link #getValue}
         * @param text the current text from {@link #getText}
         * @return the text to display or null to ignore
         */
        @Nullable
        String onFocusIn(int direction, String value, String text);

        /**
         * Called when changes have been made to the edit text view.
         * Returning a value or calling {@link EditTextView#setText}
         * will call this method again recursively.
         * Therefore to avoid getting stuck in an infinite loop,
         * return NULL when no change is to be made to display text.
         *
         * @param s the editable from {@link TextWatcher#afterTextChanged}
         * @return the text to display or null to ignore
         */
        @Nullable
        String onTextChanged(Editable s);

        /**
         * Called when the edit text view drops focus.
         *
         * @param direction the direction from {@link #onFocusChanged}
         * @param value the current value from {@link #getValue}
         * @param text the current text from {@link #getText}
         * @return the text to display or null to ignore
         */
        @Nullable
        String onFocusOut(int direction, String value, String text);
    }
}
