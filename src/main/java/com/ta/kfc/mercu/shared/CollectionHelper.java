package com.ta.kfc.mercu.shared;

import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CollectionHelper {

    public Map<Product, Long> groupProductCount(List<Product> products) {
        return products.stream().collect((Collectors.groupingBy(p -> p, Collectors.counting())));
    }

}
