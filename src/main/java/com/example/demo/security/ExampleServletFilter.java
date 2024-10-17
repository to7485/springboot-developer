package com.example.demo.security;

import java.io.IOException;

import org.springframework.util.StringUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ExampleServletFilter extends HttpFilter{

	private TokenProvider tokenProvider;
	
	@Override
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			final String token = parseBearerToken(request);
			
			if(token != null && !token.equalsIgnoreCase("null")) {
				//userId 가져오기. 위조된 경우 예외 처리된다.
				String userId = tokenProvider.validateAndGetUserId(token);
				
				//다음 ServletFilter 실행
				chain.doFilter(request,response);
			}
		} catch (Exception e) {
			//예외 발생시 response를 403 Forbidden으로 설정.
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}
	}
	
	private String parseBearerToken(HttpServletRequest request) {
		//Http 요청의 헤더를 파싱해 Barer 토큰을 반환한다.
		String bearerToken = request.getHeader("Authorization");
		
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}


}







