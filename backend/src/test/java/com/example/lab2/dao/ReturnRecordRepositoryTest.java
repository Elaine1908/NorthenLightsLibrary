package com.example.lab2.dao;

import com.example.lab2.dao.record.ReturnRecordRepository;
import com.example.lab2.entity.ReturnRecord;
import org.apache.catalina.LifecycleState;
import org.junit.Test;
import org.junit.jupiter.api.Tag;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ReturnRecordRepositoryTest {

    @Autowired
    ReturnRecordRepository returnRecordRepository;

    @Test
    @Transactional
    public void test() {
        List<ReturnRecord> returnRecords = returnRecordRepository.getReturnRecordByUserIDAndISBN(
                113, "9787115545381"
        );

    }
}
