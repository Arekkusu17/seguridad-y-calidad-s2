package com.example.backend.repository;

import org.springframework.data.repository.CrudRepository;
import com.example.backend.model.Patient;

public interface PatientRepository extends CrudRepository<Patient, Long> {
    // additional query methods can be added here
}
