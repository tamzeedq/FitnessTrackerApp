package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WeekTest {
    private Week week;

    @BeforeEach
    void runBefore() {
        week = new Week();
    }

    @Test
    void testGetLog() {
        assertEquals(null, week.getLog("lundi"));
        assertEquals(null, week.getLog("mardi"));

        Log test1 = week.getWeek().get(0);
        Log test2 = week.getWeek().get(1);
        assertEquals(test1, week.getLog("sunday"));
        assertEquals(test2, week.getLog("monday"));
    }

    @Test
    void testSetWeek() {
        Week week2 = new Week();
        assertEquals(7, week.getWeek().size());
        assertFalse(week.getWeek().equals(week2.getWeek()));

        week.setWeek(week2.getWeek());
        assertEquals(7, week.getWeek().size());
        assertTrue(week.getWeek().equals(week2.getWeek()));
    }
}
