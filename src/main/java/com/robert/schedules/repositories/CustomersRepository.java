package com.robert.schedules.repositories;

import com.robert.schedules.database.DatabaseConnection;
import com.robert.schedules.entities.Customer;
import com.robert.schedules.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class CustomersRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

    public Customer findById(String id) throws SQLException {
        Connection connection = null;
        try {
            connection = databaseConnection.getConnection();

            PreparedStatement statement = null;

            statement = connection.prepareStatement("SELECT * FROM customers WHERE id = ? LIMIT 1");

            statement.setString(1, id);

            ResultSet rs = statement.executeQuery();

            if(rs.next()){
                Customer customer = new Customer();

                customer.setId(UUID.fromString(rs.getString("id")));
                customer.setUserId(rs.getString("userId"));
                customer.setName(rs.getString("name"));
                customer.setWhatsapp(rs.getString("whatsapp"));

                customer.setCreatedAt(rs.getTimestamp("createdAt"));
                customer.setUpdatedAt(rs.getTimestamp("updatedAt"));


                statement.close();
                connection.close();

                return customer;
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

    public List<Customer> findUpdates(String userId, Timestamp lastUpdate) throws SQLException {
        Connection connection = null;
        try {
            connection = databaseConnection.getConnection();

            PreparedStatement statement = null;

            statement = connection.prepareStatement("SELECT * FROM customers WHERE userId = ? AND updatedAt >= ?");

            statement.setString(1, userId);
            statement.setTimestamp(2, lastUpdate);


            System.out.println(statement.toString());


            ResultSet rs = statement.executeQuery();

            List<Customer> customerToReturn = new ArrayList<Customer>();

            while(rs.next()){
                Customer customer = new Customer();

                customer.setId(UUID.fromString(rs.getString("id")));
                customer.setUserId(rs.getString("userId"));
                customer.setName(rs.getString("name"));
                customer.setWhatsapp(rs.getString("whatsapp"));

                customer.setCreatedAt(rs.getTimestamp("createdAt"));
                customer.setUpdatedAt(rs.getTimestamp("updatedAt"));

                System.out.println(customer);

                customerToReturn.add(customer);
            }

            statement.close();
            connection.close();

            return customerToReturn;
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

    public void save(Customer customer) throws SQLException {
        Connection connection = null;
        try {
            Boolean toUpdate = true;

            if(customer.getId() != null) {
                Customer oldCustomer = this.findById(customer.getId().toString());

                if(oldCustomer == null) {
                    toUpdate = false;
                }
            }

            if(customer.getId() == null){
                toUpdate = false;

                customer.setId(UUID.randomUUID());
            }

            connection = databaseConnection.getConnection();

            PreparedStatement statement = null;

            if(toUpdate){
                statement = connection.prepareStatement("UPDATE customers SET name = ?, whatsapp = ? WHERE id = ?");

                statement.setString(1, customer.getName());
                statement.setString(2, customer.getWhatsapp());
                statement.setString(3, customer.getId().toString());
            }
            else {
                statement = connection.prepareStatement("INSERT INTO customers (id, userId, name, whatsapp) VALUES (?, ?, ?, ?)");

                statement.setString(1, customer.getId().toString());
                statement.setString(2, customer.getUserId());
                statement.setString(3, customer.getName());
                statement.setString(4, customer.getWhatsapp());
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

    public void delete(String id) throws SQLException {
        Connection connection = null;
        try {
            connection = databaseConnection.getConnection();

            PreparedStatement statement = null;

            statement = connection.prepareStatement("UPDATE customers SET deletedAt = CURRENT_TIMESTAMP WHERE id = ?");

            statement.setString(1, id);

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
