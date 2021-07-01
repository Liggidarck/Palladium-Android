package com.george.vector.root.main;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.george.vector.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RootMainActivityTest {

    @Rule
    public ActivityTestRule<RootMainActivity> activityActivityTestRule = new ActivityTestRule<>(RootMainActivity.class);

    @Test
    public void AddTaskBtnClick() {
        onView(withId(R.id.fab_add_root)).perform(click()).check(matches(isDisplayed()));
    }

}