package cn.tekin.jpa.repository;

import cn.tekin.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(path = "user")
public interface UserRepository extends JpaRepository<User,Long> {

    /**
     * 通过手机号码查找用户
     * @param mobile
     * @return
     */
    public User findUserByMobile(String mobile);

    public User findUserById(Long id);

    /**
     * 最新注册的用户ID
     * COALESCE 返回列表中第一个非空表达式
     * @return
     */
    @Query("SELECT COALESCE(max(u.id), 0) FROM user u")
    Long getMaxId();
}
