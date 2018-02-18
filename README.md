# GeoBasedNotifications
Description<br /><br />

Application is quite simple and consists of one Activity with google map Fragment inside.<br />
There is one red marker (user location), which is moving when location changes.<br />
By long press on the map there appears a green marker, which is the center of a future circle. <br />
By clicking on green marker there appers a dialog with field to input a radius in meters.<br />
After accepting inputted radius there appears a circle.<br />
Leaving the circle or entering the circle user will receive the notification<br /><br />

Libraries<br /><br />

In this project I used next libraries:<br />
- "moxy" for implementing MVP architecture<br />
- "easypermissions" to avoid a lot of boilerplate code<br />
- "butterknife" for binding views & view groups<br />
Also I used Snackbar for showing tips for user and Floating action button for recentering user location marker.
