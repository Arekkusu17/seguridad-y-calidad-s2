package com.example.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.example.backend.model.Appointment;
import com.example.backend.model.Patient;
import com.example.backend.repository.AppointmentRepository;
import com.example.backend.repository.PatientRepository;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody Map<String, Object> body) {
        Number patientIdNum = (Number) body.get("patientId");
        if (patientIdNum == null) {
            return ResponseEntity.badRequest().body("patientId is required");
        }
        Long patientId = patientIdNum.longValue();
        Patient patient = patientRepository.findById(patientId).orElse(null);
        if (patient == null) {
            return ResponseEntity.badRequest().body("patient not found");
        }

        String dt = (String) body.get("appointmentDateTime");
        LocalDateTime dateTime = LocalDateTime.parse(dt);

        Appointment ap = new Appointment();
        ap.setPatient(patient);
        ap.setAppointmentDateTime(dateTime);
        ap.setReason((String) body.get("reason"));
        ap.setVeterinarian((String) body.get("veterinarian"));

        Appointment saved = appointmentRepository.save(ap);
        return ResponseEntity.created(URI.create("/appointments/" + saved.getId())).body(saved);
    }

    @GetMapping
    public List<Appointment> listAppointments() {
        List<Appointment> out = new ArrayList<>();
        appointmentRepository.findAll().forEach(out::add);
        return out;
    }
}
