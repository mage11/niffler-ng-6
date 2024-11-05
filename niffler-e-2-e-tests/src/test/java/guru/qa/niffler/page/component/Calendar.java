package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class Calendar {

    private final SelenideElement calendar = $("input[name='date']");

    @Step("Установить дату {date}")
    public Calendar setDateInCalendar(String date){
        calendar.setValue(date);
        return this;
    }

}
