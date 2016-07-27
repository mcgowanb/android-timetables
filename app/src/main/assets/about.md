#About me
My name is Brian and I am a second year BSC(Hons) student in computing (SG246). I have always had a keen interest in software and software
development.
I decided to create this app for a couple of reasons:
- Learning experience - I wanted this to be a learning experience for myself in android development. I've created a small app in it
before, but I was looking for something more in depth to work on
- Fill a void. In the college currently, the mobile version of the timetables works, but you need an active internet connection every 
time you load the page as it re-creates the POST request. Since there are a couple of black spots in the college for internet access
and not everyone has internet access all of the time, I found that I was without my timetable generally when I needed it. Printing it does no good since its
 subject to change without notice. The app, in addition to having a refresh button to manually load new data will sync with the website evey 
 24 hours, so it should always be up to date.
-  Sharing code. I believe in the open source movement, and all of the code is available for this app on github (details below) I would encourage
anyone who wants to, to create pull requests to improve the project and submit changes. Alternatively if anyone has an interest in porting this 
over to IOS, please use this code to do so.

#App Details
The app is split into two separate components.
##Scraper 
The scraper creates the POST request for the website with your student id via twitter4j, scrapes
the resultant html from the response into a custom Timetable java object split by day class and so on. This is returned as an object to the
requester.

##Android app
The android app takes a packaged jar file of the scraper and uses it in sync adapter to connect to the website and generate the request
When the response is received, it is persisted to a local sqlite database so its always accessible. Through the use of a few views within
android it renders information to the user.




This app is and is likely to be for a considerable amount of time a work in progress. I aim to constantly improve it and make changes to it, in addition
to leaving it open to others to make changes to it. Due to the nature of the project, it can only be as accurate as the website itself and so its not guaranteed to be
100% accurate all of the time. Oh, and this is not affiliated with the institute in any way, and is completely unofficial

Please feel free to contact me if you have any suggestions, or if you spot any bugs with the project.

Enjoy....


#My Details
Contact me: mcgowan.b@gmail.com

[Scraper Github:](https://github.com/mcgowanb/itsligo-timetables-scraper)

[Android Github](https://github.com/mcgowanb/itsligo-timetables-android)

[Blog](https://bmcgowanblog.wordpress.com/)
