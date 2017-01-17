package com.ymatou.product.infrastructure;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by chenpengxuan on 2017/1/17.
 */
@Configuration
@ImportResource("classpath*:**/dubbo-provider.xml")
public class DubboXConfig {
}
