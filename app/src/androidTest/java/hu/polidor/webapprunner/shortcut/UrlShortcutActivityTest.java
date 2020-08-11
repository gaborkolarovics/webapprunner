package hu.polidor.webapprunner.shortcut;

import android.view.View;
import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import hu.polidor.webapprunner.R;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;


@RunWith(AndroidJUnit4.class)
public class UrlShortcutActivityTest {

    @Rule
    public ActivityTestRule<UrlShortcutActivity> rule  = new  ActivityTestRule<>(UrlShortcutActivity.class);


    @Test
    public void editTextIsPresent() throws Exception {
        UrlShortcutActivity activity = rule.getActivity();
        EditText etUrl = activity.findViewById(R.id.etUrl);
        assertThat(etUrl,notNullValue());
    }
}