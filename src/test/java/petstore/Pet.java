package petstore;

import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;

public class Pet {
    String uri = "https://petstore.swagger.io/v2/pet";

    public String lerJson(String caminhoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }

    @Test(priority = 1)
    public void incluirPet() throws IOException {
        String jsonBody = lerJson("src/test/resources/db/pet1.json");

        // Gherkin -> Dado - Quando - Entao (Given - When - Then)

        given()
                .contentType("application/json")
                .log().all()
                .body(jsonBody)
        .when()
                .post(uri)
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Snoopy"))
                .body("status", is("available"))
                .body("category.name", is("dog"))
                .body("tags.name", contains("sta"));
        ;
    }

    @Test(priority = 2)
    public void consultarPet(){
        String petId = "154";
        String nameDog =
        given()
                .contentType("application/json")
                .log().all()
        .when()
                .get(uri + "/" + petId)
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Snoopy"))
                .body("category.name", is("dog"))
                .body("tags.name", contains("sta"))
        .extract()
                .path("name")
        ;

        System.out.println("O nome do dog: " + nameDog);
    }

    @Test(priority = 3)
    public void alterarPet() throws IOException {
        String jsonBody = lerJson("src/test/resources/db/pet2.json");

        given()
                .contentType("application/json")
                .log().all()
                .body(jsonBody)
        .when()
                .put(uri)
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Atena"))
                .body("status", is("sold"))
        ;
    }

    @Test(priority = 4)
    public void excluirPet(){
        String petId = "154";

        given()
                .contentType("application/json")
                .log().all()
        .when()
                .delete(uri + "/" + petId)
        .then()
                .log().all()
                .statusCode(200)
                .body("code", is(200))
                .body("type", is("unknown"))
                .body("message", is(petId))
        ;

    }


}
