package com.example.demo.service.formation;

import com.example.demo.endpoint.rest.model.InscriptionRequest;
import com.example.demo.endpoint.rest.model.InscriptionResponse;
import com.example.demo.mail.Email;
import com.example.demo.mail.Mailer;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@AllArgsConstructor
public class InscriptionService {

  private final Mailer mailer;

  public InscriptionResponse inscrire(InscriptionRequest request) {
    validate(request);

    var recipient = toInternetAddress(request.participantEmail());
    mailer.accept(
        new Email(
            recipient,
            List.of(),
            List.of(),
            "Confirmation d'inscription - " + request.formationTitle(),
            buildConfirmationBody(request),
            List.of()));

    return new InscriptionResponse(
        "Inscription confirmée pour "
            + request.participantName()
            + " à la formation "
            + request.formationTitle());
  }

  private void validate(InscriptionRequest request) {
    if (!StringUtils.hasText(request.formationTitle())) {
      throw new IllegalArgumentException("Le titre de la formation est obligatoire");
    }
    if (!StringUtils.hasText(request.participantName())) {
      throw new IllegalArgumentException("Le nom du participant est obligatoire");
    }
    if (!StringUtils.hasText(request.participantEmail())) {
      throw new IllegalArgumentException("L'adresse e-mail du participant est obligatoire");
    }
  }

  private InternetAddress toInternetAddress(String email) {
    try {
      return new InternetAddress(email);
    } catch (AddressException e) {
      throw new IllegalArgumentException("L'adresse e-mail du participant est invalide", e);
    }
  }

  private String buildConfirmationBody(InscriptionRequest request) {
    return "<p>Bonjour "
        + request.participantName()
        + ",</p>"
        + "<p>Votre inscription à la formation <b>"
        + request.formationTitle()
        + "</b> a bien été enregistrée.</p>"
        + "<p>À très bientôt !</p>";
  }
}
