package quiz_application.quiz.service;

import java.util.List;

import org.springframework.stereotype.Service;

import quiz_application.quiz.model.Category;
import quiz_application.quiz.repository.CategoryRepository;

/**
 * Category Service
 *
 * Handles all category-related business logic.
 */
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(
            CategoryRepository categoryRepository) {

        this.categoryRepository = categoryRepository;
    }

    /**
     * Get all categories.
     */
    public List<Category> getAllCategories() {

        return categoryRepository.findAll();
    }

    /**
 * Check whether category already exists.
    */
    public boolean exists(String name) {

        if (name == null || name.isBlank()) {
            return false;
        }

        return categoryRepository
                .findByNameIgnoreCase(name.trim())
                .isPresent();
    }

    /**
    * Create and save a new category.
    */
    public void addCategory(String name) {

        String normalizedName =
                normalizeCategoryName(name);

        if (exists(normalizedName)) {
            return;
        }

        Category category =
                new Category(normalizedName);

        categoryRepository.save(category);
    }

    /**
     * Normalize category names.
     *
     * Examples:
     *
     * java            -> Java
     * spring boot     -> Spring Boot
     * sql             -> SQL
     * c++             -> C++
     */
    public String normalizeCategoryName(
            String name) {

        if (name == null || name.isBlank()) {
            return "";
        }
        
        name = name.trim();

        if (name.equalsIgnoreCase("sql")) {
            return "SQL";
        }

        if (name.equalsIgnoreCase("c++")) {
            return "C++";
        }

        String[] words =
                name.toLowerCase().split("\\s+");

        StringBuilder result =
                new StringBuilder();

        for (String word : words) {

            result.append(
                    Character.toUpperCase(word.charAt(0)))
                  .append(word.substring(1))
                  .append(" ");
        }

        return result.toString().trim();
    }

    /**
    * Delete category by id.
    */
    public void deleteCategory(Long id) {

        if (!categoryRepository.existsById(id)) {
            return;
        }

        categoryRepository.deleteById(id);
    }
} 