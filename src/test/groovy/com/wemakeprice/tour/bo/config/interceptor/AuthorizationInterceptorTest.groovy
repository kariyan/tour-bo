package com.wemakeprice.tour.bo.config.interceptor

import com.fasterxml.jackson.databind.ObjectMapper
import com.wemakeprice.tour.bo.common.exception.ErrorMessage
import com.wemakeprice.tour.bo.common.exception.ErrorType
import com.wemakeprice.tour.bo.common.request.ServiceContextHolder
import com.wemakeprice.tour.bo.config.AuthorizationContextConfiguration
import com.wemakeprice.tour.bo.config.WebConfig
import com.wemakeprice.tour.bo.config.datasource.TourBoDataSourceConfig
import com.wemakeprice.tour.bo.config.datasource.TourMainDataSourceConfig
import com.wemakeprice.tour.bo.config.datasource.WtourDealDataSourceConfig
import com.wemakeprice.tour.bo.config.kms.KmsProperties
import com.wemakeprice.tour.bo.domain.entity.auth.enumtype.AuthorityType
import com.wemakeprice.tour.bo.domain.service.auth.key.AccountMenuKey
import com.wemakeprice.tour.bo.domain.store.auth.AccountMenuStore
import com.wemakeprice.tour.bo.domain.store.auth.AuthorityGroupMenuStore
import com.wemakeprice.tour.bo.service.controller.auth.AccountAuthorityRestController
import com.wemakeprice.tour.bo.service.controller.auth.DepartmentAuthorityRestController
import com.wemakeprice.tour.bo.service.controller.common.NavigationBarRestController
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
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Sql(value = "tour_bo.sql", config = @SqlConfig(dataSource = "tourBoDataSource",
        transactionManager = "tourBoTransactionManager"))
@Sql(value = "tour_main.sql", config = @SqlConfig(dataSource = "tourMainDataSource",
        transactionManager = "tourMainTransactionManager"))
@Sql(value = "wtour_deal.sql", config = @SqlConfig(dataSource = "wtourDealDataSource",
        transactionManager = "wtourDealTransactionManager"))
@Slf4j
@Import([WebConfig,
        JwtInterceptor,
        LoggerInterceptor,
        SecuredInterceptor,
        AuthorizationInterceptor,

        TourBoDataSourceConfig,
        TourMainDataSourceConfig,
        WtourDealDataSourceConfig,

        AuthorizationContextConfiguration,
])
@WebMvcTest([DepartmentAuthorityRestController, AccountAuthorityRestController,
        NavigationBarRestController])
@AutoConfigureJson
@AutoConfigureMockMvc
class AuthorizationInterceptorTest extends Specification {

    @SpringBean
    private KmsProperties kmsProperties = Mock()
    @Autowired
    private ObjectMapper objectMapper
    @Autowired
    private MockMvc mockMvc
    @Autowired
    private AccountMenuStore accountMenuStore
    @Autowired
    private AuthorityGroupMenuStore authorityGroupMenuStore

    def "?????? ????????? ?????? ????????? ?????? ?????? ?????? ??????"() {
        given:
        userContextSetup()
        def context = ServiceContextHolder.getContext()
        def targetMenuId = 7

        expect: "??????????????? ????????? ??????"
        !accountMenuStore.selectByCondition(new AccountMenuKey(
                accountId: context.accountId
        )).stream()
                .filter { it.menuId == targetMenuId }
                .findFirst()
                .isPresent()

        and: "??????????????? ?????? ????????? ????????? ??????"
        !authorityGroupMenuStore.selectByAuthorityGroupId(context.authorityGroupId).stream()
                .filter { it.menuId == targetMenuId }
                .findFirst()
                .get()
                .readFlag

        when:
        def result = mockMvc.perform(get("/v1/auth/departments/authorities")
                .header("Authorization", userToken()))
                .andExpect(status().is4xxClientError())
                .andReturn()
                .response
                .contentAsString

        then:
        def errorMessage = objectMapper.readValue(result, ErrorMessage)
        errorMessage.errorType == ErrorType.AUTHORITY_ERROR
    }

    def "?????? ????????? ?????? ????????? ?????? ?????? ?????? ??????"() {
        given:
        userContextSetup()
        def context = ServiceContextHolder.getContext()
        def targetMenuId = 3

        expect: "??????????????? ????????? ??????"
        !accountMenuStore.selectByCondition(new AccountMenuKey(
                accountId: context.accountId
        )).stream()
                .filter { it.menuId == targetMenuId }
                .findFirst()
                .isPresent()

        and: "??????????????? ?????? ????????? ????????? ??????"
        !authorityGroupMenuStore.selectByAuthorityGroupId(context.authorityGroupId).stream()
                .filter { it.menuId == targetMenuId }
                .findFirst()
                .get()
                .writeFlag

        when:
        def result = mockMvc.perform(delete("/v1/exhibition/categories")
                .queryParam("categoryIds", "104")
                .header("Authorization", userToken()))
                .andExpect(status().is4xxClientError())
                .andReturn()
                .response
                .contentAsString

        then:
        def errorMessage = objectMapper.readValue(result, ErrorMessage)
        errorMessage.errorType == ErrorType.AUTHORITY_ERROR
    }

    def "?????? ????????? ?????? ????????? ?????? ?????? ?????? ??????"() {
        given:
        adminContextSetup()
        def context = ServiceContextHolder.getContext()
        def targetMenuId = 3

        expect: "??????????????? ????????? ??????"
        !accountMenuStore.selectByCondition(new AccountMenuKey(
                accountId: context.accountId
        )).stream().filter { it.menuId == targetMenuId }
                .findFirst()
                .get()
                .readFlag

        when:
        def result = mockMvc.perform(delete("/v1/exhibition/categories")
                .queryParam("categoryIds", "104")
                .header("Authorization", adminToken()))
                .andExpect(status().is4xxClientError())
                .andReturn()
                .response
                .contentAsString

        then:
        def errorMessage = objectMapper.readValue(result, ErrorMessage)
        errorMessage.errorType == ErrorType.AUTHORITY_ERROR
    }

    def "?????? ????????? ?????? ????????? ?????? ?????? ?????? ??????"() {
        given:
        adminContextSetup()
        def context = ServiceContextHolder.getContext()
        def targetMenuId = 7

        expect: "??????????????? ????????? ??????"
        !accountMenuStore.selectByCondition(new AccountMenuKey(
                accountId: context.accountId
        )).stream().filter { it.menuId == targetMenuId }
                .findFirst()
                .get()
                .writeFlag

        when:
        def result = mockMvc.perform(delete("/v1/exhibition/categories")
                .queryParam("categoryIds", "104")
                .header("Authorization", adminToken()))
                .andExpect(status().is4xxClientError())
                .andReturn()
                .response
                .contentAsString

        then:
        def errorMessage = objectMapper.readValue(result, ErrorMessage)
        errorMessage.errorType == ErrorType.AUTHORITY_ERROR
    }

    def "?????? ????????? ?????? ????????? ?????? ?????? ?????? ??????"() {
        given:
        userContextSetup()
        def context = ServiceContextHolder.getContext()
        def targetMenuId = 5

        expect: "??????????????? ????????? ??????"
        accountMenuStore.selectByCondition(new AccountMenuKey(
                accountId: context.accountId
        )).stream().filter { it.menuId == targetMenuId }
                .findFirst()
                .get()
                .writeFlag

        and: "?????? ?????? ?????? ?????? ?????? ??????"
        mockMvc.perform(delete("/v1/auth/accounts/account-menus/${context.accountId}")
                .header("Authorization", userToken()))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString == "1"

        when: "?????? ??????????????? ????????? ???????????? ?????? ???????????? ?????? ????????? ?????? ?????? ?????? ??????"
        def result = mockMvc.perform(delete("/v1/auth/accounts/account-menus/${context.accountId}")
                .header("Authorization", userToken()))
                .andExpect(status().is4xxClientError())
                .andReturn()
                .response
                .contentAsString

        then:
        def errorMessage = objectMapper.readValue(result, ErrorMessage)
        errorMessage.errorType == ErrorType.AUTHORITY_ERROR
    }

    def "NoAuthorization ?????????????????? ?????? ?????? ???????????? ??????"() {
        given:
        userContextSetup()

        expect:
        mockMvc.perform(get("/v1/navigation-bars")
                .header("Authorization", userToken()))
                .andExpect(status().isOk())
    }

    def static adminContextSetup() {
        def context = ServiceContextHolder.getContext()
        context.accountId = "2019060005"
        context.authorityGroupId = 1
        context.authorityType = AuthorityType.ADMIN
    }

    def static userContextSetup() {
        def context = ServiceContextHolder.getContext()
        context.accountId = "2018110011"
        context.authorityGroupId = 3
        context.authorityType = AuthorityType.USER
    }

    def static adminToken() { // accountId: 2019060005, authorityGroupId: 1
        "***"
    }

    def static userToken() { // accountId: 2018110011, authorityGroupId: 3
        "***"
    }
}
