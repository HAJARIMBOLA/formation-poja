package com.formation.hei.mail;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.VerifyEmailIdentityRequest;

class EmailAddressVerifierTest {

  @Test
  void demande_la_verification_de_l_adresse_email_aupres_de_ses() throws AddressException {
    var sesClient = mock(SesClient.class);
    var emailConf = mock(EmailConf.class);
    when(emailConf.getSesClient()).thenReturn(sesClient);

    var emailAddressVerifier = new EmailAddressVerifier(emailConf);
    var emailAddress = new InternetAddress("participant@example.com");

    emailAddressVerifier.accept(emailAddress);

    verify(sesClient)
        .verifyEmailIdentity(
            eq(
                VerifyEmailIdentityRequest.builder()
                    .emailAddress("participant@example.com")
                    .build()));
  }
}
