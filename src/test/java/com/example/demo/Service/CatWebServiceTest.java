package com.example.demo.Service;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.testng.Assert.assertNotEquals;
import com.example.demo.Model.Cat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CatWebServiceTest {

    @Autowired
    private CatWebService catWebService;

    @Test
    public void testCreateCat() {
        // Create a new Cat object
        Cat cat = new Cat();
        cat.setName("Test Cat");
        cat.setRace("Test Race");
        cat.setAge(3);
        cat.setGender('M');
        cat.setWeight(5.0);
        cat.setOwnerID(5);

        // Call the createCat method
        Cat createdCat = catWebService.createCat(cat);

        // Assert that the created cat is not null
        assertNotNull(createdCat);
        // Assert that the created cat has a non-zero ID
        assertNotEquals(0, createdCat.getId());
    }

    @Test
    public void testSuccessfulDeletion() {
        int catIdToDelete = 27; // Sæt id for en kat der eksisterer, slår fejl hvis det ikke eksisterer
        assertTrue(catWebService.deleteCat(catIdToDelete));
    }

    @Test
    public void testUnsuccessfulDeletion() {
        int invalidCatId = -1; // Id der ikke eksisterer
        assertFalse(catWebService.deleteCat(invalidCatId));
    }

    @Test
    public void testNonexistentCatDeletion() {
        int nonexistentCatId = 999; // Id der ikke eksisterer
        assertFalse(catWebService.deleteCat(nonexistentCatId));
    }

    @Test
    public void testCreateCat_InvalidInput() {
        // Create a new Cat object with invalid input (e.g., null values)
        Cat cat = new Cat(); // This cat object has not been initialized with valid data

        // Call the createCat method and expect an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> catWebService.createCat(cat));
    }
}
