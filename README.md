# Gimprove - App
A project where my colleagues and I digitized fitness machines. Unfortunately we had to
stop the project for personal and business reasons. Landing Page is still available here:
[Gimprove-Website](www.gimprove.com).

## Project Overview
Gimprove is a lightweight system built to digitalize fitness equipment. Once installed on regular machines, Gimprove
allows users to automatically track all relevant keyfigures of their activities such as the number of repetitions
or the weight used. Users get feedback in realtime and can analyze their progress in the Gimprove App. For more
information about Gimprove, visit the [Gimprove-Website](www.gimprove.com).

Here's an overview over the Gimprove system and it's components:
![Overview over the single components of the Gimprove System](photos/ReadMe/GimproveSystem.png)

There are three respositories for this project:
1) [Gimprove Backend](https://github.com/denmei/gimprove-backend):
Gimprove Plattform hosting the Gimprove Website and providing the Gimprove-API.

2) [Gimprove-App](https://github.com/denmei/gimprove-app): User Interface.

3) [Gimprove-Client](https://github.com/denmei/gimprove-client): Client that is attached on the machines.

## Repository Overview
This repository contains the code for the Gimprove-App. After successful authentication, the app
gives users the possibility to track their fitness activities in real-time. This is done by using the
Gimprove-Backend as a broker, forwarding incoming messages from the gimprove upgrade components 
(client) to the right user via websockets.
Users can explore their historical activities and their progress in the app, too.

### Build With
The Gimprove-backend is based on the Django-Framework. To realize the required services, 3rd party packages were used:

* [Django-Rest-Framework](http://www.django-rest-framework.org/) for the API used by our App and the clients

* [Django-Channels](https://github.com/django/channels) Websocket-Communication for live tracking between the client
and the App to achieve real-time communication.


### Contributing
**Denni Meisner:** meisnerdennis@web.de

### Team Members
* **Lennard Rügauf:** l.ruegauf@gmx.de (Business, Hardware)
* **Matthias Schuhbauer:** matthias_schuhbauer@web.de (Hardware)
* **Dennis Meisner:** meisnerdennis@web.de (Business, Software)
