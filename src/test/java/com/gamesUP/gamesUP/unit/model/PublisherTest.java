package com.gamesUP.gamesUP.unit.model;

import com.gamesUP.gamesUP.model.Publisher;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PublisherTest {

    @Test
    void shouldCreatePublisherWithAllFields() {
        Publisher publisher = new Publisher(1L, "Electronic Arts");

        assertThat(publisher.getId()).isEqualTo(1L);
        assertThat(publisher.getName()).isEqualTo("Electronic Arts");
    }

    @Test
    void shouldCreatePublisherWithOnlyName() {
        Publisher publisher = new Publisher("Ubisoft");

        assertThat(publisher.getName()).isEqualTo("Ubisoft");
        assertThat(publisher.getId()).isNull();
    }

    @Test
    void shouldSetFieldsIndividually() {
        Publisher publisher = new Publisher();
        publisher.setId(2L);
        publisher.setName("Activision");

        assertThat(publisher.getId()).isEqualTo(2L);
        assertThat(publisher.getName()).isEqualTo("Activision");
    }
}
