# SVN and Wiki Rules #

  1. When uploading to SVN, make sure that files are updated before committing
  1. Make sure files and folders match, don't dump everything into a random folder
  1. Make a log message when committing files, so people can follow what you are doing
  1. Only upload files to your own groups folder or the main folders, don't edit in other groups folders
  1. Update files a lot!


This is how you add a new page to the wiki:
Choosing the filename in the current format:
> x..x^n\_1x\_2x....`name`

The first x'es define what level you want the page to be, so if it's in General knowledge -> lulz -> Hello wiki page, then it would be 3 x'es.

If it's 3 steps in, there will be 2 parent folders, each split with a `'_'`, like so:

> xxx\_Generalxzknowledge\_lulz\_Hello wiki page.

For making spaces in the parent folders one should use `'xz'` instead, for making spaces in the name one should use `'_'`. So the name for the current folder would be:

> xxx\_Generalxzknowledge\_lulz\_Hello\_wiki\_page


When a page has been successfully created, the ToC will have to be update.
For this the user can use the WikiToCUpdater from downloads.
This will allow the ToC to be automatically created if the name of the wiki pages are correct.

To use the program the user needs to checkout https://sw6-2012.googlecode.com/svn/wiki/
The user needs to, after running the program on the wiki folder, commit the Table\_of\_contents.wiki file.
<a href='Hidden comment: 
Add more rules
'></a>