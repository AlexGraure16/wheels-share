package com.wheelsshare.app.domain;

import com.wheelsshare.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UsersTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Users.class);
        Users users1 = new Users();
        users1.setEmailAddress("test@gmail.com");
        Users users2 = new Users();
        users2.setEmailAddress(users1.getEmailAddress());
        assertThat(users1).isEqualTo(users2);
        users2.setEmailAddress("test2@gmail.com");
        assertThat(users1).isNotEqualTo(users2);
        users1.setEmailAddress(null);
        assertThat(users1).isNotEqualTo(users2);
    }
}
