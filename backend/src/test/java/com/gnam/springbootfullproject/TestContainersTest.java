package com.gnam.springbootfullproject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestContainersTest extends AbstractTestcontainersUnit {

    @Test
    void canStartMysqlDB() {
        assertThat(mySQLContainer.isRunning()).isTrue();
        assertThat(mySQLContainer.isCreated()).isTrue();
    }
}
