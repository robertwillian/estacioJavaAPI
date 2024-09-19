package com.robert.schedules.entities.sync;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Dictionary;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SyncPullResponseDTO {
    private SyncChangesList changes;
    private Long timestamp;
}
