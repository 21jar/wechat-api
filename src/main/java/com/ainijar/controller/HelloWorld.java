package com.ainijar.controller;

import com.ainijar.common.config.Result;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Api(description = "测试接口文档")
@RequestMapping("/hello")
public class HelloWorld {

    @GetMapping("/say1/{word}")
    @ApiOperation(value = "测试接口", notes = "测试接口")
    public Result say1(@ApiParam(name = "word", value = "helloworld", required = true) @PathVariable String word) {
        return Result.success(word, "成功");
    }

    @GetMapping("/say2")
    @ApiOperation(value = "测试接口", notes = "测试接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "word", value = "字")
    })
    public Result say2(@RequestParam(defaultValue = "", required = false) String word) {
        return Result.success(word, "成功");
    }

}
