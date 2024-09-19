package com.robert.schedules.repositories;

import com.robert.schedules.database.DatabaseConnection;
import com.robert.schedules.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class UsersRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

    public User findById(String id) throws SQLException {
        Connection connection = null;
        try {
            connection = databaseConnection.getConnection();

            PreparedStatement statement = null;

            statement = connection.prepareStatement("SELECT * FROM users WHERE id = ? LIMIT 1");

            statement.setString(1, id);

            ResultSet rs = statement.executeQuery();

            if(rs.next()){
                User user = new User();
                user.setEmail(rs.getString("email"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setId(UUID.fromString(rs.getString("id")));

                statement.close();
                connection.close();

                return user;
            }

            statement.close();
            connection.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) {
                connection.close();
            }
        }

        return null;
    }

    public User findByEmail(String email) throws SQLException {
        Connection connection = null;
        try {
            connection = databaseConnection.getConnection();

            PreparedStatement statement = null;

            statement = connection.prepareStatement("SELECT * FROM users WHERE email = ? LIMIT 1");

            statement.setString(1, email);


            ResultSet rs = statement.executeQuery();

            if(rs.next()){
                User user = new User();
                user.setEmail(rs.getString("email"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setId(UUID.fromString(rs.getString("id")));

                System.out.println(user.getId());

                statement.close();
                connection.close();

                return user;
            }

            statement.close();
            connection.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) {
                connection.close();
            }
        }

        return null;
    }

    public void save(User user) throws SQLException {
        Connection connection = null;
        try {
            Boolean toUpdate = true;

            if(user.getId() == null){
                toUpdate = false;

                user.setId(UUID.randomUUID());
            }
            connection = databaseConnection.getConnection();

            PreparedStatement statement = null;

            if(toUpdate){
                statement = connection.prepareStatement("UPDATE users SET name = ?, email = ?, password = ?) WHERE id = ?");

                statement.setString(1, user.getName());
                statement.setString(2, user.getEmail());
                statement.setString(3, user.getPassword());
                statement.setString(4, user.getId().toString());
            }
            else {
                statement = connection.prepareStatement("INSERT INTO users (id, name, email, password) VALUES (?, ?, ?, ?)");

                statement.setString(1, user.getId().toString());
                statement.setString(2, user.getName());
                statement.setString(3, user.getEmail());
                statement.setString(4, user.getPassword());
            }

            statement.executeUpdate();

            statement.close();
            connection.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
