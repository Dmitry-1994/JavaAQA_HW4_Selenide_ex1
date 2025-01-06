import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class CardPositiveTest {

    private String getData(int countDay, String format) {
        return LocalDate.now().plusDays(countDay).format(DateTimeFormatter.ofPattern(format));
    }

    @ParameterizedTest
    @CsvSource({
            "Екатеринбург, 3, Дмитрий, +79122518775",
            "Москва, 4, Дмитрий Тарасов, +12345678910",
            "Санкт-Петербург, 30, Дмитрий-Тарасов, +00000000000",
            "Ростов-на-Дону, 365, Дмитрий-Тарасов Алексеевич, +99999999999"
    })
    void shouldRegisterAccount(String city, int correctDate, String name, String phone) {
        open("http://localhost:9999/");
        $("[data-test-id=city] input").setValue(city);
        $("[data-test-id='date'] input").doubleClick().sendKeys("DELETE");
        String setData = getData(correctDate, "dd.MM.yyyy");
        $("[data-test-id='date'] input").setValue(setData);
        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id='agreement']").click();
        $(".button__text").click();
        $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=notification] .notification__title").shouldHave(text("Успешно!"));
        $("[data-test-id=notification] .notification__content").shouldHave(text("Встреча успешно забронирована на " + setData));
    }

}
