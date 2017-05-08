# ChatSystem

- Binôme :

Jérémy BASSO (LokoSama) et Sandor BUGEL (JohnAnthony795)

4 IR A2


- Compilation

Version Java utilisée : 1.7

Ce projet a été codé et compilé sous l'IDE Eclipse.

- Fonctionnalités implémentées

• Connexion, déconnexion d'un utilisateur
• Mise à jour en temps réel des utilisateurs et de leurs statuts
• Création, fermeture de conversation
• Envoi, réception de messages textuels
• Envoi, réception de fichiers

- Fonctionnalités non implémentées

• Envoi à multiples utilisateurs
• Système de timeout d'un utilisateur via un envoi périodique de paquet ALIVE

- Run configurations 

Le main est Chat.java. Il peut avoir un argument :

• sans argument : le Chaysystem se lance avec un port UDP par défaut (2042)

• 1 ou 2 : le Chatsystem se lance en configuration locale de manière à pouvoir tester sur un seul PC avec deux instances (argument 1 et 2).

• autre, > 1023 : le Chatsystem se lance avec comme port UDP l'argument d'entrée


# Rapport de tests

Nous avons choisi de réaliser des tests approfondis sur la partie Model de notre application, d'abord parce que c'est une partie indépendante du reste de l'application et ensuite par la contrainte du temps qui nous a empêché de mettre en place des tests sur le réseau par exemple. Nous ne présenterons ici que les tests réalisés par notre binôme.

On peut retrouver ces tests dans notre classe ModelTest.java

- testAjoutUser
• Auteurs : notre binôme
• Principe : on ajoute un utilisateur et on teste si la taille de la liste d'utilisateurs est incrémentée et si l'utilisateur est dans la liste
• Résultat : succès en 0.003 s

- testDeleteUser
• Auteurs : notre binôme
• Principe : on ajoute un utilisateur (testé comme au dessus), on le supprimer et on vérifie la taille de la liste et l'absence de l'utilisateur de la liste
• Résultat : succès en 0.000s

- testSetStatus
• Auteurs : notre binôme
• Principe : on ajoute un utilisateur (testé comme au dessus), on modifie son statut et on vérifie son nouveau statut
• Résultat : succès en 0.000s

- testBenchMark
• Auteurs : notre binôme
• Principe : on ajoute 1000 utlisateurs appelés "Patrick"+n , puis on supprime les utilisateurs "Patrick"+n pairs. A chaque changement,on teste la taille de la liste et la présence des différents utilisateurs.
• Résultat : succès en 0.128s




