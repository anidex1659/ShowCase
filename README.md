# 🎬 ShowCase

**A beautiful, local-first media library for Android.**

ShowCase transforms your collection of movies and TV shows into a modern streaming-style experience. It automatically organizes your media, downloads metadata and artwork, remembers playback progress, and provides a clean, Netflix-inspired interface—all while keeping your files on your device.

---

## ✨ Features

- 🎥 Local movie & TV show library
- 📂 Automatic library scanning
- 🖼️ Rich metadata from TMDB
- 🎨 Posters, backdrops, logos, and thumbnails
- ▶️ Built-in ExoPlayer with VLC fallback
- ⏯️ Resume playback
- 🌙 Material 3 UI with dynamic themes
- ⚡ Offline-first architecture
- 💾 Lightweight local database
- 🎭 Beautiful streaming-service inspired interface

---

## 📸 Screenshots

> *Coming Soon*

---

# 🛠 Tech Stack

- Kotlin
- Jetpack Compose
- Material 3
- Room Database
- Coroutines & Flow
- DataStore
- Media3 ExoPlayer
- VLC (LibVLC) Fallback
- Coil
- Retrofit
- TMDB API

---

# 📂 Project Structure

```
ShowCase
│
├── app
├── features
│   ├── Home
│   ├── Library
│   ├── Player
│   ├── Search
│   ├── Metadata
│   └── Settings
│
├── data
│   ├── database
│   ├── repository
│   └── storage
│
└── ui
```

---

# 💾 Database Design

ShowCase uses a hybrid storage approach.

- **Room Database** stores relationships, playback information, and artwork references.
- **metadata.json** stores descriptive information.
- **Artwork** is stored as image files inside each media folder.

This keeps the database lightweight while allowing metadata to be updated independently.

### Entities

- **Series**
- **Episodes**
- **Artwork**
- **Playback History**

Relationship:

```
Series
 ├── Episodes
 │      └── Playback History
 │
 └── Artwork
```

---

# 📁 Folder Layout

```
Series Name
│
├── Season 1
│   ├── Episode 01.mkv
│   ├── Episode 02.mkv
│   └── ...
│
└── .showcase
    ├── metadata.json
    ├── poster.jpg
    ├── backdrop.jpg
    ├── logo.png
    └── thumbnail.jpg
```

Each library keeps its metadata alongside the media, making backups, migration, and rescanning simple.

---

# 🚀 Getting Started

### Clone

```bash
git clone https://github.com/yourusername/ShowCase.git
```

### Open

Open the project in **Android Studio**.

### Add TMDB API Key

Create a `local.properties` file and add:

```properties
TMDB_API_KEY=YOUR_API_KEY
```

### Build

Run the application on an Android device or emulator.

---

# 🎯 Roadmap

- [ ] Movie collections
- [ ] Playlists
- [ ] Watchlists
- [ ] Multi-user profiles
- [ ] Subtitle management
- [ ] Download manager
- [ ] Cloud synchronization
- [ ] Chromecast support
- [ ] Android TV optimization

---

# 🤝 Contributing

Contributions, ideas, and feature requests are welcome.

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Open a Pull Request
---

> **ShowCase** — *Your personal streaming platform, powered by your own media library.*
