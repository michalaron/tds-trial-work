package org.tds.trial.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EsimSubscriptionMapperTest {

    private EsimSubscriptionMapper esimSubscriptionMapper;

    @BeforeEach
    public void setUp() {
        esimSubscriptionMapper = new EsimSubscriptionMapperImpl();
    }
}
