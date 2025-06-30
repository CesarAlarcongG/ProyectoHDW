package com.negocio.ProyectoHerramientaDeDesarrolloWeb.serviceTest;


import com.negocio.ProyectoHerramientaDeDesarrolloWeb.dto.CredencialesDto;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.dto.UsuarioDto;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.Usuario;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.enums.Rol;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.repository.UsuarioRepository;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.service.JwtService;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerPorIdUsuarioExistente() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void testObtenerPorIdUsuarioNoExistente() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () -> {
            usuarioService.obtenerPorId(99L);
        });

        assertTrue(ex.getMessage().contains("no encontrado"));
    }

    @Test
    void testAutenticarCredenciales() {
        CredencialesDto dto = new CredencialesDto("correo@example.com", "password123");

        usuarioService.autenticarCredenciales(dto);

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("correo@example.com", "password123")
        );
    }

    @Test
    void testValidarExistenciaDeUsuario() {
        when(usuarioRepository.findByDniAndEmail("12345678", "test@example.com"))
                .thenReturn(Optional.of(new Usuario()));

        boolean existe = usuarioService.validarExistenciaDeUsuario("12345678", "test@example.com");

        assertTrue(existe);
    }

    @Test
    void testMapearAUsuario() {
        UsuarioDto dto = UsuarioDto.builder()
                .dni("87654321")
                .nombre("Carlos")
                .apellido("Ramirez")
                .correo("carlos@example.com")
                .contraseña("clave123")
                .rol("ADMINISTRADOR")
                .build();

        when(passwordEncoder.encode("clave123")).thenReturn("clave_encriptada");

        Usuario usuario = usuarioService.mapearAUsuario(dto, dto.getRol());

        assertEquals("Carlos", usuario.getNombres());
        assertEquals("clave_encriptada", usuario.getContraseña());
        assertEquals(Rol.ADMINISTRADOR, usuario.getRoles());
    }
}

