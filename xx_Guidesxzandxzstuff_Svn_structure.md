# Introduction #

The SVN should be split into a main code trunk, with the respective groups having their own folder for the current release of their code. The developer code of each group should be placed in brances.

Doing it this way will provide us with a better structure for finding the code we need. As well as each group can do a checkout to their own folder, and avoid downloading every other groups developer code, but just the released code.

Furthermore this will provide us with the opportunity of having released sets of code being available at all times, in a safe location.


## Details ##

A visual look of how the SVN should be structured:
  * Branches
    * Group 1
      * Developer code
    * ...
    * Group n
  * Tags
    * Big release 001
    * ...
    * Big release n
  * Trunk
    * Group 1
      * Released code
    * ...
    * Group n
  * Wiki
    * Wiki pages