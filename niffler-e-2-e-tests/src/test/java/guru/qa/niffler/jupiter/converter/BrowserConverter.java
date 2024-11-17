package guru.qa.niffler.jupiter.converter;

import com.codeborne.selenide.SelenideDriver;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

public class BrowserConverter implements ArgumentConverter {

    @Override
    public SelenideDriver convert(Object source, ParameterContext context) throws ArgumentConversionException {
        Browsers browser = Browsers.valueOf(source.toString());
        return new SelenideDriver(browser.driverConfig());
    }
}
