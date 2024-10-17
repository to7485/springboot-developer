package com.example.demo.model;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Lombok을 사용하여 getter, setter, toString, equals, hashCode 등을 자동 생성해준다.
@Entity // JPA에서 엔티티 클래스임을 나타낸다. 이 클래스는 데이터베이스 테이블과 매핑된다.
@Builder // Lombok의 Builder 패턴을 사용하여 객체를 생성할 수 있도록 한다.
@NoArgsConstructor // 기본 생성자를 자동으로 생성해준다.
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자를 자동으로 생성해준다.
@Table(uniqueConstraints = {@UniqueConstraint(columnNames="username")}) 
// 테이블에서 username 컬럼이 유니크하도록 설정한다. 즉, 동일한 username을 가진 유저는 중복으로 생성될 수 없다.
public class UserEntity {

    @Id // JPA에서 이 필드가 테이블의 Primary Key임을 나타낸다.
    @GeneratedValue(generator="system-uuid") // id 필드는 자동으로 생성된다. 여기서는 UUID 전략을 사용한다.
    @GenericGenerator(name="system-uuid", strategy="uuid") 
    // Hibernate에서 제공하는 UUID를 생성하는 커스텀 전략을 사용한다. system-uuid라는 이름으로 UUID를 생성하는 방식이다.
    private String id; // 유저에게 고유하게 부여되는 ID. UUID로 생성되며 고유한 값이다.

    @Column(nullable=false) // username 컬럼은 null 값을 허용하지 않는다.
    private String username; // 아이디로 사용할 유저네임. 이메일일 수도 있고, 그냥 문자열일 수도 있다.

    private String password; // 유저의 패스워드.

    private String role; // 유저의 권한. 예를 들어 "관리자", "일반사용자"와 같은 값이 들어갈 수 있다.

    private String authProvider; // 이후 OAuth에서 사용할 유저 정보 제공자 : github
}