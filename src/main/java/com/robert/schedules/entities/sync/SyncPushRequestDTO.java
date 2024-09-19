package com.robert.schedules.entities.sync;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SyncPushRequestDTO {
    private SyncChangesList changes;
    private Long timestamp;
}
