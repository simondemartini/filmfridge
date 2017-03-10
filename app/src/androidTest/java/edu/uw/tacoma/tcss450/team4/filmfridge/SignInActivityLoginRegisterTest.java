package edu.uw.tacoma.tcss450.team4.filmfridge;

import android.support.test.espresso.Espresso;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.Random;

import edu.uw.tacoma.tcss450.team4.filmfridge.authenticate.SignInActivity;
import edu.uw.tacoma.tcss450.team4.filmfridge.settings.LocalSettings;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

/**
 * Test login of the app.
 * Created by Simon DeMartini on 3/9/17.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SignInActivityLoginRegisterTest {

    @Rule
    public ActivityTestRule<SignInActivity> mActivityRule = new ActivityTestRule<>(
            SignInActivity.class);

    @Test
    public void testARegisterValid() {
        //logout first
        LocalSettings localSettings = new LocalSettings(mActivityRule.getActivity());
        localSettings.setLoggedIn(false);

        onView(withId(R.id.register_button))
                .perform(click());
        Random random = new Random();
        //Generate an email address
        String email = "email" + (random.nextInt(7) + 1)
                + (random.nextInt(8) + 1) + (random.nextInt(9) + 1)
                + (random.nextInt(100) + 1) + (random.nextInt(4) + 1)
                + "@uw.edu";

        // Type text and then press the button.
        onView(withId(R.id.register_email))
                .perform(typeText(email));
        onView(withId(R.id.confirm_register_email))
                .perform(typeText(email));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.register_password))
                .perform(typeText("test1@#"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.confirm_register_password))
                .perform(typeText("test1@#"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.addUser_button))
                .perform(click());
        onView(withText("Registered Successfully."))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testARegisterMismatchEmail() {
        //logout first
        LocalSettings localSettings = new LocalSettings(mActivityRule.getActivity());
        localSettings.setLoggedIn(false);

        onView(withId(R.id.register_button))
                .perform(click());
        Random random = new Random();
        //Generate an email address
        String email = "email" + (random.nextInt(7) + 1)
                + (random.nextInt(8) + 1) + (random.nextInt(9) + 1)
                + (random.nextInt(100) + 1) + (random.nextInt(4) + 1)
                + "@uw.edu";

        // Type text and then press the button.
        onView(withId(R.id.register_email))
                .perform(typeText(email));
        onView(withId(R.id.confirm_register_email))
                .perform(typeText(email + "2"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.register_password))
                .perform(typeText("test1@#"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.confirm_register_password))
                .perform(typeText("test1@#"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.addUser_button))
                .perform(click());
        onView(withText("Emails do not match"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testARegisterInvalidEmail() {
        //logout first
        LocalSettings localSettings = new LocalSettings(mActivityRule.getActivity());
        localSettings.setLoggedIn(false);

        onView(withId(R.id.register_button))
                .perform(click());
        Random random = new Random();
        //Generate an email address
        String email = "email" + (random.nextInt(7) + 1)
                + (random.nextInt(8) + 1) + (random.nextInt(9) + 1)
                + (random.nextInt(100) + 1) + (random.nextInt(4) + 1)
                + "uw.edu";

        // Type text and then press the button.
        onView(withId(R.id.register_email))
                .perform(typeText(email));
        onView(withId(R.id.confirm_register_email))
                .perform(typeText(email));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.register_password))
                .perform(typeText("test1@#"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.confirm_register_password))
                .perform(typeText("test1@#"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.addUser_button))
                .perform(click());
        onView(withText("Enter a valid email address"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testARegisterEmptyEmail() {
        //logout first
        LocalSettings localSettings = new LocalSettings(mActivityRule.getActivity());
        localSettings.setLoggedIn(false);

        onView(withId(R.id.register_button))
                .perform(click());
        Random random = new Random();
        //Generate an email address
        String email = "email" + (random.nextInt(7) + 1)
                + (random.nextInt(8) + 1) + (random.nextInt(9) + 1)
                + (random.nextInt(100) + 1) + (random.nextInt(4) + 1)
                + "uw.edu";

        // Type text and then press the button.
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.register_password))
                .perform(typeText("test1@#"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.confirm_register_password))
                .perform(typeText("test1@#"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.addUser_button))
                .perform(click());
        onView(withText("Enter email"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testARegisterInvalidPassword() {
        //logout first
        LocalSettings localSettings = new LocalSettings(mActivityRule.getActivity());
        localSettings.setLoggedIn(false);

        onView(withId(R.id.register_button))
                .perform(click());
        Random random = new Random();
        //Generate an email address
        String email = "email" + (random.nextInt(7) + 1)
                + (random.nextInt(8) + 1) + (random.nextInt(9) + 1)
                + (random.nextInt(100) + 1) + (random.nextInt(4) + 1)
                + "@uw.edu";

        // Type text and then press the button.
        onView(withId(R.id.register_email))
                .perform(typeText(email));
        onView(withId(R.id.confirm_register_email))
                .perform(typeText(email));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.register_password))
                .perform(typeText("test1"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.confirm_register_password))
                .perform(typeText("test1"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.addUser_button))
                .perform(click());
        onView(withText("Enter password of at least 6 characters"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testARegisterMismatchPassword() {
        //logout first
        LocalSettings localSettings = new LocalSettings(mActivityRule.getActivity());
        localSettings.setLoggedIn(false);

        onView(withId(R.id.register_button))
                .perform(click());
        Random random = new Random();
        //Generate an email address
        String email = "email" + (random.nextInt(7) + 1)
                + (random.nextInt(8) + 1) + (random.nextInt(9) + 1)
                + (random.nextInt(100) + 1) + (random.nextInt(4) + 1)
                + "@uw.edu";

        // Type text and then press the button.
        onView(withId(R.id.register_email))
                .perform(typeText(email));
        onView(withId(R.id.confirm_register_email))
                .perform(typeText(email));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.register_password))
                .perform(typeText("test1@#"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.confirm_register_password))
                .perform(typeText("test1@#2"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.addUser_button))
                .perform(click());
        onView(withText("Passwords do not match"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testARegisterEmptyPassword() {
        //logout first
        LocalSettings localSettings = new LocalSettings(mActivityRule.getActivity());
        localSettings.setLoggedIn(false);

        onView(withId(R.id.register_button))
                .perform(click());
        Random random = new Random();
        //Generate an email address
        String email = "email" + (random.nextInt(7) + 1)
                + (random.nextInt(8) + 1) + (random.nextInt(9) + 1)
                + (random.nextInt(100) + 1) + (random.nextInt(4) + 1)
                + "@uw.edu";

        // Type text and then press the button.
        onView(withId(R.id.register_email))
                .perform(typeText(email));
        onView(withId(R.id.confirm_register_email))
                .perform(typeText(email));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.addUser_button))
                .perform(click());
        onView(withText("Enter password"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));
    }
}
