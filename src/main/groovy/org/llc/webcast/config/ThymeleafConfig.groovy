package org.llc.webcast.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect

/**
 * Created by Steven McAdams on 1/10/16.
 */
@Configuration
class ThymeleafConfig {

    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }

}
