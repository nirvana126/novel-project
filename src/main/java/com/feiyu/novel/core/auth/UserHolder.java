package com.feiyu.novel.core.auth;

import lombok.experimental.UtilityClass;

/**
 * 用户信息 持有类
 * UtilityClass该注解一般用在工具类上，为类中所有成员属性和方法都加上static关键字，方便通过类名直接调用
 * @author gsj
 * @date 2022/5/18
 */
@UtilityClass
public class UserHolder {

    /**
     * 当前线程用户ID
     */
    private static final ThreadLocal<Long> userIdTL = new ThreadLocal<>();

    /**
     * 当前线程作家ID
     */
    private static final ThreadLocal<Long> authorIdTL = new ThreadLocal<>();

    public void setUserId(Long userId) {
        userIdTL.set(userId);
    }

    public Long getUserId() {
        return userIdTL.get();
    }

    public void setAuthorId(Long authorId) {
        authorIdTL.set(authorId);
    }

    public Long getAuthorId() {
        return authorIdTL.get();
    }

    public void clear() {
        userIdTL.remove();
        authorIdTL.remove();
    }

}
