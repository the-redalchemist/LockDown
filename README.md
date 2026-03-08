# LockDown Minecraft Spigot Plugin

## Übersicht

Das LockDown-Plugin schützt deinen Minecraft-Server durch Passwortschutz und verschiedene Sicherheitsfunktionen.

## Funktionen

- **Passwortschutz**: Spieler müssen ein Passwort eingeben, um sich anzumelden.
- **Bewegungseinschränkungen**: Spieler können sich nicht bewegen, bis sie das Passwort korrekt eingegeben haben.
- **Countdown-Nachrichten**: Countdown-Nachrichten werden an Spieler gesendet, bevor sie gekickt werden.
- **Anpassbare Willkommensnachrichten**: Konfiguration von Titel, Untertitel und Chat-Nachrichten beim Beitritt.
- **Whitelist-Verwaltung**: Spieler werden automatisch zur Whitelist hinzugefügt, nachdem sie das Passwort korrekt
  eingegeben haben.
- **Limitierte Versuche**: Konfiguration der Anzahl der Male, die Spieler haben, um auf den Server zuzugreifen, bevor
  sie gebannt werden

## Verwendung

1. **Willkommensnachrichten**: Spieler erhalten anpassbare Titel, Untertitel und Chat-Nachrichten beim Beitritt. Diese
   können in der `config.yml` konfiguriert werden.
2. **Passwort eingeben**: Spieler müssen das Passwort eingeben, um zu spielen. Das Passwort kann in der `config.yml`
   konfiguriert werden.
3. **Bewegungseinschränkungen**: Spieler können sich nicht bewegen, bis sie das Passwort korrekt eingegeben haben.
4. **Countdown-Nachrichten**: Countdown-Nachrichten werden an Spieler gesendet, damit sie wissen, wie viel Zeit sie noch
   haben, um das Passwort einzugeben.
5. **Strafen**: Spieler, die das Passwort nicht rechtzeitig eingeben, werden bestraft und vom Server gekickt.

## Besonderheit

Wenn ein Spieler auf der Whitelist steht, kann er sich ohne Passworteingabe anmelden. Sollte ein Spieler von der
Whitelist entfernt werden, wird er beim nächsten Beitritt aufgefordert, das Passwort einzugeben.

## Installation

1. Lade die Plugin-JAR-Datei herunter.
2. Lege die JAR-Datei im `plugins`-Ordner deines Minecraft-Servers ab.
3. Starte den Server neu, um das Plugin zu laden.
4. Konfiguriere das Plugin, indem du die `config.yml` Datei im Verzeichnis `plugins/LockDown` bearbeitest.

## Befehle

### **`/password <Passwort>`**

Überprüft das eingegebene Passwort und fügt den Spieler zur Whitelist hinzu, wenn es korrekt ist.

### Die folgenden Befehle sind nur für Spieler mit der Berechtigung `operator` verfügbar!

### **`/lockdown enable`**

Aktiviert den Passwortschutz auf dem Server.

### **`/lockdown disable`**

Deaktiviert den Passwortschutz auf dem Server.

### **`/moveFix <Spieler>`**

Falls zum Beispiel ein Spieler auf den Server joined und bevor er das Passwort eingibt, von einem Administrator zur
Whitelist hinzugefügt wird, kann es passieren, dass der Spieler sich nicht bewegen kann. Mit diesem Befehl kann der
Spieler wieder beweglich gemacht werden.

## Konfiguration

Die Konfigurationsdatei (`config.yml`) erlaubt es, verschiedene Aspekte des Plugins anzupassen.

### Erklärung der Konfiguration

#### Allgemeine Einstellungen

- **`passwordProtected`**: Aktiviert oder deaktiviert den Passwortschutz. Optionen: `true` oder `false`.
- **`Password`**: Das Passwort, das Spieler eingeben müssen, um sich anzumelden. Beispiel: `meinPasswort`.
- **`time-until-kick`**: Zeit in Sekunden, bis ein Spieler gekickt wird, wenn er das Passwort nicht eingibt.
  Beispiel: `60`.
- **`amount-of-tries`**: Anzahl der Versuche, die ein Spieler hat, um das Passwort einzugeben, bevor er gebannt wird.
  Beispiel: `3`.
- **`banned-time`**: Zeit in Tagen, für die ein Spieler gebannt wird, wenn er das Passwort nicht eingibt.
  Beispiel: `300`.

#### Telepotations-Einstellungen

##### Teleportation bei Beitritt

- **`teleport-to-location.onJoin.enabled`**: Aktiviert oder deaktiviert die Teleportation von Spielern, wenn sie noch
  nicht auf der Whitelist sind und auf den Server Joinen: `true` oder `false`.
- **`teleport-to-location.onJoin.worldSpawn`**:  Wenn diese Option aktiviert ist, wird der Spieler zum Spawn des Servers
  teleportiert. Wenn diese Option aktiviert ist, wird die nachfolgende location ignoriert. Optionen: `true`
  oder `false`.
- **`teleport-to-location.onJoin.location`**: Die Koordinaten, zu denen der Spieler teleportiert wird, wenn er noch
  nicht auf der Whitelist ist und auf den Server Joinen. Beispiel: `x: 0, y: 100, z: 0`.

##### Teleportation bei korrekter Passworteingabe

- **`teleport-to-location.onPasswordCorrect.enabled`**: Aktiviert oder deaktiviert die Teleportation von Spielern, wenn
  sie das Passwort korrekt eingegeben haben: `true` oder `false`.
- **`teleport-to-location.onPasswordCorrect.worldSpawn`**:  Wenn diese Option aktiviert ist, wird der Spieler zum Spawn
  des Servers teleportiert. Wenn diese Option aktiviert ist, wird die nachfolgende location ignoriert. Optionen: `true`
  oder `false`.
- **`teleport-to-location.onPasswordCorrect.location`**: Die Koordinaten, zu denen der Spieler teleportiert wird, wenn
  er das Passwort korrekt eingegeben hat. Beispiel: `x: 0, y: 100, z: 0`.

#### Countdown-Settings

- **`countdown`**: Aktiviert oder deaktiviert die Countdown-Nachrichten. Optionen: `true` oder `false`.
- **`countdown-timing`**: Zeitpunkte in Sekunden, zu denen Countdown-Nachrichten gesendet werden. Diese gelten jeweils
  bis zum Kicken vom Server also 10 steht für 10 Sekunden bis kick. Beispiel: `10, 20, 30`. (Getrennt durch Kommas)
- **`countdown-message-template`**: Die Nachricht, die an Spieler gesendet wird, bevor sie gekickt werden. Um die Zeit
  bis zum Kick in der Nachricht anzuzeigen, den Platzhalter `%time%` verwenden.
  Beispiel: `Du hast noch %time% Sekunden, um das Passwort einzugeben`.

#### Willkommensnachrichten

- **`Welcome-Message.title`**: Titel, der beim Beitritt angezeigt wird. Beispiel: `Willkommen`.
- **`Welcome-Message.sub-title`**: Untertitel, der beim Beitritt angezeigt wird. Beispiel: `Bitte gib das Passwort ein`.
- **`Welcome-Message.chat-message`**: Liste von Chat-Nachrichten, die beim Beitritt angezeigt werden. Beispiel:
  ```yaml
  - 1:
    message: "Du hast 60 Sekunden Zeit, das Passwort einzugeben"
  - 2:
    message: "Gib das Password mittels dem Command §c/password §rein"
    ```
  