package com.wemakeprice.tour.bo.service.controller.common

import com.wemakeprice.tour.bo.common.request.ServiceContextHolder
import com.wemakeprice.tour.bo.config.AuthorizationContextConfiguration
import com.wemakeprice.tour.bo.config.WebConfig
import com.wemakeprice.tour.bo.config.datasource.TourBoDataSourceConfig
import com.wemakeprice.tour.bo.config.datasource.WtourDealDataSourceConfig
import com.wemakeprice.tour.bo.config.interceptor.AuthorizationInterceptor
import com.wemakeprice.tour.bo.config.interceptor.JwtInterceptor
import com.wemakeprice.tour.bo.config.interceptor.LoggerInterceptor
import com.wemakeprice.tour.bo.config.interceptor.SecuredInterceptor
import com.wemakeprice.tour.bo.config.kms.KmsProperties
import com.wemakeprice.tour.bo.domain.entity.auth.enumtype.AuthorityType
import groovy.util.logging.Slf4j
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

@Sql(value = "tour_bo.sql", config = @SqlConfig(dataSource = "tourBoDataSource",
        transactionManager = "tourBoTransactionManager"))
@Sql(value = "wtour_deal.sql", config = @SqlConfig(dataSource = "tourBoDataSource",
        transactionManager = "tourBoTransactionManager"))
@Slf4j
@Import([WebConfig,
        JwtInterceptor,
        LoggerInterceptor,
        SecuredInterceptor,
        AuthorizationInterceptor,

        TourBoDataSourceConfig,
        WtourDealDataSourceConfig,

        AuthorizationContextConfiguration,
])
@WebMvcTest(NavigationBarRestController)
@AutoConfigureJson
@AutoConfigureMockMvc
class NavigationBarRestControllerTest extends Specification {

    @SpringBean
    private KmsProperties kmsProperties = Mock()
    @Autowired
    private MockMvc mockMvc

    def "일반 사용자가 메뉴 조회 시 정상 조회"() {
        given:
        userContextSetup()

        when:
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/navigation-bars")
                .header("Authorization", userToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()

        then:
        noExceptionThrown()
    }

    def static userContextSetup() {
        def context = ServiceContextHolder.getContext()
        context.accountId = "2018110011"
        context.authorityGroupId = 3
        context.authorityType = AuthorityType.USER
    }

    def static userToken() { // accountId: 2018110011, authorityGroupId: 3
        "***"
    }
}
