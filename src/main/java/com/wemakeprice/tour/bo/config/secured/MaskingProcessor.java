package com.wemakeprice.tour.bo.config.secured;

import com.wemakeprice.tour.bo.common.exception.ApplicationException;
import com.wemakeprice.tour.bo.common.util.SecuredProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MaskingProcessor {

    private static final Pattern IP_ADDRESS_PATTERN = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)|(([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]+|::(ffff(:0{1,4})?:)?((25[0-5]|(2[0-4]|1?[0-9])?[0-9]).){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1?[0-9])?[0-9]).){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9]))$");

    @Value("${wmp.addresses.office-ip}")
    private String officeIpAddresses;
    @Value("${wmp.addresses.cs-ip}")
    private String csIpAddresses;

    @PostConstruct
    private void initialize() {
        checkIpAddress(officeIpAddresses);
        checkIpAddress(csIpAddresses);
    }

    public void process(Object bean) {
        SecuredProcessor.MASKING.process(bean);
        SecuredClearancePolicy.apply(bean);
    }

    private void checkIpAddress(String ipAddresses) {
        if (StringUtils.isEmpty(ipAddresses)) {
            throw new ApplicationException("ip 주소가 설정되지 않았습니다.");
        }
        List<String> checkIpAddress = Arrays.asList(ipAddresses.split(","));
        if (CollectionUtils.isEmpty(checkIpAddress)) {
            throw new ApplicationException("ip 주소가 설정되지 않았습니다.");
        }
        for (String ipAddress : checkIpAddress) {
            if (ipAddress == null) {
                throw new ApplicationException("설정 ip 주소 오류");
            }
            if (!IP_ADDRESS_PATTERN.matcher(ipAddress).matches()) {
                throw new ApplicationException("설정 ip 주소 오류");
            }
        }
    }
}
