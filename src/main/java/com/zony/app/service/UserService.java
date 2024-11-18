
package com.zony.app.service;

import com.zony.app.repository.UserRepository;
import com.zony.app.service.mapstruct.UserMapper;
import com.zony.app.service.criteria.UserQueryCriteria;
import com.zony.common.utils.*;
import lombok.RequiredArgsConstructor;
import com.zony.common.config.FileProperties;
import com.zony.app.domain.User;
import com.zony.common.exception.EntityExistException;
import com.zony.common.exception.EntityNotFoundException;
import com.zony.app.service.dto.JobSmallDto;
import com.zony.app.service.dto.RoleSmallDto;
import com.zony.app.service.dto.UserDto;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "user")
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final FileProperties properties;
    private final RedisUtils redisUtils;


    public Object queryAll(UserQueryCriteria criteria, Pageable pageable) {
        Page<User> page = userRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);


        return PageUtil.toPage(page.map(userMapper::toDto));
    }


    public List<UserDto> queryAll(UserQueryCriteria criteria) {
        List<User> users = userRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder));
        return userMapper.toDto(users);
    }


    @Cacheable(key = "'id:' + #p0")
    @Transactional(rollbackFor = Exception.class)
    public UserDto findById(long id) {
        User user = userRepository.findByUserId(id);
        ValidationUtil.isNull(user.getUserId(),"User","id",id);
        return userMapper.toDto(user);
    }


    @Transactional(rollbackFor = Exception.class)
    public void create(User resources) {
        if(userRepository.findByUsername(resources.getUsername())!=null){
            throw new EntityExistException(User.class,"username",resources.getUsername());
        }
        if(userRepository.findByEmail(resources.getEmail())!=null){
            throw new EntityExistException(User.class,"email",resources.getEmail());
        }
        resources.setUserId(userRepository.getMaxUserId()+1);
        userRepository.save(resources);
    }


    @Transactional(rollbackFor = Exception.class)
    public void update(User resources) {
        User user = userRepository.findByUserId(resources.getUserId());
        ValidationUtil.isNull(user.getUserId(),"User","id",resources.getUserId());
        User user1 = userRepository.findByUsername(resources.getUsername());
        User user2 = userRepository.findByEmail(resources.getEmail());

        if(user1 !=null&&!user.getUserId().equals(user1.getUserId())){
            throw new EntityExistException(User.class,"username",resources.getUsername());
        }

        if(user2!=null&&!user.getUserId().equals(user2.getUserId())){
            throw new EntityExistException(User.class,"email",resources.getEmail());
        }
        // 如果用户的角色改变
        if (!resources.getRoles().equals(user.getRoles())) {
            redisUtils.del("data::user:" + resources.getUserId());
            redisUtils.del("menu::user:" + resources.getUserId());
            redisUtils.del("role::auth:" + resources.getUserId());
        }
        // 如果用户名称修改
        if(!resources.getUsername().equals(user.getUsername())){
            redisUtils.del("user::username:" + user.getUsername());
        }
        user.setUsername(resources.getUsername());
        user.setEmail(resources.getEmail());
        user.setEnabled(resources.getEnabled());
        user.setRoles(resources.getRoles());
        user.setDept(resources.getDept());
        user.setJobs(resources.getJobs());
        user.setPhone(resources.getPhone());
        user.setNickName(resources.getNickName());
        user.setGender(resources.getGender());
        user.setIsContact(resources.getIsContact());
        userRepository.save(user);
        // 清除缓存
        delCaches(user.getUserId(), user.getUsername());
    }


    @Transactional(rollbackFor = Exception.class)
    public void updateCenter(User resources) {
        User user = userRepository.findByUserId(resources.getUserId());
        user.setNickName(resources.getNickName());
        user.setPhone(resources.getPhone());
        user.setGender(resources.getGender());
        userRepository.save(user);
        // 清理缓存
        delCaches(user.getUserId(), user.getUsername());
    }


    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            // 清理缓存
            UserDto user = findById(id);
            delCaches(user.getId(), user.getUsername());
        }
        userRepository.deleteAllByIdIn(ids);
    }


    @Cacheable(key = "'username:' + #p0")
    public UserDto findByName(String userName) {
        User user = userRepository.findByUsername(userName);
        if (user == null) {
            throw new EntityNotFoundException(User.class, "name", userName);
        } else {
            return userMapper.toDto(user);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void updatePass(String username, String pass) {
        userRepository.updatePass(username,pass,new Date());
        redisUtils.del("user::username:" + username);
    }


    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> updateAvatar(MultipartFile multipartFile) {
        User user = userRepository.findByUsername(SecurityUtils.getCurrentUsername());
        String oldPath = user.getAvatarPath();
        File file = FileUtil.upload(multipartFile, properties.getPath().getPath());
        user.setAvatarPath(Objects.requireNonNull(file).getPath());
        user.setAvatarName(file.getName());
        userRepository.save(user);
        if(StringUtils.isNotBlank(oldPath)){
            FileUtil.del(oldPath);
        }
        redisUtils.del("user::username:" + user.getUsername());
        return new HashMap<String,String>(1){{put("avatar",file.getName());}};
    }


    @Transactional(rollbackFor = Exception.class)
    public void updateEmail(String username, String email) {
        userRepository.updateEmail(username,email);
        redisUtils.del("user::username:" + username);
    }


    public void download(List<UserDto> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UserDto userDTO : queryAll) {
            List<String> roles = userDTO.getRoles().stream().map(RoleSmallDto::getName).collect(Collectors.toList());
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户名", userDTO.getUsername());
            map.put("角色", roles);
            map.put("部门", userDTO.getDept().getName());
            map.put("岗位", userDTO.getJobs().stream().map(JobSmallDto::getName).collect(Collectors.toList()));
            map.put("邮箱", userDTO.getEmail());
            map.put("状态", userDTO.getEnabled() ? "启用" : "禁用");
            map.put("手机号码", userDTO.getPhone());
            map.put("修改密码的时间", userDTO.getPwdResetTime());
            map.put("创建日期", userDTO.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 清理缓存
     * @param id /
     */
    public void delCaches(Long id, String username){
        redisUtils.del("user::id:" + id);
        redisUtils.del("user::username:" + username);
    }
}
