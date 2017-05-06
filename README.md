# ChatSystem

J'ai "créée" une méthode pour broadcast UDP, et j'ai traité l'envoi et le traitement des Notifs CONNECT et STATUS_CHANGE, il reste à handle les autre types de notifs (voir TODO), nottament le ACK_CONNECT pour répondre au CONNECT.

On a aussi une méthode sur la fenetreMain pour l'écran de notif.

TODO : handle le reste des paquets UDP, se rapprocher d'un run "classique" (pas juste 2 instances).
