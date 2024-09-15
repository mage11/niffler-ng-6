package guru.qa.niffler.jupiter;

import guru.qa.niffler.api.CategoriesApiClient;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements BeforeEachCallback, AfterTestExecutionCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final CategoriesApiClient  categoriesApiClient = new CategoriesApiClient();


    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
            .ifPresent(anno ->{
                String randomName = RandomStringUtils.random(5, true, true);
                CategoryJson category = new CategoryJson(
                    null,
                    randomName,
                    anno.username(),
                    false);

            CategoryJson createdCategory = categoriesApiClient.addCategory(category);

            if(category.archived()){
               categoriesApiClient.updateCategory( new CategoryJson(
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

        if(!category.archived()){
            CategoryJson archivedCategory = new CategoryJson(
                category.id(),
                category.name(),
                category.username(),
                true
            );
            categoriesApiClient.updateCategory(archivedCategory);

        }
    }

}
