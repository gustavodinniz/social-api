package io.github.gustavodinniz.social.rest;

import io.github.gustavodinniz.social.domain.model.User;
import io.github.gustavodinniz.social.domain.repository.FollowerRepository;
import io.github.gustavodinniz.social.domain.repository.UserRepository;
import io.github.gustavodinniz.social.rest.dto.FollowerRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(FollowerResource.class)
class FollowerResourceTest {

    @Inject
    UserRepository userRepository;

    @Inject
    FollowerRepository followerRepository;

    Long userId;
    Long followerId;

    @BeforeEach
    @Transactional
    void setUp() {
        var user = new User();
        user.setAge(30);
        user.setName("Gustavo Diniz");
        userRepository.persist(user);
        userId = user.getId();

        var follower = new User();
        follower.setAge(31);
        follower.setName("Gustavo Gomes");
        userRepository.persist(follower);
        followerId = follower.getId();
    }

    @Test
    @DisplayName("should return 409 when followerId is equal to userId")
    public void saveUserAsFollowerTest() {

        var followerRequest = new FollowerRequest();
        followerRequest.setFollowerId(userId);

        given()
                .contentType(ContentType.JSON)
                .body(followerRequest)
                .pathParam("userId", userId)
                .when()
                .put()
                .then()
                .statusCode(409)
                .body(Matchers.is("You can't follow yourself"));
    }

    @Test
    @DisplayName("should return 404 when userId doesn't exist")
    public void userNotFoundTest() {

        var followerRequest = new FollowerRequest();
        followerRequest.setFollowerId(userId);

        var inexistentUserId = 999;

        given()
                .contentType(ContentType.JSON)
                .body(followerRequest)
                .pathParam("userId", inexistentUserId)
                .when()
                .put()
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("should follow a user")
    public void followUserTest() {

        var followerRequest = new FollowerRequest();
        followerRequest.setFollowerId(followerId);

        given()
                .contentType(ContentType.JSON)
                .body(followerRequest)
                .pathParam("userId", userId)
                .when()
                .put()
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }
}