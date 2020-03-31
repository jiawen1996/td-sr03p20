# SR03 TD3.2



| Faille                            | Corrections mises en place                                  |
| --------------------------------- | ----------------------------------------------------------- |
| Injection SQL                     | Contrôle des champs envoyés/réçus + Gestion de mot de passe |
| Cross-site scripting (XSS)        | Contrôle des champs envoyés/réçus                           |
| Violation de contrôle d’accès     |                                                             |
| Violation de gestion de session   | Protection des cookies                                      |
| Accès aux répertoires par http    | Protection des répertoires                                  |
| Falsification de requête (CSRF)   | Contrôle des champs envoyés/réçus                           |
| Chiffrement des données sensibles | Cryptage au niveau du stockage                              |



### Injection SQL
Ajouter la fonction `validField(field)` pour vérifier si le champs entré ne contient pas de caractères interdits telsque `' " : /` au fichier `myController.php`

Pour la connexion
* Ensuite, ajouter la fonction `existLogin(login)` pour assurer que le login se trouve bien dans la base de données.

Pour le virement
* Ajouter une condition supplémentaire que le montant doit être positif.

### Cross-site scripting (XSS)
Côté client
Dans le fichier `vw_moncompte.php`, la partie de `input.sujet/corps`, on peut ajouter un filteur de php pour contrôler JavaScript.
https://www.php.net/manual/fr/function.filter-input.php
```php
filter_input ( int $type , string $variable_name [, int $filter = FILTER_DEFAULT [, mixed $options ]] ) 
```
Côté serveur
Dans le fichier `myController.php`

### Violation de contrôle d’accès
#### 

### Violation de gestion de session

![Imgur](https://i.imgur.com/gUk414z.png)
Vue que la directive `HttpOnly` de cookie n'est pas activée, le pirate peut modifier l'id de session à travers les cookies.
Le cookie de session est bien `PHPSESSID`. Ce cookie est créé la première fois que l'on a fait appel à `session_start()` dans la page `index.php`
Ensuite ce cookie est échange entre le client et le serveur à chaque requête et réponse dans les entêtes HTTP.
Actuellement, si vous mettez dans une des pages un code du style : ``<script>alert(document.cookie);</script>`` alors vous verrez bien le cookie `PHPSESSID`.
Avec la directive `HTTPOnly`, ce code javascript ne doit plus vous donner accès à ce cookie.
Le cookie ne sera plus accessible en javascript (lecture et écriture), c'est à dire qu'il sera supprimé de la variable `document.cookie`.
Comme le cookie de session est créé automatiquement, vous pouvez par exemple utiliser la commande suivante juste avant le `session_start()` : 
```php
ini_set( 'session.cookie_httponly', 1 );
```

### Falsification de requête (CSRF)



### Chiffrement des données sensibles
Avant de stocker le mot de passe de l'utilisateur dans la base de données, il doit être chiffré (les fonction hash comme MD5 et SHA-1 ne sont pas recommandés). Ici, on utilise la fonction récommandée par PHP officiel. 
```php
password_hash ( string $password , int $algo [, array $options ] ) : string
```
Aller plus loin:  https://www.php.net/manual/fr/function.password-hash.php
Et donc lorsqu'une page Web appelle un mot de passe utilisateur à partir d'une base de données, nous devons le déchiffrer.
```php
password_verify ( string $password , string $hash ) : bool
```
Donc, dans ce cas là, on peut la rajouter dans la fonction de `findUserByLoginPwd($login, $pwd)` dans le fichier `myModel.php`.
Aller plus loin: https://www.php.net/manual/fr/function.password-verify.php




### Accès aux répertoires par http
Le droit d'access du dossier `config` est très ouvert. Donc maintenant on met `664(drw-rw-r--)` pour lui.
![Imgur](https://i.imgur.com/famlLqt.png)
Mais après on trouve que cela rendra la page Web incapable de lire le contenu de `config.ini`. Donc on fait du chiffrement aussi comme 