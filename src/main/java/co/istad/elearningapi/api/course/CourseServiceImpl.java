package co.istad.elearningapi.api.course;

import co.istad.elearningapi.api.course.web.CourseCreationDto;
import co.istad.elearningapi.api.course.web.CourseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    public CourseDto findById(Long id) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Course has not been found!"));

        return courseMapper.mapCourseToCourseDto(course);
    }

    @Override
    public void createNew(CourseCreationDto courseCreationDto) {
        Course course = courseMapper.mapCreationDtoToCourse(courseCreationDto);
        course.setCreatedAt(LocalDateTime.now());
        course.setIsDelete(false);
        course.setUpdatedAt(null);
        courseRepository.save(course);
    }

    @Override
    public Page<CourseDto> findList(int pageNumber, int pageSize) {
        Sort sortById = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortById);
        Page<Course> courses = courseRepository.findAll(pageable);
        return courses.map(courseMapper::mapCourseToCourseDto);
    }
}
