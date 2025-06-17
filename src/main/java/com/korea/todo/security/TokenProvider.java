package com.korea.todo.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.korea.todo.model.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenProvider {

	//비밀키
	private static final String SECRET_KEY = "31f85e380ce0eddcbde08956916efee486f4cff60ed00f8d201e526b83fc066c0a244fde05353e9cdfc0ff76967e4ce649c6d5d1e9d29f8fceeffe9d08aab52debc3ddded9090b2e880c0039dbd899e3fad75fa2b918c4e6b3c20c3fd6ebdb8474b235737cb8cb272efba3d51798dacd608aca90eccec51195c434a7a44b0fe960c0e06c86e92c8f0659ae75ccbf5bde9ac12ba1e947538e190eb73652c1b93224e9326fe1af83d77f3c3972badde1c762853bb01b823bdaccacf61aa8d62ea8e668e80676cb22323f368934ba6d34e87680cbcc6ccd2ea36ccd24ca1c1b51b508d1629a3e64cc4a409673a30fd83d3c86cb817e12615ac78e36c0bd1ae754a31cdf45be7809150ece8ac0aaf1c169d3564ed4ba42fb1543e628b35bf662efd4b03ab527b567e42b97db52185987b8f4fbc2f247cbc5504adb2a1ced3038e46a9c6ba00a6ed9536a23578284844d65c9e4ceffbdc0a4d7b5265fadd414e071aa7397e252fef6b2b338faf24bf7227e122c774b6f350efa3e178a8e3c25a467d98e8123c5181f70f793a83f3b0cc5dfd94b316bbea397a7f683b3b4fae0be72e574b34ffb38367b43cfc5a17a94d9caca90d88797a9ca382182d87ce71f686c6ca454a844d9f33b955144c857b039bfab77e0afacc76096bfd4a0ff9d721399629225c1056d1a726e5cde8ba312a1173bded2f57123477034407f74c53c73ee3a";
	
	//토큰을 만드는 메서드
	public String create(UserEntity entity) {
		//토큰 만료시간을 설정
		//현재 시각 + 1일
		//Instant클래스 : 타임스탬프로 찍는다.
		//plus() : 첫번째 인자는 더할 양, 시간단위
		//ChronoUnit열거형의 DAYS 일 단위를 의미한다
		Date expiryDate = Date.from(Instant.now().plus(1,ChronoUnit.DAYS));
		
		/*
		 *
		 *{
		 * "alg":"HS512"
		 *}.
		 *{
		 * "sub":"402883e596b367d80196b367edb40000",
		 * "iss":"todo app",
		 * "iat":1595733657,
		 * "exp":1596597657,
		 *}.
		 *서명
		 * */
		
		//JWT 토큰을 생성
		return Jwts.builder()
				//header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
				.signWith(SignatureAlgorithm.HS512,SECRET_KEY) //헤더 + 서명 알고리즘 설정
				.setSubject(entity.getId())//sub 클레임 : 사용자 고유 ID
				.setIssuer("todo app")//iss 클레임 : 토큰 발급자
				.setIssuedAt(new Date())//iat 클레임 : 발급 시각
				.setExpiration(expiryDate)//exp 클레임 : 만료 시각
				.compact(); //최종 직렬화된 토큰 문자열 반환
	}
	
	
	public String validateAndGetUserId(String token) {
		Claims claims = Jwts.parser()
							.setSigningKey(SECRET_KEY) //서명 검증용 키 설정
							.parseClaimsJws(token) //토큰 파싱 및 서명 검증
							.getBody(); //내부 페이로드(Claims)획득
		
		return claims.getSubject(); //sub 클레임(사용자 ID) 반환
	}
}










