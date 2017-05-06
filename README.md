# ChatSystem

#TODO-LIST GLOBALE :

[ ] handle le reste des paquets UDP, se rapprocher d'un run "classique" (pas juste 2 instances) (tu veux dire quoi ? tester avec plus de 2 instances ?)
[ ] gérer la fermeture de fenêtreMsg : soit on envoie "fin de conversation" à son partenaire (et il faut recréer une conversation depuis le main pour reprendre), soit on laisse la conversation ouverte tant qu'il ne la ferme pas aussi de son côté (et il faut du coup faire repop la fenêtre s'il te reparle)
[ ] gérer la fermeture de fenetreMain : envoyer DISCONNECT, gérer la fermeture des sockets
[ ] gérer les multi-conversations
[ ] intégrer l'envoi de fichiers à l'interface et le tester
[ ] Revoir UDPListener.run() : la partie réception de Control a l'air de faire n'imp (notamment envoyer un port en dur)
[ ] Revoir Controller.launchChatWithUser() : le port récupéré par negotiatePort n'est pas utilisé...


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

