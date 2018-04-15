package seedu.address.model.appointment;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

//@@author Godxin-test
public class DateTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Date(null));
    }

    @Test
    public void constructor_invalidAddress_throwsIllegalArgumentException() {
        String invalidDate = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Date(invalidDate));
    }

    @Test
    public void isValidDate() {
        // null date
        Assert.assertThrows(NullPointerException.class, () -> Date.isValidDate(null));

        // invalid date
        assertFalse(Date.isValidDate("")); // empty string
        assertFalse(Date.isValidDate(" ")); // spaces only
        assertFalse(Date.isValidDate("2018/01/02")); //wrong format
        // -- invalid year --
        assertFalse(Date.isValidYear(2017)); // year below lower bound
        //-- invalid days in month --
        assertFalse(Date.isValidDaysInMonth("2020-04-31")); //leap year small month days overflow
        assertFalse(Date.isValidDaysInMonth("2020-05-32")); //leap year big month days overflow
        assertFalse(Date.isValidDaysInMonth("2020-02-30")); //leap year Feb days overflow

        assertFalse(Date.isValidDaysInMonth("2019-09-31")); //non-leap year small month days overflow
        assertFalse(Date.isValidDaysInMonth("2018-11-32")); //non-leap year big month days overflow
        assertFalse(Date.isValidDaysInMonth("2021-02-29")); //non-leap year Feb days overflow

        // valid date
        assertTrue(Date.isValidDate("2018-01-02")); //standard format for a valid date
        assertTrue(Date.isValidDate("2019-02-03")); // date in the next year
        // -- valid year --
        assertTrue(Date.isValidYear(2018)); // current year
        assertTrue(Date.isValidYear(2020)); // later year
        //-- valid days in month --
        assertTrue(Date.isValidDaysInMonth("2020-02-29")); //leap year Feb
        assertTrue(Date.isValidDaysInMonth("2020-01-31")); //leap year big month
        assertTrue(Date.isValidDaysInMonth("2020-06-30")); //leap year big month

        assertTrue(Date.isValidDaysInMonth("2018-02-28")); //non-leap year Feb
        assertTrue(Date.isValidDaysInMonth("2019-12-31")); //non-leap year big month
        assertTrue(Date.isValidDaysInMonth("2019-11-30")); //non-leap year small month
    }
}
