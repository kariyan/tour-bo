package com.wemakeprice.tour.bo.config.secured;

import java.time.LocalDate;
import java.util.Optional;

/**
 * 일정 기간이 지났을 때 마스킹이 필요한 경우 아래 인터페이스를 구현한다.
 * @see com.wemakeprice.tour.bo.common.util.SecuredProcessor
 */
public interface SecuredExpirePrivacyBean {
    /**
     * 개인정보 마스킹 처리 기준일을 제공해야 하는 경우 구현합니다.
     * <ul>
     *     <li>숙박-체크아웃일자</li>
     *     <li>패키지-유효기간종료일</li>
     *     <li>엑티비티-티켓유효기간종료일</li>
     * </ul>
     * @return 기준일자
     * @see SecuredClearancePolicy
     */
    Optional<LocalDate> getExpireDateBasis();
}
