package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.model.rest.SpendJson;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;

@ParametersAreNonnullByDefault
public class SpendConditions {

    @Nonnull
    public static WebElementsCondition spends(@Nonnull SpendJson... expectedSpends){
        final int categoryPos = 1;
        final int amountPos = 2;
        final int descPos = 3;
        final int datePos = 4;
        final DecimalFormat decimalFormat = new DecimalFormat("#.##");
        final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        List<String> actualRow = new ArrayList<>();
        List<String> expectedRow = new ArrayList<>();
        return new WebElementsCondition(){
            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements){
                if (ArrayUtils.isEmpty(expectedSpends)) {
                    throw new IllegalArgumentException("No expected spends given");
                }
                if (expectedSpends.length != elements.size()) {
                    final String message = String.format("List size mismatch (expected: %s, actual: %s)",
                        expectedSpends.length, elements.size());
                    return rejected(message, elements);
                }

                for(int i=0; i < elements.size(); i++){
                    WebElement row = elements.get(i);
                    SpendJson expectedSpend = expectedSpends[i];
                    List<WebElement> columns = row.findElements(By.cssSelector("td"));

                    String category = columns.get(categoryPos).getText();
                    String amount = columns.get(amountPos).getText();
                    String desc = columns.get(descPos).getText();
                    String date = columns.get(datePos).getText();

                    actualRow.add(category + " " + amount + " " + desc + " " + date);

                    category = expectedSpend.category().name();
                    amount =  decimalFormat.format(expectedSpend.amount());
                    desc = expectedSpend.description();
                    date = dateFormat.format(expectedSpend.spendDate());

                    expectedRow.add(category + " " + amount + " ₽" + " " + desc + " " + date);
                }

                if(actualRow.containsAll(expectedRow)){
                    return accepted();
                } else {
                    final String actual = actualRow.toString();
                    final String message = String.format(
                        "List mismatch (expected: %s, actual: %s)", expectedRow, actual
                    );
                    return rejected(message, actual);
                }
            }
            @Override
            public String toString() {
                return expectedRow.toString();
            }
        };
    }

}
