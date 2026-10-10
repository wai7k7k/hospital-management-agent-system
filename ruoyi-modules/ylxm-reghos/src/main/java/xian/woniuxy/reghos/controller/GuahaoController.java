package xian.woniuxy.reghos.controller;

import com.ruoyi.common.core.domain.ResultVO;
import com.ruoyi.common.redis.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xian.woniuxy.reghos.service.GuahaoService;

/**
 * @author: song
 * @date: 2022-08-25 09:13:46
 * @desc: TODO
 */
@RestController
@RequestMapping("guahao")
@RequiredArgsConstructor
public class GuahaoController {

    private final GuahaoService guahaoService;
    @GetMapping("guahao")
    public ResultVO guahao(Long scheduleId, String patientCardId) {
        String bb = guahaoService.guahao(scheduleId, patientCardId);
        return ResultVO.success(bb);
    }
}
