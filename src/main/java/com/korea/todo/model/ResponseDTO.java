package com.korea.todo.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

//http응답으로 사용할 DTO

@Builder
@AllArgsConstructor
@Data
public class ResponseDTO<T> {
	
	private String error;
	private List<T> data;
}







