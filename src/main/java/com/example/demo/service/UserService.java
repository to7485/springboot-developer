package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.UserEntity;
import com.example.demo.persistence.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service // 이 클래스가 스프링의 서비스 계층에 속하는 빈(Bean)임을 나타낸다.
@Slf4j // Lombok을 사용하여 로깅 기능을 자동으로 추가한다. log 객체를 통해 로그를 기록할 수 있다.
public class UserService {

    @Autowired // 스프링이 UserRepository 타입의 빈을 자동으로 주입해준다.
    private UserRepository repository; // UserRepository를 통해 데이터베이스에 접근하는 역할을 한다.

    public UserEntity create(UserEntity userEntity) {
        // 주어진 UserEntity가 null이거나 username이 null인 경우, 예외를 던진다.
        if(userEntity == null || userEntity.getUsername() == null) {
            throw new RuntimeException("Invalid arguments"); // 유효하지 않은 인자에 대해 예외를 발생시킨다.
        }

        final String username = userEntity.getUsername(); // UserEntity에서 username을 가져온다.
        
        // 주어진 username이 이미 존재하는 경우, 경고 로그를 남기고 예외를 던진다.
        if(repository.existsByUsername(username)) {
            log.warn("Username already exists {}", username); // 이미 존재하는 username에 대해 로그를 기록한다.
            throw new RuntimeException("Username already exists"); // 중복된 username인 경우 예외를 던진다.
        }
        
        // username이 중복되지 않았다면 UserEntity를 데이터베이스에 저장하고 반환한다.
        return repository.save(userEntity); // UserRepository의 save 메서드를 통해 userEntity를 저장한다.
    }
 
    // 주어진 username과 password로 UserEntity를 조회한다.
    public UserEntity getByCredentials(String username, String password, final PasswordEncoder encoder) {
    	final UserEntity originalUser = repository.findByUsername(username);
    	//matches메서드를 이용해 패스워드가 같은지 확인
    	if(originalUser != null && encoder.matches(password, originalUser.getPassword())) {
    		return originalUser;
    	}
        // UserRepository의 findByUsernameAndPassword 메서드를 사용하여 유저 정보를 조회한다.
        return null;
    }
}