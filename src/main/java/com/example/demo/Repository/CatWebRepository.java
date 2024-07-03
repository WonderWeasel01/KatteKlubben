package com.example.demo.Repository;

import com.example.demo.Model.Cat;
import com.example.demo.Model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

@Repository
public class CatWebRepository {

    @Autowired
    private final JdbcTemplate jdbcTemplate;


    public CatWebRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //User Methods

    public User createUser(User user) throws DuplicateKeyException{
        String sql;
        if(user.getId() == 0){
            sql = "INSERT INTO `User`(`email`, `password`, `first_name`, `last_name`) VALUES (?,?,?,?)";
            jdbcTemplate.update(sql,user.getEmail(),user.getPassword(),user.getFirstName(),user.getLastName());
        } else{
            sql = "INSERT INTO `User`(`id`,`email`, `password`, `first_name`, `last_name`) VALUES (?,?,?,?,?)";
            jdbcTemplate.update(sql,user.getId(),user.getEmail(),user.getPassword(),user.getFirstName(),user.getLastName());
        }
        return getUserByEmail(user.getEmail());
    }

    /**
     *
     * @param email The email address of an already existing user
     * @return Returns a user from the database that matches the given email.
     */
    public User getUserByEmail(String email) throws EmptyResultDataAccessException{
        String sql ="SELECT * FROM `User` WHERE email = ?";
        return jdbcTemplate.queryForObject(sql,userRowMapper(),email);
    }
    public User getUserByID(int id) throws EmptyResultDataAccessException{
        String sql ="SELECT * FROM `User` WHERE id = ?";
        return jdbcTemplate.queryForObject(sql,userRowMapper(),id);
    }

    /**
     *
     * @param email The email address of an already existing user
     * @return The password corresponding to the provided email.
     */
    public String getPasswordByEmail(String email){
        String sql = "SELECT `password` FROM `User` WHERE email = ?";
        return jdbcTemplate.queryForObject(sql,new Object[]{email}, String.class);
    }

    public boolean deleteUser(int userID){
        String sql = "DELETE FROM `User` WHERE id = ?";
        try{
             int rowsAffected = jdbcTemplate.update(sql,userID);
             if(rowsAffected>0){
                return true;
        } else return false;
    }catch (DataAccessException e){
        e.printStackTrace();
        return false;
    }

    }

     public List<User> getAllUsers() {
        try {
        String sql = "SELECT * FROM User";
        return jdbcTemplate.query(sql, userRowMapper());
     } catch (DataAccessException ex) {
        ex.printStackTrace();
        return Collections.emptyList();
        }
     }
    public List<User> getAllUsers(String searchQuery) {
        try {
            String sql = "SELECT * FROM User";

            if (searchQuery != null && !searchQuery.isEmpty()) {
                sql += " WHERE first_name LIKE ?";
                return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), "%" + searchQuery + "%");
            } else {
                return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("Error fetching users from the database", e);
        }
    }


    public List<String> getAllFirstNames(String searchQuery){
         try {
             String sql = "SELECT first_name FROM User";

            if (searchQuery != null && !searchQuery.isEmpty()) {
                sql+= "WHERE first_name LIKE ?";
                return jdbcTemplate.queryForList(sql, String.class, "%" + searchQuery + "%");
            } else {

             return jdbcTemplate.queryForList(sql, String.class);
            }
         } catch (DataAccessException e) {
             throw new RuntimeException("Error fetching usernames from the database", e);
         }
     }

     public List<String> getAllFirstNames(){
         try {
             String sql = "SELECT first_name FROM User";
             return jdbcTemplate.queryForList(sql, String.class);
         } catch (DataAccessException e) {
             throw new RuntimeException("Error fetching usernames from the database", e);
         }
     }

    public User updateUser(int userID, User updatedUser){
        String sql = "UPDATE `User` SET `email`= ?,`password`= ?,`first_name`= ?,`last_name`= ? WHERE id = ?";
        jdbcTemplate.update(sql,updatedUser.getEmail(),updatedUser.getPassword(),updatedUser.getFirstName(),updatedUser.getLastName(),userID);
        return getUserByID(userID);
    }


    /**
     * Extracts id, email, password, firstName, lastName for a User Object
     * @return A RowMapper<User> configured to User objects
     */
    private RowMapper<User> userRowMapper(){
        return ((rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            return user;
        });
    }

    // Cat methods

    /**
     * Inserts a Cat into the mysql database.
     * @param cat
     * @return
     */
    public Cat createCat(Cat cat){
        if(cat.getId() == 0){
            KeyHolder keyHolder = new GeneratedKeyHolder();

            String sql = "INSERT INTO `Cat`(`name`, `race`, `age`, `gender`, `weight_kg`, `ownerid`) VALUES (?,?,?,?,?,?)";
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, cat.getName());
                ps.setString(2, cat.getRace());
                ps.setInt(3, cat.getAge());
                ps.setString(4, String.valueOf(cat.getGender()));
                ps.setDouble(5, cat.getWeight());
                ps.setInt(6, cat.getOwnerID());
                return ps;
            }, keyHolder);

            Number generatedKey = keyHolder.getKey();
            if (generatedKey != null) {
                int catId = generatedKey.intValue();
                cat.setId(catId);
            }
            return cat;
        } else {
            String sql = "INSERT INTO `Cat`(`id`, `name`, `race`, `age`, `gender`, `weight_kg`, `ownerid`) " + "VALUES (?,?,?,?,?,?,?)";
            jdbcTemplate.update(sql, cat.getId(), cat.getName(), cat.getRace(), cat.getAge(), cat.getGender(), cat.getWeight(), cat.getOwnerID());
        }
        return cat;
    }

    public Cat getCatByCatID(int catID){
        String sql = "SELECT * FROM `Cat` WHERE id = ?";
        return jdbcTemplate.queryForObject(sql,new Object[]{catID},catRowMapper());
    }

    public int getUserCatCount(int ownerID){
        String sql = "SELECT COUNT(*) FROM `Cat` WHERE ownerid = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, ownerID);
    }

    public List<Cat> getAllCatsByOwnerID(int ownerID){
        String sql = "SELECT * FROM `Cat` WHERE ownerid = ?";
        return jdbcTemplate.query(sql,new Object[]{ownerID},catRowMapper());
    }

    public Cat updateCat(Cat cat) {
        String sql = "UPDATE `Cat` SET `name`=?, `race`=?, `age`=?, `gender`=?, `weight_kg`=? WHERE `id`=?";
        jdbcTemplate.update(sql,
            cat.getName(), cat.getRace(), cat.getAge(), String.valueOf(cat.getGender()),
            cat.getWeight(), cat.getId()
        );
        return cat;
    }

    public boolean deleteCat(int catID){
        String sql = "DELETE FROM `Cat` WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, catID);
        return rowsAffected > 0;
    }

    private RowMapper<Cat> catRowMapper(){
        return ((rs, rowNum) -> {
            Cat cat = new Cat();
            cat.setId(rs.getInt("id"));
            cat.setName(rs.getString("name"));
            cat.setRace(rs.getString("race"));
            cat.setAge(rs.getInt("age"));
            cat.setGender(rs.getString("gender").charAt(0));
            cat.setWeight(rs.getDouble("weight_kg"));
            cat.setOwnerID(rs.getInt("ownerid"));
            return cat;
        });
    }

    public List<String> getAllUsernames() {
        String sql = "SELECT first_name FROM User";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    public Cat findCatById(int catId) {
        String sql = "SELECT * FROM Cat WHERE id = ?";
        List<Cat> cats = jdbcTemplate.query(
            con -> {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, catId);
                return ps;
            },
            (rs, rowNum) -> new Cat(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("race"),
                rs.getInt("age"),
                rs.getString("gender").charAt(0),
                rs.getDouble("weight_kg"),
                rs.getInt("ownerID")
            )
        );

        return cats.isEmpty() ? null : cats.get(0);
    }

}

