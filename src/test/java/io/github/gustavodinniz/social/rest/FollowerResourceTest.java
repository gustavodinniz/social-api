package io.github.gustavodinniz.social.rest;

import io.github.gustavodinniz.social.domain.model.Follower;
import io.github.gustavodinniz.social.domain.model.User;
import io.github.gustavodinniz.social.domain.repository.FollowerRepository;
import io.github.gustavodinniz.social.domain.repository.UserRepository;
import io.github.gustavodinniz.social.rest.dto.FollowerRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

        var followerEntity = new Follower();
        followerEntity.setFollower(follower);
        followerEntity.setUser(user);
        followerRepository.persist(followerEntity);
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
    @DisplayName("should return 404 on follow a user when userId doesn't exist")
    public void userNotFoundWhenTryingToFollowTest() {

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

    @Test
    @DisplayName("should return 404 on list user followers and userId doesn't exist")
    public void userNotFoundWhenListingFollowersTest() {
        var inexistentUserId = 999;

        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", inexistentUserId)
                .when()
                .get()
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("should list a user's followers")
    public void listFollowersTest() {

        var response = given()
                .contentType(ContentType.JSON)
                .pathParam("userId", userId)
                .when()
                .get()
                .then()
                .extract()
                .response();

        var followersCount = response.jsonPath().get("followersCount");
        assertEquals(Response.Status.OK.getStatusCode(), response.statusCode());
        assertEquals(1, followersCount);
    }
}