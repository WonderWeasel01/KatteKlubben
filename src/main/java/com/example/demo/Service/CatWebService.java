package com.example.demo.Service;
import com.example.demo.Exceptions.EntityNotFoundException;
import com.example.demo.Model.Cat;

import com.example.demo.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.Repository.CatWebRepository;

import java.util.List;

@Service
public class CatWebService {

    @Autowired
    private final CatWebRepository cwr = new CatWebRepository(new JdbcTemplate());

    @Autowired
    AuthenticationService as = new AuthenticationService(cwr);


    /**
     * Extracts all first names from every registered user in the database.
     * @return List of first names.
     */
    public List<String> getAllFirstNames() {
        return cwr.getAllFirstNames();
        // Tilføj exception handling her i stedet for i repository klasse
    }
    /**
     * Extracts all first names from every registered user in the database that matches the entered String.
     * @return List of first names.
     */
    public List<String> getAllFirstNames(String searchQuery) {
        return cwr.getAllFirstNames(searchQuery);
        // Tilføj exception handling her i stedet for i repository klasse
    }

    public List<User> getAllUsers(){
        return cwr.getAllUsers();
    }

    public List<User> getAllUsers(String searchQuery){
        return cwr.getAllUsers(searchQuery);
    }

    public User getUser(int userID){
        return cwr.getUserByID(userID);
    }

    /**
     * Creates a cat in the database with the attached values.
     * @param cat A Cat created by the user.
     * @return The created cat.
     */
    public Cat createCat(Cat cat) {
        try{
            return cwr.createCat(cat);
        } catch (DataAccessException e){
            throw e;
        }
    }

    /**
     * Updates the users information
     * @param user The user with the updated information attached.
     * @return
     */
    public User updateUser(User user){
        String hashedPassword = as.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        return cwr.updateUser(user.getId(),user);
    }

    /**
     * Extracts every cat with the ownerID that matches the entered id.
     * @param id the id of the User to extract cats from.
     * @return List of cats
     * @throws EntityNotFoundException If no cats are found in the database.
     */
    public List<Cat> getUserCats(int id) throws EntityNotFoundException {
        List<Cat> cats = cwr.getAllCatsByOwnerID(id);
        if(cats.isEmpty()){
            throw new EntityNotFoundException("No cats for userID: " + id + " were found");
        }
        return cats;
    }


    /**
     * Returns a number based on how many registered cats the user has
     * @param id The id of a user
     * @return An integer with the value of the number of cats.
     */
    public int getUserCatCount(int id){
        try{
            return cwr.getUserCatCount(id);
        } catch(NullPointerException | EmptyResultDataAccessException e){
            return 0;
        }
    }


    public boolean userOwnsCat(int userId, int catId) {
        try {
            Cat cat = cwr.findCatById(catId);
            return cat != null && cat.getOwnerID() == userId;
        } catch (DataAccessException e) {
            return false;
        }
    }

    /**
     * Extracts a cat from the database with the given id
     * @param catId The id of a cat.
     * @return Cat with the given id.
     */
    public Cat findCatById(int catId) {
        return cwr.findCatById(catId);
    }


    /**
     * Updates the information of a cat that is already registered in the system.
     * @param cat Cat with the updated information
     * @return The updated cat.
     */
    public Cat updateCat(Cat cat) {
        if (cat == null || cat.getId() == 0) {
            throw new IllegalArgumentException("Cat is null or cat ID is not set.");
        }
        return cwr.updateCat(cat);
    }


    public boolean deleteCat(int catId) {
        return cwr.deleteCat(catId);
    }

}
