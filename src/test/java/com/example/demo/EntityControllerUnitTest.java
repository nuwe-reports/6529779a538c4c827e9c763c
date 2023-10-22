package com.example.demo;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.controllers.*;
import com.example.demo.repositories.*;
import com.example.demo.entities.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(DoctorController.class)
class DoctorControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DoctorRepository doctorRepository;

    @Test
    void testGetAllDoctors() throws Exception {
        Doctor doctor = new Doctor("Juan", "Carlos", 34, "j.carlos@hospital.accwe");
        List<Doctor> doctors = Collections.singletonList(doctor);

        when(doctorRepository.findAll()).thenReturn(doctors);

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value("Juan"));

        /* Prueba que verifica la obtención de la lista de doctores.
           Se simula el comportamiento del repositorio para devolver una lista de doctores.
           Luego, se realiza una solicitud GET a "/doctors" y se verifica que la respuesta contenga
           al menos un médico cuyo primer nombre sea "Juan". */
    }

    @Test
    void testGetDoctorById() throws Exception {
        Doctor doctor = new Doctor("Juan", "Carlos", 34, "j.carlos@hospital.accwe");
        doctor.setId(1L);

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        mockMvc.perform(get("/api/doctors/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("Juan"));

        /* Prueba que verifica la obtención de un médico por ID.
           Se simula el comportamiento del repositorio para devolver un médico con un ID específico.
           Luego, se realiza una solicitud GET a "/doctors/1" (donde 1 es un ID de médico) y se verifica
           que la respuesta contenga un médico con el primer nombre "Juan". */
    }

    @Test
    void testCreateDoctor() throws Exception {
        Doctor doctor = new Doctor("Mateo", "Gil", 35, "m.gil@hospital.accwe");

        mockMvc.perform(post("/api/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isCreated());

        verify(doctorRepository, times(1)).save(any(Doctor.class));

         /* Prueba que verifica la creación de un nuevo médico mediante una solicitud POST a "/doctors".
            Se crea un médico y se verifica que la respuesta tenga un estado 201 (CREATED).
            Se utiliza Mockito para verificar que el método "save" del repositorio se llame una vez. */
    }

    @Test
    void testDeleteDoctor() throws Exception {
            // Simula que existe un médico con el ID 1
            Doctor existingDoctor = new Doctor("Clarisa","Julia", 29, "c.julia@hospital.accwe");
            when(doctorRepository.findById(1L)).thenReturn(Optional.of(existingDoctor));

            // Realiza la eliminación del médico con el ID 1
            mockMvc.perform(delete("/api/doctors/1"))
                    .andExpect(status().isOk()); // Verifica que el estado sea HttpStatus.OK

            // Verifica que el médico con el ID 1 se haya eliminado correctamente
            verify(doctorRepository, times(1)).deleteById(1L);

            // Simula que no existe un médico con el ID 2
            when(doctorRepository.findById(2L)).thenReturn(Optional.empty());

            // Realiza un intento de eliminación del médico con el ID 2
            mockMvc.perform(delete("/api/doctors/2"))
                    .andExpect(status().isNotFound()); // Verifica que el estado sea HttpStatus.NOT_FOUND

         /*Prueba que verifica la eliminación de un médico, ya sea cuando existe o no, mediante una solicitud DELETE a "/doctors/{id}".
           Primero, se simula el comportamiento del repositorio para devolver un médico con el ID 1 y se verifica que la respuesta tenga
           un estado 200 (OK) cuando intentamos eliminarlo. Luego, se utiliza Mockito para verificar que el método "deleteById" del
           repositorio se llame una vez.
           Después, se simula que no existe un médico con el ID 2 y se verifica que, al intentar eliminarlo, la respuesta tenga un estado
           404 (NOT FOUND), asegurando que no se elimine un médico inexistente.
           Esto garantiza la cobertura de ambos casos en un solo test unitario.*/

        }
    @Test
    void testDeleteAllDoctors() throws Exception {
        mockMvc.perform(delete("/api/doctors"))
                .andExpect(status().isOk());

        verify(doctorRepository, times(1)).deleteAll();

    /* Prueba que verifica la eliminación de todos los médicos mediante una solicitud DELETE a "/api/doctors".
       Se verifica que la respuesta tenga un estado 200 (OK).
       Se utiliza Mockito para verificar que el método "deleteAll" del repositorio se llame una vez. */
    }

}

@WebMvcTest(PatientController.class)
class PatientControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PatientRepository patientRepository;

    @Test
    void testGetAllPatients() throws Exception {
        Patient patient = new Patient("Clarisa", "Julia", 29, "c.julia@hospital.accwe");
        List<Patient> patients = Collections.singletonList(patient);

        when(patientRepository.findAll()).thenReturn(patients);

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value("Clarisa"));

        /*  Prueba que verifica la obtención de la lista de pacientes.
            Se simula el comportamiento del repositorio para devolver una lista de pacientes.
            Luego, se realiza una solicitud GET a "/patients" y se verifica que la respuesta contenga
            al menos un paciente cuyo primer nombre sea "Alice". */
    }

    @Test
    void testGetPatientById() throws Exception {
        Patient patient = new Patient("Juan","Carlos", 34, "j.carlos@hospital.accwe");
        patient.setId(1L);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("Juan"));

        /*  Prueba que verifica la obtención de un paciente por ID.
            Se simula el comportamiento del repositorio para devolver un paciente con un ID específico.
            Luego, se realiza una solicitud GET a "/patients/1" (donde 1 es un ID de paciente) y se verifica
            que la respuesta contenga un paciente con el primer nombre "Alice". */
    }

    @Test
    void testCreatePatient() throws Exception {
        Patient patient = new Patient("Alicia", "Smith", 28, "a.smith@hospital.accwe");

        mockMvc.perform(post("/api/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isCreated());

        verify(patientRepository, times(1)).save(any(Patient.class));

        /*  Prueba que verifica la obtención de un paciente por ID.
            Se simula el comportamiento del repositorio para devolver un paciente con un ID específico.
            Luego, se realiza una solicitud GET a "/patients/1" (donde 1 es un ID de paciente) y se verifica
            que la respuesta contenga un paciente con el primer nombre "Alice". */
    }



    @Test
    void testDeletePatient() throws Exception {
        // Simula que existe un paciente con el ID 1
        Patient existingPatient = new Patient("Cornelio","Andrea", 59, "c.andrea@hospital.accwe");
        when(patientRepository.findById(1L)).thenReturn(Optional.of(existingPatient));

        // Realiza la eliminación del paciente con el ID 1
        mockMvc.perform(delete("/api/patients/1"))
                .andExpect(status().isOk());

        // Verifica que el paciente con el ID 1 se haya eliminado correctamente
        verify(patientRepository, times(1)).deleteById(1L);

        // Simula que no existe un paciente con el ID 2
        when(patientRepository.findById(2L)).thenReturn(Optional.empty());

        // Realiza un intento de eliminación del paciente con el ID 2
        mockMvc.perform(delete("/api/patients/2"))
                .andExpect(status().isNotFound()); // Verifica que el estado sea HttpStatus.NOT_FOUND

        /*Prueba que verifica la eliminación de un paciente, ya sea cuando existe o no, mediante una solicitud DELETE a "/patients/{id}".
           Primero, se simula el comportamiento del repositorio para devolver un paciente con el ID 1 y se verifica que la respuesta tenga
           un estado 200 (OK) cuando intentamos eliminarlo. Luego, se utiliza Mockito para verificar que el método "deleteById" del
           repositorio se llame una vez.
           Después, se simula que no existe un médico con el ID 2 y se verifica que, al intentar eliminarlo, la respuesta tenga un estado
           404 (NOT FOUND), asegurando que no se elimine un paciente inexistente.
           Esto garantiza la cobertura de ambos casos en un solo test unitario.*/

    }

    @Test
    void testDeleteAllPatients() throws Exception {
        mockMvc.perform(delete("/api/patients"))
                .andExpect(status().isOk());

        verify(patientRepository, times(1)).deleteAll();

    /* Prueba que verifica la eliminación de todos los pacientes mediante una solicitud DELETE a "/api/patients".
       Se verifica que la respuesta tenga un estado 200 (OK).
       Se utiliza Mockito para verificar que el método "deleteAll" del repositorio se llame una vez. */
    }
}

@WebMvcTest(RoomController.class)
class RoomControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoomRepository roomRepository;

    @Test
    void testGetAllRooms() throws Exception {
        Room room = new Room("Operations");
        List<Room> rooms = Collections.singletonList(room);

        when(roomRepository.findAll()).thenReturn(rooms);

        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].roomName"). value("Operations"));

        /*  Prueba que verifica la obtención de la lista de habitaciones.
            Se simula el comportamiento del repositorio para devolver una lista de habitaciones.
            Luego, se realiza una solicitud GET a "/api/rooms" y se verifica que la respuesta contenga
            al menos una sala cuyo nombre sea "Operations". */
    }

    @Test
    void testGetRoomByRoomName() throws Exception {
        Room room = new Room("Operations");

        when(roomRepository.findByRoomName("Operations")).thenReturn(Optional.of(room));

        mockMvc.perform(get("/api/rooms/Operations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.roomName").value("Operations"));

        /*  Prueba que verifica la obtención de una sala por nombre.
            Se simula el comportamiento del repositorio para devolver una habitación con un nombre específico.
            Luego, se realiza una solicitud GET a "/api/rooms/Operations" y se verifica que la respuesta contenga
            una habitación con el nombre "Operations". */
    }

    @Test
    void testCreateRoom() throws Exception {
        Room room = new Room("Dining");

        mockMvc.perform(post("/api/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(room)))
                .andExpect(status().isCreated());

        verify(roomRepository, times(1)).save(any(Room.class));

        /*  Prueba que verifica la creación de una nueva habitación mediante una solicitud POST a "/api/room".
            Se crea una habitación y se verifica que la respuesta tenga un estado 201 (CREATED).
            Se utiliza Mockito para verificar que el método "save" del repositorio se llame una vez. */
    }


    @Test
    void testDeleteRoom() throws Exception {
        // Simula que existe un habitación con el nombre Operations
        Room room = new Room("Operations");
        when(roomRepository.findByRoomName("Operations")).thenReturn(Optional.of(room));

        // Realiza la eliminación de la habitación con el nombre Operations
        mockMvc.perform(delete("/api/rooms/Operations"))
                .andExpect(status().isOk()); // Verifica que el estado sea HttpStatus.OK

        // Verifica que la habitación con el nombre Operations se haya eliminado correctamente
        verify(roomRepository, times(1)).deleteByRoomName("Operations");

        // Simula que no existe una habitación con el nombre Bathroom
        when(roomRepository.findByRoomName("Bathroom")).thenReturn(Optional.empty());

        // Realiza un intento de eliminación de la habtación con el nombre Bathroom
        mockMvc.perform(delete("/api/doctors/Bathroom"))
                .andExpect(status().isNotFound()); // Verifica que el estado sea HttpStatus.NOT_FOUND

        /*Prueba que verifica la eliminación de una habitación, ya sea cuando existe o no, mediante una solicitud DELETE a "api/rooms/{roomName}".
           Primero, se simula el comportamiento del repositorio para devolver una habitación con el nombre Operations y se verifica que la respuesta tenga
           un estado 200 (OK) cuando intentamos eliminarlo. Luego, se utiliza Mockito para verificar que el método "delete" del
           repositorio se llame una vez.
           Después, se simula que no existe un habitación con el nombre Bathroom y se verifica que, al intentar eliminarlo, la respuesta tenga un estado
           404 (NOT FOUND), asegurando que no se elimine una habitación inexistente.
           Esto garantiza la cobertura de ambos casos en un solo test unitario.*/
    }

    @Test
    void testDeleteAllRooms() throws Exception {
        mockMvc.perform(delete("/api/rooms"))
                .andExpect(status().isOk());

        verify(roomRepository, times(1)).deleteAll();

    /* Prueba que verifica la eliminación de todos las habitaciones mediante una solicitud DELETE a "/api/rooms".
       Se verifica que la respuesta tenga un estado 200 (OK).
       Se utiliza Mockito para verificar que el método "deleteAll" del repositorio se llame una vez. */
    }
}
