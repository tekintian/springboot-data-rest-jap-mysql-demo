package cn.tekin.jpa.entity;


import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Date;


@Entity
public class User {

    /**
     * 指定id为主键，并设置为自增长
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "[\\u4e00-\\u9fa5]{2,10}") //名字只能是中文，2-10个字符
    private String name;

    @NotEmpty
    @Column(unique = true, name = "mobile", nullable = false, length = 11)
    @Pattern(regexp = "1([3|4|5|7|8]{1})([0-9]{9})")
    private String mobile;

    @Email
    private String email;

    private String password;
    private Integer age;

    @Column(name = "created_at", nullable = false)
    @Type(type = "java.sql.Date") //可设置为自定义的类型，指定类型全路径即可
    private Date createdAt;

    /**
     * 构造函数
     */
    public User(){}
    public User(Long id){
        this.id=id;
    }
    public User(String name, String mobile, Integer age){
        this.name=name;
        this.mobile=mobile;
        this.age=age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
