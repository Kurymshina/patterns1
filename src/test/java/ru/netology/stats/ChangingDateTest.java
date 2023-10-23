package ru.netology.stats;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.commands.ShouldBe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.util.Random;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.stats.DataGenerator.faker;
import static ru.netology.stats.DataGenerator.generateCity;

public class ChangingDateTest {
    DataGenerator dataGenerator;
    String city = DataGenerator.generateCity();
    String phone = faker.phoneNumber().phoneNumber();
    String name = DataGenerator.generateName();
    String firstTime = DataGenerator.generateDate(3, "dd.MM.yyyy");
    String secondTime = DataGenerator.generateDate(7, "dd.MM.yyyy");

    @BeforeEach
    void setup() {
        open("http://localhost:9999/");
    }

    @Test
    public void shouldChangeMeetingDate() {
        $("[data-test-id=city] input").setValue(city);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
        $("[data-test-id=date] input").setValue(firstTime);
        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();
        $(".notification__title")
                .shouldHave(Condition.text("Успешно"), Duration.ofSeconds(15));
        $(".notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на  " + firstTime))
                .shouldBe(visible);
        $(".calendar-input__custom-control input").doubleClick().sendKeys(secondTime);
        $(".button").click();
        $("[data-test-id='replan-notification'] .notification__title")
                .shouldHave(exactText("Необходимо подтверждение"));
        $("[data-test-id='replan-notification'] button").click();
        $("[data-test-id=success-notification] .notification__title")
                .shouldBe(exactText("Успешно!"), Duration.ofSeconds(40));
        $(".notification__content")
                .shouldBe(exactText("Встреча успешно запланирована на  " + secondTime))
                .shouldBe(visible);


    }
}
