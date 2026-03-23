package com.example.backend.repository;

import org.springframework.data.repository.CrudRepository;
import com.example.backend.model.Appointment;

public interface AppointmentRepository extends CrudRepository<Appointment, Long> {
    // additional query methods can be added here
}
