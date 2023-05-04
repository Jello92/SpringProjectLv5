package com.example.board_spring5.user.service;

import com.example.board_spring5.global.dto.StatusResponseDto;
import com.example.board_spring5.global.exception.ErrorException;
import com.example.board_spring5.global.exception.ErrorResponseDto;
import com.example.board_spring5.global.exception.ExceptionEnum;
import com.example.board_spring5.global.jwt.JwtUtil;
import com.example.board_spring5.user.dto.UserRequestDto;
import com.example.board_spring5.user.entity.UserRoleEnum;
import com.example.board_spring5.user.entity.Users;
import com.example.board_spring5.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public ResponseEntity<?> signUp(UserRequestDto userRequestDto) {
        String username = userRequestDto.getUsername();
        String password = passwordEncoder.encode(userRequestDto.getPassword());

        Optional<Users> check = userRepository.findByUsername(userRequestDto.getUsername());
        if (check.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(ExceptionEnum.USERS_DUPLICATION));
        }
        UserRoleEnum userRoleEnum = UserRoleEnum.USER;
        if (userRequestDto.isAdmin()) {
            if (!userRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new ErrorException(ExceptionEnum.TOKEN_NOT_FOUND);
            }
            userRoleEnum = UserRoleEnum.ADMIN;
        }
        Users users = new Users(username, password, userRoleEnum);
        userRepository.save(users);
        return ResponseEntity.ok(new StatusResponseDto("회원가입 성공", HttpStatus.OK.value()));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> login(UserRequestDto userRequestDto, HttpServletResponse httpServletResponse) {
        String username = userRequestDto.getUsername();
        String password = userRequestDto.getPassword();

        Users users = userRepository.findByUsername(username).orElseThrow(
                () -> new ErrorException(ExceptionEnum.USER_NOT_FOUND)
        );

        if (!passwordEncoder.matches(password, users.getPassword())) {
            throw new ErrorException(ExceptionEnum.INVALID_PASSWORD);
        }

        String token = jwtUtil.createToken(users.getUsername(), users.getRole());
        httpServletResponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        return ResponseEntity.ok(new StatusResponseDto("로그인 성공", HttpStatus.OK.value()));
    }

}
