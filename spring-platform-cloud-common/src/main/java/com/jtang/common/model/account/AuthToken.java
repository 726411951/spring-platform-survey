package com.jtang.common.model.account;


import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author lin512100
 * @date 2018/5/21
 */
@Getter
@Setter
public class AuthToken {
    /** 访问token就是短令牌，用户身份令牌 */
    String access_token;
    /** 刷新token */
    String refresh_token;
    /** jwt令牌 */
    String jwt_token;
}
