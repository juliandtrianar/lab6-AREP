import com.example.springback.model.MyUser;
import com.example.springback.repository.MyUserRepository;
import com.example.springback.service.MyUserDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * **Clase de prueba para MyUserDetailService**
 *
 * Esta clase contiene pruebas unitarias para el servicio MyUserDetailService,
 * utilizando Mockito para simular el repositorio de usuarios.
 */
public class MyUserDetailServiceTest {

    @Mock
    private MyUserRepository repository; // **Repositorio simulado para MyUser**

    @InjectMocks
    private MyUserDetailService userDetailService; // **Servicio de detalles de usuario a probar**

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // **Inicializa los mocks antes de cada prueba**
    }

    /**
     * **Prueba para cargar un usuario existente**
     *
     * Verifica que un usuario válido sea cargado correctamente
     * y que el rol se asigne adecuadamente.
     */
    @Test
    public void testLoadUser() {
        String username = "testuser";
        MyUser myUser = new MyUser();
        myUser.setUsername(username);
        myUser.setPassword("password");
        myUser.setRole("USER");

        when(repository.findByUsername(username)).thenReturn(Optional.of(myUser));

        UserDetails userDetails = userDetailService.loadUserByUsername(username);

        assertNotNull(userDetails); // **Verifica que userDetails no sea nulo**
        assertEquals(username, userDetails.getUsername()); // **Verifica que el nombre de usuario sea correcto**
        assertEquals("password", userDetails.getPassword()); // **Verifica que la contraseña sea correcta**

        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"))); // **Verifica que tenga el rol USER**
    }

    /**
     * **Prueba para cargar un administrador**
     *
     * Verifica que un usuario con rol ADMIN sea cargado correctamente
     * y que el rol se asigne adecuadamente.
     */
    @Test
    public void testLoadAdmin() {
        String username = "testuser";
        MyUser myUser = new MyUser();
        myUser.setUsername(username);
        myUser.setPassword("password");
        myUser.setRole("ADMIN");

        when(repository.findByUsername(username)).thenReturn(Optional.of(myUser));

        UserDetails userDetails = userDetailService.loadUserByUsername(username);

        assertNotNull(userDetails); // Verifica que userDetails no sea nulo
        assertEquals(username, userDetails.getUsername()); // Verifica que el nombre de usuario sea correcto**
        assertEquals("password", userDetails.getPassword()); // Verifica que la contraseña sea correcta**

        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))); // Verifica que tenga el rol ADMIN**
    }

    /**
     * Prueba para manejar un usuario no encontrado
     * Verifica que se lance una excepción UsernameNotFoundException
     * al intentar cargar un usuario que no existe.
     */
    @Test
    public void testLoadUserByUsername_UserNotFound() {
        String username = "nonexistentuser";

        when(repository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailService.loadUserByUsername(username); // Intenta cargar un usuario que no existe
        });
    }
}
