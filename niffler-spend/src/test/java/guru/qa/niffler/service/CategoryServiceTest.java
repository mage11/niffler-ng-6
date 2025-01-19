package guru.qa.niffler.service;

import guru.qa.niffler.data.CategoryEntity;
import guru.qa.niffler.data.repository.CategoryRepository;
import guru.qa.niffler.ex.CategoryNotFoundException;
import guru.qa.niffler.ex.InvalidCategoryNameException;
import guru.qa.niffler.ex.TooManyCategoriesException;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Test
    void getAllCategoriesShouldFilteredArchivedCategories(@Mock CategoryRepository categoryRepository) {
        String username = "myUser";
        CategoryService categoryService = new CategoryService(categoryRepository);

        when(categoryRepository.findAllByUsernameOrderByName(eq(username)))
            .thenReturn(getMockCategories(username));

        List<CategoryJson> categoryJsonList = categoryService.getAllCategories(username, true);

        assertEquals(1, categoryJsonList.size());
        assertFalse(categoryJsonList.getFirst().archived());
        assertEquals(username, categoryJsonList.getFirst().username());
        assertEquals("name1", categoryJsonList.getFirst().name());
    }

    @Test
    void getAllCategoriesShouldNotFilteredArchivedCategories(@Mock CategoryRepository categoryRepository) {
        String username = "myUser";
        CategoryService categoryService = new CategoryService(categoryRepository);

        when(categoryRepository.findAllByUsernameOrderByName(eq(username)))
            .thenReturn(getMockCategories(username));

        List<CategoryJson> categoryJsonList = categoryService.getAllCategories(username, false);

        assertEquals(2, categoryJsonList.size());
        assertFalse(categoryJsonList.getFirst().archived());
        assertEquals(username, categoryJsonList.getFirst().username());
        assertEquals("name1", categoryJsonList.getFirst().name());
        assertTrue(categoryJsonList.getLast().archived());
        assertEquals(username, categoryJsonList.getLast().username());
        assertEquals("name2", categoryJsonList.getLast().name());
    }

    @Test
    void categoryShouldBeUpdated(@Mock CategoryRepository categoryRepository) {
        String username = "myUser";
        CategoryEntity category = getMockCategories(username).getFirst();

        when(categoryRepository.findByUsernameAndId(eq(username), eq(category.getId())))
            .thenReturn(Optional.of(category));
        when(categoryRepository.save(any(CategoryEntity.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        CategoryService categoryService = new CategoryService(categoryRepository);

        CategoryJson categoryJson = new CategoryJson(
            category.getId(),
            "newName",
            category.getUsername(),
            true
        );

        CategoryJson updatedCategory = categoryService.update(categoryJson);

        assertEquals(categoryJson.id(), updatedCategory.id());
        assertEquals(categoryJson.name(), updatedCategory.name());
        assertEquals(categoryJson.username(), updatedCategory.username());
        assertEquals(categoryJson.archived(), updatedCategory.archived());
    }

    @Test
    void shouldBeErrorWhenTooManyCategoriesExistWhileUpdate(@Mock CategoryRepository categoryRepository) {
        String username = "myUser";
        CategoryEntity category = getMockCategories(username).getLast();

        when(categoryRepository.findByUsernameAndId(eq(username), eq(category.getId())))
            .thenReturn(Optional.of(category));
        when(categoryRepository.countByUsernameAndArchived(eq(username), eq(false)))
            .thenReturn(1000L);

        CategoryJson categoryJson = new CategoryJson(
            category.getId(),
            "newName",
            category.getUsername(),
            false
        );

        CategoryService categoryService = new CategoryService(categoryRepository);

        TooManyCategoriesException tooManyCategoriesException =
            assertThrows(TooManyCategoriesException.class, () -> categoryService.update(categoryJson));

        assertEquals("Can`t unarchive category for user: '" + username + "'",
            tooManyCategoriesException.getMessage());
    }

    @Test
    void shouldBeErrorWhenTooManyCategoriesExistWhileSave(@Mock CategoryRepository categoryRepository) {
        String username = "myUser";

        when(categoryRepository.countByUsernameAndArchived(eq(username), eq(false)))
            .thenReturn(1000L);

        CategoryService categoryService = new CategoryService(categoryRepository);

        CategoryJson categoryJson = new CategoryJson(
            UUID.randomUUID(),
            "newName",
            username,
            false
        );

        TooManyCategoriesException tooManyCategoriesException =
            assertThrows(TooManyCategoriesException.class, () -> categoryService.save(categoryJson));

        assertEquals("Can`t add over than 8 categories for user: '" + username + "'",
            tooManyCategoriesException.getMessage());
    }

    @Test
    void categoryShouldBeSaved(@Mock CategoryRepository categoryRepository) {
        String username = "myUser";
        CategoryEntity category = getMockCategories(username).getLast();

        when(categoryRepository.save(any(CategoryEntity.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        CategoryService categoryService = new CategoryService(categoryRepository);

        CategoryJson categoryJson = CategoryJson.fromEntity(category);

        CategoryEntity savedCategory = categoryService.save(categoryJson);

        assertEquals(category.getName(), savedCategory.getName());
        assertEquals(category.getUsername(), savedCategory.getUsername());
        assertFalse(savedCategory.isArchived());
    }

    @Test
    void categoryNotFoundExceptionShouldBeThrown(@Mock CategoryRepository categoryRepository) {
        final String username = "not_found";
        final UUID id = UUID.randomUUID();

        when(categoryRepository.findByUsernameAndId(eq(username), eq(id)))
            .thenReturn(Optional.empty());

        CategoryService categoryService = new CategoryService(categoryRepository);

        CategoryJson categoryJson = new CategoryJson(
            id,
            "",
            username,
            true
        );

        CategoryNotFoundException ex = assertThrows(
            CategoryNotFoundException.class,
            () -> categoryService.update(categoryJson)
        );
        Assertions.assertEquals(
            "Can`t find category by id: '" + id + "'",
            ex.getMessage()
        );
    }

    @ValueSource(strings = {"Archived", "ARCHIVED", "ArchIved"})
    @ParameterizedTest
    void categoryNameArchivedShouldBeDenied(String catName, @Mock CategoryRepository categoryRepository) {
        final String username = "duck";
        final UUID id = UUID.randomUUID();
        final CategoryEntity cat = new CategoryEntity();

        when(categoryRepository.findByUsernameAndId(eq(username), eq(id)))
            .thenReturn(Optional.of(
                cat
            ));

        CategoryService categoryService = new CategoryService(categoryRepository);

        CategoryJson categoryJson = new CategoryJson(
            id,
            catName,
            username,
            true
        );

        InvalidCategoryNameException ex = assertThrows(
            InvalidCategoryNameException.class,
            () -> categoryService.update(categoryJson)
        );
        Assertions.assertEquals(
            "Can`t add category with name: '" + catName + "'",
            ex.getMessage()
        );
    }

    @Test
    void onlyTwoFieldsShouldBeUpdated(@Mock CategoryRepository categoryRepository) {
        final String username = "duck";
        final UUID id = UUID.randomUUID();
        final CategoryEntity cat = new CategoryEntity();
        cat.setId(id);
        cat.setUsername(username);
        cat.setName("Магазины");
        cat.setArchived(false);

        when(categoryRepository.findByUsernameAndId(eq(username), eq(id)))
            .thenReturn(Optional.of(
                cat
            ));
        when(categoryRepository.save(any(CategoryEntity.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        CategoryService categoryService = new CategoryService(categoryRepository);

        CategoryJson categoryJson = new CategoryJson(
            id,
            "Бары",
            username,
            true
        );

        categoryService.update(categoryJson);
        ArgumentCaptor<CategoryEntity> argumentCaptor = ArgumentCaptor.forClass(CategoryEntity.class);
        verify(categoryRepository).save(argumentCaptor.capture());
        assertEquals("Бары", argumentCaptor.getValue().getName());
        assertEquals("duck", argumentCaptor.getValue().getUsername());
        assertTrue(argumentCaptor.getValue().isArchived());
        assertEquals(id, argumentCaptor.getValue().getId());
    }

    private List<CategoryEntity> getMockCategories(String username) {
        CategoryEntity categoryEntity1 = new CategoryEntity();
        categoryEntity1.setId(UUID.randomUUID());
        categoryEntity1.setName("name1");
        categoryEntity1.setUsername(username);
        categoryEntity1.setArchived(false);

        CategoryEntity categoryEntity2 = new CategoryEntity();
        categoryEntity2.setId(UUID.randomUUID());
        categoryEntity2.setName("name2");
        categoryEntity2.setUsername(username);
        categoryEntity2.setArchived(true);

        return List.of(categoryEntity1, categoryEntity2);
    }
}
