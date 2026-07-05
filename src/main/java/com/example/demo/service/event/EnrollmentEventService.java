package com.example.demo.service.event;

import com.example.demo.endpoint.event.model.EnrollmentEvent;
import com.example.demo.mail.Email;
import com.example.demo.mail.Mailer;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import java.util.List;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EnrollmentEventService implements Consumer<EnrollmentEvent> {

    private final Mailer mailer;

    @Override
    public void accept(EnrollmentEvent event) {
        mailer.accept(toEmail(event));
    }

    @SneakyThrows(AddressException.class)
    private Email toEmail(EnrollmentEvent event) {
        return new Email(
                new InternetAddress(event.getUserEmail()),
                List.of(),
                List.of(),
                "Confirmation de votre inscription : " + event.getCourseTitle(),
                htmlBody(event),
                List.of());
    }

    private String htmlBody(EnrollmentEvent event) {
        return "<p>Bonjour "
                + event.getUserName()
                + ",</p>"
                + "<p>Votre inscription au cours <strong>"
                + event.getCourseTitle()
                + "</strong> a bien été prise en compte.</p>"
                + "<p>À bientôt !</p>";
    }
}