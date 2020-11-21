package com.zt.djt_shardingsphere;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zt.djt_shardingsphere.entity.Course;
import com.zt.djt_shardingsphere.entity.Udict;
import com.zt.djt_shardingsphere.entity.User;
import com.zt.djt_shardingsphere.mapper.CourseMapper;
import com.zt.djt_shardingsphere.mapper.UdictMapper;
import com.zt.djt_shardingsphere.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class DjtShardingsphereApplicationTests {

    //注入 mapper
    @Autowired
    @Qualifier(value = "courseMapper")
     CourseMapper courseMapper;

    @Autowired
     UserMapper userMapper;


    @Autowired
    private UdictMapper udictMapper;
    //======================测试公共表===================
//添加操作
    @Test
    public void addDict() {
        Udict udict = new Udict();
        udict.setUstatus( "a");
        udict.setUvalue("天骄");
        udictMapper.insert(udict);
    }
    //删除操作
    @Test
    public void deleteDict() {
        QueryWrapper<Udict> wrapper = new QueryWrapper<>();
//设置 userid 值
        wrapper.eq("dictid", 1L);
        udictMapper.delete(wrapper);
    }
    //======================测试垂直分库==================
//添加操作
    @Test
    public void addUserDb() {
        User user = new User();
        user.setUsername( "lucy");
        user.setUstatus( "a");
        userMapper.insert(user);
    }

    //查询操作
    @Test
    public void addUserDb1() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        //设置 userid 值
        wrapper.eq("user_id", 537032196100194305L);
        User user = userMapper.selectOne(wrapper);
        System.out.println(user);
    }


    //水平分库分表查询操作
    @Test
    public void findCourseDb() {
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        //设置 userid 值
        wrapper.eq("user_id", 100L);
        //设置 cid 值
        wrapper.eq("cid", 537016291953737729L);
        Course course = courseMapper.selectOne(wrapper);
        System.out.println(course);

    }

    //分库分表插入
    @Test
    public void addCourseandfenbiao() {

            Course course = new Course();
            course.setCname("java");
            course.setUserId(101L);
            course.setCstatus("Normal");
            courseMapper.insert(course);

    }


    //添加课程的方法 ---水平分表插入
    @Test
    public void addCourse() {
        for(int i=1;i<=10;i++) {
            Course course = new Course();
            course.setCname("java"+i);
            course.setUserId(100L);
            course.setCstatus("Normal"+i);
            courseMapper.insert(course);
        }
    }


    //查询课程的方法  ---水平分表查询
    @Test
    public void findCourse() {
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.eq("cid",534420393998417921L);
        Course course = courseMapper.selectOne(wrapper);
        System.out.println(course);
    }

}
