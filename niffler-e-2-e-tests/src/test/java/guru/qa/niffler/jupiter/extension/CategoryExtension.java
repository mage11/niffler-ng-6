package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.CategoriesApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final CategoriesApiClient  categoriesApiClient = new CategoriesApiClient();


    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
            .ifPresent(anno ->{

                CategoryJson category = new CategoryJson(
                    null,
                    anno.name(),
                    anno.username(),
                    false);

            CategoryJson createdCategory = categoriesApiClient.addCategory(category);

            if(anno.archived()){
                createdCategory = categoriesApiClient.updateCategory( new CategoryJson(
                        createdCategory.id(),
                        createdCategory.name(),
                        createdCategory.username(),
                        true
                    )
                );
            }

            context.getStore(NAMESPACE).put(
                context.getUniqueId(),
                createdCategory);
            });
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {

        CategoryJson category = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);

        if(category.archived()){
            CategoryJson archivedCategory = new CategoryJson(
                category.id(),
                category.name(),
                category.username(),
                true
            );
            categoriesApiClient.updateCategory(archivedCategory);

        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }

}
