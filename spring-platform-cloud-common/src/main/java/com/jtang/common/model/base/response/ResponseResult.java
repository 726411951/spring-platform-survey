package com.jtang.common.model.base.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @date 2020/7/2 20:42
 * @author LinJinTang
 */
@Data
@ToString
@NoArgsConstructor
public class ResponseResult implements Response {

    /** 操作是否成功 */
    boolean success = SUCCESS;

    /** 操作代码 */
    int code = SUCCESS_CODE;

    /** 提示信息 */
    String message;

    public ResponseResult(ResultCode resultCode){
        this.success = resultCode.success();
        this.code = resultCode.code();
        this.message = resultCode.message();
    }

    public static ResponseResult success(){
        return new ResponseResult(CommonCode.SUCCESS);
    }
    public static ResponseResult fail(){
        return new ResponseResult(CommonCode.FAIL);
    }

}
