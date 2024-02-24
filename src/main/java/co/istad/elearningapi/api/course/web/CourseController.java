package co.istad.elearningapi.api.course.web;

import co.istad.elearningapi.api.course.Course;
import co.istad.elearningapi.api.course.CourseMapper;
import co.istad.elearningapi.api.course.CourseRepository;
import co.istad.elearningapi.api.course.CourseService;
import co.istad.elearningapi.base.BaseSuccess;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/{id}")
    BaseSuccess<?> findById(@PathVariable Long id) {
        return BaseSuccess.builder()
                .code(HttpStatus.OK.value())
                .status(true)
                .timestamp(LocalDateTime.now())
                .message("Course has been found successfully")
                .data(courseService.findById(id))
                .build();
    }

    @PreAuthorize("hasAuthority('CSTAD_elearning:write')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    void createNew(@Valid @RequestBody CourseCreationDto creationDto) {
        courseService.createNew(creationDto);
    }

    @GetMapping
    Page<CourseDto> findList(@RequestParam(required = false, defaultValue = "0") int pageNumber,
                             @RequestParam(required = false, defaultValue = "4") int pageSize) {
        return courseService.findList(pageNumber, pageSize);
    }

}
