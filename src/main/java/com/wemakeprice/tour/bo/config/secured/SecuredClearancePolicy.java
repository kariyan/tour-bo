package com.wemakeprice.tour.bo.config.secured;

import com.wemakeprice.tour.bo.common.util.SecuredProcessor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecuredClearancePolicy {
    /**
     * 마스킹 시작 기준월 ( n 개월 지나면 마스킹 )
     */
    private static final int MASK_AFTER_SPECIFIC_MONTHS = 3;
    /**
     * 삭제 기준월 ( n 개월이 지나면 데이터는 노출되지 않아야 함 )
     */
    private static final int REMOVE_AFTER_SPECIFIC_MONTHS = 9;

    public static void apply(Object bean) {
        if (bean == null) {
            return;
        }

        if (Iterable.class.isAssignableFrom(bean.getClass())) {
            StreamSupport.stream(((Iterable<?>) bean).spliterator(), false)
                    .filter(innerBean -> innerBean instanceof SecuredExpirePrivacyBean)
                    .forEach(innerBean -> process((SecuredExpirePrivacyBean) innerBean));

        } else if (bean instanceof SecuredExpirePrivacyBean) {
            process((SecuredExpirePrivacyBean) bean);
        }
    }

    private static void process(SecuredExpirePrivacyBean securedExpirePrivacyBean) {
        Optional<LocalDate> date = securedExpirePrivacyBean.getExpireDateBasis();
        if (date.isPresent()) {
            if (date.get().isBefore(LocalDate.now().minus(REMOVE_AFTER_SPECIFIC_MONTHS, ChronoUnit.MONTHS))) {
                log.warn("Clear privacy information : {}", securedExpirePrivacyBean.getClass().getCanonicalName());
                SecuredProcessor.CLEAR.process(securedExpirePrivacyBean);
                return;
            }

            if (date.get().isBefore(LocalDate.now().minus(MASK_AFTER_SPECIFIC_MONTHS, ChronoUnit.MONTHS))) {
                SecuredProcessor.MASKING.process(securedExpirePrivacyBean);
            }
        }
    }
}
