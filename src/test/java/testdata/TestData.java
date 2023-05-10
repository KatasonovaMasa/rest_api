package testdata;

import com.github.javafaker.Faker;

import java.util.Random;

public class TestData {

    static Faker faker = new Faker();

    public final static String
       //     testCaseName = faker.name(),
            testCaseName = ("Case " + faker.random().hex(8)),
            nameStepTestCaseOne = ("Step One " + faker.random().hex(8)),
            nameStepTestCaseTwo = ("Step Two " + faker.random().hex(8));

    public final static String
            jsonStringCreateTestCaseRequest = String.format("{\"steps\":[{\"name\":\"%s\",\"spacing\":\"\"}, " +
            "{\"name\":\"%s\",\"spacing\":\"\"}] }", nameStepTestCaseOne, nameStepTestCaseTwo);

    public final static String jsonStringEditingRequest = String.format("{\"steps\":[{\"name\":\"%s\",\"attachments\":[],\"steps\":[]," +
            "\"leaf\":true,\"stepsCount\":0,\"hasContent\":false,\"spacing\":\"\"}," + "{\"name\":\"%s\",\"attachments\":[]," +
            "\"steps\":[],\"leaf\":true,\"stepsCount\":0," +
            "\"hasContent\":false,\"spacing\":\"\"}],\"workPath\":[1]}", nameStepTestCaseTwo, nameStepTestCaseOne);
}
