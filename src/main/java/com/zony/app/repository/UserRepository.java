
package com.zony.app.repository;

import com.zony.app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Set;


public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    @Query(value = "SELECT max(user_id) FROM sys_user u", nativeQuery = true)
    long getMaxUserId();

    @Query(value = "SELECT u.* FROM sys_user u where u.user_id=?1", nativeQuery = true)
    User findByUserId(Long userId);
    /**
     * 根据用户名和jobName查询用户的部门负责人处长等
     *
     */
    @Query(value = "SELECT u.* FROM sys_user u ,sys_job j ,sys_users_jobs uj WHERE "
            + "u.dept_id = ?1 AND u.username = uj.username AND uj.job_id = j.job_id AND j.name = ?2", nativeQuery = true)
    User findUserByDeptIdAndJobName(Long deptId,String jobName);

    /**
     * 根据用户名查询
     *
     * @param username 用户名
     * @return /
     */
    User findByUsername(String username);

    /**
     * 根据用户名查询用户部门ID
     *
     * @param username 用户名
     * @return /
     */
    @Query(value = "SELECT u.dept_id FROM sys_user u WHERE "
                    + "u.username = ?1 ", nativeQuery = true)
    Long findDeptIdByUsername(String username);
    /**
     * 根据用户名查询用户上级部门ID
     *
     * @param username 用户名
     * @return /
     */
    @Query(value = "SELECT d.pid FROM sys_user u ,sys_dept d WHERE "
            + "u.username = ?1 and u.dept_id = d.dept_id", nativeQuery = true)
    Long findPDeptIdByUsername(String username);
    /**
     * 批量根据用户名查询
     *
     * @param usernames 用户名
     * @return /
     */
    List<User> findByUsernameIn(List<String> usernames);

    /**
     * 根据邮箱查询
     *
     * @param email 邮箱
     * @return /
     */
    User findByEmail(String email);

    /**
     * 修改密码
     *
     * @param username              用户名
     * @param pass                  密码
     * @param lastPasswordResetTime /
     */
    @Modifying
    @Query(value = "update sys_user set password = ?2 , pwd_reset_time = ?3 where username = ?1",
            nativeQuery = true)
    void updatePass(String username, String pass, Date lastPasswordResetTime);

    /**
     * 修改邮箱
     *
     * @param username 用户名
     * @param email    邮箱
     */
    @Modifying
    @Query(value = "update sys_user set email = ?2 where username = ?1", nativeQuery = true)
    void updateEmail(String username, String email);

    /**
     * 根据角色查询用户
     *
     * @param roleId /
     * @return /
     */
    @Query(value = "SELECT u.* FROM sys_user u, sys_users_roles r WHERE"
            + " u.username = r.username AND r.role_id = ?1", nativeQuery = true)
    List<User> findByRoleId(Long roleId);

    /**
     * 根据角色中的部门查询
     *
     * @param id /
     * @return /
     */
    @Query(
            value = "SELECT u.* FROM sys_user u, sys_users_roles r, sys_roles_depts d WHERE "
                    + "u.username = r.username AND r.role_id = d.role_id AND r.role_id = ?1 ",
            nativeQuery = true)
    List<User> findByDeptRoleId(Long id);

    /**
     * 根据菜单查询
     *
     * @param id 菜单ID
     * @return /
     */
    @Query(
            value = "SELECT u.* FROM sys_user u, sys_users_roles ur, sys_roles_menus rm WHERE "
                    + "u.username = ur.username AND ur.role_id = rm.role_id AND rm.menu_id = ?1 ",
            nativeQuery = true)
    List<User> findByMenuId(Long id);

    /**
     * 根据Id删除
     *
     * @param ids /
     */
    void deleteAllByIdIn(Set<Long> ids);

    /**
     * 根据岗位查询
     *
     * @param ids /
     * @return /
     */
    @Query(
            value = "SELECT count(1) FROM sys_user u, sys_users_jobs j WHERE u.username = j.username AND j.job_id IN ?1",
            nativeQuery = true)
    int countByJobs(Set<Long> ids);

    /**
     * 根据部门查询
     *
     * @param deptIds /
     * @return /
     */
    @Query(value = "SELECT count(1) FROM sys_user u WHERE u.dept_id IN ?1", nativeQuery = true)
    int countByDepts(Set<Long> deptIds);

    /**
     * 根据角色查询
     *
     * @param ids /
     * @return /
     */
    @Query(value = "SELECT count(1) FROM sys_user u, sys_users_roles r WHERE "
            + "u.username = r.username AND r.role_id in ?1", nativeQuery = true)
    int countByRoles(Set<Long> ids);
}
