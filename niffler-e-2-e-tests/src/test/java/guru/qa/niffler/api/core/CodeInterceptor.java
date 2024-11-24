package guru.qa.niffler.api.core;

import com.github.jknack.handlebars.internal.lang3.StringUtils;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;

public class CodeInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Response response = chain.proceed(chain.request());
        if (response.isRedirect() && response.header("Location") != null) {
            String location = response.header("Location");
            if (location.contains("code=")) {
                ApiLoginExtension.setCode(
                    StringUtils.substringAfter(location, "code=")
                );
            }
        }
        return response;
    }
}
