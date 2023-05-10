package tests;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import tests.TestBase;

import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;
import static io.restassured.http.ContentType.JSON;
import static config.AuthConfig.token;
import static helpers.CustomApiListener.withCustomTemplates;

public class Spec {
    public static RequestSpecification requestSpec =
            with()
                    .header("Authorization", "Api-Token " + token)
                    .cookie("ALLURE_TESTOPS_SESSION", TestBase.allureTestOpsSession)
                    .baseUri("https://allure.autotests.cloud")
                    .basePath("/api/rs")
                    .log().all()
                    .filter(withCustomTemplates())
                    .contentType(JSON);


    public static ResponseSpecification responseSpec = new ResponseSpecBuilder().
            log(STATUS).
            log(BODY).
            build();
}
