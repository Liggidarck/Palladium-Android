package com.george.vector.auth;

import android.support.test.rule.ActivityTestRule;

import com.george.vector.R;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class ActivityLoginTest {

    @Rule
    public ActivityTestRule<ActivityLogin> activityActivityTestRule = new ActivityTestRule<>(ActivityLogin.class);

    @Test
    public void clickLoginBtn() {
        onView(withId(R.id.btn_login)).perform(click()).check(matches(isDisplayed()));
    }

}