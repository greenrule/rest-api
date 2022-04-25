package tests.demowebshop;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class DemowebshopTests {

    @BeforeAll
        static void beforeAll() {
            RestAssured.baseURI = "http://demowebshop.tricentis.com";
        }

    @Test
        //добавление товара в корзину без куки
    void addToCartAsNewUserTest() {
        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .body("product_attribute_72_5_18=53" +
                        "&product_attribute_72_6_19=54" +
                        "&product_attribute_72_3_20=57" +
                        "&addtocart_72.EnteredQuantity=1")
                .when()
                .post("/addproducttocart/details/72/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your " +
                        "<a href=\"/cart\">shopping cart</a>"))
                .body("updatetopcartsectionhtml", is("(1)"));
    }


    @Test
        //проврка добавления нескольких товаров в корзину анонимом
    void quantityAddToCartWithCookieTest() {
        Integer cartSize = 8;

        ValidatableResponse response =
                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .body("product_attribute_72_5_18=53" +
                                "&product_attribute_72_6_19=54" +
                                "&product_attribute_72_3_20=57" +
                                "&addtocart_72.EnteredQuantity=" + cartSize)
                        .when()
                        .post("/addproducttocart/details/72/1")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .body("success", is(true))
                        .body("message", is("The product has been added to your " +
                                "<a href=\"/cart\">shopping cart</a>"))
                        .body("updatetopcartsectionhtml", is("(" + cartSize +")" ));

    }

    @Test
        //добавление товара в корзину с куки
    void addToCartWithCookieTest() {
        String authCookie = "Nop.customer=b61470e0-f59f-457c-99b6-a2a25e1f999a;";
        String cartAsHtmlString = given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie(authCookie)
                .when()
                .get("/")
                .then()
                .extract().asString();

        Integer oldQuantity = Integer.parseInt(cartAsHtmlString.substring(
                cartAsHtmlString.lastIndexOf("cart-qty") + 11,
                cartAsHtmlString.indexOf("span", cartAsHtmlString.lastIndexOf("cart-qty"))-3));

        Integer expectedQuantity = oldQuantity + 1;

        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie(authCookie)
                .body("product_attribute_72_5_18=53" +
                        "&product_attribute_72_6_19=54" +
                        "&product_attribute_72_3_20=57" +
                        "&addtocart_72.EnteredQuantity=1")
                .when()
                .post("/addproducttocart/details/72/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your " +
                        "<a href=\"/cart\">shopping cart</a>"))
                .body("updatetopcartsectionhtml", is("(" + expectedQuantity +")" ));
    }

    @Test
        //проверим переход в раздел книги
    void booksPageTest() {
        String response =
                given()
                        .get("/books")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().response().asString();

        String headerTitle = "Books";
        int openTag = response.indexOf("<h1>") + 4;
        int closeTag = response.indexOf("</h1>");
        String responseTitle = response.substring(openTag, closeTag);
        Assertions.assertEquals(headerTitle, responseTitle);
    }

    /*
    HttpResponse<String> response = Unirest.get("http://demowebshop.tricentis.com/books")
  .header("Cookie", "ARRAffinity=1818b4c81d905377ced20e7ae987703a674897394db6e97dc1316168f754a687; Nop.customer=4ef7b2c9-75b9-4965-b1c8-7424b1886d22")
  .asString();
    */
}