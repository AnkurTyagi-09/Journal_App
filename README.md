# 📓 JournalApp

A simple Android journaling app built with **Java + AndroidX + Room
Database**. Users can add, view, and manage journal entries, customize
layouts, and personalize background colors.

------------------------------------------------------------------------

## ✨ Features

-   ➕ Add, edit, and delete journal entries
-   📑 View entries in **Linear** or **Grid** layout
-   ⚙️ **Settings screen** to change layout & background color
-   🎨 Real-time color preview with RGB sliders
-   💾 Persistent storage using **Room Database**
-   🖱️ Floating Action Button (FAB) for quick entry addition
-   🛠️ Preferences saved with **SharedPreferences**

------------------------------------------------------------------------

## 🛠️ Tech Stack

-   **Language:** Java
-   **UI:** AndroidX, Material Components, RecyclerView
-   **Data:** Room Database, SharedPreferences
-   **Architecture:** Single-activity + multiple screens

------------------------------------------------------------------------

## 📂 Project Structure

    JournalApp/
     ├── app/src/main/
     │   ├── java/com/example/journalapp/
     │   │   ├── MainActivity.java
     │   │   ├── AddEntryActivity.java
     │   │   ├── SettingsActivity.java
     │   │   ├── JournalEntry.java
     │   │   ├── JournalDao.java
     │   │   ├── JournalDatabase.java
     │   │   └── JournalAdapter.java
     │   ├── res/layout/
     │   │   ├── activity_main.xml
     │   │   ├── activity_add_entry.xml
     │   │   ├── activity_settings.xml
     │   │   └── item_journal.xml
     │   └── res/values/
     │       ├── colors.xml
     │       ├── strings.xml
     │       └── arrays.xml
     ├── build.gradle
     └── README.md

------------------------------------------------------------------------

## 🚀 Getting Started

### Prerequisites

-   Android Studio Ladybug or newer
-   JDK 8+
-   Android SDK 33+

### Setup

1.  Clone the repository or download the project ZIP
2.  Open in **Android Studio**
3.  Let Gradle sync automatically
4.  Run on an emulator or physical device
