# SR03 Devoir 1 : Application de chat multi-thread en Java

## Contexte 

### Présentation de sujet

Dans le cadre de la formation de l'UV SR03, nous avons appris le socket. Il s'agit d'une technologie de transfert d'informations entre des programmes qui exécutent sur un réseau. Ce devoir vise à réaliser une application de chat en Java permettant de créer une salle de discussion dans laquelle plusieurs utilisateurs peuvent communiquer ensemble à l'aide de sockets. 

### Concepts

#### Application client/serveur
<!---https://fr.wikipedia.org/wiki/Client%E2%80%93serveur ---->
L'application est développée afin de créer un environnement client/serveur qui est «un mode de communication à travers un réseau entre plusieurs programmes : l'un, qualifié de client, envoie des requêtes ; l'autre ou les autres, qualifiés de serveurs, attendent les requêtes des clients et y répondent.»

#### Communication par Socket

<!---https://docs.oracle.com/javase/tutorial/networking/sockets/definition.html --->
D'après Oracle : «Un socket est un point d'extrémité d'une liaison de communication bidirectionnelle entre deux programmes exécutés sur le réseau. Un socket est lié à un numéro de port afin que la couche TCP puisse identifier l'application à laquelle les données sont destinées à être envoyées.» 

Cette définition explique également l'utilisation de socket dans ce devoir. Nous avons deux types de socket : le socket de connexion et le socket de communication.

- Socket de connexion : utilisé par le serveur et sert à établir une adresse que les clients peuvent utiliser pour trouver et se connecter au serveur 
- Socket de communication : utilisé par le serveur et le client sert à se dialoguer

![Imgur](https://i.imgur.com/Ue037A9.png?2)
Figure 1 : Client demande une connexion au serveur et le socket de communication établi sur chaque programme

#### Thread

Un thread désigne un « fil d'exécution » dans le programme. Il s'agit d'une suite linéaire et continue d'instructions qui sont exécutées séquentielles les unes après les autres. En effet, le langage Java est multi-thread, il permet d'utiliser facilement des threads.

Dans ce devoir, l'utilisation des threads permet à l'application d'être capable de traiter plusieurs tâches simultanément, par exemple : le programme de client puisse envoyer un message en recevant des autres messages, ou le serveur intercepte nombreux messages provenance de différents clients en même temps.

![Imgur](https://i.imgur.com/U6ZqYt9.png)
Figure 2 : La différence de l'execution en utilisant multi-thread et sans thread 

#### Mécanisme de HeartBeat
Comme ce que nous avons mentioné dans la partie de **Communication par socket**, la problématique c'est que les deux parties garderont leur socket ouvert indéfiniment jusqu'à le moment où le client envoie un message de «exit» au serveur. Cela laisse ouverte la possibilité qu'un côté puisse fermer son socket intentionnellement ou en raison d'une erreur, sans en informer l'autre côté. Afin de détecter ce scénario et de libérer les connexions périmées, et grâce au fait qu'on est basé sur la connexion de TCP, la mécanisme de ***Heartbeat*** sera une solution pertinante.
Grosso moto, les clients envoient des notifications périodiquement que l'application gestionnaire peut recevoir pour informer au serveur de l'état de sa connexion et dès que le serveur reçoit ce message, il va renvoyer un acquittement au client qui s'agit que le serveur a bien reçu l'état de sa connexion et parallèlement lui informe de l'état du serveur.
<img src="https://i.imgur.com/KVTO9yU.png" alt="Imgur" align=center />
Figure 3 : Scénario de Heartbeat 
Au côté serveur, si la connexion est silencieuse pendant *heartbeat_time* secondes, le serveur continuera d'attendre un moment, s'il n'y a toujours pas de message du client, cela signifie qu'il y a un problème avec le client. Dans ce cas-là le serveur se déconnecte activement et affiche une erreur « Le client est en panne ».

Et au côté client, la manière à détecter des déconnexions imprévues est similaire que cela du serveur.
### Objectifs fixés
Des objectifs à atteindre pour le devoir \:
- Établir une connexion permettant la trasmission de messages entre le client et le serveur
- Utiliser des threads différents pour traiter des messages reçus et envoyés dans le client et le serveur
- Être capable de diffuser le message du serveur à tous ses clients actifs
- Gérer la déconnexion d'un client dans le cas prévu et ainsi le cas imprévu

<!--- Cas d'utilisation théorique --->
### Cas d'utilisation théorique
![Imgur](https://i.imgur.com/9bgBIGn.png)
Figure 4 : Schéma d'utilisation théorique
## Réalisation

### Architecture de code
#### Présentation des classes, des packages
##### L'architecture du côté client
```bash
├── clients
│    ├── Client.java
│    ├── Client2.java
│    └── Client3.java
├── exception
│    └── PanneServeurException.java
├── message
│    ├── HBMessage.java
│    └── TextMessage.java
└── thread
    ├── HeartbeatAgent.java
    ├── HeartbeatListener.java
    ├── ReceiveMessage.java
    └── SendMessage.java
```
Maintenant, on a 4 packages et totalement 8 classes.
###### Package client
Dans le package **clients**, ce sont des classes `client` qui contiennent le même contenue qui permet à plusieurs de clients de démarrer son programme et ses propres threads. Chaque client doit avoir sa propre classe `client`.
###### Package exception
On crée une classe d'exception `PanneServeurException` afin de générer une erreur personnalisé qui avertit au client que le serveur a échoué.
###### Package message
Étant donné que les messages de Heartbeat doivent être envoyés tout le temps, nous devons les simplifier autant que possible afin de réduire la pression sur le serveur et le client durant le traitement des paquets de Heartbeat. C'est pour ça que nous avons créé une classe spécifique `HBMessage` pour cela.
* Classe HBMessage qui ne contient que son temps de création

    ```java=
    public class HBMessage implements Serializable {
            public String toString() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        }
    }    
    ```
* Classe TextMessage qui contient un champ `msg`
    ```java=
    public class TextMessage implements Serializable{
        private String msg = "";

        public TextMessage(String msg) {
            this.msg = msg;
        }
        public String getMsg() {
            return this.msg;
        }
    }
    ```
###### Package thread
* HeartbeatAgent
    Un thread qui permet d'envoyer de `HBMessage` au serveur toutes les `DEFAULT_SAMPLING_PERIOD` secondes.
* HeartbeatListener
    Un thread qui collecte tous des HBMessages et évaluer l'état du client.
* ReceiveMessage
    Un thread qui reçoit tous des objets de message venant de client et qui charge d'afficher des `TextMessages`.
* SendMessage
    Un thread qui s'occupe d'envoyer des objets de TextMessage.



##### L'architecture du côté serveur
```bash
├── Server.java
├── exception
│    └── PanneClientException.java
├── message
│    ├── HBMessage.java
│    └── TextMessage.java
└── thread
    ├── HeartbeatListener.java
    └── MessageReceptor.java
```
Il y a 3 packages et 6 classes. La structure est à peu près similaire au client. La seule différence est que le thread `MessageReceptor`est responsable de l'envoi et de la réception des objets de message au chaque client selon une liste de client.


#### Présentation des structures de données
##### Hashtable
<!-- Hashtable à sert quoi.   -->
##### Queue
<!-- Queue à sert quoi.   -->
Puisqu'on doit chercher une structure de donnée qui nous permet de savoir quand le client/serveur s'est déconnecté, c'est-à-dire qu’auquel moment que le client/serveur n'a reçu plus le HBmessage et donc à l'aide de la caractéristique **FIFO** de queue qui nous permet de simuler l'ordre du temps. Les dernières nouvelles de HBMessagequi sont stockées dans la file d'attente vont être dépilées plus tard. Une fois la file d'attente vide, il est probable que l'autre côté n'ait pas envoyé de HBmessage depuis longtemps.on arrive à juger de l'état de l'autre côté, qui est basé sur des HBmessages reçus. 

Voici les méthodes de queue fournis par java qu'on utilisait.
|                                           | throw Exception | retourner false/null |
| ----------------------------------------- | --------------- | -------------------- |
| Ajouter des éléments à la fin de la queue | add(E e)        | boolean offer(E e)   |
| Retourner l'élément de tête et l'enlever  | E remove()      | E poll()             |
| Retourner l'élément de tête et le garder  | E element()     | E peek()             |

### Développement
<!--- Explication et justification global le fonctionnement de l'application--->
#### Côté serveur

#### Côté client

#### Réalisation de la mécanisme de HeartBeat
<img src="https://i.imgur.com/RS8xY6e.png" alt="Imgur" style="zoom:80%;" align="center"/>
Pour ce réaliser, il nous faut penser à trois points.

##### 1. Comment envoyer d'objet `HBMessage`
On crée une fonction commune qui permet d'envoyer un objet de `HBMessage` ou `TextMessage` en fonction du paramètre reçu à l'aide de `java.io.ObjectOutputStream`.
```java=
public void sendObject(Object obj) throws IOException {
    this.oos.writeObject(obj);
    this.oos.flush();
	}
```
Donc on peut la mettre dans une boucle infinite du thread `HeartbeatAgent`.
```java=
// HeartbeatAgent.java
while (!this.closed) {
    this.sendObject(new HBMessage());
    Thread.sleep(DEFAULT_SAMPLING_PERIOD * 1000);
}
```
##### 2. Comment distinguer les objet de message
Une fois que le thread reçoit un objet de message, il appliquera la fonction `interpreterMessage()`. Nous atteignons à récupérer d'objet envoyé par l'autre côté à travers de la méthode `readObject()`, et puis on le détecte par l'opérateur `insranceof` qui nous permet de savoir si cet objet est un type donné. 
* Si oui, on l'ajoute dans notre queue de `HeartbeatListener`.
* Sinon, il s'agit que cet objet est plutôt un objet de `TextMessage`.
```java=
public void interpreterMessage() throws ClassNotFoundException, IOException {
    Object obj = this.inputStream.readObject();
    if (obj instanceof HBMessage) {
        hbMsgList.add("ACK");
    } else {
        TextMessage receivedObj = (TextMessage) obj;
        this.receivedMsg = receivedObj.getMsg();
    }
}
```
##### 3. Comment juger l'état de l'autre côté à partir des HBMessage reçus
On sait le fait que le message reçu précédemment sera d'abord ajouté à la file d'attente, et une fois qu'on l'y met, il sera retiré. C'est-à-dire qu'une fois la file d'attente vide, cela signifie que, récemment, le thread n'a pas reçu de message, ce qui nécessite de lui rappeler d'attirer l'attention. Dans ce cas-là, nous pouvons attendre un intervalle de temps(`timeout`) pour confirmer que le thread ne recevoir plus de nouveaux messages. Donc, on arrive à conclure que l'autre côté avait du problème sur la connexion et à retourner une erreur à travers d'une exception personnalisée.
```java=
//HeartbeatListener.java
...
public void checkClientAlive() throws PanneClientException {
// Si la queue de HBMsg n'est pas vide -> cette client est active
    if (hbMsgList.peek() != null) {
        System.out.println("HBListener:\t" + hbMsgList.remove() + " is alive.");

    } else {
        try {
            Thread.sleep(timeout);
            if (hbMsgList.peek() == null) {
                this.closed = true;
                throw new PanneClientException("Le client" + this.clientName + " est en panne.");
            }
        } catch (Exception e) {
            throw new PanneClientException("Le client " + this.clientName + " est en panne.");
        }
    }
}

public void run() {
    while (!this.closed) {
        try {
            checkClientAlive();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

```


## Résultat
<!--- Conclusion et répondre des questions --->
Finalement, nous avons réussi à implémenter une application valide tous les objectifs fixés que nous avons mentionnés précédemment. 
<!--- Comment lancer le projet --->
### Utilisations pratiques
<!--- Démonstration : les captures de tous les situations de l'application--->
En executant l'application, l'utilisateur peut choisir son pseudonyme qui est unique dans la session de dicussion en cours. Ensuite, l'utilisateur peut communiquer avec tout les autres utilisateurs à travers des messages. Le message associant au pseudonyme de l'utilisateur permet d'indiquer son expidéteur.

<!--- Comment récupérer le projet depuis Git--->
## Installation
Les instructions suivantes se présentent l'installation, ainsi que l'execution du projet.
### Prérequis
Pour réaliser ce devoir, nous avons utilisé \:
- [Eclipse IDE 2019-12](https://www.eclipse.org/downloads/packages/release/2019-12/r)
- [Java SE 8](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)
### Import de projet
### Execution