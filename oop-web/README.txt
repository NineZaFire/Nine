NetBeans OOP Tools Project (Maven)
=================================

This is a Maven-based Java Swing sample application implementing:
- OOP principles: Constructor, Encapsulation, Composition, Polymorphism, Abstract, Inheritance
- SQLite persistence via org.xerial:sqlite-jdbc
- Tools: Convert, Code Transform, History (shows DB history)
- Runnable JAR produced by `mvn package` (jar-with-dependencies)

How to open in NetBeans 26:
1. File -> Open Project -> select this folder (it is a Maven project).
2. NetBeans will download dependencies automatically.
3. Right-click project -> Run.
4. To build runnable JAR: `mvn package` in project root. Result JAR in target/ named oop-tools-1.0.0-jar-with-dependencies.jar

Notes:
- Requires Java 17+.
- Database file (appdata.db) will be created in user home directory (by default) or in working dir.
