package com.somerandomapps.yata;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.somerandomapps.yata.repository.AppDatabase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static com.somerandomapps.yata.Matchers.*;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class ExampleInstrumentedSomeTest {

    @Before
    public void before() {
        Context context = InstrumentationRegistry.getContext();
        AppDatabase db = AppDatabase.getAppDatabase(context);
        db.todoItemDao().removeAll();
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.somerandomapps.yata", appContext.getPackageName());
    }

    @Rule
    public ActivityTestRule<MainActivity_> mActivityRule = new ActivityTestRule<>(MainActivity_.class);

    @Test
    public void testAddingOneItem() {
        onView(withId(R.id.tvNoItems)).check(matches(isDisplayed()));

        onView(withId(R.id.lvItems)).check(matches(withListSize(0)));
        onView(withId(R.id.tvNoItems)).check(matches(isDisplayed()));
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.etName)).perform(typeText("Test 1"));
        onView(withId(android.R.id.button2)).perform(click());
        onView(withId(R.id.tvNoItems)).check(matches(isDisplayed()));
        onView(withId(R.id.lvItems)).check(matches(withListSize(0)));

        addItemWithName("Test 2");
        onView(withId(R.id.lvItems)).check(matches(withListSize(1)));
        onView(withId(R.id.tvNoItems)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)));
    }

    @Test
    public void testMarkDone() {
        addItemWithName("Item 1");
        addItemWithName("Item 2");
        onView(withId(R.id.lvItems)).check(matches(itemBeforeItem(new String[]{"Item 1", "Item 2"})));
        markDoneItemWithText("Item 1");
        onView(withId(R.id.lvItems)).check(matches(itemBeforeItem(new String[]{"Item 2", "Item 1"})));
    }

    private void markDoneItemWithText(String text) {
        onView(allOf(withId(R.id.cbDone), hasSibling(withText(text)))).perform(click());
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testWithDoneDate() {
        addItemWithName("Item 1");
        addItemWithNameAndDeadline("Item 2");

        onView(withId(R.id.lvItems)).check(matches(itemBeforeItem(new String[]{"Item 1", "Item 2"})));
        markDoneItemWithText("Item 1");
        onView(withId(R.id.lvItems)).check(matches(itemBeforeItem(new String[]{"Item 2", "Item 1"})));

        onView(withId(R.id.lvItems)).check(matches(containsItemWithNameAndDeadline("Item 2", new Date())));
    }

    private void addItemWithNameAndDeadline(String name) {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.etName)).perform(typeText(name));

        onView(withId(R.id.swHasDeadline)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
    }

    private void addItemWithName(String name) {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.etName)).perform(typeText(name));
        onView(withId(android.R.id.button1)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testClearingDoneItems() throws InterruptedException {
        addItemWithName("Item 1");
        addItemWithName("Item 2");
        addItemWithName("Item 3");
        addItemWithName("Item 4");
        onView(withId(R.id.lvItems)).check(matches(itemBeforeItem(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"})));
        markDoneItemWithText("Item 1");


        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        Thread.sleep(200);
        onView(withText(R.string.action_clearDone)).perform(click());
        onView(withId(R.id.lvItems)).check(matches(itemBeforeItem(new String[]{"Item 2", "Item 3", "Item 4"})));

        markDoneItemWithText("Item 2");
        markDoneItemWithText("Item 3");
        markDoneItemWithText("Item 4");
        onView(withId(R.id.lvItems)).check(matches(itemBeforeItem(new String[]{"Item 2", "Item 3", "Item 4"})));

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        Thread.sleep(200);
        onView(withText(R.string.action_clearDone)).perform(click());
        onView(withId(R.id.tvNoItems)).check(matches(isDisplayed()));
    }

}
