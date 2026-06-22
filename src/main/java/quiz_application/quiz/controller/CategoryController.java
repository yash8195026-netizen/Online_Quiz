package quiz_application.quiz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import quiz_application.quiz.service.CategoryService;

@Controller
public class CategoryController {

    // =====================================================
    // DEPENDENCIES
    // =====================================================

    private final CategoryService categoryService;

    public CategoryController(
            CategoryService categoryService) {

        this.categoryService = categoryService;
    }

    // =====================================================
    // CATEGORY PAGE
    // =====================================================

    /**
     * Display all categories.
     */
    @GetMapping("/categories")
    public String categories(
            Model model) {

        model.addAttribute(
                "categories",
                categoryService.getAllCategories());

        return "categories";
    }

    // =====================================================
    // ADD CATEGORY
    // =====================================================

    /**
     * Add new category.
     */
    @PostMapping("/categories/add")
    public String addCategory(
            @RequestParam String name,
            RedirectAttributes redirectAttributes) {

        if (categoryService.exists(name)) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Category already exists!");

            return "redirect:/categories";
        }

        categoryService.addCategory(name);

        redirectAttributes.addFlashAttribute(
                "success",
                "Category added successfully!");

        return "redirect:/categories";
    }

    // =====================================================
    // DELETE CATEGORY
    // =====================================================

    /**
     * Delete category by id.
     */
    @PostMapping("/categories/delete/{id}")
    public String deleteCategory(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        categoryService.deleteCategory(id);

        redirectAttributes.addFlashAttribute(
                "success",
                "Category deleted successfully!");

        return "redirect:/categories";
    }
}