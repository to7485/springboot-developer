package com.korea.todo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.korea.todo.model.TodoEntity;
import com.korea.todo.persistence.TodoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
//스프링 프레임워크에서 제공하는 어노테이션중 하나로, 서비스 레이어에 사용되는
//클래스를 명시할 때 사용
//이 어노테이션을 사용하면 스프링이 해당 클래스를 스프링 컨테이너에서 관리하는 빈(bean)
//으로 등록하고, 비즈니스 로직을 처리하는 역할을 맡는다.
public class TodoService {
	
	//영속계층의 클래스를 자동으로 주입해서 사용할 수 있다.
	@Autowired
	private TodoRepository repository;

//	public String testService() {
//		//엔티티 하나 생성
//		TodoEntity entity = TodoEntity.builder()
//								.userId("홍길동")
//								.title("My first todo item")
//								.build();
//		//TodoEntity저장(db에 저장)
//		repository.save(entity);
//		//TodoEntity검색(select)
//		List<TodoEntity> savedEntity = repository.findByUserIdQuery(entity.getUserId());
//		return savedEntity.get(0).getTitle();
//	}
	
	//추가하고 userId를 기준으로 목록을 반환
	public List<TodoEntity> create(TodoEntity entity){

		validate(entity);
		
		//데이터베이스에 추가
		//insert into todo values(....)
		repository.save(entity);
		
		log.info("Entity Id : {} is saved",entity.getId());
		
		//엔티티를 데이터베이스에 추가하고 전체 조회를 한다.
		return repository.findByUserId(entity.getUserId());
	}
	
	//조회하는 retrive메서드
	public List<TodoEntity> retrive(String userId){
		//select * from todo where userId=xxx
		return repository.findByUserId(userId);
	}
	
	
	public List<TodoEntity> update(TodoEntity entity){
		//저장할 엔티티가 유효한지 확인
		validate(entity);
		
		//넘겨받은 엔티티 id를 이용해 TodoEntity를 가져온다.
		//존재하지 않는 엔티티는 수정할 수 없으니까
		Optional<TodoEntity> original = repository.findById(entity.getId());
		
		original.ifPresent(todo -> {
			//반환된 TodoEntity가 존재한다면, 값을 새 Entity값으로 덮어씌운다.
			todo.setTitle(entity.getTitle());
			todo.setDone(entity.isDone());
			
			//데이터베이스에 새 값을 저장한다.
			repository.save(todo);
		});
		
		//전체 내용을 조회해서 반환
		return retrive(entity.getUserId());
	}
	
	//삭제하기
	//delete메서드 만들기
	//넘어온 entity가 유효한지 확인하고
	//delete()를 이용해서 db에서 삭제를 하고
	//findByUserId()를 사용해 조회를 해서 반환
	public List<TodoEntity> delete(TodoEntity entity){
		//삭제할 엔티티가 유효한지 확인
		validate(entity);
		
		repository.delete(entity);
		
		return repository.findByUserId(entity.getUserId());
	}
	
	
	
	
	
	
	
	private void validate(TodoEntity entity) {
		//매개변수로 넘어온 entity가 유효한지 검사한다.
		if(entity == null) {
			log.warn("Entity cannot be null");
			throw new RuntimeException("Entity cannot be null");
		}
		
		if(entity.getUserId() == null) {
			log.warn("Unknown user");
			throw new RuntimeException("Unknown user");
		}
	}
	
	
	
	
	
	
	
}

//Optional
//null값이 나와도 추가적인 처리를 할 수 있는 다양한 메서드를 제공한다.
//1. isPresent() : 반환된 Optional 객체 안에 값이 존재하면 true, 존재하지 않으면 false를 반환한다.
//2. get() : Optional안에 값이 존재할 때, 그 값을 반환한다.
//없는데 호출하면 NoSuchElementException이 발생할 수 있다.
//3. orElse(T other) : 값이 존재하지 않을 때 기본값을 반환한다.

//findById()메서드의 반환형이 Optional인 이유는 조회하려는 ID가 존재하지 않을 수 있기 때문이다.








