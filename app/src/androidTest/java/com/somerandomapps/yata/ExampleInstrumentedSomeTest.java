package com.somerandomapps.yata;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedSomeTest {

    String stringToBetyped;

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.somerandomapps.yata", appContext.getPackageName());
    }

    @Rule
    public ActivityTestRule<MainActivity_> mActivityRule = new ActivityTestRule<>(MainActivity_.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        stringToBetyped = "Espresso";
    }

    @Test
    public void testAddingOneItem() {
        onView(withId(R.id.lvItems)).check(matches(com.somerandomapps.yata.Matchers.withListSize(0)));
        onView(withId(R.id.tvNoItems)).check(matches(isDisplayed()));
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.etName)).perform(typeText("Test 1"));
        onView(withId(android.R.id.button2)).perform(click());
        onView(withId(R.id.tvNoItems)).check(matches(isDisplayed()));
        onView(withId(R.id.lvItems)).check(matches(com.somerandomapps.yata.Matchers.withListSize(0)));

        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.etName)).perform(typeText("Test 2"));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.lvItems)).check(matches(com.somerandomapps.yata.Matchers.withListSize(1)));
    }

}
