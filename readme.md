# Virginia SOL Simulator (VSS)

VSS is a multi-module project designed to simulate the Virginia Department of Education (DOE) and Pearson Labs end of course (EOC) online testing for students, while providing educators with the tools to create modern exams and easily compile statistics on student performance.

## Educators and Students

The project is still in alpha development. This means that the project has established development requirements and has begun to accumulate a code base. Although the project is far enough along to have working prototypes, it is not yet ready for public release.

## Developers

The project can always use some additional help! Start by looking at the issues on GitHub. Once you've found something interesting you'd like to work on, fork the repository and start coding!

The project is currently broken into three modules: vss-common, vss-studentclient, and vss-teacherclient.

### vss-common

This module contains all the code common to both sides of the project. It includes the exam structure classes, exam import/export tools, JavaFX helpers, and other generic utils. This module is a dependency of all other modules.

### vss-studentclient

This module contains the student user interface. The actual emulation of the EOC exams is taken care of by this module. Additionally, it collects performance data that can later be compiled into a class statistic.

### vss-teacherclient

This module provides a powerful user interface and statistics suite to educators. It allows them to create the exams emulated by the student client, and then retrieve and analyze performance statistics to instantly tailor the learning environment.

### Building

This project uses gradle as a cross-platform, ide-agnostic build system. For the following sections `<module>` will refer to the module you are working on. For example `vss-studentclient`. To run these commands, use a terminal (cmd for Windows) to navigate to your git repository. Ensure you have gradle installed.

The following steps list the commands (in order) necessary to setup, contribute to, test, and deploy the project.

1) Generate IDE Files

```
Eclipse:
gradle eclipse
Idea:
gradle idea
```

2) Compile Maven Dependencies

```
gradle buildDependencies
```

3) Initial Assembly

```
gradle <module>:assemble
```

4) Edit Some Code

5) Test Changes

```
gradle <module>:run
```

6) Create a Runnable Jar
```
gradle <module>:fatJar
```

7) Deploy Binaries
```
gradle <module>:jfxDeploy
```

## Licensing

This project is licensed under the GNU General Public License v3.0. This ensures everyone will have access to this project, and will ensure all contributions remain public.

Developers should ensure any libraries they add are compatible and not in violation of GPLv3.0.

If anyone is aware of an issue with this project's compliance with GPLv3.0, he or she should open an issue on GitHub so we can resolve it as soon as possible.
