# Hydratation — HydrationTracker

Application Android de suivi de la consommation d'eau, développée en Kotlin avec Jetpack Compose. Son interface en français permet de suivre sa progression vers un objectif fixe de 2 000 ml.

## Fonctionnalités

- Ajouter 250 ml avec le bouton principal.
- Ajouter rapidement 100 ml ou 500 ml.
- Retirer 250 ml pour corriger le total, sans descendre sous zéro.
- Visualiser la progression avec une jauge circulaire animée et un pourcentage.
- Consulter le volume total bu, le volume restant et le nombre de verres de 250 ml.
- Afficher un message lorsque l'objectif est atteint.
- Réinitialiser le suivi après confirmation dans une boîte de dialogue.
- Utiliser une interface sombre aux accents turquoise, avec un écran défilant.

Les données sont actuellement conservées en mémoire dans un ViewModel : elles ne sont pas sauvegardées durablement après l'arrêt du processus de l'application. La remise à zéro est manuelle et l'objectif n'est pas personnalisable dans l'interface.

## Environnement de développement

- Android Studio Meerkat Feature Drop 2024.3.2, version conservée pour respecter l'environnement imposé.
- Android Gradle Plugin 8.10.1 et Gradle 8.11.1.
- Kotlin 2.2.10, Jetpack Compose et Material 3.
- SDK de compilation et SDK cible : Android API 36.
- Version Android minimale : Android 7.0 (API 24).
- JDK utilisé pour Gradle : JBR 21 intégré à Android Studio ; cible de compilation Java/Kotlin : JVM 11.

## Ouvrir et lancer le projet

1. Ouvrir le dossier du projet dans Android Studio.
2. Installer la plateforme Android API 36 et les Build Tools 35.0.0 dans **Tools → SDK Manager**.
3. Sélectionner le JDK intégré dans **Settings → Build, Execution, Deployment → Build Tools → Gradle**.
4. Lancer **File → Sync Project with Gradle Files**.
5. Sélectionner un émulateur ou un téléphone Android avec le débogage activé, puis lancer le module `app`.

Le fichier `local.properties` doit pointer vers le SDK de la machine. AndroidX est activé dans `gradle.properties`. La version de débogage utilise la signature standard Android.

## Compiler et vérifier

Depuis PowerShell, à la racine du projet, avec `JAVA_HOME` configuré vers le JDK :

```powershell
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:testDebugUnitTest :app:lintDebug
```

L'APK de débogage est généré dans `app/build/outputs/apk/debug/app-debug.apk`.

Les tests présents couvrent notamment les calculs du suivi d'hydratation, l'accès aux ressources Android et la génération d'une capture de l'écran de démonstration.

## Validation locale et rapport des corrections

Vérification du 14 septembre 2026 avec la version d'Android Studio imposée :

- Compilation de l'APK de débogage réussie.
- Sept tests automatisés relancés et réussis, sans échec ni erreur.
- Android Lint : aucune erreur bloquante et 37 avertissements conservés, notamment des suggestions de mise à jour.
- Installation et lancement depuis Android Studio sur l'émulateur Small Phone API 33 (Android 13).
- Ajout de 250 ml vérifié à l'écran : un verre, 250 ml au total et 1 750 ml restants.

Le [rapport Word](docs/Rapport_des_corrections.docx) et sa [version Markdown](docs/CORRECTIONS.md) détaillent le diagnostic, les corrections et la procédure de reproduction. Les [preuves de validation](docs/preuves/validation.txt) et une [capture de l'application](docs/preuves/application-250ml.png) sont incluses.

Projet GitHub : [YeoGermain/HydrationTracker](https://github.com/YeoGermain/HydrationTracker). Pour récupérer les sources, utiliser **Code → Download ZIP**, puis ouvrir le dossier extrait dans Android Studio. Les SDK et dépendances doivent être disponibles ou téléchargés sur la machine du destinataire.

Pour une compilation `release` en ligne de commande, configurer `KEYSTORE_PATH`, `STORE_PASSWORD` et `KEY_PASSWORD` avec une clé dont l'alias est `upload`. Conserver la clé de signature et ses mots de passe hors du dépôt.

Auteur: Yeo Germain
