package com.robert.schedules.repositories;

import com.robert.schedules.database.DatabaseConnection;
import com.robert.schedules.entities.Customer;
import com.robert.schedules.entities.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class SchedulesRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

    public Schedule findById(String id) throws SQLException {
        Connection connection = null;
        try {
            connection = databaseConnection.getConnection();

            PreparedStatement statement = null;

            statement = connection.prepareStatement("SELECT * FROM schedules WHERE id = ? LIMIT 1");

            statement.setString(1, id);

            ResultSet rs = statement.executeQuery();

            if(rs.next()){
                Schedule schedule = new Schedule();

                schedule.setId(UUID.fromString(rs.getString("id")));
                schedule.setUserId(rs.getString("userId"));
                schedule.setCustomerId(rs.getString("customerId"));
                schedule.setDate(rs.getTimestamp("date"));
                schedule.setService(rs.getString("service"));

                schedule.setCreatedAt(rs.getTimestamp("createdAt"));
                schedule.setUpdatedAt(rs.getTimestamp("updatedAt"));

                statement.close();
                connection.close();

                return schedule;
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

    public List<Schedule> findUpdates(String userId, Timestamp lastUpdate) throws SQLException {
        Connection connection = null;
        try {
            connection = databaseConnection.getConnection();

            PreparedStatement statement = null;

            statement = connection.prepareStatement("SELECT * FROM schedules WHERE userId = ? AND updatedAt >= ?");

            statement.setString(1, userId);
            statement.setTimestamp(2, lastUpdate);


            ResultSet rs = statement.executeQuery();

            List<Schedule> scheduleToReturn = new ArrayList<Schedule>();

            while(rs.next()){
                Schedule schedule = new Schedule();

                schedule.setId(UUID.fromString(rs.getString("id")));
                schedule.setUserId(rs.getString("userId"));
                schedule.setCustomerId(rs.getString("customerId"));
                schedule.setDate(rs.getTimestamp("date"));
                schedule.setService(rs.getString("service"));

                schedule.setCreatedAt(rs.getTimestamp("createdAt"));
                schedule.setUpdatedAt(rs.getTimestamp("updatedAt"));

                scheduleToReturn.add(schedule);

                return scheduleToReturn;
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

    public void save(Schedule schedule) throws SQLException {
        Connection connection = null;
        try {
            Boolean toUpdate = true;

            if(schedule.getId() != null) {
                Schedule oldSchedule = this.findById(schedule.getId().toString());

                if(oldSchedule == null) {
                    toUpdate = false;
                }
            }

            if(schedule.getId() == null){
                toUpdate = false;

                schedule.setId(UUID.randomUUID());
            }

            connection = databaseConnection.getConnection();

            PreparedStatement statement = null;

            if(toUpdate){
                statement = connection.prepareStatement("UPDATE schedules SET date = ?, service = ? WHERE id = ?");

                statement.setTimestamp(1, schedule.getDate());
                statement.setString(2, schedule.getService());
            }
            else {
                statement = connection.prepareStatement("INSERT INTO schedules (id, userId, customerId, date, service) VALUES (?, ?, ?, ?, ?)");

                statement.setString(1, schedule.getId().toString());
                statement.setString(2, schedule.getUserId());
                statement.setString(3, schedule.getCustomerId());
                statement.setTimestamp(4, schedule.getDate());
                statement.setString(5, schedule.getService());
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

            statement = connection.prepareStatement("UPDATE schedules SET deletedAt = CURRENT_TIMESTAMP WHERE id = ?");

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
