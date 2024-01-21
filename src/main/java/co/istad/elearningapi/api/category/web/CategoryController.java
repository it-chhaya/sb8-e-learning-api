package co.istad.elearningapi.api.category.web;

import co.istad.elearningapi.api.category.Category;
import co.istad.elearningapi.api.category.CategoryRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping
    List<?> findList() {
        List<Category> categories = (List<Category>) categoryRepository.findAll();
        return categories.stream()
                .map(category -> Map.of("id", category.getId(),
                        "name", category.getName()))
                .toList();
    }

    @GetMapping("/{id}")
    Category findById(@PathVariable Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow();
    }

    @PreAuthorize("hasAuthority('CSTAD_elearning:delete')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void deleteById(@PathVariable Integer id) {
        categoryRepository.deleteById(id);
    }

    @PreAuthorize("hasAuthority('CSTAD_elearning:write')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    void createNew(@RequestBody @Valid CategoryCreationDto creationDto) {
        Category category = Category.builder()
                .name(creationDto.name())
                .description(creationDto.description())
                .icon(creationDto.icon())
                .build();
        categoryRepository.save(category);
    }

    @PreAuthorize("hasAuthority('CSTAD_elearning:update')")
    @PutMapping("/{id}")
    Category editById(@PathVariable Integer id,
                      @Valid @RequestBody CategoryCreationDto creationDto) {

        if (!categoryRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Category with ID = %d doesn't exist in db", id));
        }

        // Start edit category by ID
        Category category = Category.builder()
                .id(id)
                .name(creationDto.name())
                .description(creationDto.description())
                .icon(creationDto.icon())
                .build();

        return categoryRepository.save(category);
    }
}
