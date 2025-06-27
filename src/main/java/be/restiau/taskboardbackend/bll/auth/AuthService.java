package be.restiau.taskboardbackend.bll.auth;

import be.restiau.taskboardbackend.api.auth.dto.LoginRequest;
import be.restiau.taskboardbackend.api.auth.dto.LoginResponse;
import be.restiau.taskboardbackend.api.auth.dto.SignupRequest;
import be.restiau.taskboardbackend.api.auth.dto.UserLiteDTO;

public interface AuthService {
    UserLiteDTO register(SignupRequest signupRequest);
    LoginResponse login(LoginRequest loginRequest);
}
