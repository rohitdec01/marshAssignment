package com.example.marshAssignment.repository;

import com.example.marshAssignment.MarshAssignmentApplication;
import com.example.marshAssignment.entity.UserDetail;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MarshAssignmentApplication.class)
public class UserDetailRepositoryTest {

    @Autowired
    private UserDetailRepository userDetailRepository;

    @After
    public void tearDown() {
        userDetailRepository.deleteAllInBatch();
    }

    @Test
    public void findByUsername() {
        userDetailRepository.deleteAllInBatch();

        UserDetail userDetail = UserDetail.builder().username("test1").password("test").build();
        userDetailRepository.save(userDetail);

        assertThat(userDetailRepository.findByUsername(userDetail.getUsername()))
                .isNotNull();
        assertThat(userDetailRepository.findByUsername(userDetail.getUsername()).getUsername())
                .isEqualTo(userDetail.getUsername());
    }

}
