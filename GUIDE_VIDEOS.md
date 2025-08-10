# 🎬 Guide : Utiliser vos propres vidéos de bandes d'annonces

## 📁 Étape 1 : Placer vos vidéos

### Copiez vos vidéos dans ce dossier :
```
app/src/main/assets/
├── dune_part_two.mp4        → Pour "Dune: Part Two"
├── oppenheimer.mp4          → Pour "Oppenheimer"  
├── barbie.mp4               → Pour "Barbie"
├── spiderman_across.mp4     → Pour "Spider-Man: Across the Spider-Verse"
└── ... (autres vidéos)
```

## 📝 Étape 2 : Règles importantes

### ✅ Formats supportés :
- **MP4** (recommandé)
- **WebM** 
- **3GP**

### ✅ Contraintes :
- **Taille max** : 50MB par vidéo
- **Résolution** : 720p recommandé (1080p acceptable)
- **Durée** : 2-3 minutes max (bandes d'annonces)

### ✅ Nommage exact :
```
"Dune: Part Two"              → dune_part_two.mp4
"Oppenheimer"                 → oppenheimer.mp4
"Barbie"                      → barbie.mp4
"Spider-Man: Across..."       → spiderman_across.mp4
"Guardians of the Galaxy..."  → guardians_vol3.mp4
"John Wick: Chapter 4"        → john_wick_4.mp4
"Avatar: The Way of Water"    → avatar_2.mp4
"Top Gun: Maverick"           → top_gun_maverick.mp4
"The Batman"                  → the_batman.mp4
"Scream VI"                   → scream_6.mp4
```

## ⚙️ Étape 3 : Activer vos vidéos

### Dans le fichier `MovieImageManager.java`, décommentez :
```java
// 🎬 Vidéos que vous avez ajoutées
movieVideos.put("Dune: Part Two", "dune_part_two.mp4");
movieVideos.put("Oppenheimer", "oppenheimer.mp4");
movieVideos.put("Barbie", "barbie.mp4");
// ... autres vidéos
```

## 🎯 Comment ça fonctionne

### Système de priorité :
1. **Vérifie d'abord** : Y a-t-il une vidéo locale ?
2. **Si OUI** → Lecture avec ExoPlayer (dans l'app)
3. **Si NON** → Fallback vers YouTube/URL en ligne

### Avantages :
- ✅ **Lecture instantanée** (pas de chargement réseau)
- ✅ **Qualité contrôlée** (votre choix)
- ✅ **Pas de publicités** YouTube
- ✅ **Fonctionne hors ligne**

## 🚀 Étapes finales

1. **Copiez vos vidéos** dans `app/src/main/assets/`
2. **Décommentez les lignes** dans `MovieImageManager.java`
3. **Compilez** : `./gradlew assembleDebug`
4. **Installez** : `./gradlew installDebug`
5. **Testez** : Ouvrez un film → Bande d'annonce = VOTRE vidéo !

## ❌ Problèmes courants

### Vidéo ne se lance pas :
- Vérifiez le nom du fichier (exact)
- Vérifiez la taille (<50MB)
- Vérifiez le format (MP4 recommandé)

### Qualité mauvaise :
- Ré-encodez en 720p
- Utilisez un bitrate de 1-2 Mbps

### App crash :
- Vidéo trop lourde (réduisez la taille)
- Format non supporté (convertissez en MP4) 