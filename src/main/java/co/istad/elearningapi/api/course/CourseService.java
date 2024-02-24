package co.istad.elearningapi.api.course;

import co.istad.elearningapi.api.course.web.CourseCreationDto;
import co.istad.elearningapi.api.course.web.CourseDto;
import co.istad.elearningapi.api.course.web.CourseEditionDto;
import org.springframework.data.domain.Page;

public interface CourseService {

    void deleteById(Long id);

    void editById(Long id, CourseEditionDto courseEditionDto);

    CourseDto findById(Long id);

    void createNew(CourseCreationDto courseCreationDto);

    Page<CourseDto> findList(int pageNumber, int pageSize);

}
