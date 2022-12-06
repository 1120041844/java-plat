package com.work.plat.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.work.plat.constants.ApiResult;
import com.work.plat.controller.base.BaseController;
import com.work.plat.entity.Course;
import com.work.plat.service.ICourseService;
import com.work.plat.service.IUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/course")
public class CourseController extends BaseController {

    @Resource
    private ICourseService courseService;

    @Resource
    private IUserService userService;

    // 新增或者更新
    @PostMapping
    public ApiResult save(@RequestBody Course course) {
        courseService.saveOrUpdate(course);
        return ApiResult.success();
    }

    @PostMapping("/studentCourse/{courseId}/{studentId}")
    public ApiResult studentCourse(@PathVariable Integer courseId, @PathVariable Integer studentId) {
        courseService.setStudentCourse(courseId, studentId);
        return ApiResult.success();
    }

    @DeleteMapping("/{id}")
    public ApiResult delete(@PathVariable Integer id) {
        courseService.removeById(id);
        return ApiResult.success();
    }

    @PostMapping("/del/batch")
    public ApiResult deleteBatch(@RequestBody List<Integer> ids) {
        courseService.removeByIds(ids);
        return ApiResult.success();
    }

    @GetMapping
    public ApiResult<List<Course>> findAll() {
        return data(courseService.list());
    }

    @GetMapping("/{id}")
    public ApiResult<Course> findOne(@PathVariable Integer id) {
        return data(courseService.getById(id));
    }

    @GetMapping("/page")
    public ApiResult<Page<Course>> findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                                @RequestParam Integer pageSize) {
//        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("id");
//        Page<Course> page = courseService.page(new Page<>(pageNum, pageSize), queryWrapper);
//        List<Course> records = page.getRecords();
//        for (Course record : records) {
//            User user = userService.getById(record.getTeacherId());
//            if(user != null) {
//                record.setTeacher(user.getNickname());
//            }
//
//        }
        Page<Course> page = courseService.findPage(new Page<>(pageNum, pageSize), name);
        return data(page);
    }

}

