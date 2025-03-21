package hello.hello_spring.domain;

import jakarta.persistence.*;

// JPA가 관리하는 엔티티
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // db에 데이터를 생성하면 생성되는 ID
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
