# TV-program
Android application for TV channels show programs
To write an Android application for TV channels show programs.

Use as a data source:
http://52.50.138.211:8080/ChanelAPI/chanels - channel list
http://52.50.138.211:8080/ChanelAPI/categories - category list
http://52.50.138.211:8080/ChanelAPI/programs/<timestamp> - list of programs per day

Data are loaded asynchronously and are written in a local SQL database and are shown to a user from there. 
Firstly, you should download program schedule for today, then for a week/month. * Optionally. Automatically synchronize data for today several times a day. DONE

* Optionally. Manual database synchronization. DONE
* Optionally. Automatically synchronize data for today several times a day. DONE
* Optionally Show a progress of download/synchronization. DONE
* Optionally Show Notification while loading. DONE
* Optionally Use ContentProvider for work with a database. DONE

 On the main screen of the application is located TabLayout with channel titles and ViewPager with programs for these channels.
* Optionally. Automatically synchronize data for today several times a day. DONE
 The program is displayed for today by default. 
* Optionally ability to select a date for which a TV schedule is shown. DONE
* Optionally select channels which are preferred. DONE
* Optionally sort the list of channels on the main screen. 

* Optionally. Automatically synchronize data for today several times a day. DONE
Menu includes the following items: 
•	list of categories;
•	list of channels;
•	list of preferred channels;
•	program schedule. (Main screen)
* Optionally use NavigationDrawer DONE

The list of categories is shown on the screen with categories. When user click on some category, the list of channels of this category is displayed. When user sees the list of channels he can click on the item and by this action add or remove single item to the list of preferred channels.
For a list of all the channels and preferred ones, actions are the same.

The result of the work is as follows:
•	link to GitHub repository with a project.
•	Readme file to the repository with the description of fulfilled and unfulfilled tasks.
•	link to a APK file (google drive)
•	

