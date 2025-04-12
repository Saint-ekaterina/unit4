
import com.codeborne.selenide.ElementsCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;


public class CardOrderTest {
    public String planningDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    void openBrowser() {
        open("http://localhost:9999/");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
    }

    //Задача №1
    @Test
    void testValid() {
        $("[data-test-id='city'] input").setValue("Казань");
        $("[data-test-id='date'] input").setValue(planningDate(3));
        $("[data-test-id='name'] input").setValue("Иван Петров");
        $("[data-test-id='phone'] input").setValue("+79000000000");
        $("[data-test-id='agreement']").click();
        $(byClassName ("button")).click();
        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='notification'].notification .notification__content")
                .shouldHave(text("Встреча успешно забронирована на " + planningDate(3))).shouldBe(visible);
    }

    @Test
    void testNameSpace() {
        $("[data-test-id='city'] input").setValue("Казань");
        $("[data-test-id='date'] input").setValue(planningDate(4));
        $("[data-test-id='name'] input").setValue("Иван Ван Петров");
        $("[data-test-id='phone'] input").setValue("+79000000000");
        $("[data-test-id='agreement']").click();
        $(byClassName ("button")).click();
        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='notification'].notification .notification__content")
                .shouldHave(text("Встреча успешно забронирована на " + planningDate(4))).shouldBe(visible);
    }

    @Test
    void testNameHyphen() {
        $("[data-test-id='city'] input").setValue("Казань");
        $("[data-test-id='date'] input").setValue(planningDate(3));
        $("[data-test-id='name'] input").setValue("Иван-Ван Петров");
        $("[data-test-id='phone'] input").setValue("+79000000000");
        $("[data-test-id='agreement']").click();
        $(byClassName ("button")).click();
        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='notification'].notification .notification__content")
                .shouldHave(text("Встреча успешно забронирована на " + planningDate(3))).shouldBe(visible);
    }

    @Test
    void testLastNameHyphen() {
        $("[data-test-id='city'] input").setValue("Казань");
        $("[data-test-id='date'] input").setValue(planningDate(3));
        $("[data-test-id='name'] input").setValue("Иван Петров-Старший");
        $("[data-test-id='phone'] input").setValue("+79000000000");
        $("[data-test-id='agreement']").click();
        $(byClassName ("button")).click();
        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='notification'].notification .notification__content")
                .shouldHave(text("Встреча успешно забронирована на " + planningDate(3))).shouldBe(visible);
    }

    @Test
    void testCityHyphen() {
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id='date'] input").setValue(planningDate(3));
        $("[data-test-id='name'] input").setValue("Иван Петров");
        $("[data-test-id='phone'] input").setValue("+79000000000");
        $("[data-test-id='agreement']").click();
        $(byClassName ("button")).click();
        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='notification'].notification .notification__content")
                .shouldHave(text("Встреча успешно забронирована на " + planningDate(3))).shouldBe(visible);
    }

    @Test
    void testCityNotSubject() {
        $("[data-test-id='city'] input").setValue("Чукотка");
        $("[data-test-id='date'] input").setValue(planningDate(3));
        $("[data-test-id='name'] input").setValue("Иван Петров");
        $("[data-test-id='phone'] input").setValue("+79000000000");
        $("[data-test-id='agreement']").click();
        $(byClassName ("button")).click();
        $("[data-test-id='city'].input_invalid .input__sub")
                .shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void testEmptyCity() {
        $("[data-test-id='city'] input").setValue("");
        $("[data-test-id='date'] input").setValue(planningDate(3));
        $("[data-test-id='name'] input").setValue("Иван Петров");
        $("[data-test-id='phone'] input").setValue("+79000000000");
        $("[data-test-id='agreement']").click();
        $(byClassName ("button")).click();
        $("[data-test-id='city'].input_invalid .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void testWrongDate() {
        $("[data-test-id='city'] input").setValue("Казань");
        $("[data-test-id='date'] input").setValue(planningDate(2));
        $("[data-test-id='name'] input").setValue("Иван Петров");
        $("[data-test-id='phone'] input").setValue("+79000000000");
        $("[data-test-id='agreement']").click();
        $(byClassName ("button")).click();
        $("[data-test-id='date'].calendar-input .input__sub")
                .shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void testEmptyDate() {
        $("[data-test-id='city'] input").setValue("Казань");
        $("[data-test-id='date'] input").setValue("");
        $("[data-test-id='name'] input").setValue("Иван Петров");
        $("[data-test-id='phone'] input").setValue("+79000000000");
        $("[data-test-id='agreement']").click();
        $(byClassName ("button")).click();
        $("[data-test-id='date'].calendar-input .input__sub").shouldHave(exactText("Неверно введена дата"));
    }

    @Test
    void testLatinName() {
        $("[data-test-id='city'] input").setValue("Казань");
        $("[data-test-id='date'] input").setValue(planningDate(3));
        $("[data-test-id='name'] input").setValue("Ivan Petrov");
        $("[data-test-id='phone'] input").setValue("+79000000000");
        $("[data-test-id='agreement']").click();
        $(byClassName ("button")).click();
        $("[data-test-id='name'].input_invalid .input__sub")
                .shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void testEmptyName() {
        $("[data-test-id='city'] input").setValue("Казань");
        $("[data-test-id='date'] input").setValue(planningDate(3));
        $("[data-test-id='name'] input").setValue("");
        $("[data-test-id='phone'] input").setValue("+79000000000");
        $("[data-test-id='agreement']").click();
        $(byClassName ("button")).click();
        $("[data-test-id='name'].input_invalid .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void testPhone10() {
        $("[data-test-id='city'] input").setValue("Казань");
        $("[data-test-id='date'] input").setValue(planningDate(3));
        $("[data-test-id='name'] input").setValue("Иван Петров");
        $("[data-test-id='phone'] input").setValue("+7900000000");
        $("[data-test-id='agreement']").click();
        $(byClassName ("button")).click();
        $("[data-test-id='phone'].input_invalid .input__sub")
                .shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void testPhone12() {
        $("[data-test-id='city'] input").setValue("Казань");
        $("[data-test-id='date'] input").setValue(planningDate(3));
        $("[data-test-id='name'] input").setValue("Иван Петров");
        $("[data-test-id='phone'] input").setValue("+790000000000");
        $("[data-test-id='agreement']").click();
        $(byClassName ("button")).click();
        $("[data-test-id='phone'].input_invalid .input__sub")
                .shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void testEmptyPhone() {
        $("[data-test-id='city'] input").setValue("Казань");
        $("[data-test-id='date'] input").setValue(planningDate(3));
        $("[data-test-id='name'] input").setValue("Иван Петров");
        $("[data-test-id='phone'] input").setValue("");
        $("[data-test-id='agreement']").click();
        $(byClassName ("button")).click();
        $("[data-test-id='phone'].input_invalid .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void testWithoutCheckbox() {
        $("[data-test-id='city'] input").setValue("Казань");
        $("[data-test-id='date'] input").setValue(planningDate(3));
        $("[data-test-id='name'] input").setValue("Иван Петров");
        $("[data-test-id='phone'] input").setValue("+79000000000");
        $(byClassName ("button")).click();
        $("[data-test-id=agreement].input_invalid .checkbox__text")
                .shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

    //Задача №2
    @Test
    void testCityList() {
        $("[data-test-id='city'] input").setValue("Пе");
        ElementsCollection CityList = $$("[class='popup__container'] span");
        CityList.findBy(text("Пермь")).click();
        $("[data-test-id='date'] input").setValue(planningDate(3));
        $("[data-test-id='name'] input").setValue("Иван Петров");
        $("[data-test-id='phone'] input").setValue("+79000000000");
        $("[data-test-id='agreement']").click();
        $(byClassName ("button")).click();
        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='notification'].notification .notification__content")
                .shouldHave(text("Встреча успешно забронирована на " + planningDate(3))).shouldBe(visible);
    }

    @Test
    void testCalendar() {
        $("[data-test-id='city'] input").setValue("Казань");
        $("[data-test-id='date'] input").click();
        ElementsCollection date = $$("[class='popup__container'] [data-day]");
        int days = 14 - 3;
        int daysNextMonth;
        int currentWeek = date.size();
        if (currentWeek < days) {
            daysNextMonth = days - currentWeek;
            $("[class='popup__container'] [data-step='1']").click();
            date.get(daysNextMonth).click();
        } else {
            date.get(days).click();
        }
        $("[data-test-id='name'] input").setValue("Иван Петров");
        $("[data-test-id='phone'] input").setValue("+79000000000");
        $("[data-test-id='agreement']").click();
        $(byClassName ("button")).click();
        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='notification'].notification .notification__content")
                .shouldHave(text("Встреча успешно забронирована на " + planningDate(days + 3))).shouldBe(visible);
    }
}
