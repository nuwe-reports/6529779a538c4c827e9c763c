package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;


import java.time.LocalDateTime;

import com.example.demo.repositories.AppointmentRepository;
import com.example.demo.repositories.DoctorRepository;
import com.example.demo.repositories.PatientRepository;
import com.example.demo.repositories.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.example.demo.entities.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
class EntityUnitTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    private Doctor d1;
    private Patient p1;
    private Room r1;
    private Appointment a1;
    private Appointment a2;
    private Appointment a3;


    @Test
    void testDoctorEntity() {
        // Crear un nuevo doctor y guardar en el repositorio
        Doctor doctor = new Doctor("Mateo", "Gil", 35, "m.gil@hospital.accwe");
        doctor = doctorRepository.save(doctor);

        // Consultar el doctor por ID y comprobar si es igual al doctor creado
        Doctor retrievedDoctor = doctorRepository.findById(doctor.getId()).orElse(null);
        assertThat(retrievedDoctor).isNotNull();
        assertThat(retrievedDoctor.getFirstName()).isEqualTo("Mateo");
        assertThat(retrievedDoctor.getLastName()).isEqualTo("Gil");
        assertThat(retrievedDoctor.getAge()).isEqualTo(35);
        assertThat(retrievedDoctor.getEmail()).isEqualTo("m.gil@hospital.accwe");

    }

    @Test
    void testPatientEntity() {
        // Crear un nuevo paciente y guardar en el repositorio
        Patient patient = new Patient("Alicia", "Smith", 28, "a.smith@hospital.accwe");
        patient = patientRepository.save(patient);

        // Consultar el paciente por ID y comprobar si es igual al paciente creado
        Patient retrievedPatient = patientRepository.findById(patient.getId()).orElse(null);
        assertThat(retrievedPatient).isNotNull();
        assertThat(retrievedPatient.getFirstName()).isEqualTo("Alicia");
        assertThat(retrievedPatient.getLastName()).isEqualTo("Smith");
        assertThat(retrievedPatient.getAge()).isEqualTo(28);
        assertThat(retrievedPatient.getEmail()).isEqualTo("a.smith@hospital.accwe");
    }

    @Test
    void testRoomEntity() {
        // Crea una nueva sala y guarda en el repositorio
        Room room = new Room("Dining");
        room = roomRepository.save(room);

        // Consultar la sala por nombre y comprobar si es igual a la sala creada
        Room retrievedRoom = roomRepository.findByRoomName(room.getRoomName()).orElse(null);
        assertThat(retrievedRoom).isNotNull();
        assertThat(retrievedRoom.getRoomName()).isEqualTo("Dining");
    }

    @Test
    void testAppointmentEntity() {
        // Crear una nueva cita y guardar en el repositorio
        Doctor doctor = new Doctor("Juan", "Carlos", 34, "j.carlos@hospital.accwe");
        Patient patient = new Patient("Clarisa", "Julia", 29, "c.julia@hospital.accwe");
        Room room = new Room("Operations");
        doctor = doctorRepository.save(doctor);
        patient = patientRepository.save(patient);
        room = roomRepository.save(room);

        // Definir las fechas de inicio y finalización para la cita
        LocalDateTime startsAt = LocalDateTime.now();
        LocalDateTime finishesAt = startsAt.plusHours(1); // Suponemos una duración de 1 hora

        Appointment appointment = new Appointment(patient, doctor, room, startsAt, finishesAt);
        appointment = appointmentRepository.save(appointment);

        // Consultar la cita por ID y comprobar si es igual a la cita creada
        Appointment retrievedAppointment = appointmentRepository.findById(appointment.getId()).orElse(null);
        assertThat(retrievedAppointment).isNotNull();
        assertThat(retrievedAppointment.getDoctor()).isEqualTo(doctor);
        assertThat(retrievedAppointment.getPatient()).isEqualTo(patient);
        assertThat(retrievedAppointment.getRoom()).isEqualTo(room);
    }

    //Verificar que se pueda encontrar una sala por su nombre
    @Test
    void testFindRoomByName() {
        Room room = new Room("Dermatology");
        room = roomRepository.save(room);
        Room retrievedRoom = roomRepository.findByRoomName(room.getRoomName()).orElse(null);
        assertThat(retrievedRoom).isNotNull();
        assertThat(retrievedRoom.getRoomName()).isEqualTo("Dermatology");
    }



    //Verificar que una cita pueda ser eliminada por ID
    @Test
    void testDeleteAppointmentById() {
        // Crear una nueva cita y guardarla en el repositorio
        LocalDateTime dateTime = LocalDateTime.now();
        Doctor doctor = new Doctor("Juan", "Carlos", 34, "j.carlos@hospital.accwe");
        Patient patient = new Patient("Clarisa", "Julia", 29, "c.julia@hospital.accwe");
        Room room = new Room("Operations");
        doctor = doctorRepository.save(doctor);
        patient = patientRepository.save(patient);
        room = roomRepository.save(room);

        // Definir las fechas de inicio y finalización para la cita
        LocalDateTime startsAt = dateTime;
        LocalDateTime finishesAt = startsAt.plusHours(1); // Suponemos una duración de 1 hora

        Appointment appointment = new Appointment(patient, doctor, room, startsAt, finishesAt);

        // Guardar la cita en el repositorio y obtener su ID
        appointment = appointmentRepository.save(appointment);
        long appointmentId = appointment.getId();

        // Eliminar la cita por ID y comprobar que se haya eliminado
        appointmentRepository.deleteById(appointmentId);
        Appointment deletedAppointment = appointmentRepository.findById(appointmentId).orElse(null);
        assertThat(deletedAppointment).isNull();
    }
}
