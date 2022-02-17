package io.github.gustavodinniz.social.domain.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "tb_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Integer age;

}
