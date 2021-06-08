package com.example.demo.lemoncash.exchange.coin.type;

import com.example.demo.lemoncash.exceptions.EntityNotFoundException;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

@Service
@Builder
public class CoinTypeDataService {

    @Resource
    private final CoinTypeDAO coinTypeDAO;

    private List<CoinType> coinTypes;

    @PostConstruct
    public void init() {
        coinTypes = coinTypeDAO.getAllCoinTypes();
    }

    public List<CoinType> getAllCoinTypes() {
        return this.coinTypes;
    }

    public Optional<CoinType> getById(Integer id) {
        return coinTypes.stream()
                .filter(ct -> ct.getId().equals(id))
                .findFirst();
    }

    public Optional<CoinType> getByNameAbbr(String nameAbbr) {
        return coinTypes.stream()
                .filter(ct -> StringUtils.equals(ct.getNameAbbr(), nameAbbr))
                .findFirst();
    }

    public String getFormattedAmount(Double amount, Integer coinTypeId) throws EntityNotFoundException {
        CoinType coinType = this.getById(coinTypeId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Coin Type not found for id: %i", coinTypeId)));

        String format = coinType.getFormat();
        DecimalFormat df = new DecimalFormat(format);
        return df.format(amount);
    }

}
