# formation-poja

API Spring Boot d'inscription à des formations/cours, construite sur le framework **POJA**
(HEI). Un utilisateur peut s'inscrire à une formation ponctuelle ou s'abonner à un cours ;
un e-mail de confirmation est envoyé automatiquement après chaque inscription.

## Stack technique

- **Java 21**
- **Spring Boot 3.2.2**
- **Gradle** (wrapper inclus, `./gradlew`)
- **Lombok**
- **AWS SDK** (SES pour l'envoi d'e-mails, SQS/EventBridge pour le framework POJA)
- **JUnit 5 + Mockito + AssertJ** pour les tests
- **JaCoCo** pour la couverture de tests

## Architecture

```
com.example.demo
├── endpoint/rest/controller     # Controllers REST
│   ├── formation/               # Inscription à une formation (endpoint ponctuel)
│   ├── subscription/            # Abonnement à un cours
│   └── health/                  # Endpoints de santé
├── service/
│   ├── formation/                # InscriptionService
│   └── subscription/
│       ├── SubscriptionService.java
│       └── event/                # EnrollmentEvent + listener asynchrone
├── model/                        # User, Course
├── repository/                   # Interfaces + implémentations in-memory
├── mail/                         # Mailer, Email (généré par POJA)
├── exception/                    # Exceptions métier
└── conf/                         # Configuration (ex: AsyncConf)
```

## Endpoints

| Méthode | URL | Description |
|---|---|---|
| `POST` | `/formations/inscriptions` | Inscrit un participant à une formation et envoie un e-mail de confirmation (synchrone) |
| `POST` | `/users/{userId}/courses/{courseId}` | Abonne un utilisateur à un cours et déclenche l'envoi d'un e-mail de confirmation (asynchrone) |

### Codes de réponse

- `201 Created` — inscription/abonnement réussi
- `400 Bad Request` — données invalides (champ manquant, e-mail invalide, etc.)
- `404 Not Found` — utilisateur ou cours introuvable
- `409 Conflict` — l'utilisateur est déjà inscrit au cours

## Envoi d'e-mail asynchrone (abonnement à un cours)

Pour l'endpoint d'abonnement, l'envoi de l'e-mail ne bloque pas la réponse HTTP :

1. `SubscriptionService` sauvegarde l'inscription puis publie un `EnrollmentEvent`
   (`ApplicationEventPublisher`).
2. `EnrollmentEventListener` écoute cet événement via `@EventListener` et traite l'envoi
   dans un thread séparé grâce à `@Async` (activé par `AsyncConf`, annotée `@EnableAsync`).
3. Le `Mailer` (généré par POJA) envoie réellement l'e-mail via AWS SES.

Ce découplage permet de répondre au client dès que l'abonnement est enregistré, sans
attendre que l'e-mail parte.

## Lancer le projet

```bash
./gradlew bootRun
```

## Lancer les tests

```bash
./gradlew test
```

Les tests couvrent :
- la validation métier des services (`InscriptionService`, `SubscriptionService`)
- le listener d'événement (`EnrollmentEventListener`)
- les controllers REST (`@WebMvcTest`)
- les repositories en mémoire
- le gestionnaire d'exceptions global (`RestExceptionHandler`)

## Formatage du code

Le projet embarque `google-java-format`. Pour reformater tous les fichiers :

```bash
# Windows
format.bat

# Linux / macOS
find . -name "*.java" -exec java -jar google-java-format-1.23.0-all-deps.jar --replace {} \;
```

Les membres du groupe : 
Antonerrie STD24207
Miahy STD24123
Mbola STD24045
Lalaina STD
Franco STD24029
