package com.mmall.controller.express;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "CustomerServiceController", description = "客服/工单")
@RestController
@RequestMapping("/express/service")
@Slf4j
public class CustomerServiceController {
}
