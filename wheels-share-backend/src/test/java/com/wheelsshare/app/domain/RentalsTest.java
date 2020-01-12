package com.wheelsshare.app.domain;

import com.wheelsshare.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RentalsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rentals.class);
        Rentals rentals1 = new Rentals();
        rentals1.setId(1L);
        Rentals rentals2 = new Rentals();
        rentals2.setId(rentals1.getId());
        assertThat(rentals1).isEqualTo(rentals2);
        rentals2.setId(2L);
        assertThat(rentals1).isNotEqualTo(rentals2);
        rentals1.setId(null);
        assertThat(rentals1).isNotEqualTo(rentals2);
    }
}
