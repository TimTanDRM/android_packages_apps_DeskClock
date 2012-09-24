/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.deskclock;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.Calendar;


/**
 * TODO: Insert description here. (generated by isaackatz)
 */
public class ClockFragment extends DeskClockFragment {

    private static final String BUTTONS_HIDDEN_KEY = "buttons_hidden";

    private final static String DATE_FORMAT = "EEEE, MMMM d";

    View mButtons;
    TextView mNextAlarm;
    private TextView mDateDisplay;
    boolean mButtonsHidden = false;
    View mDigitalClock, mAnalogClock;

    public ClockFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle icicle) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.clock_fragment, container, false);
        mButtons = v.findViewById(R.id.clock_buttons);
        mNextAlarm = (TextView)v.findViewById(R.id.nextAlarm);
        mDateDisplay = (TextView) v.findViewById(R.id.date);
        mDigitalClock = v.findViewById(R.id.digital_clock);
        mAnalogClock = v.findViewById(R.id.analog_clock);
        if (icicle != null) {
            mButtonsHidden = icicle.getBoolean(BUTTONS_HIDDEN_KEY, false);
        }
        refreshAlarm();
        return v;
    }

    @Override
    public void onResume () {
        super.onResume();
        refreshAlarm();
  //      updateDate();   // No date at this point
        mButtons.setAlpha(mButtonsHidden ? 0 : 1);
        setClockStyle();


    }


    @Override
    public void onSaveInstanceState (Bundle outState) {
        outState.putBoolean(BUTTONS_HIDDEN_KEY, mButtonsHidden);
        super.onSaveInstanceState(outState);
    }

    private void setClockStyle() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String style = sharedPref.getString(SettingsActivity.KEY_CLOCK_STYLE, "analog");
        if (style.equals("analog")) {
            mDigitalClock.setVisibility(View.GONE);
            mAnalogClock.setVisibility(View.VISIBLE);
        } else if (style.equals("digital")) {
            mDigitalClock.setVisibility(View.VISIBLE);
            mAnalogClock.setVisibility(View.GONE);
        }
    }

    private void refreshAlarm() {
        if (mNextAlarm == null) return;

        mNextAlarm.setVisibility(View.GONE);
/* No next alarm at this point
        String nextAlarm = Settings.System.getString(getActivity().getContentResolver(),
                Settings.System.NEXT_ALARM_FORMATTED);
        if (!TextUtils.isEmpty(nextAlarm)) {
            mNextAlarm.setText(getString(R.string.control_set_alarm_with_existing, nextAlarm));
            mNextAlarm.setVisibility(View.VISIBLE);
        } else  {
            mNextAlarm.setVisibility(View.INVISIBLE);
        }*/
    }

    private void updateDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());

        CharSequence newDate = DateFormat.format(DATE_FORMAT, cal);
        mDateDisplay.setVisibility(View.VISIBLE);
        mDateDisplay.setText(newDate);
    }

    public void showButtons(boolean show) {
        if (mButtons == null) {
            return;
        }
        if (show && mButtonsHidden) {
            mButtons.startAnimation(
                    AnimationUtils.loadAnimation(getActivity(), R.anim.unhide));
            mButtonsHidden = false;
        } else if (!show && !mButtonsHidden) {
            mButtons.startAnimation(
                    AnimationUtils.loadAnimation(getActivity(), R.anim.hide));
            mButtonsHidden = true;
        }
    }
}
