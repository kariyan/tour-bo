package com.wemakeprice.tour.bo.service.logic.auth;

import com.wemakeprice.tour.bo.common.request.ServiceContextHolder;
import com.wemakeprice.tour.bo.config.AuthorizationContextConfiguration;
import com.wemakeprice.tour.bo.config.WebClientConfig;
import com.wemakeprice.tour.bo.config.datasource.TourBoDataSourceConfig;
import com.wemakeprice.tour.bo.config.datasource.WtourDealDataSourceConfig;
import com.wemakeprice.tour.bo.config.kms.KmsConfig;
import com.wemakeprice.tour.bo.domain.service.auth.DepartmentAuthorityService;
import com.wemakeprice.tour.bo.domain.service.auth.dto.DepartmentDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Import({
        KmsConfig.class,
        WebClientConfig.class,
        TourBoDataSourceConfig.class,
        WtourDealDataSourceConfig.class,
})
@Transactional
@SpringBootTest(classes = AuthorizationContextConfiguration.class)
class DepartmentAuthorityServiceTest {

    @Autowired
    private DepartmentAuthorityService departmentAuthorityService;

    @BeforeAll
    static void setUp() {
        ServiceContextHolder.getContext().setAccountId("2020070012");
    }

    @Test
    @DisplayName("department 권한 등록")
    void registerDepartmentAuthorities() {
        String departmentCd = "WSS4001036";
        List<String> departmentCds = Collections.singletonList(departmentCd);
        int result = departmentAuthorityService.registerDepartmentAuthorities(departmentCds);

        assertThat(result).isEqualTo(1);

        List<DepartmentDTO> departmentDTOS = departmentAuthorityService.findAllDepartmentDTOS()
                .stream()
                .filter(dto -> dto.getDepartmentCd().equals(departmentCd))
                .collect(Collectors.toList());

        assertThat(departmentDTOS.get(0).getDepartmentCd()).isEqualTo(departmentCd);
    }

    @Test
    @DisplayName("모든 department 조회")
    void findAllDepartmentDTOS() {
        List<DepartmentDTO> allDepartmentDTOS = departmentAuthorityService.findAllDepartmentDTOS();

        assertThat(allDepartmentDTOS).isNotNull();

        allDepartmentDTOS.forEach(dto -> log.info(dto.toString()));
    }

    @Test
    @DisplayName("특정 department 권한 삭제")
    void removeDepartmentAuthorities() {
        List<String> departmentCds = Collections.singletonList("WST0000224");

        assertThat(departmentAuthorityService.removeDepartmentAuthorities(departmentCds)).isPositive();
    }
}
