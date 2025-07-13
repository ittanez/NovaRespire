# 🔧 Guide de Diagnostic - NovaRespire

## Problème : Changements non visibles dans Android Studio

### ✅ **Étapes de résolution dans Android Studio :**

1. **SYNC PROJECT** (OBLIGATOIRE)
   ```
   File → Sync Project with Gradle Files
   OU cliquer sur l'icône Sync dans la toolbar
   ```

2. **CLEAN & REBUILD**
   ```
   Build → Clean Project
   Attendre la fin, puis
   Build → Rebuild Project
   ```

3. **INVALIDATE CACHES** (si problème persiste)
   ```
   File → Invalidate Caches and Restart...
   → Invalidate and Restart
   ```

### 🎯 **Changements à vérifier :**

#### **1. Couleur des boutons (Orange #eab543)**
- Page principale : Bouton "En savoir plus" 
- Page À propos : Boutons et liens
- **Fichier** : `Color.kt` ligne 15

#### **2. Photo d'Alain dans "À propos"**
- Photo cercle 150dp en haut
- Photo 80dp dans biographie
- **Fichier** : `AboutScreen.kt`
- **Ressource** : `R.drawable.photo_alain`

#### **3. Design amélioré**
- Ombres sur les cartes
- Bordures arrondies plus grandes
- **Fichier** : `AboutScreen.kt`

### ⚠️ **Si erreurs de compilation :**

#### **Erreurs Hilt possibles :**
```kotlin
// Si erreur "Cannot find symbol: class HiltViewModel"
// Vérifier dans build.gradle.kts :
implementation("com.google.dagger:hilt-android:2.48")
kapt("com.google.dagger:hilt-compiler:2.48")
```

#### **Erreurs d'imports :**
```kotlin
// Si erreur sur BreathingSessionViewModel
import com.novahypnose.novarespire.model.BreathingSessionViewModel

// Si erreur sur hiltViewModel()
import androidx.hilt.navigation.compose.hiltViewModel
```

### 🚀 **Test de fonctionnement :**

1. **Compiler l'app** : `Build → Make Project`
2. **Lancer sur émulateur/device**
3. **Naviguer vers "À propos"** 
4. **Vérifier** :
   - Couleur orange des boutons
   - Présence photo d'Alain
   - Design des cartes amélioré

### 📱 **Si rien ne change encore :**

1. **Forcer la reconstruction** :
   ```
   ./gradlew clean
   ./gradlew build
   ```

2. **Vérifier la photo** :
   - Fichier `photo_alain.jpg` existe dans `res/drawable/`
   - Pas d'erreur dans les logs Android Studio

3. **Mode Debug** :
   - Activer les logs dans Logcat
   - Chercher erreurs avec tag "NovaRespire"

### 🎨 **Changements visuels attendus :**

- **Avant** : Boutons bleus #4A90E2
- **Après** : Boutons orange #eab543
- **Avant** : Icône/placeholder pour Alain
- **Après** : Vraie photo d'Alain
- **Avant** : Cartes basiques
- **Après** : Cartes avec ombres et coins arrondis

---

**Si le problème persiste, vérifiez le Build Output dans Android Studio pour voir les erreurs exactes.**