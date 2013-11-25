SmartOTA
========

This is the code source of the mission "SmartOTA" during my internship in Bouygues TELECOM. SmartOTA is a tool for automatizing the OTA test developed by Java/Eclipse.

Le but de l’application SmartOTA est l’optimisation des tests de téléchargement BIP CAT-TP en offrant à l’utilisateur une interface graphique à distance qui lui permet de télécharger des services (Téléchargement d’applications, Suppression d’applications, mise à jour de fichier).

Pour la réalisation des téléchargements BIP CAT-TP avec Smart OTA, il est nécessaire d’avoir à disposition le matériel suivant :
Matériels :
  PC 
  Modem avec carte SIM
Logiciels :
  Java Runtime Environment(JRE) 6 
  NowSMS
  Smart OTA
  
Pour utiliser Smart OTA il faut disposer  de:
  Fichier exécutable SmartOTA-v1.1.jar,
  Fichier SmartOTA-v1.1.bat : « Windows Batch File » pour démarrer l’application
  Répertoire config contenant ces fichiers de configuration:
    commandList.ini, pour enregistrer les commandes (ou services) à envoyer,
    configSIM.ini : pour enregistrer toutes les déclarations de cartes SIM,
    pushMsg.ini : pour enregistrer la commande  SMSPUSH
    configPE.ini : pour enregistrer toutes les déclarations de PE-s 
    Répertoire log: pour enregistrer toutes les données échangées dans les sessions BIP CAT-TP

