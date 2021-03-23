package com.wemakeprice.tour.bo.domain.store.auth;

import com.wemakeprice.tour.bo.common.sharedtype.OffsetList;
import com.wemakeprice.tour.bo.domain.service.auth.dto.AccountDTO;
import com.wemakeprice.tour.bo.domain.service.auth.key.AccountKey;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Validated
public interface AccountStore {

    Optional<AccountDTO> selectById(@NotBlank String accountId);

    OffsetList<AccountDTO> selectByCondition(AccountKey condition, @NotNull Pageable pageable);
}
