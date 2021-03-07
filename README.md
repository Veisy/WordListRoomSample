# WordListRoomSample
A sample app that implements recommended architecture using the Android Architecture Components.
Repeating input is not added to list. Also sorts entries alphabetically.
  

Includes: 
Room, LiveData, ViewModel (Android Architecture Components)
  
Kotlin Coroutines,
View Binding
  

## Updates  
  ### 21.02.2021  
  Dependency Injection with Hilt is added.
  The project is refactored to keep up with this chance. 
  Accordingly ViewModel.Factory class is deleted, because with Hilt we do not need to create costum factory classes for ViewModels.  
    
  ### 21.02.2021
  UI Update  
  Delete option with UNDO is added.
  Update option with UNDO is added.  
    
  ### 20.02.2021
  Search Feature has been added. The search made in the database is dynamically displayed in the RecyclerView.
   
![Screenshot_20210221-120527_WordListRoomSample](https://user-images.githubusercontent.com/43733328/108620450-5b636600-743d-11eb-9f6f-5ce0d60454d8.jpg)




