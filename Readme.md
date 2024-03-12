# Java Chat App

This is a simple chat application built using Java and JavaFX.

## Features

- User registration and login
- Real-time messaging
- Group chat functionality
- File sharing

## Technologies Used

- Java
- JavaFX
- Socket programming
- JavaFX Scene Builder

## Getting Started

1. Clone the repository.
2. Open the project in your preferred Java IDE.
3. Build and run the application.
<!-- 
## Screenshots

![Login Screen](screenshots/login.png)
![Chat Screen](screenshots/chat.png) -->


<!-- ## Contributing

Contributions are welcome! Please fork the repository and submit a pull request. -->
# Team

* Ahmed Guetti
* Kritet Ilyas
* Boussoualef Mohamed Amine
## Our application core idea
### General vision 

The main objectif of our application is to simplify the communication  between multiple users.
we have the intetion to create an effecente and "just work" platform, due to an instance connection to answer the collaboration needs.


#### First iteration V0

In this first phase, we made a simple text chat between multiple users on the terminal as a CLI, using the power of socket, we garentide a robust and fast performence, the connextion can be between two users or a broardcast to all connected users.

Moreover we had made a simple authontification precess, even if it still not secure enough

#### Second iteration V1

Poursuivant notre engagement envers l'amélioration continue, la deuxième itération introduira des fonctionnalités plus avancées. Nous créerons des salons(rooms) pour permettre aux utilisateurs de regrouper des discussions spécifiques. 

As a next iteration in our project, wew are going to add multiple new funtionnalaties, as creating rooms and let the users to chose the room to be in. Moreover, saving all the messages and information in a database (DAO)

#### Third iteration V2

We are going to add the posibilite to send a file, making the platforme more usable as a collaboration platform.

In this step we are going to add the graphical interface, that addapte to all the current functionality

### Fourth iteration V3
Add multiple functionnality to finalise the project, for exemple:
- Reaction to messages
- change profile information
- answer a specifique message


## Technologies Stack
Our choise of technologies was made to garante a fluid developing process and an optimised result, keeping in mind the end user expirience 

- Java: we chose Java for it's popularity and performance while maintaining a solid developing experince  

- JavaFX: As one of the most famous Desktop application framework. helping us to keep the UI as clean and straight forward as possible

- mysql: our database of choise, for our application 
# Interface

En tant qu'application desktop, notre interface sera conçue de manière à être à la fois fonctionnelle et esthétiquement plaisante. Des boutons intuitifs faciliteront la navigation, tandis que les zones de texte aux saisies du clavier, créant ainsi une expérience utilisateur immersive et agréable. Nous nous engageons à fournir une interface interactive.

Althought our application is a Desktop application, we are going to try making it 
functional as well as aesthetically plesent when it comes to the UI,


# class diagram

![JAVA_CHAT_CLASS_UML drawio](https://github.com/K-Ilyas/java-chat-app/assets/61426347/94c3bd5f-b678-4483-802a-e755005852f6)

# Use Case 

<img width="476" alt="image" src="https://github.com/K-Ilyas/java-chat-app/assets/61426347/07055bf7-abed-46db-b03f-f98ec8e9c25e">

# Technical implementation 

### ExecutorService

- ExecutorService crees un ensemble de Threads
- Envoyer des taches (runnable ou callable) à l’ExecutorService .
- Recuperer les resultats des tâches sous forme de Future.
###### Implementation 

 La classe Executors permet créer un ExecutorService avec differents 
types de Thread Pool.
 - Executors.newFixedThreadPool(int n)
   - Créer un ExecutorService avec un nombre fixe n de Threads et les garde toujour actives
   - S'ils sont tous occupés les tâches seront placé dans une file d’attente
 - Executors.newCachedThreadPool()
   - Pour une nouvelle tâche Il utilise un Threads disponible sinon il crée un nouveau.
   - Si un Thread reste sans tache pour une période(une minute) il sera supprimé 
automatiquement.
 - Executors.newSingleThreadExecutor()
   - Crée un seul Thread pour une exécution séquentielle de plusieur tâches.
![executor](https://github.com/K-Ilyas/java-chat-app/assets/61426347/37136685-5e66-4554-9caa-a43e98a690af)


# MCD
![image](https://github.com/K-Ilyas/java-chat-app/assets/124268899/3e32b81c-4d66-4b61-b5be-d320b7bf785f)


# mockup

[wireframe.pdf](https://github.com/K-Ilyas/java-chat-app/files/14418175/wireframe.pdf)

## License

This project is licensed under the [MIT License](LICENSE).
