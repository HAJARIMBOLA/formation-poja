package com.example.demo.file.zip;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class FileTyperTest {

  private final FileTyper fileTyper = new FileTyper();

  @Test
  void detecte_un_fichier_texte_brut() throws IOException {
    File textFile = File.createTempFile("poja-health-check", ".txt");
    textFile.deleteOnExit();
    Files.writeString(textFile.toPath(), "Hello world", StandardCharsets.UTF_8);

    MediaType detected = fileTyper.apply(textFile);

    assertThat(detected).isEqualTo(MediaType.TEXT_PLAIN);
  }

  @Test
  void detecte_un_fichier_json() throws IOException {
    File jsonFile = File.createTempFile("poja-payload", ".json");
    jsonFile.deleteOnExit();
    Files.writeString(jsonFile.toPath(), "{\"key\": \"value\"}", StandardCharsets.UTF_8);

    MediaType detected = fileTyper.apply(jsonFile);

    assertThat(detected).isEqualTo(MediaType.APPLICATION_JSON);
  }

  @Test
  void detecte_un_fichier_vide_comme_octet_stream() throws IOException {
    File emptyFile = File.createTempFile("poja-empty", ".tmp");
    emptyFile.deleteOnExit();

    MediaType detected = fileTyper.apply(emptyFile);

    assertThat(detected).isEqualTo(MediaType.APPLICATION_OCTET_STREAM);
  }
}
