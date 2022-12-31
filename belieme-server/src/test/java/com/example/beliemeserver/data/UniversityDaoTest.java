package com.example.beliemeserver.data;

import com.example.beliemeserver.model.dao.UniversityDao;
import com.example.beliemeserver.model.dto.UniversityDto;
import com.example.beliemeserver.model.exception.DataException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.assertj.core.api.Assertions;

import java.util.List;

@SpringBootTest
public class UniversityDaoTest {
    @Autowired
    private UniversityDao universityDao;

    @Test
    public void getAllUniversitiesTest() throws DataException {
        List<UniversityDto> expected = List.of(
                new UniversityDto("HYU", "한양대학교", "https://api.hanyang.ac.kr/oauth/authorize/"),
                new UniversityDto("CKU", "가톨릭관동대학교", null),
                new UniversityDto("SNU", "서울대학교", null)
        );

        List<UniversityDto> result = universityDao.getAllUniversitiesData();
        Assertions.assertThat(result.size()).isEqualTo(expected.size());

        for(UniversityDto universityDto : result) {
            Assertions.assertThat(expected).contains(universityDto);
        }
    }
}
