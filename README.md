android-demos
=============
This repository contains a set of 4 simple Android apps I developed demonstrating different capabilities and aspects of API. Designed for learning purposes for people who are starting with Android, even though slightly more complex than traditional tutorial apps.

Apps and the capabilities they demonstrate by order of complexity:

WidgetChanger (basic widgets)
-------------
A very simple app demonstrating the usage of buttons, actions and pictures.

FavouriteGames (containers, select, lists and files)
--------------
App allowing the user to create a list of people along with their age and
favourite games. Allows to store and load those lists from files on the device.

PhotoLocator (location, motion sensors and image capture)
------------
An application that, in response to being shaken, takes a picture 1 second
after the shaking stops, and also records the GPS location at the same time. Each location is stored in a growing list, when the user touches the list item, the app displays the picture taken at that location. Detects proper shaking as opposed to any movements of the phone and uses a trick to take a picture without showing the preview to the user.

Note: A physical device is needed for this app as opposed to the emulator.

PeopleSearcher (threads and databases)
--------------
App that creates a small SQL database populated from a file containing a list of names (one per line) located at a specified URL on the Internet. It then searches for those names on Google and displays the results. The application does two different pieces of work (populating the database and searching) by spawning two separate threads from the main UI activity.
