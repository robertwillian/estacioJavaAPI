package com.robert.schedules.entities.sync;

import com.robert.schedules.entities.Customer;
import com.robert.schedules.entities.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SyncChangesList {
    private SyncChanges<Customer> customers;
    private SyncChanges<Schedule> schedules;
}
