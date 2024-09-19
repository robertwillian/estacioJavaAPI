package com.robert.schedules.controllers;

import com.robert.schedules.entities.*;
import com.robert.schedules.entities.sync.SyncChanges;
import com.robert.schedules.entities.sync.SyncChangesList;
import com.robert.schedules.entities.sync.SyncPullResponseDTO;
import com.robert.schedules.entities.sync.SyncPushRequestDTO;
import com.robert.schedules.repositories.CustomersRepository;
import com.robert.schedules.repositories.SchedulesRepository;
import com.robert.schedules.repositories.UsersRepository;
import com.robert.schedules.security.TokenService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sync")
public class SyncController {

    @Autowired
    private CustomersRepository customersRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private SchedulesRepository schedulesRepository;

    @PostMapping
    public ResponseEntity push(@RequestBody SyncPushRequestDTO data, Authentication authentication) throws SQLException {

        var changes = data.getChanges();
        var customerChanges = changes.getCustomers();
        var schedulesChange = changes.getSchedules();

        String userEmail = authentication.getName();

        User user = usersRepository.findByEmail(userEmail);

        // customers
        for (Customer customer : customerChanges.getCreated()) {
            customer.setUserId(user.getId().toString());
            customersRepository.save(customer);
        }

        for (Customer customer : customerChanges.getUpdated()) {
            customer.setUserId(user.getId().toString());
            customersRepository.save(customer);
        }

        for (String id : customerChanges.getDeleted()) {
            customersRepository.delete(id);
        }

        // schedules
        for (Schedule schedule : schedulesChange.getCreated()) {
            schedule.setUserId(user.getId().toString());
            schedulesRepository.save(schedule);
        }

        for (Schedule schedule : schedulesChange.getUpdated()) {
            schedule.setUserId(user.getId().toString());
            schedulesRepository.save(schedule);
        }

        for (String id : schedulesChange.getDeleted()) {
            schedulesRepository.delete(id);
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity pull(@PathParam("lastUpdate") Long lastUpdate, Authentication authentication) throws SQLException {
        System.out.println("Last update" + lastUpdate);

        Timestamp date = new Timestamp(lastUpdate);

        System.out.println("Date" + date);

        String userEmail = authentication.getName();

        User user = usersRepository.findByEmail(userEmail);

        List<Customer> customers = customersRepository.findUpdates(user.getId().toString(), date);
        List<Schedule> schedules = schedulesRepository.findUpdates(user.getId().toString(), date);

        SyncPullResponseDTO response = new SyncPullResponseDTO();
        SyncChangesList changesList = new SyncChangesList();

        SyncChanges<Customer> customerChanges = new SyncChanges<>();
        SyncChanges<Schedule> scheduleChanges = new SyncChanges<>();

        List<Customer> createdCustomers = new ArrayList<>();
        List<Customer> updatedCustomers = new ArrayList<>();
        List<String> deletedCustomers = new ArrayList<>();
        List<Schedule> createdSchedules = new ArrayList<>();
        List<Schedule> updatedSchedules = new ArrayList<>();
        List<String> deletedSchedules = new ArrayList<>();

        if(customers != null) {
            for (Customer customer : customers) {
                if(customer.getDeletedAt() != null && customer.getDeletedAt().after(date)){
                    deletedCustomers.add(customer.getId().toString());
                }
                else {
                    if(customer.getCreatedAt().after(date)) {
                        createdCustomers.add(customer);
                    }
                    else {
                        updatedCustomers.add(customer);
                    }
                }
            }
        }

        if(schedules != null) {
            for (Schedule schedule : schedules) {
                if(schedule.getDeletedAt() != null && schedule.getDeletedAt().after(date)){
                    deletedCustomers.add(schedule.getId().toString());
                }
                else {
                    if(schedule.getCreatedAt().after(date)) {
                        createdSchedules.add(schedule);
                    }
                    else {
                        updatedSchedules.add(schedule);
                    }
                }
            }
        }

        customerChanges.setCreated(createdCustomers);
        customerChanges.setUpdated(updatedCustomers);
        customerChanges.setDeleted(deletedCustomers);
        scheduleChanges.setCreated(createdSchedules);
        scheduleChanges.setUpdated(updatedSchedules);
        scheduleChanges.setDeleted(deletedSchedules);

        changesList.setCustomers(customerChanges);
        changesList.setSchedules(scheduleChanges);

        response.setChanges(changesList);

        response.setTimestamp(new java.util.Date().getTime());

        return ResponseEntity.ok().body(response);
    }
}