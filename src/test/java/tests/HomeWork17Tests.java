package tests;

import org.junit.jupiter.api.Test;

import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.Arrays;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HomeWork17Tests {

    @Test
    void listUsersStatusCode() {
         /*
        request: https://reqres.in/api/users?page=2

        response:
        {
            {
    "page": 2,
    "per_page": 6,
    "total": 12,
    "total_pages": 2,
    "data": [
        {
            "id": 7,
            "email": "michael.lawson@reqres.in",
            "first_name": "Michael",
            "last_name": "Lawson",
            "avatar": "https://reqres.in/img/faces/7-image.jpg"
        },
        {
            "id": 8,
            "email": "lindsay.ferguson@reqres.in",
            "first_name": "Lindsay",
            "last_name": "Ferguson",
            "avatar": "https://reqres.in/img/faces/8-image.jpg"
        },
        {
            "id": 9,
            "email": "tobias.funke@reqres.in",
            "first_name": "Tobias",
            "last_name": "Funke",
            "avatar": "https://reqres.in/img/faces/9-image.jpg"
        },
        {
            "id": 10,
            "email": "byron.fields@reqres.in",
            "first_name": "Byron",
            "last_name": "Fields",
            "avatar": "https://reqres.in/img/faces/10-image.jpg"
        },
        {
            "id": 11,
            "email": "george.edwards@reqres.in",
            "first_name": "George",
            "last_name": "Edwards",
            "avatar": "https://reqres.in/img/faces/11-image.jpg"
        },
        {
            "id": 12,
            "email": "rachel.howell@reqres.in",
            "first_name": "Rachel",
            "last_name": "Howell",
            "avatar": "https://reqres.in/img/faces/12-image.jpg"
        }
    ],
    "support": {
        "url": "https://reqres.in/#support-heading",
        "text": "To keep ReqRes free, contributions towards server costs are appreciated!"
    }
}
        }
         */
        get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200);
    }

    @Test
    void singleUserDataId() {
        /*
        {
        "data": {
            "id": 2,
            "email": "janet.weaver@reqres.in",
            "first_name": "Janet",
            "last_name": "Weaver",
            "avatar": "https://reqres.in/img/faces/2-image.jpg"
        },
        "support": {
            "url": "https://reqres.in/#support-heading",
            "text": "To keep ReqRes free, contributions towards server costs are appreciated!"
        }
        }*/
        int response = get("https://reqres.in/api/users/2")
                .then()
                .extract()
                .path("data.id");

        int expectedResponse = 2;
        assertEquals(expectedResponse, response);
    }

    @Test
    void singleUserNotFound() {
        /*
        {}*/
        get("https://reqres.in/api/users/23")
                .then()
                .statusCode(404);
    }

    @Test
    void Create() {
        /*
        request: https://reqres.in/api/users
        data:
        {
            "name": "morpheus",
            "job": "leader"
        }
        response:
        {
            "name": "morpheus",
            "job": "leader",
            "id": "542",
            "createdAt": "2022-04-20T05:21:50.672Z"
        }
         */

        String DataUser = "{\"name\": \"morpheus\", " +
                "\"job\": \"leader\"}";

        given()
                .body(DataUser)
                .contentType(JSON)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .statusCode(201)
                .body("name", is("morpheus"))
                .body("job", is("leader"));
    }

    @Test
    void Delete() {
        /*
        {}*/
        get("https://reqres.in/api/users/2")
                .then()
                .statusCode(200);
    }


}
