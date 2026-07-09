package com.example.demo.endpoint.rest.controller.formation;

import com.example.demo.endpoint.rest.model.InscriptionRequest;
import com.example.demo.endpoint.rest.model.InscriptionResponse;
import com.example.demo.service.formation.InscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class FormationInscriptionController {

  private final InscriptionService inscriptionService;

  @PostMapping("/formations/inscriptions")
  public ResponseEntity<InscriptionResponse> inscrire(@RequestBody InscriptionRequest request) {
    var response = inscriptionService.inscrire(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
