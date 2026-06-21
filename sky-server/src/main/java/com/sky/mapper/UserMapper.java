package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {
    /**
     * 根据openid查询用户
     * @param openid 微信用户的openid
     * @return 用户实体对象
     */
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

    /**
     * 插入用户
     * @param user 用户实体对象
     */
    void insert(User user);

    /**
     * 根据用户id查询用户
     * @param userId 用户id
     * @return 用户实体对象
     */
    @Select("select * from user where id = #{userId}")
    User getById(Long userId);

    /**
     * 统计当日新用户数
     * @param map 查询参数
     * @return 新用户数
     */
    @Select("select count(*) from user where create_time between #{begin} and #{end}")
    Long countNewUserByMap(Map<String, Object> map);
}
