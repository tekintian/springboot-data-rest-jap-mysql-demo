# 基于Spring boot, Spring data rest + jpa + MySQL数据库 REST演示

JPA+MYSQL8 数据库连接配置

~~~properties
# 服务器访问端口
server.port=8003
# 数据库基本配置 
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/testdb?useUnicode=true&characterEncoding=utf-8&useSSL=false\
  &serverTimezone=Asia/Shanghai
spring.datasource.username=test
spring.datasource.password=test888
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.database=MYSQL
# 显示后台处理的SQL语句
spring.jpa.show-sql=true
# 自动检查实体和数据库表是否一致，如果不一致则会进行更新数据库表
spring.jpa.hibernate.ddl-auto=update
~~~


---
- 使用 spring.jpa.properties.* 属性定义Hibernate 原生配置属性
Use spring.jpa.properties.* for Hibernate native properties (the prefix is  stripped before adding them to the entity
 manager)
- 配置JPA的Dialet规则为自定义的类
 自定义Dialet类
 ~~~java
 package cn.tekin.jpa.dialect;
 
 import org.hibernate.dialect.MySQLDialect;
 
 /**
  * 通过重写 getTableTypeString 自定义Mysql数据库的表生成类型
  * JPA默认的数据库编码为 MyISAM引擎   latin1编码
  */
 public class Mysql5InnodbUtf8 extends MySQLDialect {
     @Override
     public String getTableTypeString() {
         return " ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
     }
 }
 ~~~

配置：
~~~properties
# Use spring.jpa.properties.* for Hibernate native properties (the prefix is
# stripped before adding them to the entity manager)

# The SQL dialect makes Hibernate generate better SQL for the chosen database
spring.jpa.properties.hibernate.dialect = cn.tekin.jpa.dialect.Mysql5InnodbUtf8
~~~

- lombok不能用于data-rest的类里面！ 无法获取数据
// 用于自动set,get等的减少代码量的工具 lombok 不能用于 spring data rest[无法获取数据]
//    compileOnly group: 'org.projectlombok', name: 'lombok', version: '1.18.4'



## PagingAndSortingRepository

AppleFramework在数据访问控制层采用了Spring Data作为这一层的解决方案，

下面就对Spring Data相关知识作一个较为详细的描述。

1.Spring Data所解决的问题

Spring Data :提供了一整套数据访问层(DAO)的解决方案，

致力于减少数据访问层(DAO)的开发量。

它使用一个叫作Repository的接口类为基础，

它被定义为访问底层数据模型的超级接口。

而对于某种具体的数据访问操作，则在其子接口中定义。

public interface Repository

Serializable> {

}

所有继承这个接口的interface都被spring所管理，此接口作为标识接口，功能就是用来控制domain模型的。

Spring Data可以让我们只定义接口，只要遵循spring data的规范，就无需写实现类。

2.什么是Repository？

2.1 Repository（资源库）：通过用来访问领域对象的一个类似集合的接口，

在领域与数据映射层之间进行协调。这个叫法就类似于我们通常所说的DAO，

在这里，我们就按照这一习惯把数据访问层叫Repository

Spring Data给我们提供几个Repository，基础的Repository提供了最基本的数据访问功能，

其几个子接口则扩展了一些功能。它们的继承关系如下：

Repository： 仅仅是一个标识，表明任何继承它的均为仓库接口类，方便Spring自动扫描识别

CrudRepository： 继承Repository，实现了一组CRUD相关的方法

PagingAndSortingRepository：继承CrudRepository，实现了一组分页排序相关的方法

JpaRepository：继承PagingAndSortingRepository，实现一组JPA规范相关的方法

JpaSpecificationExecutor： 比较特殊，不属于Repository体系，实现一组JPACriteria查询相关的方法

我们自己定义的XxxxRepository需要继承JpaRepository，

这样我们的XxxxRepository接口就具备了通用的数据访问控制层的能力。

2.2 JpaRepository 所提供的基本功能

2.2.1 CrudRepository

Serializable>：

这个接口提供了最基本的对实体类的添删改查操作

T save(T entity);//保存单个实体

Iterablesave(Iterableentities);//保存集合

T findOne(ID id);//根据id查找实体

boolean exists(ID id);//根据id判断实体是否存在

IterablefindAll();//查询所有实体,不用或慎用!

long count();//查询实体数量

void delete(ID id);//根据Id删除实体

void delete(T entity);//删除一个实体

void delete(Iterable entities);//删除一个实体的集合

void deleteAll();//删除所有实体,不用或慎用!

2.2.2 PagingAndSortingRepository

这个接口提供了分页与排序功能

Iterable findAll(Sort

sort);//排序

PagefindAll(Pageable pageable);//分页查询（含排序功能）

2.2.3 JpaRepository

Serializable>

这个接口提供了JPA的相关功能

List findAll();//查找所有实体

ListfindAll(Sort sort);//排序 查找所有实体

Listsave(Iterableentities);//保存集合

void flush();//执行缓存与数据库同步

T saveAndFlush(T entity);//强制执行持久化

void deleteInBatch(Iterable

entities);//删除一个实体集合

3.Spring data 查询

3.1 简单条件查询:查询某一个实体类或者集合

按照Spring data 定义的规则，查询方法以find|read|get开头

涉及条件查询时，条件的属性用条件关键字连接，要注意的是：条件属性以首字母大写其余字母小写为规定。

例如：定义一个Entity实体类

class User｛

private String firstname;

private String lastname;

｝

使用And条件连接时，应这样写：

findByLastnameAndFirstname(String lastname,Stringfirstname);

条件的属性名称与个数要与参数的位置与个数一一对应

其他条件关键字如下表的定义：

Table 3.1. Supported keywords inside method names

3.2 使用JPA NamedQueries （标准规范实现）

这种查询是标准的JPA规范所定义的，直接声明在Entity实体类上，

调用时采用在接口中定义与命名查询对应的method，由Spring Data根据方法名自动完成命名查询的寻找。

（1）在Entity实体类上使用@NamedQuery注解直接声明命名查询。

@Entity

@NamedQuery(name ="User.findByEmailAddress",

query = "select u from User u whereu.emailAddress = ?1")

public class User {

}

注：定义多个时使用下面的注解

@NamedQueries(value = {

@NamedQuery(name =User.QUERY_FIND_BY_LOGIN,

query = "select u from User uwhere u." + User.PROP_LOGIN

+ " =:username"),

@NamedQuery(name ="getUsernamePasswordToken",

query ="select new com.aceona.weibo.vo.TokenBO(u.username,u.password) fromUser u where u." + User.PROP_LOGIN

+ " = :username")})

（2）在interface中定义与(1)对应的方法

public interface UserRepository extends

JpaRepository {

ListfindByLastname(String lastname);

User findByEmailAddress(StringemailAddress);

}

3.3 使用@Query自定义查询（Spring Data提供的）

这种查询可以声明在Repository方法中，摆脱像命名查询那样的约束，

将查询直接在相应的接口方法中声明，结构更为清晰，这是Spring data的特有实现。

例如：

public interface UserRepository extends

JpaRepository {

@Query("select u from User u whereu.emailAddress = ?1")

User findByEmailAddress(StringemailAddress);

}

3.4 @Query与 @Modifying 执行更新操作

这两个annotation一起声明，可定义个性化更新操作，例如只涉及某些字段更新时最为常用，示例如下：

@Modifying

@Query("update User u set u.firstname = ?1 where u.lastname =

?2")

int setFixedFirstnameFor(String firstname, String

lastname);

3.5 索引参数与命名参数

（1）索引参数如下所示，索引值从1开始，查询中 ”?X” 个数需要与方法定义的参数个数相一致，并且顺序也要一致

@Modifying

@Query("update User u set u.firstname = ?1 where u.lastname =

?2")

int setFixedFirstnameFor(String firstname, String

lastname);

（2）命名参数（推荐使用这种方式）

可以定义好参数名，赋值时采用@Param("参数名")，而不用管顺序。如下所示：

public interface UserRepository extends

JpaRepository {

@Query("select u from User u whereu.firstname = :firstname or u.lastname = :lastname")

UserfindByLastnameOrFirstname(@Param("lastname") String lastname,

@Param("firstname") Stringfirstname);

}

4. Transactionality（事务）

4.1 操作单个对象的事务

Spring

Data提供了默认的事务处理方式，即所有的查询均声明为只读事务，对于持久化，更新与删除对象声明为有事务。

参见org.springframework.data.jpa.repository.support.SimpleJpaRepository

ID>

@org.springframework.stereotype.Repository

@Transactional(readOnly = true)

public class SimpleJpaRepository

Serializable> implements

JpaRepository,

JpaSpecificationExecutor {

……

@Transactional

public void delete(ID id) {

delete(findOne(id));

}

……

}

对于自定义的方法，如需改变spring

data提供的事务默认方式，可以在方法上注解@Transactional声明

4.2 涉及多个Repository的事务处理

进行多个Repository操作时，也应该使它们在同一个事务中处理，按照分层架构的思想，这部分属于业务逻辑层，因此，需要在Service层实现对多个Repository的调用，并在相应的方法上声明事务。

例如：

@Service(“userManagement”)

class UserManagementImpl implements UserManagement {

private final UserRepositoryuserRepository;

private final RoleRepositoryroleRepository;

@Autowired

public UserManagementImpl(UserRepositoryuserRepository,

RoleRepositoryroleRepository) {

this.userRepository =userRepository;

this.roleRepository =roleRepository;

}

@Transactional

public void addRoleToAllUsers(StringroleName) {

Role role =roleRepository.findByName(roleName);

for (User user :userRepository.readAll()) {

user.addRole(role);

userRepository.save(user);

}

}

5.关于DAO层的规范

5.1对于不需要写实现类的情况：

定义XxxxRepository 接口并继承JpaRepository接口，

如果Spring data所提供的默认接口方法不够用，可以使用@Query在其中定义个性化的接口方法。

5.2对于需要写实现类的情况：

定义XxxxDao

接口并继承com.aceona.appleframework.persistent.data.GenericDao

书写XxxxDaoImpl实现类并继承com.aceona.appleframework.persistent.data.GenericJpaDao，

同时实现XxxxDao接口中的方法

在Service层调用XxxxRepository接口与XxxxDao接口完成相应的业务逻辑