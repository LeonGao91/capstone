Welcome
-------

If you downloaded the codes in Zip, please first unzip it and you can find the folder structure described as below.

**Step 1**: Download the zip file or clone the repository.

    git clone https://github.com/LeonGao91/capstone.git



Directory layout
-----------------

## intel/*

The main web application is located in `intel` directory.

	cd capstone

The application is built in Netbeans enviroment. Therefore, to open the project, simply import this project in the latest Netbeans. Also notice that your Netbeans should have installed Glassfish by default. After you have this project in IDE, simply right click the project and RUN it. Them Netbeans will lead you to the homepage.
	
### dist

This folder has the .war package which you can deploy on a Glassfish server.

### src

This folder contains all Java codes. Including Servlets code.

### web

This folder contains all frontend resources. Including all jsp, javascript and css codes and libraries. Also the media resources is in this folder.

### Other directories and files

  * build/*
  * nbproject
  * built.xml
  * catalog.xml

These are Netbeans project files. Normally there is no need to modify them.


