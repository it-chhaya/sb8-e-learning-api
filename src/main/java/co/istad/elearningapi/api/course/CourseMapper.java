package co.istad.elearningapi.api.course;

import co.istad.elearningapi.api.course.web.CourseCreationDto;
import co.istad.elearningapi.api.course.web.CourseDto;
import co.istad.elearningapi.api.course.web.CourseEditionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    // Map Course List to Course Dto List
    // Source: Course List
    // Target: Course Dto List
    List<CourseDto> mapCourseListToCourseDtoList(List<Course> courses);

    CourseDto mapCourseToCourseDto(Course course);

    @Mapping(source = "categoryId", target = "category.id")
    Course mapCreationDtoToCourse(CourseCreationDto creationDto);

    void mapEditionDtoToCourse(CourseEditionDto courseEditionDto,
                               @MappingTarget Course course);

}
