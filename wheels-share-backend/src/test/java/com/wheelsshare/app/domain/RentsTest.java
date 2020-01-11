package com.wheelsshare.app.domain;

import com.wheelsshare.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RentsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rents.class);
        Rents rents1 = new Rents();
        rents1.setId(1L);
        Rents rents2 = new Rents();
        rents2.setId(rents1.getId());
        assertThat(rents1).isEqualTo(rents2);
        rents2.setId(2L);
        assertThat(rents1).isNotEqualTo(rents2);
        rents1.setId(null);
        assertThat(rents1).isNotEqualTo(rents2);
    }
}
