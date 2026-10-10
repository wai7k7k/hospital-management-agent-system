package xian.woniuxy.customer.controller;

import com.ruoyi.common.core.domain.ResultVO;
import com.ruoyi.system.api.RemoteSysNoticeService;
import com.ruoyi.system.api.RemoteUserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xian.woniuxy.customer.service.IPatientService;

import java.util.Arrays;

/**
 * @author G
 * @description: TODO
 * @date 2022/8/23 14:50
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("customer")
public class CustomerController {



//    @NonNull
//    private RemoteSysNoticeService noticeService;
//    @NonNull
//    private RecordService recordService;
//    @NonNull
//    private PrescriptionService presService;
//    @NonNull
//    private RemoteUserService userServicel;

//    /**
//     * @description:
//     * @param: null
//     * @return:
//     * @author G
//     * @date: 2022/8/19 17:36
//     */
//    @GetMapping("notice")
//    public ResultVO notice(){
//        return ResultVO.success().data("Notices",noticeService.selNotices());
//    }
//    //  处方
//    @GetMapping("pres")
//    public ResultVO record(@RequestParam(value = "dateRange",required = false) String[] dateRange){
//        System.out.println("dateRange = " + Arrays.toString(dateRange));
//        String startTime = dateRange == null || dateRange.length != 2 ? "" : dateRange[0];
//        String endTime = dateRange == null || dateRange.length != 2 ? "" : dateRange[1];
//        return ResultVO.success().data("Pres",presService.selPres(startTime,endTime));
//    }
//    //  病历
//    @GetMapping("record")
//    public ResultVO record(){
//        return ResultVO.success().data("Record",recordService.selRecord());
//    }
//    //  充值
//    @PostMapping("recharge/{num}")
//    public ResultVO recharge(@PathVariable Integer num){
//        return userServicel.recharge(num) > 0 ? ResultVO.success() : ResultVO.failure();
//    }
}
