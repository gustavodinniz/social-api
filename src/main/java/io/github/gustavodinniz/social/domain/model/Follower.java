package io.github.gustavodinniz.social.domain.model;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "tb_followers")
@Data
public class Follower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;
}
