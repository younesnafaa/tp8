package champollion;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class ChampollionJUnitTest {
	Enseignant untel;
	UE uml, java;

	@BeforeEach
	public void setUp() {
		untel = new Enseignant("untel", "untel@gmail.com");
		uml = new UE("UML");
		java = new UE("Programmation en java");
	}

	@Test
	public void testNouvelEnseignantSansService() {
		assertEquals(0, untel.heuresPrevues(),
				"Un nouvel enseignant doit avoir 0 heures prévues");
	}

	@Test
	public void testAjouteHeures() {
		// 10h TD pour UML
		untel.ajouteEnseignement(uml, 0, 10, 0);

		assertEquals(10, untel.heuresPrevuesPourUE(uml),
				"L'enseignant doit maintenant avoir 10 heures prévues pour l'UE 'uml'");

		// 20h TD pour UML
		untel.ajouteEnseignement(uml, 0, 20, 0);

		assertEquals(10 + 20, untel.heuresPrevuesPourUE(uml),
				"L'enseignant doit maintenant avoir 30 heures prévues pour l'UE 'uml'");
	}

	@Test
	public void testAjouteInterventionValide() {
		untel.ajouteEnseignement(uml, 10, 20, 15);

		Intervention inter = new Intervention(new Date(), 2, uml, TypeIntervention.CM);
		untel.ajouteIntervention(inter);

		assertEquals(8, untel.resteAPlanifier(uml, TypeIntervention.CM),
				"Il doit rester 8 heures à planifier pour les CM de l'UE 'uml'");
	}

	@Test
	public void testAjouteInterventionDepassement() {
		untel.ajouteEnseignement(uml, 5, 10, 5); // 5 CM, 10 TD, 5 TP

		Intervention inter = new Intervention(new Date(), 6, uml, TypeIntervention.CM);
		assertThrows(IllegalArgumentException.class, () -> untel.ajouteIntervention(inter),
				"Ajouter une intervention qui dépasse le service prévu doit lever une exception");
	}

	@Test
	public void testResteAPlanifier() {
		untel.ajouteEnseignement(uml, 5, 5, 5); // 5 CM, 5 TD, 5 TP

		Intervention inter = new Intervention(new Date(), 3, uml, TypeIntervention.CM);
		untel.ajouteIntervention(inter);

		assertEquals(2, untel.resteAPlanifier(uml, TypeIntervention.CM),
				"Il doit rester 2 heures à planifier pour les CM de l'UE 'uml'");
		assertEquals(5, untel.resteAPlanifier(uml, TypeIntervention.TD),
				"Il doit rester 5 heures à planifier pour les TD de l'UE 'uml'");
		assertEquals(5, untel.resteAPlanifier(uml, TypeIntervention.TP),
				"Il doit rester 5 heures à planifier pour les TP de l'UE 'uml'");
	}

	@Test
	public void testEnSousService() {
		untel.ajouteEnseignement(uml, 10, 20, 15); // Total : 15 + 20 + 11 = 46
		assertTrue(untel.enSousService(),
				"L'enseignant doit être en sous-service avec moins de 192 heures prévues");

		untel.ajouteEnseignement(java, 100, 50, 40); // Total : 150 + 50 + 30 = 230
		assertFalse(untel.enSousService(),
				"L'enseignant ne doit plus être en sous-service avec 230 heures prévues");
	}

	@Test
	public void testInterventionsPourUE() {
		untel.ajouteEnseignement(uml, 10, 20, 15);

		untel.ajouteIntervention(new Intervention(new Date(), 5, uml, TypeIntervention.TD));
		untel.ajouteIntervention(new Intervention(new Date(), 3, uml, TypeIntervention.TP));

		assertEquals(15, untel.resteAPlanifier(uml, TypeIntervention.TD),
				"Il doit rester 15 heures à planifier pour les TD de l'UE 'uml'");
		assertEquals(12, untel.resteAPlanifier(uml, TypeIntervention.TP),
				"Il doit rester 12 heures à planifier pour les TP de l'UE 'uml'");
	}
}
