package com.wemakeprice.tour.bo.service.controller.common;

import com.wemakeprice.tour.bo.common.annotation.NoAuthorizationRequired;
import com.wemakeprice.tour.bo.common.request.URIMapping;
import com.wemakeprice.tour.bo.domain.service.code.CodeService;
import com.wemakeprice.tour.bo.service.logic.code.CodeServiceLogic;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(URIMapping.V1_BASE_URI + "/codes")
@RequiredArgsConstructor
public class CodeRestController {
    private final CodeService codeService;

    @Operation(summary = "코드타입 조회")
    @GetMapping
    @NoAuthorizationRequired
    public List<CodeServiceLogic.CodeEnumType> findCodeEnums() {
        return codeService.findCodeEnums();
    }
}
