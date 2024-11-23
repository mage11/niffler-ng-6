package guru.qa.niffler.api.core;

import com.github.jknack.handlebars.Formatter;
import com.github.jknack.handlebars.internal.lang3.StringUtils;
import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;

public class CodeInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Response response = chain.proceed(chain.request());
        if (response.isRedirect()) {
            String location = Objects.requireNonNull(
                response.header("Location")
            );
            if (location.contains("code=")) {
                CodeStore.setCode(
                    StringUtils.substringAfter(
                        location, "code="
                    )
                );
            }
        }
        return response;
    }
}
