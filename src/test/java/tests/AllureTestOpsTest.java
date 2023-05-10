package tests;

import helpers.Attach;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import models.CreateTestCaseResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.html.HTMLInputElement;
import testdata.TestData;

import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static config.AuthConfig.projectId;
import static config.ConfigBrowser.openBaseUrlBrowser;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static testdata.TestData.*;
import static tests.Spec.requestSpec;
import static tests.Spec.responseSpec;


@Owner("Катасонова Мария")
@Feature("Тест кейсы в Allure TestOps")
@Epic("Автоматизация api")
@DisplayName("Тест на AllureTestOpsTest")
public class AllureTestOpsTest extends TestBase {

    @Test
    @BeforeEach
    @DisplayName("Создание тест кейса")
    public void testCreateTestCase() {

        CreateTestCaseResponse testCaseResponse = step("Создаем тест кейс", () -> given(requestSpec)
                .body(testCaseBody)
                .queryParam("projectId", projectId)
                .when()
                .post("/testcasetree/leaf")
                .then()
                .spec(responseSpec)
                .statusCode(200).extract().as(CreateTestCaseResponse.class));

        step("Проверка имени тест кейса", () -> {
            assertThat(testCaseResponse.getName()).isEqualTo(testCaseName);
        });

        step("Открываем браузер и меняем стату на активный", () -> {
            openBaseUrlBrowser();
            $x(String.format("//div[text()='%s']", testCaseName)).click();
            $x("//*[contains(@class, 'Menu__trigger')]").click();
            $x("//span[text()='Rename test case']").click();
            String name = String.valueOf($(byName("name")).setValue("name"));
            $x("//*[contains(@class, 'Modal__content')]//*[@name='submit']").click();
            withText(name);
            Attach.screenshotAs("Screenshot step");
        });

        testCaseID = testCaseResponse.getId();
    }

    @Test
    @DisplayName("Удаление созданного тест кейса")
    public void testDeleteTEstCase() {

        String jsonStringDeleteTestCaseRequest = format("{\"selection\":{\"inverted\":false,\"groupsInclude\":[]," +
                "\"groupsExclude\":[],\"leafsInclude\":[%s],\"leafsExclude\":[],\"kind\":\"TreeSelectionDto\"," +
                "\"projectId\":%s,\"path\":[]}}", testCaseID, projectId); //todo убрать в TestBase

        step("Удаление созданного тейст кейса", () -> {
            given(requestSpec)
                    .body(jsonStringDeleteTestCaseRequest)
                    .when()
                    .post("/testcase/bulk/remove")
                    .then()
                    .spec(responseSpec)
                    .statusCode(204);
        });

        step("Открываем браузер и проверяем, что тест кейc удален", () -> {
            openBaseUrlBrowser();
            String messageDelete = $("[class='Alert Alert_status_failed Alert_center']").innerText();

            step("Сообщение - тест кейс удален", () -> {
                assertThat(messageDelete).isEqualTo("Test case was deleted");
            });
            Attach.screenshotAs("Screenshot step");
        });
    }

    @Test
    @DisplayName("Добавление шагов и их редактирование")
    public void testAddingSteps() {
        step("Добавляем шаги в созданный тест кейс", () -> {
            given(requestSpec)
                    .body(jsonStringCreateTestCaseRequest)
                    .when()
                    .post(format("/testcase/%s/scenario", testCaseID))
                    .then()
                    .spec(responseSpec)
                    .statusCode(200);
        });

        step("Открываем браузер и делаем скриншот результата добавление шагов в тест кейс", () -> {
            openBaseUrlBrowser();
            Attach.screenshotAs("Screenshot step");
        });

        step("Меняем местами название шагов", () -> {
            given(requestSpec)
                    .body(jsonStringEditingRequest)
                    .when()
                    .post(format("/testcase/%s/scenario", testCaseID))
                    .then()
                    .spec(responseSpec)
                    .statusCode(200);
        });

        step("Открываем браузер и проверяем, что шаги поменялись местами", () -> {
            openBaseUrlBrowser();
            Attach.screenshotAs("Screenshot step");
        });
    }
}
