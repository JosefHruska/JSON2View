# JSON2View

Simple android app working with JSON and Realm DB. Just for personal educational purpose. 



This app had to implement this conditions:

* Items in JSON are saved in local DB
* Scrollable list filled with items from DB
* Show simple detail when is item in list clicked
* Different layout for tablet (more than 7") and phone
* Show loading animation when is user on the end of list 
* Avoid obstacles connected to device-rotation
* Posibility to delete DB and reload list manually
* minSDK as low as possible


### Used libraries

List of libraries which i have used in this sample :
* [GSON] - JSON -> Realm DB
* [Realm DB] - use for DB
* [Prefs] - use for working with shared-prefs
* [Kotlin + Anko] - .... self-explained
* [Volley+] - use for get json from API 
* [EventBus] - use for comonucation between fragments
* [LoadingVew] - simple "material-like"loading animation

   [GSON]: <https://github.com/google/gson>
   [Realm DB]: <https://realm.io/>
   [Prefs]: <https://github.com/GrenderG/Prefs>
   [Kotlin + Anko]: <https://github.com/Kotlin/anko>
   [Volley+]: <https://github.com/DWorkS/VolleyPlus>
   [EventBus]: <https://github.com/greenrobot/EventBus>
   [LoadingVew]: <https://android-arsenal.com/details/1/3736>
   


