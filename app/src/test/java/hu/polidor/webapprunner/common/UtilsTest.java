package hu.polidor.webapprunner.common;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UtilsTest {

    @Test
    public void isOverDate() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -5);
        long targetDate = cal.getTime().getTime();

        boolean actualBefore = Utils.isOverDate(targetDate, 1);
        boolean actualAfter = Utils.isOverDate(targetDate, 10);
        assertTrue("Util method isOverDate failed", actualBefore);
        assertFalse("Util method isOverDate failed", actualAfter);
    }

}