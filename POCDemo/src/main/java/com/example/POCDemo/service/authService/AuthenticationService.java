package com.example.POCDemo.service.authService;

import com.example.POCDemo.Configuration.security.JwtService;
import com.example.POCDemo.domain.Role;
import com.example.POCDemo.domain.User;
import com.example.POCDemo.repository.IUserRepository;
import com.example.POCDemo.request.AuthenticationRequest;
import com.example.POCDemo.request.RegisterRequest;
import com.example.POCDemo.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final IUserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        //
        try {
            User user = User.builder()
                    .userName(registerRequest.getUsername())
                    .firstName(registerRequest.getFirstName())
                    .lastName(registerRequest.getLastName())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .roles(List.of(Role.builder().role("User_Role").build()))
                    .build();
            userRepository.save(user);
            var authResponse=generateToken(user,"");
            return authResponse;
        } catch (Exception ex) {
            return AuthenticationResponse.builder()
                    .token(null)
                    .refreshToken(null)
                    .message(ex.getMessage())
                    .build();
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken
                            (authenticationRequest.getUsername()
                                    , authenticationRequest.getPassword()));
            User user = userRepository.findByuserName(authenticationRequest.getUsername())
                    .orElseThrow(ChangeSetPersister.NotFoundException::new);
            var authResponse=generateToken(user,"");
           return authResponse;
        } catch (Exception ex) {
            return AuthenticationResponse.builder()
                    .token(null)
                    .refreshToken(null)
                    .message(ex.getMessage())
                    .build();
        }

    }

    public AuthenticationResponse handelRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            final String authHeader = request.getHeader("Authorization");
            final String refreshToken;
            final String username;
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new Exception("Forbidden");
            }
            refreshToken = authHeader.substring(7);
            username = jwtService.extractUsername(refreshToken);// to do implement this method
            if (username != null ) {
                UserDetails user = userRepository.findByuserName(username).orElse(null);
                if (user != null) {
                    if (jwtService.isJwtValid(refreshToken, user)) {
                        var authResponse = generateToken(user,refreshToken);
                        return authResponse;

                    }
                    else {
                        throw new Exception("Forbidden");
                    }
                }
                else {
                    throw new Exception("User is not exist");

                }
            }
            else {
                throw new Exception("Forbidden");

            }

        }
        catch(Exception ex){
            return AuthenticationResponse.builder()
                    .token(null)
                    .refreshToken(null)
                    .message(ex.getMessage())
                    .build();
        }

    }
    private AuthenticationResponse generateToken(UserDetails user,String refreshToken){
        List<String> authorities = user.getAuthorities().stream()
                .map(grantedAuth -> grantedAuth.getAuthority()).collect(Collectors.toList());
        Map<String, List<String>> claims = new HashMap<>();
        claims.put("Role", authorities);
        String jwt = jwtService.generateJwt(claims, user);

        if(refreshToken.isEmpty()||refreshToken.isBlank()){
            refreshToken = jwtService.generateRefreshJWT(null, user);
        }
        return AuthenticationResponse.builder()
                .token(jwt)
                .refreshToken(refreshToken)
                .message("success")
                .build();
    }
}