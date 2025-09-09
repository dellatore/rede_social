# Rede Social(rede_social) 
Rede Social is a project developed in Kotlin that implements a typical social network application. The structure includes Gradle configuration, modular architecture, and Kotlin backend code. Ideal for those looking to learn Kotlin–Gradle integration and web application design patterns.

## 🚀 Starting
The instructions below will allow you to clone and run the project on your machine, in addition to the recommended tests based on the main functions of the app.

### 📋 Prerequisites
- Java JDK 21
- Gradle (embedded by the wrapper present in the repository)
- IDE with Kotlin support (IntelliJ IDEA, Android Studio or similar)


## 🔧 Installation
Step-by-step: 

1. Clone the repository:
```
git clone https://github.com/dellatore/rede_social.git
```
```
cd rede_social
```
2. Open the project in your favorite IDE or directly via the command line:
```
./gradlew build
```
```
./gradlew run
```
3. Run in case there is a problem in gradle while uploading the app:
```
./gradlew clean
```
```
./gradlew build
```
```
./gradlew run (to start the application)
```
4. After running, access the application in the configured local environment (e.g., localhost:8080) — if this interface is implemented.

## ⚙️ Main Functions
1. Log in to the system;
2. Post to the system;
3. View posts;
4. Edit profile.

## 🔩 Suggested Tests
1. Try registering in the system and check if you can access the app;
2. Try posting in the system, simulating a publication on a real network, and check if the publication is successful;
3. Log in with another user and check if the posts you published appear for that user;
4. Edit your profile and see if your information has changed.

## 🛠️ Build with
Tools and technologies used in the project:

- Kotlin – main code language
- Gradle (Kotlin DSL) – build system
- Firestore (Database)
  
## 📌 Version
v1.0.0

##✒️ Authors
Dellatore – Main author of this project

## 📄 License
Apache 2.0
