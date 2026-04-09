# DESCRIPTION DU PROJET
# INSTRUCTIONS D'INSTALLATION 
# EXEMPLES D'UTILISATION


# 📱 **Simulation Mobile Money** - Système de Gestion de Transactions

Ce projet est une application de **gestion financière** développée en **Java** avec une persistance des données sous **MySQL**. Elle simule les fonctionnalités essentielles d'un service de Mobile Money (Dépôts, Retraits, Consultation).

---

L'objectif de cette application est de fournir une plateforme robuste pour gérer de manière automatisée et sécurisée les interactions financières des clients. Le système repose sur une architecture **DAO (Data Access Object)** pour garantir une séparation claire entre la logique métier et le stockage des données.

### **Architecture Technique**
Le projet est structuré en plusieurs couches :
* **Model** : Représentation des entités métier (**Client**, **Compte**, **Operation**).
* **DAO** : Gestion de la persistance des données via **JDBC**.
* **Database** : Connexion centralisée à MySQL via le pattern **Singleton**.

### **Modélisation UML**
Le système a été conçu en respectant les principes de la **POO**. Voici le diagramme de classes qui sert de base au développement :

![Diagramme de classes UML](schema_uml.png)

---

## ⚙️ **2. INSTRUCTIONS D'INSTALLATION**

### **Prérequis**
* **Java JDK 17+** installé.
* **XAMPP** (pour MySQL et phpMyAdmin).
* Pilote JDBC : **`mysql-connector-j-9.5.0.jar`** (placé dans le dossier `/lib`).

### **Configuration de la Base de Données**
1. Démarrez les modules **Apache** et **MySQL** via le panneau de contrôle XAMPP.
2. Créez une base de données nommée `simulation_mobile_money`.
3. Importez le script SQL :
   ```sql
   /* Dans votre terminal MySQL ou via l'onglet SQL de phpMyAdmin */
   SOURCE database.sql;
