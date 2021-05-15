package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ReqresApiTests {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://reqres.in";
    }

    @Test
    public void successfulUsersLListTest() {
        given().when()
                .get("/api/users?page=2")
                .then()
                .statusCode(200)
                .body("page", is(2))
                .body("per_page", is(6))
                .body("total", is(12))
                .body("total_pages", is(2))
                .body("data", not(nullValue()))
                .body("support.url", is("https://reqres.in/#support-heading"))
                .body("support.text", is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    @Test
    public void successfulSingleUserTest() {
        given().when()
                .get("/api/users/2")
                .then()
                .statusCode(200)
                .body("data.id", is(2))
                .body("data.email", is("janet.weaver@reqres.in"))
                .body("data.first_name", is("Janet"))
                .body("data.last_name", is("Weaver"))
                .body("data.avatar", is("https://reqres.in/img/faces/2-image.jpg"))
                .body("support.url", is("https://reqres.in/#support-heading"))
                .body("support.text", is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    @Test
    public void unsuccessfulSingleUserTest() {
        given().when()
                .get("/api/users/23")
                .then()
                .statusCode(404)
                .body("data", is(nullValue()));
    }

    @Test
    public void successfulLoginTest() {
        given().contentType(ContentType.JSON)
                .body("{ \"email\": \"eve.holt@reqres.in\", " +
                        "\"password\": \"cityslicka\" }")
                .when()
                .post("/api/login")
                .then()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    public void unsuccessfulLoginTest() {
        given().contentType(ContentType.JSON)
                .body("{ \"email\": \"eve.holt@reqres.in\"}")
                .when()
                .post("/api/login")
                .then()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    public void successfulRegistrationTest() {
        given().contentType(ContentType.JSON)
                .body("{ \"email\": \"eve.holt@reqres.in\", " +
                        "\"password\": \"pistol\" }")
                .when()
                .post("/api/register")
                .then()
                .statusCode(200)
                .body("id", is(4))
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    public void unsuccessfulRegistrationTest() {
        given().contentType(ContentType.JSON)
                .body("{ \"email\": \"eve.holt@reqres.in\"}")
                .when()
                .post("/api/register")
                .then()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    public void successfulSingleResourceTest() {
        given().when()
                .get("/api/unknown/2")
                .then()
                .statusCode(200)
                .body("data.id", is(2))
                .body("data.name", is("fuchsia rose"))
                .body("data.year", is(2001))
                .body("data.color", is("#C74375"))
                .body("data.pantone_value", is("17-2031"))
                .body("support.url", is("https://reqres.in/#support-heading"))
                .body("support.text", is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    @Test
    public void successfulCreateUserTest() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

        given().contentType(ContentType.JSON)
                .body("{ \"name\": \"morpheus\", \"job\": \"leader\" }")
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .body("name", is("morpheus"))
                .body("job", is("leader"))
                .body("id",not(nullValue()))
                .body("createdAt", containsString(currentDate));
    }

    @Test
    public void successfulUpdateUserTest() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

        given().contentType(ContentType.JSON)
                .body("{ \"name\": \"morpheus2\", \"job\": \"leader\" }")
                .when()
                .put("/api/users/2")
                .then()
                .statusCode(200)
                .body("name", is("morpheus2"))
                .body("job", is("leader"))
                .body("updatedAt", containsString(currentDate));
    }

    @Test
    public void successfulDeleteUserTest() {
        given().contentType(ContentType.JSON)
                .when()
                .delete("/api/users/2")
                .then()
                .statusCode(204);
    }
}
