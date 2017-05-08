# ChatSystem

• Créateurs

Jérémy BASSO (LokoSama) et Sandor BUGEL (JohnAnthony795)

4 IR A2


• Compilation

Version Java utilisée : 1.7

Ce projet a été codé et compilé sous l'IDE Eclipse.

• Fonctionnalités implémentées

- Connexion, déconnexion d'un utilisateur
- Mise à jour en temps réel des utilisateurs et de leurs statuts
- Création, fermeture de conversation
- Envoi, réception de messages textuels
- Envoi, réception de fichiers

• Fonctionnalités non implémentées

- Envoi à multiples utilisateurs
- Système de timeout d'un utilisateur via un envoi périodique de paquet ALIVE

• Run configurations 

Le main est Chat.java.

Sans argument : le ChatSystem se lance avec un port d'écoute et d'envoi UDP fixe. Pour communiquer avec le ChatSystem d'autres groupes, ces ports doivent être identiques.
Avec un argument = 1 ou = 2 : le Chatsystem se lance en configuration locale de manière à pouvoir tester sur un seul PC avec deux instances (argument 1 et 2).





