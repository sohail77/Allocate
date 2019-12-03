# Allocate

## Completeness
This is a fully functional prototype. Currently there are no bugs in the app. We have accomplished all the features that we initially planned for. This application uses MySQL as a backend database. 
The interesting part about our project is that we have created an algorithm that calulates the hospital with the least amount of waiting time. All the calculations happen on the backend and this app uses REST apis to interact with the backend.

**Intructions to run**

* There is a .apk file provided in the root directory of this repository. The markers can either install this apk on their      Android phone or build the project using Android Studio to run the app. 
* After opening the application for the first time you'll be welcomed with a login screen. Please use the following credentials to login (**Username:** "ambulance1", **Password:** "Ambulance").
* In the start transfer screen you can get a transfer ID from the Web app of our project or you can try the following transfer ID (**5022**).

## Code Quality
This application is built using Kotlin programming language. It is built using MVVM (Model view View Model) architecture. The decision to use MVVM architure was made because of the following reasons.
* It is the standards that Google is currently recommending.
* It seperates the UI logic of the application from the data processing logic.
* This seperation of concerns allows us to test the data processing code independently. 

Apart from the MVVM architecture the code uses the new concept of Data Binding in Android which allows the xml code to be directly connected to the Kotlin code without defining any "findViewByIds". This app also uses the new Navigation component and is bulit using the "Single Activity" style coding as recommended by Google. Finally, the code contains small re-usable self explanatory and modular functions. 

## User Experience
This application was designed to be as minimalistics and simplistics as possible without comprimising on the functionality. This app has a pretty standard on-boarding. the main functions that'll be performed by the paramedics while using the app are atmost 2 clicks away. 

## Scalability
This application was designed with scalability in mind. Since the calculations related to waiting times are all performed on the backend and all the network calls made in the app are made on the background thread to avoid UI blockage, this app will perform perfectly for any number of hospitals. 

The main concern that we currently have in-relation to scalability is, due to the time constraint we coudln't implement a backup server but in future if we decided to implement a back up server, we should be able to accomplish it by modifying the endpoints. 

## Security
We were focused on maintainig good security standards while developing this application. This app doesn't require any api keys for any specific services. The application doesn't access any "extra" sensor permissions from the user.

## Analytics
We currently are not recording any analytics from the user. This is one of the furture scope of our application. In future we are still on-the-fence, as to whether we should use Firebase analytics or Google Analystics sdk for Android? Up until now we relied on the Questionaire to make changes in the application but Analystics will be an important addition and a good in-sight provider for us in the future. 

