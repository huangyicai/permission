package com.mmall.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Huang YiCai
 * @since on 2018/6/28.
 */
@Configuration
@EnableSwagger2
@Slf4j
public class SwaggerConfig {
    /**
     * 可以注入多个doket，也就是多个版本的api，可以在看到有三个版本groupName不能是重复的，v1和v2是ant风格匹配，配置文件
     * @return
     */
    public static final String FIRST = "弗恩平台";
    public static final String SECOND = "快递公司平台";
    public static final String THIRD = "客户平台";

    public static final String PUBLIC_API = "公共api";
    public ParameterBuilder aParameterBuilder= new ParameterBuilder() ;


    public SwaggerConfig() {
        aParameterBuilder.parameterType("header")
                .name("Authorization")
                .description("header中Authorization字段用于认证")
                .modelRef(new ModelRef("string"))
                //非必需，这里是全局配置，然而在登陆的时候是不用验证的
                .required(false).build();
        log.info("启动成功");
    }

    @Bean
    public Docket fn() {
        //可以添加多个header或参数
        List<Parameter> aParameters = new ArrayList<Parameter>();
        aParameters.add(aParameterBuilder.build());
        return new Docket(DocumentationType.SWAGGER_2).groupName(PUBLIC_API).select().apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/public/**")).build().apiInfo(apiInfo4()).globalOperationParameters(aParameters);
    }

    @Bean
    public Docket express() {
        //可以添加多个header或参数
        List<Parameter> aParameters = new ArrayList<Parameter>();
        aParameters.add(aParameterBuilder.build());
        return new Docket(DocumentationType.SWAGGER_2).groupName(SECOND).select().apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/express/**")).build().apiInfo(apiInfo2()).globalOperationParameters(aParameters);
    }

    @Bean
    public Docket Customer() {
        //可以添加多个header或参数
        List<Parameter> aParameters = new ArrayList<Parameter>();
        aParameters.add(aParameterBuilder.build());
        return new Docket(DocumentationType.SWAGGER_2).groupName(THIRD).select().apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/customer/**")).build().apiInfo(apiInfo3()).globalOperationParameters(aParameters);
    }
    @Bean
    public Docket publicApi() {
        //可以添加多个header或参数
        List<Parameter> aParameters = new ArrayList<Parameter>();
        aParameters.add(aParameterBuilder.build());
        return new Docket(DocumentationType.SWAGGER_2).groupName(FIRST).select().apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/fn/**")).build().apiInfo(apiInfo1()).globalOperationParameters(aParameters);
    }


    private ApiInfo apiInfo1() {
        return new ApiInfoBuilder()
                .title(FIRST)
               .description(FIRST+"接口")
                .version("v0.01")
                .build();
    }
    private ApiInfo apiInfo2() {
        return new ApiInfoBuilder()
                .title(SECOND)
                .description(SECOND+"接口")
                .version("v0.01")
                .build();
    }
    private ApiInfo apiInfo3() {
        return new ApiInfoBuilder()
                .title(THIRD)
                .description(THIRD+"接口")
                .version("v0.01")
                .build();
    }
    private ApiInfo apiInfo4() {
        return new ApiInfoBuilder()
                .title(PUBLIC_API)
                .description(PUBLIC_API+"接口")
                .version("v0.01")
                .build();
    }

}
