package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FoodTest {
    private Food food;

    @BeforeEach
    void runBefore() {
        food = new Food("Protein Bar", 200, 19, 20, 5);
    }

    @Test
    void testGetName() {
        assertEquals("Protein Bar", food.getName());
    }

    @Test
    void testGetCalories() {
        assertEquals(200, food.getCalories());
    }

    @Test
    void testGetProtein() {
        assertEquals(19, food.getProtein());
    }

    @Test
    void testGetCarbohydrates() {
        assertEquals(20, food.getCarbohydrates());
    }

    @Test
    void testGetFat() {
        assertEquals(5, food.getFat());
    }
}
