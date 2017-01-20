/*
 *
 *  (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.productsync.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author luoshiqian 2016/8/30 19:17
 */
@Configuration
@ImportResource("classpath:spring/dubbo-provider.xml")
public class DubboConfig {
}
