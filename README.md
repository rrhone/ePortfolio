# Capstone Project

[![Generic badge](https://img.shields.io/badge/page_builder-GitHub_Pages-purple.svg)](https://pages.github.com/) [![Generic badge](https://img.shields.io/badge/language-Markdown_|_HTML-orange.svg)](https://www.markdownguide.org/) [![Generic badge](https://img.shields.io/badge/editor-Markdown_Monster-blue.svg)](https://markdownmonster.west-wind.com/) [![Generic badge](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)

This repository contains my files and documents for development of my ePortfolio for my CS-499 Computer Science Capstone course final project. The creation of this professional portfolio showcases my skills, abilities, and strengths learned and grown throughout my Computer Science program at Southern New Hampshire University. I have selected a project to inmprove on and document my growth within the Computer Science Program by showcasing my knowledge and abilities in three categories: **Software Design & Engineering**, **Algorithms & Data Structure**, and **Databases**.

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
- Audit trail logs all inventory changes with user role and timestamps

---

## Capstone Enhancements
This project was enhanced in three key areas to demonstrate core computer science competencies:

### 1. Role-Based Access Control (Software Design & Engineering)
- Added a `LoginActivity` to select role (Admin or User)
- Used `SharedPreferences` to persist user role
- Restricted access to add/edit/delete features for non-admin users
- Shared logout/navigation menu using a custom `BaseActivity`

### 2. Low-Stock Alerts + Real-Time Search (Algorithms & Data Structures)
- Items with quantity less than 5 are visually flagged in the UI
- Implemented a dynamic search bar for real-time filtering of inventory
- Optimized RecyclerView updates for performance and usability

### 3. Inventory Audit Trail (Databases)
- Added a new Room table `InventoryLog` to track all item changes
- Each log stores: action type, item name, user role, and timestamp
- Logs displayed in a dedicated screen for admin review

---

## Tech Stack
- **Language:** Java
- **Database:** Room (SQLite abstraction)
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

---
