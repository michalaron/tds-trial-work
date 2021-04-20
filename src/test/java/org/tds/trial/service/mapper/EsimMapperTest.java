package org.tds.trial.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EsimMapperTest {

    private EsimMapper esimMapper;

    @BeforeEach
    public void setUp() {
        esimMapper = new EsimMapperImpl();
    }
}
