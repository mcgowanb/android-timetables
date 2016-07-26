# Timetables app in Android for IT Sligo

This app is a personal project and was created to 'appify' the timetable schedule that students have in college. This work is my own and is not affiliated with [IT Sligo](https://itsligo.ie/) in any way

This app creates a POST request with a student ID to the college [website](https://itsligo.ie/student-hub/my-timetable/) and scrapes the resultant html. This result is parsed and displayed to user in the app.
As there are blackspots in the college for internet access and not everyone always has internet connectivity when you need it, data is persisted to local SQLite storage for offline access.
Since timetables are subject to change on a regular basis a Sync Adapter has been scheduled to run every 24 hours to update the local database.

Data displyed on this app is only as accurate as the information presented on the IT sligo website.
