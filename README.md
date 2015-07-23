pdj - java plugin interface to pure-data
========================================

RELEASE 0.8.7 / March 2010

PDJ enables you to write java code to interact with pure-data objects. The
API is totally based on Cycling74 Max/MSP 'mxj' object implementation. This 
will enable java mxj objects to run on pure-data with pdj. You can also 
create signal/tilde (~) objects.

This is a work in progress and some of features are missing and some might 
not even be ported.		

(download)[http://www.le-son666.com/software/pdj]

### IMPLEMENTED

	* MaxClock 100%
	* MSPBuffer 100%
	* Callback 100%
	* MaxObject 95%
	* Atom 98%
	* MaxSystem 90%
	* MaxQelem 90%
	* MSPObject 90% (missing signal in/outlet detection)
	* MSPPerformable 100%
	* MaxPatcher 5%

### LIMITATION

	* Signal inlets cannot be hot inlets for receiving atom messages.
	  This means that if you create a signal inlet and send an atom
	  to this inlet, pdj won't be able to process it; float or symbol.
	  This looks like a work as designed in pd.

### WORKAROUNDS

	* on most cases you don't have to put the JVM libs dir to the 
	  LD_LIBRARY_PATH. If you have a UnsatisfiedLinkError, add it 
	  before you start PD. 
	* on some machines, the System.out redirection can crash PD. If
	  it is the case, you can disable it in the pdj.properties file;
	  check the property: pdj.redirect-pdio.
	* you must use pdj pd scheduler if you need to use AWT with OS X. 
	  See below.

### REQUIEREMENTS
	
	* pure-data 0.40.x powerpc/intel/ub
	* java JDK version 1.4.x or better; use 1.5 if you can !
	* works on linux, windows and OS X (10.3 or better)
	
### REQUIEREMENTS FOR BUILDING


	* apache ant 1.6.x or better
	* comes with it's own version of cpptask that has been patched for 
	  linking with GCC/OS X with the -arch argument.
	* c compiler

INSTALLATION
------------

	* if you are using the source distribution, build it before
	  -> download java sdk from java.sun.com (unless you are using OS X)
	  -> edit file <your platform>-build.properties
	  -> in the root directory of pdj, run 'ant package'
	* the other files of the original binary directory must be in 
	  the same directory
	* double check dist/pdj.properties to be sure that the JAVA 
	  environment parameters are right
	* Use the corresponding build for MacOS 10.3 or 10.4 and greater

USAGE
-----

	* put your .java file in the /classes directory
	* create a pdj object with the name of the java class; if
	  you have not compiled it before, pdj will do it for you
	* you can test your Java code by using embedded Javascript 
	  object "jjs".

#### USING AWT WITH OS X


	Unlike Linux or Windows, you cannot just simply fire-up a AWT form on OS X. 
	This is because the event GUI mechanism has these limitation :
 	
	--> A CFRunLoopRun must be park in the main thread before the main loop is 
	    started
	--> Java must be run in a secondary thread.
 
	Since this prerequisite need a pure-data patch, we will write our own pd 
	scheduler. This scheduler will simple fire-up another thread that will 
	run the real pd scheduler (m_mainloop) and park the main thread with a 
	CFRunLoopRun.

	You must configure your pure-data environment to add the option : 
      -schedlib [fullpath of the pdj external without the extension]. 
    Use the menu Pd -> Preference -> Startup to do this. Don't forget to click 
    on [Save All Settings].

	Be careful when you configure this switch since it can crash PD on startup. 
	If you do have the problem; you will have to delete all pd-preferences by 
	deleting file: ~/Library/Preferences/org.puredata.pd.plist
	
	If the scheduler is loaded, you should have this pd message :
		'pdj: using pdj scheduler for Java AWT'

CHANGELOG
---------

	--- VERSION 0.8.7
	* Added preliminary support for JavaScript by using Rhino
	* FIXED: Number of arguments on class methods bug
	* FIXED: Parent methods are now mapped in a MaxObject

	--- VERSION 0.8.6
	* atoms of any size can now be sended via outlets
	* FIXED: MaxObject reflection on constructor with empty agruments.
	* Added support for dynamic typing of String and float upon calls. This means you 
	  can substitute symbol(Atom[]) for symbol(String,float) depending on the arguments.
	* Orignal PD patch for sched is removed from pdj distribtion since it's already
	  fixed in pure-data 0.42

	--- VERSION 0.8.5
	* added com.cycling74.net package
	* support for the pd-extended auto-build, slowly pdj will be part of pd-extended
	* can now be build with mingw on windows
	* FIXED: sending un-initialized array crash pure-data
	* FIXED: MaxObject reflection with java 1.5 caused an RMI/IIOP error
	* FIXED: Callback reflection with java 1.5 caused IllegalArgumentException
	* FIXED: OS X 10.3 support

	--- VERSION 0.8.4
	* amd64 for Linux (thanks to Sergio Torres-Perez)
	* getInlets() starts at 0 not 1 (thanks to MiS)
	* OS X gets pdj path by it's library path
	* OS X powerpc/intel distribution
	* added a user guide
	* FIXED: AWT usage on OS X

	--- VERSION 0.8.3
	* Atom.getInt() on a float now works.
	* corrected some classpath definition issues with windows
	* bypass the java compilation with pdj.compiler=null
	* remove dependencies task for cpptasks.jar (now part of distribution)
	* FIXED: comment on property pdj.vm_args failed to initialize VM

	--- VERSION 0.8.2
	* if javac is set, using the javac compiler from the JAVA_HOME first
	* optimization (main thread JVM and symbol method resolution)
	* FIXED: using pdj.classpath with directories
	* FIXED: search path on file in the current directory
	* FIXED: leak with open_path and casting warnings
	* build for Intel Mac
	* getting better at attribute support
	
THANKS
------

	* Loïc Reboursier
	* Sergio Torres-Perez
	* Thomas Grill
	* Michal Seta
	* patrick a 11h11
	* pd-mtl crew !

(c) Pascal Gauthier 2004-2010, under BSD like license
asb2m10@users.sourceforge.net
