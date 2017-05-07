# ChatSystem

#TODO-LIST GLOBALE :

[ ] gérer les multi-conversations

[ ] gérer la réception de fichiers


#Changelist :

------------------------
Loko; v1

J'ai "créée" une méthode pour broadcast UDP, et j'ai traité l'envoi et le traitement des Notifs CONNECT et STATUS_CHANGE, il reste à handle les autre types de notifs (voir TODO), nottament le ACK_CONNECT pour répondre au CONNECT.

On a aussi une méthode sur la fenetreMain pour l'écran de notif.

-------------------------
JohnAnthony; v1.1

J'ai fait sendNotif et sendFile (pas testé puisque pas intégré à l'UI). sendFile s'appelle avec (User dest, String strPath), il suffit de lui passer le chemin vers le fichier à envoyer en String et il se débrouille (ça me parait pratique, à voir si ça l'est vraiment).

J'ai fait du débug parce que ça marchait pas, le second user à se connecter mourait en recevant un ACK_CONNECT. J'ai rendu le booléen Model.connected utile (il est mis à jour lors de la co, et du coup le UDPListener attend que tu sois login avant de se lancer) et j'ai viré la variable Model.localIP qui était obsolète (IP stockée dans Model.localUser) et qui m'a mis le cancer.

La découverte du réseau a l'air de marcher, les users se voient et peuvent chatter par message. Ça se passe moins bien quand on quitte la fenêtre de chat (spam d'exceptions massif).

Note : j'ai importé java.io.File pour gérer les fichiers, du coup pour utiliser la structure File de Alexis il faut utiliser Network.Packet.File

-----------------------
Loko; v1.2

Gestion de la notif DISCONNECT : - à l'envoi : grâce à un WindowListener sur FenetreMain, on peut effectuer des actions lors de la fermeture (peut être utile pour le fermeture de socket)
- à la réception : on deleteUser avec la même méthode que setStatus = parcours de liste dégueu 
