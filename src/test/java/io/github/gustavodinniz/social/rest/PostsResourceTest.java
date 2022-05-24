package io.github.gustavodinniz.social.rest;

import io.github.gustavodinniz.social.domain.model.User;
import io.github.gustavodinniz.social.domain.repository.UserRepository;
import io.github.gustavodinniz.social.rest.dto.CreatePostRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(PostsResource.class)
class PostsResourceTest {

    @Inject
    UserRepository userRepository;

    Long userId;

    @BeforeEach
    @Transactional
    public void setUP() {
        var user = new User();
        user.setAge(30);
        user.setName("Gustavo Diniz");
        userRepository.persist(user);
        userId = user.getId();
    }

    @Test
    @DisplayName("should create a post for a user")
    public void createPostTest() {
        var postRequest = new CreatePostRequest();
        postRequest.setText("Some text");

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("userId", userId)
                .when()
                .post()
                .then()
                .statusCode(201);
    }
}