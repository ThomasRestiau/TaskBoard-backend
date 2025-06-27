package be.restiau.taskboardbackend.bll.auth;

import be.restiau.taskboardbackend.api.auth.dto.SignupRequest;
import be.restiau.taskboardbackend.api.auth.dto.UserLiteDTO;
import be.restiau.taskboardbackend.domain.User;
import be.restiau.taskboardbackend.dal.UserRepository;
import be.restiau.taskboardbackend.infra.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AuthServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks private AuthServiceImpl authService;

    private SignupRequest request;
    private User toSave;
    private User saved;

    @BeforeEach
    void setUp() {
        request = new SignupRequest("john@mail.com", "John Doe", "pass12345");

        toSave = new User();                           // entité avant save
        saved  = new User();                           // entité retournée par save
        saved.setEmail("john@mail.com");
        saved.setFullName("John Doe");
        saved.setPasswordHash("bcryptHash");
    }

    @Test
    void register_should_create_new_user() {
        // Arrange
        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("bcryptHash");
        when(userMapper.toEntity(request, "bcryptHash")).thenReturn(toSave);
        when(userRepository.save(toSave)).thenReturn(saved);
        when(userMapper.toDTO(saved))
                .thenReturn(new UserLiteDTO(1L, "john@mail.com", "John Doe"));

        // Act
        UserLiteDTO dto = authService.register(request);

        // Assert
        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.email()).isEqualTo("john@mail.com");
        verify(userRepository).save(toSave);
    }

    @Test
    void register_should_throw_if_email_already_exists() {
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Email");

        verify(userRepository, never()).save(any());
    }
}
