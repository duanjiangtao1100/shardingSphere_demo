前言：
使用 shardingSphere shardingjdbc 练习 demo ,有详细注释,新学者,水平分库分表,垂直分库分表, 分表查询 读写分离 
1、应用
（1）在数据库设计时候考虑垂直分库和垂直分表
（2）随着数据库数据量增加，不要马上考虑做水平切分，首先考虑缓存处理，读写分离，使用索引等等方式，如果这些方式不能根本解决问题了，再考虑做水平分库和水平分表
2、分库分表问题
（1）跨节点连接查询问题（分页、排序）
（2）多数据源管理问题
1、分库分表有两种方式：垂直切分和水平切分
2、垂直切分：垂直分表和垂直分库：根据业务拆分表结构为多张表(垂直分表);根据业务拆分表进不同库（垂直分库）
3、水平切分：水平分表和水平分库：大数据量表数据一分为N（水平分表）;大数据量库一份为N（水平分库）
#首先引入需要的相关依赖：
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.20</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.shardingsphere</groupId>
            <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
            <version>4.0.0-RC1</version>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.0.5</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
    </dependencies>
    
 ###################################################################################################   
一：以下为具体操作：
具体实现了：水平分表、水平分表分库、垂直分库、垂直分表、公共表操作

##水平分表
#水平分表===========================================================
#配置 Sharding-JDBC 分片策略
#配置数据源,并给数据源起名字
spring.shardingsphere.datasource.names=m1
# 一个实体对应两个表
spring.main.allow-bean-definition-overriding=true
#配置数据源具体内容，包含连接池，驱动，地址，用户名和密码
spring.shardingsphere.datasource.m1.type=com.alibaba.druid.pool.DruidDataSource
spring.shardingsphere.datasource.m1.driver-class-name=com.mysql.cj.jdbc.Driver
spring.shardingsphere.datasource.m1.url=jdbc:mysql://localhost:3306/course_db?serverTimezone=GMT%2B8
spring.shardingsphere.datasource.m1.username=root
spring.shardingsphere.datasource.m1.password=root
 #指定 course 表分布情况，配置表在哪个数据库里面，表名称都是什么 m1.course_1 ,m1.course_2
spring.shardingsphere.sharding.tables.course.actual-data-nodes=m1.course_$->{1..2}
# 指定course表里面主键cid 生成策略  SNOWFLAKE
spring.shardingsphere.sharding.tables.course.key-generator.column=cid
spring.shardingsphere.sharding.tables.course.key-generator.type=SNOWFLAKE
# 指定分片策略 约定 cid
spring.shardingsphere.sharding.tables.course.table-strategy.inline.sharding-column=cid
#值偶数添加到 course_1 表，如果 cid 是奇数添加到 course_2表
spring.shardingsphere.sharding.tables.course.table-strategy.inline.algorithm-expression=course_$->{cid % 2 + 1}
# 打开 sql 输出日志
spring.shardingsphere.props.sql.show=true

#步骤二：
@Data
public class Course {
    private Long cid;
    private String cname;
    private Long userId;
    private String cstatus;
}


#步骤三：
@Repository
public interface CourseMapper extends BaseMapper<Course> {

}


#步骤四：
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









