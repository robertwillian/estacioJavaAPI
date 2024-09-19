package com.robert.schedules.entities.sync;

import com.robert.schedules.entities.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SyncChanges<T> {
    private List<T> created;
    private List<T> updated;
    private List<String> deleted;
}
