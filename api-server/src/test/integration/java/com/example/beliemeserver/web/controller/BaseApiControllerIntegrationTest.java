package com.example.beliemeserver.web.controller;

import com.example.beliemeserver.domain.dto.*;
import com.example.beliemeserver.util.StubWithInitialData;
import com.example.beliemeserver.util.FakeDao;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class BaseApiControllerIntegrationTest {
    protected StubWithInitialData stub = new StubWithInitialData();

    protected final FakeDao<UniversityDto> universityFakeDao = new FakeDao<>(stub.ALL_UNIVS);
    protected final FakeDao<MajorDto> majorFakeDao = new FakeDao<>(stub.ALL_MAJORS);
    protected final FakeDao<DepartmentDto> departmentFakeDao = new FakeDao<>(stub.ALL_DEPTS);
    protected final FakeDao<UserDto> userFakeDao = new FakeDao<>(stub.ALL_USERS);
    protected final FakeDao<StuffDto> stuffFakeDao = new FakeDao<>(stub.ALL_STUFFS);
    protected final FakeDao<ItemDto> itemFakeDao = new FakeDao<>(stub.ALL_ITEMS);
    protected final FakeDao<HistoryDto> historyFakeDao = new FakeDao<>(stub.ALL_HISTORIES);
}
