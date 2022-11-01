package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WorkoutTest {
    private Workout workout;

    @BeforeEach
    void runBefore() {
        workout = new Workout("cardio", 100, 15);
    }

    @Test
    void testGetType() {
        assertEquals("cardio", workout.getType());
    }

    @Test
    void testGetCaloriesBurned() {
        assertEquals(100, workout.getCaloriesBurned());
    }

    @Test
    void testGetDuration() {
        assertEquals(15, workout.getDuration());
    }
}
