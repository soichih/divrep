divrep
======

* DivRep Ajax Library for Java

DivRep library is a simple Java servlet based AJAX library that allows you to create interactive web applications without having to write Javascript. DivRep focuses on users who prefer traditional JSP like interface with only certain portions of a page to be AJAX-enabled without having to learn a large web framework or letting such framework completely take over the entire interface.


DevRep allows you to create both server and the client code in plain Java and without a special recompilation step of the Java code to Javascript. DivRep application simply forward all events that are caused by user on a browser to DivRep and it will then dispatch events, and return updated content to the browser thus creating an effect that user is interacting with a AJAX based application.


This mechanism allows you to debug, or profile client side code just as you would with plain Java GUI applications without having to learn complex web framework. It also allows highly stable interface that works across many web browsers especially the older ones. Developing application with DivRep will be similar to developing a native GUI application, but instead of drawing to a native windowing system, the DivRep application will render updated HTML to a target div section on the browser.


Applications built with DivRep will be inherently more secure since user can not invoke functionality via URLs like a normal web applications. This also reduces the risk of cross-site-scripting attack, and eliminate a need to synchronize between client and server by passing the form element IDs back and forth.


DivRep handles View and Controller part of what is traditionally known as MVC framework. Unlike many other AJAX web frameworks DivRep's goal is to let user who are already familiar with their web application to keep as much of the code as possible while allowing them to add AJAX-enabled section of the page that are usually hard to implement using more traditional tools.

Soichi
