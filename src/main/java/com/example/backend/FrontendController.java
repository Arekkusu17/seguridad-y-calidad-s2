package com.example.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

@Controller
public class FrontendController {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @GetMapping({"/", "/login-ui"})
    public String loginPage() {
        return "login";
    }

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("name", "Veterinary System");
        return "home";
    }

    @GetMapping("/login")
    public String loginGet() { return "login"; }

    @GetMapping("/patients")
    public String patientsPage(Model model) {
        ArrayList<Patient> list = new ArrayList<>();
        patientRepository.findAll().forEach(list::add);
        model.addAttribute("patients", list);
        return "patients";
    }

    @GetMapping("/appointments")
    public String appointmentsPage(Model model) {
        ArrayList<Appointment> list = new ArrayList<>();
        appointmentRepository.findAll().forEach(list::add);
        model.addAttribute("appointments", list);
        return "appointments";
    }

    @GetMapping("/patients/new")
    public String patientForm(Model model) { model.addAttribute("patient", new Patient()); return "patient_form"; }

    @GetMapping("/appointments/new")
    public String appointmentForm(Model model) { model.addAttribute("appointment", new Appointment()); model.addAttribute("patients", patientRepository.findAll()); return "appointment_form"; }

    @PostMapping("/patients")
    public String createPatientFromForm(@ModelAttribute Patient patient, RedirectAttributes attrs) {
        patientRepository.save(patient);
        return "redirect:/patients";
    }

    @PostMapping("/appointments")
    public String createAppointmentFromForm(@RequestParam Long patientId, @RequestParam String dateTimeStr, @RequestParam String reason, @RequestParam String veterinarian, RedirectAttributes attrs) {
        Patient p = patientRepository.findById(patientId).orElse(null);
        if (p == null) {
            attrs.addFlashAttribute("error", "Paciente no encontrado");
            return "redirect:/appointments/new";
        }
        Appointment ap = new Appointment();
        ap.setPatient(p);
        ap.setAppointmentDateTime(java.time.LocalDateTime.parse(dateTimeStr));
        ap.setReason(reason);
        ap.setVeterinarian(veterinarian);
        appointmentRepository.save(ap);
        return "redirect:/appointments";
    }
}
