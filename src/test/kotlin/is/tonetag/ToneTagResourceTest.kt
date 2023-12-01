package `is`.tonetag // "is" is a reserved keyword in Kotlin, so we need to escape it...

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test

@QuarkusTest
class ToneTagResourceTest {

    @Test
    fun testJokingEnGBEndpoint() {
        given()
          .`when`().get("/j/en_gb")
          .then()
             .statusCode(200)
             .body(containsString("This tone indicator shows that the text is a joke and not meant to be taken seriously."))
    }

    @Test
    fun testAliasedJokingEnGBEndpoint() {
        given()
          .`when`().get("/jk/en_gb")
          .then()
             .statusCode(200)
             .body(containsString("This tone indicator shows that the text is a joke and not meant to be taken seriously."))
    }
    @Test
    fun testEasterEggEnGBEndpoint() {
        given()
            .`when`().get("/silly/en_gb")
            .then()
            .statusCode(200)
            .body(containsString("SLASH SILLY!!???!!!"))
    }

}