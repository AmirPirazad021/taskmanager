This is a TaskManager application designed to help you efficiently manage your tasks. The app allows you to create tasks by adding a title, description, and setting a reminder date and time.

Key Features:

Architecture : using MVVM
Data Fetching: The app fetches data from the server using Retrofit and OkHttp.
Local Storage: Room is used as the local database for storing tasks.
Data Persistence: DataStore is utilized to manage and save data fetched from the server when the app is launched for the first time.
Reminders: AlarmManager is employed to set reminders, which trigger notifications and play a sound.
User Interface: The UI is designed using XML.
