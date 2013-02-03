# Gamedev-Server
A game server, which is developed in Java. It's capable of bringing multiple clients together. The aim was to be generic, which means that the server can host any type of game. If you need game logic on the server, there's the possibility to write a plugin.

## Settings
You can change the server settings with the constants in the class `edu.hm.gamedev.server.settings.Settings`. The server works out of the box, but if you want to use the account verification and password reset feature, you have to enter the credentials for an email account.

## Compiling
Project is built with Maven. You can start a tomcat server with `mvn tomcat7:run`.

## License
Licensed under the [GNU Lesser General Public License 3.0](http://www.gnu.org/licenses/lgpl-3.0.html).