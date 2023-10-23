import com.codeborne.selenide.Condition;
import data.DataGeneration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class appCardDeliveryTest {
    @BeforeEach
    void setup() {
        open("http://localhost:9999/");
    }

    @Test
    public void shouldBeSuccessfullyCompleted() {
        DataGeneration.User validUser = DataGeneration.Registration.generateUser("ru");
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        String firstDate = DataGeneration.dateGenerate(3);
        String secondDate = DataGeneration.dateGenerate(7);
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE + firstDate);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $(byText("Запланировать")).click();
        $(".notification__title")
                .shouldHave(Condition.text("Успешно"), Duration.ofSeconds(15));
        $(".notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на  " + firstDate))
                .shouldBe(visible);
        $("[data-test-id='date'] input").doubleClick().sendKeys(secondDate);
        $(".button").click();
        $("[data-test-id='replan-notification'] .notification__title")
                .shouldHave(exactText("Необходимо подтверждение"));
        $("[data-test-id='replan-notification'] button").click();
        $("[data-test-id=success-notification] .notification__title")
                .shouldBe(exactText("Успешно!"), Duration.ofSeconds(40));
        $(".notification__content")
                .shouldBe(exactText("Встреча успешно запланирована на  " + secondDate))
                .shouldBe(visible);
    }
}
