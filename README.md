# PayMyBuddy

**PayMyBuddy** est un prototype d'application web de transfert d'argent développé en **Java 21** avec **Spring Boot** et **Maven**.  
Elle permet aux utilisateurs de s'inscrire, se connecter, ajouter des contacts, et effectuer des transferts d'argent dans un environnement sécurisé.

## 🚀 Fonctionnalités

- ✅ Inscription des utilisateurs
- ✅ Connexion
- ✅ Affichage des informations utilisateur
- ✅ Ajout de contacts
- ✅ Transfert d’argent entre contacts
- ✅ Déconnexion

## 🌐 Interface utilisateur

Voici les pages accessibles via l’interface web :

| URL             | Description                         | Accès |
|------------------|-------------------------------------|-------|
| `/login`         | Page de connexion utilisateur       | Public |
| `/register`      | Page d'inscription                  | Public |
| `/connections`   | Ajouter un contact                  | Authentifié |
| `/transfer`      | Transférer de l’argent à un contact | Authentifié |
| `/profil`        | Voir ses informations utilisateur   | Authentifié |    

## 🧩 Modèle Physique de Données (MPD)

Voici une vue du schéma de base de données utilisé dans l'application :

![Modèle Physique de Données](docs/img/mpd_oc6.png)

## 🛠️ Technologies

- **Langage :** Java 21
- **Framework :** Spring Boot
- **Build Tool :** Maven
- **Base de données :** MySQL
- **Frontend :** Thymeleaf
- **Tests unitaires :** JUnit 5 et Mockito
- **Couverture de code :** JaCoCo
- **Sécurité :**
    - Mots de passe cryptés
    - Accès restreint aux pages si l’utilisateur n’est pas connecté

## ⚙️ Installation et lancement

1. **Cloner le dépôt :**

    ```bash
    git clone https://github.com/votre-utilisateur/paymybuddy.git
    cd paymybuddy

2. **Créer un fichier `.env` à la racine du projet** avec les variables suivantes :  
   👉 Remplacez les valeurs par celles de **votre propre base de données locale** MySQL.

    ```env
    DB_URL=jdbc:mysql://localhost:3306/paymybuddy?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    DB_USER=**votre_nom_utilisateur_mysql**
    DB_PASSWORD=**votre_mot_de_passe_mysql**
   ```

    ```markdown
    > 🔐 Ne partagez jamais ce fichier `.env` ou vos identifiants dans un dépôt public.

3. **Lancer l’application :**

    ```bash
    mvn spring-boot:run

4. **Accéder à l’application via http://localhost:8080**



