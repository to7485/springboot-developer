package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.UserEntity;
import com.example.demo.security.TokenProvider;
import com.example.demo.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenProvider tokenProvider;
	
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
		try {
			// 리퀘스트를 이용해 저장할 유저 만들기
			UserEntity user = UserEntity.builder()
							.username(userDTO.getUsername())
							.password(passwordEncoder.encode(userDTO.getPassword()))
							.build();
			// 서비스를 이용해 리파지토리에 유저 저장
			UserEntity registeredUser = userService.create(user);
			UserDTO responseUserDTO = UserDTO.builder()
							.id(registeredUser.getId())
							.username(registeredUser.getUsername())
							.build();
			// 유저 정보는 항상 하나이므로 그냥 리스트로 만들어야하는 ResponseDTO를 사용하지 않고 그냥 UserDTO 리턴.
			return ResponseEntity.ok(responseUserDTO);
		} catch (Exception e) {
			// 예외가 나는 경우 bad 리스폰스 리턴.
			ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
			return ResponseEntity
							.badRequest()
							.body(responseDTO);
		}
	}

	@PostMapping("/signin")
	public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
		UserEntity user = userService.getByCredentials(
						userDTO.getUsername(),
						userDTO.getPassword(),
						passwordEncoder);

		if(user != null) {
			///////////////////토큰 생성////////////////
			final String token = tokenProvider.create(user);
         ///////////////////토큰 생성////////////////
			final UserDTO responseUserDTO = UserDTO.builder()
							.username(user.getUsername())
							.id(user.getId())
                     //////토큰 주입///////
							.token(token)
                     //////토큰 주입///////
							.build();
			return ResponseEntity.ok().body(responseUserDTO);
		} else {
			ResponseDTO responseDTO = ResponseDTO.builder()
							.error("Login failed.")
							.build();
			return ResponseEntity
							.badRequest()
							.body(responseDTO);
		}
	}
}