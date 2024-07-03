package com.example.demo.Service;

import com.example.demo.Exceptions.UserAlreadyExistException;
import com.example.demo.Exceptions.EntityNotFoundException;
import com.example.demo.Model.User;
import com.example.demo.Repository.CatWebRepository;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

@Service
public class AuthenticationService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private final CatWebRepository cwr;

    public static User user;

    public AuthenticationService(CatWebRepository catWebRepository){
        this.cwr = catWebRepository;
    }

    /**
     * Hashes the entered password and inserts the users information into the database
     * @param user A user with the entered information attached.
     * @throws UserAlreadyExistException Thrown if the users email already exists in the database
     */
    public void signUp(User user) throws UserAlreadyExistException {
        String hashedPassword = hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        try {
            cwr.createUser(user);
        } catch (DuplicateKeyException e){
            throw new UserAlreadyExistException("Email already exists");
        } catch (DataAccessException e){
            throw e;
        }
    }

    /**
     * Checks the users credentials with the database. Sets the static object user in AuthenticationService to a user from the database,
     * if the entered credentials are valid.
     * @param email An already existing users email
     * @param password The users password.
     * @throws EntityNotFoundException Thrown if the email or password doesn't match or exist in the database
     */
    public void login(String email, String password) throws EntityNotFoundException {
        String hashedPassword;
        try{
            hashedPassword = cwr.getPasswordByEmail(email);
        } catch (EmptyResultDataAccessException e){
            throw new EntityNotFoundException("User with given email not found");
        }

        if(!BCrypt.checkpw(password, hashedPassword)) {
            throw new EntityNotFoundException("Incorrect password");
        }

        AuthenticationService.user = cwr.getUserByEmail(email);
        System.out.println(this.user);
    }

    public void logOut(){
        AuthenticationService.user = null;
        
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }


    /**
     * Deletes a user from the database
     * @param id The current users id
     * @return True if the User was successfully deleted. False if something went wrong
     */
    public boolean deleteUser(int id){
        boolean loggedOut = cwr.deleteUser(id);
        if(loggedOut){
            AuthenticationService.user = null;
        }
        return loggedOut;
    }

   



}
