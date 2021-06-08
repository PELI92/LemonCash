package com.example.demo.lemoncash.exchange.coin.type;

import lombok.Builder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;


@Service
@Builder
public class CoinTypeService {

    @Resource
    private final CoinTypeDataService coinTypeDataService;

    private List<CoinType> coinTypes;

    @PostConstruct
    public void init() {
        coinTypes = Collections.unmodifiableList(coinTypeDataService.getAllCoinTypes());
    }

    public CoinType getCoinTypeByNameAbbr(String nameAbbr) {
        return coinTypes.stream()
                .filter(ct -> StringUtils.equals(ct.getNameAbbr(), nameAbbr))
                .findFirst()
                .get();
    }

}
