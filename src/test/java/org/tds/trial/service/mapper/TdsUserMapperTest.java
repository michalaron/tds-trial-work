package org.tds.trial.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TdsUserMapperTest {

    private TdsUserMapper tdsUserMapper;

    @BeforeEach
    public void setUp() {
        tdsUserMapper = new TdsUserMapperImpl();
    }
}
