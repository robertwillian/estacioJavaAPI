package com.robert.schedules;

import com.robert.schedules.database.DatabaseConnection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class SchedulesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchedulesApplication.class, args);
	}

}
