package cn.geek51.kun.controller;

import cn.geek51.kun.entity.EmployeeSalary;
import cn.geek51.kun.service.EmployeeSalaryService;
import cn.geek51.util.ResponseUtil;
import cn.geek51.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

/**
 * @description: 员工工资统计
 * @author: kun
 * @create: 2020-07-24 14:40
 **/
@RestController
public class EmployeeSalaryController {

    @Autowired(required = false)
    private EmployeeSalaryService employeeSalaryService;

    //查询
    @GetMapping("/employeeSalarys")
    public Object list(Integer page,Integer limit,String query) throws Exception {

        HashMap queryMap = new HashMap();
        // 进行拼接, 拼接成一个MAP查询
        if (query != null) {
            queryMap = new ObjectMapper().readValue(query, HashMap.class);
        }

        if (StringUtils.isEmpty(queryMap.get("qstartTime"))){
            LocalDateTime localDateTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String format = dateTimeFormatter.format(localDateTime);
            queryMap.put("qstartTime",format);
        }

        PageHelper.startPage(page,limit);
        List<EmployeeSalary> employeeSalaryList = employeeSalaryService.employeeSalaryList(queryMap);
        PageInfo<EmployeeSalary> pageInfo = new PageInfo<>(employeeSalaryList);
        HashMap<Object, Object> map = new HashMap<>();
        map.put("size",pageInfo.getTotal());
        return ResponseUtil.general_response(employeeSalaryList,map);
    }

}
