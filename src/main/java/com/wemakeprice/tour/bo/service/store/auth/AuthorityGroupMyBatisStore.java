package com.wemakeprice.tour.bo.service.store.auth;

import com.wemakeprice.tour.bo.domain.entity.auth.AuthorityGroup;
import com.wemakeprice.tour.bo.domain.store.auth.AuthorityGroupStore;
import com.wemakeprice.tour.bo.service.store.auth.mapper.AuthorityGroupMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AuthorityGroupMyBatisStore implements AuthorityGroupStore {

    private final AuthorityGroupMapper authorityGroupMapper;

    @Override
    public int insert(AuthorityGroup authorityGroup) {
        return authorityGroupMapper.insert(authorityGroup);
    }

    @Override
    public List<AuthorityGroup> selectAll() {
        return authorityGroupMapper.selectAll();
    }
}
