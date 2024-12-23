package com.example.demo.test;

import com.sankuai.inf.leaf.service.SegmentService;
import com.sankuai.inf.leaf.service.SnowflakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/leaf")
public class LeafTestController {

    @Autowired
    private SnowflakeService snowflakeService;

    @GetMapping("/test")
    public String test() {
        return snowflakeService.getId("test").toString();
    }
}
