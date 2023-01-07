package com.example.beliemeserver.data;

import com.example.beliemeserver.util.FakeDataSet;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public abstract class DaoTest {
    @BeforeAll
    public static void initFakeDataSet() {
        FakeDataSet.init();
    }
}