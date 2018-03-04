package com.somerandomapps.yata;

import android.view.View;
import android.widget.ListView;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

class Matchers {
    public static Matcher<View> withListSize(final int size) {
        return new TypeSafeMatcher<View>() {
            private int count;
            @Override
            public boolean matchesSafely(final View view) {
                count = ((ListView) view).getCount();
                return count == size;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("ListView should have " + size + " items but had " + count);
            }
        };
    }
}