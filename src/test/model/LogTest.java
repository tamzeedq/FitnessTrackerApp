package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LogTest {
    private Log log;

    @BeforeEach
    void runBefore() {
        log = new Log("Friday");
    }

    @Test
    void testConstructor() {
        assertEquals("Friday", log.getDay());
        assertTrue(log.isDietEmpty());
        assertTrue(log.isWorkoutsEmpty());
    }

    @Test
    void testAddNewWorkout() {
        ArrayList<String> workouts = log.getWorkouts();
        assertEquals(0, workouts.size());

        log.addNewWorkout("cardio", 50, 15);
        workouts = log.getWorkouts();
        assertEquals(1, workouts.size());

        log.addNewWorkout("cardio", 50, 15);
        workouts = log.getWorkouts();
        assertEquals(2, workouts.size());
    }

    @Test
    void testAddNewFood() {
        ArrayList<String> food = log.getFoods();
        assertEquals(0, food.size());

        log.addNewFood("food", 100, 15, 20, 15);
        food = log.getFoods();
        assertEquals(1, food.size());

        log.addNewFood("food", 100, 15, 20, 15);
        food = log.getFoods();
        assertEquals(2, food.size());
    }

    @Test
    void testRemoveWorkout() {
        log.addNewWorkout("cardio", 50, 15);
        log.addNewWorkout("weights", 80, 60);
        log.addNewWorkout("yoga", 10, 15);

        Workout w1 = log.getWorkout(0);
        Workout w2 = log.getWorkout(1);

        ArrayList<String> workouts = log.getWorkouts();
        assertEquals(3, workouts.size());

        log.removeWorkout(2);
        workouts = log.getWorkouts();
        assertEquals(2, workouts.size());
        assertEquals(w1, log.getWorkout(0));
        assertEquals(w2, log.getWorkout(1));

        log.removeWorkout(1);
        workouts = log.getWorkouts();
        assertEquals(1, workouts.size());
        assertEquals(w1, log.getWorkout(0));

        log.removeWorkout(0);
        workouts = log.getWorkouts();
        assertEquals(0, workouts.size());
    }

    @Test
    void testRemoveFood() {
        log.addNewFood("water", 0,0,0,0);
        log.addNewFood("cereal", 100,5,2,0);
        log.addNewFood("burger", 200,20,30,30);

        Food f1 = log.getFood(0);
        Food f2 = log.getFood(1);

        ArrayList<String> foods = log.getFoods();
        assertEquals(3, foods.size());

        log.removeFood(2);
        foods = log.getFoods();
        assertEquals(2, foods.size());
        assertEquals(f1, log.getFood(0));
        assertEquals(f2, log.getFood(1));

        log.removeFood(1);
        foods = log.getFoods();
        assertEquals(1, foods.size());
        assertEquals(f1, log.getFood(0));

        log.removeFood(0);
        foods = log.getFoods();
        assertEquals(0, foods.size());
    }

    @Test
    void testGetTotalCaloriesBurned() {
        log.addNewWorkout("cardio", 50, 15);
        assertEquals(50, log.getTotalCaloriesBurned());

        log.addNewWorkout("weights", 80, 60);
        assertEquals(130, log.getTotalCaloriesBurned());

        log.addNewWorkout("yoga", 10, 15);
        assertEquals(140, log.getTotalCaloriesBurned());
    }

    @Test
    void testGetTotalCaloriesConsumed() {
        log.addNewFood("water", 0,0,0,0);
        assertEquals(0, log.getTotalCaloriesConsumed());

        log.addNewFood("cereal", 100,5,2,0);
        assertEquals(100, log.getTotalCaloriesConsumed());

        log.addNewFood("burger", 200,20,30,30);
        assertEquals(300, log.getTotalCaloriesConsumed());
    }

    @Test
    void testGetDay() {
        assertEquals("Friday", log.getDay());
    }

    @Test
    void testIsWorkoutsEmpty() {
        assertTrue(log.isWorkoutsEmpty());

        log.addNewWorkout("cardio", 50, 15);
        assertFalse(log.isWorkoutsEmpty());
    }

    @Test
    void testIsDietEmpty() {
        assertTrue(log.isDietEmpty());

        log.addNewFood("food", 100, 15, 20, 15);
        assertFalse(log.isDietEmpty());
    }

    @Test
    void testNotes() {
        assertEquals(0, log.getNotes().size());
        assertTrue(log.isNotesEmpty());

        log.addNewNote("Hit a new PR");
        assertEquals("Hit a new PR", log.getNote(0));
        assertEquals(1, log.getNotes().size());
        assertFalse(log.isNotesEmpty());

        log.addNewNote("Bench press 2x8");
        assertEquals("Bench press 2x8", log.getNote(1));
        assertEquals(2, log.getNotes().size());
        assertFalse(log.isNotesEmpty());

        log.removeNote(0);
        assertEquals("Bench press 2x8", log.getNote(0));
        assertEquals(1, log.getNotes().size());

        log.removeNote(0);
        assertEquals(0, log.getNotes().size());
        assertTrue(log.isNotesEmpty());

    }
}