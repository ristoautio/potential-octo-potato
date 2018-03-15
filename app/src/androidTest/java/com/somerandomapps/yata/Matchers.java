package com.somerandomapps.yata;

import android.view.View;
import android.widget.ListView;
import com.somerandomapps.yata.repository.TodoItem;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Date;

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

    public static Matcher<View> containsItemWithNameAndDeadline(final String itemName, final Date deadline) {
        return new TypeSafeMatcher<View>() {
            ListView view;
            private String expected;
            private String actual;

            @Override
            protected boolean matchesSafely(View item) {
                view = (ListView) item;
                for (int i = 0; i < view.getCount(); i++) {
                    TodoItem listItem = (TodoItem) view.getItemAtPosition(i);
                    if (listItem.isHasDeadline() && listItem.getName().equalsIgnoreCase(itemName)) {
                        long diff = listItem.getDeadline().getTime() - deadline.getTime();
                        if ((diff > -5000) && (diff < 5000)) {
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Could not find item with name: " + itemName + " and deadline: " + deadline.toString());
            }
        };
    }

    public static Matcher<View> itemBeforeItem(final String[] labels) {
        return new TypeSafeMatcher<View>() {
            ListView view;
            private String expected;
            private String actual;
            private int pos;

            @Override
            protected boolean matchesSafely(View item) {
                view = (ListView) item;

                for (int i = 0; i < labels.length; i++) {
                    if (!checkLabelAtPosition(i)){
                        pos = i;
                        return false;
                    }
                }

                return true;
            }

            private boolean checkLabelAtPosition(int index) {
                TodoItem todoItem = (TodoItem) view.getItemAtPosition(index);
                expected = labels[index];
                actual = todoItem.getName();
                return expected.contentEquals(actual);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Expected " + expected + " but was " + actual + " at position " + pos);
            }
        };
    }

}