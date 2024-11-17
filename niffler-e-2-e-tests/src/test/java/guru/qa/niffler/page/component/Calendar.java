package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.ParametersAreNonnullByDefault;


@ParametersAreNonnullByDefault
public class Calendar {

    private final SelenideElement calendar;

    public Calendar(SelenideDriver driver){
        this.calendar = driver.$("input[name='date']");
    }
    @Step("Установить дату {date}")
    public Calendar setDateInCalendar(String date) {
        calendar.setValue(date);
        return this;
    }
}
