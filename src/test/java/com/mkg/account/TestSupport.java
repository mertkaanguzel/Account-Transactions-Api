package com.mkg.account;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;

public class TestSupport {
    public static final String  CUSTOMER_API_ENDPOINT = "/v1/customer/";
    public static final String ACCOUNT_API_ENDPOINT = "/v1/account/";

    public static Instant getInstant() {
        Instant expectedInstant = Instant.parse("2023-08-30T19:22:00Z");
        Clock clock = Clock.fixed(expectedInstant, Clock.systemDefaultZone().getZone());

        return Instant.now(clock);
    }

    public static LocalDateTime getLocalDateTime() {
        return LocalDateTime.ofInstant(getInstant(), Clock.systemDefaultZone().getZone());
    }
}
