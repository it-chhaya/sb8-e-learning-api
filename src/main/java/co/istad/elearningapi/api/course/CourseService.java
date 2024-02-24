package co.istad.elearningapi.api.course;

import co.istad.elearningapi.api.course.web.CourseCreationDto;
import co.istad.elearningapi.api.course.web.CourseDto;
import org.springframework.data.domain.Page;

public interface CourseService {

    CourseDto findById(Long id);

    void createNew(CourseCreationDto courseCreationDto);

    Page<CourseDto> findList(int pageNumber, int pageSize);

}
