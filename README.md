# Capstone Project

[![Generic badge](https://img.shields.io/badge/page_builder-GitHub_Pages-purple.svg)](https://pages.github.com/) [![Generic badge](https://img.shields.io/badge/language-Markdown_|_HTML-orange.svg)](https://www.markdownguide.org/) [![Generic badge](https://img.shields.io/badge/editor-Markdown_Monster-blue.svg)](https://markdownmonster.west-wind.com/) [![Generic badge](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)

This repository contains my files and documents for development of my ePortfolio for my CS-499 Computer Science Capstone project. This portfolio showcases the skills (technical and non-technical) I have learned throughout the Computer Science program at Southern New Hampshire University. For this capstone, I have selected a project to inmprove on and document my growth within the Computer Science Program by showcasing my knowledge and abilities in three categories: **Software Design & Engineering**, **Algorithms & Data Structure**, and **Databases**.

<div style="text-align: center;">
    <a href="https://rrhone.github.io/ePortfolio" title="ePortfolio Home Page"><img src="https://img.shields.io/badge/Home-ePortfolio-cyan.svg?style=for-the-badge&logo=homeassistant" /></a>
</div>

---

## EZ Inventory
This is an Android-based inventory management application built during my time at Southern New Hampshire University. This app is designed for small teams or individuals who need to track items, quantities, and updates in a local and role-aware system.

### Features
- View a list of inventory items with name and quantity
- Add, edit, and delete items (admin-only functionality)
- Role-based login for admin and regular users
- Real-time search bar for filtering inventory
- Visual alerts for low-stock items

---

## Capstone Enhancements
This project was enhanced in three key areas to demonstrate core computer science competencies:

### 1. Role-Based Access Control (Software Design & Engineering)
- Integrated Firebase Authentication to securely manage user login and registration
- Implemented role selection (Admin or User) at account creation using Firestore to persist roles
- Restricted access to sensitive features like adding, editing, or deleting items based on user role
- Dynamically adjusted navigation and menu options through a shared BaseActivity to reflect the logged-in user's role

### 2. Low-Stock Alerts w/ Real-Time Search (Algorithms & Data Structures)
- Items with quantity below 5 are visually flagged in the UI
- Implemented a dynamic search bar for real-time filtering inventory items
- Optimized RecyclerView updates for performance and usability

### 3. Firebase Firestore Integration (Databases)
- Replaced the local Room database with Firebase Firestore for real-time, cloud-based data storage
- Updated all CRUD operations to work with Firestore collections and documents
- Ensured seamless data sync across devices with Firestore’s real-time listeners
- Simplified future scalability and remote access to inventory data

---

## Tech Stack
- **Language:** Java
- **Database:** Room (SQLite abstr
- **Backend Services:** Firebase Authentication, Firebase Firestore)
- **Architecture:** MVVM-lite with centralized `BaseActivity`
- **Tools:** Android Studio, Git, GitHub
- **Version Control:** Git with feature branches for enhancements

---

## Tools & Installation

### Required Tools
| Tool | Description |
|------|-------------|
| [Android Studio](https://developer.android.com/studio) | Official IDE for Android development |
| [Java SDK 8+](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html) | Included in Android Studio |
| [Git](https://git-scm.com/) | For cloning and version control |
| Android Emulator or Device | Minimum API 21 (Android 5.0) |
| [Firebase Console](https://console.firebase.google.com/) | Used for user authentication; storage for user roles and inventory data |

---

### Installation Steps
1. Clone the repository
```bash
   git clone https://github.com/rrhone/ePortfolio/ez-inventory.git
   cd ez-inventory
```
3. Open the project in Android Studio
4. Let Gradle sync and finish building the project
5. Connect an emulator or Android device
6. Click the green ▶ Run button

### Firebase Setup
To enable Firebase Auth and Firestore in your local build:
1. Create a Firebase project in the [Firebase Console](https://console.firebase.google.com/)
2. Register your Android app and download the `google-services.json` file
3. Place `google-services.json` in the `app/` directory of the project
4. Make sure Firebase services are enabled in your `build.gradle` files

---
