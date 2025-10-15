import com.example.springback.model.MyUser;
import com.example.springback.repository.MyUserRepository;
import com.example.springback.controler.RegistrationController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RegistrationControllerTest {

    @Mock
    private MyUserRepository myUserRepository; // **Repositorio simulado para MyUser

    @Mock
    private PasswordEncoder passwordEncoder; // PasswordEncoder simulado

    @InjectMocks
    private RegistrationController registrationController; // Controlador a probar

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks antes de cada prueba**
    }

    /**
     * Prueba para crear un nuevo usuario
     * Verifica que un nuevo usuario sea creado y que su contraseña esté en formato hash.
     */
    @Test
    public void testCreateUser() {
        MyUser user = new MyUser();
        user.setUsername("testuser");
        user.setPassword("password");

        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(myUserRepository.save(any(MyUser.class))).thenReturn(user);

        MyUser createdUser = registrationController.createUser(user);

        assertNotNull(createdUser); // **Verifica que el usuario creado no sea nulo**
        assertEquals("testuser", createdUser.getUsername()); // **Verifica que el nombre de usuario sea correcto**
        assertEquals("encodedPassword", createdUser.getPassword()); // **Verifica que la contraseña esté codificada**
        verify(myUserRepository).save(user); // **Verifica que se llame al método save en el repositorio**
    }

    /**
     * Prueba para agregar un usuario como administrador**
     * Verifica que se redirija correctamente después de agregar un nuevo usuario.
     */
    @Test
    public void testAddUser() throws IOException {
        MyUser user = new MyUser();
        user.setUsername("adminuser");
        user.setPassword("adminpassword");
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedAdminPassword");
        registrationController.addUser(user, response);
        verify(myUserRepository).save(user);
        verify(response).sendRedirect("https://mipaginita.duckdns.org/home_admin.html");
    }
    /**
     * Prueba para obtener todos los usuarios**
     * Verifica que se devuelva la lista de usuarios correctamente.
     */
    @Test
    public void testGetAllUsers() {
        MyUser user1 = new MyUser();
        user1.setUsername("user1");
        MyUser user2 = new MyUser();
        user2.setUsername("user2");

        List<MyUser> users = Arrays.asList(user1, user2);

        when(myUserRepository.findAll()).thenReturn(users); // Simula la respuesta del repositorio**

        List<MyUser> result = registrationController.getAllUsers(); // Llama al método para obtener todos los usuarios**

        assertNotNull(result); // Verifica que la lista no sea nula**
        assertEquals(2, result.size()); // **Verifica que la lista tenga el tamaño correcto**
        assertEquals("user1", result.get(0).getUsername()); // Verifica el primer usuario**
        assertEquals("user2", result.get(1).getUsername()); // Verifica el segundo usuario**
    }

    /**
     * **Prueba para obtener todos los usuarios como respuesta de entidad**
     *
     * Verifica que se devuelva la respuesta correcta al solicitar usuarios.
     */
    @Test
    public void testFindUsers() {
        MyUser user1 = new MyUser();
        user1.setUsername("user1");
        MyUser user2 = new MyUser();
        user2.setUsername("user2");

        List<MyUser> users = Arrays.asList(user1, user2);

        when(myUserRepository.findAll()).thenReturn(users); // Simula la respuesta del repositorio**

        ResponseEntity<List<MyUser>> response = registrationController.findUsers(); // Llama al método para obtener todos los usuarios**

        assertNotNull(response); //Verifica que la respuesta no sea nula**
        assertEquals(200, response.getStatusCodeValue()); //Verifica que el código de estado sea 200**
        assertEquals(2, response.getBody().size()); // Verifica que la lista de usuarios tenga el tamaño correcto**
    }
}
