package com.formation.hei.datastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class ListGrouperTest {

  private final ListGrouper<Integer> listGrouper = new ListGrouper<>();

  @Test
  void groupe_une_liste_dont_la_taille_est_un_multiple_de_la_taille_de_groupe() {
    var result = listGrouper.apply(List.of(1, 2, 3, 4, 5, 6), 2);

    assertThat(result).containsExactly(List.of(1, 2), List.of(3, 4), List.of(5, 6));
  }

  @Test
  void groupe_une_liste_dont_le_dernier_groupe_est_incomplet() {
    var result = listGrouper.apply(List.of(1, 2, 3, 4, 5), 2);

    assertThat(result).containsExactly(List.of(1, 2), List.of(3, 4), List.of(5));
  }

  @Test
  void retourne_un_seul_groupe_si_la_taille_de_groupe_depasse_la_taille_de_la_liste() {
    var result = listGrouper.apply(List.of(1, 2, 3), 10);

    assertThat(result).containsExactly(List.of(1, 2, 3));
  }

  @Test
  void retourne_une_liste_vide_si_la_liste_d_entree_est_vide() {
    var result = listGrouper.apply(List.of(), 3);

    assertThat(result).isEmpty();
  }
}
