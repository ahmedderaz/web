## Guide Lines #
+ Use the following **prefix** before any added commit. <br>
+ Add **Jira** task followed be the commit message if it related to Jira.
+ When taking a release separate it from the **current** development and add the date and name of the release,<br>
Then change the name.
> \!! fix issue <br> 
> \+ add feature <br>
> ~ refactor <br/>

+ Example: <br>
~ [AVL-1157](http://jira.afaqy.sa/browse/AVL-1157) refactor receiver logger <br>
***
## Change log
~ Release a new build <br>
## _2020/4/25_ : ## Release v2.9,1 <br>
~ [AVL-4360](http://jira.afaqy.sa/browse/AVL-4360) Move WebNotifier Microservice to new Project Structure
## Release v2.8.9 <br>
!! fix web notifier performance issue
## _2020/1/15_ : Release v2.8.8 <br>
\+ [AVL-](http://jira.afaqy.sa/browse/) Fix consuming from multi topics issue <br>
\+ [AVL-3492](http://jira.afaqy.sa/browse/AVL-3492) Make web notifier use SSL protocol <br>
\+ [AVL-1594](http://jira.afaqy.sa/browse/AVL-1594) Split web notifier in a separated micro-service <br>
## _2019/5/8_ : Release v1.7.8 <br>
\+ [AVL-2032](http://jira.afaqy.sa/browse/AVL-2032) Decode IOTM Protocol <br>
\+ [AVL-1847](http://jira.afaqy.sa/browse/AVL-1847) Enable/disable zeros location filter. <br>
~ [AVL-1906](http://jira.afaqy.sa/browse/AVL-1906) BCE remove calibrations and change parameters names. <br>
!! [AVL-1561](http://jira.afaqy.sa/browse/AVL-1561) Overspeed/Underspeed event with zones and time. <br>
!! [AVL-1770](http://jira.afaqy.sa/browse/AVL-1770) Prevent admin to stop cmd protocol. <br>
!! [AVL-1921](http://jira.afaqy.sa/browse/AVL-1921) Fix logs names. <br>
## _2019/3/3_ : Release v1.7.7 <br>
!! [AVL-1803](http://jira.afaqy.sa/browse/AVL-1803) Fix unit's group assignation to sub user issue. <br>
!! [AVL-1750](http://jira.afaqy.sa/browse/AVL-1750) Fix unit assignation sub user issue. <br>
\+ [AVL-1634](http://jira.afaqy.sa/browse/AVL-1634) Decode BCE protocol's masks. <br>
!! [AVL-1619](http://jira.afaqy.sa/browse/AVL-1619) Fix adding new unit issue. <br>
\+ [AVL-1618](http://jira.afaqy.sa/browse/AVL-1618) Creating indexes on documents automatically. <br>
\+ [AVL-1526](http://jira.afaqy.sa/browse/AVL-1526) Deploy Geocoder microservice. <br>
!! [AVL-1478](http://jira.afaqy.sa/browse/AVL-1478) Fix fuel consumption calculation. <br>
## _2019/01_ : Release v1.0 <br>
\+ [AVL-1485](http://jira.afaqy.sa/browse/AVL-1485) Save old signals in Delayed_Data collection. <br>
\+ [AVL-1484](http://jira.afaqy.sa/browse/AVL-1484) Add unitId to GPRS Command. <br>
\+ [AVL-1483](http://jira.afaqy.sa/browse/AVL-1483) Make sure that events signals is not older than 15 minutes.<br>
~ [AVL-1482](http://jira.afaqy.sa/browse/AVL-1482) Change date to be in milliseconds instead of strings in socket events.<br>
\+ [AVL-1480](http://jira.afaqy.sa/browse/AVL-1480) Add kafka commands and configs. <br>
\+ [AVL-1479](http://jira.afaqy.sa/browse/AVL-1479) Add "zones" field to event status<br>
!! [AVL-1352](http://jira.afaqy.sa/browse/AVL-1352) Fix events notification issue.<br> 
!! [AVL-1355](http://jira.afaqy.sa/browse/AVL-1355) Fix email and sms notification issue.<br>
!! [AVL-1360](http://jira.afaqy.sa/browse/AVL-1360) Fix callback issue.<br>
~ [AVL-1335](http://jira.afaqy.sa/browse/AVL-1335) Separate log4j config for each environment.<br>
\+ [AVL-1242](http://jira.afaqy.sa/browse/AVL-1242) Add change log <br>
~ [AVL-1240](http://jira.afaqy.sa/browse/AVL-1240) Change AtomicLong used in counters to be AtomicBigDecimal.<br>
\+ [AVL-1238](http://jira.afaqy.sa/browse/AVL-1238) Create CMD consumer for Events Service.<br>
!! [AVL-1237](http://jira.afaqy.sa/browse/AVL-1237) Fix GPRS issue "sent current device status, send GPRS".<br>
\+ [AVL-1236](http://jira.afaqy.sa/browse/AVL-1236) Add unit group structure to events.<br>
~ [AVL-1157](http://jira.afaqy.sa/browse/AVL-1157) Refactor receiver logger.<br>
!! [AVL-928](http://jira.afaqy.sa/browse/AVL-928) Send live update of subUser's unitGroup.<br>
\+ [AVL-383](http://jira.afaqy.sa/browse/AVL-383) Save parameters change date.<br>
