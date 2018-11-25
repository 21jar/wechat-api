package com.ainijar.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author slt
 * @date 2018/10/19
 */
@EnableWebMvc
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket userApi() {
        //添加head参数start
//        ParameterBuilder tokenPar = new ParameterBuilder();
//        List<Parameter> pars = new ArrayList<Parameter>();
//        tokenPar.name("userkey").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
//        pars.add(tokenPar.build());
//                .globalOperationParameters(pars)

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("ainijar")
                .select()  // 选择那些路径和api会生成document
                .apis(RequestHandlerSelectors.basePackage("com.ainijar.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(userInfo());
    }

    private ApiInfo userInfo() {
        return new ApiInfoBuilder()
                .title("API")
                .contact("slt")
                .version("v1.0")
                .build();
    }
}
