package com.ruoyi.common.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

/**
 * @author: song
 * @createDate: 2022-07-18 16:11
 * @description: TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultVO {
    private Integer code;
    private String message;
    private HashMap<String, Object> data = new HashMap<>();

    public ResultVO(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResultVO success() {
        return success("操作成功");
    }

    public static ResultVO success(String message) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(HttpStatus.OK.value());
        resultVO.setMessage(message);
        return resultVO;
    }

    public static ResultVO failure() {
        return failure("操作失败");
    }

    public static ResultVO failure(String message) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        resultVO.setMessage(message);
        return resultVO;
    }

    public ResultVO data(String str, Object obj) {
        this.data.put(str, obj);
        return this;
    }
}
