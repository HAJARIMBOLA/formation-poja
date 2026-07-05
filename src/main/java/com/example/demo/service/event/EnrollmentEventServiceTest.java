package com.example.demo.service.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.example.demo.endpoint.event.model.EnrollmentEvent;
import com.example.demo.mail.Email;
import com.example.demo.mail.Mailer;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class EnrollmentEventServiceTest {

    @Test
    void accept_sendsAConfirmationEmailToTheEnrolledUser() {
        Mailer mailer = mock(Mailer.class);
        EnrollmentEventService service = new EnrollmentEventService(mailer);

        EnrollmentEvent event =
                new EnrollmentEvent(1L, "ada@example.com", "Ada Lovelace", 2L, "Algorithmique avancée");

        service.accept(event);

        ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);
        verify(mailer).accept(emailCaptor.capture());

        Email sentEmail = emailCaptor.getValue();
        assertThat(sentEmail.to().getAddress()).isEqualTo("ada@example.com");
        assertThat(sentEmail.subject()).contains("Algorithmique avancée");
        assertThat(sentEmail.htmlBody()).contains("Ada Lovelace").contains("Algorithmique avancée");
    }
}