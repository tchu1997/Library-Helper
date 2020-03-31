# 151Project
What's currently working:
1. A database to store book information with operations - Tuong Chu
2. "Add Book" GUI: successfully added books into database - Tuong Chu
3. "Book List" GUI: successfully displayed book list as a table - Tuong CHu
4. "Add Member" GUI: successfully added new members into database - Duy Le
5. "List Member" GUI: successfully view list of members - Duy Le
6. "Main Window" GUI: currently working on - Duy Le
7. "Book Issue" and "Renew/Submission" - Duy Le
8. "Delete mutiple books" - Duy Le
9. "Delete multiple members" - Duy Le
10. "New submit/renew page" - Duy Le
11. "Multiple cart removal" - Duy Le
12. "Multiple submit/renew" - Duy Le
13. "Wrap up codes into new UI" - Duy Le
14. "Documentation and submit" - Duy Le

<<<<<<< HEAD
x. " Make Executable (exe)": successfully created LibraryHelper\dist and runable - Duy Tran
x. "Create Windows Icon" and "Logging Improvement - 'Enter' to log in" - Duy Tran (Try to add it into other field later)
x. "Delete Book from the bookList" - Duy Tran (Try to do the samething for add book, remove member later)
x. "Edit Book" - Duy Tran
x. "Edit Member" - Duy Tran

=======
x. " Make Executable (exe)": successfully created LibraryHelper\dist and runable - Duy Tran   
x. "Create Windows Icon" and "Logging Improvement - 'Enter' to log in" - Duy Tran (Try to add it into other field later)  
x. "Delete Book from the bookList" - Duy Tran (Try to do the samething for add book, remove member later)  
x. "Edit Book" - Duy Tran  
x. "Edit Member" - Duy Tran
>>>>>>> 445555ba53e4f9476d791a9c10fbc5486ec924ad


New updates:
11/29 10:23pm Tuong Chu

- DatabaseHandler.java: remove unecessary functions, add warning_alert() and information_alert()
- ui.addmember: add isMemberInfoValid(), isMemberAlreadyAdded(), limit Member ID to 7 characters (lol), improve gui
- ui.booklist: add isBookInfoValid(), isBookAlreadyAdded()
- ui.main: add MainWindowLoader()


Stupid Question?:  
Book_listController line 54: commented for what?  
    //ObservableList<Book> list = FXCollections.observableArrayList();  
	Created in #5 11:21 https://youtu.be/l5_zfkh5vi8?t=680  
	replaced by tableView.getItems() ??? why?  
	tableView.getItems().setAll(list); removed??  
uncomment "list" and the code due to conflict in later method (deleteBookSelection in BookListController line 115)  
	(#24 17:00 https://youtu.be/iWeCDU1uNPo?t=1020)

<<<<<<< HEAD
Ask for help:
Add userName && password character limit ( less than 16 and "maybe" more than 4) in SettingButtonController

Later work:
Add "add" option for book list and member list
=======
Later work:  
Add password character limit
>>>>>>> 445555ba53e4f9476d791a9c10fbc5486ec924ad


Note:
- ALWAYS GIT PULL BEFORE MAKING CHANGES TO THE PROJECT
- If there is merging error after git pull: please run git stash, then git pull 
- "Database connection failed" re-open the NetBean (idk how it's failed T.T, sometimes it's failed. If it's failed, it mean the modification has something wrong
							trace back to the original and the modify the code again one by one to see the error, mostly logical)
							
							
-----------------------------------------------
Update 12/4 4:13am Tuong
- Add new UI in library.helper.ui.main
- Book Issue: everything works. Admin is able to issue multiple books to a member. Error messages are properly displayed when necessary.
- Books: update book table ech time a book is ussed (change from Available to Unavailable)


WHAT'S NOT DONE IN THE NEW UI:
- RENEW/SUBMISSION
- SETTINGS
- ABILITY TO EDIT BOOKS, REMOVE BOOKS
--> DONE BY Duy Le
